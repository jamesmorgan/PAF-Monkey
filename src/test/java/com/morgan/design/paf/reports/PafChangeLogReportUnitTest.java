package com.morgan.design.paf.reports;

import org.junit.Test;

import com.morgan.design.paf.domain.PafChangeLog;

public class PafChangeLogReportUnitTest {

	@Test
	public void shouldCreatePdf() {
		final PafChangeLogReport logReport = PafChangeLogReport.getInstance();

		final PafChangeLog log = PafChangeLog.createSourceLog();
		log.begin();
		log.finish();
		logReport.generate(log);
	}

}
