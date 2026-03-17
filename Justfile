set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

default:
	@just --list

new name:
	bash ./scripts/new-project.sh "{{name}}"

new-bagel name:
	bash ./scripts/new-project.sh bagel "{{name}}"
