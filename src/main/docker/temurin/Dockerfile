FROM kokuwaio/keycloak:${image.tag}-base

# https://github.com/opencontainers/image-spec/blob/main/annotations.md
LABEL \
  org.opencontainers.image.title       ${project.name} \
  org.opencontainers.image.description ${project.description} \
  org.opencontainers.image.url         ${project.url} \
  org.opencontainers.image.source      ${project.url}/src/main/docker/Dockerfile \
  org.opencontainers.image.vendor      ${project.organization.name} \
  org.opencontainers.image.authors     https://github.com/orgs/kokuwaio/people \
  org.opencontainers.image.licenses    Apache-2.0 \
  org.opencontainers.image.version     ${version.org.keycloak} \
  org.opencontainers.image.created     ${git.build.time} \
  org.opencontainers.image.revision    ${git.commit.id} \
  org.opencontainers.image.ref.name    ${image.tag}-temurin \
  org.opencontainers.image.base.name   docker.io/eclipse-temurin:${maven.compiler.target}-jre

# https://www.keycloak.org/server/all-config
ENV \
  KC_DB=postgres \
  KC_CACHE=ispn \
  KC_CACHE_STACK=kubernetes \
  KC_CACHE_DNS=keycloak-headless \
  KC_CACHE_OWNERS=2 \
  KC_HEALTH_ENABLED=true \
  KC_METRICS_ENABLED=true \
  URI_METRICS_ENABLED=false \
  URI_METRICS_DETAILED=false \
  KC_PROXY=edge \
  KC_LOG_CONSOLE_COLOR=false \
  KC_LOG_CONSOLE_OUTPUT=JSON

COPY cache-ispn.xml /tmp/keycloak-${version.org.keycloak}/conf/cache-ispn.xml
COPY keycloak-event-metrics-${version.io.kokuwa.keycloak.metrics}.jar /app/providers/metrics-spi.jar
RUN java -Dkc.home.dir=/app -jar /app/lib/quarkus-run.jar build
ENTRYPOINT ["java","-XX:+ExitOnOutOfMemoryError","-Djgroups.dns.query=${KC_CACHE_DNS}","-jar","/app/lib/quarkus-run.jar"]
CMD ["start", "--optimized"]
