package io.kokuwa.keycloak.keycloak;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.UserInfo;

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
			@FormParam(OAuth2Constants.PASSWORD) String password);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("http://auth.{realm-name}.127.0.0.1.nip.io:8080/realms/{realm-name}/protocol/openid-connect//userinfo")
	UserInfo userinfo(@PathParam("realm-name") String realm, @HeaderParam(HttpHeaders.AUTHORIZATION) String auth);
}
