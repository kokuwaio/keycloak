package io.kokuwa.keycloak.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;

import io.kokuwa.keycloak.TestProperties;

public class OpenIDConnect {

	private final TokenClient client;

	public OpenIDConnect(TokenClient client) {
		this.client = client;
	}

	public AccessTokenResponse token(String realm, String username, String password) {
		return client.token(
				realm,
				TestProperties.IP,
				OAuth2Constants.PASSWORD,
				"test-client",
				username,
				password,
				OAuth2Constants.SCOPE_OPENID);
	}

	public String userinfo(String realm, AccessTokenResponse token) {
		return client.userinfo(realm, TestProperties.IP, "Bearer " + token.getToken());
	}
}
