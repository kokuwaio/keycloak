package io.kokuwa.keycloak.mailhog;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("http://mail.{ip}.nip.io:8080")
public interface Mailhog {

	@Path("/api/v1/messages")
	@DELETE
	void deleteMessages(@PathParam("ip") String ip);

	@Path("/api/v2/messages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	MessagesVO getMessages(@PathParam("ip") String ip);

	@Path("/api/v2/messages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	String getMessagess(@PathParam("ip") String ip);
}
