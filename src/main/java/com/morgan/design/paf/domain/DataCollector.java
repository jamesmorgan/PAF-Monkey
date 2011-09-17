package com.morgan.design.paf.domain;

import java.util.List;

import com.google.common.collect.Lists;

public class DataCollector {

	private List<Object[]> parameters = Lists.newArrayList();

	private boolean removedFirstRow = false;

	private final int dataFileIndex;
	private final int dataFilesSize;

	public DataCollector(final int dataFilesSize, final int dataFileIndex) {
		this.dataFilesSize = dataFilesSize;
		this.dataFileIndex = dataFileIndex;
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
		this.parameters = Lists.newArrayList();
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

	private String extractColumnData(final String line, final int currentCharIndex, final ColumnDefinition column) {
		return line.substring(currentCharIndex, currentCharIndex + column.getLength()).trim();
	}

	public boolean notRemovedHeaderRow() {
		return !this.removedFirstRow && isFirstOrLastInSeries();
	}

	/**
	 * Only remove the footer row if one data file or the last in a series
	 */
	public boolean isFirstOrLastInSeries() {
		return 1 == this.dataFilesSize || this.dataFileIndex == this.dataFilesSize - 1;
	}
}
