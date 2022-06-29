package io.kokuwa.keycloak.mailhog;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("http://mail.127.0.0.1.nip.io:8080")
public interface Mailhog {

	@Path("/api/v1/messages")
	@DELETE
	void deleteMessages();

	@Path("/api/v2/messages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	MessagesVO getMessages();

	@Path("/api/v2/messages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	String getMessagess();
}