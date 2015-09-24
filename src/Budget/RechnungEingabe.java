package Budget;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.sql.Statement;
import java.util.Vector;

/**
 *In der Klasse RechnungEingabe wird ein Frame erstellt, in dem man die
 * Rechnungsdaten eingeben kann. Ausserdem finden die Umbuchungen der Betraege
 * in dieser Klasse statt.
 *<p>
 * Title: RechnungEingabe
 * 
 * @author Haupt, Liebhart
 **/
public class RechnungEingabe extends JFrame implements ActionListener {

	// sonstiges
	/** Labels, die bei der Rechnungseingabe als Hinweise erscheinen **/
	private JLabel[] labels;
	/** Hinweise, die dann in den Labels stehen **/
	private String[] hinweise = { "W-Nummer", "Bestellbetrag",
			"Rechnungsbetrag", "Externe Nummer", "Interne Nummer",
			"Inventarnummer", "Buchhaltungsbelege", "Sonderabzug", "Skonto",
			"Zahlart", "Zahlbetrag" };
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;
	/** Objekt von der Klasse DatenUpdate **/
	private DatenUpdate du;
	/** Statement **/
	private Statement stmt;
	/** ComboBox fuer die Namen der Sonderbudgets **/
	private JComboBox combo = new JComboBox();
	/** Prozentwerte der Abteilungen **/
	private double[][] werte;

	// TextFields
	/** Eingabefelder bei der Rechnung **/
	private JTextField[] fields;
	/** Feld, in dem der berechnte Skontobetrag erscheint **/
	private JTextField skontoEuro = new JTextField();

	// Connection
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	/** Connection zur Logistikdatenbank **/
	private Connection conL;

	// double
	/** Bestellbetrag **/
	private double bestellbetrag;
	/** Skonto in Prozent **/
	private double skontoProzent;
	/** Skonto in Euro **/
	private double skontoBetrag;
	/** Summe der Sonderabzuege **/
	private double summeSonderabzuege;
	/** Skonto je Bestellposition **/
	private double skontoJeBestPos;
	/** (Bestellbetrag- Rechnungsbetrag)/Anzahl **/
	private double minusBetrag;
	/** berechneter Zahlbetrag **/
	private double zahlbetragEuro;
	/** Rechnugsbetrag **/
	private double rechnungsbetrag;
	private String rechnungsbetragS;
	/** El- Betrag **/
	private double el = 0;
	/** ET- Betrag **/
	private double et = 0;
	/** WI- Betrag **/
	private double wi = 0;
	/** MI- Betrag **/
	private double mi = 0;
	/** LT- Betrag **/
	private double lt = 0;

	// String
	/** W- Nummer **/
	private String wNummer;
	/** interne Nummer **/
	private String interneNummer;
	/** externe Nummer **/
	private String externeNummer;
	/** Buchhaltungsbelegnummer **/
	private String buchhaltungsbeleg;
	/** Inventarnummer **/
	private String inventarnummer;
	/** Titel des JFrames **/
	private String titel;
	/** Zahlart **/
	private String zahlart;
	/** Budget **/
	private String budget;
	/** Name der Tabelle **/
	private String tabelle;
	/** Befehl fuer die Datenbank **/
	private String query;

	// Buttons
	/** ButtonGroup fuer die RadioButtons **/
	private ButtonGroup bg = new ButtonGroup();
	/** wenn die Rechnung zweckgebunden ist **/
	private JRadioButton zweckgebunden = new JRadioButton("zweckgebunden");
	/** wenn die Rechnung teilrechtsfaehig ist **/
	private JRadioButton teilrechtsfaehig = new JRadioButton("Teilrechtsfähig");
	/** wenn die Rechnung regulaer ist **/
	private JRadioButton regulaer = new JRadioButton("Regulär");
	/** den Zahlbetrag ausrechnen **/
	private JButton zahlbetrag = new JButton("Zahlbetrag ausrechnen");
	/** die Rechnung speichern **/
	private JButton ok = new JButton("Fertig");
	/** Landesschulrat **/
	private JRadioButton landesschulrat = new JRadioButton();

	// boolean
	/** ob es einen Fehler bei der Eingabe gegeben hat **/
	private boolean fehler;
	/** soll die Rechnungs upgedatet werden **/
	private boolean rechnungUpdate;
	/**
	 * kommt bei der Bestellung entweder die Kostenstelle gemeinsam oder el/et
	 * oder wi/mi vor
	 **/
	private boolean mitAnteil = false;

	// int
	private int bestID;
	/** Rechnnungsstatus der Rechnung **/
	private int rechnungsstatus;
	/** Anzahl der Zeilen **/
	private int zeilen;

	// Vector
	/** Sonderabzuege der einzelnen Bestellpositionen **/
	private Vector<Double> sonderabzuege = new Vector<Double>();
	/** Preis minus der Sonderabzuege der einzelnen Bestellpositionen **/
	private Vector<Double> preisMinusSonder = new Vector<Double>();
	/** Der gesamte Preis pro Bestellposition **/
	private Vector<Double> preisGesamt = new Vector<Double>();
	/** Preise, die zu bezahlen sind pro Bestellposition **/
	private Vector<Double> preisZuZahlen = new Vector<Double>();
	/** Kostenstellen, die betroffen sind **/
	private Vector<String> kostenstelle = new Vector<String>();
	/** IDs der gewaehlten Bestellpositionen **/
	private Vector<Integer> bestposIDs = new Vector<Integer>();
	/** Daten der gewaehlten Rechnung **/
	private Vector datenZuRechnung = new Vector();
	/** Namen der Sonderbudgets **/
	private Vector sonder = new Vector();
	/** Menge, die bezahlt werden kann: geliefert - bezahlt aus DB**/
	private Vector<Double> menge = new Vector<Double>();
	/**
	 * an welcher Stelle im Vector preisZuBezahlen steht der richtige Preis zur
	 * richtigen Bestellposition
	 **/
	private Vector<Integer> woImVector = new Vector<Integer>();
	/**
	 * Wieviele und in welcher Reihenfolge kommen die Kostenstellen el/et, wi/mi
	 * und gemeinsam vor
	 **/
	private Vector<Integer> welcheKostenstelle = new Vector<Integer>();
	/** Zeilennummer der angehakten Bestellpositionen **/
	private Vector<Integer> angeklickt = new Vector<Integer>();
	/**
	 * true; wenn die Bestellung nun fertig bezahlt ist; false: wenn noch
	 * BestPos ausständig sind
	 **/
	private boolean fertigBez;
	private boolean teilGesamt;

	/**
	 * Konstruktor1; wird aufgerufen, wenn es keine Kostenstellen mit Anteilen
	 * und keine vorhandenen Rechnungen gibt
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
	 * @param bestposIDs
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
	 */
	public RechnungEingabe(double summeSonderabzuege,
			Vector<Double> preisGesamt, int bestID, Connection con,
			Connection conL, boolean teilGesamt, String budget,
			Vector<Integer> bestposIDs, Vector<String> kostenstelle,
			int zeilen, Vector<Double> sonderabzuege,
			Vector<Double> preisMinusSonder, Vector<Integer> angeklickt,
			boolean fertigBez, Vector<Double> menge)

	{
		super("Rechnung");

		this.preisGesamt = preisGesamt;
		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.teilGesamt = teilGesamt;
		this.budget = budget;
		this.bestposIDs = bestposIDs;
		this.zeilen = zeilen;
		this.kostenstelle = kostenstelle;
		this.summeSonderabzuege = summeSonderabzuege;
		this.sonderabzuege = sonderabzuege;
		this.preisMinusSonder = preisMinusSonder;
		this.angeklickt = angeklickt;
		this.fertigBez = fertigBez;
		this.menge = menge;

		setLayout();
		setLayoutOhneVorhandeneRechnung();

	}

	/**
	 * Konstruktor2; wird aufgerufen, wenn es Kostenstellen mit Anteilen und
	 * keine vorhandene Rechnung gibt
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
	 * @param bestposIDs
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
	 * @param welcheBestellPositonen
	 *            wo im Vector stehen die richtigen Informationen
	 * @param angeklickt
	 *            an welcher Stelle im Vector bestposIds stehen die angeklicken
	 *            Bestellpositionen
	 */
	public RechnungEingabe(double summeSonderabzuege,
			Vector<Double> preisGesamt, int bestID, Connection con,
			Connection conL, boolean teilGesamt, String budget,
			Vector<Integer> bestposIDs, Vector<String> kostenstelle,
			int zeilen, Vector<Double> sonderabzuege,
			Vector<Double> preisMinusSonder, double[][] werte,
			Vector<Integer> welcheKostenstelle,
			Vector<Integer> welcheBestellPositonen, Vector<Integer> angeklickt,
			boolean fertigBez, Vector<Double> menge)

	{
		super("Rechnung");

		this.preisGesamt = preisGesamt;
		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.teilGesamt = teilGesamt;
		this.budget = budget;
		this.bestposIDs = bestposIDs;
		this.zeilen = zeilen;
		this.kostenstelle = kostenstelle;
		this.summeSonderabzuege = summeSonderabzuege;
		this.sonderabzuege = sonderabzuege;
		this.preisMinusSonder = preisMinusSonder;
		this.werte = werte;
		this.welcheKostenstelle = welcheKostenstelle;
		this.woImVector = welcheBestellPositonen;
		this.angeklickt = angeklickt;
		this.fertigBez = fertigBez;
		this.menge = menge;

		mitAnteil = true;

		setLayout();
		setLayoutOhneVorhandeneRechnung();

	}

	/**
	 * Konstruktor3; wird aufgerufen, wenn die Kostenstellen ohne Anteile sind
	 * aber mindestens eine Rechnung existiert
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
	 * @param bestposIDs
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
	 * @param datenZuRechnung
	 *            Daten der vorhandenen Rechnung
	 */
	public RechnungEingabe(double summeSonderabzuege,
			Vector<Double> preisGesamt, int bestID, Connection con,
			Connection conL, boolean teilGesamt, String budget,
			Vector<Integer> bestposIDs, Vector<String> kostenstelle,
			int zeilen, Vector<Double> sonderabzuege,
			Vector<Double> preisMinusSonder, Vector<Integer> angeklickt,
			Vector datenZuRechnung, boolean fertigBez, Vector<Double> menge) {

		super("Rechnung");

		this.summeSonderabzuege = summeSonderabzuege;
		this.preisGesamt = preisGesamt;
		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.teilGesamt = teilGesamt;
		this.budget = budget;
		this.kostenstelle = kostenstelle;
		this.zeilen = zeilen;
		this.sonderabzuege = sonderabzuege;
		this.preisMinusSonder = preisMinusSonder;
		this.angeklickt = angeklickt;
		this.datenZuRechnung = datenZuRechnung;
		this.fertigBez = fertigBez;
		this.menge = menge;

		setLayout();
		setLayoutMitVorhandeneRechnung();

	}

	/**
	 *Konstruktor4; wird aufgerufen, wenn die Kostenstellen mit Anteile sind
	 * aber mindestens eine Rechnung existiert
	 * 
	 * @param summeSonderabzuege
	 *            Summe der Sonderabzuege
	 * @param preisGesamt
	 *            BesetllpositionsIds
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
	 * @param welcheBestellPositonen
	 *            wo im Vector stehen die richtigen Informationen
	 * @param angeklickt
	 *            an welcher Stelle im Vector bestposIds stehen die angeklicken
	 *            Bestellpositionen
	 * @param datenZuRechnung
	 *            Daten der vorhandenen Rechnung
	 */

	public RechnungEingabe(double summeSonderabzuege,
			Vector<Double> preisGesamt, int bestID, Connection con,
			Connection conL, boolean teilGesamt, String budget,
			Vector<Integer> bestposIDs, Vector<String> kostenstelle,
			int zeilen, Vector<Double> sonderabzuege,
			Vector<Double> preisMinusSonder, double[][] werte,
			Vector<Integer> welcheKostenstelle,
			Vector<Integer> welcheBestellPositonen, Vector<Integer> angeklickt,
			Vector datenZuRechnung, boolean fertigBez, Vector<Double> menge) {

		super("Rechnung");

		this.summeSonderabzuege = summeSonderabzuege;
		this.preisGesamt = preisGesamt;
		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.teilGesamt = teilGesamt;
		this.budget = budget;
		this.kostenstelle = kostenstelle;
		this.zeilen = zeilen;
		this.sonderabzuege = sonderabzuege;
		this.preisMinusSonder = preisMinusSonder;
		this.werte = werte;
		this.welcheKostenstelle = welcheKostenstelle;
		this.woImVector = welcheBestellPositonen;
		this.angeklickt = angeklickt;
		this.datenZuRechnung = datenZuRechnung;
		this.fertigBez = fertigBez;
		this.menge = menge;

		mitAnteil = true;

		setLayout();
		setLayoutMitVorhandeneRechnung();

	}

	/**
	 * definiert das Layout
	 */
	public void setLayout() {
		if (budget.equals("UT8"))
			tabelle = "kostenstelleut8";
		else if (budget.equals("UT3"))
			tabelle = "abteilungut3";
		else if (budget.equals("LMB1"))
			tabelle = "lmb";
		else if (budget.equals("LMB2"))
			tabelle = "lmb";
		else
			tabelle = "sonderbudget";

		labels = new JLabel[hinweise.length];
		fields = new JTextField[hinweise.length];

		setLayout(new GridLayout(16, 3));

		di = new DatenImport(con, conL);
		wNummer = di.getWNummerZuBestellID(bestID);

		du = new DatenUpdate(con, conL);

		zahlart = "regulaer"; // defaultwert

		zweckgebunden.addActionListener(this);
		teilrechtsfaehig.addActionListener(this);
		regulaer.addActionListener(this);
		zahlbetrag.addActionListener(this);
		ok.addActionListener(this);
		landesschulrat.addActionListener(this);

		bg.add(zweckgebunden);
		bg.add(teilrechtsfaehig);
		bg.add(regulaer);
		bg.add(landesschulrat);

		regulaer.setSelected(true);

	}

	/**
	 * definiert das Layout, wenn es keine vorhandenen Rechnungen gibt
	 */
	public void setLayoutOhneVorhandeneRechnung() {

		rechnungUpdate = false;

		for (int i = 0; i < hinweise.length - 3; i++) {
			labels[i] = new JLabel(hinweise[i]);
			add(labels[i]);
			fields[i] = new JTextField();
			add(fields[i]);
			add(new JLabel(""));
		}

		add(labels[8] = new JLabel(hinweise[8]));
		add(fields[8] = new JTextField());
		add(skontoEuro);

		add(labels[hinweise.length - 1] = new JLabel(
				hinweise[hinweise.length - 1]));
		add(fields[hinweise.length - 1] = new JTextField());
		add(new JLabel(""));

		add(labels[hinweise.length - 2] = new JLabel(
				hinweise[hinweise.length - 2] + ":"));
		labels[hinweise.length - 2]
				.setFont(new Font("SansSerif", Font.BOLD, 12));
		add(new JLabel(""));
		add(new JLabel(""));

		add(zweckgebunden);
		add(teilrechtsfaehig);
		add(regulaer);

		sonder = di.getSonderbudgets();

		for (int i = 0; i < sonder.size(); i++)
			combo.addItem(sonder.get(i).toString());

		add(new JLabel("Landesschulrat"));
		add(landesschulrat);
		add(combo);

		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));

		add(zahlbetrag);
		add(new JLabel(""));
		add(ok);

		for (int i = 0; i < preisMinusSonder.size(); i++)
			bestellbetrag += runde(preisMinusSonder.get(i)
					+ sonderabzuege.get(i));

		fields[0].setText(wNummer);
		fields[0].setEditable(false);
		fields[1].setText(" " + bestellbetrag);
		fields[1].setEditable(false);
		fields[7].setText("-" + runde(summeSonderabzuege));
		fields[7].setEditable(false);
		skontoEuro.setEditable(false);
		fields[hinweise.length - 1].setEditable(false);

	}

	/**
	 * definiert das Layout, wenn es bereits mindestens eine Rechnung zu dieser
	 * Bestellung gibt
	 */
	public void setLayoutMitVorhandeneRechnung() {
		rechnungUpdate = true;

		for (int i = 0; i < hinweise.length - 3; i++) {
			labels[i] = new JLabel(hinweise[i]);
			add(labels[i]);
			if (i == 2)
				fields[i] = new JTextField();
			else
				fields[i] = new JTextField(datenZuRechnung.get(i + 1)
						.toString());
			add(fields[i]);
			add(new JLabel(""));

		}
		fields[1].setText(" " + bestellbetrag);

		add(labels[8] = new JLabel(hinweise[8]));
		add(fields[8] = new JTextField());
		add(skontoEuro);

		add(labels[hinweise.length - 1] = new JLabel(
				hinweise[hinweise.length - 1]));
		add(fields[hinweise.length - 1] = new JTextField());
		add(new JLabel(""));

		add(labels[hinweise.length - 2] = new JLabel(
				hinweise[hinweise.length - 2] + ":"));
		labels[hinweise.length - 2]
				.setFont(new Font("SansSerif", Font.BOLD, 12));
		add(new JLabel(""));
		add(new JLabel(""));

		add(zweckgebunden);
		add(teilrechtsfaehig);
		add(regulaer);

		sonder = di.getSonderbudgets();

		for (int i = 0; i < sonder.size(); i++)
			combo.addItem(sonder.get(i).toString());

		add(new JLabel("Landesschulrat"));
		add(landesschulrat);
		add(combo);

		add(new JLabel(""));
		add(new JLabel(""));
		add(new JLabel(""));

		add(zahlbetrag);
		add(new JLabel(""));
		add(ok);

		for (int i = 0; i < preisMinusSonder.size(); i++)
			bestellbetrag += runde(preisMinusSonder.get(i)
					+ sonderabzuege.get(i));

		fields[0].setText(wNummer);
		fields[0].setEditable(false);
		fields[1].setText(" " + bestellbetrag);
		fields[1].setEditable(false);
		fields[7].setText("-" + runde(summeSonderabzuege));
		fields[7].setEditable(false);
		fields[3].setEditable(false);
		fields[4].setEditable(false);
		fields[5].setEditable(false);
		fields[6].setEditable(false);
		skontoEuro.setEditable(false);
		fields[hinweise.length - 1].setEditable(false);

	}

	/**
	 * Action Performed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == landesschulrat)
			zahlart = "Landesschulrat";

		if (e.getSource() == zweckgebunden) {

			zahlart = "zweckgebunden";
		}
		if (e.getSource() == teilrechtsfaehig) {
			zahlart = "teilrechtsfaehig";
		}
		if (e.getSource() == regulaer) {
			zahlart = "regulaer";
		}

		if (e.getSource() == ok) {
			fehler = false;
			rechnungsbetrag = getRechnungsbetrag();
			skontoProzent = getSkonto();

			minusBetrag = (bestellbetrag - rechnungsbetrag) / zeilen;

			preisZuZahlen.clear();
			for (int i = 0; i < preisMinusSonder.size(); i++) {
				skontoJeBestPos = (preisMinusSonder.get(i) - minusBetrag)
						* skontoProzent / 100;
				preisZuZahlen.add(preisMinusSonder.get(i) - minusBetrag
						- skontoJeBestPos);
			}

			if (fields[3].getText().length() == 0)
				externeNummer = "Keine Angabe";
			else
				externeNummer = fields[3].getText();

			if (fields[4].getText().length() == 0)
				interneNummer = "Keine Angabe";
			else
				interneNummer = fields[4].getText();

			if (fields[5].getText().length() == 0)
				inventarnummer = "Keine Angabe";
			else
				inventarnummer = fields[5].getText();

			if (fields[6].getText().length() == 0)
				buchhaltungsbeleg = "Keine Angabe";
			else
				buchhaltungsbeleg = fields[6].getText();

			if (externeNummer.length() > 20 || interneNummer.length() > 20
					|| inventarnummer.length() > 15
					|| buchhaltungsbeleg.length() > 15)
				JOptionPane
						.showMessageDialog(
								this,
								"Bitte beachten Sie die Länge Ihrer Eingaben. Die externe und interne Nummer dürfen nicht größer als 20 Zeichen lang sein und die Inventarnummer und die Buchhaltungsbelegnummer dürfen nicht länger als 15 Zeichen lang sein");

			if (mitAnteil) {
				berechneAnteile(werte, welcheKostenstelle, preisZuZahlen,
						woImVector);

			}
			if (!fehler) {
				/*
				 * if (rechnungUpdate) updateRechnung(); else
				 */

				speichereInDatenbank();
			}
			dispose();

		}
		if (e.getSource() == zahlbetrag) {

			skontoEuro.setText("" + berechneSkonto(summeSonderabzuege));
			fields[hinweise.length - 1].setText(""
					+ getZahlbetrag(rechnungsbetrag, summeSonderabzuege,
							skontoBetrag));
		}

	}

	/**
	 * gibt den Rechnungsbetrag zurueck
	 * 
	 * @return Rechnungsbetrag
	 */
	public double getRechnungsbetrag() {
		if ((fields[2].getText().length() == 0)) {
			rechnungsbetrag = 0;

		} else
			
			rechnungsbetragS= fields[2].getText();
			rechnungsbetragS = rechnungsbetragS.replace(',', '.');
			
			try {

				rechnungsbetrag = Double.parseDouble(rechnungsbetragS);

			} catch (NumberFormatException e1) {
				JOptionPane
						.showMessageDialog(this,
								"Bitte geben Sie für den Rechnungsbetrag nur Zahlen ein");
				fehler = true;

			}
		return rechnungsbetrag;
	}

	/**
	 * berechnet die Anteile der Abteilungen, wenn die Kostenstelle wi/mi, el/et
	 * oder gemeinsam ist
	 * 
	 * @param werte
	 *            Prozentwerte
	 * @param welcheKostenstelle
	 *            welcheKostenstelle (gemeinsam, el/et,wi/mi)
	 * @param preisZuZahlen
	 *            Preis zu bezahlen
	 * @param woImVector
	 *            wo im Vector preisZuZahlen stehen die benoetigten
	 *            Informationen
	 */
	public void berechneAnteile(double werte[][],
			Vector<Integer> welcheKostenstelle, Vector<Double> preisZuZahlen,
			Vector<Integer> woImVector) {

		el = 0;
		et = 0;
		wi = 0;
		mi = 0;
		lt = 0;

		for (int i = 0; i < welcheKostenstelle.size(); i++) {

			if (welcheKostenstelle.get(i) == 1) {

				wi += preisZuZahlen.get(woImVector.get(i))
						* (werte[i][0] / 100);
				mi += preisZuZahlen.get(woImVector.get(i))
						* (werte[i][1] / 100);
				et += preisZuZahlen.get(woImVector.get(i))
						* (werte[i][2] / 100);
				el += preisZuZahlen.get(woImVector.get(i))
						* (werte[i][3] / 100);

				if (werte[i].length == 5)
					lt += preisZuZahlen.get(woImVector.get(i))
							* (werte[i][4] / 100);

			}
			if (welcheKostenstelle.get(i) == 2) {

				et += preisZuZahlen.get(woImVector.get(i))
						* (werte[i][0] / 100);
				el += preisZuZahlen.get(woImVector.get(i))
						* (werte[i][1] / 100);

			}
			if (welcheKostenstelle.get(i) == 3) {

				wi += preisZuZahlen.get(woImVector.get(i))
						* (werte[i][0] / 100);
				mi += preisZuZahlen.get(woImVector.get(i))
						* (werte[i][1] / 100);

			}
		}
		el = runde(el);
		wi = runde(wi);
		mi = runde(mi);
		et = runde(et);

	}

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

	/**
	 * liefert den Skonto in Prozent
	 * 
	 * @return Skonto in Prozent
	 */
	public double getSkonto() {
		if ((fields[8].getText().length() == 0)) {
			skontoProzent = 0;

		} else
			try {

				skontoProzent = Double.parseDouble(fields[8].getText());

			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this,
						"Bitte geben Sie den Skonto in Zahlen ein");
				fehler = true;
			}
		return skontoProzent;
	}

	/**
	 * berechnet den Skonto in Euro
	 * 
	 * @param sonderabzug
	 *            Sonderabzuege
	 * @return Skonto in Euro
	 */
	public double berechneSkonto(double sonderabzug) {

		rechnungsbetrag = getRechnungsbetrag();
		skontoProzent = getSkonto();

		skontoBetrag = runde((rechnungsbetrag - sonderabzug)
				* (skontoProzent / 100));

		return skontoBetrag;

	}

	/**
	 * liefert den zu zahlenden Betrag
	 * 
	 * @param rechnungsbetrag
	 *            Rechnungsbetrag
	 * @param summeSonderabzuege
	 *            Summe der Sonderabzuege
	 * @param skonto
	 *            Skonto in euro
	 * @return Zahlbetrag
	 */
	public double getZahlbetrag(double rechnungsbetrag,
			double summeSonderabzuege, double skonto) {
		zahlbetragEuro = runde(rechnungsbetrag - summeSonderabzuege - skonto);

		return zahlbetragEuro;
	}

	/**
	 * speichert die Rechnung in die Datenbank
	 */
	public void speichereInDatenbank() {
		try {

			stmt = con.createStatement();

			if (!fertigBez) {
				du.aendereStatusBezahlung(bestID, 3);
				rechnungsstatus = 1;
			} else {
				du.aendereStatusBezahlung(bestID, 4);
				if (!teilGesamt)// Teilrechnung
				{
					rechnungsstatus = 1;
				} else// Gesamtrechnung
				{
					rechnungsstatus = 2;
				}
			}
			
		
			
			for (int i = 0; i < bestposIDs.size(); i++) { 
				du.aendereBezahlt(bestposIDs.get(i), menge.get(i));
				if (!di.alleGeliefert(bestposIDs.get(i)))
					du.aendereStatusBestellPos(bestposIDs.get(i), 3);
				else
					du.aendereStatusBestellPos(bestposIDs.get(i), 4);
			}

			query = "insert into rechnung(rechnungsbetrag,bestellbetrag,skonto,externeNummer,interneNummer,zahlart,wNummer,inventarnummer,buchhaltungsbelege,rechnungsstatus,sonderabzug) values("
					+ rechnungsbetrag
					+ ","
					+ bestellbetrag
					+ ","
					+ skontoProzent
					+ ",'"
					+ externeNummer
					+ "','"
					+ interneNummer
					+ "','"
					+ zahlart
					+ "','"
					+ wNummer
					+ "','"
					+ inventarnummer
					+ "','"
					+ buchhaltungsbeleg
					+ "',"
					+ rechnungsstatus + "," + summeSonderabzuege + ");";

			stmt.executeUpdate(query);
			JOptionPane.showMessageDialog(this,
					"Die Rechnung wurde erfoglreich erstellt");

			
			// ABBUCHEN

			abbuchen();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this,
					"Fehler beim Einlesen in die Datenbank");
			e.printStackTrace();
		}

	}

	/**
	 * updatet einer bereits vorhandene Rechnung
	 */
	/*
	 * public void updateRechnung() { try {
	 * 
	 * stmt = con.createStatement();
	 * 
	 * if (!teilGesamt) {//Teilrechnung du.aendereStatusBezahlung(bestID, 3);
	 * rechnungsstatus = 1; } else{//Gesamtrechnung
	 * du.aendereStatusBezahlung(bestID, 4); rechnungsstatus = 2; }
	 * 
	 * for (int i = 0; i < bestposIDs.size(); i++) { for (int j = 0; j <
	 * angeklickt.size(); j++) { if (angeklickt.get(j) == i) {
	 * 
	 * du.aendereStatusBestellPos(bestposIDs.get(i), 4); } } }
	 * 
	 * query = "update rechnung set rechnungsbetrag=rechnungsbetrag+" +
	 * rechnungsbetrag + ",skonto=skonto+" + skontoProzent +
	 * ",sonderabzug=sonderabzug+" + summeSonderabzuege + ",externeNummer='" +
	 * externeNummer + "',bestellbetrag=bestellbetrag+" + bestellbetrag +
	 * ",rechnungsstatus=" + rechnungsstatus + " where nummer=" +
	 * datenZuRechnung.get(0);
	 * 
	 * stmt.executeUpdate(query); JOptionPane.showMessageDialog(this,
	 * "Die Rechnung wurde erfoglreich verändert.");
	 * 
	 * abbuchen();
	 * 
	 * } catch (SQLException e) { JOptionPane.showMessageDialog(this,
	 * "Fehler beim Einlesen in die Datenbank"); e.printStackTrace(); }
	 * 
	 * }
	 */

	/**
	 * bucht die Betraege um
	 */
	public void abbuchen() {

		if (landesschulrat.isSelected()) {

			String name = combo.getSelectedItem().toString();

			du.aendereBudgetBeiBestellung(name, wNummer);

			du.aendereAusgegebenMehr(getZahlbetrag(rechnungsbetrag,
					summeSonderabzuege, skontoBetrag), name);

			for (int i = 0; i < zeilen; i++)
				du.aendereGesperrtMinus(tabelle, preisGesamt.get(i),
						kostenstelle.get(i), budget);

		} else

		if (zahlart.equals("zweckgebunden")
				|| zahlart.equals("teilrechtsfaehig")) {
			for (int i = 0; i < zeilen; i++) {

				du.aendereGesperrtMinus(tabelle, preisGesamt.get(i),
						kostenstelle.get(i), budget);

				if (budget.length() != 0)
					du.aendereGesperrtMinus(tabelle, Double
							.parseDouble(preisGesamt.get(i).toString()),
							budget, budget);

			}
		} else {
			if (mitAnteil) {
				berechneAnteile(werte, welcheKostenstelle, preisZuZahlen,
						woImVector);

				for (int i = 0; i < welcheKostenstelle.size(); i++) {
					if (welcheKostenstelle.get(i) == 1) {

						du.aenderegesperrtMinusMitAnteil(preisGesamt
								.get(woImVector.get(i)), budget, "gemeinsam");

					}
					if (welcheKostenstelle.get(i) == 2) {

						du.aenderegesperrtMinusMitAnteil(preisGesamt
								.get(woImVector.get(i)), budget, "EL/ET");
					}

					if (welcheKostenstelle.get(i) == 3) {

						du.aenderegesperrtMinusMitAnteil(preisGesamt
								.get(woImVector.get(i)), budget, "WI/MI");

					}

					du.aendereGesperrtZuAusgegeben(tabelle, preisZuZahlen
							.get(woImVector.get(i)), preisGesamt.get(woImVector
							.get(i)), budget, budget);

				}

				du.aendereAusgegebenAnteile(el, et, wi, mi, lt, budget);

				for (int i = 0; i < zeilen; i++) {
					boolean keineAnteilKostenstelle = true;
					for (int j = 0; j < woImVector.size(); j++) {

						if (i == woImVector.get(j)) {

							keineAnteilKostenstelle = false;

						}
					}
					if (keineAnteilKostenstelle)

					{
						du.aendereGesperrtZuAusgegeben(tabelle, preisZuZahlen
								.get(i), preisGesamt.get(i), kostenstelle
								.get(i), budget);

						du.aendereGesperrtZuAusgegeben(tabelle, preisZuZahlen
								.get(i), preisGesamt.get(i), budget, budget);

					}

				}

			} else {
				for (int i = 0; i < zeilen; i++) {

					du.aendereGesperrtZuAusgegeben(tabelle, preisZuZahlen
							.get(i), preisGesamt.get(i), kostenstelle.get(i),
							budget);

					if (budget.equals("UT8") || budget.equals("UT3")
							|| budget.equals("LMB1")
							|| budget.equals("LMB1_2011")) {
						du.aendereGesperrtZuAusgegeben(tabelle, preisZuZahlen
								.get(i), preisGesamt.get(i), budget, budget);

					}
				}
			}
		}

	}
}
