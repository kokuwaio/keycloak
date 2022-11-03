FROM docker.io/eclipse-temurin:${maven.compiler.target}-jre

ARG VERSION=${version.org.keycloak}
ADD keycloak-quarkus-dist-$VERSION.tar.gz /tmp
RUN mv /tmp/keycloak-$VERSION /app && rm -rf /app/bin && chmod -R ugo+r /app
