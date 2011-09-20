package com.morgan.design.paf.reports;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.morgan.design.paf.domain.PafChangeLog;

public class PafChangeLogReportUnitTest {

	private PafChangeLogReport logReport;

	@Before
	public void setUp() {
		this.logReport = new PafChangeLogReport();
	}

	@Test
	public void shouldCreatePdf() {
		final PafChangeLog log = PafChangeLog.createSourceLog();
		log.begin();
		log.finish();
		this.logReport.generate(log);
		assertTrue(new File(this.logReport.generateTitle()).exists());
	}
}
