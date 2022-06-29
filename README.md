# Keycloak

Keycloak container image for Kokuwa.

[![License](https://img.shields.io/github/license/kokuwaio/keycloak.svg?label=License)](https://github.com/kokuwaio/keycloak/blob/main/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/io.kokuwa.maven/keycloak.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.kokuwa.maven%22%20AND%20a:%22keycloak%22)
[![Build](https://img.shields.io/github/workflow/status/kokuwaio/keycloak/Build?label=Build)](https://github.com/kokuwaio/keycloak/actions/workflows/snapshot.yaml?label=Build)
[![Lint](https://img.shields.io/github/workflow/status/kokuwaio/keycloak/CI?label=Lint)](https://github.com/kokuwaio/keycloak/actions/workflows/lint.yaml?label=Lint)

## Features

- BaseImage is [`gcr.io/distroless/java11:nonroot`]
- preconfigured with PostgeSQL
- preconfigured for Kubernetes:
  - health enabled
  - quarkus metrics enabled
  - keycloak metrics enabled (see [aerogear/keycloak-metrics-spi](https://github.com/aerogear/keycloak-metrics-spi))
  - logs as json
  - ispn cache
- integration [test](/src/test/k3s) with [`k3s`](https://k3s.io/)

## Images & Registries

[ghcr.io/kokuwaio/keycloak]

## Develop and testing

Start k3s: `mvn pre-integration-test`

Open: <http://help.127.0.0.1.nip.io:8080>

Or use `kubectl`:

```sh
export KUBECONFIG=/tmp/k3s-maven-plugin/mount/kubeconfig.yaml
kubectl get all --all-namespaces
```

