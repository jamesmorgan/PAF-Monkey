package com.morgan.design.paf.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.ColumnDefinition;
import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.exception.NoDataFilesFoundException;
import com.morgan.design.paf.exception.NoDefinitionFilesFoundException;
import com.morgan.design.paf.util.FileLoaderUtils;
import com.morgan.design.paf.util.IterableBufferedFileReader;
import com.morgan.design.paf.util.SqlUtils;

/**
 * @author James Edward Morgan
 */
@Service
public class PafParsingServiceImpl implements PafParsingService {

	private final Logger logger = LoggerFactory.getLogger(PafParsingService.class);

	@Autowired
	private BasicDataSource dataSource;

	private SimpleJdbcOperations pafJdbcOperations;

	@Override
	public void updatePafFiles(final CommandLinePafArgs pafArgs) {
		constructDateSource(pafArgs);
		this.pafJdbcOperations = new SimpleJdbcTemplate(this.dataSource);
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public void sourcePafFiles(final CommandLinePafArgs pafArgs) {
		constructDateSource(pafArgs);
		this.pafJdbcOperations = new SimpleJdbcTemplate(this.dataSource);

		// traverse directory and find data files
		final File[] dataFiles = FileLoaderUtils.retrieveFiles(pafArgs.directory, FileLoaderUtils.getDataFileFilter());
		validateDataFiles(pafArgs, dataFiles);

		// if files found look up related definition file
		final File[] definitionFiles =
				FileLoaderUtils.retrieveFiles(pafArgs.definitionDirectory, FileLoaderUtils.getDefinitionFileFilter());
		validateDefinitionFiles(pafArgs, definitionFiles);

		// create objects from definition files
		final List<TableDefinition> tableDefinitions = TableDefinitionBuilder.parseDefinitionFiles(definitionFiles);
		this.logger.debug("Created {} table definitons", tableDefinitions.size());
		this.logger.debug("Table Definitons created = {}", tableDefinitions);

		final Map<TableDefinition, List<File>> tableToFilesMapping = Maps.newHashMap();

		// Group Files and Definitions : read file(s) found, pass to parser
		for (final File dataFile : dataFiles) {
			final TableDefinition definition = Iterables.find(tableDefinitions, tableToFileNamePredicate(dataFile));
			if (!tableToFilesMapping.containsKey(definition)) {
				tableToFilesMapping.put(definition, new ArrayList<File>());
			}
			tableToFilesMapping.get(definition).add(dataFile);
		}

		for (final TableDefinition definitions : tableToFilesMapping.keySet()) {
			populateTable(definitions, tableToFilesMapping.get(definitions));
		}
	}

	private void populateTable(final TableDefinition definition, final List<File> dataFiles) {
		final List<ColumnDefinition> columns = definition.getColumns();
		for (int dataFileIndex = 0; dataFileIndex < dataFiles.size(); dataFileIndex++) {
			final File dataFile = dataFiles.get(dataFileIndex);
			this.logger.debug("Inserting data file: [{}]", dataFile.getName());

			try {
				final IterableBufferedFileReader fileReader = new IterableBufferedFileReader(dataFile);

				final String batchUpdate = SqlUtils.createBatchUpdateStatement(definition);
				this.logger.debug("Insert statement: {}", batchUpdate);

				int batchCount = 0;
				int totalInsertCount = 0;
				boolean removedFirstRow = false;
				final int currentTotalLineLength = definition.getTotalLineLength();

				List<Object[]> parameters = Lists.newArrayList();
				for (final String line : fileReader) {
					final List<Object> paramValues = Lists.newArrayList();
					int currentCharIndex = 0;
					for (final ColumnDefinition column : columns) {
						if (confirmValidDataLine(currentTotalLineLength, line, column)) {
							paramValues.add(line.substring(currentCharIndex, currentCharIndex + column.getLength()).trim());
						}
						currentCharIndex += column.getLength();
					}

					parameters.add(paramValues.toArray());

					if (shouldRemoveHeaderRow(dataFiles, dataFileIndex, removedFirstRow)) {
						removeHeaderRow(parameters);
						removedFirstRow = true;
					}

					batchCount++;
					totalInsertCount++;

					if (batchCount == 1000) {
						this.logger.debug("Batch insert, Table=[{}], Total Count=[{}]", definition.getName(), totalInsertCount);
						this.pafJdbcOperations.batchUpdate(batchUpdate, parameters);
						batchCount = 0;
						parameters = Lists.newArrayList();
					}
				}

				if (shouldRemoveFooterRow(dataFiles, dataFileIndex)) {
					removeFooterRow(parameters);
				}
				this.pafJdbcOperations.batchUpdate(batchUpdate, parameters);
			}
			catch (final Exception e) {
				this.logger.error("Unknown Exception: ", e);
			}
		}
	}

	/**
	 * Ensure the working line is the exact size of expected data and that current column is not a filler column
	 */
	private boolean confirmValidDataLine(final int currentTotalLineLength, final String line, final ColumnDefinition column) {
		return column.isNotFiller() && line.length() == currentTotalLineLength;
	}

	/**
	 * Only remove the footer row if one data file or the last in a series
	 */
	private boolean shouldRemoveFooterRow(final List<File> dataFiles, final int dataFileIndex) {
		return 1 == dataFiles.size() || dataFileIndex == dataFiles.size() - 1;
	}

	/**
	 * Only remove the header row if one data file or the first in a series
	 */
	private boolean shouldRemoveHeaderRow(final List<File> dataFiles, final int dataFileIndex, final boolean removedFirstRow) {
		return !removedFirstRow && (1 == dataFiles.size() || dataFileIndex == 0);
	}

	private Predicate<TableDefinition> tableToFileNamePredicate(final File dataFile) {
		return new Predicate<TableDefinition>() {
			@Override
			public boolean apply(final TableDefinition input) {
				return dataFile.getName().startsWith(input.getFileName());
			}
		};
	}

	private void removeHeaderRow(final List<Object[]> parameters) {
		parameters.remove(0);
	}

	private void removeFooterRow(final List<Object[]> parameters) {
		parameters.remove(parameters.size() - 1);
	}

	private void validateDataFiles(final CommandLinePafArgs pafArgs, final File[] dataFiles) {
		if (validateFiles(dataFiles)) {
			throw new NoDataFilesFoundException(pafArgs.directory);
		}
	}

	private void validateDefinitionFiles(final CommandLinePafArgs pafArgs, final File[] definitionFiles) {
		if (validateFiles(definitionFiles)) {
			throw new NoDefinitionFilesFoundException(pafArgs.definitionDirectory);
		}
	}

	private boolean validateFiles(final File[] files) {
		return null == files || 0 == files.length;
	}

	private void constructDateSource(final CommandLinePafArgs pafArgs) {
		this.dataSource.setUsername(pafArgs.username);
		this.dataSource.setPassword(pafArgs.password);
		this.dataSource.setUrl("jdbc:mysql://" + pafArgs.host + ":3306/" + pafArgs.schema
			+ "?autoReconnect=true&amp;rewriteBatchedStatements=false&amp;allowMultiQueries=true&amp;zeroDateTimeBehavior=convertToNull");
	}
}
