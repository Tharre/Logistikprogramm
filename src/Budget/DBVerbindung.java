package Budget;

import java.sql.*;

/**
 *In der Klasse Datenbankverbindung werden die für dieses Programm notwendigen
 * Treiber geladen. Ausserdem werden die Verbindungen zur Logistik- und zur
 * Budget-Datenbank hergestellt.
 *<p>
 * Title: Datenbankverbindung
 * 
 * @author Haupt, Liebhart
 **/
public class DBVerbindung {
	/** Connection **/
	private Connection con;
	/** URL **/
	private String url;
	/** Datenbankname **/
	private String dbName;
	/** Name des Servers+Portnummer+Tabellenname **/
	private String con_str;
	/** Username für den DB-Login **/
	private String user;
	/** Passwort für den DB-Login **/
	private String pwt;

	/**
	 * Konstruktor
	 */
	public DBVerbindung() {
	}

	/**
	 * laedt den notwendigen Treiber
	 */
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // Datenbranktreiber für
			// mysql laden
		} catch (ClassNotFoundException ex) // Falls Datenbanktreiber nicht
		// gefunden werden konnte
		{
			System.out.println(ex.getMessage()
					+ "\n Class.forName Treiber wurde nicht geladen");
			ex.printStackTrace();
		}
	}

	/**
	 * baut Verbindung zur aktuellen Datenbank auf: budget2 (scharfe Version),
	 * budget4 (Testversion)
	 * 
	 * @return Connection zur Datenbank
	 */
	public Connection verbindeDatenbankAktuell() {
		dbName = "budget2";
		user = "budget2009";
		pwt = "cafelatte";
		con = verbindeDatenbank(dbName, user, pwt);
		return con;
	}// verbindeDatenbankAktuell

	/**
	 * baut Verbindung zur Vorjahresdatenbank auf: budget1 (scharfe Version),
	 * budget3 (Testversion)
	 * 
	 * @return con Connection zur Datenbank
	 */
	public Connection verbindeDatenbankAlt() {
		dbName = "budget1";
		user = "budget2009";
		pwt = "cafelatte";
		con = verbindeDatenbank(dbName, user, pwt);
		return con;
	}// verbindeDatenbankAlt

	/**
	 * baut Verbindung zur Logistikdatenbank auf: logistik_2 (aktuelle DB) oder
	 * logistik_1 (Test-DB)
	 * 
	 * @return Connection zur Datenbank
	 */
	public Connection verbindeDatenbankLogistik() {
		dbName = "logistik_2";
		user = "logistik1";
		pwt = "4ahwii";
		con = verbindeDatenbank(dbName, user, pwt);
		return con;
	}// verbindeDatenbankLogistik

	/**
	 * baut Verbindung zur Budget-DB mit dem mitgeschickten Namen auf
	 * 
	 * @param dbName
	 *            Name der DB mit der Verbindung aufgebaut werden soll
	 * @return con Connection zur Datenbank
	 */
	public Connection verbindeDatenbank(String dbName, String user, String pwt) {
		con_str = "//logistik.htl-hl.ac.at:3306/" + dbName;
		url = "jdbc:mysql:" + con_str;

		try {
			con = DriverManager.getConnection(url, user, pwt);
			System.out.println("Verbindung zur Datenbank " + dbName
					+ " wurde aufgebaut.");
		} catch (SQLException ex) {
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("Verbindung zur Datenbank " + dbName
					+ " konnte nicht hergestellt werden.");
			ex.printStackTrace();
		}
		return con;
	}// verbindeDatenbank
}
