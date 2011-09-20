package com.morgan.design.paf.service;

import com.morgan.design.paf.domain.PafChangeLog;

public interface ReportGenerator {

	void generateChangeLogReport(PafChangeLog changeLog);

}
