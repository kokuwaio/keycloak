package io.kokuwa.keycloak.keycloak;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.keycloak.representations.idm.RealmRepresentation;

public class Prometheus {

	private final PrometheusClient client;

	public Prometheus(PrometheusClient client) {
		this.client = client;
	}

	public int logins(RealmRepresentation realm) {
		return scrap("keycloak_logins", "realm", realm.getId()).intValue();
	}

	public int failedLoginAttempts(RealmRepresentation realm) {
		return scrap("keycloak_failed_login_attempts", "realm", realm.getId()).intValue();
	}

	public int loginAttempts(RealmRepresentation realm) {
		return scrap("keycloak_login_attempts", "realm", realm.getId()).intValue();
	}

	private Set<PrometheusMetric> scrap() {
		return Stream.of(client.scrap().split("[\\r\\n]+"))
				.filter(line -> !line.startsWith("#"))
				.map(line -> {
					var name = line.substring(0, line.contains("{") ? line.indexOf("{") : line.lastIndexOf(" "));
					var tags = line.contains("{")
							? Stream.of(line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(","))
									.map(tag -> tag.split("="))
									.filter(tag -> tag.length >= 2)
									.collect(Collectors.toMap(tag -> tag[0], tag -> tag[1].replace("\"", "")))
							: Map.<String, String>of();
					var value = Double.parseDouble(line.substring(line.lastIndexOf(" ")));
					return new PrometheusMetric(name, tags, value);
				})
				.collect(Collectors.toSet());
	}

	private Double scrap(String name, String tag, String value) {
		return scrap().stream()
				.filter(metric -> Objects.equals(metric.getName(), name))
				.filter(metric -> Objects.equals(metric.getTags().get(tag), value))
				.mapToDouble(PrometheusMetric::getValue)
				.sum();
	}
}
