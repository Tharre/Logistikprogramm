package org.htl_hl.Logistikprogramm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.jooq.*;
import org.jooq.impl.DSL;

public class LConnection {

	private Connection connection;
	public DSLContext create;

	public LConnection() throws Exception {
		// TODO(Tharre): should connect with the server here
		// but instead we are going to connect with the DB for now to simulate that

		String url = "jdbc:mysql://127.0.0.1:3306/";
		connection = DriverManager.getConnection(url, "root", "test");

		create = DSL.using(connection, SQLDialect.MYSQL);
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
