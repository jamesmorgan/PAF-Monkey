package com.morgan.design.paf.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.domain.TableDefinition;
import com.morgan.design.paf.util.SqlUtils;

@Service("jdbcPafRepository")
public class JdbcPafRepositoryImpl implements PafRepository {

	@Autowired
	private BasicDataSource dataSource;

	private SimpleJdbcOperations pafJdbcOperations;

	@Override
	public void insertChangeLog(final CommandLinePafArgs pafArgs, final PafChangeLog changeLog) {
		constructDateSource(pafArgs);
		this.pafJdbcOperations = new SimpleJdbcTemplate(this.dataSource);

		final Map<String, Object> params = Maps.newHashMap();
		params.put("mode", changeLog.getMode()
			.toString());
		params.put("buildingNames", changeLog.getBuildNames());
		params.put("localities", changeLog.getLocalities());
		params.put("mailSort", changeLog.getMailSort());
		params.put("pafAddress", changeLog.getPafAddress());
		params.put("organisations", changeLog.getOrganisations());
		params.put("subBuildingName", changeLog.getSubBuildingName());
		params.put("thoroughfare", changeLog.getThoroughfare());
		params.put("thoroughfareDescriptor", changeLog.getThoroughfareDescriptor());
		params.put("udprn", changeLog.getUdprn());
		params.put("endDate", date(changeLog.getEndDate()));
		params.put("startDate", date(changeLog.getStartDate()));

		this.pafJdbcOperations.update("INSERT INTO `paf_change_log` SET "
			+ "`StartDate`=:startDate, `EndDate`=:endDate, `Mode`=:mode, `BuildNames`=:buildingNames, `Localities`=:localities, "
			+ "`MailSort`=:mailSort, `Organisations`=:organisations, `PafAddress`=:pafAddress, `SubBuildingName`=:subBuildingName, "
			+ "`ThoroughfareDescriptor`=:thoroughfareDescriptor, `Thoroughfare`=:thoroughfare, `Udprn`=:udprn", params);
	}

	@Override
	public void saveBatch(final CommandLinePafArgs pafArgs, final TableDefinition definition, final List<Object[]> batch) {
		constructDateSource(pafArgs);
		this.pafJdbcOperations = new SimpleJdbcTemplate(this.dataSource);

		final String batchUpdateStmt = SqlUtils.createBatchUpdateStatement(definition);
		this.pafJdbcOperations.batchUpdate(batchUpdateStmt, batch);
	}

	private String date(final Date value) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(value);
	}

	private void constructDateSource(final CommandLinePafArgs pafArgs) {
		this.dataSource.setUsername(pafArgs.username);
		this.dataSource.setPassword(pafArgs.password);
		this.dataSource.setUrl("jdbc:mysql://" + pafArgs.host + ":" + pafArgs.port + "/" + pafArgs.schema
			+ "?autoReconnect=true&amp;rewriteBatchedStatements=false&amp;allowMultiQueries=true&amp;zeroDateTimeBehavior=convertToNull");
	}
}
