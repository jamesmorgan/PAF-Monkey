package com.morgan.design.paf.db;

import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

@Component
public class MongoConnection {

	private static MongoConnection INSTANCE;

	private static Mongo m = null;
	private static DB db = null;

	public synchronized static MongoConnection getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MongoConnection();
		}
		return INSTANCE;
	}

	public void connect(final String username, final String password, final String hostname, final String database, final int port)
			throws Exception {
		try {
			m = new Mongo(hostname, port);
			db = m.getDB(database);

			if (!db.authenticate(username, password.toCharArray())) {
				throw new IllegalArgumentException("Could not authenticate to database '" + database + "' with user '" + username + "'.");
			}
		}
		catch (final MongoException.Network e) {
			throw new Exception("Could not connect to Mongo DB.");
		}
	}

	public Mongo getConnection() {
		return m;
	}

	public DB getDatabase() {
		return db;
	}
}
