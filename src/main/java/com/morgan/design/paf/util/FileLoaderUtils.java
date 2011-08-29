package com.morgan.design.paf.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLoaderUtils {

	private final static Logger logger = LoggerFactory.getLogger(FileLoaderUtils.class);

	public static FileFilter getDefinitionFileFilter() {
		return new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.getName().contains(".xml");
			}
		};
	}

	public static FileFilter getDataFileFilter() {
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

	public static File[] retrieveFiles(final String directory, final FileFilter fileFilter) {
		final File dataDirectory = new File(directory);
		File[] dataFiles = null;
		if (dataDirectory.isDirectory()) {
			dataFiles = dataDirectory.listFiles(fileFilter);
			logger.debug("Found {} files", dataFiles.length);
			logger.debug("Files found = {}", Arrays.toString(dataFiles));
		}
		return dataFiles;
	}

}
