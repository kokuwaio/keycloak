package io.kokuwa.keycloak.keycloak;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

public interface PrometheusClient {

	@GET
	@Path("http://keycloak.127.0.0.1.nip.io:8080/realms/master/metrics")
	@Consumes(MediaType.TEXT_PLAIN)
	String scrap();
}