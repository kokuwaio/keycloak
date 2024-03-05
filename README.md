# Keycloak

[Keycloak](https://github.com/keycloak/keycloak) container image for Kokuwa.

[![License](https://img.shields.io/github/license/kokuwaio/keycloak.svg?label=License)](https://github.com/kokuwaio/keycloak/blob/main/LICENSE)
[![Build](https://img.shields.io/github/actions/workflow/status/kokuwaio/keycloak/build.yaml?branch=main&label=Build)](https://github.com/kokuwaio/keycloak/actions/workflows/build.yaml)
[![CI](https://img.shields.io/github/actions/workflow/status/kokuwaio/keycloak/ci.yaml?branch=main&label=Lint)](https://github.com/kokuwaio/keycloak/actions/workflows/ci.yaml)

## Features

- BaseImage:
  - [docker.io/eclipse-temurin:17-jre](https://hub.docker.com/_/eclipse-temurin) (amd64 & arm64)
  - [gcr.io/distroless/java17:nonroot](https://gcr.io/distroless/java17:nonroot) (amd64)
- preconfigured with PostgeSQL
- preconfigured for Kubernetes:
  - health enabled
  - quarkus metrics enabled
  - keycloak metrics enabled (see [keycloak-event-metrics](https://github.com/kokuwaio/keycloak-event-metrics)) with model names
  - logs as json
  - ispn cache
- preconfigured tag for realm import from directory `/realms` (see [job.yaml](/src/test/k3s/keycloak/job.yaml))
- [integration test](/src/test/k3s) with [k3s](https://k3s.io/) and deployment of [example theme](/src/main/docker/themes/kokuwa)

## Why

in June 2022 no preconfigured Keycloak container image without legacy Keycloak was available.

Use this image if you intend to:

1. use Keycloak in Quarkus version, not Wildfly version
1. use PostgeSQL as backend
1. deploy to Kubernetes with clustered cache
1. no need for [auto-build](https://www.keycloak.org/server/configuration#_the_auto_build_option_automatic_detection_when_the_server_needs_a_build) for faster startup

Alternatives:

- [quay.io/keycloak/keycloak](https://quay.io/repository/keycloak/keycloak)
- [docker.io/bitnami/keycloak](https://hub.docker.com/r/bitnami/keycloak)

Both are not preconfigured for PostgreSQL, have a larger base images and still use legagy version.

## Registries & Tags

Registries:

- [ghcr.io/kokuwaio/keycloak](https://github.com/kokuwaio/keycloak/pkgs/container/keycloak)
- [docker.io/kokuwaio/keycloak](https://hub.docker.com/r/kokuwaio/keycloak)

Tags:

- `latest`
- `<yyyyMMdd-HHmmss>` timestamped version (e.g. for FluxCD)
- `<majorVersion>` latest major version for Keycloak
- `<majorVersion>-<yyyyMMdd-HHmmss>` timestamped major version (e.g. for FluxCD)
- `<version>` specific version for Keycloak
- `<version>-<yyyyMMdd-HHmmss>` timestamped version (e.g. for FluxCD)
- all tags are available with suffix `-import` for realm import without cache
- all tags are available with suffix `-temurin` or `-distroless` for specific base images

## Sizes

Created with [print-size.sh](/.github/print-size.sh): `.github/print-size.sh`

| image                                                | uncompressed      | compressed        |
|------------------------------------------------------|-------------------|-------------------|
| quay.io/keycloak/keycloak:23.0.1                     | 459.018.503 bytes | 262.181.076 bytes |
| docker.io/kokuwaio/keycloak:23.0.1                   | 446.125.014 bytes | 256.232.904 bytes |
| docker.io/kokuwaio/keycloak:23.0.1-import            | 446.043.228 bytes | 256.160.692 bytes |
| docker.io/kokuwaio/keycloak:23.0.1-distroless        | 403.345.214 bytes | 254.675.552 bytes |
| docker.io/kokuwaio/keycloak:23.0.1-distroless-import | 403.263.428 bytes | 254.603.340 bytes |

- `compressed`: size transfered from registry
- `uncompressed`: size on disk

## Develop and testing

Start k3s: `mvn pre-integration-test`

Open <http://help.127.0.0.1.nip.io:8080> or use `kubectl`:

```sh
export KUBECONFIG=~/.kube/k3s-maven-plugin/mount/kubeconfig.yaml
kubectl get all --all-namespaces
```
