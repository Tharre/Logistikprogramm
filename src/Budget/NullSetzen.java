package Budget;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

/**
 *In der Klasse NullSetzen werden alle Daten der Datenbank geloescht
 *<p>
 * Title: NullSetzen
 * 
 * @author Haupt, Liebhart
 **/
public class NullSetzen extends JPanel {
	/** Statement **/
	private Statement stmt;

	/** Befehl fuer die Datenbank **/
	private String query;

	/** Variable, um die Entscheidung zu speichern (JA=0, NEIN=1) **/
	private int hilf;

	/** Namen aller Tabellen (ausser der Tabelle rechnung) **/
	private String[] tabellen = { "abteilungut3", "bereichut8",
			"hauptbereichut8", "hauptkostenstelleut8", "kostenstelleut8",
			"lmb", "sonderbudget", "projekt", "rechnung" };

	/** Connection zur BudgetDatenbank **/
	private Connection con;

	/**
	 * Konsturktor
	 * 
	 * @param con
	 *            Connection zur Datenbank Budget
	 */
	public NullSetzen(Connection con) {
		this.con = con;
	}

	/**
	 * Die Methode erzeugeMeldung, erzeugt zuerst ein Dialogfenster um zu
	 * fragen, ob man sich wirklich sicher ist und alle Daten loeschen moechte
	 **/
	public void erzeugeMeldung() {
		hilf = JOptionPane.showConfirmDialog(this, "Sind Sie sicher",
				"WARNUNG", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (hilf == 0) // wenn die Eingabe ja war (hilf==0), dann werden
			// Datensaetze geloescht
			loescheDatensaetze();

	}

	/**
	 * Die Methode loescheDatensaetze, loescht alle Datensaetzte der Tabellen
	 * ausser die Daten der Rechnungen
	 **/
	public void loescheDatensaetze() {
		try {
			stmt = con.createStatement();

			for (int i = 0; i < tabellen.length; i++) {
				query = "delete from " + tabellen[i];
				stmt.execute(query);
			}
			stmt.close();

			JOptionPane.showMessageDialog(this, "Daten wurden geloescht");
		} catch (SQLException ex) {
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
			System.out.println("Daten der Tabelle konnte nicht gelöscht werden");
		}
	}
}