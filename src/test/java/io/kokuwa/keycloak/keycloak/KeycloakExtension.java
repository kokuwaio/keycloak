package io.kokuwa.keycloak.keycloak;

import java.util.Set;

import jakarta.ws.rs.client.ClientBuilder;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.keycloak.admin.client.Keycloak;

import io.kokuwa.keycloak.TestProperties;

public class KeycloakExtension implements ParameterResolver {

	private static final Keycloak keycloak = Keycloak.getInstance(
			"http://keycloak." + TestProperties.IP + ".nip.io:8080",
			"master",
			"admin",
			"password",
			"admin-cli");
	private static final ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return Set
				.of(Keycloak.class, OpenIDConnect.class, Prometheus.class)
				.contains(parameterContext.getParameter().getType());
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		if (parameterContext.getParameter().getType().equals(Prometheus.class)) {
			return new Prometheus(client.target("").proxy(PrometheusClient.class));
		}
		if (parameterContext.getParameter().getType().equals(OpenIDConnect.class)) {
			return new OpenIDConnect(client.target("").proxy(TokenClient.class));
		}
		return keycloak;
	}
}
