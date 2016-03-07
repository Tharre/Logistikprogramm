package Logistik;

import org.htl_hl.Logistikprogramm.LConnection;

import java.sql.*;

/**
 * Verwaltet die Verbindung mit der Datenbank Baut die Verbindung auf, stellt
 * die Anfragen an die Datenbank und schließt die Verbindung wieder
 */

public class DBConnection {
	private Connection con = null;

	public DBConnection(String dbName, String user, String password) {
		try {
			LConnection server = new LConnection(dbName);
			con = server.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
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