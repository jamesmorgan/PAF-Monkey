package com.morgan.design.paf.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.DataCollector;
import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.repository.PafRepository;
import com.morgan.design.paf.util.FileLoaderUtils;
import com.morgan.design.paf.util.IterableBufferedFileReader;
import com.morgan.design.paf.util.TableDefinitionBuilder;

/**
 * @author James Edward Morgan
 */
@Service
public class PafParsingServiceImpl implements PafParsingService {

	private final Logger logger = LoggerFactory.getLogger(PafParsingServiceImpl.class);
	private final Logger verboseLogger = LoggerFactory.getLogger("com.morgan.design.verbose");

	@Resource(name = "persistenceService")
	private PafRepository pafRepository;

	@Autowired
	private ReportGenerator reportGenerator;

	@Value("${paf.load.maxBatchSize}")
	int maxBatchSize = 1000;

	@Override
	public void sourcePafFiles(final CommandLinePafArgs pafArgs) {
		final PafChangeLog changeLog = PafChangeLog.createSourceLog();
		changeLog.begin();

		// traverse directory and find data files
		final File[] dataFiles = FileLoaderUtils.loadDataFiles(pafArgs.directory);

		// create objects from definition files
		final List<TableDefinition> tableDefinitions = TableDefinitionBuilder.parseDefinitionFiles(pafArgs.definitionDirectory);
		this.logger.debug("Created {} table definitons", tableDefinitions.size());
		this.logger.debug("Table Definitons created = {}", tableDefinitions);

		final Map<TableDefinition, List<File>> tableToFilesMapping = groupFileAndDefinitions(tableDefinitions, dataFiles);

		this.logger.info("Begin table population, a total of {} files will be loaded", dataFiles.length);
		for (final TableDefinition definition : tableToFilesMapping.keySet()) {
			final int totalInsertCount = populateTable(pafArgs, definition, tableToFilesMapping.get(definition));
			this.logger.info("Completed insert of table {}, {} entries created.", definition.getName(), totalInsertCount);
			changeLog.setCount(definition, totalInsertCount);
		}
		changeLog.finish();
		this.logger.info("Finished table population");

		this.pafRepository.insertChangeLog(pafArgs, changeLog);
		this.reportGenerator.generateChangeLogReport(changeLog);
	}

	private Map<TableDefinition, List<File>> groupFileAndDefinitions(final List<TableDefinition> tableDefinitions, final File[] dataFiles) {
		final Map<TableDefinition, List<File>> tableToFilesMapping = Maps.newHashMap();
		for (final File dataFile : dataFiles) {
			final TableDefinition definition = Iterables.find(tableDefinitions, new Predicate<TableDefinition>() {
				@Override
				public boolean apply(final TableDefinition input) {
					return dataFile.getName().startsWith(input.getFileName());
				}
			});
			if (!tableToFilesMapping.containsKey(definition)) {
				tableToFilesMapping.put(definition, new ArrayList<File>());
			}
			tableToFilesMapping.get(definition).add(dataFile);
		}
		return tableToFilesMapping;
	}

	protected int populateTable(final CommandLinePafArgs pafArgs, final TableDefinition definition, final List<File> dataFiles) {
		int totalInsertCount = 0;

		final int dataFilesSize = dataFiles.size();
		for (int currentDateFileIndex = 0; currentDateFileIndex < dataFilesSize; currentDateFileIndex++) {
			final File dataFile = dataFiles.get(currentDateFileIndex);

			this.logger.debug("Inserting data file: [{}]", dataFile.getName());

			try {
				final DataCollector dataCollector = new DataCollector(dataFilesSize, currentDateFileIndex, this.maxBatchSize);
				dataCollector.setDefinition(definition);

				for (final String line : new IterableBufferedFileReader(dataFile)) {
					dataCollector.eatLine(line);
					if (dataCollector.reachedMaxBatchSize()) {
						saveBatch(pafArgs, definition, dataCollector);
						dataCollector.clearBatch();
					}
				}

				if (dataCollector.shouldRemoveFooterRow()) {
					dataCollector.removeFooterRow();
				}
				if (dataCollector.batchNotEmpty()) {
					saveBatch(pafArgs, definition, dataCollector);
				}

				totalInsertCount += dataCollector.getTotalInsertCount();
			}
			catch (final Exception e) {
				this.logger.error("Unknown Exception: ", e);
				Throwables.propagate(e);
			}
		}
		return totalInsertCount;
	}

	private void saveBatch(final CommandLinePafArgs pafArgs, final TableDefinition definition, final DataCollector dataCollector) {
		if (pafArgs.verbose) {
			this.verboseLogger.debug("Batch insert, Table=[{}], Total Count=[{}]", definition.getName(),
					dataCollector.getTotalInsertCount());
		}
		this.pafRepository.saveBatch(pafArgs, definition, dataCollector.getBatch());
	}

}
