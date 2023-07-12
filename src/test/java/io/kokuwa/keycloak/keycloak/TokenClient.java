package io.kokuwa.keycloak.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.UserInfo;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

public interface TokenClient {

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("http://auth.{realm-name}.127.0.0.1.nip.io:8080/realms/{realm-name}/protocol/openid-connect/token")
	AccessTokenResponse token(
			@PathParam("realm-name") String realm,
			@FormParam(OAuth2Constants.GRANT_TYPE) String grantType,
			@FormParam(OAuth2Constants.CLIENT_ID) String clientId,
			@FormParam(OAuth2Constants.USERNAME) String username,
			@FormParam(OAuth2Constants.PASSWORD) String password,
			@FormParam(OAuth2Constants.SCOPE) String scope);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("http://auth.{realm-name}.127.0.0.1.nip.io:8080/realms/{realm-name}/protocol/openid-connect/userinfo")
	UserInfo userinfo(@PathParam("realm-name") String realm, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth);
}
