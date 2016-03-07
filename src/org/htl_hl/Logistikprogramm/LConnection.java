package org.htl_hl.Logistikprogramm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import org.jooq.*;
import org.jooq.impl.DSL;

public class LConnection {

	private Connection connection;
	public DSLContext create;

	public LConnection(String dbname) throws Exception {
		// TODO(Tharre): should connect with the server here
		// but instead we are going to connect with the DB for now to simulate that

		Properties config = new Properties();
		config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql_auth.properties"));

		String server = config.getProperty("server");
		String username = config.getProperty("username");
		String password = config.getProperty("password");

		String url = "jdbc:mysql://" + server + "/" + dbname;
		connection = DriverManager.getConnection(url, username, password);

		create = DSL.using(connection, SQLDialect.MYSQL);
	}

	public LConnection() throws Exception {
		this("");
	}

	// TODO(Tharre): remove this
	public Connection getConnection() {
		return connection;
	}

	public <E extends Record> Result<E> getTableData(Table<E> table) {
		long startTime = System.nanoTime();
		Result<E> r = create.selectFrom(table).fetch();
		long endTime = System.nanoTime();
		System.out.println(table.getName() + " loading time: " + (endTime - startTime)/1000000000.0 + "s");
		return r;
	}

	// TODO(Tharre): generalize this
	public <E extends Record, T> List<T> getTableField(Table<E> table, TableField<E, T> wanted) {
		long startTime = System.nanoTime();
		List<T> r = create.selectFrom(table).fetch(wanted);
		long endTime = System.nanoTime();
		System.out.println(table.getName() + " loading time: " + (endTime - startTime)/1000000000.0 + "s");
		return r;
	}
}
