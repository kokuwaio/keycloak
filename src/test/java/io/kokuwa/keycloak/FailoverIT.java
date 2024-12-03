package io.kokuwa.keycloak;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.kokuwa.keycloak.k8s.Kubernetes;
import io.kokuwa.keycloak.k8s.KubernetesExtension;
import io.kokuwa.keycloak.keycloak.KeycloakExtension;
import io.kokuwa.keycloak.keycloak.OpenIDConnect;

@DisplayName("failover")
@ExtendWith(KubernetesExtension.class)
@ExtendWith(KeycloakExtension.class)
public class FailoverIT {

	@DisplayName("login survives rolling restart")
	@Test
	void rollingRestart(Kubernetes kubernetes, OpenIDConnect oidc) {

		// get token and validate on userinfo endpoint

		var token = assertDoesNotThrow(() -> oidc.token("test", "horst", "password"), "token before restart");
		assertDoesNotThrow(() -> oidc.userinfo("test", token), "session should be available before restart");

		// patch to enforce rolling update

		kubernetes.restartKeycloak();

		// check if token is valid

		IntStream.range(1, 100).parallel().forEach(i -> assertDoesNotThrow(
				() -> oidc.userinfo("test", token),
				"session should be available after restart"));
	}

	@DisplayName("session should remain after scaling keycloak to 0")
	@Test
	void sessionRemains(Kubernetes kubernetes, OpenIDConnect oidc) {

		// get token and validate on userinfo endpoint

		var token = assertDoesNotThrow(() -> oidc.token("test", "horst", "password"), "token before restart");
		assertDoesNotThrow(() -> oidc.userinfo("test", token), "session should be available before restart");

		// scale keycloak to 0 to purge caches/sessions

		kubernetes.scaleKeycloak(0);
		kubernetes.scaleKeycloak(Kubernetes.KEYCLOAK_REPLICAS);

		// try to validate old token, should be fine because session should be stored since KeyCloak >= 26

		assertDoesNotThrow(() -> oidc.userinfo("test", token), "session should still be valid");
	}
}
