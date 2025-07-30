# https://just.systems/man/en/

[private]
@default:
	just --list --unsorted

# Run linter.
@lint:
	docker run --rm --read-only --volume=$(pwd):$(pwd):ro --workdir=$(pwd) kokuwaio/hadolint
	docker run --rm --read-only --volume=$(pwd):$(pwd):ro --workdir=$(pwd) kokuwaio/yamllint
	docker run --rm --read-only --volume=$(pwd):$(pwd):rw --workdir=$(pwd) kokuwaio/markdownlint --fix
	docker run --rm --read-only --volume=$(pwd):$(pwd):ro --workdir=$(pwd) kokuwaio/renovate-config-validator
	docker run --rm --read-only --volume=$(pwd):$(pwd):ro --workdir=$(pwd) woodpeckerci/woodpecker-cli lint

# Build image with local docker daemon.
@build TARGET="debian":
	docker build . --target={{TARGET}} --tag=kokuwaio/keycloak:dev-{{TARGET}}

# Build all variants
@build-all:
	docker buildx build . --target=debian     --platform=amd64
	docker buildx build . --target=debian     --platform=arm64
	docker buildx build . --target=temurin    --platform=amd64
	docker buildx build . --target=temurin    --platform=arm64
	docker buildx build . --target=distroless --platform=amd64
	docker buildx build . --target=themes     --platform=amd64
	docker buildx build . --target=themes     --platform=arm64

# Inspect image layers with `dive`.
@dive TARGET="debian":
	dive build . --target={{TARGET}}
