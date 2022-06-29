package io.kokuwa.keycloak.mailhog;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContentVO {

	public static final String JSON_PROPERTY_HEADERS = "Headers";

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_HEADERS)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private Map<String, List<String>> headers;

	// methods

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		MessageContentVO other = (MessageContentVO) object;
		return Objects.equals(headers, other.headers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(headers);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("MessageContentVO[")
				.append("headers=").append(headers)
				.append("]")
				.toString();
	}

	// getter/setter

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public MessageContentVO setHeaders(Map<String, List<String>> newHeaders) {
		this.headers = newHeaders;
		return this;
	}
}
