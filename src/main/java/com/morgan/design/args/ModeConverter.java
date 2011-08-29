package com.morgan.design.args;

import com.beust.jcommander.IStringConverter;
import com.morgan.design.paf.domain.Mode;

/**
 * @author James Edward Morgan
 */
public class ModeConverter implements IStringConverter<Mode> {
	@Override
	public Mode convert(final String value) {
		return Mode.fromValue(value);
	}
}
