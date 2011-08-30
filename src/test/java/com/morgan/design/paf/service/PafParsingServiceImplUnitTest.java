package com.morgan.design.paf.service;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.morgan.design.paf.domain.ColumnDefinition;

public class PafParsingServiceImplUnitTest {

	@Test
	public void shouldCorrectlyDetermineAndCreateParam() {
		final ColumnDefinition alphaColumn = new ColumnDefinition();
		alphaColumn.setType("A");
		alphaColumn.setLength(3);
		alphaColumn.setName("alpha");

		final ColumnDefinition numericColumn = new ColumnDefinition();
		numericColumn.setType("N");
		numericColumn.setLength(8);
		numericColumn.setName("numeric");

		final PafParsingServiceImpl service = new PafParsingServiceImpl();
		System.out.println(Integer.class.isAssignableFrom(service.createParam("ABC00018564STHE MART", 0, alphaColumn).getClass()));
		assertThat((Integer) service.createParam("00018564STHE MART", 0, numericColumn), Is.is(18564));
		assertThat((Integer) service.createParam("01856400STHE MART", 0, numericColumn), Is.is(1856400));
		assertThat((String) service.createParam("ABC00018564STHE MART", 0, alphaColumn), Is.is("ABC"));
	}
}
