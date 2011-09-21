package com.morgan.design.paf.service;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.morgan.design.paf.domain.ColumnDefinition;
import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.util.FileLoaderUtils;
import com.morgan.design.paf.util.TableDefinitionBuilder;

public class TableDefinitionBuilderUnitTest {

	@Test
	public void shouldBuildDefinitionsCorrectly() {

		final File[] retrieveFiles = FileLoaderUtils.loadDefinitionFiles("src\\main\\resources\\definitions\\");
		assertThat(retrieveFiles.length, Is.is(10));
		assertThat(retrieveFiles[0].toString(), Is.is("src\\main\\resources\\definitions\\address.xml"));
		assertThat(retrieveFiles[1].toString(), Is.is("src\\main\\resources\\definitions\\building_names.xml"));
		assertThat(retrieveFiles[2].toString(), Is.is("src\\main\\resources\\definitions\\localities.xml"));
		assertThat(retrieveFiles[3].toString(), Is.is("src\\main\\resources\\definitions\\mailsort.xml"));
		assertThat(retrieveFiles[4].toString(), Is.is("src\\main\\resources\\definitions\\organisations.xml"));
		assertThat(retrieveFiles[5].toString(), Is.is("src\\main\\resources\\definitions\\sub_building_names.xml"));
		assertThat(retrieveFiles[6].toString(), Is.is("src\\main\\resources\\definitions\\thoroughfare.xml"));
		assertThat(retrieveFiles[7].toString(), Is.is("src\\main\\resources\\definitions\\thoroughfare_descriptor.xml"));
		assertThat(retrieveFiles[8].toString(), Is.is("src\\main\\resources\\definitions\\udprn.xml"));
		assertThat(retrieveFiles[9].toString(), Is.is("src\\main\\resources\\definitions\\welsh_address.xml"));

		final List<ColumnDefinition> columnDefinitions = Lists.newLinkedList();
		columnDefinitions.add(ColumnDefinition.create("Postcode", 7, "A"));
		columnDefinitions.add(ColumnDefinition.create("AddressKey", 8, "N"));
		columnDefinitions.add(ColumnDefinition.create("UdprnKey", 8, "N"));
		columnDefinitions.add(ColumnDefinition.create("OrganisationKey", 8, "N"));
		columnDefinitions.add(ColumnDefinition.create("PostcodeType", 1, "A"));
		columnDefinitions.add(ColumnDefinition.create("DeliveryPointSuffix", 2, "A"));
		columnDefinitions.add(ColumnDefinition.create("SuOrganisationIndicator", 1, "A"));

		final TableDefinition udprnTableDef = new TableDefinition();
		udprnTableDef.setFileName("udprnful");
		udprnTableDef.setName("udprn");
		udprnTableDef.setColumns(columnDefinitions);

		final List<TableDefinition> definitionFiles = TableDefinitionBuilder.parseDefinitionFiles("src\\main\\resources\\definitions\\");
		assertThat(definitionFiles.size(), Is.is(10));
		assertThat(definitionFiles.get(8), Is.is(udprnTableDef));
		
		assertThat(definitionFiles.get(9).getName(), Is.is("paf_address"));
		assertThat(definitionFiles.get(9).getFileName(), Is.is("wfmainfl"));
		assertThat(definitionFiles.get(9).getIgnoreDuplicates(), Is.is(true));
		

	}
}
