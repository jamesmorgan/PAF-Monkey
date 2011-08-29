package com.morgan.design.service;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.morgan.design.paf.domain.ColumnDefinition;
import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.service.TableDefinitionBuilder;
import com.morgan.design.paf.util.FileLoaderUtils;

public class TableDefinitionBuilderUnitTest {

	@Test
	public void shouldBuildDefinitionsCorrectly() {
		final List<ColumnDefinition> columnDefinitions = Lists.newArrayList();
		columnDefinitions.add(ColumnDefinition.create("Postcode", 7, "A"));
		columnDefinitions.add(ColumnDefinition.create("AddressKey", 8, "N"));
		columnDefinitions.add(ColumnDefinition.create("UdprnKey", 8, "N"));
		columnDefinitions.add(ColumnDefinition.create("OrganisationKey", 8, "N"));
		columnDefinitions.add(ColumnDefinition.create("PostcodeType", 1, "A"));
		columnDefinitions.add(ColumnDefinition.create("DeliveryPointSuffix", 2, "A"));
		columnDefinitions.add(ColumnDefinition.create("SuOrganisationIndicator", 1, "A"));

		final TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setFileName("udprnful");
		tableDefinition.setName("udprn");
		tableDefinition.setColumns(columnDefinitions);

		final File[] retrieveFiles =
				FileLoaderUtils.retrieveFiles("src\\test\\resources\\test_data\\definitions\\", FileLoaderUtils.getDefinitionFileFilter());
		assertThat(retrieveFiles.length, Is.is(1));
		assertThat(retrieveFiles[0].toString(), Is.is("src\\test\\resources\\test_data\\definitions\\udprn.xml"));

		final List<TableDefinition> definitionFiles = TableDefinitionBuilder.parseDefinitionFiles(retrieveFiles);
		assertThat(definitionFiles.size(), Is.is(1));
		assertThat(definitionFiles.get(0), Is.is(tableDefinition));
	}
}
