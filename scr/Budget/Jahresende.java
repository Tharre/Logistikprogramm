package Budget;

import java.sql.Connection;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Logistik.Laden;
import Logistik.AnzTabelleB;

/**
 * Am Ende des Jahres soll mit Hilfe dieser Klasse die neue Datenbank geleert
 * werden und wieder mit den Standarddaten gefuellt werden. Dann werden die
 * gesperrten Werte aus der alten Datenbank uebernommen und bei den Budgets vom
 * Vorjahr (alte Datenbank) geloescht.
 * <p>
 * Title: Jahresende
 * 
 *@author Haupt, Liebhart
 **/
public class Jahresende extends JPanel {

	// Connection
	/** Connection zur alten Budget-Datenbank vom Vorjahr **/
	private Connection conB_alt;
	/** Connection zur Budgetdatenbank **/
	private Connection con;

	// int
	/** Hilfsvariable **/
	private int hilf;

	// Objekte von eigenen Klassen
	/** Objekt der Klasse Nullsetzen; wird verwendet um die Datenbank zu leeren **/
	private NullSetzen dbLeeren;
	/** Objekt der Klasse DatenImport **/
	private DatenImport di;
	/** Objekt der Klasse DatenUpdate **/
	private DatenUpdate du;

	// sonstiges
	/** das gesperrte Budget, das in die neue Datenbank uebernommen werden soll **/
	private double gesperrt;
	/** die Namen aller Tabellen, in denen es gesperrtes Budget gibt **/
	private String[] tabellenNamen = { "abteilungut3", "kostenstelleut8",
			"lmb", "sonderbudget" };
	/** Nummern der Datensaetze von der Datenbank **/
	private Vector<Integer> nummern = new Vector<Integer>();
	//private DatenExportJahresende de;
	private DBExport deKomplett;
	private TextdateiImport ti;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur neuen Budget-Datenbank
	 * @param conL
	 *            Connection zur Logistik-Datenbank
	 * @param conB_alt
	 *            Connection zur alten Budget-Datenbank vom Vorjahr
	 */
	public Jahresende(Connection con, Connection conL, Connection conB_alt) {

		this.conB_alt = conB_alt;
		this.con = con;
		//de = new DatenExportJahresende(con);
		//deKomplett = new DatenbankExport(con);
		dbLeeren = new NullSetzen(conB_alt);
		di = new DatenImport(con, conL);
		du = new DatenUpdate(con, conL);

	}

	/**
	 * Die Methode erzeugeMeldung, erzeugt zuerst ein Dialogfenster um zu
	 * fragen, ob man sich wirklich sicher ist, dass man ein neues Jahr beginnen
	 * möchte
	 **/
	public void erzeugeMeldung() {
		hilf = JOptionPane.showConfirmDialog(this, "Sind Sie sicher",
				"WARNUNG", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (hilf == 0) // wenn die Eingabe ja war (hilf==0), dann werden
			// Datensaetze geloescht
		{
			final Laden l = new Laden();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ausfuehren();
		l.dispose();
			}
		});
		}

	}

	/**
	 * die neue Datenbank wird geleert und dann wieder mit den Standardwerten
	 * gefuellt; die gesperrten Betraege werden uebernommen
	 */
	public void ausfuehren() {
		//double summe = 0;
		//deKomplett = new DatenbankExport(con);
		//ti = new TextdateiImport(conB_alt);
		
		final Jahresende je = this;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				deKomplett = new DBExport(con,conB_alt,1,je);
				deKomplett.pack();
				deKomplett.setLocationRelativeTo(null);
				deKomplett.setVisible(true);
			}

		});
		
		
	}
	
	/**
	 *geplant und ausgegeben werden in der aktuellen DB gelöscht in: UT3, UT8, Sonderbudget, LMB 
	 */
	public void betraegeLoeschen()
	{
		try{
		du.jahresendeNullSetzen(con, "abteilungut3");
		du.jahresendeNullSetzen(con, "hauptbereichut8");
		du.jahresendeNullSetzen(con, "bereichut8");
		du.jahresendeNullSetzen(con, "hauptkostenstelleut8");
		du.jahresendeNullSetzen(con, "kostenstelleut8");
		du.jahresendeNullSetzen(con, "sonderbudget");
		du.jahresendeNullSetzen(con, "lmb");
		
		JOptionPane.showMessageDialog(null,
		"Die gesperrten Betraege wurden uebernommen!");
		
		}catch(Exception e)
		{
			
			JOptionPane.showMessageDialog(null,
			"Fehler beim löschen der ausgegebenen und geplanten Beträge!");
			
			e.printStackTrace();
		}
		
		
	}
	

}
