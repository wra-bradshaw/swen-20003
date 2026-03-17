set -eu -o pipefail

usage() {
	printf "Usage: %s [template] <project-name>\n" "$0" >&2
	printf "Templates: maven, bagel\n" >&2
}

case "$#" in
0)
	printf "Error: No project name provided.\n" >&2
	usage
	exit 1
	;;
1)
	template_name="maven"
	name="$1"
	;;
2)
	template_name="$1"
	name="$2"
	;;
*)
	printf "Error: Too many arguments provided.\n" >&2
	usage
	exit 1
	;;
esac

root_directory="$(git rev-parse --show-toplevel)"
projects_directory="$root_directory/projects"
directory="$projects_directory/$name"
manifest_path="$root_directory/manifest.json"

case "$template_name" in
maven | maven-project)
	template_directory="$root_directory/templates/maven-project"
	;;
bagel | bagel-project)
	template_directory="$root_directory/templates/bagel-project"
	;;
*)
	printf "Error: Unknown template '%s'.\n" "$template_name" >&2
	usage
	exit 1
	;;
esac

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
