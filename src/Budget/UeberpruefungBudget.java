package Budget;

import java.sql.*;
import java.util.Vector;

/**
 *Hier wird ueberprueft, ob der geplante Wert, den man an einem Budget aendern
 * moechte, groeßer ist als der Betrag, der zur Verfuegung steht
 *<p>
 * Title: Ueberpruefung
 * 
 *@author Haupt, Liebhart
 **/
public class UeberpruefungBudget {
	// Connections
	/** Connection zur Budget-Datenbank **/
	private Connection con;

	// Statements
	/** Statement fuer einen SQL-Befehl **/
	private Statement stmt;

	// ResultSets
	/** Result Set um einen SQL-Befehl auszufuehren **/
	private ResultSet rs;

	// String
	/** Query fuer einen SQL-Befehl **/
	private String query;
	/** die Tabelle, in der das zu aendernde Feld steht **/
	private String tabelle;

	// double
	/** Die Summe aller geplanten Budgets in einer Tabelle **/
	private double summe;
	/** Der Betrag, den der neue Wert nicht ueberschreiten darf **/
	private double uebersumme;
	/** der geplante Wert vom Hauptbudget **/
	private double geplant;
	/** der aktuell geplante Wert an der Stelle, die man aendern will **/
	private double aktuellGeplantes;

	// int
	/** die Nummer von dem Feld, das man aendern will **/
	private int zeilennummer;
	/** true: es handelt sich um UT8; false: es handelt sich nicht um UT8 **/
	private int isUT8;

	// sonstiges
	/** true: der Wert ist korrekt; false: der Wert ist zu hoch **/
	private boolean ok;

	/**
	 * Konstruktor
	 * 
	 * @param tabelle
	 *            die Tabelle, in der etwas geaendert werden soll
	 * @param zeilennummer
	 *            die Nummer von dem Feld, das geandert werden soll
	 * @param con
	 *            Verbindung zur Budget-Datenbank
	 */
	public UeberpruefungBudget(String tabelle, int zeilennummer, Connection con) {
		this.zeilennummer = zeilennummer;
		this.tabelle = tabelle;
		this.con = con;

	}

	/**
	 * prueft, ob der Betrag nicht groeßer ist, als der verfuegbare Betrag; wird
	 * aufgerufen, wenn ein neues Budget angelegt wird
	 * 
	 * @param budget
	 *            das betroffene Budget
	 * @param nummern
	 *            wird fuer den "mehr" Button bei UT8 gebraucht
	 * @param isUt8
	 *            true: das Budget ist UT8; false: das Budget ist nicht UT8
	 * @return boolean-Wert; true: der Wert kann gespeichert werden; false: der
	 *         Wert ist zu hoch
	 */
	public boolean ueberpruefeWertNeuesBudget(double budget, Vector nummern,
			boolean isUt8) {
		berechneUebersumme(nummern);

		if (!isUt8) {
			if ((summeGeplantesBudgetTabelle(tabelle, isUt8, nummern)
					- uebersumme + budget) > uebersumme) {
				ok = false;
			} else {
				ok = true;
			}
		} else if ((summeGeplantesBudgetTabelle(tabelle, isUt8, nummern) + budget) > uebersumme) {
			ok = false;
		} else {
			ok = true;
		}

		return ok;

	}

	/**
	 * prueft, ob der neu eingegeben Betrag nicht groeßer ist, als der
	 * verfuegbare Betrag; wird nur fuer LMB verwendet
	 * 
	 * @param budget
	 *            das betroffene Budget
	 * @param kennung
	 *            1: LMB1; 2: LMB2
	 * @return boolean-Wert; true: der Wert kann gespeichert werden; false: der
	 *         Wert ist zu hoch
	 */
	public boolean ueberpruefeLMB(double budget, int kennung) {
		if (kennung == 1) // LMB1
		{
			uebersumme = geplantesBudgetLMB(kennung);
		} else // LMB2
		{
			uebersumme = geplantesBudgetLMB(kennung);
		}

		aktuellGeplantes = getAktuellGeplantes(tabelle);
		if ((summeGeplantesBudgetTabelleLMB(kennung) - uebersumme
				- aktuellGeplantes + budget) > uebersumme) {
			ok = false;
		} else {
			ok = true;
		}

		return ok;
	}

	/**
	 * prueft, ob der neu eingegeben Betrag nicht groeßer ist, als der
	 * verfuegbare Betrag; wird aufgerufen, wenn ein geplanter Betrag geaendert
	 * wird
	 * 
	 * @param budget
	 *            das betroffene Budget
	 * @param nummern
	 *            wird fuer den "mehr" Button bei UT8 gebraucht
	 * @return boolean-Wert; true: der Wert kann gespeichert werden; false: der
	 *         Wert ist zu hoch
	 */
	public boolean ueberpruefeWert(double budget, Vector nummern) {

		berechneUebersumme(nummern);

		aktuellGeplantes = getAktuellGeplantes(tabelle);
		if (isUT8 == 1) {
			if ((summeGeplantesBudgetTabelle(tabelle, true, nummern)
					- aktuellGeplantes + budget) > uebersumme) {
				ok = false;
			} else
				ok = true;

		} else {
			if ((summeGeplantesBudgetTabelle(tabelle, false, nummern)
					- aktuellGeplantes - uebersumme + budget) > uebersumme) {
				ok = false;
			} else {
				ok = true;
			}
		}

		return ok;
	}

	/**
	 * berechnet die Summe vom Ueberbudget; also bei einer UT8-Hauptkostenstelle
	 * z.B. die Summe des dazugehoerigen Bereiches; bei einer UT3-Kostenstelle
	 * die Summe des Hauptbudgets
	 * 
	 * @param nummern
	 *            wird fuer den "mehr" Button bei UT8 gebraucht
	 */
	public void berechneUebersumme(Vector nummern) {
		if (tabelle == "kostenstelleut8") {
			uebersumme = geplantesBudgetUT8("hauptkostenstelleut8", nummern);
			isUT8 = 1;
		} else if (tabelle == "hauptkostenstelleut8") {
			uebersumme = geplantesBudgetUT8("bereichut8", nummern);
			isUT8 = 1;
		} else if (tabelle == "bereichut8") {
			uebersumme = geplantesBudgetUT8("hauptbereichut8", nummern);
			isUT8 = 1;
		} else {
			uebersumme = geplantesBudget(tabelle);
			isUT8 = 0;
		}
	}

	/**
	 * berechnet die Summe der geplanten Budgets in der Tabelle, in der man den
	 * Betrag aendern will
	 * 
	 * @param tabelle
	 *            in welcher Tabelle der Betrag veraendert wird
	 * @param isUt8
	 *            true: das Budget ist UT8; false: das Budget ist nicht UT8
	 * @param nummern
	 *            wird fuer den "mehr" Button bei UT8 gebraucht
	 * @return ein double-Wert; die Summe der geplanten Budgets in der aktuellen
	 *         Tabelle
	 */
	public double summeGeplantesBudgetTabelle(String tabelle, boolean isUt8,
			Vector nummern) {
		summe = 0;
		try {
			if (isUt8) {
				query = "select geplant from "
						+ tabelle
						+ " where hauptnummer="
						+ Integer.parseInt(nummern.get(nummern.size() - 1)
								.toString());
			} else {
				query = "select geplant from " + tabelle + ";";
			}

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				summe += rs.getDouble("geplant");
			}

			rs.close(); // Offene Variablen, abfragen und Connention schließen
			stmt.close(); // Befehl schließen

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return summe;
	}

	/**
	 * berechnet fuer die Methode "berechneUebersumme" das geplante Budget
	 * 
	 * @param ueberTabelle
	 *            die betroffene Tabelle
	 * @param nummern
	 *            wird fuer den "mehr" Button bei UT8 gebraucht
	 * @return ein double-Wert; der geplante Wert aus der mitgeschickten Tabelle
	 */
	public double geplantesBudgetUT8(String ueberTabelle, Vector nummern) {
		summe = 0;
		try {
			stmt = con.createStatement();

			int nummer = 50;
			if ((nummern.size()) != 0)
				nummer = Integer.parseInt(nummern.get(nummern.size() - 1)
						.toString());

			query = "select geplant from " + ueberTabelle + " where nummer="
					+ nummer;
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				summe = rs.getDouble("geplant");
			}

			rs.close(); // Offene Variablen, abfragen und Connention schließen
			stmt.close(); // Befehl schließen

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return summe;
	}

	/**
	 * berechnet das geplante Budget des Hauptbudgets fuer UT3 und Hauptbereiche
	 * in UT8
	 * 
	 * @param tabelle
	 *            die betroffene Tabelle
	 * @return ein double-Wert; der geplante Wert des Hauptbudgets
	 */
	public double geplantesBudget(String tabelle) {
		try {
			stmt = con.createStatement();
			query = "select geplant from " + tabelle + " where nummer=1;";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				geplant = rs.getDouble("geplant");
			}

			rs.close(); // Offene Variablen, abfragen und Connention schließen
			stmt.close(); // Befehl schließen

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return geplant;
	}

	/**
	 * berechnet das geplante Budget des Hauptbudgets fuer LMB
	 * 
	 * @param kennung
	 *            1: LMB1; 2: LMB2
	 * @return ein double-Wert; der geplante Wert des Hauptbudgets
	 */
	public double geplantesBudgetLMB(int kennung) {
		try {
			stmt = con.createStatement();
			query = "select geplant from lmb where nummer=" + kennung;
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				geplant = rs.getDouble("geplant");
			}

			rs.close(); // Offene Variablen, abfragen und Connention schließen
			stmt.close(); // Befehl schließen

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return geplant;
	}

	/**
	 * berechnet den geplanten Wert von dem Feld, bei dem man diesen Wert
	 * aendern will
	 * 
	 * @param tabelle
	 *            die betroffene Tabelle
	 * @return ein double-Wert; der aktuell geplante Wert von dem Feld, das
	 *         geaendert werden soll
	 */
	public double getAktuellGeplantes(String tabelle) {
		try {
			stmt = con.createStatement();
			/*if (tabelle.equals("kostenstelleut8")
					|| tabelle.equals("hauptkostenstelleut8")
					|| tabelle.equals("bereichut8")
					|| tabelle.equals("hauptbereichut8"))
				query = "select geplant from " + tabelle
						+ " where nummerSelbst=" + zeilennummer + ";";
			else*/
			query = "select geplant from " + tabelle + " where nummer="
						+ zeilennummer + ";";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				aktuellGeplantes = rs.getDouble("geplant");
			}

			rs.close(); // Offene Variablen, abfragen und Connention schließen
			stmt.close(); // Befehl schließen

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return aktuellGeplantes;
	}

	/**
	 * berechnet die Summe der geplanten Werte fuer ein LMB-Budget (LMB1 oder LMB2)
	 * 
	 * @param kennung
	 *            1: LMB1; 2: LMB2
	 * @return ein double-Wert; die Summe der geplanten Werte fuer ein LMB-Budget
	 */
	public double summeGeplantesBudgetTabelleLMB(int kennung) {
		try {
			stmt = con.createStatement();
			query = "select geplant from lmb where kennung=" + kennung;
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				summe += rs.getDouble("geplant");
			}

			rs.close(); // Offene Variablen, abfragen und Connention schließen
			stmt.close(); // Befehl schließen

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return summe;
	}

}
