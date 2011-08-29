package com.morgan.design.args;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.IStringConverterFactory;
import com.morgan.design.paf.domain.Mode;

/**
 * @author James Edward Morgan
 */
public class ConverterFactory implements IStringConverterFactory {
	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends IStringConverter<?>> getConverter(@SuppressWarnings("rawtypes") final Class forType) {
		if (forType.equals(Mode.class)) {
			return ModeConverter.class;
		}
		return null;
	}
}
