# Keycloak

Keycloak container image for Kokuwa.

[![License](https://img.shields.io/github/license/kokuwaio/keycloak.svg?label=License)](https://github.com/kokuwaio/keycloak/blob/main/LICENSE)
[![Build](https://img.shields.io/github/workflow/status/kokuwaio/keycloak/Build?label=Build)](https://github.com/kokuwaio/keycloak/actions/workflows/build.yaml)
[![Lint](https://img.shields.io/github/workflow/status/kokuwaio/keycloak/CI/main?label=CI)](https://github.com/kokuwaio/keycloak/actions/workflows/ci.yaml)

## Features

- BaseImage is [gcr.io/distroless/java11:nonroot](https://gcr.io/distroless/java11:nonroot)
- preconfigured with PostgeSQL
- preconfigured for Kubernetes:
  - health enabled
  - quarkus metrics enabled
  - keycloak metrics enabled (see [aerogear/keycloak-metrics-spi](https://github.com/aerogear/keycloak-metrics-spi))
  - logs as json
  - ispn cache
- [integration test](/src/test/k3s) with [k3s](https://k3s.io/)

## Registries & Tags

Not yet pushed to public repositories.

## Develop and testing

Start k3s: `mvn pre-integration-test`

Open <http://help.127.0.0.1.nip.io:8080> or use `kubectl`:

```sh
export KUBECONFIG=/tmp/k3s-maven-plugin/mount/kubeconfig.yaml
kubectl get all --all-namespaces
```
