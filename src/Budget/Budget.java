package Budget;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.*;
import java.beans.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *In der Klasse Budget wird das Menue erstellt und die Karten erstellt, die im
 * mittleren Panel angezeigt werden
 *<p>
 * Title: Budget
 * 
 *@author Haupt, Liebhart
 **/
public class Budget extends JFrame implements ActionListener {

	// --------------------------------Connection--------------------
	/** Connection zur Budget-Datenbank */
	private Connection con;
	/** Connection zur Logistik-Datenbank */
	private Connection conL;
	/** Connection zur alten Budget- Datenbank **/
	private Connection conB_alt;

	// --------------------------------Panels-----------------------
	/** beinhaltet die Leiste mit den Buttons und die Namensleiste */
	private JPanel pTop;
	/**
	 * Leiste mit den Buttons UT8, UT3, LMB, Sonderbudget, Projekte und
	 * Bezahlung
	 */
	private JPanel pControls;
	/** beinhaltet die Karten für das CardLayout */
	private JPanel pCenter;

	// --------------------------------Buttons-----------------------
	// Die Buttons, die immer in der oberen Leiste zu sehen sind
	/** Der Button "UT8" in der oberen Leiste **/
	private JButton btnUt8;
	/** Der Button "UT3" in der oberen Leiste **/
	private JButton btnUt3;
	/** Der Button "LMB1" in der oberen Leiste **/
	private JButton btnLmb;
	/** Der Button "LMB1_2011" in der oberen Leiste **/
	private JButton btnLmb2;
	/** Der Button "Projekte" in der oberen Leiste **/
	private JButton btnProjekte;
	/** Der Button "Sonderbudgets" in der oberen Leiste **/
	private JButton btnSonder;
	/** Der Button "Bezahlung" in der oberen Leiste **/
	private JButton btnBezahlung;

	// -----------------------------------Menue-----------------------
	/** Das Menue, das ganz oben angezeigt wird **/
	private JMenuBar menuBar;

	// Menueeintraege mit Untermenue
	/** Der Menueeintrag Datei **/
	private JMenu datei;
	/** Der Menueeintrag Hilfe **/
	private JMenu hilfe;
	/** Der Menueeintrag Rechnung **/
	private JMenu rechnung;
	/** Der Menueeintrag Datenbank **/
	private JMenu datenbank;
	/** Der Menueeintrag Abfragen **/
	private JMenu abfragen;
	/** Der Menueeintrag Neues Budget **/
	private JMenu neuesBudget;
	/** Der Menueeintrag Abfragen **/
	private JMenu abfrage1;
	/** Der Menueeintrag UT3 bei der Abfrage Aktueller Stand **/
	private JMenu mIUt3;
	/** Der Menueeintrag LMB1 bei der Abfrage Aktueller Stand **/
	private JMenu mILmb1;
	/** Der Menueeintrag LMB2 bei der Abfrage Aktueller Stand **/
	private JMenu mILmb2;
	/** Der Menueeintrag Uebersicht Bestellungen **/
	private JMenu abfrage2;
	/**
	 * Der Menueeintrag Banfs nach Haubtbereich, Bereich, Hauptkostenstelle,
	 * Kostenstelle
	 **/
	private JMenu banfsNachBereichen;

	// auswaehlbare Menueeintraege
	/** Der auswaehlbare Menueeintrag Bezahlung **/
	private JMenuItem bezahlung;
	/** Der auswaehlbare Menueeintrag Beenden **/
	private JMenuItem beenden;
	/** Der auswaehlbare Menueeintrag Rechnung suchen **/
	private JMenuItem rechnungSuchen;
	/** Der auswaehlbare Menueeintrag Datenbank befuellen **/
	private JMenuItem datenbankBefuellen;
	/** Der auswaehlbare Menueeintrag Null setzen **/
	private JMenuItem nullSetzen;
	/** Der auswaehlbare Menueeintrag Jahresende **/
	private JMenuItem jahresende;
	/** Der auswaehlbare Menueeintrag Daten in Excel exportieren **/
	private JMenuItem exExportieren;
	/** Der auswaehlbare Menueeintrag Datenbank exportieren **/
	private JMenuItem dbExportieren;
	/** Der auswaehlbare Menueeintrag Information **/
	private JMenuItem info;
	/** Der auswaehlbare Menueeintrag UT8 in der Abfrage Aktueller Status **/
	private JMenuItem mIUt8;
	/** Der auswaehlbare Menueeintrag UT3 nach Abteilung **/
	private JMenuItem ut3NachAbteilungen;
	/** Der auswaehlbare Menueeintrag UT3 nach Status **/
	private JMenuItem ut3NachStatus;
	/** Der auswaehlbare Menueeintrag LMB1 nach Abteilung **/
	private JMenuItem lmb1NachAbteilungen;
	/** Der auswaehlbare Menueeintrag LMB1 nach Status **/
	private JMenuItem lmb1NachStatus;
	/** Der auswaehlbare Menueeintrag LMB2 nach Abteilung **/
	private JMenuItem lmb2NachAbteilungen;
	/** Der auswaehlbare Menueeintrag LMB2 nach Status **/
	private JMenuItem lmb2NachStatus;
	/**
	 * Der auswaehlbare Menueeintrag Sonderbudget in der Abfrage Aktueller
	 * Status
	 **/
	private JMenuItem mISonder;
	/** Der auswaehlbare Menueeintrag UT8 in der Abfrage Uebersicht Bestellungen **/
	private JMenuItem uebersichtUt8;
	/** Der auswaehlbare Menueeintrag UT3 in der Abfrage Uebersicht Bestellungen **/
	private JMenuItem uebersichtUt3;
	/**
	 * Der auswaehlbare Menueeintrag LMB1 in der Abfrage Uebersicht Bestellungen
	 **/
	private JMenuItem uebersichtLmb1;
	/**
	 * Der auswaehlbare Menueeintrag LMB2 in der Abfrage Uebersicht Bestellungen
	 **/
	private JMenuItem uebersichtLmb2;
	/**
	 * Der auswaehlbare Menueeintrag alle Sonderbudgets in der Abfrage
	 * Uebersicht Bestellungen
	 **/
	private JMenuItem uebersichtSonderAlle;
	/**
	 * Der auswaehlbare Menueeintrag Sonderbudget in der Abfrage Uebersicht
	 * Bestellungen
	 **/
	private JMenuItem uebersichtSonder;
	/** Der auswaehlbare Menueeintrag Banf Auflistung nach Lehrer **/
	private JMenuItem banfAuflistung;
	/** Der auswaehlbare Menueeintrag Banf Auflistung nach Hauptbereich **/
	private JMenuItem banfHauptbereich;
	/** Der auswaehlbare Menueeintrag Banf Auflistung nach Bereich **/
	private JMenuItem banfBereich;
	/** Der auswaehlbare Menueeintrag Banf Auflistung nach Hauptkostenstelle **/
	private JMenuItem banfHauptkst;
	/** Der auswaehlbare Menueeintrag Banf Auflistung nach Kostenstelle **/
	private JMenuItem banfKst;
	/** Der auswaehlbare Menueeintrag Auswertung nach Abteilung - betragsmaeßig **/
	private JMenuItem auswertungAbtBetrag;
	/**
	 * Der auswaehlbare Menueeintrag Auswertung nach Abteilung -
	 * verhaeltnismaeßig
	 **/
	private JMenuItem auswertungAbtVerh;
	/** Der auswaehlbare Menueeintrag Umsatz Je Firma **/
	private JMenuItem umsatzFirma;
	/** Der auswaehlbare Menueeintrag gekaufte Artikel Je Firma **/
	private JMenuItem gekaufteArtikel;
	/** Der auswaehlbare Menueeintrag ABC-Analyse nach Firmen **/
	private JMenuItem abcFirmen;
	/** Der auswaehlbare Menueeintrag ABC-Analyse nach Artikel **/
	private JMenuItem abcArtikel;
	/** Der auswaehlbare Menueeintrag neuen Hauptbereich anlegen **/
	private JMenuItem hauptbereich;
	/** Der auswaehlbare Menueeintrag neuen Bereich anlegen **/
	private JMenuItem bereich;
	/** Der auswaehlbare Menueeintrag neue Hauptkostenstelle anlegen **/
	private JMenuItem hauptkostenstelle;
	/** Der auswaehlbare Menueeintrag neue Kostenstelle anlegen **/
	private JMenuItem kostenstelle;
	/** Der auswaehlbare Menueeintrag neue Abteilung in UT3 anlegen **/
	private JMenuItem ut3;
	/** Der auswaehlbare Menueeintrag neue Abteilung in LMB anlegen **/
	private JMenuItem lmb;
	/** Der auswaehlbare Menueeintrag neues Sonderbudget anlegen **/
	private JMenuItem sonder;
	/** Der auswaehlbare Menueeintrag neues Projekt anlegen **/
	private JMenuItem projekt;
	/** Der auswaehlbare Menueeintrag rechnungen **/
	private JMenuItem rechnungen;
	/** Der auswaehlbare Menueeintrag rechnungenLoeschen **/
	private JMenuItem rechnungenLoeschen;
	/** Der auswaehlbare Menueeintrag rechnungenLoeschen **/
	private JMenuItem buchungenLoeschen;
	private JMenuItem rechnungAendern;
	private JMenuItem rechnungenHeurige;

	// --------------------------Objekte unserer Klassen--------------------
	/** zeigt das Budget UT8 an **/
	private Vorlage ut8V;
	/** zeigt das Budget UT3 an **/
	private Vorlage ut3V;
	/** zeigt das Budget LMB1 an **/
	private Vorlage lmbV1;
	/** zeigt das Budget LMB2 an **/
	private Vorlage lmbV2;
	/** zeigt die Sonderbudgets an **/
	private Vorlage sonderbudgetV;
	/** zeigt die Projekte an **/
	private Vorlage projektV;
	/** wird benoetigt, um die Daten aus der Datenbank zu loeschen **/
	private NullSetzen nullsetzen;
	/** Diagramm fuer den Status von UT3 **/
	private Diagramm diagramm;
	/** Diagramm fuer UT3 nach Abteilungen **/
	private Diagramm diagramm2;
	/** Diagramm fuer den Status von LMB1 **/
	private Diagramm diagramm3;
	/** Diagramm fuer LMB1 nach Abteilungen **/
	private Diagramm diagramm4;
	/** Diagramm fuer den Status von LMB2 **/
	private Diagramm diagramm5;
	/** Diagramm fuer LMB2 nach Abteilungen **/
	private Diagramm diagramm6;
	/** Diagramm fuer den Status der Sonderbudgets **/
	private Diagramm diagramm7;
	/** Diagramm fuer den Status von UT8 **/
	private Diagramm diagramm8;
	/** fuer die Abfrage: Uebersicht Bestellungen **/
	private BudgetAbfragen ab;
	/** fuer die Abfrage: Banf Auflistung nach Lehrern **/
	private BudgetAbfragen ab2;
	/** fuer die Abfrage: Auswertung nach Abteilung - betragsmaeßig **/
	private BudgetAbfragen ab3;
	/** fuer die Abfrage: Auswertung nach Abteilung - verhaeltnismaeßig **/
	private BudgetAbfragen ab4;
	/** fuer die Abfrage: Umsatz je Firma **/
	private BudgetAbfragen ab5;
	/** fuer die Abfrage: gekaufte Artikel je Firma **/
	private BudgetAbfragen ab6;
	/** fuer die Abfrage: Bestellungen nach Haubtbereich **/
	private BudgetAbfragen ab7;
	/** fuer die Abfrage: Bestellungen nach Bereich **/
	private BudgetAbfragen ab8;
	/** fuer die Abfrage: Bestellungen nach Hauptkostenstelle **/
	private BudgetAbfragen ab9;
	/** fuer die Abfrage: Bestellungen nach Kostenstelle **/
	private BudgetAbfragen ab10;
	/** fuer die Abfrage: Übersicht Bestellungen - Sonderbudgets **/
	private BudgetAbfragen ab11;
	/** fuer die Abfrage: ABC-Analyse nach Artikeln **/
	private BudgetAbfragen ab12;
	/** fuer die Abfrage: ABC-Analyse nach Firmen **/
	private BudgetAbfragen ab13;
	/** fuer die Abfrage: alle Rechnungen **/
	private BudgetAbfragen ab14;
	/** fuer die Abfrage: alle Rechnungen **/
	private BudgetAbfragen ab15;
	/** fuer das Befuellen der Datenbank mit den Standarddaten **/
	private DBBefuellung befuellung;
	/** fuer das Verwenden der Methoden in der Klasse DatenExport **/
	private AbfrageExportExcel de;
	/**
	 * fuer das Anzeigen der Namensleiste, die angibt, in welcher Aktion man
	 * sich befindet
	 **/
	private Namensleiste pNamensLeiste;
	/** fuer das Aufbauen der Verbindung zu den Datenbanken **/
	private DBVerbindung dv = new DBVerbindung();
	/** fuer das Anzeigen der Informationen unter dem Menuepunkt Info **/
	private Info information;
	/** fuer die Auswahl "Jahresende" **/
	private Jahresende jahrEnde;
	private RechnungLoeschen rL;
	private RechnungLoeschen bL;
	private DBExport dbX;

	// --------------------------sonstige--------------------
	/** false: man will mit altem Budget arbeiten; true: aktuelles Budget **/
	private boolean aktuell;
	/** wird verwendet, um das aktuelle Jahr zu ermitteln **/
	private Date datum;
	/** die aktuelle Jahreszahl **/
	private int jahr;
	/** Der Container */
	private Container c;
	/** ist ein CardLayout - Objekt */
	private CardLayout card;
	/** ob man den Menueeintrag Jahresende gewaehlt hat **/
	private boolean jahresendeB = false;
	private TextdateiImport txtImport;

	private JProgressBar progressBar;

	private Runnable runnable;

	/** Jahreszahl für die Bezeichnung des LMB1_x **/
	private int jahreszahl;

	/**
	 * Kostruktor
	 * 
	 * @param aktuell
	 *            true: man will mit den aktuellen Daten arbeiten; false: man
	 *            will mit den alten Daten arbeiten
	 * @param name
	 *            Titel des Frames
	 */
	public Budget(boolean aktuell, String name) {
		super(name);

		this.aktuell = aktuell;

		progressBar = new JProgressBar();
		runnable = new Runnable() {
			public void run() {
				for (int i = 0; i > 100; i++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
					}
					progressBar.setValue(i);
				}

			}

		};

		c = getContentPane();
		c.setLayout(new BorderLayout());
		setSize(
				(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 25);

		// Verbindungsaufbau zu den Datenbanken

		datum = new Date();
		jahr = datum.getYear() + 1900;

		conL = dv.verbindeDatenbankLogistik();

		if (!aktuell) {
			con = dv.verbindeDatenbankAlt();// budget3/1
			jahreszahl = datum.getYear() + 1899;

		} else {
			con = dv.verbindeDatenbankAktuell();// budget 4/2
			jahreszahl = datum.getYear() + 1900;

		}

		// Die Objekte werden definiert
		ab = new BudgetAbfragen(con, conL, aktuell);
		ab2 = new BudgetAbfragen(con, conL, aktuell);
		ab3 = new BudgetAbfragen(con, conL, aktuell);
		ab4 = new BudgetAbfragen(con, conL, aktuell);
		ab5 = new BudgetAbfragen(con, conL, aktuell);
		ab6 = new BudgetAbfragen(con, conL, aktuell);
		ab7 = new BudgetAbfragen(con, conL, aktuell);
		ab8 = new BudgetAbfragen(con, conL, aktuell);
		ab9 = new BudgetAbfragen(con, conL, aktuell);
		ab10 = new BudgetAbfragen(con, conL, aktuell);
		ab11 = new BudgetAbfragen(con, conL, aktuell);
		ab12 = new BudgetAbfragen(con, conL, aktuell);
		ab13 = new BudgetAbfragen(con, conL, aktuell);
		ab14 = new BudgetAbfragen(con, conL, aktuell);
		ab15 = new BudgetAbfragen(con, conL, aktuell);

		nullsetzen = new NullSetzen(con);

		diagramm = new Diagramm(con, conL);
		diagramm2 = new Diagramm(con, conL);
		diagramm3 = new Diagramm(con, conL);
		diagramm4 = new Diagramm(con, conL);
		diagramm5 = new Diagramm(con, conL);
		diagramm6 = new Diagramm(con, conL);
		diagramm7 = new Diagramm(con, conL);
		diagramm8 = new Diagramm(con, conL);

		ut8V = new Vorlage(5, con, conL, jahreszahl);
		ut3V = new Vorlage(1, con, conL, jahreszahl);
		projektV = new Vorlage(2, con, conL, jahreszahl);
		sonderbudgetV = new Vorlage(4, con, conL, jahreszahl);
		lmbV1 = new Vorlage(3, con, conL, jahreszahl);
		lmbV2 = new Vorlage(9, con, conL, jahreszahl);
		rL = new RechnungLoeschen(con, conL);
		bL = new RechnungLoeschen(con, conL);

		pNamensLeiste = new Namensleiste();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		// Menüleiste definieren
		menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createHelpMenu());
		menuBar.add(createRechnungMenu());
		menuBar.add(createDatenbankMenu());
		setJMenuBar(menuBar);

		// CardLayout definieren
		card = new CardLayout();
		pCenter = new JPanel(card);

		// definieren, welche Klasse bei welcher Karte aufgerufen werden soll
		pCenter.add("Card1", new Startseite());
		pCenter.add("Card2", ut8V);
		pCenter.add("Card3", ut3V);
		pCenter.add("Card4", lmbV1);
		pCenter.add("Card5", projektV);
		pCenter.add("Card6", sonderbudgetV);
		pCenter.add("Card7", new Bestellungen(con, conL)); // Bezahlung
		pCenter.add("Card8", new Rechnung(con, conL, 0));
		pCenter.add("Card10", nullsetzen);
		pCenter.add("Card12", diagramm8);
		pCenter.add("Card13", diagramm2);
		pCenter.add("Card14", diagramm);
		pCenter.add("Card15", diagramm3);
		pCenter.add("Card16", diagramm4);
		pCenter.add("Card17", diagramm5);
		pCenter.add("Card18", diagramm6);
		pCenter.add("Card19", diagramm7);
		pCenter.add("Card20", ab);// Übersicht Bestellungen
		pCenter.add("Card21", ab2);// Banf Auflistung nach Lehrern
		pCenter.add("Card23", ab3);// Auswertung nach Abteilung - betragsmäßig
		pCenter.add("Card24", ab4);// verhältnismäßig
		pCenter.add("Card25", ab5);// Umsatz je Firma
		pCenter.add("Card26", ab6);// gekaufte Artikel je Firma
		pCenter.add("Card22", ab7);// Bestellungen nach Haubtbereich
		pCenter.add("Card30", ab8);// Bestellungen nach Bereich
		pCenter.add("Card31", ab9);// Bestellungen nach Hauptkostenstelle
		pCenter.add("Card32", ab10);// Bestellungen nach Kostenstelle
		pCenter.add("Card34", ab11);// Uebersicht Sonderbudgets
		pCenter.add("Card28", ab12);// ABC-Analyse nach Artikeln
		pCenter.add("Card27", ab13);// ABC-Analyse nach Firmen
		pCenter.add("Card35", ab14);// alle Rechnungen
		pCenter.add("Card39", ab15);
		pCenter.add("Card33", lmbV2);
		pCenter.add("Card36", rL);
		pCenter.add("Card37", bL);
		pCenter.add("Card38", new Rechnung(con, conL, 1));

		// Buttons definieren
		btnUt8 = new JButton("UT8");
		btnUt3 = new JButton("UT3");
		btnLmb = new JButton("LMB1");
		btnProjekte = new JButton("Projekte");
		btnSonder = new JButton("Sonderbudget");
		btnBezahlung = new JButton("Bezahlung");
		btnLmb2 = new JButton("LMB2");

		// Button dem ActionListener hinzufügen
		btnUt8.addActionListener(this);
		btnUt3.addActionListener(this);
		btnLmb.addActionListener(this);
		btnProjekte.addActionListener(this);
		btnSonder.addActionListener(this);
		btnBezahlung.addActionListener(this);
		btnLmb2.addActionListener(this);

		// ToolTipText setzen
		btnUt8.setToolTipText("UT8");
		btnUt3.setToolTipText("UT3");
		btnLmb.setToolTipText("LMB 1");
		btnProjekte.setToolTipText("Projekte");
		btnSonder.setToolTipText("Sonderbudgets");
		btnBezahlung.setToolTipText("Bezahlung der Bestellungen");
		btnLmb2.setToolTipText("LMB2");

		// Buttons dem pControls - Panel hinzufügen
		pControls = new JPanel();
		pControls.setLayout(new FlowLayout());
		pControls.add(btnUt8);
		pControls.add(btnUt3);
		pControls.add(btnLmb);
		pControls.add(btnLmb2);
		pControls.add(btnProjekte);
		pControls.add(btnSonder);
		pControls.add(btnBezahlung);

		// Panels definieren
		pTop = new JPanel();
		pTop.setLayout(new GridLayout(2, 1));
		pTop.add(pControls);
		pTop.add(pNamensLeiste);

		// Alles dem Container hinzufügen
		c.add(pTop, BorderLayout.NORTH);
		c.add(pCenter, BorderLayout.CENTER);

	}

	/**
	 * Die Methode wird vom ActionListener vorgeschrieben und wird aufgerufen,
	 * wenn auf einen Button geklickt wurde oder ein Menueeintrag ausgewaehlt
	 * wurde
	 * 
	 * @param e
	 *            ein ActionEvent - Objekt
	 */
	public void actionPerformed(ActionEvent e) {

		// obere Buttonsleiste
		if (e.getSource() == btnUt8) {
			card.show(pCenter, "Card2");
			pNamensLeiste.aendereName("UT8");
		} else if (e.getSource() == btnUt3) {
			card.show(pCenter, "Card3");
			pNamensLeiste.aendereName("UT3");
		} else if (e.getSource() == btnLmb) {
			card.show(pCenter, "Card4");
			pNamensLeiste.aendereName("LMB 1");
		} else if (e.getSource() == btnProjekte) {
			card.show(pCenter, "Card5");
			pNamensLeiste.aendereName("Projekte");
		} else if (e.getSource() == btnSonder) {
			card.show(pCenter, "Card6");
			pNamensLeiste.aendereName("Sonderbudgets");
		} else if (e.getSource() == btnBezahlung) {
			card.show(pCenter, "Card7");
			pNamensLeiste.aendereName("Bezahlung");
		} else if (e.getSource() == btnLmb2) {
			card.show(pCenter, "Card33");
			pNamensLeiste.aendereName("LMB2");

		}

		// Menueeintraege

		if (e.getActionCommand().equals("Bezahlung")) {
			card.show(pCenter, "Card7");
			pNamensLeiste.aendereName("Bezahlung");
		} else if (e.getActionCommand().equals("Rechnung suchen")) {
			card.show(pCenter, "Card8");
			pNamensLeiste.aendereName("Rechnung suchen");
		} else if (e.getActionCommand().equals("Budget-Datenbank importieren")) {
			pNamensLeiste.aendereName("Budget-Datenbank importieren");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					txtImport = new TextdateiImport(con, 0, jahrEnde);
					txtImport.pack();
					txtImport.setLocationRelativeTo(null);
					txtImport.setVisible(true);
				}

			});
			// befuellung = new Datenbankbefuellung(con);

			// Menue - Abfragen
		} else if (e.getActionCommand().equals("UT8 nach Status")) {

			diagramm8.nachStatus("UT8 nach Status", "hauptbereichut8", 1,
					"UT8", aktuell);
			card.show(pCenter, "Card12");
			pNamensLeiste.aendereName("Aktueller Stand - UT8");

		} else if (e.getActionCommand().equals(
				"UT3 nach Abteilungen (ausgegeben)")) {
			diagramm2.nachAbteilung("UT3 nach Abteilungen",
					"select * from abteilungut3 where nummer>1", 1, aktuell,
					"UT3");
			card.show(pCenter, "Card13");
			pNamensLeiste
					.aendereName("Aktueller Stand - UT3 nach Abteilungen (ausgegebenes Budget)");

		} else if (e.getActionCommand().equals("UT3 nach Status")) {
			diagramm.nachStatus("UT3 nach Status",

			"abteilungut3", 1, "UT3", aktuell);

			card.show(pCenter, "Card14");
			pNamensLeiste.aendereName("Aktueller Stand - UT3 nach Status");

		} else if (e.getActionCommand().equals(
				"LMB1 nach Abteilungen(ausgegeben)")) {
			diagramm3.nachAbteilung("LMB1 nach Abteilungen",

			"select * from lmb where nummer>2 and kennung=1", 2, aktuell,
					"LMB1");
			card.show(pCenter, "Card15");
			pNamensLeiste
					.aendereName("Aktueller Stand - LMB1 nach Abteilungen (ausgegebenes Budget)");

		} else if (e.getActionCommand().equals("LMB1 nach Status")) {

			diagramm4.nachStatus("LMB1 nach Status", "lmb", 1, "LMB1", aktuell);
			card.show(pCenter, "Card16");
			pNamensLeiste.aendereName("Aktueller Stand - LMB1 nach Status");

		} else if (e.getActionCommand().equals(
				"LMB1_2011 nach Abteilungen(ausgegeben)")) {

			diagramm5.nachAbteilung("LMB1_2011 nach Abteilungen",
					"select * from lmb where nummer>2 and kennung=2", 2,
					aktuell, "LMB1_2011");
			card.show(pCenter, "Card17");
			pNamensLeiste
					.aendereName("Aktueller Stand - LMB1_2011 nach Abteilungen (ausgegebenes Budget)");

		} else if (e.getActionCommand().equals("LMB1_2011 nach Status")) {
			diagramm6.nachStatus("LMB1_2011 nach Status",

			"lmb", 2, "LMB1_2011", aktuell);
			card.show(pCenter, "Card18");
			pNamensLeiste
					.aendereName("Aktueller Stand - LMB1_2011 nach Status");

		} else if (e.getActionCommand().equals("Sonderbudgets")) {
			diagramm7.erstelleLayoutSonderbudget(aktuell);
			card.show(pCenter, "Card19");
			pNamensLeiste.aendereName("Aktueller Stand - Sonderbudgets");

		} else if (e.getActionCommand().equals("Übersicht UT8")) {

			Thread thread = new Thread(runnable);
			thread.start();

			ab.anzeigen(2);
			card.show(pCenter, "Card20");
			pNamensLeiste.aendereName("Übersicht Bestellungen - UT8");
		} else if (e.getActionCommand().equals("Übersicht UT3")) {
			ab.anzeigen(3);
			card.show(pCenter, "Card20");
			pNamensLeiste.aendereName("Übersicht Bestellungen - UT3");
		} else if (e.getActionCommand().equals("Übersicht LMB1")) {
			ab.anzeigen(4);
			card.show(pCenter, "Card20");
			pNamensLeiste.aendereName("Übersicht Bestellungen - LMB1");
		} else if (e.getActionCommand().equals("Übersicht LMB2")) {
			ab.anzeigen(5);
			card.show(pCenter, "Card20");
			pNamensLeiste.aendereName("Übersicht Bestellungen - LMB2");
		} else if (e.getActionCommand().equals("Übersicht Sonderbudgets alle")) {
			ab.anzeigen(6);
			card.show(pCenter, "Card20");
			pNamensLeiste
					.aendereName("Übersicht Bestellungen - Sonderbudgets alle");

		} else if (e.getActionCommand().equals("Übersicht Sonderbudgets")) {
			ab11.anzeigen(18);

			card.show(pCenter, "Card34");
			pNamensLeiste.aendereName("Übersicht Bestellungen - Sonderbudgets");

		} else if (e.getActionCommand().equals("BANF Auflistung nach Lehrer")) {
			ab2.anzeigen(7);
			card.show(pCenter, "Card21");
			pNamensLeiste.aendereName("BANF Auflistung nach Lehrer");

		} else if (e.getActionCommand().equals("Hauptbereich")) {

			ab7.anzeigen(8);
			card.show(pCenter, "Card22");

			pNamensLeiste.aendereName("Hauptbereich");
		} else if (e.getActionCommand().equals("Bereich")) {

			ab8.anzeigen(15);
			card.show(pCenter, "Card30");

			pNamensLeiste.aendereName("Bereich");
		} else if (e.getActionCommand().equals("Hauptkostenstelle")) {

			ab9.anzeigen(16);
			card.show(pCenter, "Card31");

			pNamensLeiste.aendereName("Hauptkostenstelle");
		} else if (e.getActionCommand().equals("Kostenstelle")) {

			ab10.anzeigen(17);
			card.show(pCenter, "Card32");

			pNamensLeiste.aendereName("Kostenstelle");

		} else if (e.getActionCommand().equals(
				"Auswertung nach Abteilung - betragsmäßig")) {
			ab3.anzeigen(9);
			card.show(pCenter, "Card23");
			pNamensLeiste
					.aendereName("Auswertung nach Abteilung - betragsmäßig");

		} else if (e.getActionCommand().equals(
				"Auswertung nach Abteilung - verhältnismäßig")) {
			ab4.anzeigen(10);
			card.show(pCenter, "Card24");
			pNamensLeiste
					.aendereName("Auswertung nach Abteilung - verhältnismäßig");

		} else if (e.getActionCommand().equals("Umsatz je Firma")) {
			ab5.anzeigen(11);
			card.show(pCenter, "Card25");
			pNamensLeiste.aendereName("Umsatz je Firma");

		} else if (e.getActionCommand().equals("gekaufte Artikel je Firma")) {
			ab6.anzeigen(12);
			card.show(pCenter, "Card26");
			pNamensLeiste.aendereName("gekaufte Artikel je Firma");

		} else if (e.getActionCommand().equals("ABC-Analyse nach Firmen")) {

			ab13.anzeigen(13);
			card.show(pCenter, "Card27");

			pNamensLeiste.aendereName("ABC-Analyse nach Firmen");

		} else if (e.getActionCommand().equals("ABC-Analyse nach Artikel")) {

			ab12.anzeigen(14);
			card.show(pCenter, "Card28");

			pNamensLeiste.aendereName("ABC-Analyse nach Artikel");
			
		} else if (e.getActionCommand().equals("Heurige Rechnungen anzeigen")) {

			ab14.anzeigen(19);
			card.show(pCenter, "Card35");

			pNamensLeiste.aendereName("Heurige Rechnungen anzeigen");

		} else if (e.getActionCommand().equals("Alle Rechnungen anzeigen")) {

			ab15.anzeigen(20);
			card.show(pCenter, "Card39");

			pNamensLeiste.aendereName("Alle Rechnungen anzeigen");

		} else if (e.getActionCommand().equals("Rechnung löschen")) {

			rL.anzeigenRechnungen();
			card.show(pCenter, "Card36");

			pNamensLeiste.aendereName("Rechnung löschen");
		}

		else if (e.getActionCommand().equals("Buchungen löschen")) {

			bL.anzeigenBuchungen();
			card.show(pCenter, "Card37");

			pNamensLeiste.aendereName("Buchungen löschen");
		}

		else if (e.getActionCommand().equals("Rechnung korrigieren")) {

			card.show(pCenter, "Card38");
			pNamensLeiste.aendereName("Rechnung korrigieren");
		}

		// Menue - neues Budget anlegen
		else if (e.getActionCommand().equals("Sonderbudget ")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewDatensatz maskeSonderbudget = new NewDatensatz(
							"Sonderbudget", 4, con, conL);
					maskeSonderbudget.pack();
					maskeSonderbudget.setLocationRelativeTo(null);
					maskeSonderbudget.setVisible(true);
				}
			});

		}

		else if (e.getActionCommand().equals("UT8 Hauptbereich")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewDatensatz maskeHauptbereich = new NewDatensatz(
							"Hauptbereich", 5, con, conL);
					maskeHauptbereich.pack();
					maskeHauptbereich.setLocationRelativeTo(null);
					maskeHauptbereich.setVisible(true);
				}
			});

		}

		else if (e.getActionCommand().equals("UT8 Bereich")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewDatensatz maskeBereich = new NewDatensatz("Bereich", 6,
							con, conL);
					maskeBereich.pack();
					maskeBereich.setLocationRelativeTo(null);
					maskeBereich.setVisible(true);
				}
			});

		} else if (e.getActionCommand().equals("UT8 Hauptkostenstelle")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewDatensatz maskeHauptkostenstelle = new NewDatensatz(
							"Hauptkostenstelle", 7, con, conL);
					maskeHauptkostenstelle.pack();
					maskeHauptkostenstelle.setLocationRelativeTo(null);
					maskeHauptkostenstelle.setVisible(true);
				}
			});
		}

		else if (e.getActionCommand().equals("UT8 Kostenstelle")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewDatensatz maskeKostenstelle = new NewDatensatz(
							"Kostenstelle", 8, con, conL);
					maskeKostenstelle.pack();
					maskeKostenstelle.setLocationRelativeTo(null);
					maskeKostenstelle.setVisible(true);
				}
			});
		}

		else if (e.getActionCommand().equals("Projekt")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewDatensatz maskeProjekt = new NewDatensatz("Projekt", 2,
							con, conL);
					maskeProjekt.pack();
					maskeProjekt.setLocationRelativeTo(null);
					maskeProjekt.setVisible(true);
				}
			});

		}

		else if (e.getActionCommand().equals("UT3 Abteilungen")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewDatensatz maskeAbteilung = new NewDatensatz("Abteilung",
							1, con, conL);
					maskeAbteilung.pack();
					maskeAbteilung.setLocationRelativeTo(null);
					maskeAbteilung.setVisible(true);
				}
			});
		}

		else if (e.getActionCommand().equals("LMB ")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewDatensatz maskeLmb = new NewDatensatz("LMB", 3, con,
							conL);

					maskeLmb.pack();
					maskeLmb.setLocationRelativeTo(null);
					maskeLmb.setVisible(true);
				}
			});

			// Menueeintraege

		} else if (e.getActionCommand().equals("Daten in EXCEL exportieren")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					de = new AbfrageExportExcel(con, conL, new Vector(), null);
					de.pack();
					de.setLocationRelativeTo(null);
					de.setVisible(true);
				}

			});

			pNamensLeiste.aendereName("Daten in EXCEL exportieren");

		} else if (e.getActionCommand().equals("Budget-Datenbank exportieren")) {

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					dbX = new DBExport(con, con, 0, jahrEnde);
					dbX.pack();
					dbX.setLocationRelativeTo(null);
					dbX.setVisible(true);
				}

			});
			pNamensLeiste.aendereName("Budget-Datenbank exportieren");

		} else if (e.getActionCommand().equals("Null setzen")) {
			card.show(pCenter, "Card15");
			nullsetzen.erzeugeMeldung();
			pNamensLeiste.aendereName("Null setzen");

		} else if (e.getActionCommand().equals("Jahresende")) {

			conB_alt = dv.verbindeDatenbankAlt();

			if (jahresendeB != true)
				jahresendeB = true;

			pNamensLeiste.aendereName("Jahresende");
			jahrEnde = new Jahresende(con, conL, conB_alt);
			jahrEnde.erzeugeMeldung();

		}

		else if (e.getActionCommand().equals("Beenden")) {
			try {
				con.close();
				conL.close();
				if (jahresendeB)
					conB_alt.close();
				System.out
						.println("Die Verbindungen zu den Datenbanken wurden getrennt.");
			} catch (SQLException e1) {
				System.out
						.println("Die Verbindungen zu den Datenbanken konnten nicht getrennt werden.");
				e1.printStackTrace();
			}

			dispose();
		}

		else if (e.getActionCommand().equals("Info"))

		{
			information = new Info();
			information.setSize(600, 700);
			information.setLocationRelativeTo(null);
			information.setVisible(true);
		}

	}

	/**
	 * Das Auswahlmenue "Menü" mit den einzelnen Submenues wird erzeugt
	 * 
	 * @return das komplette Menü vom Typ "JMenu" wird zurueck gegeben
	 */
	public JMenu createFileMenu() {
		datei = new JMenu("Menü");
		datei.setMnemonic('M'); // Tastenkürzel (Alt-M) zum Öffnen des Menüs

		bezahlung = new JMenuItem("Bezahlung", 'z');
		setCtrlAccelerator(bezahlung, 'Z'); // Tastenkombination Strg+Z (auch
		// bei geschlossenem Menü)
		bezahlung.addActionListener(this);
		datei.add(bezahlung);

		abfragen = new JMenu("Abfragen");
		abfragen.setMnemonic('A'); // Tastenkürzel (Alt-A) - Alt muss gedrueckt
		// gehalten werden

		// Submenue

		abfrage1 = new JMenu("Aktueller Stand");
		abfragen.add(abfrage1);

		// Submenue

		mIUt8 = new JMenuItem("UT8 nach Status");
		mIUt8.addActionListener(this);
		abfrage1.add(mIUt8);

		mIUt3 = new JMenu("UT3");
		abfrage1.add(mIUt3);

		// Submenue

		ut3NachAbteilungen = new JMenuItem("UT3 nach Abteilungen (ausgegeben)");
		ut3NachAbteilungen.addActionListener(this);
		mIUt3.add(ut3NachAbteilungen);

		ut3NachStatus = new JMenuItem("UT3 nach Status");
		ut3NachStatus.addActionListener(this);
		mIUt3.add(ut3NachStatus);

		// End Submenue

		mILmb1 = new JMenu("LMB 1");
		abfrage1.add(mILmb1);

		// Submenue

		lmb1NachAbteilungen = new JMenuItem("LMB1 nach Abteilungen(ausgegeben)");
		lmb1NachAbteilungen.addActionListener(this);
		mILmb1.add(lmb1NachAbteilungen);

		lmb1NachStatus = new JMenuItem("LMB1 nach Status");
		lmb1NachStatus.addActionListener(this);
		mILmb1.add(lmb1NachStatus);

		// End Submenue

		mILmb2 = new JMenu("LMB2");
		abfrage1.add(mILmb2);

		// Submenue

		lmb2NachAbteilungen = new JMenuItem("LMB2 nach Abteilungen(ausgegeben)");
		lmb2NachAbteilungen.addActionListener(this);
		mILmb2.add(lmb2NachAbteilungen);

		lmb2NachStatus = new JMenuItem("LMB2 nach Status");
		lmb2NachStatus.addActionListener(this);
		mILmb2.add(lmb2NachStatus);

		// End Submenue

		mISonder = new JMenuItem("Sonderbudgets");
		mISonder.addActionListener(this);
		abfrage1.add(mISonder);

		abfrage2 = new JMenu("Übersicht Bestellungen");
		abfragen.add(abfrage2);

		// Submenue

		uebersichtUt8 = new JMenuItem("Übersicht UT8");
		uebersichtUt8.addActionListener(this);
		abfrage2.add(uebersichtUt8);

		uebersichtUt3 = new JMenuItem("Übersicht UT3");
		uebersichtUt3.addActionListener(this);
		abfrage2.add(uebersichtUt3);

		uebersichtLmb1 = new JMenuItem("Übersicht LMB1");
		uebersichtLmb1.addActionListener(this);
		abfrage2.add(uebersichtLmb1);

		uebersichtLmb2 = new JMenuItem("Übersicht LMB2");
		uebersichtLmb2.addActionListener(this);
		abfrage2.add(uebersichtLmb2);

		uebersichtSonderAlle = new JMenuItem("Übersicht Sonderbudgets alle");
		uebersichtSonderAlle.addActionListener(this);
		abfrage2.add(uebersichtSonderAlle);

		uebersichtSonder = new JMenuItem("Übersicht Sonderbudgets");
		uebersichtSonder.addActionListener(this);
		abfrage2.add(uebersichtSonder);

		// End Submenue

		banfAuflistung = new JMenuItem("BANF Auflistung nach Lehrer");
		banfAuflistung.addActionListener(this);
		setCtrlAccelerator(banfAuflistung, '1'); // Tastenkombination Strg+1
		abfragen.add(banfAuflistung);

		banfsNachBereichen = new JMenu(
				"Banfs nach Haubtbereich, Bereich, Hauptkostenstelle, Kostenstelle");
		abfragen.add(banfsNachBereichen);

		// Submenue

		banfHauptbereich = new JMenuItem("Hauptbereich");
		banfHauptbereich.addActionListener(this);
		banfsNachBereichen.add(banfHauptbereich);

		banfBereich = new JMenuItem("Bereich");
		banfBereich.addActionListener(this);
		banfsNachBereichen.add(banfBereich);

		banfHauptkst = new JMenuItem("Hauptkostenstelle");
		banfHauptkst.addActionListener(this);
		banfsNachBereichen.add(banfHauptkst);

		banfKst = new JMenuItem("Kostenstelle");
		banfKst.addActionListener(this);
		banfsNachBereichen.add(banfKst);

		// End Submenue

		auswertungAbtBetrag = new JMenuItem(
				"Auswertung nach Abteilung - betragsmäßig");
		auswertungAbtBetrag.addActionListener(this);
		setCtrlAccelerator(auswertungAbtBetrag, '3'); // Tastenkombination
		// Strg+3
		abfragen.add(auswertungAbtBetrag);

		auswertungAbtVerh = new JMenuItem(
				"Auswertung nach Abteilung - verhältnismäßig");
		auswertungAbtVerh.addActionListener(this);
		setCtrlAccelerator(auswertungAbtVerh, '4'); // Tastenkombination Strg+4
		abfragen.add(auswertungAbtVerh);

		umsatzFirma = new JMenuItem("Umsatz je Firma");
		umsatzFirma.addActionListener(this);
		setCtrlAccelerator(umsatzFirma, '5'); // Tastenkombination Strg+5
		abfragen.add(umsatzFirma);

		gekaufteArtikel = new JMenuItem("gekaufte Artikel je Firma");
		gekaufteArtikel.addActionListener(this);
		setCtrlAccelerator(gekaufteArtikel, '6'); // Tastenkombination Strg+6
		abfragen.add(gekaufteArtikel);

		abcFirmen = new JMenuItem("ABC-Analyse nach Firmen");
		abcFirmen.addActionListener(this);
		setCtrlAccelerator(abcFirmen, '7'); // Tastenkombination Strg+7
		abfragen.add(abcFirmen);

		abcArtikel = new JMenuItem("ABC-Analyse nach Artikel");
		abcArtikel.addActionListener(this);
		setCtrlAccelerator(abcArtikel, '8'); // Tastenkombination Strg+8
		abfragen.add(abcArtikel);

		// End Submenue

		datei.add(abfragen);

		neuesBudget = new JMenu("neues Budget/Kostenstelle anlegen");
		datei.add(neuesBudget);

		// Submenue
		hauptbereich = new JMenuItem("UT8 Hauptbereich");
		hauptbereich.addActionListener(this);
		neuesBudget.add(hauptbereich);

		bereich = new JMenuItem("UT8 Bereich");
		bereich.addActionListener(this);
		neuesBudget.add(bereich);

		hauptkostenstelle = new JMenuItem("UT8 Hauptkostenstelle");
		hauptkostenstelle.addActionListener(this);
		neuesBudget.add(hauptkostenstelle);

		kostenstelle = new JMenuItem("UT8 Kostenstelle");
		kostenstelle.addActionListener(this);
		neuesBudget.add(kostenstelle);

		ut3 = new JMenuItem("UT3 Abteilungen");
		ut3.addActionListener(this);
		neuesBudget.add(ut3);

		lmb = new JMenuItem("LMB ");
		lmb.addActionListener(this);
		neuesBudget.add(lmb);

		sonder = new JMenuItem("Sonderbudget ");
		sonder.addActionListener(this);
		neuesBudget.add(sonder);

		projekt = new JMenuItem("Projekt");
		projekt.addActionListener(this);
		neuesBudget.add(projekt);

		// End Submenue

		buchungenLoeschen = new JMenuItem("Buchungen löschen");
		buchungenLoeschen.addActionListener(this);
		setCtrlAccelerator(buchungenLoeschen, 'L'); // Tastenkombination Strg+8
		datei.add(buchungenLoeschen);

		// der Menueeintrag Jahresende erscheint nur, wenn man mit den aktuellen
		// Daten arbeitet
		if (aktuell) {
			jahresende = new JMenuItem("Jahresende", 'J');
			setCtrlAccelerator(jahresende, 'J'); // Tastenkombination Strg+I
			jahresende.addActionListener(this);
			datei.add(jahresende);
		}
		// Separator
		datei.addSeparator();

		beenden = new JMenuItem("Beenden", 'B');
		setCtrlAccelerator(beenden, 'B'); // Tastenkombination Strg+B
		beenden.addActionListener(this);
		datei.add(beenden);

		// End Separator

		return datei;
	}

	/**
	 * Das Auswahlmenue "Hilfe" wird erzeugt
	 * 
	 * @return Das komplette Menue vom Typ "JMenu" wird zurück gegeben
	 */
	public JMenu createHelpMenu() {
		hilfe = new JMenu("Hilfe");
		hilfe.setMnemonic('H'); // Tastenkuerzel (Alt-D) zum Oeffnen des Menues
		info = new JMenuItem("Info", 'I');
		setCtrlAccelerator(info, 'I'); // Tastenkombination Strg+I
		info.addActionListener(this);
		hilfe.add(info);

		return hilfe;
	}

	public JMenu createRechnungMenu() {
		rechnung = new JMenu("Rechnung");

		rechnungSuchen = new JMenuItem("Rechnung suchen", 'R');
		setCtrlAccelerator(rechnungSuchen, 'R'); // Tastenkombination Strg+R
		rechnungSuchen.addActionListener(this);
		rechnung.add(rechnungSuchen);

		rechnungAendern = new JMenuItem("Rechnung korrigieren");
		rechnungAendern.addActionListener(this);
		setCtrlAccelerator(rechnungAendern, 'A'); // Tastenkombination Strg+A
		rechnung.add(rechnungAendern);

		rechnungenLoeschen = new JMenuItem("Rechnung löschen");
		rechnungenLoeschen.addActionListener(this);
		setCtrlAccelerator(rechnungenLoeschen, '0'); // Tastenkombination Strg+0
		rechnung.add(rechnungenLoeschen);

		rechnungen = new JMenuItem("Alle Rechnungen anzeigen");
		rechnungen.addActionListener(this);
		setCtrlAccelerator(rechnungen, '9'); // Tastenkombination Strg+9
		rechnung.add(rechnungen);

		rechnungenHeurige = new JMenuItem("Heurige Rechnungen anzeigen");
		rechnungenHeurige.addActionListener(this);
		setCtrlAccelerator(rechnungenHeurige, 'B'); // Tastenkombination Strg+8
		rechnung.add(rechnungenHeurige);

		return rechnung;

	}

	public JMenu createDatenbankMenu() {
		datenbank = new JMenu("Datenbank");

		dbExportieren = new JMenuItem("Budget-Datenbank exportieren", 'S');
		setCtrlAccelerator(dbExportieren, 'S'); // Tastenkombination Strg+S für
		// Save
		dbExportieren.addActionListener(this);
		datenbank.add(dbExportieren);

		datenbankBefuellen = new JMenuItem("Budget-Datenbank importieren", 'I');
		setCtrlAccelerator(datenbankBefuellen, 'I'); // Tastenkombination Strg+D
		datenbankBefuellen.addActionListener(this);
		datenbank.add(datenbankBefuellen);

		exExportieren = new JMenuItem("Daten in EXCEL exportieren", 'X');
		setCtrlAccelerator(exExportieren, 'X'); // Tastenkombination Strg+X
		exExportieren.addActionListener(this);
		datenbank.add(exExportieren);

		nullSetzen = new JMenuItem("Null setzen", 'N');
		setCtrlAccelerator(nullSetzen, 'N'); // Tastenkombination Strg+N
		nullSetzen.addActionListener(this);
		datenbank.add(nullSetzen);

		return datenbank;

	}

	/**
	 * die Methode lauscht, ob ein Tastenkuerzel mit "Strg" gedrueckt wurde
	 * 
	 * @param mi
	 *            das JMenuItem, das ausgefuehrt werden soll
	 * @param acc
	 *            drückt man Strg + diese Taste, wird das JMenuItem ausgefuehrt
	 */
	public void setCtrlAccelerator(JMenuItem mi, char acc) {
		KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.CTRL_MASK);
		mi.setAccelerator(ks);
	}

}
