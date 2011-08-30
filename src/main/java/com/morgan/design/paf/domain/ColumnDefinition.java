package com.morgan.design.paf.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author James Edward Morgan
 */
public class ColumnDefinition {

	private String name;
	private int length;
	private String type;

	public static ColumnDefinition create(final String name, final int length, final String type) {
		final ColumnDefinition definition = new ColumnDefinition();
		definition.setLength(length);
		definition.setName(name);
		definition.setType(type);
		return definition;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(final int length) {
		this.length = length;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public boolean isAlphaNumeric() {
		return this.type.equals("A");
	}

	public boolean isNotFiller() {
		return !this.name.equalsIgnoreCase("Filler");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(final Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
