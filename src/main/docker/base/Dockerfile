FROM docker.io/eclipse-temurin:${maven.compiler.target}-jre

# update and remove unsed software
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get -qq purge wget curl && apt-get -qq autoremove --yes --purge

ARG VERSION=${version.org.keycloak}
ADD keycloak-quarkus-dist-$VERSION.tar.gz /tmp
RUN mv /tmp/keycloak-$VERSION /app && rm -rf /app/bin && chmod -R ugo+r /app
