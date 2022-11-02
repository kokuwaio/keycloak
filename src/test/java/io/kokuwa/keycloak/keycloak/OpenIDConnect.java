package io.kokuwa.keycloak.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.UserInfo;

public class OpenIDConnect {

	private final TokenClient client;

	public OpenIDConnect(TokenClient client) {
		this.client = client;
	}

	public AccessTokenResponse token(String realm, String username, String password) {
		return client.token(
				realm, OAuth2Constants.PASSWORD, "test-client", username, password, OAuth2Constants.SCOPE_OPENID);
	}

	public UserInfo userinfo(String realm, AccessTokenResponse token) {
		return client.userinfo(realm, "Bearer " + token.getToken());
	}
}
