package Logistik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;

public class LConnection {

	private Connection connection;

	public LConnection() throws Exception {
		// TODO(Tharre): should connect with the server here
		// but instead we are going to connect with the DB for now to simulate that

		String url = "jdbc:mysql://127.0.0.1:3306/logistik_2"; // TODO(Tharre): dbname?
		connection = DriverManager.getConnection(url, "root", "test");
	}

	// TODO(Tharre): remove this
	public Connection getConnection() {
		return connection;
	}

	public <E extends Record> Result<E> getTableData(Table<E> table) {
		DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
		long startTime = System.nanoTime();
		Result<E> r = create.selectFrom(table).fetch();
		long endTime = System.nanoTime();
		System.out.println(table.getName() + " loading time: " + (endTime - startTime)/1000000000.0 + "s");
		return r;
	}

	// TODO(Tharre): generalize this
	public <E extends Record, T> List<T> getTableField(Table<E> table, TableField<E, T> wanted) {
		DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
		long startTime = System.nanoTime();
		List<T> r = create.selectFrom(table).fetch(wanted);
		long endTime = System.nanoTime();
		System.out.println(table.getName() + " loading time: " + (endTime - startTime)/1000000000.0 + "s");
		return r;
	}
}
