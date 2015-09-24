package Budget;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;

/**
 *In der Klasse Eingabemaske wird ein Frame erzeugt, in dem man eine neue
 * Abteilung, Sonderbudget, Projekt, Hauptbereich, Bereich,Hauptkostenstelle,
 * Kostenstelle anlegen kann
 *<p>
 * Title: Eingabemaske
 * 
 * @author Haupt, Liebhart
 **/
public class NewDatensatz extends JFrame implements ActionListener, KeyListener {

	// Textfield
	/** JTextField zum Einlesen der Daten **/
	private JTextField[] eingaben;
	/** Langnummer fuer UT8 **/
	private JTextField langnummer;

	// Button
	/** Button speichern, um die Eingabe zu speichern **/
	private JButton btnSpeichern = new JButton("Speichern");
	/** Button OK **/
	private JButton buttonOK = new JButton("OK");
	/** Button OK **/
	private JButton buttonOK2 = new JButton("OK");
	/** Button OK **/
	private JButton buttonOK3 = new JButton("OK");
	/** Button Neue Eingabe, um eine neue Eingabe zu machen **/
	private JButton btnNeueEingabe = new JButton("Neue Eingabe");

	// sonstiges
	/** Connection zur Datenbank **/
	private Connection con;
	/** Budget, das eingegeben wird **/
	private double budget;
	/** Result **/
	private ResultSet rs;
	/** Container **/
	private Container c = getContentPane();
	/** Objekt von der Klasse Ueberpruefung **/
	private UeberpruefungBudget pruefen;
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;
	/** ob es sich um das Bugdet UT8 handelt oder nicht **/
	private boolean isUt8;

	// Statement
	/** Statement 1 **/
	private Statement stmt;
	/** Statement 2 **/
	private Statement stmt2;

	// int
	/**
	 * die Variable Kennnung gibt Auskunft ob die Daten fuer LMB1 oder LMB2 sind
	 **/
	private int kennung;
	/** Hilfsvariable **/
	private int nummer = 0;
	/** Kennnummer der Tabelle **/
	private int kennnummer;

	// String[]
	/** Namen der Tabellen **/
	private String[] namenTabellen = { "abteilungut3", "projekt", "lmb",
			"sonderbudget", "hauptbereichut8", "bereichut8",
			"hauptkostenstelleut8", "kostenstelleut8", "lmb" };
	/** Namen der Ueberschriften (Tabellen) **/
	private String[] namen = { "Abteilung", "Projekt", "LMB 1", "Sonderbudget",
			"Hauptbereich", "Bereich", "Hauptkostenstelle", "Kostenstelle",
			"LMB1_2011" };
	/** Hinweise bei der Erstellung eines Projektes **/
	String[] projekthinweise = { "Nummer", "Name/Gegenstand", "Lehrer",
			"Kurzz", "Klasse", "Datum", "Teilnehmer" };

	// ComboBox
	/** JComboBox Hauptbereich **/
	private JComboBox hauptbereich = new JComboBox();
	/** JComboBox Bereich **/
	private JComboBox bereich = new JComboBox();
	/** JComboBox Hauptkostenstelle **/
	private JComboBox hauptkostenstelle = new JComboBox();
	/**
	 * Combobox fuer die Abteilungen, die man beim Anlegen eines auswählen kann
	 **/
	private JComboBox projekt = new JComboBox();

	// String
	/** welcher Eintrag des Hauptbereiches wurde gewaehlt **/
	private String welcherEintragHauptbereich;
	/** welcher Eintrag des Bereiches wurde gewaehlt **/
	private String welcherEintragBereich;
	/** welcher Eintrag der Hauptkostenstelle wurde gewaehlt **/
	private String welcherEintragHauptkostenstelle;
	/** Tabellenname **/
	private String tabellennamen;
	/** Befehl fuer die Datenbank **/
	private String query;

	// Vector
	/** Vector fuer die Eintraege der Tabelle Hauptbereich **/
	private Vector<String> namenHauptbereich;
	/** Vector fuer die Eintraege der Tabelle Bereich **/
	private Vector<String> namenBereich;
	/** Vector fuer die Eintragee der Tabelle Hauptkostenstelle **/
	private Vector<String> namenHauptkostenstelle;
	/** Hilfsvektoren **/
	private Vector<String> row;
	/** Nummern der uebergeordneten Eintraege **/
	private Vector nummern = new Vector();
	/** uebergeordnete Eintraege **/
	private Vector<String> eintraege = new Vector<String>();

	private NewTextdatei txt = new NewTextdatei();

	/**
	 * Konstruktor
	 * 
	 * @param titel
	 *            Titel der Eingabemaske
	 * @param kennnummer
	 *            gibt Auskunft in welche Tabelle die Daten eingefuegt werden
	 *            soll
	 * @param con
	 *            Connection zur Datenbank Budget
	 * @param conL
	 *            Connection zur Datenbank Logisitk
	 */
	public NewDatensatz(String titel, int kennnummer, Connection con,
			Connection conL) {
		super(titel);

		this.kennnummer = kennnummer;
		this.con = con;

		di = new DatenImport(con, conL);

		tabellennamen = namenTabellen[kennnummer - 1];

		pruefen = new UeberpruefungBudget(tabellennamen, kennnummer, con);

		// Buttons dem ActionListener hinzufuegen
		btnSpeichern.addActionListener(this);
		buttonOK.addActionListener(this);
		buttonOK2.addActionListener(this);
		buttonOK3.addActionListener(this);
		btnNeueEingabe.addActionListener(this);

		btnSpeichern.addKeyListener(this);
		buttonOK.addKeyListener(this);
		buttonOK2.addKeyListener(this);
		buttonOK3.addKeyListener(this);
		btnNeueEingabe.addKeyListener(this);

		btnSpeichern.setToolTipText("Speichern des neuen Eintrages");
		btnNeueEingabe.setToolTipText("Neue Eingabe");

		// am Anfang sollen die Buttons OK2 und OK3 nicht drueckbar sein
		buttonOK2.setEnabled(false);
		buttonOK3.setEnabled(false);

		if (kennnummer == 1 || kennnummer == 5 || kennnummer == 4
				|| kennnummer == 3 || kennnummer == 9) {
			erstelleLayoutNormal();
		}
		// wenn die Kennnummer 2(bedeutet Projekt), muss ein anderes Layout
		// erstellt werden, da es noch zusaetzliche Felder gibt
		if (kennnummer == 2) {
			c.setLayout(new GridLayout(9, 2));
			erstelleLayoutFuerProjekt();
		}
		// wenn die Kennnummer =6 oder 7 (bereich, hauptkostenstelle),
		// muss ein anderes Layout erstellt werden
		if (kennnummer == 6 || kennnummer == 7) {
			c.setLayout(new GridLayout((kennnummer - 1), 3));
			erstelleLayoutFuerUT8();
		}
		// wenn kennummer=8 (kostenstelleut8), dann gibt es zusaetzlich noch die
		// Felder Langnummer,Raumnummer und Kurzbezeichnung
		if (kennnummer == 8) {
			c.setLayout(new GridLayout(9, 3));
			erstelleLayoutFuerUT8Kostenstelle();
		}

		// Button Speichern wird hinzugefügt ist
		add(btnSpeichern);
		add(btnNeueEingabe);

		c.setVisible(true);

	}

	/**
	 * Die Methode erstelleLayout erstellt das normale Layout fuer die
	 * Eingabemaske
	 **/
	public void erstelleLayoutNormal() {

		eingaben = new JTextField[2];

		if (kennnummer == 5) {
			c.setLayout(new GridLayout(4, 2));
			add(new JLabel("Langnummer:"));
			langnummer = new JTextField();
			add(langnummer);
		} else
			c.setLayout(new GridLayout(3, 2));

		add(new JLabel(namen[kennnummer - 1])); // Tabellenname
		eingaben[0] = new JTextField(); // Textfeld
		add(eingaben[0]); // wird hinzugefuegt

		add(new JLabel("geplantes Budget")); // geplantes Budget
		eingaben[1] = new JTextField(); // Textfeld
		add(eingaben[1]); // wird hinzugefuegt

		isUt8 = false;

	}// end erstelleLayoutNormal

	/**
	 * Die Methode erstelleLayoutFuerProjekt erstellt das Layout fuer Projekte
	 */
	public void erstelleLayoutFuerProjekt() {

		projekt.addItem("MI");
		projekt.addItem("WI");
		projekt.addItem("EL");
		projekt.addItem("ET");

		eingaben = new JTextField[7];

		for (int i = 0; i < eingaben.length; i++)

		{
			add(new JLabel(projekthinweise[i]));
			eingaben[i] = new JTextField();
			add(eingaben[i]);
		}

		add(new JLabel("Abteilung"));
		add(projekt); // ComboBox

		isUt8 = false;

	}// end erstelleLayoutFuerProjekt

	/**
	 * Die Methode erstelleLayoutFuerUT8 erstellt das Layout fuer Bereich und
	 * Hauptkostenstelle
	 */
	public void erstelleLayoutFuerUT8() {

		isUt8 = true;

		eingaben = new JTextField[2]; // zwei Standardfelder

		add(new JLabel("Langnummer:"));
		langnummer = new JTextField();
		add(langnummer);
		add(new JLabel(""));

		add(new JLabel(namen[kennnummer - 1])); // Tabellennamen
		eingaben[0] = new JTextField();
		add(eingaben[0]);
		add(new JLabel(""));

		add(new JLabel("geplantes Budget"));
		eingaben[1] = new JTextField();
		add(eingaben[1]);
		add(new JLabel(""));

		// man bekommt die Namen der Hauptbereiche der Tabelle Hauptbereichut8,
		// gespeichert in einem Vector
		namenHauptbereich = auslesenNamen(4);

		// Befuellung der JComboBox mit den Namen der Hauptbereiche/ j=1 weil
		// die erste Zeile nicht erscheinen soll (hauptbereichut8)
		for (int j = 1; j < namenHauptbereich.size(); j++) {
			hauptbereich.addItem(namenHauptbereich.elementAt(j));
		}

		add(new JLabel("Hauptbereich"));
		add(hauptbereich);
		add(buttonOK);

		// wenn die Kennnumer==7 (Hauptkostenstelle), dann braucht
		// man zusaetzlich noch einen Bereich (ComboBox ist aber noch leer)
		if (kennnummer == 7) {
			add(new JLabel("Bereich"));
			add(bereich);
			add(buttonOK2);
		}

	}

	/**
	 * Die Methode erstelleLayoutFuerUT8Kostenstelle erstellt das Layout fuer
	 * die Kostenstelle
	 */
	public void erstelleLayoutFuerUT8Kostenstelle() {

		isUt8 = true;

		eingaben = new JTextField[4]; // zwei Standardfelder

		add(new JLabel("Langnummer"));
		langnummer = new JTextField();
		add(langnummer);
		add(new JLabel(""));

		add(new JLabel(namen[kennnummer - 1])); // Tabellennamen
		eingaben[0] = new JTextField();
		add(eingaben[0]);
		add(new JLabel(""));

		add(new JLabel("geplantes Budget"));
		eingaben[1] = new JTextField();
		add(eingaben[1]);
		add(new JLabel(""));

		add(new JLabel("Raumnummer")); // Tabellennamen
		eingaben[2] = new JTextField();
		add(eingaben[2]);
		add(new JLabel(""));

		add(new JLabel("Kurzbezeichnung")); // Tabellennamen
		eingaben[3] = new JTextField();
		add(eingaben[3]);
		add(new JLabel(""));

		// man bekommt die Namen der Hauptbereiche der Tabelle Hauptbereichut8,
		// gespeichert in einem Vector
		namenHauptbereich = auslesenNamen(4);

		// Befuellung der JComboBox mit den Namen der Hauptbereiche/ j=1 weil
		// die erste Zeile nicht erscheinen soll (hauptbereichut8)

		hauptbereich.removeAllItems();
		for (int j = 1; j < namenHauptbereich.size(); j++) {
			hauptbereich.addItem(namenHauptbereich.elementAt(j));
		}

		add(new JLabel("Hauptbereich"));
		add(hauptbereich);
		add(buttonOK);

		add(new JLabel("Bereich"));
		add(bereich);
		add(buttonOK2);

		add(new JLabel("Hauptkostenstelle"));
		add(hauptkostenstelle);
		add(buttonOK3);

	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 **/
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSpeichern) // klickt man den Button speichern
		// werden die Daten in die Datenbank
		// gespeichert

		{
			if (ueberpruefeEingabe(nummern, isUt8)) // Eingabe wird auf Fehler
			// ueberprueft
			{ // soll in lmb1 oder lmb2 gespeichert werden

				speicherInDatenbank();

				// setzt wieder alle Eingabefelder auf Null
				for (int i = 0; i < eingaben.length; i++)
					eingaben[i].setText(null);

				if (kennnummer > 4 && kennnummer < 9)
					langnummer.setText(null);
				nummer = 0;
				eintraege.clear();
				nummern.clear();

			} else {
				eintraege.clear();
				nummern.clear();
			}
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});

			buttonOK.setEnabled(true);
			buttonOK2.setEnabled(false);
			buttonOK3.setEnabled(false);

		}

		if (e.getSource() == buttonOK) {
			welcherEintragHauptbereich = hauptbereich.getSelectedItem()
					.toString(); // der gewaehlte Hauptberich wird
			// herausgelesen

			namenBereich = auslesenNamenMitWhere(4, welcherEintragHauptbereich);
			bereich.removeAllItems();

			for (int j = 0; j < namenBereich.size(); j++) {
				bereich.addItem(namenBereich.elementAt(j));
			}

			eintraege.add(welcherEintragHauptbereich);

			nummern = getNummernVonEintraegen(eintraege);

			buttonOK.setEnabled(false);
			buttonOK2.setEnabled(true);
		}

		if (e.getSource() == buttonOK2) {
			welcherEintragBereich = bereich.getSelectedItem().toString();

			namenHauptkostenstelle = auslesenNamenMitWhere(5,
					welcherEintragBereich);

			hauptkostenstelle.removeAllItems();

			for (int j = 0; j < namenHauptkostenstelle.size(); j++) {
				hauptkostenstelle.addItem(namenHauptkostenstelle.elementAt(j));
			}

			eintraege.add(welcherEintragBereich);
			nummern = getNummernVonEintraegen(eintraege);

			buttonOK2.setEnabled(false);
			buttonOK3.setEnabled(true);
		}// end if(buttonOK2)

		if (e.getSource() == buttonOK3) {
			welcherEintragHauptkostenstelle = hauptkostenstelle
					.getSelectedItem().toString();

			auslesenNamenMitWhere(6, welcherEintragHauptkostenstelle);

			eintraege.add(welcherEintragHauptkostenstelle);
			nummern = getNummernVonEintraegen(eintraege);

			buttonOK3.setEnabled(false);
		}// end if(buttonOk3)

		// wenn der Button Neue Eingabe gedrueckt wird, wird alles wieder
		// zurückgesetzt
		if (e.getSource() == btnNeueEingabe) {
			buttonOK.setEnabled(true);
			buttonOK2.setEnabled(false);
			buttonOK3.setEnabled(false);

			for (int i = 0; i < eingaben.length; i++)
				eingaben[i].setText(null);

			if (kennnummer > 4 && kennnummer < 9)
				langnummer.setText(null);
			nummer = 0;

			bereich.removeAllItems();
			hauptkostenstelle.removeAllItems();

			eintraege.clear();
			nummern.clear();
		}
	}// end actionPerformed

	/**
	 * Die Methode speicherInDatenbank speichert die Eingaben in die Datenbank
	 **/
	public void speicherInDatenbank() {

		try {
			stmt = con.createStatement();

			if (kennnummer == 2) {
				query = "insert into "
						+ tabellennamen
						+ "(nummerSelbst,name,lehrer,kurzz,klasse,datum,teilnehmer,abteilung)values('"
						+ nummer + "','" + eingaben[1].getText() + "','"
						+ eingaben[2].getText() + "','" + eingaben[3].getText()
						+ "','" + eingaben[4].getText() + "','"
						+ eingaben[5].getText() + "','" + eingaben[6].getText()
						+ "','" + projekt.getSelectedItem() + "');";

				txt.schreibeEinenDatensatzInTextdatei(eingaben[1].getText(),
						"projektName.txt");
				txt.schreibeEinenDatensatzInTextdatei("" + nummer,
						"projektNummer.txt");
				txt.schreibeEinenDatensatzInTextdatei(eingaben[2].getText(),
						"projektLehrer.txt");
				txt.schreibeEinenDatensatzInTextdatei(eingaben[3].getText(),
						"projektkurzz.txt");
				txt.schreibeEinenDatensatzInTextdatei(eingaben[4].getText(),
						"projektKlasse.txt");
				txt.schreibeEinenDatensatzInTextdatei(eingaben[5].getText(),
						"projektDatum.txt");
				txt.schreibeEinenDatensatzInTextdatei(eingaben[6].getText(),
						"projektTeilnehmer.txt");
				txt.schreibeEinenDatensatzInTextdatei(projekt.getSelectedItem()
						.toString(), "projektAbteilung.txt");

			} else if (kennnummer == 6 || kennnummer == 7) {// Zuordnungsnummer
															// der
				// ueberstehenden
				// Tabelle
				// auslesen
				auslesenNamenMitWhere(kennnummer - 2,
						welcherEintragHauptbereich);
				query = "insert into " + tabellennamen
						+ "(hauptnummer, name,geplant,nummerSelbst)values("
						+ nummer + ",'" + eingaben[0].getText() + "'," + budget
						+ ",'" + langnummer.getText() + "')";

				if (kennnummer == 6) {
					txt.schreibeEinenDatensatzInTextdatei(
							eingaben[0].getText(), "bereichut8.txt");
					txt.schreibeEinenDatensatzInTextdatei(langnummer.getText(),
							"nummerSelbstUt8ber.txt");
					txt.schreibeEinenDatensatzInTextdatei("" + nummer,
							"hauptnummerUt8ber.txt");
				}

				else {
					txt.schreibeEinenDatensatzInTextdatei(
							eingaben[0].getText(), "hauptkostenstelleut8.txt");
					txt.schreibeEinenDatensatzInTextdatei(langnummer.getText(),
							"nummerSelbstUt8hkst.txt");
					txt.schreibeEinenDatensatzInTextdatei("" + nummer,
							"hauptnummerUt8hkst.txt");

				}

			} else if (kennnummer == 8) {
				auslesenNamenMitWhere(kennnummer - 2,
						welcherEintragHauptbereich);
				query = "insert into "
						+ tabellennamen
						+ "(hauptnummer, name,geplant,raumnummer,projektkennung,nummerSelbst,kurzbezeichn)values("
						+ nummer + ",'" + eingaben[0].getText() + "'," + budget
						+ ",'" + eingaben[2].getText() + "',0,'"
						+ langnummer.getText() + "','" + eingaben[3].getText()
						+ "')";

				txt.schreibeEinenDatensatzInTextdatei(eingaben[0].getText(),
						"kostenstellenut8.txt");
				txt.schreibeEinenDatensatzInTextdatei(langnummer.getText(),
						"nummerSelbstUt8kst.txt");
				txt.schreibeEinenDatensatzInTextdatei(eingaben[3].getText(),
						"kurzbezUt8.txt");
				txt.schreibeEinenDatensatzInTextdatei(eingaben[2].getText(),
						"raumnummerUt8.txt");
				txt.schreibeEinenDatensatzInTextdatei("" + nummer,
						"hauptnummerUt8kst.txt");

			} else if (kennnummer == 3 || kennnummer == 9) {

				if (kennnummer == 3)
					kennung = 1;
				else
					kennung = 2;

				query = "insert into " + tabellennamen
						+ "(name,geplant,kennung)values('"
						+ eingaben[0].getText() + "'," + budget + "," + kennung
						+ ")";

				txt.schreibeEinenDatensatzInTextdatei("" + kennung,
						"kennung.txt");
				txt.schreibeEinenDatensatzInTextdatei(eingaben[0].getText(),
						"abteilungenlmb.txt");

			} else if (kennnummer == 5)

			{
				query = "insert into " + tabellennamen
						+ "(name,geplant,nummerSelbst)values('"
						+ eingaben[0].getText() + "'," + budget + ",'"
						+ langnummer.getText() + "')";

				txt.schreibeEinenDatensatzInTextdatei(eingaben[0].getText(),
						"hauptbereichut8.txt");
				txt.schreibeEinenDatensatzInTextdatei(langnummer.getText(),
						"nummerSelbstUt8hber.txt");

			} else
				query = "insert into " + tabellennamen
						+ "(name,geplant)values('" + eingaben[0].getText()
						+ "'," + budget + ")";

			if (kennnummer == 1)
				txt.schreibeEinenDatensatzInTextdatei(eingaben[0].getText(),
						"abteilungenut3.txt");
			if (kennnummer == 4)
				txt.schreibeEinenDatensatzInTextdatei(eingaben[0].getText(),
						"sonderbudget.txt");

			stmt.executeUpdate(query);

			dispose();

		} catch (SQLException ex) {
			System.out.println("Fehler beim Einfuegen von dem neuen Datensatz");
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
			ex.printStackTrace();
		}

	}// end speicherInDatenbank()

	/**
	 * Die Methode auslesenNamen liesst die Namen einer Tabelle aus
	 * 
	 * @param kennung
	 *            Kennung, welche Tabelle (Kennnummer-1)
	 * @return Vector mit den Daten
	 **/
	public Vector<String> auslesenNamen(int kennung) {

		row = new Vector<String>();

		try {
			stmt = con.createStatement();
			query = "select * from " + namenTabellen[kennung];
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row.add(rs.getString("Name"));
			}

			rs.close();

		} catch (SQLException ex) {
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return row;
	}

	/**
	 * Die Methode auslesenNamenMitWhere liesst die Namen einer Tabelle zu einer
	 * bestimmten Nummer aus
	 * 
	 * @param kennung
	 *            Kennung, welche Tabelle (Kennnummer-1)
	 * @param name
	 *            Name des Eintrages in der Datenbank, zu dem man die
	 *            zugehoerigen Daten der Untertabellen wissen moechte
	 * @return Vector<String> mit den Daten
	 */
	public Vector<String> auslesenNamenMitWhere(int kennung, String name) {
		row = new Vector<String>();

		try {
			stmt = con.createStatement();
			query = "select nummer from " + namenTabellen[kennung]
					+ " where name= '" + name + "'";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				nummer = rs.getInt("Nummer");
			}

			rs.close();

			stmt2 = con.createStatement();
			query = "select * from " + namenTabellen[kennung + 1]
					+ " where hauptnummer= " + nummer;

			rs = stmt2.executeQuery(query);

			while (rs.next()) {
				row.add(rs.getString("Name"));
			}

			rs.close();
		} catch (SQLException ex) {
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return row;
	}

	/**
	 * Die Methode ueberpruefeEingabe ueberprueft die Eingaben vom User
	 * 
	 * @param nummern
	 *            Nummern der Ueberbereiche
	 * @param isUt8
	 *            ob es sich um das Budget UT8 handelt
	 * @return true, wenn die Eingabe in Ordnung war; false, wenn die Eingabe
	 *         nicht in Ordnung war
	 **/
	public boolean ueberpruefeEingabe(Vector nummern, boolean isUt8) {

		if ((nummern.size() == 0) && (isUt8)) {
			JOptionPane.showMessageDialog(this,
					"Sie haben keine Ueberbereiche ausgewaehlt!");
			return false;
		}
		if (kennnummer != 2) {
			try {
				budget = Double.parseDouble(eingaben[1].getText());

			} catch (NumberFormatException e1) {
				JOptionPane
						.showMessageDialog(this,
								"Sie muessen eine Zahl für das geplante Budget eingeben!");
				return false;
			}
		}

		if ((kennnummer == 1 || kennnummer == 3 || kennnummer > 4)
				&& !pruefen.ueberpruefeWertNeuesBudget(budget, nummern, isUt8)) {
			JOptionPane.showMessageDialog(this, "Der Betrag ist zu hoch");
			return false;
		}

		// wenn der Text bei den Feldern Name oder geplantes Budget 0 ist,
		// bedeutet das, dass
		// nichts eingegeben wurde--> Fehler
		// wenn der Eintrag laenger als 50 Zeichen ist --> Fehler
		// (funktioniert mit der Datenbank nicht)
		if (kennnummer != 2 && kennnummer != 8) {
			if ((((eingaben[0].getText()).length()) > 50)
					|| (eingaben[0].getText()).length() == 0) {
				JOptionPane
						.showMessageDialog(
								this,
								"Bitte geben Sie einen Namen ein oder beachten Sie die Laenge Ihrer Eingabe (<50)");
				return false;
			}

			// wenn das Budget eine negative Zahl ist
			if (budget < 0) {
				JOptionPane.showMessageDialog(this,
						"Das Budget darf nicht negativ sein");
				return false;
			}
		}

		if (kennnummer == 8) {
			if (budget < 0) {
				JOptionPane.showMessageDialog(this,
						"Das Budget darf nicht negativ sein");
				return false;
			}

			if (langnummer.getText().length() > 30
					|| eingaben[0].getText().length() > 50

					|| eingaben[2].getText().length() > 30
					|| eingaben[3].getText().length() > 10) {
				JOptionPane.showMessageDialog(this,
						"Bitte beachten Sie die Laenge Ihrer Eingabe");
				return false;
			}
			if (eingaben[0].getText().length() == 0
					|| eingaben[1].getText().length() == 0
					|| eingaben[2].getText().length() == 0
					|| eingaben[3].getText().length() == 0) {
				JOptionPane.showMessageDialog(this,
						"Sie haben eine Eingabefeld leer gelassen");
				return false;
			}
		}
		// wenn der Text bei den Feldern Betreuer oder Teilnehmer 0 ist,
		// wurde
		// nichts eingegeben --> Fehler
		// wenn der Eintrag laenger als 150 Zeichen
		// ist -->Fehler
		// (funktioniert mit der Datenbank nicht)
		if (kennnummer == 2) {

			if ((((eingaben[0].getText()).length()) > 11)

			|| (((eingaben[1].getText()).length()) > 30)

			|| (((eingaben[2].getText()).length()) > 50)

			|| (((eingaben[3].getText()).length()) > 30)

			|| (((eingaben[4].getText()).length() > 10))

			|| (((eingaben[5].getText()).length() > 10))

			|| (((eingaben[6].getText()).length() > 50)))

			{
				JOptionPane.showMessageDialog(this,
						"Bitte beachten Sie die Laenge Ihrer Eingabe");
				return false;
			}
			if ((((eingaben[0].getText()).length()) == 0)
					|| (((eingaben[1].getText()).length()) == 0)
					|| (((eingaben[2].getText()).length() == 0))
					|| (((eingaben[3].getText()).length()) == 0)
					|| (((eingaben[4].getText()).length() == 0))
					|| (((eingaben[5].getText()).length() == 0))
					|| (((eingaben[6].getText()).length() == 0))) {
				JOptionPane.showMessageDialog(this,
						"Sie haben ein Eingabefeld leer gelassen");
				return false;
			}

			try {
				nummer = Integer.parseInt(eingaben[0].getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this,
						"Sie müssen eine Zahl bei dem Feld Nummer eingeben.");
				return false;
			}
		}

		return true;

	}// endUeberpruefeEingabe

	/**
	 * gibt die Nummern der Eintraege zurueck
	 * 
	 * @param eintraege
	 *            Vector mit den Eintraegen
	 * @return Nummern der Eintraege
	 */
	public Vector getNummernVonEintraegen(Vector eintraege) {
		nummern = di.getNummernVonEintraegen(eintraege);
		return nummern;
	}

	public void keyPressed(KeyEvent k) {
		if (k.getKeyCode() == 10) {
			if (k.getSource() == btnSpeichern) // klickt man den Button
			// speichern
			// werden die Daten in die Datenbank
			// gespeichert

			{
				if (ueberpruefeEingabe(nummern, isUt8)) // Eingabe wird auf
				// Fehler
				// ueberprueft
				{ // soll in lmb1 oder lmb2 gespeichert werden

					speicherInDatenbank();

					// setzt wieder alle Eingabefelder auf Null
					for (int i = 0; i < eingaben.length; i++)
						eingaben[i].setText(null);

					if (kennnummer > 4 && kennnummer < 9)
						langnummer.setText(null);
					nummer = 0;
					eintraege.clear();
					nummern.clear();

				} else {
					eintraege.clear();
					nummern.clear();
				}
				addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						dispose();
					}
				});

				buttonOK.setEnabled(true);
				buttonOK2.setEnabled(false);
				buttonOK3.setEnabled(false);

			}

			if (k.getSource() == buttonOK) {
				welcherEintragHauptbereich = hauptbereich.getSelectedItem()
						.toString(); // der gewaehlte Hauptberich wird
				// herausgelesen

				namenBereich = auslesenNamenMitWhere(4,
						welcherEintragHauptbereich);
				bereich.removeAllItems();

				for (int j = 0; j < namenBereich.size(); j++) {
					bereich.addItem(namenBereich.elementAt(j));
				}

				eintraege.add(welcherEintragHauptbereich);

				nummern = getNummernVonEintraegen(eintraege);

				buttonOK.setEnabled(false);
				buttonOK2.setEnabled(true);
			}

			if (k.getSource() == buttonOK2) {
				welcherEintragBereich = bereich.getSelectedItem().toString();

				namenHauptkostenstelle = auslesenNamenMitWhere(5,
						welcherEintragBereich);

				hauptkostenstelle.removeAllItems();

				for (int j = 0; j < namenHauptkostenstelle.size(); j++) {
					hauptkostenstelle.addItem(namenHauptkostenstelle
							.elementAt(j));
				}

				eintraege.add(welcherEintragBereich);
				nummern = getNummernVonEintraegen(eintraege);

				buttonOK2.setEnabled(false);
				buttonOK3.setEnabled(true);
			}// end if(buttonOK2)

			if (k.getSource() == buttonOK3) {
				welcherEintragHauptkostenstelle = hauptkostenstelle
						.getSelectedItem().toString();

				auslesenNamenMitWhere(6, welcherEintragHauptkostenstelle);

				eintraege.add(welcherEintragHauptkostenstelle);
				nummern = getNummernVonEintraegen(eintraege);

				buttonOK3.setEnabled(false);
			}// end if(buttonOk3)

			// wenn der Button Neue Eingabe gedrueckt wird, wird alles wieder
			// zurückgesetzt
			if (k.getSource() == btnNeueEingabe) {
				buttonOK.setEnabled(true);
				buttonOK2.setEnabled(false);
				buttonOK3.setEnabled(false);

				for (int i = 0; i < eingaben.length; i++)
					eingaben[i].setText(null);

				if (kennnummer > 4 && kennnummer < 9)
					langnummer.setText(null);
				nummer = 0;

				bereich.removeAllItems();
				hauptkostenstelle.removeAllItems();

				eintraege.clear();
				nummern.clear();
			}
		}
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}// end Klasse Eingabemaske
