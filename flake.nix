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
        in
        projectPackages
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
