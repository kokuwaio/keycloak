FROM gcr.io/distroless/java${maven.compiler.target}:nonroot

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
  org.opencontainers.image.ref.name    ${image.tag}-distroless-import \
  org.opencontainers.image.base.name   gcr.io/distroless/java${maven.compiler.target}:nonroot

# https://www.keycloak.org/server/all-config
ENV \
  KC_DB=postgres \
  KC_CACHE=local \
  KC_LOG_CONSOLE_COLOR=false \
  KC_LOG_CONSOLE_OUTPUT=JSON

# hadolint ignore=DL3022
COPY --from=kokuwaio/keycloak:${image.tag}-temurin-import /app /app
ENTRYPOINT ["java","-XX:+ExitOnOutOfMemoryError","-jar","/app/lib/quarkus-run.jar"]
CMD ["import", "--dir=/realms"]
