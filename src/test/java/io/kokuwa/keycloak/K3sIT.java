package io.kokuwa.keycloak;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.NotAuthorizedException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;

import io.kokuwa.keycloak.k8s.Kubernetes;
import io.kokuwa.keycloak.k8s.KubernetesExtension;
import io.kokuwa.keycloak.keycloak.KeycloakExtension;
import io.kokuwa.keycloak.keycloak.OpenIDConnect;
import io.kokuwa.keycloak.keycloak.Prometheus;
import io.kokuwa.keycloak.mailhog.Mailhog;
import io.kokuwa.keycloak.mailhog.MailhogExtension;

@DisplayName("k3s")
@ExtendWith(KeycloakExtension.class)
@ExtendWith(KubernetesExtension.class)
@ExtendWith(MailhogExtension.class)
public class K3sIT {

	@DisplayName("keycloak realms created")
	@Test
	void realmsCreated(Keycloak keycloak) {
		var actual = keycloak.realms().findAll().stream().map(realm -> realm.getRealm()).collect(Collectors.toSet());
		var expected = Set.of("test", "kokuwa", "master");
		assertEquals(expected, actual);
	}

	@DisplayName("keycloak metrics is working")
	@Test
	void metrics(Keycloak keycloak, Kubernetes kubernetes, OpenIDConnect oidc, Prometheus prometheus) {

		// expect only one keycloak to get metrics from

		kubernetes.scaleKeycloak(1);

		// get state before test

		var kokuwa = keycloak.realm("kokuwa").toRepresentation();
		var test = keycloak.realm("test").toRepresentation();
		var loginAttemptsGrayc = prometheus.loginAttempts(kokuwa);
		var loginSuccessGrayc = prometheus.logins(kokuwa);
		var loginFailedGrayc = prometheus.failedLoginAttempts(kokuwa);
		var loginAttemptsTest = prometheus.loginAttempts(test);
		var loginSuccessTest = prometheus.logins(test);
		var loginFailedTest = prometheus.failedLoginAttempts(test);

		// do some successful logins

		assertDoesNotThrow(() -> oidc.token("kokuwa", "horst", "password"));
		assertDoesNotThrow(() -> oidc.token("kokuwa", "admin", "password"));
		assertDoesNotThrow(() -> oidc.token("kokuwa", "admin", "password"));
		assertDoesNotThrow(() -> oidc.token("test", "horst", "password"));
		assertDoesNotThrow(() -> oidc.token("test", "admin", "password"));

		// do some failed logins

		assertThrows(NotAuthorizedException.class, () -> oidc.token("kokuwa", "admin", "nope"));
		assertThrows(NotAuthorizedException.class, () -> oidc.token("kokuwa", "user", "password"));
		assertThrows(NotAuthorizedException.class, () -> oidc.token("test", "horst", "nope"));

		// check metrics count

		assertAll("metrics",
				() -> assertEquals(loginAttemptsGrayc + 5, prometheus.loginAttempts(kokuwa), "kokuwa loginAttempts"),
				() -> assertEquals(loginSuccessGrayc + 3, prometheus.logins(kokuwa), "kokuwa loginSuccess"),
				() -> assertEquals(loginFailedGrayc + 2, prometheus.failedLoginAttempts(kokuwa), "kokuwa loginFailed"),
				() -> assertEquals(loginAttemptsTest + 3, prometheus.loginAttempts(test), "test loginAttempts"),
				() -> assertEquals(loginSuccessTest + 2, prometheus.logins(test), "test loginSuccess"),
				() -> assertEquals(loginFailedTest + 1, prometheus.failedLoginAttempts(test), "test loginFailed"));
	}

	@DisplayName("keycloak mail configured")
	@Test
	void mail(Keycloak keycloak, Mailhog mailhog) {

		// purge all emails

		mailhog.deleteMessages();

		// trigger password refresh

		keycloak.realms().realm("kokuwa").users().get("kokuwa-admin").executeActionsEmail(List.of("UPDATE_PASSWORD"));

		// check for email in greenmail

		var mails = mailhog.getMessages();
		assertEquals(1, mails.getTotal(), "total mails");
		var mail = mails.getItems().get(0);
		assertAll("mail: " + mail,
				() -> assertEquals("noreply", mail.getFrom().getMailbox(), "from.mailbox"),
				() -> assertEquals("kokuwa.127.0.0.1.nip.io", mail.getFrom().getDomain(), "from.domain"),
				() -> assertEquals("admin", mail.getTo().get(0).getMailbox(), "from.mailbox"),
				() -> assertEquals("example.org", mail.getTo().get(0).getDomain(), "from.domain"),
				() -> assertEquals("Update Your Account", mail.getContent().getHeaders().get("Subject").get(0), "sub"),
				() -> assertTrue(mail.getRaw().getData().contains("This link will expire within 12 hours."), "expire"),
				() -> assertTrue(mail.getRaw().getData()
						.contains("http://auth.kokuwa.127.0.0.1.nip.io:8080/realms/kokuwa/login-actions"), "link"));
	}
}
