package com.morgan.design.paf.domain;

import java.util.List;

import com.google.common.collect.Lists;

public class DataCollector {

	private final List<Object[]> parameters = Lists.newArrayList();

	private boolean removedFirstRow = false;

	private final int currentDataFileIndex;
	private final int totalDataFilesCount;

	public DataCollector(final int totalDataFilesCount, final int currentDataFileIndex) {
		this.totalDataFilesCount = totalDataFilesCount;
		this.currentDataFileIndex = currentDataFileIndex;
	}

	public void removeHeaderRow() {
		this.parameters.remove(0);
		this.removedFirstRow = true;
	}

	public void removeFooterRow() {
		if (this.parameters.size() != 0) {
			this.parameters.remove(this.parameters.size() - 1);
			this.removedFirstRow = true;
		}
	}

	public void collectLineData(final List<Object> paramValues) {
		this.parameters.add(paramValues.toArray());
	}

	public void clearBatch() {
		this.parameters.clear();
	}

	public List<Object[]> getBatch() {
		return this.parameters;
	}

	public void eatLine(final TableDefinition definition, final String workingLine) {
		final int currentTotalLineLength = definition.getTotalLineLength();
		final List<Object> paramValues = Lists.newArrayList();
		int currentCharIndex = 0;
		for (final ColumnDefinition column : definition.getColumns()) {
			if (confirmValidDataLine(currentTotalLineLength, workingLine, column)) {
				paramValues.add(extractColumnData(workingLine, currentCharIndex, column));
			}
			currentCharIndex += column.getLength();
		}
		collectLineData(paramValues);
	}

	/**
	 * Ensure the working line is the exact size of expected data and that current column is not a filler column
	 */
	private boolean confirmValidDataLine(final int currentTotalLineLength, final String line, final ColumnDefinition column) {
		return column.isNotFiller() && line.length() == currentTotalLineLength;
	}

	private Object extractColumnData(final String line, final int currentCharIndex, final ColumnDefinition column) {
		final String paramValue = line.substring(currentCharIndex, currentCharIndex + column.getLength());
		try {
			return column.isAlphaNumeric()
					? (String) paramValue.trim()
					: Integer.valueOf(paramValue);
		}
		catch (final NumberFormatException e) {
			return 0;
		}
	}

	public boolean notRemovedHeaderRow() {
		return !this.removedFirstRow && this.currentDataFileIndex == 0;
	}

	/**
	 * Only remove the footer row if last in a series
	 */
	public boolean shouldRemoveFooterRow() {
		return this.currentDataFileIndex == this.totalDataFilesCount - 1;
	}

	public boolean batchNotEmpty() {
		if (null == this.parameters) {
			return true;
		}
		return !this.parameters.isEmpty();
	}
}
