{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    systems.url = "systems";
  };

  outputs =
    {
      self,
      nixpkgs,
      systems,
    }:
    let
      forEachSystem = nixpkgs.lib.genAttrs (import systems);
      projectManifest = builtins.fromJSON (builtins.readFile ./manifest.json);
    in
    {
      overlays.default = final: prev: { };

      packages = forEachSystem (
        system:
        let
          pkgs = import nixpkgs {
            inherit system;
            overlays = [ self.overlays.default ];
          };
          lib = pkgs.lib;
          projects = map (project: {
            name = project.name;
            version = project.version;
            mvnHash = project.mvnHash or lib.fakeHash;
          }) projectManifest;
          projectPackages = lib.listToAttrs (
            map (
              project:
              let
                pname = project.name;
                version = project.version;
              in
              {
                name = pname;
                value = pkgs.maven.buildMavenPackage {
                  inherit pname version;
                  src = ./projects + "/${pname}";
                  mvnHash = project.mvnHash;
                  doCheck = false;
                  nativeBuildInputs = [ pkgs.makeWrapper ];
                  meta.mainProgram = pname;
                };
              }
            ) projects
          );
          helperPackages = {
            new-project = pkgs.writeShellApplication {
              name = "new-project";
              runtimeInputs = [
                pkgs.git
                pkgs.jdk21
                pkgs.jq
                pkgs.maven
              ];
              text = ''
                if [ -z "$1" ]; then
                	printf "Error: No project name provided.\n" >&2
                	printf "Usage: %s <project-name>\n" "$0" >&2
                	exit 1
                fi

                ROOT_DIRECTORY="$(git rev-parse --show-toplevel)"
                PROJECTS_DIRECTORY="$ROOT_DIRECTORY/projects"
                DIRECTORY="$PROJECTS_DIRECTORY/$1"
                MANIFEST_PATH="$ROOT_DIRECTORY/manifest.json"

                if [ -e "$DIRECTORY" ]; then
                	printf "Error: Project '%s' already exists.\n" "$1" >&2
                	exit 1
                fi

                mvn --batch-mode archetype:generate \
                	-DarchetypeGroupId=org.apache.maven.archetypes \
                	-DarchetypeArtifactId=maven-archetype-quickstart \
                	-DarchetypeVersion=1.5 \
                	-DgroupId=org.example \
                	-DartifactId="$1" \
                	-Dversion=1.0-SNAPSHOT \
                	-Dpackage="org.example.$1" \
                	-DinteractiveMode=false \
                	-DoutputDirectory="$PROJECTS_DIRECTORY"

                printf '%s\n' \
                	"set shell := [\"bash\", \"-eu\", \"-o\", \"pipefail\", \"-c\"]" \
                	"" \
                	"default:" \
                	"    @just --list" \
                	"" \
                  "run:" \
                  "    mvn compile exec:java -Dexec.mainClass=\"org.example.$1.App\"" \
                  "" \
                  "test:" \
                  "    mvn test" \
                  > "$DIRECTORY/Justfile"

                TEMP_MANIFEST="$(mktemp)"

                jq \
                	--arg name "$1" \
                	--arg version "1.0-SNAPSHOT" \
                	--arg mvnHash "${lib.fakeHash}" \
                	'
                		map(
                		if type == "string" then
                			{
                				name: .,
                				version: $version,
                				mvnHash: $mvnHash,
                			}
                		else
                			.
                			+ {
                				version: (.version // $version),
                				mvnHash: (.mvnHash // $mvnHash),
                			}
                			end
                			)
                			| if any(.name == $name) then
                			.
                		else
                			. + [
                				{
                					name: $name,
                					version: $version,
                					mvnHash: $mvnHash,
                				}
                		]
                end
                ' "$MANIFEST_PATH" > "$TEMP_MANIFEST"

                mv "$TEMP_MANIFEST" "$MANIFEST_PATH"

                git add "$DIRECTORY" "$MANIFEST_PATH"
              '';
            };
          };
          defaultProject = if projects == [ ] then null else (builtins.head projects).name;
        in
        projectPackages
        // helperPackages
        // {
          default =
            if defaultProject == null then helperPackages.new-project else projectPackages.${defaultProject};
        }
      );

      devShells = forEachSystem (
        system:
        let
          pkgs = import nixpkgs {
            inherit system;
            overlays = [ self.overlays.default ];
          };
        in
        {
          default = pkgs.mkShell {
            packages = [
              self.packages.${system}.new-project
              pkgs.jdk21
              pkgs.just
              pkgs.jq
              pkgs.maven
            ];
            buildInputs = [ pkgs.maven ];
          };
        }
      );
    };
}
