package com.morgan.design.paf.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morgan.design.paf.exception.NoDataFilesFoundException;
import com.morgan.design.paf.exception.NoDefinitionFilesFoundException;

public class FileLoaderUtils {

	private final static Logger logger = LoggerFactory.getLogger(FileLoaderUtils.class);

	public static File[] loadDefinitionFiles(final String directory) {
		final File[] retrieveFiles = retrieveFiles(directory, getDefinitionFileFilter());
		if (filesDontExist(retrieveFiles)) {
			throw new NoDefinitionFilesFoundException(directory);
		}
		return retrieveFiles;
	}

	public static File[] loadDataFiles(final String directory) {
		final File[] retrieveFiles = retrieveFiles(directory, getDataFileFilter());
		if (filesDontExist(retrieveFiles)) {
			throw new NoDataFilesFoundException(directory);
		}
		return retrieveFiles;
	}

	private static File[] retrieveFiles(final String directory, final FileFilter fileFilter) {
		final File dataDirectory = new File(directory);
		File[] dataFiles = null;
		if (dataDirectory.isDirectory()) {
			dataFiles = dataDirectory.listFiles(fileFilter);
			logger.debug("Found {} files", dataFiles.length);
			logger.debug("Files found = {}", Arrays.toString(dataFiles));
		}
		return dataFiles;
	}

	private static FileFilter getDefinitionFileFilter() {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.getName().contains(".xml");
			}
		};
	}

	private static FileFilter getDataFileFilter() {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				final int nameLength = pathname.getName().length();
				if (nameLength == 0 || nameLength < 4) {
					return false;
				}
				return pathname.getName().substring(nameLength - 4, nameLength).matches("[.c]{1,2}[\\d]{2,2}");
			}
		};
	}

	private static boolean filesDontExist(final File[] files) {
		return null == files || 0 == files.length;
	}
}
