package Budget;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

/**
 *In der Klasse Vorlage wird das Layout erzeugt fuer alle Karten
 *<p>
 * Title: Vorlage
 * 
 * @author Haupt, Liebhart
 **/
public class Vorlage extends JPanel implements ActionListener {

	// Vector
	/** Vector mit den ausgelesenen Daten **/
	private Vector daten;
	/** Vector mit einzelnen Datensaetzen **/
	private Vector satz;
	/** Hilfsvector **/
	private Vector hilfsvector;
	/** Hilfsvector fuer die Nummern bei UT8 **/
	private Vector nummern = new Vector();

	// Buttons
	/** Button bearbeiten **/
	private JButton bearbeiten = new JButton("BEARBEITEN");;
	/** Button Neues Budget anlegen **/
	private JButton budgetAnlegen;
	/** Untertabellen anzeigen **/
	private JButton mehr = new JButton("mehr");
	/** zurueck zur oberen Tabelle **/
	private JButton zurueck = new JButton("zurück");
	/** zeigt Details zu den Projekten an **/
	private JButton details = new JButton("Details");
	/** das Hauptbudget bearbeiten **/
	private JButton bearbeitenHB = new JButton("Bearbeiten Hauptbudget");
	/** die Tabellen aktualisieren **/
	private JButton aktualisieren = new JButton("Aktualisiern");
	/** Datensatz aendern **/
	private JButton aendern = new JButton("Ändern");
	/** Datensatz loeschen **/
	private JButton loeschen = new JButton("Löschen");
	/** die Anteile fuer die Projekte anzeigen **/
	private JButton anteil = new JButton("Anteile");
	/** den LT- Anteil eingeben **/
	private JButton ltanteil = new JButton("LT- Anteil");
	/** den EDV- HAT Anteil eingeben **/
	private JButton edvHat = new JButton("EDV- HAT Anteil");
	private JButton buchungen = new JButton("Aushändigungen");

	// Panels
	/** Panel, in dem die Tabellen sind **/
	private JPanel hilfP = new JPanel();
	/** Panel, das den Button bearbeiten und neues Budget anlegen beinhaltet **/
	private JPanel buttonsEast = new JPanel();
	/** Panel, das die Buttons mehr und zurueck beinhaltet **/
	private JPanel buttonsSouth = new JPanel();

	// Tabellen
	/** Tabelle fuer die Datensaetze **/
	private Tabelle tabelle;
	/** Tabelle, in dem das Hauptbudget dargestellt wird **/
	private Tabelle tabelleOben;
	/** Tabellen fuer das Budget UT8 **/
	private Tabelle[] tables = new Tabelle[4];
	/** Tabelle fuer die Daten des Hauptbudgets UT8 **/
	private Tabelle tabelleObenUt8;

	// int
	/**
	 * jede Tabelle hat eine Nummer:
	 * ut3=1,projekt=2,lmb1=3,sonderbudget=4,hauptbereichut8
	 * =5,bereichut8=6,hauptkostenstelleut8=7,kostenstelleut8=8,lmb2=9
	 **/
	private int kennnummer;
	/** Kennung fuer die Tabelle lmb, lmb1=1; lmb2=2 **/
	private int kennnung;
	/** Zeilennummer, die gleichzeitig eine where- Bedingung ist **/
	private int whereK;
	/** Nummer, der markierten Zeile **/
	private int zeilennummer;
	/**
	 * wie Oft wurde schon der Button "mehr" gedrueckt; gibt Auskunft darueber,
	 * in welcher Tabelle man sich befindet
	 **/
	private int wieOft;

	// int[]
	/** Nummern, der markierten Zeilen **/
	private int[] merke = new int[3];

	// String
	/** Tabellenname **/
	private String tabelleName;
	/** Tabellenname der naechsten Tabelle **/
	private String tabelleName2;
	/**
	 * Name einer Spalte einer Tabelle; wird benoetigt um enstprechende Daten
	 * aus dieser Tabelle zu bekommen
	 **/
	private String spalteDatenbank = "Nummer";
	/** Name des gewaehlten projektes **/
	private String projektname;

	// String[]
	/** Spaltennamen des LMB Hauptbudgets **/
	private String[] spalteLMB = { "Nummer", "Name", "geplant", "ausgegeben",
			"gesperrt", "verfügbar" };
	/** Spaltennamen des UT3 Hauptbudgets **/
	private String[] spalteUT3 = { "Nummer", "Name", "EDV_HAT_Anteil",
			"geplant", "ausgegeben", "gesperrt", "verfügbar" };
	/**
	 * es werden die benoetigten Spalten aus dem spaltennmane[][]
	 * herausgespeichert
	 **/
	private String[] spalte;
	/** Namen der Tabellen der Datenbank **/
	private String[] tabellennamen = { "abteilungut3", "projekt", "lmb",
			"sonderbudget", "hauptbereichut8", "bereichut8",
			"hauptkostenstelleut8", "kostenstelleut8", "lmb" };
	/** Spaltennamen fuer die Tabelle der Anteile vom Projekt **/
	private String[] spaltenAnteil = { "Budget", "Betrag in ", "Prozent" };
	/** Namen der Buttons **/
	private String[] namenFuerButtons = { "Neue Abteilung anlegen",
			"Neues Projekt anlegen", "Neue Abteilung anlegen",
			"Neues Sonderbudget anlegen", "Neuen Hauptbereich anlegen",
			"Neuen Bereich anlegen", "Neue Hauptkostenstelle anlegen",
			"Neue Kostenstelle anlegen", "Neue Abteilung anlegen" };
	/** Spaltennamen fuer die Tabelle der Details vom Projekt **/
	private String[] spaltenDetailsProjekt = { "BanfID", "Antragsteller",
			"Kostenstelle", "Status Lieferung", "Datum", "Firma" };
	private String[] spaltenBuchungenProjekt = { "BuchungsID", "User",
			"Datum", "Gesamtpreis", "Stück", "Material", "Firma" };

	// String[][]
	/**
	 * Spaltennamen der Tabellen [0]= abteilungut3,[1]= projekt,[2]=lmb1,[3]=
	 * sonderbudget,[4]= hauptbereichut8,[5]= bereichut8, [6]=
	 * hauptkostenstelleut8,[7]= kostenstelleut8,[8]=lmb2, die Spalten sind
	 * immer an der Stelle[kennnummer-1]
	 **/
	private String[][] spaltennamen = {
			{ "Nummer", "Name", "EDV_HAT_Anteil", "fest geplant/Projektnr.",
					"geplant", "ausgegeben", "gesperrt", "verfügbar" },
			{ "Nummer", "Name", "Lehrer", "Kurzz", "Klasse", "Datum",
					"Teilnehmer", "Abteilung" },
			{ "Nummer", "Name", "fest geplant/Projektnr.", "geplant",
					"ausgegeben", "gesperrt", "verfügbar" },
			{ "Nummer", "Name", "geplant", "ausgegeben", "gesperrt",
					"verfügbar" },
			{ "Nummer", "Name", "geplant", "ausgegeben", "gesperrt",
					"verfügbar" },
			{ "Nummer", "Name", "geplant", "ausgegeben", "gesperrt",
					"verfügbar" },
			{ "Nummer", "Name", "geplant", "ausgegeben", "gesperrt",
					"verfügbar" },
			{ "Nummer", "Name", "Raumnummer", "Kurzbezeichnung", "geplant",
					"ausgegeben", "gesperrt", "verfügbar" },
			{ "Nummer", "Name", "fest geplant/Projektnr.", "geplant",
					"ausgegeben", "gesperrt", "verfügbar" } };

	// ScrollPanes
	/** ScrollPane fuer die Tabelle **/
	private JScrollPane scroll;
	/** Hilfsvariable **/
	private JScrollPane scrollHilf;

	// Connections
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	/** Connection zur Logistikdatenbank **/
	private Connection conL;

	// double[]
	/** Werte des EDV Anteils und des HAT Anteils **/
	double[] werte = new double[2];

	// boolean
	/** wenn eine Zeile markiert ist: true **/
	private boolean markiert = false;
	/**
	 * true: das Layout der Karte wird zum ersten Mal erstellt; false: das
	 * Layout wurde schon einmal erstellt
	 **/
	private boolean anfang = true;

	// Objekte unserer eigenen Klassen
	/** Eingabemaske, in der man ein neues Budget anlegen kann **/
	private NewDatensatz maske;
	/** Eingabemaske, in der man das geplante Budget aendern kann **/
	private EditBudget maske2;
	/** Fenster, in dem Tabellen dargestellt werden **/
	private AnzAbfrage fenster;
	/** um Daten von der Datenbank zu bekommen **/
	private DatenImport di;
	/** um Daten in der Datenbank upzudaten **/
	private DatenUpdate du;
	/** um Daten aendern zu koennen **/
	private EditDatensatz ds;
	/** um den LT- Anteil eingeben zu koennen **/
	private BezLTAnteil la;
	/** um den EDV-HAT- Anteil eingeben zu koennen **/
	private BezEDVHAT edvHatAnteil;
	/** Objekt von der Klasse Abteilungsanteile **/
	private BezAbteilungsanteile at;
	
	private NewTextdatei txt = new NewTextdatei();

	// sonstiges
	/** notwendige Variable fuer das GridBag Layout **/
	private GridBagConstraints c;

	// JLabel
	/** Label EDV- Anteil **/
	private JLabel edv;
	/** Label HAT- Anteil (Haustechnik-Anteil) **/
	private JLabel hat;
	/** Datum **/
	private Date datum;
	/** Jahreszahl, wird für Bezeichnung des Budgets LMB1_x verwendet **/
	private int jahreszahl;

	/**
	 * Konstruktor
	 * 
	 * @param kennnummer
	 *            Kennnummer der Tabelle
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 */
	public Vorlage(int kennnummer, Connection con, Connection conL, int jahreszahl) {
		this.kennnummer = kennnummer;
		this.con = con;
		this.conL = conL;
		this.jahreszahl = jahreszahl;

		di = new DatenImport(con, conL);
		du = new DatenUpdate(con, conL);
		at = new BezAbteilungsanteile(con, conL);

		c = new GridBagConstraints();

		// Name der Tabelle

		tabelleName = tabellennamen[kennnummer - 1];

		// die benoetigten Spaltennamen aus dem Spaltennamen[][]
		spalte = new String[spaltennamen[kennnummer - 1].length];

		for (int i = 0; i < spaltennamen[kennnummer - 1].length; i++) {
			spalte[i] = spaltennamen[kennnummer - 1][i];
		}

		tabelleName = tabellennamen[kennnummer - 1];
		whereK = 1; // default Wert ist 1
		wieOft = 6; // default ist 6, weil bereich hat die Kennnummer 6
		markiert = false;

		// Layout setzen
		setLayout(new BorderLayout());
		buttonsEast.setLayout(new GridLayout(20, 1));

		if (kennnummer == 1 || kennnummer == 3 || kennnummer > 4) {
			erstelleLayoutAnders();
			buttonsEast.add(bearbeitenHB);
		} else

			erstelleLayout();

		// bei ut8 gibt es zusaetzlich noch Buttons
		if (kennnummer > 4 && kennnummer < 9) {
			buttonsSouth.add(mehr);
			buttonsSouth.add(zurueck);
			zurueck.setEnabled(false);

		}// bei Projekt gibt es zusaetzlich noch Buttons
		if (kennnummer == 2) {
			buttonsEast.add(new JLabel(""));
			buttonsEast.add(details);
			buttonsEast.add(anteil);
			buttonsEast.add(buchungen);

		}

		// je nach dem wo man sich befindet, haben die Buttons andere Namen
		budgetAnlegen = new JButton(namenFuerButtons[kennnummer - 1]);

		// Buttons dem ActionListener adden
		bearbeiten.addActionListener(this);
		mehr.addActionListener(this);
		budgetAnlegen.addActionListener(this);
		zurueck.addActionListener(this);
		details.addActionListener(this);
		aktualisieren.addActionListener(this);
		bearbeitenHB.addActionListener(this);
		aendern.addActionListener(this);
		loeschen.addActionListener(this);
		anteil.addActionListener(this);
		ltanteil.addActionListener(this);
		edvHat.addActionListener(this);
		buchungen.addActionListener(this);

		bearbeiten
				.setToolTipText("Bearbeiten des geplanten Betrages (von dem markierten Eintrag)");
		mehr.setToolTipText("Untertabellen anzeigen");
		zurueck.setToolTipText("zurück zur Übertabelle");
		details.setToolTipText("Banfs zu einem bestimmten Projekt");
		aktualisieren.setToolTipText("Aktualisieren");
		bearbeitenHB.setToolTipText("Bearbeiten des Hauptbudgets");
		aendern.setToolTipText("Ändern des markierten Eintrages");
		loeschen.setToolTipText("Löschen des markierten Eintrages");
		anteil.setToolTipText("Budget-Anteile des Projektes");
		ltanteil.setToolTipText("Anteil der Abteilung Lebensmitteltechnologie");
		edvHat.setToolTipText("EDV- HAT- Anteil");
		buchungen.setToolTipText("Aushändigungen aus Zentrallager");

		// dem Panel die Buttons adden
		if (kennnummer != 2) {
			buttonsEast.add(new JLabel(""));
			buttonsEast.add(bearbeiten);
		}
		buttonsEast.add(new JLabel(""));
		buttonsEast.add(budgetAnlegen);
		buttonsEast.add(new JLabel(""));
		buttonsEast.add(aktualisieren);
		buttonsEast.add(new JLabel(""));

		if (kennnummer != 1 && kennnummer != 3 && kennnummer != 9)
			buttonsEast.add(aendern);
		buttonsEast.add(loeschen);

		if (kennnummer == 3 || kennnummer == 9) {
			buttonsEast.add(new JLabel(""));
			buttonsEast.add(ltanteil);

		}
		if (kennnummer == 1) {

			buttonsEast.add(new JLabel(""));
			buttonsEast.add(edvHat);
			werte = di.getEDVHATAnteil();
			buttonsEast.add(new JLabel(""));
			hat = new JLabel("HAT- Anteil: " + ("" + werte[1]) + "%");
			edv = new JLabel("EDV- Anteil: " + ("" + werte[0]) + "%");
			buttonsEast.add(edv);
			buttonsEast.add(hat);

		}

		add(buttonsEast, BorderLayout.EAST);
		add(buttonsSouth, BorderLayout.SOUTH);

	}// end Konstruktor

	/**
	 * erstellt das Layout fuer die Tabellen Sonderbudget und Projekte
	 */
	public void erstelleLayout() {

		daten = di.auslesenDaten(tabelleName, kennnummer);
		tabelle = new Tabelle(spalte, daten, con, conL);
		scroll = tabelle.getTabelle();

		add(scroll);
	}// end erstelleLayout()

	/**
	 * erstellt das Layout fuer die Tabellen UT3, UT8 und LMB1/2
	 */
	public void erstelleLayoutAnders() {
		hilfP = new JPanel(); // Hilfspanel

		hilfP.setLayout(new GridBagLayout());

		// oberes Label
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.ipadx = 950;
		c.ipady = 30;
		c.gridx = 0;
		c.gridy = 0;
		
		datum = new Date();

		if (kennnummer == 1) // UT3
			hilfP.add(new JLabel("Hauptbudget UT3"), c);
		else if (kennnummer == 5) // UT8
			hilfP.add(new JLabel("Hauptbudget UT8"), c);
		else if (kennnummer == 3) // LMB1
			hilfP.add(new JLabel("Hauptbudget LMB1"), c);
		else if (kennnummer == 9)
			hilfP.add(new JLabel("Hauptbudget LMB2"), c);

		// obere Tabelle
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 2;

		if (kennnummer == 1) // UT3
		{
			whereK = 1;
			daten = di.auslesenDatenMitWhereKlausel(tabelleName,
					spalteDatenbank, whereK, kennnummer);
			tabelleOben = new Tabelle(spalteUT3, daten, con, conL);
			hilfP.add(tabelleOben.getHeader(), c);

			c.gridy = 3;
			hilfP.add(tabelleOben.getNurTabelle(), c);
		} else if (kennnummer == 5) // UT8
		{
			whereK = 1;
			daten = di.auslesenDatenMitWhereKlausel(tabelleName,
					spalteDatenbank, whereK, kennnummer);
			tabelleObenUt8 = new Tabelle(spalte, daten, con, conL);
			hilfP.add(tabelleObenUt8.getHeader(), c);

			c.gridy = 3;
			hilfP.add(tabelleObenUt8.getNurTabelle(), c);
		} else if (kennnummer == 3) // LMB1
		{
			kennnung = 1;
			whereK = 1;
			daten = di.auslesenDatenMitWhereKlausel(tabelleName,
					spalteDatenbank, whereK, kennnummer);
			tabelleOben = new Tabelle(spalteLMB, daten, con, conL);
			hilfP.add(tabelleOben.getHeader(), c);

			c.gridy = 3;
			hilfP.add(tabelleOben.getNurTabelle(), c);
		} else if (kennnummer == 9) // LMB2
		{
			whereK = 2;
			daten = di.auslesenDatenMitWhereKlausel(tabelleName,
					spalteDatenbank, whereK, kennnummer);
			tabelleOben = new Tabelle(spalteLMB, daten, con, conL);
			hilfP.add(tabelleOben.getHeader(), c);

			c.gridy = 3;
			hilfP.add(tabelleOben.getNurTabelle(), c);
		}

		// unteres Label
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 4;
		c.ipady = 30;

		if (kennnummer == 1) // UT3
			hilfP.add(new JLabel("Abteilungen"), c);
		else if (kennnummer == 5) // UT8
			hilfP.add(new JLabel("Hauptbereiche"), c);
		else if (kennnummer == 3) // LMB1
			hilfP.add(new JLabel("LMB1"), c);
		else if (kennnummer == 9) // LMB2
			hilfP.add(new JLabel("LMB2"), c);

		// untere Tabelle
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 30;
		c.ipadx = 600;
		c.weightx = 0.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 6;

		if (kennnummer == 1) // UT3
		{
			daten = di.auslesenDaten(tabelleName, kennnummer);

			for (int i = 0; i < daten.size(); i++) {
				hilfsvector = (Vector) daten.get(i);
				if (Integer.parseInt(hilfsvector.get(0).toString()) == 1)
					daten.remove(i);
			}
			tabelle = new Tabelle(spalte, daten, con, conL);
			scroll = tabelle.getTabelle();
		} else if (kennnummer == 5) // UT8
		{
			daten = di.auslesenDaten(tabelleName, kennnummer);

			for (int i = 0; i < daten.size(); i++) {
				hilfsvector = (Vector) daten.get(i);
				if (hilfsvector.get(0).toString().equals("0."))
					daten.remove(i);
			}

			tables[kennnummer - 5] = new Tabelle(spalte, daten, con, conL);
			scroll = tables[kennnummer - 5].getTabelle();
		} else if (kennnummer == 3) // LMB1
		{
			daten.clear();
			spalteDatenbank = "kennung";
			daten = di.auslesenDatenMitWhereKlausel(tabelleName,
					spalteDatenbank, kennnung, kennnummer); // Daten werden
			for (int i = 0; i < daten.size(); i++) {
				hilfsvector = (Vector) daten.get(i);
				if (Integer.parseInt(hilfsvector.get(0).toString()) == 1)
					daten.remove(i);

			}
			tabelle = new Tabelle(spalte, daten, con, conL);
			scroll = tabelle.getTabelle();
		} else if (kennnummer == 9) // LMB2
		{
			spalteDatenbank = "kennung";
			kennnung = 2;

			daten.clear();
			daten = di.auslesenDatenMitWhereKlausel(tabelleName,
					spalteDatenbank, kennnung, kennnummer);
			for (int i = 0; i < daten.size(); i++) {
				hilfsvector = (Vector) daten.get(i);
				if (Integer.parseInt(hilfsvector.get(0).toString()) == 2)
					daten.remove(i);
			}
			tabelle = new Tabelle(spalte, daten, con, conL);
			scroll = tabelle.getTabelle();
		}

		hilfP.add(scroll, c);
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		hilfP.setBorder(loweredbevel);

		if (anfang) {
			scrollHilf = new JScrollPane(hilfP);
			add(scrollHilf, BorderLayout.CENTER);
		} else {
			remove(scrollHilf);
			scrollHilf = new JScrollPane(hilfP);
			add(scrollHilf, BorderLayout.CENTER);
		}

	}// end erstelleLayouAnders

	/**
	 * wird aufgerufen, wenn auf einen Button geklickt wurde
	 * 
	 * @param e
	 *            ein ActionEvent-Objekt
	 **/
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == budgetAnlegen) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					maske = new NewDatensatz(namenFuerButtons[kennnummer - 1],
							kennnummer, con, conL); // Eingabemaske
					// erzeugen
					maske.pack();
					maske.setLocationRelativeTo(null);
					maske.setVisible(true);
				}
			});

		}// end if budgetAnlegen

		if (e.getSource() == mehr) {
			anfang = false;
			if (tables[kennnummer - 5].welcheZeile()) // wenn man sich in einer
			// Zeile befindet
			{
				zurueck.setEnabled(true);

				// wenn es keine Tabellen mehr gibt
				if (wieOft > 8) {
					JOptionPane.showMessageDialog(this,
							"Keine weiteren Untertabellen");
					mehr.setEnabled(false);

				} else {
					hilfP.setLayout(new GridBagLayout());

					whereK = tables[kennnummer - 5].getZeilennummer();
					nummern.add(whereK);

					merke[kennnummer - 5] = whereK;
					kennnummer = wieOft;

					budgetAnlegen.setText(namenFuerButtons[kennnummer - 1]);

					tabelleName = tabellennamen[kennnummer - 1];
					tabelleName2 = tabellennamen[kennnummer - 2];

					if (kennnummer == 6)/* Bereich */{

						setzeLayoutMehr(2, c);
					}

					if (kennnummer == 7)/* Hauptkostenstelle */{

						setzeLayoutMehr(3, c);
					}

					if (kennnummer == 8)/* Kostenstelle */{

						setzeLayoutMehr(4, c);
					}

					// wenn sich keine Daten in der Datenbank befinden
					if (daten.size() == 0) {
						JOptionPane.showMessageDialog(this,
								"Keine weiteren Daten in den Untertabellen");
						mehr.setEnabled(false);
					}
					wieOft++;
				}

			}// end if

		}// end if(e.get....

		if (e.getSource() == bearbeiten) {
			if (kennnummer < 5 || kennnummer == 9) {
				if (tabelle.welcheZeile()) {
					zeilennummer = tabelle.getZeilennummer();
					markiert = true;

				}
			}
			if (kennnummer > 4 && kennnummer < 9) {
				if (tables[kennnummer - 5].welcheZeile()) {
					zeilennummer = tables[kennnummer - 5].getZeilennummer();
					markiert = true;
				}
			}

			if (markiert) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						maske2 = new EditBudget("geplantes Budget ändern",
								kennnummer, zeilennummer, con, conL, nummern);
						maske2.pack();
						maske2.setLocationRelativeTo(null);
						maske2.setVisible(true);
					}

				});
			}

			markiert = false;

		}// end if bearbeiten

		if (e.getSource() == details) {

			if (tabelle.welcheZeile()) {
				projektname = tabelle.getDaten(1);

				daten.clear();
				daten = di.auslesenDetails(projektname);
				if (daten.size() != 0) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							fenster = new AnzAbfrage("Details zum Projekt",
									spaltenDetailsProjekt, daten, con, conL);
							fenster.setSize(900, 500);
							fenster.setLocationRelativeTo(null);
							fenster.setVisible(true);
						}
					});

				} else
					JOptionPane.showMessageDialog(this,
							"Es wurden keine Details gefunden.", "Information",
							1);

			}

		}
		if(e.getSource() == buchungen)
		{
			if (tabelle.welcheZeile()) {
				projektname = tabelle.getDaten(1);

				daten.clear();
				daten = di.auslesenBuchungen(projektname);
				
				if (daten.size() != 0) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							fenster = new AnzAbfrage("Aushändigungen Zentrallager",
									spaltenBuchungenProjekt, daten, con, conL);
							fenster.setSize(1100, 500);
							fenster.setLocationRelativeTo(null);
							fenster.setVisible(true);
						}
					});

				} else
					JOptionPane.showMessageDialog(this,
							"Es wurden keine Details gefunden.", "Information",
							1);

			}
		}
		if (e.getSource() == zurueck) {

			if (kennnummer != 6) // wenn es nicht von Bereich in Hauptbereich
			// ist
			{
				whereK = Integer.parseInt(nummern.get(nummern.size() - 2)
						.toString());
			}

			nummern.removeElementAt(nummern.size() - 1);
			kennnummer = kennnummer - 1;

			budgetAnlegen.setText(namenFuerButtons[kennnummer - 1]);

			tabelleName = tabellennamen[kennnummer - 1];
			spalte = new String[spaltennamen[kennnummer - 1].length];

			for (int i = 0; i < spaltennamen[kennnummer - 1].length; i++) {
				spalte[i] = spaltennamen[kennnummer - 1][i];
			}

			wieOft = wieOft - 1;

			if (kennnummer > 5)/* von KST in HKST und von HKST in Bereich */{

				if (kennnummer == 7) {
					hilfP.remove(13);
					hilfP.remove(12);
					hilfP.remove(11);
					hilfP.remove(10);

					// Tabelle Hauptkostenstellen
					c.fill = GridBagConstraints.HORIZONTAL;
					c.ipady = 30;
					c.ipadx = 600;
					c.weightx = 0.0;
					c.gridwidth = GridBagConstraints.REMAINDER;
					c.gridx = 0;
					c.gridy = 10;

					daten.clear();
					int hnr = Integer.parseInt(nummern.get(nummern.size() - 1)
							.toString());
					daten = di.auslesenDatenMitWhereKlausel(tabelleName,
							"Hauptnummer", hnr, kennnummer);
					for (int i = 0; i < spaltennamen[kennnummer - 1].length; i++) {
						spalte[i] = spaltennamen[kennnummer - 1][i];
					}
					tables[kennnummer - 5] = new Tabelle(spalte, daten, con,
							conL);
					scroll = tables[kennnummer - 5].getTabelle();

					hilfP.add(scroll, c);
				} else if (kennnummer == 6) {
					hilfP.remove(10);
					hilfP.remove(9);
					hilfP.remove(8);
					hilfP.remove(7);

					// Tabelle Bereich
					c.fill = GridBagConstraints.HORIZONTAL;
					c.ipady = 30;
					c.ipadx = 600;
					c.weightx = 0.0;
					c.gridwidth = GridBagConstraints.REMAINDER;
					c.gridx = 0;
					c.gridy = 7;

					daten.clear();
					int hnr = Integer.parseInt(nummern.get(nummern.size() - 1)
							.toString());
					daten = di.auslesenDatenMitWhereKlausel(tabelleName,
							"Hauptnummer", hnr, kennnummer);

					for (int i = 0; i < spaltennamen[kennnummer - 1].length; i++) {
						spalte[i] = spaltennamen[kennnummer - 1][i];
					}

					tables[kennnummer - 5] = new Tabelle(spalte, daten, con,
							conL);
					JScrollPane scroll2 = new JScrollPane(
							tables[kennnummer - 5].getTabelle());

					hilfP.add(scroll2, c);

				}

				validate();
				repaint();

			} else if (kennnummer == 5) /* von Bereich zu Hauptbereich */{

				budgetAnlegen.setText(namenFuerButtons[kennnummer - 1]);

				hilfP.remove(7);
				hilfP.remove(6);
				hilfP.remove(5);
				hilfP.remove(4);

				c.fill = GridBagConstraints.HORIZONTAL;
				c.ipady = 30;
				c.ipadx = 600;
				c.weightx = 0.0;
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.gridx = 0;
				c.gridy = 6;

				daten.clear();
				tabelleName = tabellennamen[kennnummer - 1];
				spalte = new String[spaltennamen[kennnummer - 1].length];

				for (int i = 0; i < spaltennamen[kennnummer - 1].length; i++) {
					spalte[i] = spaltennamen[kennnummer - 1][i];
				}
				daten = di.auslesenDaten(tabelleName, kennnummer);

				for (int i = 0; i < daten.size(); i++) {
					hilfsvector = (Vector) daten.get(i);
					if (hilfsvector.get(0).toString().equals("0."))
						daten.remove(i);
				}

				tables[kennnummer - 5] = new Tabelle(spalte, daten, con, conL);
				scroll = tables[kennnummer - 5].getTabelle();
				hilfP.add(scroll, c);

				zurueck.setEnabled(false);
			}
			mehr.setEnabled(true);

			validate();
			repaint();
		}

		if (e.getSource() == aktualisieren) {
			auslesenDatenFuerAktualisiere();

		}

		if (e.getSource() == bearbeitenHB) {
			if ((kennnummer == 5) && (tabelleObenUt8.welcheZeile())) {
				zeilennummer = tabelleObenUt8.getZeilennummer();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						maske2 = new EditBudget("geplantes Budget ändern",
								kennnummer, zeilennummer, con, conL, nummern);
						maske2.pack();

						maske2.setLocationRelativeTo(null);
						maske2.setVisible(true);
					}

				});

			} else if (tabelleOben.welcheZeile()) {

				zeilennummer = tabelleOben.getZeilennummer();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						maske2 = new EditBudget("geplantes Budget ändern",
								kennnummer, zeilennummer, con, conL, nummern);
						maske2.pack();

						maske2.setLocationRelativeTo(null);
						maske2.setVisible(true);
					}

				});

			}
		}

		if (e.getSource() == loeschen) {
			String name = "";
			String nummerSelbst = "";
			String kurz = "";
			String raum = "";
			int hauptnr = 0;
			String nameTabelle = tabellennamen[kennnummer - 1];
			Vector proj = new Vector();
			int kennung = 0;

			int hilf = JOptionPane.showConfirmDialog(this,
					"Sind Sie sicher, dass sie den Datensatz löschen möchten?",
					"WARNUNG", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (hilf == 0) {

				if (kennnummer < 5 || kennnummer == 9)// UT3,Projekt,LMB,Sonderbudget
				{
					if (tabelle.welcheZeile()) {
						zeilennummer = tabelle.getZeilennummer();
						if (kennnummer == 1)// UT3
						{
							name = di.getEintragZuNummer(zeilennummer, 1, 0);
							txt.loescheDatensatzAusTextdatei(name,
									"abteilungenut3");

						} else if (kennnummer == 2)// Projekt
						{
							String[] projNamen = { "projektName","projektAbteilung",
									"projektDatum", "projektKlasse",
									"projektkurzz", "projektLehrer",
									"projektNummer",
									"projektTeilnehmer" };

							proj = di.getProjektDaten(zeilennummer);

							txt.loescheDatensatzAusTextdatei(proj.get(0), projNamen[0]);
							for (int i = 1; i < projNamen.length; i++) {
								txt.loescheDatensatzAnBestimmterStelle(proj.get(i),
										projNamen[i]);
							}
						} else if (kennnummer == 3 || kennnummer == 9)// LMB1 +
						// LMB2
						{
							name = di.getEintragZuNummer(zeilennummer,
									kennnummer, 0);
							kennung = di.getKennungLmb(zeilennummer);
							txt.loescheDatensatzAusTextdatei(name,
									"abteilungenlmb");
							txt.loescheDatensatzAnBestimmterStelle(kennung,
											"kennung");
						}
						else if(kennnummer==4)//sonderbudget
						{
							name = di.getEintragZuNummer(zeilennummer, kennnummer, 0);
							txt.loescheDatensatzAusTextdatei(name, "sonderbudget");
						}

						du.loescheDatensatz(zeilennummer, nameTabelle);
					}// end if

					// ut8
				} else {
					if (tables[kennnummer - 5].welcheZeile()) {

						zeilennummer = tables[kennnummer - 5].getZeilennummer();
						name = di.getEintragZuNummer(zeilennummer, kennnummer,
								0);
						nummerSelbst = di.getEintragZuNummer(zeilennummer,
								kennnummer, 3);

								
						if (kennnummer != 5)// bereich oder HKSt oder KSt
						{
							hauptnr = di.getYZurZummer(zeilennummer,
									nameTabelle, "hauptnummer");

							if (kennnummer == 6)// Bereich
							{
								txt.loescheDatensatzAusTextdatei(name,
										nameTabelle);
								txt.loescheDatensatzAnBestimmterStelle(hauptnr,
										"hauptnummerUt8ber");
								txt.loescheDatensatzAnBestimmterStelle(nummerSelbst,
										"nummerSelbstUt8ber");
							} else if (kennnummer == 7)// HKST
							{
								txt.loescheDatensatzAusTextdatei(name,
										nameTabelle);
								txt.loescheDatensatzAnBestimmterStelle(hauptnr,
										"hauptnummerUt8hkst");
								txt.loescheDatensatzAnBestimmterStelle(nummerSelbst,
										"nummerSelbstUt8hkst");
							} else if (kennnummer == 8)// Kst
							{
								kurz = di.getEintragZuNummer(zeilennummer,
										kennnummer, 2);
								raum = di.getEintragZuNummer(zeilennummer,
										kennnummer, 1);


								txt.loescheDatensatzAusTextdatei(name,
										"kostenstellenut8");
								txt.loescheDatensatzAnBestimmterStelle(hauptnr,
										"hauptnummerUt8kst");
								txt.loescheDatensatzAnBestimmterStelle(kurz,
										"kurzbezUt8");
								txt.loescheDatensatzAnBestimmterStelle(raum,
										"raumnummerUt8");
								txt.loescheDatensatzAnBestimmterStelle(nummerSelbst,
										"nummerSelbstUt8kst");
							}

						} else if (kennnummer == 5)// Hbereich
						{
							txt.loescheDatensatzAusTextdatei(name, nameTabelle);
							txt.loescheDatensatzAnBestimmterStelle(nummerSelbst,
									"nummerSelbstUt8hber");
						}

						du.loescheDatensatz(zeilennummer, nameTabelle);
					}
				}// end else
			}// end if(hilf==0)
		}

		if (e.getSource() == aendern) {

			if (kennnummer < 5 || kennnummer == 9) {
				if (tabelle.welcheZeile()) {
					zeilennummer = tabelle.getZeilennummer();
					markiert = true;
				}
			}

			else {
				if (tables[kennnummer - 5].welcheZeile()) {

					zeilennummer = tables[kennnummer - 5].getZeilennummer();
					markiert = true;

				}
			}

			if (markiert) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						ds = new EditDatensatz(con, conL, zeilennummer,
								kennnummer); // Eingabemaske
						// erzeugen
						ds.pack();
						ds.setLocationRelativeTo(null);
						ds.setVisible(true);
					}

				});
			}
			markiert = false;
		}

		if (e.getSource() == ltanteil) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					la = new BezLTAnteil(kennnummer, con, conL);// Eingabemaske
					// erzeugen
					la.pack();
					la.setLocationRelativeTo(null);
					la.setVisible(true);
				}

			});

		}
		if (e.getSource() == anteil) {

			if (tabelle.welcheZeile()) {
				projektname = tabelle.getDaten(1);
				daten.clear();
				daten = di.auslesenAnteile(projektname);

				if (daten.size() != 0) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							fenster = new AnzAbfrage("Projektanteile",
									spaltenAnteil, daten, con, conL); // Eingabemaske
							// erzeugen
							fenster.pack();
							fenster.setLocationRelativeTo(null);
							fenster.setVisible(true);
						}

					});

				} else
					JOptionPane.showMessageDialog(this,
							"Es wurden keine Informationen gefunden.");
			}

		}
		if (e.getSource() == edvHat) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					edvHatAnteil = new BezEDVHAT(con, conL); // Eingabemaske
					// erzeugen
					edvHatAnteil.pack();
					edvHatAnteil.setLocationRelativeTo(null);
					edvHatAnteil.setVisible(true);
				}

			});

		}

	}// end actionPerformed

	/**
	 * aktualisiert die Tabelle, nachdem Aenderungen durchgefuehrt wurden
	 * 
	 * @param table
	 *            Tabelle, die aktualisiert werden soll
	 * 
	 * @param data
	 *            neuen Daten für die Tabelle
	 */

	public void aktualisiere(Tabelle table, Vector data) {

		table.setzeDaten(data);
		table.aufrufen();

		if (kennnummer == 1) {
			werte = di.getEDVHATAnteil();

			hat.setText("HAT- Anteil: " + ("" + werte[1]) + "%");

			edv.setText("EDV- Anteil: " + ("" + werte[0]) + "%");
			repaint();
		}

	}

	/**
	 * liesst die Daten fuer das Aktualisieren aus
	 */
	public void auslesenDatenFuerAktualisiere() {
		daten.clear();


		if (kennnummer == 1) {
			at.berechneAnteileUT3();

			daten = di.auslesenDaten(tabellennamen[kennnummer - 1], kennnummer);

			hilfsvector = new Vector();
			for (int i = 0; i < daten.size(); i++) {
				hilfsvector = (Vector) daten.get(i);
				if (Integer.parseInt(hilfsvector.get(0).toString()) == 1) {
					daten.remove(i);
				}
			}

			aktualisiere(tabelle, daten);

			satz = di.auslesenDatenMitWhereKlausel(
					tabellennamen[kennnummer - 1], spalteDatenbank, 1,
					kennnummer);

			aktualisiere(tabelleOben, satz);

		}
		if (kennnummer == 5) {
			satz = new Vector();
			daten = di.auslesenDaten(tabellennamen[kennnummer - 1], kennnummer);
			hilfsvector = new Vector();

			for (int i = 0; i < daten.size(); i++) {
				hilfsvector = (Vector) daten.get(i);
				if (hilfsvector.get(0).toString().equals("0.")) {
					satz = hilfsvector;
					daten.remove(i);

				}
			}

			if (satz.size() != 0) {
				hilfsvector = new Vector();
				hilfsvector.add(satz);
				aktualisiere(tabelleObenUt8, hilfsvector);
			} else
				aktualisiere(tabelleObenUt8, satz);

			aktualisiere(tables[kennnummer - 5], daten);
		}
		if (kennnummer == 2 || kennnummer == 4) {
			daten = di.auslesenDaten(tabellennamen[kennnummer - 1], kennnummer);

			aktualisiere(tabelle, daten);
		}

		if (kennnummer > 5 && kennnummer < 9) {

			daten = di.auslesenDatenMitWhereKlausel(
					tabellennamen[kennnummer - 1], "hauptnummer", whereK,
					kennnummer);

			aktualisiere(tables[kennnummer - 5], daten);
		}

		if (kennnummer == 3) {

			at.berechneAnteileLMB(kennnummer);
			daten = di.auslesenDatenMitWhereKlausel(tabelleName, "kennung", 1,
					kennnummer);
			hilfsvector = new Vector();
			satz = new Vector();
			for (int i = 0; i < daten.size(); i++) {
				hilfsvector = (Vector) daten.get(i);
				if (Integer.parseInt(hilfsvector.get(0).toString()) == 1) {

					daten.remove(i);
				}
			}

			aktualisiere(tabelle, daten);

			satz = di.auslesenDatenMitWhereKlausel(tabelleName, "Nummer", 1,
					kennnummer);

			aktualisiere(tabelleOben, satz);

		}

		if (kennnummer == 9) {
			at.berechneAnteileLMB(kennnummer);
			daten = di.auslesenDatenMitWhereKlausel(tabelleName, "kennung", 2,
					kennnummer);
			hilfsvector = new Vector();
			satz = new Vector();
			for (int i = 0; i < daten.size(); i++) {
				hilfsvector = (Vector) daten.get(i);
				if (Integer.parseInt(hilfsvector.get(0).toString()) == 2) {
					daten.remove(i);
				}
			}
			aktualisiere(tabelle, daten);

			satz = di.auslesenDatenMitWhereKlausel(tabelleName, "Nummer", 2,
					kennnummer);

			aktualisiere(tabelleOben, satz);

		}

	}

	/**
	 * setzt das Layout fuer UT8, wenn man auf den Button "mehr" geklickt hat
	 * 
	 * @param stufe
	 *            in welcher Stufe des UT8 befindet man sich; 2: Bereich, 3:
	 *            Hauptkostenstelle, 4: Kostenstelle
	 */
	public void setzeLayoutMehr(int stufe, GridBagConstraints c) {

		hilfP.removeAll();

		// oberes Label
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.ipady = 10;
		c.gridx = 0;
		c.gridy = 0;

		hilfP.add(new JLabel("Hauptbudget UT8"), c);

		// obere Tabelle
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 1;

		hilfP.add(tabelleObenUt8.getHeader(), c);

		c.gridy = 2;
		hilfP.add(tabelleObenUt8.getNurTabelle(), c);

		if (stufe == 2)// Bereich
		{
			// Label Hauptbereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipady = 0;
			c.ipadx = 0;
			c.gridx = 0;
			c.gridy = 3;

			hilfP.add(new JLabel("Hauptbereich"), c);

			// Tabelle HauptBereich
			c.fill = GridBagConstraints.BOTH;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipadx = 0;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 4;

			merke[0] = whereK;

			daten.clear();
			daten = di.auslesenDatenMitWhereKlausel(tabelleName2, "Nummer",
					whereK, kennnummer - 1);

			tables[0] = new Tabelle(spalte, daten, con, conL);
			hilfP.add(tables[0].getHeader(), c);

			c.gridy = 5;
			hilfP.add(tables[0].getNurTabelle(), c);

			// Label Bereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 6;

			hilfP.add(new JLabel("Bereiche"), c);

			// Tabelle Bereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.ipady = 30;
			c.ipadx = 600;
			c.weightx = 0.0;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.gridx = 0;
			c.gridy = 7;

			daten.clear();
			daten = di.auslesenDatenMitWhereKlausel(tabelleName, "Hauptnummer",
					whereK, kennnummer);

			for (int i = 0; i < spaltennamen[kennnummer - 1].length; i++) {
				spalte[i] = spaltennamen[kennnummer - 1][i];
			}

			tables[kennnummer - 5] = new Tabelle(spalte, daten, con, conL);
			JScrollPane scroll2 = new JScrollPane(tables[kennnummer - 5]
					.getTabelle());

			hilfP.add(scroll2, c);
			validate();
			repaint();
		}
		if (stufe == 3)// Hauptkostenstelle
		{
			// Label Hauptbereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipady = 0;
			c.ipadx = 0;
			c.gridx = 0;
			c.gridy = 3;

			hilfP.add(new JLabel("Hauptbereich"), c);

			// Tabelle HauptBereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipadx = 0;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 4;

			merke[0] = whereK;
			hilfP.add(tables[0].getHeader(), c);

			c.gridy = 5;
			hilfP.add(tables[0].getNurTabelle(), c);

			// Label Bereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipadx = 0;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 6;

			hilfP.add(new JLabel("Bereiche"), c);

			// Tabelle Bereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipadx = 0;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 7;

			daten.clear();
			daten = di.auslesenDatenMitWhereKlausel(tabelleName2, "Nummer",
					whereK, kennnummer - 1);
			tables[1] = new Tabelle(spalte, daten, con, conL);
			hilfP.add(tables[1].getHeader(), c);

			c.gridy = 8;
			hilfP.add(tables[1].getNurTabelle(), c);

			// Label Hauptkostenstellen
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipadx = 0;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 9;

			hilfP.add(new JLabel("Hauptkostenstellen"), c);

			// Tabelle Hauptkostenstellen
			c.fill = GridBagConstraints.HORIZONTAL;
			c.ipady = 30;
			c.ipadx = 600;
			// c.weightx = 0.0;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.gridx = 0;
			c.gridy = 10;

			daten.clear();
			daten = di.auslesenDatenMitWhereKlausel(tabelleName, "Hauptnummer",
					whereK, kennnummer);
			for (int i = 0; i < spaltennamen[kennnummer - 1].length; i++) {
				spalte[i] = spaltennamen[kennnummer - 1][i];
			}
			tables[kennnummer - 5] = new Tabelle(spalte, daten, con, conL);
			scroll = tables[kennnummer - 5].getTabelle();

			hilfP.add(scroll, c);
			validate();
			repaint();
		}
		if (stufe == 4)// Kostenstelle
		{
			// Label Hauptbereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 3;

			hilfP.add(new JLabel("Hauptbereich"), c);

			// Tabelle HauptBereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipadx = 0;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 4;

			merke[0] = whereK;
			hilfP.add(tables[0].getHeader(), c);

			c.gridy = 5;
			hilfP.add(tables[0].getNurTabelle(), c);

			// Label Bereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 6;

			hilfP.add(new JLabel("Bereiche"), c);

			// Tabelle Bereich
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipadx = 0;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 7;

			hilfP.add(tables[1].getHeader(), c);

			c.gridy = 8;
			hilfP.add(tables[1].getNurTabelle(), c);

			// Label Hauptkostenstelle
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 9;

			hilfP.add(new JLabel("Hauptkostenstellen"), c);

			// Tabelle Hauptkostenstelle
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipadx = 0;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 10;

			daten.clear();
			daten = di.auslesenDatenMitWhereKlausel(tabelleName2, "Nummer",
					whereK, kennnummer - 1);
			tables[2] = new Tabelle(spalte, daten, con, conL);
			hilfP.add(tables[2].getHeader(), c);

			c.gridy = 11;
			hilfP.add(tables[2].getNurTabelle(), c);

			// Label Kostenstelle
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.ipady = 0;
			c.gridx = 0;
			c.gridy = 12;

			hilfP.add(new JLabel("Kostenstellen"), c);

			// Tabelle Kostenstelle
			c.fill = GridBagConstraints.HORIZONTAL;
			c.ipady = 30;
			c.ipadx = 600;
			c.weightx = 0.0;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.gridx = 0;
			c.gridy = 13;

			daten.clear();
			daten = di.auslesenDatenMitWhereKlausel(tabelleName, "Hauptnummer",
					whereK, kennnummer);
			spalte = new String[spaltennamen[kennnummer - 1].length];
			for (int i = 0; i < spaltennamen[kennnummer - 1].length; i++) {
				spalte[i] = spaltennamen[kennnummer - 1][i];
			}
			tables[kennnummer - 5] = new Tabelle(spalte, daten, con, conL);
			scroll = tables[kennnummer - 5].getTabelle();
			hilfP.add(scroll, c);
			validate();
			repaint();
		}
	}

}// end Klasse Vorlage

