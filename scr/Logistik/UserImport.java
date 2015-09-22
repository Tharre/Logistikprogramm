package Logistik;

import java.util.Date;
import java.text.*;
import java.sql.*;

/**
 * Beinhaltet die Daten des eingeloggten Users
 */

public class UserImport {
	private long lastAction;
	private long loginTime;
	private String login;
	private int usergroup;
	private DBConnection con;
	private DBConnection conKst;
	private String st;

	private Date datum;
	private int jahr;

	public UserImport(String s, DBConnection con, String st) {
		this.con = con;
		this.st = st;

		datum = new Date();
		jahr = datum.getYear() + 1900;

		conKst = new DBConnection("budget4", "budget2009", "cafelatte");

		setBase(s);
		setMail(getMail());
		setName(getName());
		login = s;
		loginTime = System.currentTimeMillis();
		lastAction = loginTime;
		setUserGroup(getUserGroup());
	}

	public String getPath() {
		return st;
	}

	public String getMail() {
		String qry = "SELECT mail FROM ldap_user WHERE cn LIKE '" + login
				+ "';";
		try {
			ResultSet rs = con.mysql_query(qry);
			rs.next();
			String s = rs.getString("mail");
			rs.close();
			return s;
		} catch (Exception e) {
			return null;
		}
		// return ldap.getUserAttribute(base,"mail");
	}

	public String getName() {
		String qry = "SELECT name FROM ldap_user WHERE cn LIKE '" + login
				+ "';";
		try {
			ResultSet rs = con.mysql_query(qry);
			rs.next();
			String n = rs.getString("name");
			rs.close();
			return n;
		} catch (Exception e) {
			return null;
		}
		// return ldap.getUserAttribute(base,"fullName");
	}

	public String getCn() {
		return login;
		// return ldap.getUserAttribute(base,"cn");
	}

	private void setBase(String s) {
		if (s.charAt(0) == '.') {
			s = s.substring(1);
		}
		String[] st = s.split("\\.");
		String b = "cn=" + st[0];

		for (int i = 1; i < st.length - 1; i++) {
			b += ",ou=" + st[i];
		}
		b += ",o=" + st[st.length - 1];
	}

	public void setMail(String s) {
	}

	public void setName(String s) {
	}

	public void setCn(String s) {
	}

	public void doAction() {
		lastAction = System.currentTimeMillis();
	}

	public String getLoginTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(loginTime);
	}

	public String getLastActionString() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(lastAction);
	}

	public void setUserGroup(int ug) {
		usergroup = ug;
	}

	public int getUserGroup() {
		try {
			String qry = "SELECT usergroup FROM user WHERE cn='" + login + "';";
			ResultSet rs = con.mysql_query(qry);
			if (rs.next()) {
				int i = rs.getInt("usergroup");
				rs.close();
				return i;
			}
			rs.close();
		} catch (Exception e) {
		}
		return -1;
	}

	public boolean hasRecht(int recht) {
		try {

			String qry = "SELECT id FROM rechte WHERE usergroup=" + usergroup
					+ " AND recht=" + recht + ";";
			ResultSet rs = con.mysql_query(qry);
			boolean b = rs.next();
			rs.close();
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public DBConnection getConnection() {
		return con;
	}

	public DBConnection getConnectionKst() {
		return conKst;
	}

}