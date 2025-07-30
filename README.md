# Keycloak

[Keycloak](https://github.com/keycloak/keycloak) container image for Kokuwa.

[![license](https://img.shields.io/badge/license-EUPL%201.2-blue)](https://git.kokuwa.io/kokuwaio/keycloak/src/branch/main/LICENSE)
[![issues](https://img.shields.io/gitea/issues/open/kokuwaio/keycloak?gitea_url=https%3A%2F%2Fgit.kokuwa.io)](https://git.kokuwa.io/kokuwaio/keycloak/issues)
[![prs](https://img.shields.io/gitea/pull-requests/open/kokuwaio/keycloak?gitea_url=https%3A%2F%2Fgit.kokuwa.io)](https://git.kokuwa.io/kokuwaio/keycloak/pulls)
[![build](https://ci.kokuwa.io/api/badges/kokuwaio/keycloak/status.svg)](https://ci.kokuwa.io/repos/kokuwaio/keycloak/)

## Features

- BaseImage:
  - [debian:stable-slim](https://hub.docker.com/_/debian) (amd64 & arm64)
  - [eclipse-temurin:17-jre](https://hub.docker.com/_/eclipse-temurin) (amd64 & arm64)
  - [distroless/java17:nonroot](https://gcr.io/distroless/java17:nonroot) (amd64)
- preconfigured with PostgeSQL
- preconfigured for Kubernetes:
  - health enabled
  - quarkus metrics enabled
  - keycloak metrics enabled (see [keycloak-event-metrics](https://git.kokuwa.io/kokuwaio/keycloak-event-metrics)) with model names
  - logs as json
- [integration test](src/test/k3s) with [k3s](https://k3s.io/) and deployment of [example theme](src/themes/kokuwa)

## Why

in June 2022 no preconfigured Keycloak container image without legacy Keycloak was available.

Use this image if you intend to:

1. use Keycloak in Quarkus version, not Wildfly version
1. use PostgeSQL as backend
1. no need for [auto-build](https://www.keycloak.org/server/configuration#_the_auto_build_option_automatic_detection_when_the_server_needs_a_build) for faster startup

## Alternatives

| Image                                                                      | amd64 | arm64 |
| -------------------------------------------------------------------------- |:-----:|:-----:|
| [kokuwaio/keycloak:debian](https://hub.docker.com/r/kokuwaio/keycloak)     | [![size](https://img.shields.io/docker/image-size/kokuwaio/keycloak/debian?arch=amd64&label=)](https://hub.docker.com/r/kokuwaio/keycloak)     | [![size](https://img.shields.io/docker/image-size/kokuwaio/keycloak/debian?arch=arm64&label=)](https://hub.docker.com/r/kokuwaio/keycloak)     |
| [kokuwaio/keycloak:temurin](https://hub.docker.com/r/kokuwaio/keycloak)    | [![size](https://img.shields.io/docker/image-size/kokuwaio/keycloak/temurin?arch=amd64&label=)](https://hub.docker.com/r/kokuwaio/keycloak)    | [![size](https://img.shields.io/docker/image-size/kokuwaio/keycloak/temurin?arch=arm64&label=)](https://hub.docker.com/r/kokuwaio/keycloak)    |
| [kokuwaio/keycloak:distroless](https://hub.docker.com/r/kokuwaio/keycloak) | [![size](https://img.shields.io/docker/image-size/kokuwaio/keycloak/distroless?arch=amd64&label=)](https://hub.docker.com/r/kokuwaio/keycloak) | [![size](https://img.shields.io/docker/image-size/kokuwaio/keycloak/distroless?arch=arm64&label=)](https://hub.docker.com/r/kokuwaio/keycloak) |
| [keycloak/keycloak](https://hub.docker.com/r/keycloak/keycloak)            | [![size](https://img.shields.io/docker/image-size/keycloak/keycloak/latest?arch=amd64&label=)](https://hub.docker.com/r/keycloak/keycloak)     | [![size](https://img.shields.io/docker/image-size/keycloak/keycloak/latest?arch=arm64&label=)](https://hub.docker.com/r/keycloak/keycloak)     |
| [bitnami/keycloak](https://hub.docker.com/r/bitnami/keycloak)              | [![size](https://img.shields.io/docker/image-size/bitnami/keycloak/latest?arch=amd64&label=)](https://hub.docker.com/r/bitnami/keycloak)       | [![size](https://img.shields.io/docker/image-size/bitnami/keycloak/latest?arch=arm64&label=)](https://hub.docker.com/r/bitnami/keycloak)       |

## Registries & Tags

Registries:

- [ghcr.io/kokuwaio/keycloak](https://github.com/kokuwaio/keycloak/pkgs/container/keycloak)
- [docker.io/kokuwaio/keycloak](https://hub.docker.com/r/kokuwaio/keycloak)
- [registry.kokuwa.io/kokuwaio/keycloak](https://hub.docker.com/r/kokuwaio/keycloak) (no HA setup!)

Tags:

- `latest`
- `<version>` specific version for Keycloak
- all tags are available with suffix `-debian`, `-temurin` or `-distroless` for specific base images

## Develop and testing

Start k3s: `mvn pre-integration-test`

Open <http://keycloak.127.0.0.1.nip.io:8080/admin/master/console/> or use `kubectl`:

```sh
export KUBECONFIG=~/.kube/k3s.yaml
kubectl get all --all-namespaces
```
