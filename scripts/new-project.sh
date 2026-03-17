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
template_directory="$root_directory/templates/maven-project"
package_segment="$(printf '%s' "$name" | tr '[:upper:]' '[:lower:]' | tr '-' '_' | tr -cd '[:alnum:]_')"

if [ -z "$package_segment" ]; then
	printf "Error: Project '%s' does not produce a valid Java package segment.\n" "$name" >&2
	exit 1
fi

case "$package_segment" in
[0-9]*)
	package_segment="project_$package_segment"
	;;
esac

package_name="org.example.$package_segment"

if [ -e "$directory" ]; then
	printf "Error: Project '%s' already exists.\n" "$name" >&2
	exit 1
fi

cookiecutter \
	--no-input \
	--output-dir "$projects_directory" \
	"$template_directory" \
	project_name="$name" \
	artifact_id="$name" \
	group_id="org.example" \
	version="1.0-SNAPSHOT" \
	package_name="$package_name" \
	main_class="App"

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
