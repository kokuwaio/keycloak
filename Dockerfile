##
## Download and preconfigure
##

FROM docker.io/library/debian:13.2-slim@sha256:e711a7b30ec1261130d0a121050b4ed81d7fb28aeabcf4ea0c7876d4e9f5aca2 AS build
WORKDIR /build
# hadolint ignore=DL3008
RUN --mount=type=cache,target=/var/lib/apt/lists,sharing=locked \
	--mount=type=tmpfs,target=/var/cache \
	--mount=type=tmpfs,target=/var/log \
	apt-get -qq update && \
	apt-get -qq install --yes --no-install-recommends wget ca-certificates gpg gpg-agent dirmngr && \
	rm -rf /var/lib/dpkg/*-old

FROM build AS java
SHELL ["/bin/bash", "-u", "-e", "-o", "pipefail", "-c"]
ARG TARGETARCH
ARG JAVA_VERSION=jdk-17.0.8+7
RUN --mount=type=cache,target=/build,sharing=private \
	--mount=type=cache,target=/root \
	--mount=type=tmpfs,target=/tmp \
	[[ $TARGETARCH == amd64 ]] && export ARCH=x64; \
	[[ $TARGETARCH == arm64 ]] && export ARCH=aarch64; \
	[[ -z ${ARCH:-} ]] && echo "Unknown arch: $TARGETARCH" && exit 1; \
	JAVA_VERSION=${JAVA_VERSION/jdk-/} && \
	wget --quiet --no-hsts --no-clobber \
		"https://github.com/adoptium/temurin17-binaries/releases/download/jdk-${JAVA_VERSION/+/%2B}/OpenJDK17U-jre_${ARCH}_linux_hotspot_${JAVA_VERSION/+/_}.tar.gz" \
		"https://github.com/adoptium/temurin17-binaries/releases/download/jdk-${JAVA_VERSION/+/%2B}/OpenJDK17U-jre_${ARCH}_linux_hotspot_${JAVA_VERSION/+/_}.tar.gz.sha256.txt" \
		"https://github.com/adoptium/temurin17-binaries/releases/download/jdk-${JAVA_VERSION/+/%2B}/OpenJDK17U-jre_${ARCH}_linux_hotspot_${JAVA_VERSION/+/_}.tar.gz.sig" && \
	sha256sum --quiet --check --strict "OpenJDK17U-jre_${ARCH}_linux_hotspot_${JAVA_VERSION/+/_}.tar.gz.sha256.txt" && \
	gpg --keyserver keyserver.ubuntu.com --recv-keys 3B04D753C9050D9A5D343F39843C48A565F8F04B && \
	gpg --verify "OpenJDK17U-jre_${ARCH}_linux_hotspot_${JAVA_VERSION/+/_}.tar.gz.sig" "OpenJDK17U-jre_${ARCH}_linux_hotspot_${JAVA_VERSION/+/_}.tar.gz" && \
	mkdir /opt/java && \
	tar --extract --file="OpenJDK17U-jre_${ARCH}_linux_hotspot_${JAVA_VERSION/+/_}.tar.gz" --directory=/opt/java --strip-components=1 --exclude=NOTICE --exclude=legal --exclude=man --exclude=release

FROM build AS keycloak
RUN --mount=type=cache,target=/build,sharing=private \
	--mount=type=cache,target=/root \
	--mount=type=tmpfs,target=/tmp \
	wget --quiet --no-hsts --no-clobber \
		https://repo1.maven.org/maven2/org/keycloak/keycloak-quarkus-dist/26.4.7/keycloak-quarkus-dist-26.4.7.tar.gz  \
		https://repo1.maven.org/maven2/org/keycloak/keycloak-quarkus-dist/26.4.7/keycloak-quarkus-dist-26.4.7.tar.gz.sha1 \
		https://repo1.maven.org/maven2/org/keycloak/keycloak-quarkus-dist/26.4.7/keycloak-quarkus-dist-26.4.7.tar.gz.asc && \
	echo "$(cat keycloak-quarkus-dist-26.4.7.tar.gz.sha1) keycloak-quarkus-dist-26.4.7.tar.gz" sha1sum --quiet --check --strict - && \
	gpg --keyserver keyserver.ubuntu.com --recv-keys 861AB50E8CC6611FB6BC01A6B8F12EA26FD6EEBA && \
	gpg --verify keycloak-quarkus-dist-26.4.7.tar.gz.asc keycloak-quarkus-dist-26.4.7.tar.gz && \
	mkdir /opt/keycloak && \
	tar -xf keycloak-quarkus-dist-26.4.7.tar.gz --directory=/opt/keycloak/ --strip-components=1 --exclude=**/*.md --exclude=**/*.txt --exclude=**/*.bat

FROM build AS metrics
RUN --mount=type=cache,target=/build,sharing=private \
	--mount=type=cache,target=/root \
	--mount=type=tmpfs,target=/tmp \
	wget --quiet --no-hsts --no-clobber \
		https://repo1.maven.org/maven2/io/kokuwa/keycloak/keycloak-event-metrics/2.0.0/keycloak-event-metrics-2.0.0.jar \
		https://repo1.maven.org/maven2/io/kokuwa/keycloak/keycloak-event-metrics/2.0.0/keycloak-event-metrics-2.0.0.jar.sha1 \
		https://repo1.maven.org/maven2/io/kokuwa/keycloak/keycloak-event-metrics/2.0.0/keycloak-event-metrics-2.0.0.jar.asc && \
	echo "$(cat keycloak-event-metrics-2.0.0.jar.sha1) keycloak-event-metrics-2.0.0.jar" sha1sum --quiet --check --strict - && \
	gpg --keyserver keyserver.ubuntu.com --recv-keys 16E64B1DB637EE17228B13E999D36D198C64335E && \
	gpg --verify keycloak-event-metrics-2.0.0.jar.asc keycloak-event-metrics-2.0.0.jar && \
	mkdir -p /opt/keycloak/providers && \
	mv keycloak-event-metrics-2.0.0.jar /opt/keycloak/providers/

FROM build AS keycloak-runtime
COPY --link --from=java /opt/java /opt/java
COPY --link --from=keycloak /opt/keycloak /opt/keycloak
COPY --link --from=metrics /opt/keycloak/providers /opt/keycloak/providers
# https://www.keycloak.org/server/all-config
ENV KC_DB=postgres KC_HEALTH_ENABLED=true KC_METRICS_ENABLED=true
RUN /opt/java/bin/java -Dkc.home.dir=/opt/keycloak -jar /opt/keycloak/lib/quarkus-run.jar build

##
## Debian
##

FROM docker.io/library/debian:13.2-slim@sha256:e711a7b30ec1261130d0a121050b4ed81d7fb28aeabcf4ea0c7876d4e9f5aca2 AS debian
COPY --link --from=java /opt/java /opt/java
COPY --link --from=keycloak-runtime /opt/keycloak /opt/keycloak
ENV \
  KC_DB=postgres \
  KC_HTTP_ENABLED=true \
  KC_HTTP_METRICS_HISTOGRAMS_ENABLED=true \
  KC_PROXY_HEADERS=xforwarded \
  KC_HEALTH_ENABLED=true \
  KC_METRICS_ENABLED=true \
  KC_METRICS_EVENT_REPLACE_IDS=true \
  KC_METRICS_STATS_ENABLED=true \
  KC_LOG_CONSOLE_OUTPUT=json
ENTRYPOINT ["/opt/java/bin/java", "-XX:+ExitOnOutOfMemoryError", "-Dkc.home.dir=/opt/keycloak", "-jar", "/opt/keycloak/lib/quarkus-run.jar"]
CMD ["start", "--optimized"]
USER 1000:1000

##
## Temurin
##

FROM docker.io/eclipse-temurin:17.0.15_6-jre@sha256:e46d19ca8758322d37a4f26150d63e5038b5974ea999667bf698a92449cbb108 AS temurin
COPY --link --from=keycloak-runtime /opt/keycloak /opt/keycloak
ENV \
  KC_DB=postgres \
  KC_HTTP_ENABLED=true \
  KC_HTTP_METRICS_HISTOGRAMS_ENABLED=true \
  KC_PROXY_HEADERS=xforwarded \
  KC_HEALTH_ENABLED=true \
  KC_METRICS_ENABLED=true \
  KC_METRICS_EVENT_REPLACE_IDS=true \
  KC_METRICS_STATS_ENABLED=true \
  KC_LOG_CONSOLE_OUTPUT=json
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Dkc.home.dir=/opt/keycloak", "-jar", "/opt/keycloak/lib/quarkus-run.jar"]
CMD ["start", "--optimized"]
USER 1000:1000

##
## Distroless
##

FROM gcr.io/distroless/java17:nonroot@sha256:4db00c8da3be99095adba2974bd5e416b0561519a194662bea7c677d0f819c16 AS distroless
COPY --link --from=keycloak-runtime /opt/keycloak /opt/keycloak
ENV \
  KC_DB=postgres \
  KC_HTTP_ENABLED=true \
  KC_HTTP_METRICS_HISTOGRAMS_ENABLED=true \
  KC_PROXY_HEADERS=xforwarded \
  KC_HEALTH_ENABLED=true \
  KC_METRICS_ENABLED=true \
  KC_METRICS_EVENT_REPLACE_IDS=true \
  KC_METRICS_STATS_ENABLED=true \
  KC_LOG_CONSOLE_OUTPUT=json
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Dkc.home.dir=/opt/keycloak", "-jar", "/opt/keycloak/lib/quarkus-run.jar"]
CMD ["start", "--optimized"]
USER 1000:1000

##
## Themes
##

FROM docker.io/library/debian:13.2-slim@sha256:e711a7b30ec1261130d0a121050b4ed81d7fb28aeabcf4ea0c7876d4e9f5aca2 AS themes
COPY src/themes /themes
ENTRYPOINT ["cp", "-r", "/themes", "/opt/keycloak/themes/"]
USER 1000:1000
