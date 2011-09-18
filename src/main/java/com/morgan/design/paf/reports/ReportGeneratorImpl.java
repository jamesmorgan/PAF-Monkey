package com.morgan.design.paf.reports;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.domain.PafChangeLogReport;

@Service
public class ReportGeneratorImpl implements ReportGenerator {

	@Resource
	private PafChangeLogReport logReportInstance;

	private final Logger logger = LoggerFactory.getLogger(ReportGeneratorImpl.class);

	@Override
	public void generateChangeLogReport(final PafChangeLog changeLog) {
		this.logger.debug("Generating Change log Report");
		this.logReportInstance.generate(changeLog);
	}

}
