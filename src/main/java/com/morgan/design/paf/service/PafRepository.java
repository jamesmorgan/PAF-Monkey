package com.morgan.design.paf.service;

import java.util.List;

import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.domain.TableDefinition;

public interface PafRepository {

	void insertChangeLog(CommandLinePafArgs pafArgs, PafChangeLog changeLog);

	void saveBatch(CommandLinePafArgs pafArgs, TableDefinition definition, List<Object[]> batch);

}
