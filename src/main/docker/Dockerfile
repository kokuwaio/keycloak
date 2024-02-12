FROM docker.io/library/debian:stable-slim AS keycloak
ARG VERSION=${version.org.keycloak}
ADD keycloak-quarkus-dist-$VERSION.tar.gz /tmp
RUN mv "/tmp/keycloak-$VERSION" /app && rm -rf /app/bin/client /app/bin/*.bat

FROM docker.io/eclipse-temurin:${maven.compiler.target}-jre AS keycloak-runtime
# https://www.keycloak.org/server/all-config
ENV \
  KC_DB=postgres \
  KC_CACHE=ispn \
  KC_CACHE_STACK=kubernetes \
  KC_CACHE_DNS=keycloak-headless \
  KC_CACHE_OWNERS=2 \
  KC_HEALTH_ENABLED=true \
  KC_METRICS_ENABLED=true \
  KC_METRICS_EVENT_REPLACE_IDS=true \
  KC_METRICS_STATS_ENABLED=true \
  URI_METRICS_ENABLED=false \
  URI_METRICS_DETAILED=false
COPY --from=keycloak /app /app
COPY cache-ispn.xml /app/conf/cache-ispn.xml
COPY keycloak-event-metrics-${version.io.kokuwa.keycloak.metrics}.jar /app/providers/metrics-spi.jar
RUN java -Dkc.home.dir=/app -jar /app/lib/quarkus-run.jar build

FROM docker.io/eclipse-temurin:${maven.compiler.target}-jre AS keycloak-import
ENV KC_DB=postgres KC_CACHE=local KC_LOG_CONSOLE_OUTPUT=json
COPY --from=keycloak /app /app
RUN java -Dkc.home.dir=/app -jar /app/lib/quarkus-run.jar build

###
### Temurin
###

FROM docker.io/eclipse-temurin:${maven.compiler.target}-jre AS temurin

# https://github.com/opencontainers/image-spec/blob/main/annotations.md
LABEL org.opencontainers.image.title       ${project.name}
LABEL org.opencontainers.image.description ${project.description}
LABEL org.opencontainers.image.url         ${project.url}
LABEL org.opencontainers.image.source      ${project.url}/src/main/docker/Dockerfile
LABEL org.opencontainers.image.vendor      ${project.organization.name}
LABEL org.opencontainers.image.authors     https://github.com/orgs/kokuwaio/people
LABEL org.opencontainers.image.licenses    Apache-2.0
LABEL org.opencontainers.image.version     ${version.org.keycloak}
LABEL org.opencontainers.image.created     ${git.build.time}
LABEL org.opencontainers.image.revision    ${git.commit.id}
LABEL org.opencontainers.image.ref.name    ${image.tag}-temurin
LABEL org.opencontainers.image.base.name   docker.io/eclipse-temurin:${maven.compiler.target}-jre

# https://www.keycloak.org/server/all-config
ENV \
  KC_DB=postgres \
  KC_CACHE=ispn \
  KC_CACHE_STACK=kubernetes \
  KC_CACHE_DNS=keycloak-headless \
  KC_CACHE_OWNERS=2 \
  KC_HEALTH_ENABLED=true \
  KC_METRICS_ENABLED=true \
  KC_METRICS_EVENT_REPLACE_IDS=true \
  KC_METRICS_STATS_ENABLED=true \
  URI_METRICS_ENABLED=false \
  URI_METRICS_DETAILED=false \
  KC_PROXY=edge \
  KC_LOG_CONSOLE_OUTPUT=json

COPY --from=keycloak-runtime /app /app
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Dkc.home.dir=/app", "-Djgroups.dns.query=${KC_CACHE_DNS}", "-jar", "/app/lib/quarkus-run.jar"]
CMD ["start", "--optimized"]

###
### Temurin Import
###

FROM docker.io/eclipse-temurin:${maven.compiler.target}-jre AS temurin-import

# https://github.com/opencontainers/image-spec/blob/main/annotations.md
LABEL org.opencontainers.image.title       ${project.name}
LABEL org.opencontainers.image.description ${project.description}
LABEL org.opencontainers.image.url         ${project.url}
LABEL org.opencontainers.image.source      ${project.url}/src/main/docker/Dockerfile
LABEL org.opencontainers.image.vendor      ${project.organization.name}
LABEL org.opencontainers.image.authors     https://github.com/orgs/kokuwaio/people
LABEL org.opencontainers.image.licenses    Apache-2.0
LABEL org.opencontainers.image.version     ${version.org.keycloak}
LABEL org.opencontainers.image.created     ${git.build.time}
LABEL org.opencontainers.image.revision    ${git.commit.id}
LABEL org.opencontainers.image.ref.name    ${image.tag}-temurin-import
LABEL org.opencontainers.image.base.name   docker.io/eclipse-temurin:${maven.compiler.target}-jre

# https://www.keycloak.org/server/all-config
ENV KC_DB=postgres KC_CACHE=local KC_LOG_CONSOLE_OUTPUT=json

COPY --from=keycloak-import /app /app
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Dkc.home.dir=/app", "-jar", "/app/lib/quarkus-run.jar"]
CMD ["import", "--dir=/realms"]

###
### Distroless
###

FROM gcr.io/distroless/java${maven.compiler.target}:nonroot AS distroless

# https://github.com/opencontainers/image-spec/blob/main/annotations.md
LABEL org.opencontainers.image.title       ${project.name}
LABEL org.opencontainers.image.description ${project.description}
LABEL org.opencontainers.image.url         ${project.url}
LABEL org.opencontainers.image.source      ${project.url}/src/main/docker/Dockerfile
LABEL org.opencontainers.image.vendor      ${project.organization.name}
LABEL org.opencontainers.image.authors     https://github.com/orgs/kokuwaio/people
LABEL org.opencontainers.image.licenses    Apache-2.0
LABEL org.opencontainers.image.version     ${version.org.keycloak}
LABEL org.opencontainers.image.created     ${git.build.time}
LABEL org.opencontainers.image.revision    ${git.commit.id}
LABEL org.opencontainers.image.ref.name    ${image.tag}-distroless
LABEL org.opencontainers.image.base.name   gcr.io/distroless/java${maven.compiler.target}:nonroot

# https://www.keycloak.org/server/all-config
ENV \
  KC_DB=postgres \
  KC_CACHE=ispn \
  KC_CACHE_STACK=kubernetes \
  KC_CACHE_DNS=keycloak-headless \
  KC_CACHE_OWNERS=2 \
  KC_HEALTH_ENABLED=true \
  KC_METRICS_ENABLED=true \
  KC_METRICS_EVENT_REPLACE_IDS=true \
  KC_METRICS_STATS_ENABLED=true \
  URI_METRICS_ENABLED=false \
  URI_METRICS_DETAILED=false \
  KC_PROXY=edge \
  KC_LOG_CONSOLE_OUTPUT=json

COPY --from=keycloak-runtime /app /app
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Dkc.home.dir=/app", "-Djgroups.dns.query=${KC_CACHE_DNS}", "-jar", "/app/lib/quarkus-run.jar"]
CMD ["start", "--optimized"]

###
### Distroless Import
###

FROM gcr.io/distroless/java${maven.compiler.target}:nonroot AS distroless-import

# https://github.com/opencontainers/image-spec/blob/main/annotations.md
LABEL org.opencontainers.image.title       ${project.name}
LABEL org.opencontainers.image.description ${project.description}
LABEL org.opencontainers.image.url         ${project.url}
LABEL org.opencontainers.image.source      ${project.url}/src/main/docker/Dockerfile
LABEL org.opencontainers.image.vendor      ${project.organization.name}
LABEL org.opencontainers.image.authors     https://github.com/orgs/kokuwaio/people
LABEL org.opencontainers.image.licenses    Apache-2.0
LABEL org.opencontainers.image.version     ${version.org.keycloak}
LABEL org.opencontainers.image.created     ${git.build.time}
LABEL org.opencontainers.image.revision    ${git.commit.id}
LABEL org.opencontainers.image.ref.name    ${image.tag}-distroless-import
LABEL org.opencontainers.image.base.name   gcr.io/distroless/java${maven.compiler.target}:nonroot

# https://www.keycloak.org/server/all-config
ENV KC_DB=postgres KC_CACHE=local KC_LOG_CONSOLE_OUTPUT=json

COPY --from=keycloak-import /app /app
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Dkc.home.dir=/app", "-jar", "/app/lib/quarkus-run.jar"]
CMD ["import", "--dir=/realms"]

###
### Themes
###

FROM docker.io/library/debian:stable-slim AS themes

COPY themes /themes
