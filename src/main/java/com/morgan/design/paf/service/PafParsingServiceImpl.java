package com.morgan.design.paf.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.google.common.base.Throwables;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.DataCollector;
import com.morgan.design.paf.domain.PafChangeLog;
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

	private static final int BATCH_SIZE = 1000;

	private final Logger logger = LoggerFactory.getLogger(PafParsingService.class);

	@Autowired
	private BasicDataSource dataSource;

	private SimpleJdbcOperations pafJdbcOperations;

	@Override
	public void updatePafFiles(final CommandLinePafArgs pafArgs) {
		constructDateSource(pafArgs);
		// TODO GitHub issue: #3 - parse update files
		this.pafJdbcOperations = new SimpleJdbcTemplate(this.dataSource);
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public void sourcePafFiles(final CommandLinePafArgs pafArgs) {
		final PafChangeLog changeLog = PafChangeLog.createSourceLog();
		changeLog.begin();

		constructDateSource(pafArgs);
		this.pafJdbcOperations = new SimpleJdbcTemplate(this.dataSource);

		// traverse directory and find data files
		final File[] dataFiles = FileLoaderUtils.retrieveFiles(pafArgs.directory, FileLoaderUtils.getDataFileFilter());
		if (filesDontExist(dataFiles)) {
			throw new NoDataFilesFoundException(pafArgs.directory);
		}

		// if files found look up related definition file
		final File[] definitionFiles =
				FileLoaderUtils.retrieveFiles(pafArgs.definitionDirectory, FileLoaderUtils.getDefinitionFileFilter());
		if (filesDontExist(definitionFiles)) {
			throw new NoDefinitionFilesFoundException(pafArgs.definitionDirectory);
		}

		// create objects from definition files
		final List<TableDefinition> tableDefinitions = TableDefinitionBuilder.parseDefinitionFiles(definitionFiles);
		this.logger.debug("Created {} table definitons", tableDefinitions.size());
		this.logger.debug("Table Definitons created = {}", tableDefinitions);

		final BiMap<TableDefinition, List<File>> tableToFilesMapping = HashBiMap.create();

		// Group Files and Definitions : read file(s) found, pass to parser
		for (final File dataFile : dataFiles) {
			final TableDefinition definition = Iterables.find(tableDefinitions, tableToFileNamePredicate(dataFile));
			if (!tableToFilesMapping.containsKey(definition)) {
				tableToFilesMapping.put(definition, new ArrayList<File>());
			}
			tableToFilesMapping.get(definition).add(dataFile);
		}

		this.logger.info("Begin table population, a total of {} files will be loaded", dataFiles.length);
		for (final TableDefinition definition : tableToFilesMapping.keySet()) {
			final int totalInsertCount = populateTable(definition, tableToFilesMapping.get(definition));
			this.logger.info("Completed insert of table {}, {} entries created.", definition.getName(), totalInsertCount);
			// TODO GitHub issue: #2 - output and save results
			changeLog.setCount(definition, totalInsertCount);
		}
		this.logger.info("Finished table population");

		changeLog.finish();

		insertChangeLog(changeLog);
	}

	private void insertChangeLog(final PafChangeLog changeLog) {
		final Map<String, Object> params = Maps.newHashMap();
		params.put("mode", changeLog.getMode());
		params.put("buildingNames", changeLog.getBuildNames());
		params.put("localities", changeLog.getLocalities());
		params.put("mailSort", changeLog.getMailSort());
		params.put("pafAddress", changeLog.getPafAddress());
		params.put("organisations", changeLog.getOrganisations());
		params.put("subBuildingName", changeLog.getSubBuildingName());
		params.put("thoroughfare", changeLog.getThoroughfare());
		params.put("thoroughfareDescriptor", changeLog.getThoroughfareDescriptor());
		params.put("udprn", changeLog.getUdprn());
		params.put("endDate", date(changeLog.getEndDate()));
		params.put("startDate", date(changeLog.getStartDate()));

		this.pafJdbcOperations.update("INSERT INTO `paf_change_log` SET "
			+ "`StartDate`=:startDate, `EndDate`=:endDate, `Mode`=:mode, `BuildNames`=:buildingNames, `Localities`=:localities, "
			+ "`MailSort`=:mailSort, `Organisations`=:organisations, `PafAddress`=:pafAddress, `SubBuildingName`=:subBuildingName, "
			+ "`ThoroughfareDescriptor`=:thoroughfareDescriptor, `Thoroughfare`=:thoroughfare, `Udprn`=:udprn", params);
	}

	private String date(final Date value) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(value);
	}

	private int populateTable(final TableDefinition definition, final List<File> dataFiles) {
		int totalInsertCount = 0;

		final int dataFilesSize = dataFiles.size();
		for (int currentDateFileIndex = 0; currentDateFileIndex < dataFilesSize; currentDateFileIndex++) {
			final File dataFile = dataFiles.get(currentDateFileIndex);

			this.logger.debug("Inserting data file: [{}]", dataFile.getName());

			try {
				final IterableBufferedFileReader fileReader = new IterableBufferedFileReader(dataFile);

				final String batchUpdate = SqlUtils.createBatchUpdateStatement(definition);
				this.logger.debug("Insert statement: {}", batchUpdate);

				final DataCollector dataCollector = new DataCollector(dataFilesSize, currentDateFileIndex);

				int batchCount = 0;
				for (final String line : fileReader) {
					dataCollector.eatLine(definition, line);
					if (dataCollector.notRemovedHeaderRow()) {
						dataCollector.removeHeaderRow();
					}
					batchCount++;
					totalInsertCount++;

					if (batchCount == BATCH_SIZE) {
						this.logger.debug("Batch insert, Table=[{}], Total Count=[{}]", definition.getName(), totalInsertCount);
						this.pafJdbcOperations.batchUpdate(batchUpdate, dataCollector.getBatch());
						batchCount = 0;
						dataCollector.clearBatch();
					}
				}

				if (dataCollector.isFirstOrLastInSeries()) {
					dataCollector.removeFooterRow();
				}
				this.pafJdbcOperations.batchUpdate(batchUpdate, dataCollector.getBatch());
			}
			catch (final Exception e) {
				// TODO GitHub issue: #1 - add error reporting
				this.logger.error("Unknown Exception: ", e);
				Throwables.propagate(e);
			}
		}
		return totalInsertCount;
	}

	private Predicate<TableDefinition> tableToFileNamePredicate(final File dataFile) {
		return new Predicate<TableDefinition>() {
			@Override
			public boolean apply(final TableDefinition input) {
				return dataFile.getName().startsWith(input.getFileName());
			}
		};
	}

	private boolean filesDontExist(final File[] files) {
		return null == files || 0 == files.length;
	}

	private void constructDateSource(final CommandLinePafArgs pafArgs) {
		this.dataSource.setUsername(pafArgs.username);
		this.dataSource.setPassword(pafArgs.password);
		this.dataSource.setUrl("jdbc:mysql://" + pafArgs.host + ":3306/" + pafArgs.schema
			+ "?autoReconnect=true&amp;rewriteBatchedStatements=false&amp;allowMultiQueries=true&amp;zeroDateTimeBehavior=convertToNull");
	}
}
