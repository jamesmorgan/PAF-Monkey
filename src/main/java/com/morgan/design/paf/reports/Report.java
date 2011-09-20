package com.morgan.design.paf.reports;

import com.morgan.design.paf.domain.PafChangeLog;


public interface Report {

	void generate(PafChangeLog log);
}
