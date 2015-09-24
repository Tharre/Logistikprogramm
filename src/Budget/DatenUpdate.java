package Budget;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;

/**
 *In der Klasse DatenUpdate werden die Daten in der Datenbank geaendert
 *<p>
 * Title: DatenUpdate
 * 
 * @author Haupt, Liebhart
 **/
public class DatenUpdate {

	// Connections
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	/** Connection zur Logistikdatenbank **/
	private Connection conL;

	// sonstiges
	/**
	 * Hilfsvariable um zu erkennen ob es einen Fehler beim Loeschen gegeben hat
	 **/
	private boolean keinFehler = true;
	/** Objekt der Klasse Abteilungsanteile **/
	private BezAbteilungsanteile aa;
	/** gesperrter Betrag **/
	private double gesperrt;
	/** Kennung fuer LMB **/
	private int kennung;
	/** Statement fuer einen SQL-Befehl **/
	private Statement stmt;
	/** Befehl fuer die Datenbank **/
	private String query;

	/**
	 * 
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 */
	public DatenUpdate(Connection con, Connection conL) {
		this.con = con;
		this.conL = conL;

	}

	/**
	 * aendert das geplante Budget fuer einen bestimmten Datensatz
	 * 
	 * @param tabelle
	 *            Name der Tabelle
	 * @param zeile
	 *            Zeilennummer des Datensatzes
	 * @param budget
	 *            Betrag in der Datenbank, der geaendert werden soll
	 */
	public void update(String tabelle, int zeile, double budget) {
		try {
			stmt = con.createStatement();

			query = "update " + tabelle + " set geplant=" + budget
					+ " where nummer=" + zeile;
			stmt.executeUpdate(query);

			stmt.close();

			if (tabelle.equals("abteilungut3")) {
				aa = new BezAbteilungsanteile(con, conL);

			}

		} catch (SQLException ex) // moegliche Fehler waehrend der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

	}

	/**
	 * aendert den Bezahlstatus einer Bestellung
	 * 
	 * @param bestId
	 *            ID der Bezahlung
	 * @param status
	 *            Status der in der Bezahlung geaendert wird
	 */
	public void aendereStatusBezahlung(int bestId, int status) {
		try {

			stmt = conL.createStatement();
			query = "update bestellung set statusbez=" + status
					+ " where bestId=" + bestId;

			stmt.executeUpdate(query);
			
		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			ex.printStackTrace();
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}
	}
	


	/**
	 * aendert den Status einer Bestellposition
	 * 
	 * @param id
	 *            ID der Bestellposition
	 * @param status
	 *            Status der in der Datenbank gespeichert wird
	 */
	public void aendereStatusBestellPos(int id, int status) {

		try {

			stmt = conL.createStatement();
			query = "update bestpos set statusbez=" + status + " where id="
					+ id;

			stmt.executeUpdate(query);
			
		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			ex.printStackTrace();
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}
	}

	/**
	 * vermindert den Betrag der Spalte gesperrt
	 * 
	 * @param tabelle
	 *            Name der Tabelle
	 * @param betrag
	 *            Betrag der abegzogen wird
	 * @param kostenstelle
	 *            Name der Kostenstelle, bei der der Betrag geaendert wird
	 */
	public void aendereGesperrtMinus(String tabelle, double betrag,
			String kostenstelle, String budget) {

		try {
			stmt = con.createStatement();

			if (budget.equals("LMB1")) {
				query = "update " + tabelle + " set gesperrt=gesperrt- "
						+ betrag + " where name like '" + kostenstelle
						+ "' AND kennung=1";
			} else if (budget.equals("LMB2")) {
				query = "update " + tabelle + " set gesperrt=gesperrt- "
						+ betrag + " where name like '" + kostenstelle
						+ "' AND kennung=2";

			} else {
				query = "update " + tabelle + " set gesperrt=gesperrt- "
						+ betrag + " where name like '" + kostenstelle + "'";
			}

			stmt.executeUpdate(query);

		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

	}

	/**
	 * zieht den Betrag von der Spalte gesperrt ab und addiert ihn zu dem Betrag
	 * der Spalte ausgegeben
	 * 
	 * @param tabelle
	 *            Name der Tabelle
	 * @param betrag
	 *            Betrag der abgebucht bzw. aufgebucht wird
	 * @param kostenstelle
	 *            bei welcher Kostenstelle umgebucht wird
	 * @param budget
	 *            bei welchem Budget umgebucht wird
	 */
	public void aendereGesperrtZuAusgegeben(String tabelle, double betrag,
			double betragPreisGesamt, String kostenstelle, String budget) {

		try {
			stmt = con.createStatement();

			
			if (budget.equals("LMB1")) {
				query = "update " + tabelle + " set gesperrt=gesperrt- "
						+ betragPreisGesamt + " ,ausgegeben =ausgegeben+ "
						+ betrag + " where name like '" + kostenstelle
						+ "' AND kennung=1";
			} else if (budget.equals("LMB2")) {
				query = "update " + tabelle + " set gesperrt=gesperrt- "
						+ betragPreisGesamt + " ,ausgegeben =ausgegeben+ "
						+ betrag + " where name like '" + kostenstelle
						+ "' AND kennung=2";

			} else if (budget.equals("UT3") || budget.equals("UT8")) {
				query = "update " + tabelle + " set gesperrt=gesperrt- "
						+ betragPreisGesamt + " ,ausgegeben =ausgegeben+ "
						+ betrag + " where name like '" + kostenstelle + "'";

			} else // bei Sonderbudget
			{
				query = "update " + tabelle + " set gesperrt=gesperrt- "
						+ betragPreisGesamt + " ,ausgegeben =ausgegeben+ "
						+ betrag + " where name like '" + budget + "'";
			}

			stmt.executeUpdate(query);
			
		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

	}

	/**
	 * addiert den Bestellbetrag der bezahlten Rechnung zur Spalte "ausgegeben";
	 * wird aufgerufen, wenn eine Rechnung ueber den Landesschulrat bezahlt
	 * wurde
	 * 
	 * @param betrag
	 *            der Betrag, der dazu addiert wird
	 * @param kostenstelle
	 *            das Sonderbudget, an dem der Betrag geaendert wird
	 */
	public void aendereAusgegebenMehr(double betrag, String kostenstelle) {

		try {
			stmt = con.createStatement();

			query = "update sonderbudget set ausgegeben =ausgegeben+ " + betrag
					+ " where name like '" + kostenstelle + "'";

			stmt.executeUpdate(query);


		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

	}

	/**
	 * zieht den Betrag von der Spalte gesperrt ab und addiert ihn zu dem Betrag
	 * der Spalte ausgegeben
	 * 
	 * @param betragEL
	 *            EL- Betrag
	 * @param betragET
	 *            ET- Betrag
	 * @param betragWI
	 *            WI- Betrag
	 * @param betragMI
	 *            MI- Betrag
	 * @param betragLT
	 *            LT- Betrag
	 * 
	 * @param budget
	 *            bei welchem Budget soll umgebucht werden
	 */
	public void aendereAusgegebenAnteile(double betragEL, double betragET,
			double betragWI, double betragMI, double betragLT, String budget) {

		try {
			stmt = con.createStatement();

			if (budget.equals("LMB1")) {
				query = "update lmb set ausgegeben =ausgegeben+ " + betragET
						+ " where name like 'ET' AND kennung=1";

				ausfuehren(query);

				query = "update lmb set ausgegeben =ausgegeben+ " + betragEL
						+ " where name like 'EL' AND kennung=1";

				ausfuehren(query);

				query = "update lmb set ausgegeben =ausgegeben+ " + betragWI
						+ " where name like 'WI' AND kennung=1";

				ausfuehren(query);

				query = "update lmb set ausgegeben =ausgegeben+ " + betragMI
						+ " where name like 'MI' AND kennung=1";

				ausfuehren(query);

				query = "update lmb set ausgegeben =ausgegeben+ " + betragLT
						+ " where name like 'LT' AND kennung=1";

				ausfuehren(query);

			} else if (budget.equals("LMB2")) {
				query = "update lmb set ausgegeben =ausgegeben+ " + betragET
						+ " where name like 'ET' AND kennung=2";

				ausfuehren(query);

				query = "update lmb set ausgegeben =ausgegeben+ " + betragEL
						+ " where name like 'EL' AND kennung=2";

				ausfuehren(query);

				query = "update lmb set ausgegeben =ausgegeben+ " + betragWI
						+ " where name like 'WI' AND kennung=2";

				ausfuehren(query);

				query = "update lmb set ausgegeben =ausgegeben+ " + betragMI
						+ " where name like 'MI' AND kennung=2";

				ausfuehren(query);

				query = "update lmb set ausgegeben =ausgegeben+ " + betragLT
						+ " where name like 'LT' AND kennung=2";

				ausfuehren(query);

			} else {
				query = "update abteilungut3 set ausgegeben =ausgegeben+ "
						+ betragEL + " where name like 'EL'";

				ausfuehren(query);

				query = "update abteilungut3 set ausgegeben =ausgegeben+ "
						+ betragET + " where name like 'ET'";

				ausfuehren(query);

				query = "update abteilungut3 set ausgegeben =ausgegeben+ "
						+ betragWI + " where name like 'WI'";

				ausfuehren(query);

				query = "update abteilungut3 set ausgegeben =ausgegeben+ "
						+ betragMI + " where name like 'MI'";

				ausfuehren(query);

			}

		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

	}

	/**
	 * verringert den gesperrten Betrag bei einer Kostenstelle, die verschiedene
	 * Anteile von Abteilungen hat (z.B. gemeinsam, MI/WI,...)
	 * 
	 * @param betrag
	 *            der Betrag, der abgezogen wird
	 * @param budget
	 *            das Budget, das betroffen ist
	 * @param kostenstelle
	 *            die Kostenstelle, bei der der Betrag abgezogen wird
	 */
	public void aenderegesperrtMinusMitAnteil(double betrag, String budget,
			String kostenstelle) {

		if (budget.equals("LMB1")) {
			query = "update lmb set gesperrt=gesperrt- " + betrag
					+ " where name like '" + kostenstelle + "' and kennung=1";

			ausfuehren(query);
		} else if (budget.equals("LMB2")) {
			query = "update lmb set gesperrt=gesperrt- " + betrag
					+ " where name like '" + kostenstelle + "' and kennung=2";

			ausfuehren(query);

		} else {

			query = "update abteilungut3 set gesperrt=gesperrt- " + betrag
					+ " where name like '" + kostenstelle + "'";

			ausfuehren(query);

		}

	}

	/**
	 * loescht einen Datensatz aus der Datenbank
	 * 
	 * @param zeilennummer
	 *            Nummer des Datensatzes
	 * @param tabelleName
	 *            Name der Tabelle
	 */
	public void loescheDatensatz(int zeilennummer, String tabelleName) {

		if (tabelleName.equals("hauptbereichut8")) {

			query = "delete from kostenstelleut8 where hauptnummer=(select nummer from hauptkostenstelleut8 where hauptnummer=(select nummer from bereichut8 where hauptnummer="
					+ zeilennummer + "))";

			ausfuehren(query);

			query = "delete from hauptkostenstelleut8 where hauptnummer=(select nummer from bereichut8 where hauptnummer="
					+ zeilennummer + ")";

			ausfuehren(query);

			query = "delete from bereichut8 where hauptnummer=" + zeilennummer;

			ausfuehren(query);

			query = "delete from hauptbereichut8 where nummer=" + zeilennummer;

			ausfuehren(query);

		} else if (tabelleName.equals("bereichut8")) {

			query = "delete from kostenstelleut8 where hauptnummer=(select nummer from hauptkostenstelleut8 where hauptnummer="
					+ zeilennummer + ")";

			ausfuehren(query);

			query = "delete from hauptkostenstelleut8 where hauptnummer="
					+ zeilennummer;
			ausfuehren(query);

			query = "delete from bereichut8 where nummer=" + zeilennummer;

			ausfuehren(query);

		} else if (tabelleName.equals("hauptkostenstelleut8")) {
			query = "delete from hauptkostenstelleut8 where nummer="
					+ zeilennummer;
			ausfuehren(query);

			query = "delete from kostenstelleut8 where hauptnummer="
					+ zeilennummer;

			ausfuehren(query);

		} else if (tabelleName.equals("projekt")) {
			query = "delete from projekt where nummerSelbst=" + zeilennummer;
			ausfuehren(query);

		} else if (tabelleName.equals("sonderbudget")) {

			query = "delete from " + tabelleName + " where nummer= "
					+ zeilennummer;
			ausfuehren(query);
		} else {

			query = "select * from " + tabelleName + " where nummer="
					+ zeilennummer;

			try {
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {
					gesperrt = rs.getDouble("gesperrt");

				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
			if (tabelleName.equals("lmb")) {
				query = "select * from " + tabelleName + " where nummer="
						+ zeilennummer;

				try {
					stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);

					while (rs.next()) {
						kennung = rs.getInt("kennung");

					}
				} catch (SQLException e) {

					e.printStackTrace();
				}

				query = "delete from " + tabelleName + " where nummer= "
						+ zeilennummer;
				ausfuehren(query);
				query = "update " + tabelleName + " set gesperrt=gesperrt-"
						+ gesperrt + "where nummer=" + kennung;
				ausfuehren(query);
			} else {
				query = "delete from " + tabelleName + " where nummer= "
						+ zeilennummer;
				ausfuehren(query);

				query = "update " + tabelleName + " set gesperrt=gesperrt-"
						+ gesperrt + "where nummer=1";
				ausfuehren(query);
			}
		}
		if (keinFehler)
			JOptionPane.showMessageDialog(null,
					"Die Datensätze wurden gelöscht!");

	}

	/**
	 * setzt den Befehl fuer die Datenbank ab
	 * 
	 * @param query
	 *            Befehl fuer die Datenbank
	 */
	public void ausfuehren(String query) {
		try {
			stmt = con.createStatement();

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fehler!");

			if (keinFehler)
				keinFehler = false;

			e.printStackTrace();
		}

	}

	public void ausfuehrenLogistik(String query) {
		try {
			stmt = conL.createStatement();

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fehler!");

			if (keinFehler)
				keinFehler = false;

			e.printStackTrace();
		}

	}

	/**
	 * aendert einen Eintrag in der Datenbank
	 * 
	 * @param zeilennummer
	 *            Nummer des Datensatzes
	 * @param name
	 *            Name der geaendert werden soll
	 * @param kennnummer
	 *            Kennnummer der Tabelle
	 * @param spalte
	 *            Name einer Spalte in der Tabelle
	 */
	public void aendereDatensatz(int zeilennummer, String name, int kennnummer,
			String spalte) {

		String tabelleName = getNameZuKennnummer(kennnummer);

		try {
			stmt = con.createStatement();

			if (kennnummer == 2)

				query = "update projekt set " + spalte + "='" + name
						+ "' where nummerSelbst=" + zeilennummer;
			else
				query = "update " + tabelleName + " set " + spalte + "='"
						+ name + "' where nummer=" + zeilennummer;

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fehler!");

			e.printStackTrace();
		}

	}

	/**
	 * aendert das Budget, das in einer Bestellung gespeichert ist; wird nur
	 * aufgerufen, wenn die Bestellung vom Landesschulrat bezahlt wurde
	 * 
	 * @param budget
	 *            das Budget, das nun gespeichert wird
	 * @param wNummer
	 *            die W-Nummer der Bestellung
	 */
	public void aendereBudgetBeiBestellung(String budget, String wNummer) {

		try {
			Statement stmtl = conL.createStatement();

			query = "update bestellung set budget='" + budget
					+ "' where wNummer like '" + wNummer + "'";

			
			stmtl.executeUpdate(query);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * uebernimmt die gesperrten Betraege vom Vorjahr fuer das naechste Jahr
	 * 
	 * @param tabelle
	 *            Name der Tabelle
	 * @param nummer
	 *            Nummer des Datensatzes
	 * @param gesperrt
	 *            gesperrter Betrag
	 * @param conB_alt
	 *            Connection
	 */
	public void gesperrtUebernehmen(String tabelle, int nummer,
			double gesperrt, Connection conB_alt) {
		boolean existiert = false;
		try {
			stmt = conB_alt.createStatement();

			query = "select * from " + tabelle + " where nummer=" + nummer;
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
				existiert = true;

			if (existiert) {
				query = "update " + tabelle + " set gesperrt=" + gesperrt
						+ " where nummer=" + nummer;
				stmt.executeUpdate(query);
			} else {

				neueKSTanlegen(tabelle, nummer, gesperrt, conB_alt);
			}

		} catch (SQLException e) {

		}

	}

	/**
	 * eine neue Kostenstelle wird angelegt, wenn im Vorjahr ein Betrag an einer
	 * Kostenstelle gesperrt wurde, die es im naechsten Jahr nicht mehr gibt,
	 * also die nicht zu den Standarddaten gehoert
	 * 
	 * @param tabelle
	 *            die Tabelle, die betroffen ist
	 * @param nummer
	 *            die Nummer der Kostenstelle
	 * @param gesperrt
	 *            der gesperrte Wert
	 * @param conB_alt
	 *            die Connection zur Datenbank vom Vorjahr
	 */
	public void neueKSTanlegen(String tabelle, int nummer, double gesperrt,
			Connection conB_alt) {

		String name = "";
		int kennung = 0;
		int hauptNr = 0;
		String nrSelbst = "";

		try {
			stmt = con.createStatement();
			query = "select * from " + tabelle + " where nummer=" + nummer;
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				name = rs.getString("name");
				if (tabelle.equals("lmb"))
					kennung = rs.getInt("kennung");
				if (tabelle.equals("kostenstelleut8")) {
					hauptNr = rs.getInt("hauptnummer");
					nrSelbst = rs.getString("NummerSelbst");
					kennung = 3;
				}
			}

			stmt = conB_alt.createStatement();
			if (kennung == 0)// UT3, Sonderbudget
				query = "insert into " + tabelle
						+ "(nummer,name,gesperrt) values(" + nummer + ",'"
						+ name + "'," + gesperrt + ")";
			else if (kennung == 3)// UT8
				query = "insert into " + tabelle
						+ "(nummer,hauptnummer,name,gesperrt,NummerSelbst) "
						+ "values(" + nummer + "," + hauptNr + ",'" + name
						+ "'," + gesperrt + ",'" + nrSelbst + "')";
			else
				// LMB
				query = "insert into " + tabelle
						+ "(nummer,name,kennung, gesperrt) " + "values("
						+ nummer + ",'" + name + "'," + kennung + ","
						+ gesperrt + ")";

			stmt.executeUpdate(query);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * setzt die gesperrten Werte im Vorjahresbudget auf 0
	 * 
	 * @param tabellenNamen
	 *            die betroffene Tabelle
	 * @param conB_alt
	 *            Connection zur Vorjahres-Datenbank
	 */
	public void setzeGesperrtaufNull(String tabellenNamen, Connection conB_alt) {
		try {
			stmt = conB_alt.createStatement();

			query = "update " + tabellenNamen + " set gesperrt=0;";

			stmt.executeUpdate(query);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * summiert die gesperrten Betraege der LMB- Kostenstellen auf und speichert
	 * diesen Betrag zum gesperrten Hauptbudget
	 */
	public void gesperrtHauptbudgetLMB() {
		double summe = 0;

		for (int i = 1; i < 3; i++) {
			summe = 0;
			try {
				stmt = con.createStatement();

				query = "select sum(gesperrt) from lmb where kennung=" + i
						+ " AND nummer<>" + i + ";";

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					summe = rs.getDouble("sum(gesperrt)");
				}

				query = "update lmb set gesperrt=" + summe + " where nummer="
						+ i;

				stmt.executeUpdate(query);

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * man bekommt den Tabellennamen zu einer Kennnummer
	 * 
	 * @param kennnummer
	 */
	public String getNameZuKennnummer(int kennnummer) {
		String tabellennamen = "";

		switch (kennnummer) {
		case 1:
			tabellennamen = "abteilungut3";
			break;
		case 2:
			tabellennamen = "projekt";
			break;
		case 3:
			tabellennamen = "lmb";
			break;
		case 4:
			tabellennamen = "sonderbudget";
			break;
		case 5:
			tabellennamen = "hauptbereichut8";
			break;
		case 6:
			tabellennamen = "bereichut8";
			break;
		case 7:
			tabellennamen = "hauptkostenstelleut8";
			break;
		case 8:
			tabellennamen = "kostenstelleut8";
			break;
		case 9:
			tabellennamen = "lmb";

		}

		return tabellennamen;

	}

	public void loescheBuchungen(int tagBis, int monatBis, int jahrBis,
			int tagVon, int monatVon, int jahrVon) {

		Date datumVon = new Date(jahrVon - 1900, monatVon - 1, tagVon);
		Date datumBis = new Date(jahrBis - 1900, monatBis - 1, tagBis);

		query = "delete from buchungen where datum between "
				+ (datumBis.getTime() / 1000) + " and "
				+ (datumVon.getTime() / 1000);

		ausfuehrenLogistik(query);

		query = "delete from buchungen_firma where datum between "
				+ (datumBis.getTime() / 1000) + " and "
				+ (datumVon.getTime() / 1000);
		ausfuehrenLogistik(query);

	}

	public void loescheRechnung(String wNummer) {

		query = "delete from rechnung where wNummer like '" + wNummer + "'";

		ausfuehren(query);

	}

	public boolean korrigiereRechnung(int id, String wNummer,
			double bestellbetrag, double rechnungsbetrag, double skonto,
			String externeN, String interneN, String zahlart, String bhbeleg,
			String inventarnummer, double sonderabzug) {

		try {
			stmt = con.createStatement();

			query = "update rechnung set bestellbetrag=" + bestellbetrag
					+ " where nummer=" + id;

			stmt.executeUpdate(query);

			query = "update rechnung set wNummer='" + wNummer
					+ "' where nummer=" + id;

			stmt.executeUpdate(query);

			query = "update rechnung set rechnungsbetrag=" + rechnungsbetrag
					+ " where nummer=" + id;

			stmt.executeUpdate(query);
			query = "update rechnung set skonto=" + skonto + " where nummer="
					+ id;

			stmt.executeUpdate(query);
			query = "update rechnung set externeNummer='" + externeN
					+ "' where nummer=" + id;

			stmt.executeUpdate(query);
			query = "update rechnung set interneNummer='" + interneN
					+ "' where nummer=" + id;

			stmt.executeUpdate(query);
			query = "update rechnung set zahlart=" + zahlart + " where nummer="
					+ id;

			stmt.executeUpdate(query);

			query = "update rechnung set buchhaltungsbelege='" + bhbeleg
					+ "' where nummer=" + id;

			stmt.executeUpdate(query);
			query = "update rechnung set inventarnummer='" + inventarnummer
					+ "' where nummer=" + id;

			stmt.executeUpdate(query);
			query = "update rechnung set sonderabzug=" + sonderabzug
					+ " where nummer=" + id;

			stmt.executeUpdate(query);
			return true;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return false;
	}
	
	
	public void jahresendeNullSetzen(Connection con, String tabelle)
	{
		Vector<Integer> id = new Vector<Integer>();
		query = "select nummer from "+tabelle;
		Statement stmt;
		try {
			stmt = con.createStatement();
		
		ResultSet rs1 = stmt.executeQuery(query);
		while(rs1.next())
		{
			id.add(rs1.getInt("nummer"));
		}
		
		for(int i=0; i<id.size(); i++)
		{
			setzeNull(con, id.get(i), tabelle);
		}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setzeNull(Connection con, int id, String tabelle)
	{
		if(tabelle.equals("bereichut8")||tabelle.equals("hauptbereichut8")||tabelle.equals("hauptkostenstelleut8"))
			query = "update "+tabelle+" set geplant=0 where nummer="+id;
		else if(tabelle.equals("sonderbudget")||tabelle.equals("kostenstelleut8"))
			query = "update "+tabelle+" set geplant=0, ausgegeben=0 where nummer="+id;
		else if(tabelle.equals("abteilungut3"))
			query = "update "+tabelle+" set geplant=0, ausgegeben=0, festgeplant=0, edvhatAnteil=0 where nummer="+id;
		else if(tabelle.equals("lmb"))
			query = "update "+tabelle+" set geplant=0, ausgegeben=0, festgeplant=0 where nummer="+id;

		Statement stmt;
		try {
			stmt = con.createStatement();
		
		stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void aendereBezahlt(int bestposId, double menge)
	{
		query = "update bestpos set bezahlt=bezahlt+"+menge+" where id="+bestposId;
		System.out.println("QUERY "+query);
		Statement stmt;
		try {
			stmt = conL.createStatement();
		
		stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
