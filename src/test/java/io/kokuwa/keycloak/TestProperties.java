package io.kokuwa.keycloak;

import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.extension.ParameterResolutionException;

public class TestProperties {

	public static final String IP;
	public static final String KUBECONFIG;

	static {
		var properties = new Properties();
		try {
			properties.load(TestProperties.class.getResourceAsStream("/k3s.properties"));
		} catch (IOException e) {
			throw new ParameterResolutionException("Failed to read properties", e);
		}
		IP = properties.getProperty("ip");
		KUBECONFIG = properties.getProperty("kubeconfig");
	}
}
