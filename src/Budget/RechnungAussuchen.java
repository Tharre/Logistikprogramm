package Budget;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *In der Klasse RechnungAussuchen kann man eine bereits vorhandene Rechnung
 * auswaehlen um sie zu bearbeiten dargestellt werden, erstellt
 *<p>
 * Title: RechnungAussuchen
 * 
 * @author Haupt, Liebhart
 **/
public class RechnungAussuchen extends JFrame implements ActionListener {

	// sonstiges
	/** Tabelle **/
	private Tabelle table;

	/** Scrollpane fuer die Tabelle **/
	private JScrollPane scroll = new JScrollPane();

	/** eine Rechnung auswaehlen **/
	private JButton auswaehlen = new JButton("Rechnung bearbeiten");
	/** Spalten der Tabelle **/
	private String[] spalten = { "Nummer", "W-Nummer", "Bestellbetrag",
			"Rechnungsbetrag", "externe Nummer", "interne Nummer",
			"Inventarnummer", "Buchhaltungsbelege", "Sonderabzug", "Skonto",
			"Zahlart", "Preis gesamt","Rechnungsstatus" };

	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;
	/** Objekt von der Klasse RechnungEingabe **/
	private RechnungEingabe re;
	/** gibt es eine Kostenstelle mi/wi, el/et, gemeinsam **/
	private boolean mitAnteile = false;

	// Connection
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	/** Connection zur Logistikdatenbank **/
	private Connection conL;

	// int
	/** BestellID **/
	private int bestID;
	/** Anzahl der Zeilen **/
	private int zeilen;

	// double
	/** Summe der Sonderabzuege */
	private double summeSonderabzuege;
	/** Prozentwerte fuer die Aufteilung des Betrages auf die Abteilungen **/
	private double[][] werte;

	// String
	/** Budget **/
	private String budget;
	/** Titel **/
	//private String titel;
	/** W-Nummer **/
	private String wNummer;

	// Vector
	/** Die einzelnen gesamten Preise der ausgewaehlten Bestellpositionen **/
	private Vector<Double> preisGesamt = new Vector<Double>();
	/**
	 * Die Kostenstellen der Banfs zu den ausgewaehlten Bestellpositionen; hier
	 * soll der Betrag verbucht werden
	 **/
	private Vector<String> kostenstelle = new Vector<String>();
	/** Die IDs der Bestellpositionen **/
	private Vector<Integer> bestposIds = new Vector<Integer>();
	/** Daten der gewaehlten Rechnung **/
	private Vector datenZuRechnung = new Vector();
	/** alle Sonderabzuege **/
	private Vector<Double> sonderabzuege = new Vector<Double>();
	/** Preise minus der Sonderabzuege **/
	private Vector<Double> preisMinusSonder = new Vector<Double>();
	/** Wo im Vector stehen die richtigen Informationen **/
	private Vector<Integer> welcheBestellPositionen = new Vector<Integer>();
	/**
	 * 1-3: 1:Kostenstelle "gemeinsam", 2:Kostenstelle "EL/ET", 3:Kostenstelle
	 * "WI/MI
	 **/
	private Vector<Integer> welcheKostenstelle = new Vector<Integer>();
	/** Zeilennummer der angehakten Bestellpositionen **/
	private Vector<Integer> angeklickt = new Vector<Integer>();
	/** Daten der Tabelle **/
	private Vector daten = new Vector();
	/** true; wenn die Bestellung nun fertig bezahlt ist; false: wenn noch BestPos ausständig sind**/
	/** Menge, die bezahlt werden kann: geliefert - bezahlt aus DB**/
	private Vector<Double> menge = new Vector<Double>();
	private boolean fertigBez;
	private boolean teilGesamt;

	/**
	 * Konstruktor wenn es keine gemeinsamen Kostenstellen gibt
	 * 
	 * @param summeSonderabzuege
	 *            Summe der Sonderabzuege
	 * @param preisGesamt
	 *            Der gesamte Preis pro Bestellposition
	 * @param bestID
	 *            Bestellids Bestellid
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 * @param titel
	 *            Titel der Rechnung
	 * @param budget
	 *            betroffenes Budget
	 * @param bestposIds
	 *            IDs der gewaehlten Bestellpositionen
	 * @param kostenstelle
	 *            welche Kostenstellen sind betroffen
	 * @param zeilen
	 *            Anzahl der Zeilen
	 * @param sonderabzuege
	 *            Sonderabzuege der Bestellpositionen
	 * @param preisMinusSonder
	 *            Preis minus der Sonderabzuege
	 * @param angeklickt
	 *            an welcher Stelle im Vector bestposIds stehen die angeklicken
	 *            Bestellpositionen
	 * @param wNummer
	 *            W-Nummer der Bestellung
	 */

	public RechnungAussuchen(double summeSonderabzuege,
			Vector<Double> preisGesamt, int bestID, Connection con,
			Connection conL, boolean teilGesamt, String budget,
			Vector<Integer> bestposIds, Vector<String> kostenstelle,
			int zeilen, Vector<Double> sonderabzuege,
			Vector<Double> preisMinusSonder, Vector<Integer> angeklickt,
			String wNummer, boolean fertigBez, Vector<Double> menge) {

		super("vorhandene Rechnungen");

		this.summeSonderabzuege = summeSonderabzuege;
		this.preisGesamt = preisGesamt;
		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.teilGesamt = teilGesamt;
		this.budget = budget;
		this.bestposIds = bestposIds;
		this.kostenstelle = kostenstelle;
		this.zeilen = zeilen;
		this.sonderabzuege = sonderabzuege;
		this.preisMinusSonder = preisMinusSonder;
		this.angeklickt = angeklickt;
		this.wNummer = wNummer;
		this.fertigBez = fertigBez;
		this.menge = menge;

		erstelleLayout();

	}

	/**
	 * Konstruktor wenn es gemeinsamen Kostenstellen gibt
	 * 
	 * @param summeSonderabzuege
	 *            Summe der Sonderabzuege
	 * @param preisGesamt
	 *            gesamter Preis pro Bestellposition
	 * @param bestID
	 *            Bestellid
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 * @param titel
	 *            Titel der Rechnung
	 * @param budget
	 *            betroffenes Budget
	 * @param bestposIds
	 *            IDs der gewaehlten Bestellpositionen
	 * @param kostenstelle
	 *            welche Kostenstellen sind betroffen
	 * @param zeilen
	 *            Anzahl der Zeilen
	 * @param sonderabzuege
	 *            Sonderabzuege der Bestellpositionen
	 * @param preisMinusSonder
	 *            Preis minus der Sonderabzuege
	 * @param werte
	 *            Prozentwerte fuer die Anteile
	 * @param welcheKostenstelle
	 *            welche gemeinsamen Kostenstellen gibt es
	 * @param welcheBestellPositionen
	 *            wo im Vector stehen die richtigen Informationen
	 * @param wNummer
	 *            W-Nummer der Bestellung
	 * @param angeklickt
	 *            an welcher Stelle im Vector bestposIds stehen die angeklicken
	 *            Bestellpositionen
	 */
	public RechnungAussuchen(double summeSonderabzuege,
			Vector<Double> preisGesamt, int bestID, Connection con,
			Connection conL, boolean teilGesamt, String budget,
			Vector<Integer> bestposIds, Vector<String> kostenstelle,
			int zeilen, Vector<Double> sonderabzuege,
			Vector<Double> preisMinusSonder, double[][] werte,
			Vector<Integer> welcheKostenstelle,
			Vector<Integer> welcheBestellPositionen, String wNummer,
			Vector<Integer> angeklickt, boolean fertigBez, Vector<Double> menge) {

		super("vorhandene Rechnungen");

		this.summeSonderabzuege = summeSonderabzuege;
		this.preisGesamt = preisGesamt;
		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.teilGesamt = teilGesamt;
		this.budget = budget;
		this.bestposIds = bestposIds;
		this.kostenstelle = kostenstelle;
		this.zeilen = zeilen;
		this.sonderabzuege = sonderabzuege;
		this.preisMinusSonder = preisMinusSonder;
		this.angeklickt = angeklickt;
		this.werte = werte;
		this.welcheKostenstelle = welcheKostenstelle;
		this.welcheBestellPositionen = welcheBestellPositionen;
		this.wNummer = wNummer;
		this.fertigBez = fertigBez;
		this.menge = menge;

		mitAnteile = true;

		erstelleLayout();

	}

	/**
	 * erstellt das Layout fuer den Frame
	 */
	public void erstelleLayout() {
		di = new DatenImport(con, conL);
		String query = "select * from rechnung where wNummer like '" + wNummer
				+ "'";
		daten.clear();
		daten = di.sucheRechnung(query);
		
		

		table = new Tabelle(spalten, daten, con, conL);
		scroll = table.getTabelle();
		add(scroll, BorderLayout.CENTER);
		add(auswaehlen, BorderLayout.SOUTH);

		auswaehlen.addActionListener(this);

	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == auswaehlen) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					if (table.welcheZeile()) {
						for (int i = 0; i < daten.size(); i++) {
							datenZuRechnung = (Vector) daten.get(i);

							if (Integer.parseInt(datenZuRechnung.get(0)
									.toString()) == table.getZeilennummer())

							{
								if (mitAnteile) {
									
									re = new RechnungEingabe(
											summeSonderabzuege, preisGesamt,
											bestID, con, conL, teilGesamt, budget,
											bestposIds, kostenstelle, zeilen,
											sonderabzuege, preisMinusSonder,
											werte, welcheKostenstelle,
											welcheBestellPositionen,
											angeklickt, datenZuRechnung, fertigBez, menge);

									re.pack();
									re.setLocationRelativeTo(null);
									re.setVisible(true);
								} else {

									re = new RechnungEingabe(
											summeSonderabzuege, preisGesamt,
											bestID, con, conL, teilGesamt, budget,
											bestposIds, kostenstelle, zeilen,
											sonderabzuege, preisMinusSonder,
											angeklickt, datenZuRechnung, fertigBez, menge);

									re.pack();
									re.setLocationRelativeTo(null);
									re.setVisible(true);

								}
							}
						}
					}
				}
			});

		}
	}
}
