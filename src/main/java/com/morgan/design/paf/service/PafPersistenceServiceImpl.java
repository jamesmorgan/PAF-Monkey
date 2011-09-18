package com.morgan.design.paf.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.repository.PafRepository;

@Service("persistenceService")
public class PafPersistenceServiceImpl implements PafRepository {

	@Resource(name = "jdbcPafRepository")
	private PafRepository jdbcPafRepository;

	@Resource(name = "mongoPafRepository")
	private PafRepository mongoPafRepository;

	private PafRepository getRepository(final CommandLinePafArgs pafArgs) {
		return pafArgs.mongoEnabled()
				? this.mongoPafRepository
				: this.jdbcPafRepository;
	}

	@Override
	public void insertChangeLog(final CommandLinePafArgs pafArgs, final PafChangeLog changeLog) {
		getRepository(pafArgs).insertChangeLog(pafArgs, changeLog);
	}

	@Override
	public void saveBatch(final CommandLinePafArgs pafArgs, final TableDefinition definition, final List<Object[]> batch) {
		getRepository(pafArgs).saveBatch(pafArgs, definition, batch);
	}
}
