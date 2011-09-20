package com.morgan.design.paf.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.db.MongoConnection;
import com.morgan.design.paf.domain.ColumnDefinition;
import com.morgan.design.paf.domain.PafChangeLog;
import com.morgan.design.paf.domain.TableDefinition;

@Service("mongoPafRepository")
public class MongoPafRepositoryImpl implements PafRepository {

	private final Logger logger = LoggerFactory.getLogger(MongoPafRepositoryImpl.class);

	@Autowired
	private MongoConnection mongoConnection;

	@Override
	public void insertChangeLog(final CommandLinePafArgs pafArgs, final PafChangeLog changeLog) {
		try {

			this.mongoConnection.connect(pafArgs.username, pafArgs.password, pafArgs.host, pafArgs.db, pafArgs.port);
			final DB db = this.mongoConnection.getDatabase();
			final DBCollection coll = db.getCollection("paf_address");

			final TreeMap<String, Integer> newTreeMap = Maps.newTreeMap();
			final Field[] declaredFields = PafChangeLog.class.getDeclaredFields();
			for (final Field field : declaredFields) {
				final Type genericType = field.getGenericType();
				field.setAccessible(true);

				if (genericType == int.class) {
					newTreeMap.put(field.getName(), (Integer) field.get(changeLog));
				}
			}

			final BasicDBObject doc = new BasicDBObject();
			for (final Entry<String, Integer> field : newTreeMap.entrySet()) {
				doc.put(field.getKey(), field.getValue());
			}
			coll.insert(doc);
		}
		catch (final UnknownHostException e) {
			this.logger.error("UnknownHostException : ", e);
		}
		catch (final MongoException e) {
			this.logger.error("MongoException : ", e);
		}
		catch (final IllegalArgumentException e) {
			this.logger.error("IllegalArgumentException : ", e);
		}
		catch (final IllegalAccessException e) {
			this.logger.error("IllegalAccessException : ", e);
		}
		catch (final Exception e) {
			this.logger.error("Exception : ", e);
		}
	}

	@Override
	public void saveBatch(final CommandLinePafArgs pafArgs, final TableDefinition definition, final List<Object[]> batch) {
		try {
			this.mongoConnection.connect(pafArgs.username, pafArgs.password, pafArgs.host, pafArgs.db, pafArgs.port);
			final DB db = this.mongoConnection.getDatabase();
			final DBCollection coll = db.getCollection(definition.getName());

			for (final Object[] objects : batch) {
				final BasicDBObject doc = new BasicDBObject();
				final Iterator<ColumnDefinition> allNoneFillerColumns = definition.getAllNoFillerColumns()
					.iterator();
				int i = 0;
				while (allNoneFillerColumns.hasNext()) {
					final ColumnDefinition def = allNoneFillerColumns.next();
					doc.put(def.getName(), objects[i++]);
				}
				coll.insert(doc);
			}
		}
		catch (final UnknownHostException e) {
			this.logger.error("UnknownHostException : ", e);
		}
		catch (final MongoException e) {
			this.logger.error("MongoException : ", e);
		}
		catch (final IllegalArgumentException e) {
			this.logger.error("IllegalArgumentException : ", e);
		}
		catch (final Exception e) {
			this.logger.error("Exception : ", e);
		}
	}
}
