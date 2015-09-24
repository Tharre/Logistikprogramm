package Budget;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *In der Klasse Bestellungen werden alle Bestellungen, alle nicht bezahlte und
 * alle teileweise bezahlten Bestellungen aufgelistet
 *<p>
 * Title: Bestellungen
 * 
 * @author Haupt, Liebhart
 **/
public class Bestellungen extends JPanel implements ActionListener {

	// -------------------------------Connections---------------------------
	/** Connection zur Budget-Datenbank **/
	private Connection con;
	/** Connection zur Logistik-Datenbank **/
	private Connection conL;

	// -------------------------------Buttons-------------------------------
	/** Button bezahlen **/
	private JButton bezahlen = new JButton("Bezahlen");
	/** Button bezahlen der teilweisen Rechnungen **/
	private JButton bezahlenTW = new JButton(
			"Bezahlen der teilweise bezahlten Bestellungen");
	/** Button nicht bezahlte Rechnungen **/
	private JButton nichtBez = new JButton("Nicht bezahlte Bestellungen");
	/** Button alle Bestellungen **/
	private JButton alleBestellungen = new JButton("Alle Bestellungen");
	/** Button teilweise bezahlte Bestellungen **/
	private JButton teilweiseBez = new JButton(
			"Teilweise bezahlte Bestellungen");
	private JButton heurigeBestellungen = new JButton(
	"Heurige Bestellungen");
	/** Button aktualisieren **/
	private JButton aktualisieren = new JButton("Aktualisieren");

	// -------------------------------Panels-------------------------------
	/** alle Buttons werden im ButtonsPanel zusammengefasst **/
	private JPanel buttons = new JPanel();
	/** Panel fuer die Mitte **/
	private JPanel mitte;
	/** Hilfspanel zum Anzeigen der nicht bezahlten Bestellungen **/
	private JPanel hilfspanel1 = new JPanel();
	/** Hilfspanel zum Anzeigen der teilweise bezahlten Bestellungen **/
	private JPanel hilfspanel2 = new JPanel();
	private JPanel hilfspanel3 = new JPanel();

	// --------------------------Objekte unserer Klassen--------------------
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;
	/** Objekt von der Klasse Bezahlung **/
	private Bezahlung bez;
	/** Tabelle mit allen Bestellungen **/
	private Tabelle table;
	/** Tabelle mit allen nicht bezahlten Bestellungen **/
	private Tabelle tableNB;
	/** Tabelle mit allen teilweise bezahlten Bestellungen **/
	private Tabelle tableTB;
	private Tabelle tableHB;

	// ----------------------------JScrollpanes-----------------------------
	/** Scrollpane fuer die Tabelle table **/
	private JScrollPane scroll;
	/** Scrollpane fuer die Tabelle tableNB **/
	private JScrollPane scrollNB;
	/** Scrollpane fuer die Tabelle tableTB **/
	private JScrollPane scrollTB;
	private JScrollPane scrollHB;

	// ----------------------------sonstiges-----------------------------
	/** Vector, in dem die Daten von der Datenbank gespeichert werden **/
	private Vector daten = new Vector();
	/** Spaltennamen fuer die Tabelle **/
	private String[] spalten = { "BestellID", "Datum", "Antragssteller",
			"Status Bestellung", "Status Bezahlung", "Budget", "WNummer",
			"Firma" };
	/**
	 * hier wird der Wert gespeichert, der in der Tabelle im Feld "Nummer" steht
	 **/
	private int zeilennummer;
	/** CardLayout **/
	private CardLayout card = new CardLayout();

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budget-Datenbank
	 * @param conL
	 *            Connection zur Logistik-Datenbank
	 */
	public Bestellungen(Connection con, Connection conL) {

		this.con = con;
		this.conL = conL;

		setLayout(new BorderLayout());
		buttons.setLayout(new GridLayout(20, 1));

		hilfspanel1.setLayout(new BorderLayout());
		hilfspanel2.setLayout(new BorderLayout());
		hilfspanel3.setLayout(new BorderLayout());

		di = new DatenImport(con, conL);
		bez = new Bezahlung(con, conL);

		// erstellt die beiden Tabellen
		erstelleTabelleheurigeBestellungen();
		erstelleTabelle();
		erstelleTabelleNichtBezahlt();
		erstelleTabelleTeilweiseBezahlt();
		

		mitte = new JPanel(card);

		// ins Panel mitte adden
		mitte.add("heurige",hilfspanel3);
		mitte.add("Bestellungen", scroll);
		mitte.add("Bezahlung", bez);
		mitte.add("nichtBEZ", hilfspanel1);
		mitte.add("Teilweise", hilfspanel2);
		

		// die Buttons dem Panel adden
		buttons.add(nichtBez);
		buttons.add(teilweiseBez);
		buttons.add(heurigeBestellungen);
		buttons.add(alleBestellungen);
		buttons.add(aktualisieren);
		

		// ActionListener adden
		bezahlen.addActionListener(this);
		nichtBez.addActionListener(this);
		alleBestellungen.addActionListener(this);
		teilweiseBez.addActionListener(this);
		bezahlenTW.addActionListener(this);
		heurigeBestellungen.addActionListener(this);
		aktualisieren.addActionListener(this);

		add(buttons, BorderLayout.EAST);
		add(mitte, BorderLayout.CENTER);

	}

	/**
	 * erstellt die Tabelle mit allen Bestellungen
	 **/
	public void erstelleTabelle() {
		daten.clear();
		daten = di.auslesenDatenLogistik("bestellung");

		table = new Tabelle(spalten, daten, con, conL);
		scroll = table.getTabelle();

		setVisible(true);

	}

	/**
	 * erstellt die Tabelle mit allen nicht bezahlten Bestellungen
	 **/
	public void erstelleTabelleNichtBezahlt() {
		daten.clear();
		daten = di.auslesenNichtbezahlteBestellung();

		tableNB = new Tabelle(spalten, daten, con, conL);
		scrollNB = tableNB.getTabelle();

		hilfspanel1.add(scrollNB, BorderLayout.CENTER);
		hilfspanel1.add(bezahlen, BorderLayout.SOUTH);

		setVisible(true);

	}
	
	public void erstelleTabelleheurigeBestellungen() {
		daten.clear();
		daten = di.auslesenheurigeBestellungen();

		tableHB = new Tabelle(spalten, daten, con, conL);
		scrollHB = tableHB.getTabelle();

		hilfspanel3.add(scrollHB, BorderLayout.CENTER);
		hilfspanel3.add(bezahlen, BorderLayout.SOUTH);

		setVisible(true);

	}

	/**
	 * erstellt die Tabelle mit allen teilweise bezahlten Bestellungen
	 **/
	public void erstelleTabelleTeilweiseBezahlt() {
		daten.clear();
		daten = di.auslesenTeilweiseBezahlteBestellungen();

		tableTB = new Tabelle(spalten, daten, con, conL);
		scrollTB = tableTB.getTabelle();

		hilfspanel2.add(scrollTB, BorderLayout.CENTER);
		hilfspanel2.add(bezahlenTW, BorderLayout.SOUTH);

		setVisible(true);

	}

	/**
	 * Die Methode actionPerformed lauscht, ob ein Button gedrueckt wird
	 * 
	 * @param e
	 *            ein ActionEvent-Objekt
	 **/
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bezahlen) {
			if (tableNB.welcheZeile()) {
				zeilennummer = tableNB.getZeilennummer();

				card.show(mitte, "Bezahlung");
			bez.anzeigen(zeilennummer, tableNB.getDaten(5), false);
			}
		}

		if (e.getSource() == bezahlenTW) {
			if (tableTB.welcheZeile()) {
				zeilennummer = tableTB.getZeilennummer();

				card.show(mitte, "Bezahlung");
			bez.anzeigen(zeilennummer, tableTB.getDaten(5), di
						.gibtRechnung(tableTB.getDaten(6)));
			}

		}
		if (e.getSource() == nichtBez) {
			aktualisiere(tableNB, di.auslesenNichtbezahlteBestellung());
			card.show(mitte, "nichtBEZ");

		}

		if (e.getSource() == alleBestellungen) {
			aktualisiere(table, di.auslesenDatenLogistik("bestellung"));
			card.show(mitte, "Bestellungen");
		}
		if (e.getSource() == teilweiseBez) {
			aktualisiere(tableTB, di.auslesenTeilweiseBezahlteBestellungen());
			card.show(mitte, "Teilweise");

		}
		if (e.getSource() == heurigeBestellungen) {
			aktualisiere(tableHB, di.auslesenheurigeBestellungen());
			card.show(mitte, "heurige");

		}
		if (e.getSource() == aktualisieren) {
			aktualisiere(table, di.auslesenDatenLogistik("bestellung"));
			aktualisiere(tableTB, di.auslesenTeilweiseBezahlteBestellungen());
			aktualisiere(tableNB, di.auslesenNichtbezahlteBestellung());
			aktualisiere(tableHB, di.auslesenheurigeBestellungen());
		}
	}

	/**
	 * klickt man auf den Button "aktualisieren", wird diese Methode aufgerufen;
	 * die mitgeschickte Tabelle wird aktualisiert
	 * 
	 * @param table
	 *            die Tabelle, die aktualisiert werden soll
	 * @param data
	 *            die Daten, die angezeigt werden sollen
	 */
	public void aktualisiere(Tabelle table, Vector data) {

		table.setzeDaten(data);
		table.aufrufen();

	}

}