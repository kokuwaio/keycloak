package io.kokuwa.keycloak.mailhog;

import java.util.List;
import java.util.Objects;

public class PathVO {

	public static final String JSON_PROPERTY_RELAYS = "Relays";
	public static final String JSON_PROPERTY_MAILBOX = "Mailbox";
	public static final String JSON_PROPERTY_DOMAIN = "Domain";
	public static final String JSON_PROPERTY_PARAMS = "Params";

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_RELAYS)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private List<String> relays;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_MAILBOX)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private String mailbox;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_DOMAIN)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private String domain;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_PARAMS)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private String params;

	// methods

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		PathVO other = (PathVO) object;
		return Objects.equals(relays, other.relays)
				&& Objects.equals(mailbox, other.mailbox)
				&& Objects.equals(domain, other.domain)
				&& Objects.equals(params, other.params);
	}

	@Override
	public int hashCode() {
		return Objects.hash(relays, mailbox, domain, params);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("PathVO[")
				.append("relays=").append(relays).append(",")
				.append("mailbox=").append(mailbox).append(",")
				.append("domain=").append(domain).append(",")
				.append("params=").append(params)
				.append("]")
				.toString();
	}

	// getter/setter

	public List<String> getRelays() {
		return relays;
	}

	public PathVO setRelays(List<String> newRelays) {
		this.relays = newRelays;
		return this;
	}

	public String getMailbox() {
		return mailbox;
	}

	public PathVO setMailbox(String newMailbox) {
		this.mailbox = newMailbox;
		return this;
	}

	public String getDomain() {
		return domain;
	}

	public PathVO setDomain(String newDomain) {
		this.domain = newDomain;
		return this;
	}

	public String getParams() {
		return params;
	}

	public PathVO setParams(String newParams) {
		this.params = newParams;
		return this;
	}
}
