package com.morgan.design.paf.reports;

import com.morgan.design.paf.domain.PafChangeLog;

public interface ReportGenerator {

	void generateChangeLogReport(PafChangeLog changeLog);

}
