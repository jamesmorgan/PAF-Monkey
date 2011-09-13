package com.morgan.design.paf.domain;

import com.google.common.annotations.Beta;

/**
 * @author James Edward Morgan
 */
public enum Mode {

	SOURCE,

	@Beta
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
