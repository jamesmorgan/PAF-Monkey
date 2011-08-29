package com.morgan.design.paf.exception;


public class NoDefinitionFilesFoundException extends RuntimeException {

	private static final long serialVersionUID = -599708778137875699L;

	public NoDefinitionFilesFoundException(final String directory) {
		super(String.format("No definition files found @ %s", directory));
	}

}
