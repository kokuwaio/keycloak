package io.kokuwa.keycloak.keycloak;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.keycloak.representations.idm.RealmRepresentation;

/**
 * Client to access Prometheus metric values:
 *
 * @author Stephan Schnabel
 */
public class Prometheus {

	private final Set<PrometheusMetric> state = new HashSet<>();
	private final PrometheusClient client;

	public Prometheus(PrometheusClient client) {
		this.client = client;
	}

	public int logins(RealmRepresentation realm) {
		return state.stream()
				.filter(metric -> Objects.equals(metric.name(), "keycloak_event_user_total"))
				.filter(metric -> Objects.equals(metric.tags().get("realm"), realm.getRealm()))
				.filter(metric -> Objects.equals(metric.tags().get("type"), "LOGIN"))
				.mapToInt(metric -> metric.value().intValue())
				.sum();
	}

	public int loginErrors(RealmRepresentation realm) {
		return state.stream()
				.filter(metric -> Objects.equals(metric.name(), "keycloak_event_user_total"))
				.filter(metric -> Objects.equals(metric.tags().get("realm"), realm.getRealm()))
				.filter(metric -> Objects.equals(metric.tags().get("type"), "LOGIN_ERROR"))
				.mapToInt(metric -> metric.value().intValue())
				.sum();
	}

	public void scrap() {
		state.clear();
		Stream.of(client.scrap().split("[\\r\\n]+"))
				.filter(line -> !line.startsWith("#"))
				.filter(line -> line.startsWith("keycloak"))
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
				.forEach(state::add);
	}
}
