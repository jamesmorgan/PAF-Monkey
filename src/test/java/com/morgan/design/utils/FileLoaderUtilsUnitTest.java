package com.morgan.design.utils;

import static org.junit.Assert.assertThat;

import java.io.File;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.morgan.design.paf.util.FileLoaderUtils;

public class FileLoaderUtilsUnitTest {

	@Test
	public void shouldLoadDefinitionFiles() {
		final File[] retrieveFiles =
				FileLoaderUtils.retrieveFiles("src\\test\\resources\\test_data\\definitions\\", FileLoaderUtils.getDefinitionFileFilter());
		assertThat(retrieveFiles.length, Is.is(1));
		assertThat(retrieveFiles[0].toString(), Is.is("src\\test\\resources\\test_data\\definitions\\udprn.xml"));
	}

	@Test
	public void shouldLoadDataFiles() {
		final File[] retrieveFiles =
				FileLoaderUtils.retrieveFiles("src\\test\\resources\\test_data\\data\\", FileLoaderUtils.getDataFileFilter());
		assertThat(retrieveFiles.length, Is.is(4));
		assertThat(retrieveFiles[0].toString(), Is.is("src\\test\\resources\\test_data\\data\\fpmainfl.c02"));
		assertThat(retrieveFiles[1].toString(), Is.is("src\\test\\resources\\test_data\\data\\fpmainfl.c03"));
		assertThat(retrieveFiles[2].toString(), Is.is("src\\test\\resources\\test_data\\data\\fpmainfl.c04"));
		assertThat(retrieveFiles[3].toString(), Is.is("src\\test\\resources\\test_data\\data\\fpmainfl.c06"));
	}
}
