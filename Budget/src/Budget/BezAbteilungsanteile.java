package Budget;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 *In der Klasse Abteilungsanteile werden die Anteile der Kostenstellen
 * berechnet und in die Datenbank gespeichert
 *<p>
 * Title: Abteilungsanteile
 * 
 * @author Haupt Marianne, Liebhart Stefanie
 **/
public class BezAbteilungsanteile {

	// double
	/** Betrag des Hauptbudgets **/
	private double budget;
	/** EDV- HAT Anteil **/
	private double edvHatAnteil;
	/** Abteilungsanteil **/
	private double abteilungAnteil;
	/** Werkstaettenanteil **/
	private double werkstattAnteil;
	/** HAT- Anteil in Prozent **/
	private double prozentHAT;
	/** EDV- Anteil in Prozent **/
	private double prozentEDV;
	/** LT- Anteil in Prozent **/
	private double ltAnteil;
	/** Ein sechstel des Budgets **/
	private double einSechstel;

	// String
	/** Befehl 1 **/
	private String query1;
	/** Befehl 2 **/
	private String query2;
	/** Befehl 3 **/
	private String query3;
	/** Befehl 4 **/
	private String query4;
	/** Befehl 5 **/
	private String query5;
	/** Befehl 6 **/
	private String query6;

	// sonstiges
	/** Statement **/
	private Statement stmt;
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	/**
	 * EDV- HAT- Anteil in Prozent an der Stelle [0]: EDV- Anteil; an der Stelle
	 * [1]: HAT- Anteil
	 **/
	private double[] edvhatAnteile;
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 */
	public BezAbteilungsanteile(Connection con, Connection conL) {

		this.con = con;

		di = new DatenImport(con, conL);

	}

	/**
	 * berechnet den LT- Anteil und die Abteilungsanteile
	 * 
	 * @param kennnummer
	 *            Kennnummer, um welches LMB es sich handelt
	 */
	public void berechneAnteileLMB(int kennnummer) {
		try {
			ltAnteil = di.getLTAnteil(kennnummer);
			budget = di.getHauptbudget(kennnummer);

			ltAnteil = runde((budget * ltAnteil) / 100);
			einSechstel = runde((budget - ltAnteil) / 6);

			speichereInDatenbankLT(kennnummer);

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"Bitte geben Sie eine Zahl ein.");

		}

	}

	/**
	 * speichert den LT- Anteile in die Datenbank
	 * 
	 * @param kennnummer
	 *            Kennnummer, um welches LMB es sich handelt; Kennnummer 3=LMB
	 *            1, Kennnummer 9=LMB1_2011
	 */
	public void speichereInDatenbankLT(int kennnummer) {

		try {
			stmt = con.createStatement();

			if (kennnummer == 3) {
				query1 = "update lmb set festgeplant=" + ltAnteil
						+ " where nummer=7 and kennung=1";
				query2 = "update lmb set festgeplant=" + einSechstel
						+ " where nummer=5 and kennung=1";
				query3 = "update lmb set festgeplant=" + einSechstel
						+ " where nummer=4 and kennung=1";
				query4 = "update lmb set festgeplant=" + einSechstel
						+ " where nummer=3 and kennung=1";
				query5 = "update lmb set festgeplant=" + einSechstel
						+ " where nummer=6 and kennung=1";
				query6 = "update lmb set festgeplant=" + einSechstel * 2
						+ " where nummer=8 and kennung=1";
			} else {
				query1 = "update lmb set festgeplant=" + ltAnteil
						+ " where nummer=16 and kennung=2";
				query2 = "update lmb set festgeplant=" + einSechstel
						+ " where nummer=14 and kennung=2";
				query3 = "update lmb set festgeplant=" + einSechstel
						+ " where nummer=13 and kennung=2";
				query4 = "update lmb set festgeplant=" + einSechstel
						+ " where nummer=12 and kennung=2";
				query5 = "update lmb set festgeplant=" + einSechstel
						+ " where nummer=15 and kennung=2";
				query6 = "update lmb set festgeplant=" + einSechstel * 2
						+ " where nummer=17 and kennung=2";

			}

			stmt.executeUpdate(query1);
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3);
			stmt.executeUpdate(query4);
			stmt.executeUpdate(query5);
			stmt.executeUpdate(query6);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Fehler beim Einlesen in die Datenbank.");
			e.printStackTrace();
		}

	}

	/**
	 * berechnet den Abteilungs-,EDV-,HAT- und Werkstaettenanteil
	 */
	public void berechneAnteileUT3() {

		budget = di.getHauptbudget(1);
		edvhatAnteile = di.getEDVHATAnteil();
		prozentEDV = edvhatAnteile[0];
		prozentHAT = edvhatAnteile[1];

		edvHatAnteil = runde((budget * (prozentEDV + prozentHAT) / 100) / 4);

		werkstattAnteil = runde(budget / 6 * 2);

		abteilungAnteil = runde((budget - werkstattAnteil - edvHatAnteil * 4) / 4);

		speichereInDatenbank();

	}

	/**
	 * speichert den EDV-HAT- Anteil der Abteilungen und den Werkstaettenanteil
	 * in die Datenbank
	 */
	public void speichereInDatenbank() {

		try {
			stmt = con.createStatement();

			query1 = "update abteilungut3 set festgeplant=" + werkstattAnteil
					+ " where nummer=6";
			query2 = "update abteilungut3 set festgeplant=" + abteilungAnteil
					+ ",EDVHATAnteil='" + edvHatAnteil + "' where nummer=2";
			query3 = "update abteilungut3 set festgeplant=" + abteilungAnteil
					+ ",EDVHATAnteil='" + edvHatAnteil + "' where nummer=3";
			query4 = "update abteilungut3 set festgeplant=" + abteilungAnteil
					+ ",EDVHATAnteil='" + edvHatAnteil + "' where nummer=5";
			query5 = "update abteilungut3 set festgeplant=" + abteilungAnteil
					+ " ,EDVHATAnteil='" + edvHatAnteil + "' where nummer=4";
			query6 = "update abteilungut3 set festgeplant=" + budget
					+ " where nummer=1";

			stmt.executeUpdate(query1);
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3);
			stmt.executeUpdate(query4);
			stmt.executeUpdate(query5);
			stmt.executeUpdate(query6);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Fehler beim Einfuegen in die Datenbank.");
			e.printStackTrace();
		}

	}

	/**
	 * rundet eine Zahl auf zwei Kommastellen
	 * 
	 * @param zahl
	 *            Zahl, die gerundet werden soll
	 * @return gerundete Zahl
	 */
	public double runde(double zahl) {
		zahl = zahl * 100;
		zahl = Math.round(zahl);
		zahl = zahl / 100;
		return zahl;

	}
}
