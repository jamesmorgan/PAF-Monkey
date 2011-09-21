package com.morgan.design.paf.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.beust.jcommander.internal.Lists;
import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.repository.PafRepository;
import com.morgan.design.paf.util.TableDefinitionBuilder;

@SuppressWarnings({ "synthetic-access", "unqualified-field-access" })
public class PafParsingSeviceImplUnitTest {

	Mockery context = new JUnit4Mockery();

	protected PafParsingServiceImpl pafParsingSevice;

	private PafRepository pafRepository;
	private ReportGenerator reportGenerator;

	@Before
	public void setUp() {
		this.pafParsingSevice = new PafParsingServiceImpl();

		this.pafRepository = this.context.mock(PafRepository.class);
		this.reportGenerator = this.context.mock(ReportGenerator.class);

		ReflectionTestUtils.setField(this.pafParsingSevice, "pafRepository", this.pafRepository);
		ReflectionTestUtils.setField(this.pafParsingSevice, "reportGenerator", this.reportGenerator);
	}

	@Test
	public void shouldCorrectlyParseMultipleUdprnFiles() {
		final TableDefinition udprnDef =
				TableDefinitionBuilder.loadTableDefinition(new File("src\\main\\resources\\definitions\\udprn.xml"));

		final CommandLinePafArgs args = new CommandLinePafArgs();
		args.definitionDirectory = "src\\main\\resources\\definitions\\";
		args.directory = "src\\test\\resources\\test_data\\data\\parsing\\udprn\\";

		final List<Object[]> batch1 = Lists.newArrayList();
		batch1.add(new Object[] { "AB101AA", 1896311, 50464672, 0, "L", "1A", "" });
		batch1.add(new Object[] { "AB101AB", 1896312, 52447276, 0, "L", "1A", "" });
		batch1.add(new Object[] { "LE100LD", 17421193, 13275003, 0, "S", "2E", "" });

		final List<Object[]> batch2 = Lists.newArrayList();
		batch2.add(new Object[] { "LE100LD", 17421194, 13275004, 0, "S", "2F", "" });

		final List<Object[]> batch3 = Lists.newArrayList();
		batch3.add(new Object[] { "WN1 2HJ", 15031853, 26592342, 0, "S", "1J", "" });
		batch3.add(new Object[] { "ZE3 9JZ", 11170275, 27478155, 0, "S", "2E", "" });

		this.context.checking(new Expectations() {
			{
				one(pafRepository).saveBatch(with(args), with(udprnDef), with(isObjectArray(batch1)));
				one(pafRepository).saveBatch(with(args), with(udprnDef), with(isObjectArray(batch2)));
				one(pafRepository).saveBatch(with(args), with(udprnDef), with(isObjectArray(batch3)));

				one(pafRepository).insertChangeLog(with(args), with(any(PafChangeLog.class)));
				one(reportGenerator).generateChangeLogReport(with(any(PafChangeLog.class)));
			}
		});
		this.pafParsingSevice.sourcePafFiles(args);
	}

	@Test
	public void shouldCorrectlyParseMultipleAddressFiles() {
		final TableDefinition addressDef =
				TableDefinitionBuilder.loadTableDefinition(new File("src\\main\\resources\\definitions\\address.xml"));

		final CommandLinePafArgs args = new CommandLinePafArgs();
		args.definitionDirectory = "src\\main\\resources\\definitions\\";
		args.directory = "src\\test\\resources\\test_data\\data\\parsing\\address\\";

		final List<Object[]> batch1 = Lists.newArrayList();
		batch1.add(new Object[] { "AB10", "1AA", 1896311, 2, 217, 2, 0, 0, 0, 974383, 0, 1, 0, "L", "", "1A", "", "" });
		batch1.add(new Object[] { "AB10", "1AB", 1896312, 2, 217, 2, 0, 0, 0, 974385, 0, 1, 0, "L", "", "1A", "", "" });

		final List<Object[]> batch2 = Lists.newArrayList();
		batch2.add(new Object[] { "DA1", "5JF", 4976886, 14979, 1039, 12, 0, 0, 1, 0, 0, 1, 0, "S", "", "1A", "", "" });
		batch2.add(new Object[] { "DA1", "5JF", 4976887, 14979, 1039, 12, 0, 0, 101, 0, 0, 1, 0, "S", "", "1B", "", "" });
		batch2.add(new Object[] { "DA1", "5JF", 4976889, 14979, 1039, 12, 0, 0, 105, 0, 0, 1, 0, "S", "", "1E", "", "" });

		final List<Object[]> batch3 = Lists.newArrayList();
		batch3.add(new Object[] { "KY6", "3HN", 28255102, 10625, 170065, 1, 0, 0, 0, 31483, 0, 1, 0, "S", "", "1B", "", "" });
		batch3.add(new Object[] { "KY6", "3HN", 28255103, 10625, 170065, 1, 0, 0, 0, 1558055, 0, 1, 0, "S", "", "1D", "", "" });
		batch3.add(new Object[] { "KY6", "3HN", 28508359, 10625, 170065, 1, 0, 0, 0, 8268, 0, 1, 0, "S", "", "1E", "", "" });

		final List<Object[]> batch4 = Lists.newArrayList();
		batch4.add(new Object[] { "TN31", "7US", 33705088, 9956, 6704, 5, 0, 0, 1, 0, 0, 1, 0, "S", "", "1A", "", "" });
		batch4.add(new Object[] { "ZE3", "9JZ", 11170274, 14412, 2141, 7, 0, 0, 8, 0, 0, 0, 0, "S", "", "2D", "", "" });
		batch4.add(new Object[] { "ZE3", "9JZ", 11170275, 14412, 2141, 7, 0, 0, 9, 0, 0, 0, 0, "S", "", "2E", "", "" });

		this.context.checking(new Expectations() {
			{
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch1)));
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch2)));
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch3)));
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch4)));

				one(pafRepository).insertChangeLog(with(args), with(any(PafChangeLog.class)));
				one(reportGenerator).generateChangeLogReport(with(any(PafChangeLog.class)));
			}
		});
		this.pafParsingSevice.sourcePafFiles(args);
	}

	@Test
	public void shouldCorrectlyParseSingleBuilingNamesFile() {
		final TableDefinition addressDef =
				TableDefinitionBuilder.loadTableDefinition(new File("src\\main\\resources\\definitions\\building_names.xml"));

		final CommandLinePafArgs args = new CommandLinePafArgs();
		args.definitionDirectory = "src\\main\\resources\\definitions\\";
		args.directory = "src\\test\\resources\\test_data\\data\\parsing\\building_names\\";

		final List<Object[]> batch = Lists.newArrayList();
		batch.add(new Object[] { 1, "23A" });
		batch.add(new Object[] { 2, "12A" });
		batch.add(new Object[] { 3, "20A" });
		batch.add(new Object[] { 4, "30A" });
		batch.add(new Object[] { 5, "8A" });
		batch.add(new Object[] { 2360366, "5 RAVENS WOOD" });
		batch.add(new Object[] { 2360367, "6 RAVENS WOOD" });
		batch.add(new Object[] { 2360368, "7 RAVENS WOOD" });
		batch.add(new Object[] { 2360369, "8 RAVENS WOOD" });
		batch.add(new Object[] { 2360370, "9 RAVENS WOOD" });

		this.context.checking(new Expectations() {
			{
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch)));
				one(pafRepository).insertChangeLog(with(args), with(any(PafChangeLog.class)));
				one(reportGenerator).generateChangeLogReport(with(any(PafChangeLog.class)));
			}
		});
		this.pafParsingSevice.sourcePafFiles(args);
	}

	@Test
	public void shouldCorrectlyParseSingleBuilingNamesFileWithBatchSizeOfTwo() {
		// Set batch size to 2
		ReflectionTestUtils.setField(pafParsingSevice, "maxBatchSize", 2);

		final TableDefinition addressDef =
				TableDefinitionBuilder.loadTableDefinition(new File("src\\main\\resources\\definitions\\building_names.xml"));

		final CommandLinePafArgs args = new CommandLinePafArgs();
		args.definitionDirectory = "src\\main\\resources\\definitions\\";
		args.directory = "src\\test\\resources\\test_data\\data\\parsing\\building_names\\";

		final List<Object[]> batch1 = Lists.newArrayList();
		batch1.add(new Object[] { 1, "23A" });
		batch1.add(new Object[] { 2, "12A" });

		final List<Object[]> batch2 = Lists.newArrayList();
		batch2.add(new Object[] { 3, "20A" });
		batch2.add(new Object[] { 4, "30A" });

		final List<Object[]> batch3 = Lists.newArrayList();
		batch3.add(new Object[] { 5, "8A" });
		batch3.add(new Object[] { 2360366, "5 RAVENS WOOD" });

		final List<Object[]> batch4 = Lists.newArrayList();
		batch4.add(new Object[] { 2360367, "6 RAVENS WOOD" });
		batch4.add(new Object[] { 2360368, "7 RAVENS WOOD" });

		final List<Object[]> batch5 = Lists.newArrayList();
		batch5.add(new Object[] { 2360369, "8 RAVENS WOOD" });
		batch5.add(new Object[] { 2360370, "9 RAVENS WOOD" });

		this.context.checking(new Expectations() {
			{
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch1)));
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch2)));
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch3)));
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch4)));
				one(pafRepository).saveBatch(with(args), with(addressDef), with(isObjectArray(batch5)));
				one(pafRepository).insertChangeLog(with(args), with(any(PafChangeLog.class)));
				one(reportGenerator).generateChangeLogReport(with(any(PafChangeLog.class)));
			}
		});
		this.pafParsingSevice.sourcePafFiles(args);
	}

	public static Matcher<List<Object[]>> isObjectArray(final List<Object[]> batch) {
		return new ObjectArrayMatcher(batch);
	}

	private static class ObjectArrayMatcher extends BaseMatcher<List<Object[]>> {

		private final List<Object[]> batch;

		public ObjectArrayMatcher(final List<Object[]> batch) {
			this.batch = batch;
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean matches(final Object arg) {
			if (!(arg instanceof List)) {
				return false;
			}
			final List<Object[]> converted = (List<Object[]>) arg;

			if (converted.size() != batch.size()) {
				return false;
			}

			for (int i = 0; i < converted.size(); i++) {
				assertThat(converted.get(i), is(batch.get(i)));
			}
			return true;
		}

		@Override
		public void describeTo(final Description arg) {
			//
		}
	}
}
