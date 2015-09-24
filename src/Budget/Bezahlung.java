package Budget;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;

import Logistik.LayoutRow;

/**
 *Hier werden die Bestellpositionen angezeigt, die zu der zu bezahlenden
 * Bestellung gehoeren
 *<p>
 * Title: Bezahlung
 * 
 *@author Haupt, Liebhart
 **/
public class Bezahlung extends JPanel implements ActionListener {

	// Connections
	/** Connection zur Budget-Datenbank */
	private Connection con;
	/** Connection zur Logistik-Datenbank */
	private Connection conL;

	// int
	/** Laufindex fuer for-Schleifen **/
	private int i = 0;
	/** BestellID der anbestPosIDsen, zu bezahlenden Bestellung **/
	private int bestID;
	/** wie viele Bestellpositionen beinhaltet die Bestellung **/
	private int anzDatensaetze;
	/** wie viele Kostenstellen sind "gemeinsam" **/
	private int gemeinsam;
	/** wie viele Kostenstellen sind "EL/ET" **/
	private int ele;
	/** wie viele Kostenstellen sind "WI/MI" **/
	private int wimi;
	/** wie viele Bestellpositionen sind zu bezahlen **/
	private int anzBestPos;

	// String
	/** mit welchem Budget wird die Bestellung bezahlt **/
	private String budget;
	/**
	 * die Spaltenueberschriften der Tabelle, in der die Bestellpositionen
	 * angezeigt werden
	 **/
	private String[] spalten = { "BestellID", "BanfID", "Antragsteller",
			"Kostenstelle", "Material", "Menge", "Status Lieferung",
			"Status Bezahlung", "Preis inkl.", "USt [%]", "Preis gesamt" };

	// boolean
	/** true: Gesamtrechnung; ralse: Teilrechnung **/
	private boolean teilGesamt;
	/**
	 * true: es gibt schon eine Rechnung zu dieser Bestellung; false: es gibt
	 * noch keine Rechnung
	 **/
	private boolean gibtRechnung;
	/**
	 * true: die Kostenstellen gemeinsam, EL/ET und WI/MI kommen nicht vor;
	 * false: min. eine davon kommt vor
	 **/
	private boolean warNichtDrin = true;

	// Vectoren
	/**
	 * an welcher Stelle im Vector "preisGes" sind Kostenstellen mit Anteilen
	 * betroffen
	 **/
	private Vector<Integer> woImVector = new Vector<Integer>();
	/** notwendige Infos, die aus der gewaehlten Bestellung geholt werden **/
	private Vector daten = new Vector();
	/**
	 * die Daten, die tatsaechlich angezeigt werden, wenn man auf den Button
	 * "bezahlen" gecklickt hat
	 **/
	private Vector daten2 = new Vector();
	/** die einzelnen gesamten Preise der ausgewaehlten Bestellpositionen **/
	private Vector<Double> preisGes = new Vector<Double>();
	/** Menge, die bezahlt werden kann: geliefert - bezahlt aus DB**/
	private Vector<Double> menge = new Vector<Double>();
	/** die IDs der Bestellanforderungen (Banfs) **/
	private Vector<Integer> banfIds = new Vector<Integer>();
	/** Zeilennummer der angehakten Bestellpositionen **/
	private Vector<Integer> ausgewaehlt = new Vector<Integer>();
	private Vector<Integer> iBestPosIDs = new Vector<Integer>();
	/**
	 * die Kostenstellen der Banfs zu den ausgewaehlten Bestellpositionen; hier
	 * soll der Betrag verbucht werden
	 **/
	private Vector<String> kostenstelle = new Vector<String>();
	/** Zwischenspeicher **/
	private Vector<Integer> hilf = new Vector<Integer>();
	/** die Bezahlstati der ausgewaehlten Bestellpositionen **/
	private Vector<Integer> bezahlt = new Vector<Integer>();
	/** ob die Bestellpositionen lieferbar sind (true) oder nicht (false) **/
	private Vector<Boolean> lieferbar = new Vector<Boolean>();
	private Vector<String> materialV = new Vector<String>();
	/**
	 * 1-3: 1:Kostenstelle "gemeinsam", 2:Kostenstelle "EL/ET", 3:Kostenstelle
	 * "WI/MI
	 **/
	private Vector<Integer> welcheKostenstelle = new Vector<Integer>();

	// JPanels
	/** beinhaltet die Buttons "alles markieren" und "weiter" **/
	private JPanel unten = new JPanel();
	/**
	 * hier werden die Checkboxes plaziert, je nachdem wie viele
	 * Bestellpositionen es gibt
	 **/
	private JPanel links = new JPanel();

	// JButtons
	/** Button "weiter" **/
	private JButton weiter = new JButton("weiter");
	/** Button "alle markieren" **/
	private JButton alle = new JButton("alle markieren");

	// JScrollPane
	/** beinhaltet die Tabelle mit den Daten **/
	private JScrollPane scroll = new JScrollPane();

	// Objekte von eigenen Klassen
	/** Objekt der Klasse DatenImport **/
	private DatenImport di;
	/** Objekt der Klasse Tabelle **/
	// private Tabelle table;
	/** Objekt der Klasse Sonderabzuege **/
	private BezSonderabzuege abzuege;
	/** Objekt der Klasse TabelleMitButtons **/
	private TabelleButtons tableButtons;
	/** Objekt der Klasse Anteile **/
	private BezAnteile anteil;

	Logistik.AnzTabelleA anzTabelle;

	private Object[] bestPosIDs;
	private Object[] geklicktBanf;
	private Object[] geklicktPreisGes;
	private Object[] geklicktKST;
	private Object[] geklicktMaterial;

	private Vector<Double> nettoStueck = new Vector<Double>();
	private boolean alleMark = true;

	/**
	 * true; wenn die Bestellung nun fertig bezahlt ist; false: wenn noch
	 * BestPos ausständig sind
	 **/
	private boolean fertigBez;

	private int nichtLieferbar;

	private int komplettBez;
	
	private int abweichend;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budget-Datenbank
	 * @param conL
	 *            Connection zur Logistik-Datenbank
	 */
	Bezahlung(Connection con, Connection conL) {
		this.con = con;
		this.conL = conL;

		di = new DatenImport(con, conL);

		// Layouts
		setLayout(new BorderLayout());
		unten.setLayout(new GridLayout(1, 2));
		// links.setLayout(new BorderLayout());

		alle.addActionListener(this);
		weiter.addActionListener(this);

	}

	/**
	 * wird aufgerufen, wenn man eine Bestellung zahlen will; zeigt alle
	 * Bestellpositionen an, die zu dieser Bestellung gehören
	 * 
	 * @param zeilennummer
	 *            die ID der Bestellung, die man bezahlen will
	 * @param budget
	 *            mit welchem Budget wird bezahlt
	 * @param gibtRechnung
	 *            true: es gibt bereits eine Rechnung zu der Bestellung false:
	 *            es gibt noch keine
	 */
	public void anzeigen(int zeilennummer, String budget, boolean gibtRechnung) {
		removeAll();

		this.bestID = zeilennummer;
		this.budget = budget;
		this.gibtRechnung = gibtRechnung;

		daten.clear();
		daten2.clear();
		nettoStueck.clear();

		String query = "select count(bestp.id) from banf b, bestellung best, banfpos bp, bestpos bestp, material m where best.bestId="
				+ bestID
				+ " and bestp.bestellId=best.bestId and bestp.banfposnr = bp.banfposnr and bp.banf = b.id and bp.bezeichnung = m.id";
		try {
			Statement stmt = conL.createStatement();
			ResultSet rs1 = stmt.executeQuery(query);

			while (rs1.next()) {
				anzBestPos = rs1.getInt("count(bestp.id)");
			}

			rs1.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		ResultSet rs2 = di.zuBezahlen(zeilennummer);
		ResultSet rs3 = di.zuBezahlen(zeilennummer);

		try {

			while (rs2.next()) {

				nettoStueck.add(rs2.getDouble("bestp.preisExkl"));
				menge.add(rs2.getDouble("zuBezahlen"));
				
			}
			
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		String[] titles = new String[] { "BestposID", "BanfID",
				"Antragsteller", "Kostenstelle", "Material", "Gesamtmenge","offene Menge",
				"Status Lieferung", "Status Bezahlung", "Preis inkl.",
				"Ust[%]", "Preis gesamt", "Auswahl" };

		String[] spaltennamen = new String[] { "bestp.id", "b.id",
				"b.antragsteller", "b.kostenstelle", "m.bezeichnung","Gesamtmenge",
				"zuBezahlen", "bestp.status", "bestp.statusbez",
				"bestp.preisInkl", "bestp.mwst", "preisGesamt" };

		Class[] klasses = { Integer.class, Integer.class, String.class,
				String.class, String.class, Integer.class, Double.class, String.class,
				String.class, Double.class, Integer.class, Double.class,
				Boolean.class };
		anzTabelle = new Logistik.AnzTabelleA(spaltennamen, titles, klasses,
				rs3,1);

		LayoutRow[] detR = anzTabelle.getRows();
		for (int i = 0; i < detR.length; i++) {
			String sb = detR[i].getData(7).toString();// Status Lieferung

			if (sb.equals("1") || sb.equals("nicht abgeschickt"))
				anzTabelle.setValueAt("nicht abgeschickt", i, 7);
			if (sb.equals("2") || sb.equals("abgeschickt"))
				anzTabelle.setValueAt("abgeschickt", i, 7);
			if (sb.equals("3") || sb.equals("richtig geliefert"))
				anzTabelle.setValueAt("richtig geliefert", i, 7);
			if (sb.equals("4") || sb.equals("abweichend geliefert"))
				anzTabelle.setValueAt("abweichend geliefert", i, 7);
			if (sb.equals("5") || sb.equals("nicht lieferbar"))
				anzTabelle.setValueAt("nicht lieferbar", i, 7);
			if (sb.equals("6") || sb.equals("komplett - Teile nicht lieferbar"))
				anzTabelle.setValueAt("komplett - Teile nicht lieferbar", i, 7);

			sb = detR[i].getData(8).toString();// Status Bezahlung
			if (sb.equals("0") || sb.equals("keine Lieferung"))
				anzTabelle.setValueAt("keine Lieferung", i, 8);
			if (sb.equals("1") || sb.equals("teilw geliefert"))
				anzTabelle.setValueAt("teilw geliefert", i, 8);
			if (sb.equals("2") || sb.equals("komplett geliefert"))
				anzTabelle.setValueAt("komplett geliefert", i, 8);
			if (sb.equals("3") || sb.equals("teilw bezahlt"))
				anzTabelle.setValueAt("teilw bezahlt", i, 8);
			if (sb.equals("4") || sb.equals("komplett bezahlt"))
				anzTabelle.setValueAt("komplett bezahlt", i, 8);
		}

		// scroll = table.getTabelle();
		scroll = new JScrollPane(anzTabelle);
		// anzDatensaetze = table.getDatensaetze();

		unten.add(alle);
		unten.add(weiter);

		/*
		 * tableButtons = new TabelleMitButtons(); if (gibtRechnung) {
		 * tableButtons.anzeigenTW(anzDatensaetze, bezahlt, lieferbar); } else {
		 * tableButtons.anzeigen(anzDatensaetze, lieferbar); }
		 * 
		 * links.removeAll(); links.add(tableButtons, BorderLayout.NORTH);
		 */

		// add(links, BorderLayout.WEST);
		add(unten, BorderLayout.SOUTH);
		add(scroll, BorderLayout.CENTER);

		setVisible(true);

		repaint();
		try {
			rs2.close();
			rs3.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * wird aufgerufen, wenn ein Button angeklickt wurde
	 * 
	 * @param e
	 *            ein ActionEvent-Objekt
	 */
	public void actionPerformed(ActionEvent e) {

		Object[] alleErscheinenden;

		if (e.getSource() == weiter) {

			bestPosIDs = anzTabelle.getKlicked("BestposID", "Auswahl");

			alleErscheinenden = anzTabelle
					.getAllKlicked("BestposID", "Auswahl");

			String query = "select count(bestp.id) from banf b, bestellung best, banfpos bp, bestpos bestp, material m where best.bestId="
					+ bestID
					+ " and bestp.bestellId=best.bestId and bestp.banfposnr = bp.banfposnr and bp.banf = b.id and bp.bezeichnung = m.id and (bestp.status=5)";
			try {
				Statement stmt = conL.createStatement();
				ResultSet rs1 = stmt.executeQuery(query);

				while (rs1.next()) {
					nichtLieferbar = rs1.getInt("count(bestp.id)");
				}

				rs1.close();

				query = "select count(bestp.id) from banf b, bestellung best, banfpos bp, bestpos bestp, material m where best.bestId="
						+ bestID
						+ " and bestp.bestellId=best.bestId and bestp.banfposnr = bp.banfposnr and bp.banf = b.id and bp.bezeichnung = m.id and (bestp.statusbez=4)";

				ResultSet rs2 = stmt.executeQuery(query);

				while (rs2.next()) {
					komplettBez = rs2.getInt("count(bestp.id)");
				}

				rs2.close();
				
				String query2 = "select count(bestp.id) from banf b, bestellung best, banfpos bp, bestpos bestp, material m where best.bestId="
					+ bestID
					+ " and bestp.bestellId=best.bestId and bestp.banfposnr = bp.banfposnr and bp.banf = b.id and bp.bezeichnung = m.id and (bestp.status=4)";

				ResultSet rs3 = stmt.executeQuery(query2);

				while (rs3.next()) {
					abweichend = rs3.getInt("count(bestp.id)");
				}

				rs3.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			for (int i = 0; i < alleErscheinenden.length; i++) {
				ausgewaehlt.add(Integer.parseInt(alleErscheinenden[i]
						.toString()));
			}

			if (bestPosIDs.length == 0) {
				JOptionPane.showMessageDialog(this,
						"Sie haben keine Bestell-Position ausgewaehlt!");
			} else {

				geklicktBanf = anzTabelle.getKlicked("BanfID", "Auswahl");
				geklicktPreisGes = anzTabelle.getKlicked("Preis gesamt",
						"Auswahl");
				geklicktKST = anzTabelle.getKlicked("Kostenstelle", "Auswahl");
				geklicktMaterial = anzTabelle.getKlicked("Material", "Auswahl");

				banfIds.clear();
				preisGes.clear();
				kostenstelle.clear();
				materialV.clear();

				for (int i = 0; i < bestPosIDs.length; i++) {
					banfIds.add(Integer.parseInt(geklicktBanf[i].toString()));
					preisGes.add(Double.parseDouble(geklicktPreisGes[i]
							.toString()));
					kostenstelle.add(geklicktKST[i].toString());
					materialV.add(geklicktMaterial[i].toString());
				}

				// Prüfung ob alle bezahlbaren BestPos angeklickt wurden
				// (Teil-/Gesamtrechnung)
				if ((anzBestPos
						- (nichtLieferbar + komplettBez + bestPosIDs.length) == 0)&&(abweichend==0)) {
					fertigBez = true;

					if (gibtRechnung)// Teilrechnung
						teilGesamt = false;
					else
						// Gesamtrechnung
						teilGesamt = true;
				} else {
					fertigBez = false;
					teilGesamt = false;
				}

				for (int i = 0; i < bestPosIDs.length; i++) {
					iBestPosIDs.add(Integer.parseInt(bestPosIDs[i].toString()));
				}

				if (pruefeGemeinsam()) {

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							abzuege = new BezSonderabzuege(banfIds.size(),
									banfIds, preisGes, bestID, con, conL,
									teilGesamt, budget, gibtRechnung,
									kostenstelle, iBestPosIDs, ausgewaehlt,
									materialV, nettoStueck, fertigBez, menge);
							abzuege.pack();
							abzuege.setLocationRelativeTo(null);
							abzuege.setVisible(true);
						}

					});
				}

			}

		}// Button weiter

		if (e.getSource() == alle) {

			for (int i = 0; i < anzTabelle.getRowCount(); i++)
				anzTabelle.setValueAt(alleMark, i,
						anzTabelle.getColumnCount() - 1);

			anzTabelle.aktualisieren();

			if (alleMark)
				alle.setText("alle demarkieren");
			else
				alle.setText("alle markieren");

			alleMark = !alleMark;
		}
	}

	/**
	 * prueft, ob eine Kostenstelle mit Anteilen vorkommt (gemeinsam, EL/ET,
	 * MI/WI); wenn ja, wird die Klasse Anteile aufgerufen; wenn nein, wird die
	 * Klasse Sonderabzuege aufgerufen
	 * 
	 * @return ein boolean; true: es kommt keine Kostenstelle mit Anteilen vor;
	 *         false: es kommt min. 1 Kostenstelle mit Anteilen vor
	 */
	public boolean pruefeGemeinsam() {

		gemeinsam = 0;
		ele = 0;
		wimi = 0;
		warNichtDrin = true;

		welcheKostenstelle.clear();
		woImVector.clear();

		for (i = 0; i < kostenstelle.size(); i++) {

			if (kostenstelle.get(i).equals("gemeinsam")) {
				gemeinsam++;
				woImVector.add(i);
				welcheKostenstelle.add(1);

			}
			if (kostenstelle.get(i).equals("EL/ET")) {
				ele++;
				woImVector.add(i);
				welcheKostenstelle.add(2);

			}
			if (kostenstelle.get(i).equals("WI/MI")) {
				wimi++;
				woImVector.add(i);
				welcheKostenstelle.add(3);

			}
		}

		if (gemeinsam != 0 || ele != 0 || wimi != 0) {
			warNichtDrin = false;

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					anteil = new BezAnteile(banfIds.size(), banfIds, preisGes,
							bestID, con, conL, teilGesamt, budget,
							gibtRechnung, kostenstelle, iBestPosIDs, gemeinsam,
							ele, wimi, woImVector, welcheKostenstelle,
							ausgewaehlt, materialV, nettoStueck, fertigBez, menge);
					anteil.pack();
					anteil.setLocationRelativeTo(null);
					anteil.setVisible(true);
					anteil.zaehler++;
				}

			});

		}
		return warNichtDrin;
	}

}
