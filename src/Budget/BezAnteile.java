package Budget;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *In der Klasse Anteile wird eine Eingabemaske erzeugt, in der man die
 * Prozentanteile der Abteilungen eingeben kann
 *<p>
 * Title: Anteile
 * 
 * @author Haupt, Liebhart 
 **/
public class BezAnteile extends JFrame implements ActionListener {

	// int
	/** Zaehler **/
	public int zaehler = 0;
	/** BestellID **/
	private int bestID;
	/** Anzahl der zeilen **/
	private int zeilen;

	// String
	/** Hinweisfeld **/
	private String hinweis = "Bestellposition";
	/** Budget **/
	private String budget;

	// Vector
	/** An welcher Stelle im Vector stehen die richtigen Informationen **/
	private Vector<Integer> woImVector = new Vector<Integer>();
	/** Banfids **/
	private Vector<Integer> banfIds = new Vector<Integer>();
	/** Gesamtpreis pro Bestellposition **/
	private Vector<Double> preisGes = new Vector<Double>();
	/** Menge, die bezahlt werden kann: geliefert - bezahlt aus DB**/
	private Vector<Double> menge = new Vector<Double>();
	/** Kostenstellen **/
	private Vector<String> kostenstelle = new Vector<String>();
	/** Bestellpositionsids **/
	private Vector<Integer> bestposIds = new Vector<Integer>();
	/** welche gemeinsamen Kostenstellen gibt es **/
	private Vector<Integer> welcheKostenstelle = new Vector<Integer>();
	/**
	 * an welcher Stelle im Vector bestposIds stehen die angeklicken
	 * Bestellpositionen
	 **/
	private Vector<Integer> angeklickt = new Vector<Integer>();
	private Vector<String> materialV = new Vector<String>();
	private Vector<Double> nettoStueck;

	// boolean
	/** ist die Rechnung eine Teilrechnung oder Gesamtrechnung **/
	private boolean teilGesamt;
	/** gibt es bereits eine Rechnung **/
	private boolean gibtRechnung;

	// Connection
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	/** Connection zur Logistikdatenbank **/
	private Connection conL;

	// sonstiges
	/** Prozentwerte **/
	private double[][] werte;
	/** Label fuer die Bestellpositionsnummer **/
	private JLabel nummer;
	/** Objekt von der Klasse Sonderabzuege **/
	private BezSonderabzuege abzuege;
	/** Array mit den Namen der Abteilungen **/
	private String[] namen = { "WI:", "MI:", "ET:", "EL:", "LT:" };
	/** Eingabefelder fuer die Eingaben der Prozente **/
	private JTextField[][] text;
	/** Button weiter **/
	private JButton weiter = new JButton("weiter");
	/** true; wenn die Bestellung nun fertig bezahlt ist; false: wenn noch BestPos ausständig sind**/
	private boolean fertigBez;

	/**
	 * Konstruktor
	 * 
	 * @param zeilen
	 *            Anzahl der ausgewählten Bestellpositionen
	 * @param banfIds
	 *            Banf IDs
	 * @param preisGes
	 *            Vector mit den gesamten Preisen pro Bestellposition
	 * @param bestID
	 *            ID, der zu zahlenden Bestellung
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 * @param teilGesamt
	 *            ein Boolean; true: alle Bestellpositionen sollen bezahlt
	 *            werden; false: es bleiben unbezahlte Bestellpositionen ueber
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
	 * @param gemeinsam
	 *            wieviele gemeinsame Kostenstellen gibt es
	 * @param ele
	 *            wieviele el/et Kostenstellen gibt es
	 * @param wimi
	 *            wieviele wi/mi Kostenstellen gibt es
	 * @param woImVector
	 *            an welcher Stelle im Vector stehen die richtigen Informationen
	 * @param welcheKostenstelle
	 *            welche gemeinsamen Kostenstellen gibt es
	 * @param angeklickt
	 *            an welcher Stelle im Vector bestposIds stehen die angeklicken
	 *            Bestellpositionen
	 */
	public BezAnteile(int zeilen, Vector<Integer> banfIds,
			Vector<Double> preisGes, int bestID, Connection con,
			Connection conL, boolean teilGesamt, String budget,
			boolean gibtRechnung, Vector<String> kostenstelle,
			Vector<Integer> bestposIds, int gemeinsam, int ele, int wimi,
			Vector<Integer> woImVector, Vector<Integer> welcheKostenstelle,
			Vector<Integer> angeklickt,Vector materialV,Vector<Double> nettoStueck, boolean fertigBez, Vector<Double> menge) {

		super("Anteile");

		this.zeilen = zeilen;
		this.banfIds = banfIds;
		this.preisGes = preisGes;
		this.bestID = bestID;
		this.con = con;
		this.conL = conL;
		this.teilGesamt = teilGesamt;
		this.budget = budget;
		this.gibtRechnung = gibtRechnung;
		this.kostenstelle = kostenstelle;
		this.bestposIds = bestposIds;
		this.woImVector = woImVector;
		this.welcheKostenstelle = welcheKostenstelle;
		this.angeklickt = angeklickt;
		this.materialV= materialV;
		this.nettoStueck = nettoStueck;
		this.fertigBez = fertigBez;
		this.menge = menge;

		setLayout(new GridLayout(((6 * gemeinsam) + (3 * (ele + wimi))) + 1, 2));

		// da es bei LMB1 und LMB1_2011 noch zusaetzlich die Kostenstelle LT gibt,
		// sind die beiden Arrays um eins groesser
		if (budget.equals("LMB1") || budget.equals("LMB1_2011")) {
			text = new JTextField[gemeinsam + ele + wimi][5];
			werte = new double[gemeinsam + ele + wimi][5];

		} else {
			text = new JTextField[gemeinsam + ele + wimi][4];
			werte = new double[gemeinsam + ele + wimi][4];

		}

		weiter.addActionListener(this);

		// es wird in allen Feldern 0 geschrieben (erleichert den Benutzer die
		// Eingabe, falls er nur wenigen Abteilungen Prozente vergeben moechte)
		// moeglicherweise auch Verminderung der Fehlerquelle, dass der Benutzer
		// ein Feld vergisst
		for (int i = 0; i < text.length; i++) {
			for (int l = 0; l < text[i].length; l++) {
				text[i][l] = new JTextField();
				text[i][l].setText("0");
			}
		}

		zaehler = 0;

		for (int k = 0; k < welcheKostenstelle.size(); k++) {
			if (welcheKostenstelle.get(k) == 1) // 1 = gemeinsam (alle
			// Abteilungen)
			{

				nummer = new JLabel(hinweis
						+ " "
						+ bestposIds.get(
								Integer.parseInt(woImVector.get(k).toString()))
								.toString());
				nummer.setBackground(Color.YELLOW);
				nummer.setFont(new Font("SansSerif", Font.BOLD, 10));
				nummer.setOpaque(true);
				add(nummer);

				add(new JLabel(""));

				// alle vier Abteilungen
				for (int l = 0; l < 4; l++) {

					JLabel name = new JLabel(namen[l]);
					name.setFont(new Font("SansSerif", Font.BOLD, 10));
					name.setOpaque(true);
					add(name);
					add(text[k][l]);

				}
				// wenn es sich um LMB1 oder LMB1_2011 handelt, gibt es zusaetzlich
				// noch die Kostenstelle LT
				if (budget.equals("LMB1") || budget.equals("LMB1_2011")) {
					JLabel nameL = new JLabel(namen[4]);
					nameL.setFont(new Font("SansSerif", Font.BOLD, 10));
					nameL.setOpaque(true);
					add(nameL);
					add(text[k][4]);

				}
				zaehler++;

			}
			if (welcheKostenstelle.get(k) == 2) // Kostenstelle el/et
			{

				nummer = new JLabel(hinweis
						+ " "
						+ bestposIds.get(
								Integer.parseInt(woImVector.get(zaehler)
										.toString())).toString());
				nummer.setBackground(Color.YELLOW);
				nummer.setFont(new Font("SansSerif", Font.BOLD, 10));
				nummer.setOpaque(true);
				add(nummer);

				add(new JLabel(""));

				for (int l = 0; l < 2; l++) {

					JLabel name = new JLabel(namen[l + 2]);
					name.setFont(new Font("SansSerif", Font.BOLD, 10));
					name.setOpaque(true);
					add(name);
					add(text[k][l]);

				}
				zaehler++;

			}
			if (welcheKostenstelle.get(k) == 3) //Kostenstelle wi/mi
			{

				nummer = new JLabel(hinweis
						+ " "
						+ bestposIds.get(
								Integer.parseInt(woImVector.get(zaehler)
										.toString())).toString());
				nummer.setBackground(Color.YELLOW);
				nummer.setFont(new Font("SansSerif", Font.BOLD, 10));
				nummer.setOpaque(true);
				add(nummer);

				add(new JLabel(""));

				for (int l = 0; l < 2; l++) {

					JLabel name = new JLabel(namen[l]);
					name.setFont(new Font("SansSerif", Font.BOLD, 10));
					name.setOpaque(true);
					add(name);
					add(text[k][l]);

				}
			}
		}
		add(new JLabel());
		add(weiter);

	}

	/**
	 * Actionperformed
	 * 
	 * @param e
	 *            ActionEvent
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		if (ueberpruefe()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					abzuege = new BezSonderabzuege(zeilen, banfIds, preisGes,
							bestID, con, conL, teilGesamt, budget,
							gibtRechnung, kostenstelle, bestposIds, werte,
							welcheKostenstelle, woImVector, angeklickt,materialV,nettoStueck, fertigBez, menge);
					abzuege.pack();
					abzuege.setLocationRelativeTo(null);
					abzuege.setVisible(true);
				}

			});

			dispose();
		}

	}

	/**
	 * die Methode ueberpruefe ueberprueft die Richtigkeit der Daten
	 * 
	 * @return true, wenn die Daten in Ordnung waren; false, wenn die Daten
	 *         nicht in Ordnung waren
	 */
	public boolean ueberpruefe() {

		double summe;
		for (int i = 0; i < text.length; i++) {
			summe = 0;
			for (int k = 0; k < text[i].length; k++) {
				try {
					werte[i][k] = Double.parseDouble(text[i][k].getText());

					if (werte[i][k] < 0) {
						JOptionPane.showMessageDialog(this,
								"Sie dürfen keine negativen Zahlen eingeben.");
						return false;

					}

					summe += werte[i][k];

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this,
							"Sie dürfen nur Zahlen eingeben");
					return false;
				}
			}

			if (summe < 100) {
				JOptionPane.showMessageDialog(this,
						"Sie müssen 100% insgesamt vergeben.");
				return false;
			}
			if (summe > 100) {
				JOptionPane.showMessageDialog(this,
						"Sie dürfen nicht mehr als 100% insgesamt vergeben.");
				return false;
			}
		}
		return true;
	}

}
