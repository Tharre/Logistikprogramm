package Budget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 *In der Klasse Abfragen werden die Abfragen für das Budget-Programm definiert
 *<p>
 * Title: Abfragen
 * 
 * @author Haupt, Liebhart
 **/
public class BudgetAbfragen extends JPanel implements ActionListener {

	// Buttons
	/** Button Detail **/
	private JButton btnDetails = new JButton("Details");
	/** Button "Anzeigen" **/
	private JButton btnAnzeigen = new JButton("Anzeigen");
	/** Button "gekaufte Ware suchen" **/
	private JButton btnArtikelSuchen = new JButton("gekaufte Ware suchen");
	/** Button "Bestellungen suchen" **/
	private JButton btnBanfSuchen = new JButton("Banfs suchen");
	/** Button "Umsatz berechnen" **/
	private JButton btnUmsatzBerechnen = new JButton("Umsatz berechnen");
	/** Button "ABC Analyse Artikel anzeigen" **/
	private JButton btnAbcAnzeigenArtikel = new JButton(
			"ABC Analyse der Artikel anzeigen");
	/** Button "ABC Analyse Firma anzeigen" **/
	private JButton btnAbcAnzeigenFirma = new JButton(
			"ABC Analyse der Firmen anzeigen");
	/** Button "Bestellungen zu Sonderbudgets suchen" **/
	private JButton btnSonderbudgetBestellung = new JButton(
			"Bestellungen suchen");
	/** Button "in Excel exportieren" **/
	private JButton btnExportieren = new JButton("in Excel exportieren");

	// int
	/** Zeilennummer einer Zeile in der Tabelle, die gewaehlt wurde **/
	private int zeilennummer;
	/**
	 * gibt Auskunft, ob man die Kostenstellen von einem Hauptbereich, Bereich,
	 * Hauptkostenstelle oder gleich die Kostenstelle moechte
	 **/
	private int welcheStufe;

	// double
	/** Summe aller Betraege fuer die Abfrage Auswertung nach Verhaeltnis **/
	private double gesamt;
	/** Umsatz je Firma **/
	private double umsatz = 0;

	// String
	/** Name des Budgets **/
	private String sBudget;
	/** gewaehlte Firma oder Kostenstelle **/
	private String sGewaehlt;

	// Panel
	/** Panel fuer unten **/
	private JPanel pUnten = new JPanel();
	/** Panel fuer oben **/
	private JPanel pOben = new JPanel();
	/** Hilfspanel **/
	private JPanel pHilf = new JPanel();

	// ComboBox
	/** ComboBox fuer die Antragssteller **/
	private JComboBox cbAntragsteller = new JComboBox();
	/** ComboBox fuer die Firmen **/
	private JComboBox cbFirmen = new JComboBox();
	/** ComboBox fuer Hauptbereiche, Bereiche, Hauptkostenstellen, Kostenstellen **/
	private JComboBox cbNamen = new JComboBox();

	// Label
	/** Label Antragssteller **/
	private JLabel lblAntragsteller = new JLabel("      Antragsteller:");
	/** Label Firma **/
	private JLabel lblHinweis = new JLabel("Firma");
	/** Label Umsatz je Firma **/
	private JLabel lblUmsatzHinweis = new JLabel("");

	// Vectoren
	/** Firmennamen **/
	private Vector<String> firmennamen = new Vector<String>();
	/** Daten der gekauften Artikeln **/
	private Vector datenWare = new Vector();
	/** Datenvector **/
	private Vector daten = new Vector();
	/** einen Eintrag eines zweidimensialen Vectors **/
	private Vector rows = new Vector();
	/** Namen von Hauptbereiche, Bereiche, Hauptkostenstellen, Kostenstellen **/
	private Vector<String> namenUT8 = new Vector<String>();
	/** Daten einer Banf **/
	private Vector banfs = new Vector();
	/** Namen der Kostenstellen **/
	private Vector<String> kostenstelle = new Vector<String>();
	/** Namen der Abteilungen **/
	private Vector<String> abteilung = new Vector<String>();
	/** Namen der Abteilungen **/
	private Vector<String> namenAbteilungen = new Vector<String>();
	/** Namen der Projekte **/
	private Vector<String> namenProjekte = new Vector<String>();
	/** Namen der Sonderbudgets **/
	private Vector<String> namenSonderbudget = new Vector<String>();

	// String Arrays
	/** Spaltennamen fuer Waren **/
	private String[] spaltenWare = { "Bezeichnung", "Menge", "Preis gesamt" };
	/** Spaltennamen fuer die Abfrage "Verhaeltnis in Prozent" **/
	private String[] spaltenVerhaeltnis = { "Abteilung",
			"UT3 Anteil in Prozent", "LMB1 Anteil in Prozent",
			"LMB2 Anteil in Prozent", "Gesamtanteil in Prozent" };
	/** Spaltennamen fuer die Abfrage "Verhaeltnis" **/
	private String[] spalten1 = { "Abteilung", "UT3 Anteil", "LMB1 Anteil",
			"LMB2 Anteil", "Insgesamt" };
	/** Spalten fuer die Bestellungen **/
	private String[] spaltenBestellung = { "BestellID", "Budget", "Datum",
			"W-Nummer", "Antragsteller", "Status Lieferung",
			"Status Bezahlung", "Firma", "Bestellbetrag", "Rechnungsbetrag",
			"Differenz" };
	/** Spalten fuer die Banfs einer Bestellung **/
	private String[] spaltenDetails = { "BestellID", "BanfID", "Antragsteller",
			"Kostenstelle", "Bezeichnung", "Firma", "Menge", "Preis inkl.",
			"Preis gesamt", "USt [%]" };
	/** Spalten fuer die Abfrage "Banfauflistung nach Lehrer" **/
	private String[] spaltenAnzeigen = { "ID", "Antragsteller", "Kostenstelle",
			"Status Lieferung", "Datum", "Firma" };
	/** Spalten fuer die Banfs **/
	private String[] spaltenBanf = { "Antragssteller", "Datum", "Status",
			"Kostenstelle", "Firma" };
	/** Spalten fuer die ABC- Analyse nach Artikel **/
	private String[] spaltenABCArtikel = { "Kategorie", "Artikel", "Umsatz",
			"Menge" };
	/** Spalten fuer die ABC- Analyse nach Firmen **/
	private String[] spaltenABCFirma = { "Kategorie", "Firma", "Umsatz",
			"Menge" };
	/** Spalten fuer die Rechnungen **/
	private String[] spaltenRechnung = { "W-Nummer", "Bestellbetrag",
			"Rechnungsbetrag", "externe Nummer", "interne Nummer",
			"Inventarnummer", "Buchhaltungsbelege", "Sonderabzug", "Skonto",
			"Zahlart", "Zahlbetrag", "Rechnungsstatus" };
	/** Spaltennamen fuer das Fenster **/
	private String[] spalten;

	// sonstiges
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;
	/** Objekt von der Klasse Tabelle **/
	private Tabelle table;
	/** Objekt von der Klasse Fenster **/
	private AnzAbfrage fenster;
	/** Objekt von der Klasse DatenExport **/
	private AbfrageExportExcel de;
	/** ScrollPane fuer die Tabelle **/
	private JScrollPane scroll = new JScrollPane();
	/**
	 * true: es wird mit aktueller DB gearbeitet; false: es wird mit Test-DB
	 * gearbeitet
	 **/
	private boolean aktuell;

	// Connection
	/** Connection zur Budget-Datenbank */
	private Connection con;
	/** Connection zur Logistik-Datenbank */
	private Connection conL;

	// Datum
	/** aktuelles Datum **/
	private Date heute;
	/** von diesem Datum soll Abfrage durchgeführt werden **/
	private long von;
	/** bis zu diesem Datum soll Abfrage durchgeführt werden **/
	private long bis;
	/** Hilfsvariabe zur Berechnung des Startdatums für eine Zeitperiode **/
	private Date datumV;
	/** Hilfsvariabe zur Berechnung des Enddatums für eine Zeitperiode **/
	private Date datumB;
	/** Hilfsvariabe zur Berechnung des Startdatums für eine Zeitperiode **/
	private long vonD;
	/** Hilfsvariabe zur Berechnung des Enddatums für eine Zeitperiode **/
	private long bisD;
	/** Hilfsvariable zur Ermittlung der aktuellen Jahreszahl **/
	public Calendar cal;
	/** aktuelles Jahr **/
	public int year;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budget-DB
	 * @param conL
	 *            Connection zur Logistik-DB
	 * @param aktuell
	 *            prüft ob mit der aktuellen DB oder der Test-DB gearbeitet wird
	 */
	public BudgetAbfragen(Connection con, Connection conL, boolean aktuell) {
		this.con = con;
		this.conL = conL;
		this.aktuell = aktuell;

		di = new DatenImport(con, conL);

		setLayout(new BorderLayout());

		// Buttons dem ActionListener adden
		btnDetails.addActionListener(this);
		btnAnzeigen.addActionListener(this);
		btnArtikelSuchen.addActionListener(this);
		btnBanfSuchen.addActionListener(this);
		btnUmsatzBerechnen.addActionListener(this);
		btnSonderbudgetBestellung.addActionListener(this);
		btnExportieren.addActionListener(this);
		btnAbcAnzeigenArtikel.addActionListener(this);
		btnAbcAnzeigenFirma.addActionListener(this);

		lblAntragsteller.setFont(new Font("Sans Serif", Font.BOLD, 15));
		lblHinweis.setFont(new Font("Sans Serif", Font.BOLD, 15));

		cal = new GregorianCalendar();
		year = cal.get(Calendar.YEAR);

	}

	/**
	 * je nachdem, welche Abfrage ausgewaehlt wurde, wird hier definiert, was
	 * angezeigt wird
	 * 
	 * @param kennnummer
	 *            welche Abfrage soll angezeigt werden
	 */
	public void anzeigen(int kennnummer) {

		if (kennnummer == 2) /* Übersicht Bestellungen UT8 */
		{
			uebersichtBestellungen(1);
		} else if (kennnummer == 3) /* Übersicht Bestellungen UT3 */
		{
			uebersichtBestellungen(2);
		} else if (kennnummer == 4) /* Übersicht Bestellungen LMB1 */
		{
			uebersichtBestellungen(3);
		} else if (kennnummer == 5) /* Übersicht Bestellungen LMB2 */
		{
			uebersichtBestellungen(4);
		} else if (kennnummer == 6) /* Übersicht Bestellungen Sonderbudgets */
		{
			uebersichtBestellungen(5);
		} else if (kennnummer == 7) /* BANF Auflistung nach Lehrer */
		{
			ansichtBanfZuLehrer();
		} else if (kennnummer == 8) /* Banfs nach Hauptbereich */
		{
			banfsNachHauptbereich();

		} else if (kennnummer == 9) /* Auswertung nach Abteilung - betragsmäßig */
		{
			auswertungNachAbteilungBetrag();

		} else if (kennnummer == 10) /*
									 * Auswertung nach Abteilung -
									 * verhältnismäßig
									 */
		{
			auswertungNachAbteilungVerhaeltnis();

		} else if (kennnummer == 11) /* Umsatz je Firma */
		{
			umsatzJeFirma();

		} else if (kennnummer == 12) /* gekaufte Artikel je Firma */
		{
			gekaufteArtikelJeFirma();

		} else if (kennnummer == 13) /* ABC-Analyse nach Firmen */
		{
			abcAnalyseFirmen();

		} else if (kennnummer == 14) /* ABC-Analyse nach Artikel */
		{
			abcAnalyseArtikel();

		} else if (kennnummer == 15) {/* Banfs nach Bereich */

			banfsNachBereich();

		} else if (kennnummer == 16) {/* Banfs nach Hauptkostenstelle */

			banfsNachHKSt();

		} else if (kennnummer == 17) {/* Banfs nach Kostenstelle */

			banfsNachKSt();

		} else if (kennnummer == 18) {/* Bestellungen nach Sonderbudgets */

			bestNachSonderbudget();

		} else if (kennnummer == 19) {/* alle Rechnungen anzeigen */

			heurigeRechnungenAnzeigen();

		}

		else if (kennnummer == 20) {/* alle Rechnungen anzeigen */

			alleRechnungenAnzeigen();
		}

	}// anzeigen

	/**
	 * Die Bestellungen, abhaengig vom ausgewaehlten Budget, werden angezeigt
	 * sowie ein Button Details, der die Banfs zu einer Bestellung anzeigt
	 * 
	 * @param kennung
	 *            gibt an, welches Budget ausgewählt wurde; 1=UT8, 2=UT3,
	 *            3=LMB1, 4=LMB2, 5=Sonderbudget
	 */
	public void uebersichtBestellungen(int kennung) {

		long von;
		long bis;

		removeAll();
		daten.clear();
		pUnten.removeAll();

		cal = new GregorianCalendar();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		if (!aktuell) {
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.YEAR, year - 1);
			date = cal.getTime();
			von = cal.getTimeInMillis();

			cal.set(Calendar.DATE, 31);
			cal.set(Calendar.MONTH, 11);
			date = cal.getTime();
			bis = cal.getTimeInMillis();
		} else {
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.YEAR, year);
			date = cal.getTime();
			von = cal.getTimeInMillis();

			cal.set(Calendar.DATE, 31);
			cal.set(Calendar.MONTH, 11);
			date = cal.getTime();
			bis = cal.getTimeInMillis();
		}

		if (kennung < 6) {

			if (kennung == 1)
				sBudget = "UT8";
			else if (kennung == 2)
				sBudget = "UT3";
			else if (kennung == 3)
				sBudget = "LMB1";
			else if (kennung == 4)
				sBudget = "LMB2";
			else if (kennung == 5)
				sBudget = "Sonderbudget";

			daten = di.bestellungenZuBudget(sBudget, von / 1000, bis / 1000);
		} else {

			daten = di.getBestellungZuSonderbudget(cbNamen.getSelectedItem()
					.toString(), von / 1000, bis / 1000);
		}

		for (int i = 0; i < daten.size(); i++) {
			Vector hilf = (Vector) daten.get(i);
		}

		int summe = 0;
		int status;

		for (int i = 0; i < daten.size(); i++) {
			Vector reihe = new Vector();
			reihe = (Vector) daten.get(i);
			if (reihe.get(5).toString().equals("gelöscht"))
				status = 15;
			else
				status = 0;

			summe += di.summeBestellung(Integer.parseInt(reihe.get(0)
					.toString()), status);
		}

		pUnten.setLayout(new GridLayout(1, 3));
		JLabel lSumme = new JLabel("Summe aller Bestellungen:  " + summe);
		lSumme.setFont(new Font("Arial", Font.BOLD, 15));

		pUnten.add(lSumme);
		pUnten.add(btnDetails);
		pUnten.add(btnExportieren);

		spalten = spaltenBestellung;
		table = new Tabelle(spaltenBestellung, daten, con, conL);
		scroll = table.getTabelle();

		add(scroll, BorderLayout.CENTER);
		add(pUnten, BorderLayout.SOUTH);

		validate();
		repaint();
	}

	/**
	 * Die von einem bestimmten Lehrer erstellten Banfs werden angezeigt
	 */
	public void ansichtBanfZuLehrer() {
		removeAll();
		pOben.removeAll();
		daten.clear();

		pOben.setLayout(new GridLayout(2, 5));
		daten = di.getAntragsteller();

		for (int i = 0; i < daten.size(); i++) {
			cbAntragsteller.addItem(daten.elementAt(i).toString());
		}

		pOben.add(new JLabel());
		pOben.add(new JLabel());
		pOben.add(new JLabel());
		pOben.add(new JLabel());
		pOben.add(new JLabel());
		pOben.add(lblAntragsteller);
		pOben.add(cbAntragsteller);
		pOben.add(new JLabel());
		pOben.add(btnAnzeigen);
		pOben.add(new JLabel());

		add(pOben, BorderLayout.NORTH);
		repaint();
	}

	/**
	 * Banfs zu einem bestimmten Hauptbereich werden angezeigt
	 */
	public void banfsNachHauptbereich() {
		namenUT8.clear();
		namenUT8 = di.auslesenUT8("hauptbereichut8");
		if (namenUT8.size() != 0)
			namenUT8.remove(0);
		for (int i = 0; i < namenUT8.size(); i++) {
			cbNamen.addItem(namenUT8.get(i).toString());
		}
		pHilf.removeAll();
		revalidate();
		repaint();
		pHilf.add(new JLabel("Bitte wählen Sie einen Hauptbereich aus."));
		pHilf.add(cbNamen);
		pHilf.add(btnBanfSuchen);

		welcheStufe = 1;

		add(pHilf, BorderLayout.CENTER);
	}

	/**
	 * Auswertung, welche Abteilung wie viel vom Budget verwendet hat
	 * (betragsmäßig)
	 */
	public void auswertungNachAbteilungBetrag() {
		daten.clear();
		rows.clear();

		abteilung.clear();
		abteilung = di.getAbteilungen();

		for (int i = 0; i < abteilung.size(); i++) {
			rows = new Vector();
			rows.add(abteilung.get(i).toString());

			rows.add(runde(di.getAbteilungsbetragUT3(abteilung.get(i)
					.toString())));
			rows.add(runde(di.getAbteilungsbetragLMB(abteilung.get(i)
					.toString(), 1)));
			rows.add(runde(di.getAbteilungsbetragLMB(abteilung.get(i)
					.toString(), 2)));
			rows.add(runde(di.getAbteilungsbetragUT3(abteilung.get(i)
					.toString())
					+ (di
							.getAbteilungsbetragLMB(
									abteilung.get(i).toString(), 1))
					+ (di
							.getAbteilungsbetragLMB(
									abteilung.get(i).toString(), 2))));
			daten.add(rows);
		}
		spalten = spalten1;

		table = new Tabelle(spalten1, daten, con, conL);
		scroll = table.getTabelle();
		add(scroll, BorderLayout.CENTER);
		add(btnExportieren, BorderLayout.SOUTH);
	}

	/**
	 * Auswertung, welche Abteilung wie viel vom Budget verwendet hat
	 * (verhältnismäßig)
	 */
	public void auswertungNachAbteilungVerhaeltnis() {
		daten.clear();

		abteilung = di.getAbteilungen();

		double[][] werte = new double[abteilung.size()][3];

		for (int i = 0; i < abteilung.size(); i++) {
			rows = new Vector();
			werte[i][0] = runde(di.getAbteilungsbetragUT3(abteilung.get(i)
					.toString()));
			werte[i][1] = runde(di.getAbteilungsbetragLMB(abteilung.get(i)
					.toString(), 1));
			werte[i][2] = runde(di.getAbteilungsbetragLMB(abteilung.get(i)
					.toString(), 2));

			gesamt = werte[i][0] + werte[i][1] + werte[i][2];

			rows.add(abteilung.get(i).toString());

			if ((Double.doubleToLongBits((werte[i][0] * 100) / gesamt)) == Double
					.doubleToLongBits(Double.NaN))
				rows.add("Kein Wert");
			else
				rows.add(runde((werte[i][0] * 100) / gesamt));

			if ((Double.doubleToLongBits((werte[i][1] * 100) / gesamt)) == Double
					.doubleToLongBits(Double.NaN))
				rows.add("Kein Wert");
			else
				rows.add(runde((werte[i][1] * 100) / gesamt));

			if ((Double.doubleToLongBits((werte[i][2] * 100) / gesamt)) == Double
					.doubleToLongBits(Double.NaN))
				rows.add("Kein Wert");
			else
				rows.add(runde((werte[i][2] * 100) / gesamt));

			if (Double
					.doubleToLongBits((((werte[i][0] + werte[i][1] + werte[i][2]) * 100) / (gesamt))) == Double
					.doubleToLongBits(Double.NaN))
				rows.add("Kein Wert");
			else
				rows
						.add(runde(((werte[i][0] + werte[i][1] + werte[i][2]) * 100)
								/ (gesamt)));

			daten.add(rows);
		}
		spalten = spaltenVerhaeltnis;

		table = new Tabelle(spaltenVerhaeltnis, daten, con, conL);
		scroll = table.getTabelle();
		add(scroll, BorderLayout.CENTER);
		add(btnExportieren, BorderLayout.SOUTH);
	}

	/**
	 * Auswertung des Umsatzes pro Firma
	 */
	public void umsatzJeFirma() {
		removeAll();
		pOben.removeAll();
		daten.clear();

		pOben.setLayout(new GridLayout(3, 5));
		firmennamen = new Vector<String>();
		firmennamen = di.getFirmennamen();

		for (int i = 0; i < firmennamen.size(); i++) {
			cbFirmen.addItem(firmennamen.get(i).toString());
		}

		pOben.add(new JLabel(""));
		pOben.add(new JLabel(""));
		pOben.add(new JLabel(""));
		pOben.add(new JLabel(""));
		pOben.add(new JLabel(""));

		pOben.add(lblHinweis);
		pOben.add(cbFirmen);
		pOben.add(new JLabel(""));
		pOben.add(btnUmsatzBerechnen);
		pOben.add(new JLabel(""));

		pOben.add(lblUmsatzHinweis);
		pOben.add(new JLabel(""));
		pOben.add(new JLabel(""));
		pOben.add(new JLabel(""));
		pOben.add(new JLabel(""));

		add(pOben, BorderLayout.NORTH);

		repaint();
	}

	/**
	 * Auswertung der gekauften Artikel pro Firma
	 */
	public void gekaufteArtikelJeFirma() {
		removeAll();
		pOben.removeAll();
		daten.clear();

		pOben.setLayout(new GridLayout(2, 5));
		firmennamen = new Vector();
		firmennamen = di.getFirmennamen();

		for (int i = 0; i < firmennamen.size(); i++) {
			cbFirmen.addItem(firmennamen.get(i).toString());
		}

		pOben.add(new JLabel());
		pOben.add(new JLabel());
		pOben.add(new JLabel());
		pOben.add(new JLabel());
		pOben.add(new JLabel());
		pOben.add(lblHinweis);
		pOben.add(cbFirmen);
		pOben.add(new JLabel());
		pOben.add(btnArtikelSuchen);
		pOben.add(new JLabel());

		add(pOben, BorderLayout.NORTH);

		repaint();
	}

	/**
	 * ABC-Analyse nach Artikel
	 */
	public void abcAnalyseArtikel() {
		spalten = spaltenABCArtikel;

		daten = new Vector();

		daten = di.getABCArtikel();
		if (daten != null) {
			table = new Tabelle(spaltenABCArtikel, daten, con, conL);
			scroll = table.getTabelle();

			add(scroll, BorderLayout.CENTER);
			add(btnExportieren, BorderLayout.SOUTH);
		}
	}

	/**
	 * ABC-Analyse nach Firmen
	 */
	public void abcAnalyseFirmen() {
		spalten = spaltenABCFirma;

		daten = new Vector();
		daten = di.getABCFirma();

		if (daten != null) {
			table = new Tabelle(spaltenABCFirma, daten, con, conL);
			scroll = table.getTabelle();

			add(scroll, BorderLayout.CENTER);
			add(btnExportieren, BorderLayout.SOUTH);
		}
	}

	/**
	 * Auflistung der Banfs von einem bestimmten Bereich
	 */
	public void banfsNachBereich() {
		namenUT8.clear();
		namenUT8 = di.auslesenUT8("bereichut8");
		for (int i = 0; i < namenUT8.size(); i++) {
			cbNamen.addItem(namenUT8.get(i).toString());
		}

		welcheStufe = 2;

		pHilf.removeAll();
		revalidate();
		repaint();
		pHilf.add(new JLabel("Bitte wählen Sie einen Bereich aus."));
		pHilf.add(cbNamen);
		pHilf.add(btnBanfSuchen);

		add(pHilf, BorderLayout.CENTER);
	}

	/**
	 * Auflistung der Banfs von einer bestimmten Hauptkostenstelle
	 */
	public void banfsNachHKSt() {
		namenUT8.clear();
		namenUT8 = di.auslesenUT8("hauptkostenstelleut8");
		for (int i = 0; i < namenUT8.size(); i++) {
			cbNamen.addItem(namenUT8.get(i).toString());
		}

		welcheStufe = 3;

		pHilf.removeAll();
		revalidate();
		repaint();
		pHilf.add(new JLabel("Bitte wählen Sie eine Hauptkostenstelle aus."));
		pHilf.add(cbNamen);
		pHilf.add(btnBanfSuchen);

		add(pHilf, BorderLayout.CENTER);
	}

	/**
	 * Auflistung der Banfs von einer bestimmten Kostenstelle
	 */
	public void banfsNachKSt() {
		namenUT8.clear();
		namenUT8 = di.auslesenUT8("kostenstelleut8");
		namenAbteilungen = di.getAbteilungen();
		namenProjekte = di.getProjekte();

		for (int i = 0; i < namenUT8.size(); i++) {
			cbNamen.addItem(namenUT8.get(i).toString());
		}
		for (int i = 0; i < namenAbteilungen.size(); i++) {
			cbNamen.addItem(namenAbteilungen.get(i).toString());

		}
		for (int i = 0; i < namenProjekte.size(); i++) {
			cbNamen.addItem(namenProjekte.get(i).toString());

		}
		welcheStufe = 4;
		pHilf.removeAll();
		revalidate();
		repaint();
		pHilf.add(new JLabel("Bitte wählen Sie eine Kostenstelle aus."));
		pHilf.add(cbNamen);
		pHilf.add(btnBanfSuchen);

		add(pHilf, BorderLayout.CENTER);
	}

	/**
	 * Auflistung der Bestellungen zu einem bestimmten Sonderbudget
	 */
	public void bestNachSonderbudget() {
		removeAll();
		pHilf.removeAll();
		namenSonderbudget = new Vector<String>();
		namenSonderbudget = di.getSonderbudgets();

		cbNamen.removeAllItems();
		for (int i = 0; i < namenSonderbudget.size(); i++) {

			cbNamen.addItem(namenSonderbudget.get(i).toString());
		}

		if (cbNamen.getItemCount() == 0)
			btnSonderbudgetBestellung.setEnabled(false);
		pHilf.add(new JLabel("Bitte wählen Sie ein Sonderbudget aus."));
		pHilf.add(cbNamen);
		pHilf.add(btnSonderbudgetBestellung);

		add(pHilf, BorderLayout.CENTER);

		validate();
		repaint();
	}

	/**
	 * Auflistung aller vorhandenen Rechnungen
	 */
	public void alleRechnungenAnzeigen() {
		removeAll();
		daten.clear();

		daten = di.getRechnungen();

		spalten = spaltenRechnung;

		table = new Tabelle(spaltenRechnung, daten, con, conL);
		scroll = table.getTabelle();
		add(scroll, BorderLayout.CENTER);
		add(btnExportieren, BorderLayout.SOUTH);
	}

	public void heurigeRechnungenAnzeigen() {
		removeAll();
		daten.clear();

		von = year;

		if (!aktuell) {
			von = year - 1;
		}

		long zahl = von - 2000;

		daten = di.getRechnungenHeuer(zahl);

		spalten = spaltenRechnung;

		table = new Tabelle(spaltenRechnung, daten, con, conL);
		scroll = table.getTabelle();
		add(scroll, BorderLayout.CENTER);
		add(btnExportieren, BorderLayout.SOUTH);
	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            Ein ActionEvent-Objekt
	 */
	public void actionPerformed(ActionEvent e) {

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		if (!aktuell) {
			year = year - 1;
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.YEAR, year);
			date = cal.getTime();
			von = cal.getTimeInMillis();

			cal.set(Calendar.DATE, 31);
			cal.set(Calendar.MONTH, 11);
			date = cal.getTime();
			bis = cal.getTimeInMillis();
		} else {
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.YEAR, year);
			date = cal.getTime();
			von = cal.getTimeInMillis();

			cal.set(Calendar.DATE, 31);
			cal.set(Calendar.MONTH, 11);
			date = cal.getTime();
			bis = cal.getTimeInMillis();
		}

		// gekaufte Artikel je Firma
		if (e.getSource() == btnArtikelSuchen) {
			sGewaehlt = (cbFirmen.getSelectedItem()).toString();

			datenWare = di
					.getGekaufteArtikel(sGewaehlt, von / 1000, bis / 1000);
			spalten = spaltenWare;

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					fenster = new AnzAbfrage("gekaufte Ware", spaltenWare,
							datenWare, con, conL);
					fenster.setSize((int) Toolkit.getDefaultToolkit()
							.getScreenSize().getWidth() / 3 * 2,
							(int) Toolkit.getDefaultToolkit().getScreenSize()
									.getHeight() / 3 * 2);
					fenster.setLocationRelativeTo(null);
					fenster.setVisible(true);
				}
			});
		}

		// Banfs zu einer Bestellung
		if (e.getSource() == btnDetails) {
			spalten = spaltenDetails;

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (table.welcheZeile()) {

						daten.clear();
						zeilennummer = table.getZeilennummer();
						daten = di
								.detailsZuBestellung(zeilennummer, von / 1000);
						fenster = new AnzAbfrage("Banfliste zu Bestellungen",
								spaltenDetails, daten, con, conL);
						fenster.setSize((int) Toolkit.getDefaultToolkit()
								.getScreenSize().getWidth() / 3 * 2,
								(int) Toolkit.getDefaultToolkit()
										.getScreenSize().getHeight() / 3 * 2);
						fenster.setLocationRelativeTo(null);
						fenster.setVisible(true);
					}
				}
			});
		}

		// Banfauflistung nach Lehrer
		else if (e.getSource() == btnAnzeigen) {

			spalten = spaltenAnzeigen;

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {

					daten.clear();
					daten = di.banfAuflistungNachLehrer(cbAntragsteller
							.getSelectedItem().toString(), von / 1000,
							bis / 1000);

					fenster = new AnzAbfrage("Banfliste", spaltenAnzeigen,
							daten, con, conL);
					fenster.setSize((int) Toolkit.getDefaultToolkit()
							.getScreenSize().getWidth() / 3 * 2,
							(int) Toolkit.getDefaultToolkit().getScreenSize()
									.getHeight() / 3 * 2);
					fenster.setLocationRelativeTo(null);
					fenster.setVisible(true);
				}
			});
		}

		// Banfs zu Hauptbereich, Bereich, Hauptkostenstelle, Kostenstelle
		if (e.getSource() == btnBanfSuchen) {

			spalten = spaltenBanf;
			banfs = new Vector();
			daten = new Vector();

			sGewaehlt = cbNamen.getSelectedItem().toString();

			if (welcheStufe == 4) {
				banfs = di.getBanfs(sGewaehlt, von / 1000, bis / 1000);

				for (int j = 0; j < banfs.size(); j++)
					daten.add(banfs.get(j));
			} else {

				kostenstelle = di.getKostenstelle(sGewaehlt, welcheStufe);
				String[] kStellen = new String[kostenstelle.size()];

				for (int i = 0; i < kStellen.length; i++)
					kStellen[i] = kostenstelle.get(i).toString();

				for (int i = 0; i < kStellen.length; i++) {

					if (di.getBanfs(kStellen[i], von / 1000, bis / 1000) != null) {
						banfs = di
								.getBanfs(kStellen[i], von / 1000, bis / 1000);

					}

					for (int j = 0; j < banfs.size(); j++)
						daten.add(banfs.get(j));
				}
			}

			if (daten.size() == 0)
				JOptionPane.showMessageDialog(this,
						"Es wurden leider keine Banfs gefunden");
			else
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						fenster = new AnzAbfrage("Banfs", spaltenBanf, daten,
								con, conL);
						fenster.setSize((int) Toolkit.getDefaultToolkit()
								.getScreenSize().getWidth() / 3 * 2,
								(int) Toolkit.getDefaultToolkit()
										.getScreenSize().getHeight() / 3 * 2);
						fenster.setLocationRelativeTo(null);
						fenster.setVisible(true);
					}
				});
		}

		// Umsatz je Firma
		if (e.getSource() == btnUmsatzBerechnen) {

			sGewaehlt = (cbFirmen.getSelectedItem()).toString();
			umsatz = runde(di.getUmsatzJeFirma(sGewaehlt, von / 1000,
					bis / 1000));
			lblUmsatzHinweis.setText("Der Umsatz beträgt: " + umsatz);
			lblUmsatzHinweis.setFont(new Font("SansSerif", Font.BOLD, 15));
			repaint();

		}

		// Bestellungen zu Sonderbudget
		if (e.getSource() == btnSonderbudgetBestellung) {
			uebersichtBestellungen(6);
		}

		// in Excel exportieren
		if (e.getSource() == btnExportieren) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					de = new AbfrageExportExcel(con, conL, daten, spalten);
					de.pack();
					de.setLocationRelativeTo(null);
					de.setVisible(true);
				}
			});
		}
	}// actionPerformed

	/**
	 * rundet eine Zahl auf zwei Kommastellen
	 * 
	 * @param zahl
	 *            die zu rundende Zahl
	 * @return gerundete Zahl
	 */
	public double runde(double zahl) {
		zahl = zahl * 100;
		zahl = Math.round(zahl);
		zahl = zahl / 100;
		return zahl;
	}
}