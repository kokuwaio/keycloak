# https://just.systems/man/en/

[private]
@default:
    just --list --unsorted

# Run linter.
@lint:
    docker run --rm --read-only --volume=$PWD:$PWD:ro --workdir=$PWD kokuwaio/just:1.50.0
    docker run --rm --read-only --volume=$PWD:$PWD:ro --workdir=$PWD kokuwaio/hadolint:v2.14.0
    docker run --rm --read-only --volume=$PWD:$PWD:ro --workdir=$PWD kokuwaio/yamllint:v1.38.0
    docker run --rm --read-only --volume=$PWD:$PWD:rw --workdir=$PWD kokuwaio/markdownlint:0.48.0 --fix
    docker run --rm --read-only --volume=$PWD:$PWD:ro --workdir=$PWD kokuwaio/renovate-config-validator:43
    docker run --rm --read-only --volume=$PWD:$PWD:ro --workdir=$PWD woodpeckerci/woodpecker-cli:v3 lint --strict

# Build image with local docker daemon.
@build TARGET="debian":
    docker build . --target={{ TARGET }} --tag=kokuwaio/keycloak:dev-{{ TARGET }}

# Build all variants
@build-all:
    docker buildx build . --target=debian     --platform=amd64 --platform=arm64
    docker buildx build . --target=temurin    --platform=amd64 --platform=arm64
    docker buildx build . --target=distroless --platform=amd64 --platform=arm64
    docker buildx build . --target=themes     --platform=amd64 --platform=arm64

# Inspect image layers with `dive`.
@dive TARGET="debian":
    dive build . --target={{ TARGET }}
