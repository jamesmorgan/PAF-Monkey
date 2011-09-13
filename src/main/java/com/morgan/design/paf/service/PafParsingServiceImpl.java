package com.morgan.design.paf.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.DataCollector;
import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.util.FileLoaderUtils;
import com.morgan.design.paf.util.IterableBufferedFileReader;

/**
 * @author James Edward Morgan
 */
@Service
public class PafParsingServiceImpl implements PafParsingService {

	private static final int BATCH_SIZE = 1000;

	private final Logger logger = LoggerFactory.getLogger(PafParsingService.class);

	@Autowired
	private PafRepository pafRepository;

	@Override
	public void updatePafFiles(final CommandLinePafArgs pafArgs) {
		// TODO GitHub issue: #3 - parse update files
		throw new UnsupportedOperationException("Not implemented yet!");
	}

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
			// TODO GitHub issue: #2 - output and save results
			changeLog.setCount(definition, totalInsertCount);
		}
		changeLog.finish();
		this.logger.info("Finished table population");

		this.pafRepository.insertChangeLog(pafArgs, changeLog);
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

	private int populateTable(final CommandLinePafArgs pafArgs, final TableDefinition definition, final List<File> dataFiles) {
		int totalInsertCount = 0;

		final int dataFilesSize = dataFiles.size();
		for (int currentDateFileIndex = 0; currentDateFileIndex < dataFilesSize; currentDateFileIndex++) {
			final File dataFile = dataFiles.get(currentDateFileIndex);

			this.logger.debug("Inserting data file: [{}]", dataFile.getName());

			try {
				final IterableBufferedFileReader fileReader = new IterableBufferedFileReader(dataFile);

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
						this.pafRepository.saveBatch(pafArgs, definition, dataCollector.getBatch());
						batchCount = 0;
						dataCollector.clearBatch();
					}
				}

				if (dataCollector.isFirstOrLastInSeries()) {
					dataCollector.removeFooterRow();
				}
				this.pafRepository.saveBatch(pafArgs, definition, dataCollector.getBatch());
			}
			catch (final Exception e) {
				// TODO GitHub issue: #1 - add error reporting
				this.logger.error("Unknown Exception: ", e);
				Throwables.propagate(e);
			}
		}
		return totalInsertCount;
	}

}
