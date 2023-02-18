package io.kokuwa.keycloak.keycloak;

import java.util.Map;

public class PrometheusMetric {

	private final String name;
	private final Map<String, String> tags;
	private final Double value;

	public PrometheusMetric(String name, Map<String, String> tags, Double value) {
		this.name = name;
		this.tags = tags;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public Double getValue() {
		return value;
	}
}
