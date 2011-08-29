package com.morgan.design.paf.exception;

public class NoDataFilesFoundException extends RuntimeException {

	private static final long serialVersionUID = -8964013827176572522L;

	public NoDataFilesFoundException(final String directory) {
		super(String.format("No data files found @ %s", directory));
	}

}
