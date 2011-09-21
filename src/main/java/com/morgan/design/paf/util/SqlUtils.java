package com.morgan.design.paf.util;

import java.util.LinkedList;

import com.google.common.collect.Lists;
import com.morgan.design.paf.domain.ColumnDefinition;
import com.morgan.design.paf.domain.TableDefinition;

public class SqlUtils {

	/**
	 * Create the batch update statement used when saving PAf data, dynamically built using the given table definition
	 */
	public static String createBatchUpdateStatement(final TableDefinition definition) {
		final int columnSize = definition.columnSize();

		// build column definition statement
		final LinkedList<String> columns = Lists.newLinkedList();
		for (int i = 0; i < columnSize; i++) {
			final ColumnDefinition column = definition.getColumn(i);
			if (column.isNotFiller()) {
				columns.add(column.getName());
			}
		}

		String columnValues = "";
		String columnDefs = "";
		for (int i = 0; i < columns.size(); i++) {
			columnDefs += String.format("`%s`", columns.get(i));
			columnValues += "?";
			if (i < (columns.size() - 1)) {
				columnDefs += ",";
				columnValues += ",";
			}
		}

		if (definition.getIgnoreDuplicates()) {
			return String.format("INSERT IGNORE INTO `%s` (%s) VALUES (%s)", definition.getName(), columnDefs, columnValues);
		}
		return String.format("INSERT INTO `%s` (%s) VALUES (%s)", definition.getName(), columnDefs, columnValues);
	}
}
