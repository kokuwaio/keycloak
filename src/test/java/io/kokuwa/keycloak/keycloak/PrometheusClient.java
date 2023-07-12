package io.kokuwa.keycloak.keycloak;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

/**
 * JAX-RS client for prometheus endpoint.
 *
 * @author Stephan Schnabel
 */
public interface PrometheusClient {

	@GET
	@Path("http://keycloak.127.0.0.1.nip.io:8080/metrics")
	@Consumes(MediaType.TEXT_PLAIN)
	String scrap();
}
