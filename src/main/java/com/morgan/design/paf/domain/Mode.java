package com.morgan.design.paf.domain;

/**
 * @author James Edward Morgan
 */
public enum Mode {

	SOURCE,

	@Deprecated
	UPDATE;

	public static Mode fromValue(final String value) {
		for (final Mode mode : values()) {
			if (mode.toString().toLowerCase().equalsIgnoreCase(value)) {
				return mode;
			}
		}
		return null;
	}
}
