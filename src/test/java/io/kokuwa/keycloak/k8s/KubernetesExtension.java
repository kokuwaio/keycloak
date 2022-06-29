package io.kokuwa.keycloak.k8s;

import java.io.IOException;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.kubernetes.client.util.Config;

public class KubernetesExtension implements ParameterResolver, BeforeAllCallback, AfterEachCallback {

	private static Kubernetes kubernetes;

	public Kubernetes kubernetes() {
		if (kubernetes == null) {
			try {
				kubernetes = new Kubernetes(Config.fromConfig("/tmp/k3s-maven-plugin/mount/kubeconfig.yaml"));
			} catch (IOException e) {
				throw new ParameterResolutionException("Failed to read kubeconfig", e);
			}
		}
		return kubernetes;
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		kubernetes().scaleKeycloak(Kubernetes.KEYCLOAK_REPLICAS);
	}

	@Override
	public void afterEach(ExtensionContext context) {
		kubernetes().scaleKeycloak(Kubernetes.KEYCLOAK_REPLICAS);
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return parameterContext.getParameter().getType().equals(Kubernetes.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return kubernetes();
	}
}
