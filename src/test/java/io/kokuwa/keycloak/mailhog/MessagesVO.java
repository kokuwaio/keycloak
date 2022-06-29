package io.kokuwa.keycloak.mailhog;

import java.util.List;
import java.util.Objects;

public class MessagesVO {

	public static final String JSON_PROPERTY_TOTAL = "total";
	public static final String JSON_PROPERTY_START = "start";
	public static final String JSON_PROPERTY_COUNT = "count";
	public static final String JSON_PROPERTY_ITEMS = "items";

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_TOTAL)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private java.lang.Long total;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_START)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private java.lang.Long start;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_COUNT)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private java.lang.Long count;

	@com.fasterxml.jackson.annotation.JsonProperty(JSON_PROPERTY_ITEMS)
	@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private List<MessageVO> items;

	// methods

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		MessagesVO other = (MessagesVO) object;
		return Objects.equals(total, other.total)
				&& Objects.equals(start, other.start)
				&& Objects.equals(count, other.count)
				&& Objects.equals(items, other.items);
	}

	@Override
	public int hashCode() {
		return Objects.hash(total, start, count, items);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("MessagesVO[")
				.append("total=").append(total).append(",")
				.append("start=").append(start).append(",")
				.append("count=").append(count).append(",")
				.append("items=").append(items)
				.append("]")
				.toString();
	}

	// getter/setter

	public java.lang.Long getTotal() {
		return total;
	}

	public MessagesVO setTotal(java.lang.Long newTotal) {
		this.total = newTotal;
		return this;
	}

	public java.lang.Long getStart() {
		return start;
	}

	public MessagesVO setStart(java.lang.Long newStart) {
		this.start = newStart;
		return this;
	}

	public java.lang.Long getCount() {
		return count;
	}

	public MessagesVO setCount(java.lang.Long newCount) {
		this.count = newCount;
		return this;
	}

	public List<MessageVO> getItems() {
		return items;
	}

	public MessagesVO setItems(List<MessageVO> newItems) {
		this.items = newItems;
		return this;
	}
}
