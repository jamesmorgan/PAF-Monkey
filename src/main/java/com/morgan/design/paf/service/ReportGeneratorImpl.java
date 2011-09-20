package com.morgan.design.paf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.reports.Report;

@Service
public class ReportGeneratorImpl implements ReportGenerator {

	@Autowired
	private Report logReportInstance;

	private final Logger logger = LoggerFactory.getLogger(ReportGeneratorImpl.class);

	@Override
	public void generateChangeLogReport(final PafChangeLog changeLog) {
		this.logger.debug("Generating Change log Report");
		this.logReportInstance.generate(changeLog);
	}

}
