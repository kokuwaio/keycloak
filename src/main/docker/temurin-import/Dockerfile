FROM kokuwaio/keycloak:${image.tag}-base

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
ENV \
  KC_DB=postgres \
  KC_CACHE=local \
  KC_LOG_CONSOLE_COLOR=false \
  KC_LOG_CONSOLE_OUTPUT=JSON

RUN java -Dkc.home.dir=/app -jar /app/lib/quarkus-run.jar build
ENTRYPOINT ["java","-XX:+ExitOnOutOfMemoryError","-jar","/app/lib/quarkus-run.jar"]
CMD ["import", "--dir=/realms"]
