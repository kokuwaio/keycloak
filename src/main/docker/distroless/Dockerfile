FROM gcr.io/distroless/java${maven.compiler.target}:nonroot

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
  KC_LOG_CONSOLE_COLOR=false \
  KC_LOG_CONSOLE_OUTPUT=json

# hadolint ignore=DL3022
COPY --from=kokuwaio/keycloak:${image.tag}-temurin /app /app
ENTRYPOINT ["java", "--add-opens", "java.base/java.util=ALL-UNNAMED", "-XX:+ExitOnOutOfMemoryError", "-Djgroups.dns.query=${KC_CACHE_DNS}","-jar","/app/lib/quarkus-run.jar"]
CMD ["start", "--optimized"]
