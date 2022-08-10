###
### keycloak base
###

FROM eclipse-temurin:${maven.compiler.target}-jre AS build

ARG VERSION=${version.org.keycloak}
ADD keycloak-quarkus-dist-$VERSION.tar.gz /tmp
ADD https://github.com/aerogear/keycloak-metrics-spi/releases/download/2.5.3/keycloak-metrics-spi-2.5.3.jar /tmp/keycloak-$VERSION/providers/metrics-spi.jar
COPY cache-ispn.xml /tmp/keycloak-$VERSION/conf/cache-ispn.xml
RUN mv /tmp/keycloak-$VERSION /app && rm -rf /app/bin && chmod -R ugo+r /app

# https://www.keycloak.org/server/all-config
ENV KC_DB=postgres
ENV KC_CACHE=ispn
ENV KC_CACHE_STACK=kubernetes
ENV KC_CACHE_DNS=keycloak-headless
ENV KC_CACHE_OWNERS=2
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
RUN java -Dkc.home.dir=/app -jar /app/lib/quarkus-run.jar build

###
### server
###

FROM eclipse-temurin:${maven.compiler.target}-jre

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
LABEL org.opencontainers.image.ref.name    ${image.tag}
LABEL org.opencontainers.image.base.name   eclipse-temurin:${maven.compiler.target}-jre

WORKDIR /app
COPY --from=build /app /app

ENV KC_DB=postgres
ENV KC_CACHE=ispn
ENV KC_CACHE_STACK=kubernetes
ENV KC_CACHE_DNS=keycloak-headless
ENV KC_CACHE_OWNERS=2
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV URI_METRICS_ENABLED=false
ENV URI_METRICS_DETAILED=false
ENV KC_PROXY=edge
ENV KC_LOG_CONSOLE_COLOR=false
ENV KC_LOG_CONSOLE_OUTPUT=JSON

ENTRYPOINT ["java","-XX:+ExitOnOutOfMemoryError","-Djgroups.dns.query=${KC_CACHE_DNS}","-jar","/app/lib/quarkus-run.jar"]
CMD ["start", "--optimized"]
