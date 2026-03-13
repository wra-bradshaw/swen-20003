set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

default:
	@just --list

new name:
	new-project "{{name}}"
