package Logistik;

import java.sql.*;

/**
 * Verwaltet die Verbindung mit der Datenbank Baut die Verbindung auf, stellt
 * die Anfragen an die Datenbank und schlieﬂt die Verbindung wieder
 */

public class DBConnection {
	private Connection con = null;
	private String url;
	private String user;
	private String password;

	public DBConnection(String dbName, String user, String password) {
		this.user = user;
		this.password = password;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			ex.getMessage();
		}

		String con_str = "//logistik.htl-hl.ac.at:3306/" + dbName;
		url = "jdbc:mysql:" + con_str;
		connect();
	}

	public void connect() {
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException ex) {
			ex.getMessage();
		}
	}

	public void close() {
		try {
			con.close();
		} catch (SQLException ex) {
			ex.getMessage();
		}
	}

	public int mysql_update(String query) {
		try {
			Statement stmt = con.createStatement();
			int c = stmt.executeUpdate(query);
			stmt.close();
			return c;
		} catch (Exception e) {
			e.getMessage();
			return 0;
		}
	}

	public ResultSet mysql_query(String query) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// stmt.close();
			return rs;
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

}