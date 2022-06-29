package io.kokuwa.keycloak.mailhog;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageVO {

	public static final String JSON_PROPERTY_I_D = "ID";
	public static final String JSON_PROPERTY_FROM = "From";
	public static final String JSON_PROPERTY_TO = "To";
	public static final String JSON_PROPERTY_CONTENT = "Content";
	public static final String JSON_PROPERTY_CREATED = "Created";
	public static final String JSON_PROPERTY_RAW = "Raw";

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_I_D)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private String ID;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_FROM)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private PathVO from;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_TO)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private List<PathVO> to;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_CONTENT)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private MessageContentVO content;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_RAW)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private MessageRawVO raw;

	// methods

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		MessageVO other = (MessageVO) object;
		return Objects.equals(ID, other.ID)
				&& Objects.equals(from, other.from)
				&& Objects.equals(to, other.to)
				&& Objects.equals(content, other.content)
				&& Objects.equals(raw, other.raw);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID, from, to, content, raw);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("MessageVO[")
				.append("ID=").append(ID).append(",")
				.append("from=").append(from).append(",")
				.append("to=").append(to).append(",")
				.append("content=").append(content).append(",")
				.append("raw=").append(raw)
				.append("]")
				.toString();
	}

	// getter/setter

	public String getID() {
		return ID;
	}

	public MessageVO setID(String newID) {
		this.ID = newID;
		return this;
	}

	public PathVO getFrom() {
		return from;
	}

	public MessageVO setFrom(PathVO newFrom) {
		this.from = newFrom;
		return this;
	}

	public List<PathVO> getTo() {
		return to;
	}

	public MessageVO setTo(List<PathVO> newTo) {
		this.to = newTo;
		return this;
	}

	public MessageContentVO getContent() {
		return content;
	}

	public MessageVO setContent(MessageContentVO newContent) {
		this.content = newContent;
		return this;
	}

	public MessageRawVO getRaw() {
		return raw;
	}

	public MessageVO setRaw(MessageRawVO newRaw) {
		this.raw = newRaw;
		return this;
	}
}
