package Budget;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Vector;

/**
 *Hier koennen die Sonderabzuege zu den jeweiligen Bestellpositionen eingegeben
 * werden
 *<p>
 * Title: Sonderabzuege
 * 
 *@author Haupt, Liebhart
 **/
public class BezSonderabzuege extends JFrame implements ActionListener {

	// Connection
	/** Connection zur Budget-Datenbank **/
	private Connection con;
	/** Connection zur Logistik-Datenbank **/
	private Connection conL;

	// JPanels
	/** beinhaltet die BestellID, zur die Bestellpositionen gehoeren **/
	private JPanel links11 = new JPanel();
	/** beinhaltet die BanfIDs **/
	private JPanel links1 = new JPanel();
	/** beinhaltet die Gesamtpreise der Bestellpositionen **/
	private JPanel links = new JPanel();
	/** beinhaltet die Textfelder, in denen man die Sonderabzuege eingeben kann **/
	private JPanel rechts = new JPanel();
	private JPanel nettoStueckPreisP = new JPanel();
	private JPanel nettopreisP = new JPanel();
	private JPanel materialP = new JPanel();

	// Objekte von eigenen Klassen
	/** Objekt von der Klasse RechnungEingabe **/
	private RechnungEingabe re;
	/** Objekt von der Klasse RechnungAussuchen **/
	private RechnungAussuchen ra;
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;

	// JLabels
	/** Label BestellID **/
	private JLabel lBestId = new JLabel("BestellId:");
	/** Label BanfId **/
	private JLabel lId = new JLabel("BanfId:");
	/** Label Preis gesamt **/
	private JLabel lHinweis1 = new JLabel("Preis gesamt:",JLabel.CENTER);
	/** Label Sonderabzuege **/
	private JLabel lHinweis2 = new JLabel("Sonderabzuege:");
	private JLabel nettopreis = new JLabel("Nettopreis:",JLabel.CENTER);
	private JLabel material = new JLabel("Material:");
	private JLabel lNettoStueck = new JLabel(" Netto Stueckpreis: ",JLabel.CENTER);

	// sonstiges
	/** Button weiter **/
	private JButton weiter = new JButton("weiter");
	/** Eingabefelder fuer die Sonderabzuege **/
	private JTextField[] field;
	private JTextField[] fieldNetto;
	

	// int
	/** BestellID **/
	private int bestID;
	/** Anzahl der Zeilen **/
	private int zeilen;

	// boolean
	/** Hilfsvariabel, ob die Eingabe des Benutzers in Ordnung ist **/
	private boolean passt;
	/** ist die Rechnung eine Gesamtrechnung oder eine Teilrechnung **/
	private boolean istGesamt;
	/** gibt es bereits eine Rechnung */
	private boolean gibtRechnung;
	/** kommen in der Bestellung gemeinsame Kostenstellen vor **/
	private boolean mitAnteile = false;

	// Vectoren
	/** Wo im Vector stehen die richtigen Informationen **/
	private Vector<Integer> woImVector = new Vector<Integer>();
	/** Banfids **/
	private Vector<Integer> banfIds;
	/** Gesamtpreis **/
	private Vector<Double> preisGes;
	/** Kostenstellen **/
	private Vector<String> kostenstelle;
	/** Bestellpositionsids **/
	private Vector<Integer> bestposIds;
	/** welche gemeinsamen Kostenstellen gibt es **/
	private Vector<Integer> welcheKostenstelle;
	/** alle Sonderabzuege **/
	private Vector<Double> sonderabzuege = new Vector<Double>();
	/** Preise minus der Sonderabzuege **/
	private Vector<Double> preisMinusSonder = new Vector<Double>();
	/** Zeilennummer der angeklickten Bestellpositionen **/
	private Vector<Integer> angeklickt;
	private Vector<String> materialV;
	private Vector<Double> nettoStueck;
	/** Menge, die bezahlt werden kann: geliefert - bezahlt aus DB**/
	private Vector<Double> menge = new Vector<Double>();

	// double
	/** Summe der Sonderabzuege **/
	private double summeSonderabzuege;
	/** Werte mit den Prozenten **/
	private double[][] werte;

	// String
	/** Titel fuer das Frame RechnungEingabe **/
	private String titel;
	/** Name des Budgets der Bestellung **/
	private String budget;
	/** W- Nummer **/
	private String wNummer;
	/** true; wenn die Bestellung nun fertig bezahlt ist; false: wenn noch BestPos ausständig sind**/
	private boolean fertigBez;

	/**
	 * Konstruktor, der aufgerufen wird, wenn es eine Bestellung ohne Anteile
	 * ist
	 * 
	 * @param zeilen
	 *            anzahl der ausgewählten Bestellpositionen
	 * @param banfIds
	 *            die Banf IDs
	 * @param preisGes
	 *            Vector, gesamter Preis pro Bestellposition
	 * @param bestID
	 *            die ID, der zu bezahlenden Bestellung
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 * @param istGesamt
	 *            ein Boolean; true: alle Bestellpositionen sollen bezahlt
	 *            werden; false: es bleiben unbezahlte Bestellpositionen über
	 * @param budget
	 *            mit welchem Budget wird die Bestellung bezahlt
	 * @param gibtRechnung
	 *            ein Boolean; true: es gibt schon eine Rechnung zu der
	 *            W-Nummer; false: es gibt noch keine Rechnung
	 * @param kostenstelle
	 *            ein Vector, auf dem alle betroffenen Kostenstellen gespeichert
	 *            sind
	 * @param bestposIds
	 *            alle BestellpositionsIDs, die ausgewaehlt wurden
	 * @param angeklickt
	 *            an welcher Stelle im Vector bestposIds stehen die angeklicken
	 *            Bestellpositionen
	 */
	public BezSonderabzuege(int zeilen, Vector<Integer> banfIds,
			Vector<Double> preisGes, int bestID, Connection con,
			Connection conL, boolean istGesamt, String budget,
			boolean gibtRechnung, Vector<String> kostenstelle,
			Vector<Integer> bestposIds, Vector<Integer> angeklickt,
			Vector<String> materialV,Vector<Double> nettoStueck, boolean fertigBez, Vector<Double> menge) {
		super("Sonderabzüge");

		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.istGesamt = istGesamt;
		this.budget = budget;
		this.gibtRechnung = gibtRechnung;
		this.banfIds = banfIds;
		this.kostenstelle = kostenstelle;
		this.zeilen = zeilen;
		this.preisGes = preisGes;
		this.bestposIds = bestposIds;
		this.angeklickt = angeklickt;
		this.materialV = materialV;
		this.nettoStueck = nettoStueck;
		this.fertigBez = fertigBez;
		this.menge = menge;

		setLayout();
	}

	/**
	 * Dieser Konstruktor wird aufgerufen, wenn es Kostenstellen mit Anteilen
	 * gibt
	 * 
	 * @param zeilen
	 *            anzahl der ausgewählten Bestellpositionen
	 * @param banfIds
	 *            die Banf IDs
	 * @param preisGes
	 *            Vector, gesamter Preis pro Bestellposition
	 * @param bestID
	 *            die ID, der zu zahlenden Bestellung
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 * @param istGesamt
	 *            ein Boolean; true: alle Bestellpositionen sollen bezahlt
	 *            werden; false: es bleiben unbezahlte Bestellpositionen über
	 * @param budget
	 *            mit welchem Budget wird die Bestellung bezahlt
	 * @param gibtRechnung
	 *            ein Boolean; true: es gibt schon eine Rechnung zu der
	 *            W-Nummer; false: es gibt noch keine Rechnung
	 * @param kostenstelle
	 *            ein Vector, auf dem alle betroffenen Kostenstellen gespeichert
	 *            sind
	 * @param bestposIds
	 *            alle BestellpositionsIDs, die ausgewaehlt wurden
	 * @param werte
	 *            die Prozentwerte, wie viel Prozent jede Abteilung bekommt
	 * @param welcheKostenstelle
	 *            beinhaltet die Kostenstellen gemeinsam, el/et und/oder wi/mi
	 * @param woImVector
	 *            an welcher Stelle von den ausgewaehlten Banfpositionen ist die
	 *            Kostenstelle mit Anteilen
	 * @param angeklickt
	 *            an welcher Stelle im Vector bestposIds stehen die angeklicken
	 *            Bestellpositionen
	 */
	public BezSonderabzuege(int zeilen, Vector<Integer> banfIds,
			Vector<Double> preisGes, int bestID, Connection con,
			Connection conL, boolean istGesamt, String budget,
			boolean gibtRechnung, Vector<String> kostenstelle,
			Vector<Integer> bestposIds, double[][] werte,
			Vector<Integer> welcheKostenstelle, Vector<Integer> woImVector,
			Vector<Integer> angeklickt, Vector materialV, Vector<Double> nettoStueck, boolean fertigBez, Vector<Double> menge) {
		super("Sonderabzuege");

		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.istGesamt = istGesamt;
		this.budget = budget;
		this.gibtRechnung = gibtRechnung;
		this.banfIds = banfIds;
		this.kostenstelle = kostenstelle;
		this.zeilen = zeilen;
		this.preisGes = preisGes;
		this.bestposIds = bestposIds;
		this.werte = werte;
		this.welcheKostenstelle = welcheKostenstelle;
		this.woImVector = woImVector;
		this.angeklickt = angeklickt;
		mitAnteile = true;
		this.materialV = materialV;
		this.nettoStueck = nettoStueck;
		this.fertigBez = fertigBez;
		this.menge = menge;

		setLayout();
	}

	/**
	 * setzt das Layout fuer den Frame
	 */
	public void setLayout() {
		setLayout(new GridLayout(1,6));
		weiter.addActionListener(this);

		di = new DatenImport(con, conL);

		lBestId.setFont(new Font("Sans Serif", Font.BOLD, 15));
		lId.setFont(new Font("Sans Serif", Font.BOLD, 15));
		lHinweis1.setFont(new Font("Sans Serif", Font.BOLD, 15));
		lHinweis2.setFont(new Font("Sans Serif", Font.BOLD, 15));
		nettopreis.setFont(new Font("Sans Serif", Font.BOLD, 15));
		material.setFont(new Font("Sans Serif", Font.BOLD, 15));
		lNettoStueck.setFont(new Font("Sans Serif", Font.BOLD, 15));

		lBestId.setBackground(Color.YELLOW);
		lBestId.setOpaque(true);
		lId.setBackground(Color.YELLOW);
		lId.setOpaque(true);
		lHinweis1.setBackground(Color.YELLOW);
		lHinweis1.setOpaque(true);
		lHinweis2.setBackground(Color.YELLOW);
		lHinweis2.setOpaque(true);
		nettopreis.setBackground(Color.YELLOW);
		nettopreis.setOpaque(true);
		material.setBackground(Color.YELLOW);
		material.setOpaque(true);
		lNettoStueck.setBackground(Color.YELLOW);
		lNettoStueck.setOpaque(true);
		

		
		field = new JTextField[zeilen];
		fieldNetto = new JTextField[zeilen];

		for (int i = 0; i < zeilen; i++) {
			field[i] = new JTextField();
			fieldNetto[i] = new JTextField();
		}

		links11.setLayout(new GridLayout(zeilen + 3, 1));
		links1.setLayout(new GridLayout(zeilen + 3, 1));
		links.setLayout(new GridLayout(zeilen + 3, 1));
		rechts.setLayout(new GridLayout(zeilen + 3, 1));
		nettopreisP.setLayout(new GridLayout(zeilen + 3, 1));
		materialP.setLayout(new GridLayout(zeilen + 3, 1));
		nettoStueckPreisP.setLayout(new GridLayout(zeilen + 3, 1));

		links11.add(lBestId);
		links1.add(lId);
		materialP.add(material);
		links.add(lHinweis1);
		rechts.add(lHinweis2);
		nettopreisP.add(nettopreis);
		nettoStueckPreisP.add(lNettoStueck);
		

		for (int i = 0; i < zeilen; i++) {
			links11.add(new JLabel("" + bestID));
			links1.add(new JLabel("" + banfIds.get(i)));
			materialP.add(new JLabel("" + materialV.get(i),JLabel.CENTER));
			links.add(new JLabel("" + preisGes.get(i) + " €", JLabel.CENTER));
			rechts.add(field[i]);
			nettopreisP.add(fieldNetto[i]);
			nettoStueckPreisP.add(new JLabel(""+nettoStueck.get(i)+"€",JLabel.CENTER));
			
		}
	
		links11.add(new JLabel());
		links11.add(new JLabel());
		links1.add(new JLabel());
		links1.add(new JLabel());
		materialP.add(new JLabel());
		materialP.add(new JLabel());
		links.add(new JLabel());
		links.add((new JLabel()));
		rechts.add(new JLabel());
		rechts.add(new JLabel());
		nettoStueckPreisP.add(new JLabel());
		nettoStueckPreisP.add(new JLabel());
		nettopreisP.add(new JLabel());
		nettopreisP.add(weiter);
		
		add(links11);
		add(links1);
		add(materialP);
		add(nettoStueckPreisP);
		add(links);
		add(rechts);
		add(nettopreisP);
		
		

		setVisible(true);
	}

	/**
	 * wird aufgerufen, wenn ein Button geklickt wurde
	 * 
	 * @param e
	 *            ein ActionEvent-Objekt
	 */
	public void actionPerformed(ActionEvent e) {
		summeSonderabzuege = 0;
		double hilf = 0;
		passt = true;
		
		int firma = di.getFirmaZuBestellung(bestID);

		Vector<Integer> material = new Vector<Integer>();
		material = di.getMaterialZuBestellPosition(bestID);
		
		for (int i = 0; i < field.length; i++) {
			
			if (field[i].getText().length() == 0) {
				summeSonderabzuege += 0;
				sonderabzuege.add(0.0);
				preisMinusSonder.add(preisGes.get(i));
			} else {
				try {
					hilf = 0;
					sonderabzuege.add(Double.parseDouble(field[i].getText()));
					summeSonderabzuege += (Double.parseDouble(field[i]
							.getText()));
					hilf = preisGes.get(i);
					hilf = hilf - (Double.parseDouble(field[i].getText()));
					preisMinusSonder.add(hilf);

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(this,
							"Bitte geben Sie nur Zahlen ein!");
					passt = false;
					break;
				}

			}
			
			
			if (fieldNetto[i].getText().length() != 0)

				try {
					
					
					double netto = Double.parseDouble(fieldNetto[i].getText());
					
					int materialI = material.get(i);

					di.aendereFirmaMaterialBeziehungPreis(netto, materialI,
							firma);
				} catch (NumberFormatException n) {
					JOptionPane.showMessageDialog(this,
							"Bitte geben Sie nur Zahlen ein!");

				}
		}

		if (summeSonderabzuege < 0) {
			passt = false;
			JOptionPane.showMessageDialog(this,
					"Bitte geben Sie keine negativen Zahlen ein!");
		}
		if (passt) {

			di = new DatenImport(con, conL);
			wNummer = di.getWNummerZuBestellID(bestID);

			/*if (gibtRechnung) {
				hilf = JOptionPane
						.showConfirmDialog(
								this,
								"Wollen Sie eine neue Rechnung anlegen <JA>, oder wollen Sie eine bestehende Rechnung ändern <NEIN>?",
								"WARNUNG", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				dispose();
			}*/

			/*if (!gibtRechnung || hilf == 0) {*/
				if (mitAnteile) {
					re = new RechnungEingabe(summeSonderabzuege, preisGes,
							bestID, con, conL, istGesamt, budget, bestposIds,
							kostenstelle, zeilen, sonderabzuege,
							preisMinusSonder, werte, welcheKostenstelle,
							woImVector, angeklickt, fertigBez, menge);
					re.pack();
					re.setLocationRelativeTo(null);
					re.setVisible(true);
					dispose();
				} else {
					re = new RechnungEingabe(summeSonderabzuege, preisGes,
							bestID, con, conL, istGesamt, budget, bestposIds,
							kostenstelle, zeilen, sonderabzuege,
							preisMinusSonder, angeklickt, fertigBez, menge);
					re.pack();
					re.setLocationRelativeTo(null);
					re.setVisible(true);
					dispose();

				}

			/*{
				if (mitAnteile)
					rufeFensterAufmitAnteilen();
				else
					rufeFensterAuf();

			}*/
		}

	}

	/**
	 * ruft den Konstruktor des JFrames RechnungAussuchen ohne Anteile auf
	 */
	public void rufeFensterAuf() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				ra = new RechnungAussuchen(summeSonderabzuege, preisGes,
						bestID, con, conL, istGesamt, budget, bestposIds,
						kostenstelle, zeilen, sonderabzuege, preisMinusSonder,
						angeklickt, wNummer, fertigBez, menge); // Eingabemaske
				// erzeugen
				ra.pack();
				ra.setLocationRelativeTo(null);
				ra.setVisible(true);
			}
		});

	}

	/**
	 * ruft den Konstruktor des JFrames RechnungAussuchen mit Anteile auf
	 */

	public void rufeFensterAufmitAnteilen() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				ra = new RechnungAussuchen(summeSonderabzuege, preisGes,
						bestID, con, conL, istGesamt, budget, bestposIds,
						kostenstelle, zeilen, sonderabzuege, preisMinusSonder,
						werte, welcheKostenstelle, woImVector, wNummer,
						angeklickt, fertigBez, menge); // Eingabemaske
				// erzeugen
				ra.pack();
				ra.setLocationRelativeTo(null);
				ra.setVisible(true);
			}
		});

	}

}
