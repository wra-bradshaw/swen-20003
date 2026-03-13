set -eu -o pipefail

if [ "$#" -eq 0 ]; then
	printf "Error: No project name provided.\n" >&2
	printf "Usage: %s <project-name>\n" "$0" >&2
	exit 1
fi

name="$1"
root_directory="$(git rev-parse --show-toplevel)"
projects_directory="$root_directory/projects"
directory="$projects_directory/$name"
manifest_path="$root_directory/manifest.json"

if [ -e "$directory" ]; then
	printf "Error: Project '%s' already exists.\n" "$name" >&2
	exit 1
fi

mvn --batch-mode archetype:generate \
	-DarchetypeGroupId=org.apache.maven.archetypes \
	-DarchetypeArtifactId=maven-archetype-quickstart \
	-DarchetypeVersion=1.5 \
	-DgroupId=org.example \
	-DartifactId="$name" \
	-Dversion=1.0-SNAPSHOT \
	-Dpackage="org.example.$name" \
	-DinteractiveMode=false \
	-DoutputDirectory="$projects_directory"

printf '%s\n' \
	"set shell := [\"bash\", \"-eu\", \"-o\", \"pipefail\", \"-c\"]" \
	"" \
	"default:" \
	"    @just --list" \
	"" \
	"run:" \
	"    mvn compile exec:java -Dexec.mainClass=\"org.example.$name.App\"" \
	"" \
	"test:" \
	"    mvn test" \
	>"$directory/Justfile"

temp_manifest="$(mktemp)"

jq \
	--arg name "$name" \
	--arg version "1.0-SNAPSHOT" \
	--arg mvnHash "sha256-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=" \
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
	' "$manifest_path" >"$temp_manifest"

mv "$temp_manifest" "$manifest_path"

git add "$directory" "$manifest_path"
