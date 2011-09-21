package com.morgan.design.paf.domain;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author James Edward Morgan
 */
public class TableDefinition {

	private String name;
	private String fileName;
	private List<ColumnDefinition> columns;
	private boolean ignoreDuplicates = false;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public List<ColumnDefinition> getColumns() {
		return this.columns;
	}

	public Iterable<ColumnDefinition> getAllNoFillerColumns() {
		return Iterables.filter(this.columns, new Predicate<ColumnDefinition>() {
			@Override
			public boolean apply(final ColumnDefinition input) {
				return input.isNotFiller();
			}
		});
	}

	public ColumnDefinition getColumn(final int index) {
		return this.columns.get(index);
	}

	public int columnSize() {
		return this.columns.size();
	}

	public void setColumns(final List<ColumnDefinition> columns) {
		this.columns = columns;
	}

	public void setIgnoreDuplicates(final boolean ignoreDuplicates) {
		this.ignoreDuplicates = ignoreDuplicates;
	}

	public boolean getIgnoreDuplicates() {
		return this.ignoreDuplicates;
	}

	public int getTotalLineLength() {
		int length = 0;
		for (final ColumnDefinition definition : this.columns) {
			length += definition.getLength();
		}
		return length;
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
