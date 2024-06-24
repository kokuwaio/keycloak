package io.kokuwa.keycloak.k8s;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.util.wait.Wait;

public class Kubernetes {

	public static final String KEYCLOAK_NAMESPACE = "default";
	public static final String KEYCLOAK_NAME = "keycloak";
	public static final int KEYCLOAK_REPLICAS = 3;

	private final Logger log = LoggerFactory.getLogger(Kubernetes.class);
	private final AppsV1Api appApi;

	Kubernetes(ApiClient client) {
		this.appApi = new AppsV1Api(client);
	}

	public void restartKeycloak() {
		try {
			var random = RandomStringUtils.randomAlphabetic(10);
			var patch = "[{\"op\":\"add\",\"path\":\"/spec/template/metadata/labels/" + random + "\",\"value\":\"1\"}]";
			appApi.patchNamespacedStatefulSet(KEYCLOAK_NAME, KEYCLOAK_NAMESPACE,
					new V1Patch(patch), null, null, null, null, null);
			log.info("Patched Keycloak to trigger restart of pods");
		} catch (ApiException e) {
			fail("failed to patch keycloak", e);
		}
		waitForKeycloakReady();
	}

	public void scaleKeycloak(int replicas) {
		try {
			var patch = "[{\"op\":\"replace\",\"path\":\"/spec/replicas\",\"value\":" + replicas + "}]";
			appApi.patchNamespacedStatefulSet(KEYCLOAK_NAME, KEYCLOAK_NAMESPACE,
					new V1Patch(patch), null, null, null, null, null);
			log.info("Scaled to {} replicas", replicas);
		} catch (ApiException e) {
			fail("failed to scale keycloak", e);
		}
		waitForKeycloakReady();
	}

	public void waitForKeycloakReady() {
		Wait.poll(Duration.ofMillis(500), Duration.ofMinutes(5), () -> {
			try {
				var statefulset = appApi.readNamespacedStatefulSet(KEYCLOAK_NAME, KEYCLOAK_NAMESPACE, "true");
				var expectedReplicas = statefulset.getSpec().getReplicas();
				var currentReplicas = Optional.ofNullable(statefulset.getStatus().getCurrentReplicas()).orElse(0);
				var availableReplicas = Optional.ofNullable(statefulset.getStatus().getAvailableReplicas()).orElse(0);
				var actualReplicas = statefulset.getStatus().getReplicas();
				log.info("Found {}/{} available/current of {}/{} replicas",
						availableReplicas, currentReplicas, actualReplicas, expectedReplicas);
				return availableReplicas == expectedReplicas
						&& currentReplicas == expectedReplicas;
			} catch (ApiException e) {
				fail("failed to wait for finishing scaling keycloak", e);
				return false;
			}
		});
	}
}
