package com.morgan.design.paf.domain;

import java.util.List;

import com.google.common.collect.Lists;

public class DataCollector {

	private final List<Object[]> parameters = Lists.newArrayList();

	private boolean removedFirstRow = false;

	private final int currentDataFileIndex;
	private final int totalDataFilesCount;
	private final int maxBatchSize;

	private int totalInsertCount;
	private int batchCount = 0;
	private TableDefinition definition;

	public DataCollector(final int totalDataFilesCount, final int currentDataFileIndex, final int maxBatchSize) {
		this.totalDataFilesCount = totalDataFilesCount;
		this.currentDataFileIndex = currentDataFileIndex;
		this.maxBatchSize = maxBatchSize;
	}

	public boolean batchNotEmpty() {
		if (null == this.parameters) {
			return true;
		}
		return !this.parameters.isEmpty();
	}

	public void clearBatch() {
		this.batchCount = 0;
		this.parameters.clear();
	}

	public void collectLineData(final List<Object> paramValues) {
		this.parameters.add(paramValues.toArray());
	}

	public void eatLine(final String workingLine) {
		final int currentTotalLineLength = this.definition.getTotalLineLength();
		final List<Object> paramValues = Lists.newArrayList();
		int currentCharIndex = 0;
		for (final ColumnDefinition column : this.definition.getColumns()) {
			if (confirmValidDataLine(currentTotalLineLength, workingLine, column)) {
				paramValues.add(extractColumnData(workingLine, currentCharIndex, column));
				this.totalInsertCount++;
			}
			currentCharIndex += column.getLength();
		}
		collectLineData(paramValues);
		this.batchCount++;

		if (notRemovedHeaderRow()) {
			removeHeaderRow();
		}
	}

	public List<Object[]> getBatch() {
		return this.parameters;
	}

	public final int getTotalInsertCount() {
		return this.totalInsertCount;
	}

	public boolean notRemovedHeaderRow() {
		return !this.removedFirstRow && this.currentDataFileIndex == 0;
	}

	public boolean reachedMaxBatchSize() {
		return this.batchCount == this.maxBatchSize;
	}

	public void removeFooterRow() {
		if (this.parameters.size() != 0) {
			this.totalInsertCount--;
			this.parameters.remove(this.parameters.size() - 1);
		}
	}

	public void removeHeaderRow() {
		this.parameters.remove(0);
		this.removedFirstRow = true;
		this.batchCount--;
		this.totalInsertCount--;
	}

	public void setDefinition(final TableDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Only remove the footer row if last in a series
	 */
	public boolean shouldRemoveFooterRow() {
		return this.currentDataFileIndex == this.totalDataFilesCount - 1;
	}

	/**
	 * Ensure the working line is the exact size of expected data and that current column is not a filler column
	 */
	private boolean confirmValidDataLine(final int currentTotalLineLength, final String line, final ColumnDefinition column) {
		return column.isNotFiller() && line.length() == currentTotalLineLength;
	}

	private Object extractColumnData(final String line, final int currentCharIndex, final ColumnDefinition column) {
		final String paramValue = line.substring(currentCharIndex, currentCharIndex + column.getLength());
		return column.isVarChar()
				? (String) paramValue.trim()
				: extractIntValue(column, paramValue);
	}

	private Integer extractIntValue(final ColumnDefinition column, final String paramValue) {
		try {
			final Integer intVal = Integer.valueOf(paramValue);
			return column.isNullable() && 0 == intVal
					? null
					: intVal;
		}
		catch (final NumberFormatException e) {
			return column.isNullable()
					? null
					: 0;
		}
	}
}
