package com.morgan.design.paf.service;

import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.morgan.design.args.CommandLinePafArgs;

@Repository
public class PafRepositoryImpl implements PafRepository {

	@Autowired
	private BasicDataSource dataSource;

	private SimpleJdbcOperations pafJdbcOperations;

	@Override
	public void savePafEntries(final CommandLinePafArgs pafArgs, final List entries) {
		constructDateSource(pafArgs);
		this.pafJdbcOperations = new SimpleJdbcTemplate(this.dataSource);

	}

	@Override
	public void updatePafEntries(final CommandLinePafArgs pafArgs, final List entries) {
		constructDateSource(pafArgs);
		this.pafJdbcOperations = new SimpleJdbcTemplate(this.dataSource);

	}

	private void constructDateSource(final CommandLinePafArgs pafArgs) {
		this.dataSource.setUsername(pafArgs.username);
		this.dataSource.setPassword(pafArgs.password);
		this.dataSource.setUrl("jdbc:mysql://" + pafArgs.host + ":3306/" + pafArgs.schema
			+ "?autoReconnect=true&amp;rewriteBatchedStatements=false&amp;allowMultiQueries=true&amp;zeroDateTimeBehavior=convertToNull");
	}
}
