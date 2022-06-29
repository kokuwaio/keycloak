package io.kokuwa.keycloak.mailhog;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageRawVO {

	public static final String JSON_PROPERTY_DATA = "Data";

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_DATA)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private String data;

	// methods

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		MessageRawVO other = (MessageRawVO) object;
		return Objects.equals(data, other.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(data);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("MessageRawVO[")
				.append("data=").append(data)
				.append("]")
				.toString();
	}

	// getter/setter

	public String getData() {
		return data;
	}

	public MessageRawVO setData(String newData) {
		this.data = newData;
		return this;
	}

}
