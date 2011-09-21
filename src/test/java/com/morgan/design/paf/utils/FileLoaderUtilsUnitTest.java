package com.morgan.design.paf.utils;

import static org.junit.Assert.assertThat;

import java.io.File;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.morgan.design.paf.util.FileLoaderUtils;

public class FileLoaderUtilsUnitTest {

	@Test
	public void shouldLoadDefinitionFiles() {
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
	}

	@Test
	public void shouldLoadDataFiles() {
		final File[] retrieveFiles = FileLoaderUtils.loadDataFiles("src\\test\\resources\\test_data\\data\\parsing\\address");
		assertThat(retrieveFiles.length, Is.is(4));
		assertThat(retrieveFiles[0].toString(), Is.is("src\\test\\resources\\test_data\\data\\parsing\\address\\fpmainfl.c02"));
		assertThat(retrieveFiles[1].toString(), Is.is("src\\test\\resources\\test_data\\data\\parsing\\address\\fpmainfl.c03"));
		assertThat(retrieveFiles[2].toString(), Is.is("src\\test\\resources\\test_data\\data\\parsing\\address\\fpmainfl.c04"));
		assertThat(retrieveFiles[3].toString(), Is.is("src\\test\\resources\\test_data\\data\\parsing\\address\\fpmainfl.c06"));
	}
}
