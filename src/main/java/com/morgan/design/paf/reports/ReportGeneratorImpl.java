package com.morgan.design.paf.reports;

import org.springframework.stereotype.Service;

import com.morgan.design.paf.domain.PafChangeLog;

@Service
public class ReportGeneratorImpl implements ReportGenerator {

	@Override
	public void generateChangeLogReport(final PafChangeLog changeLog) {
		final PafChangeLogReport logReportInstance = PafChangeLogReport.getInstance();
		logReportInstance.generate(changeLog);
	}

}
