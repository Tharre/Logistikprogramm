package Budget;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;

/**
 *In der Klasse Diagramm werden die Daten fuer das Diagramm ausgelesen und das
 * Layout erstellt
 *<p>
 * Title: Diagramm
 * 
 * @author Haupt, Liebhart
 **/

public class Diagramm extends JPanel implements ActionListener {
	/** Werte, die im Diagramm dargestellt werden **/
	private double[] werte;
	/** Diagramm **/
	private DiagrammPanel diagramm = new DiagrammPanel();
	/** Titel(Label) des Diagramms **/
	private JLabel titelL;
	/** Titel(String) des Diagramms **/
	private String titel;
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;
	/** Namen fuer die Legende zum Diagramm **/
	private String[] legende;
	/** es werden die Abteilungen gespeichert **/
	private Vector<String> legendeV = new Vector<String>();
	/**
	 * gibt an, ob eine Abfrage nach dem Status (true) oder nach Abteilungen
	 * (false) getätigt wurde
	 **/
	private boolean status;
	/** Objekt von der Klasse Legende **/
	private Legende legendeNeu;

	private JButton sonderbudget = new JButton("Sonderbudget wählen");
	private JPanel hilf = new JPanel();

	private JComboBox namen = new JComboBox();
	private boolean aktuell;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 */
	public Diagramm(Connection con, Connection conL) {
		di = new DatenImport(con, conL);
	}

	/**
	 * Die Methode nachStatus liesst die Daten fuer das Diagramm nach Status aus
	 */
	public void nachStatus(String name, String tabelle, int zahl, String nameS,
			boolean aktuell) {
		titel = name;
		java.util.Date heute = new java.util.Date();

		heute = new Date();
		int von = heute.getYear();
		int bis = von + 1;

		if (!aktuell) {
			von = von - 1;
			bis = von + 1;
		}
		Date datumV = new Date(von, 0, 1);
		Date datumB = new Date(bis, 0, 1);
		long vonD = datumV.getTime();
		long bisD = datumB.getTime();

		werte = di.getDatenfuerStatus(tabelle, zahl, nameS, vonD / 1000,
				bisD / 1000);
		legende = new String[3];

		legende[0] = "ausgegeben";
		legende[1] = "gesperrt";
		legende[2] = "verfuegbar";

		status = true;
		erstelleLayout(0);
	}

	public void erstelleLayoutSonderbudget(boolean aktuell) {

		removeAll();
		validate();
		repaint();

		this.aktuell = aktuell;

		sonderbudget.addActionListener(this);
		Vector namenSonderbudget = new Vector();
		namenSonderbudget = di.getSonderbudgets();

		namen.removeAllItems();
		for (int i = 0; i < namenSonderbudget.size(); i++) {

			namen.addItem(namenSonderbudget.get(i).toString());

		}

		hilf.add(new JLabel("Bitte wählen Sie ein Sonderbudget aus."));
		hilf.add(namen);
		hilf.add(sonderbudget);

		add(hilf, BorderLayout.CENTER);

		validate();
		repaint();

	}

	/**
	 * Die Methode nachAbteilung liesst die Daten fuer das Diagramm nach
	 * Abteilungen aus
	 */
	public void nachAbteilung(String name, String befehl1, int welcheFarbe,boolean aktuell,String budget) {
		titel = name;
		legendeV.clear();
		legendeV = di.namenAbteilungen(befehl1);

		java.util.Date heute = new java.util.Date();

		heute = new Date();
		int von = heute.getYear();
		int bis = von + 1;

		if (!aktuell) {
			von = von - 1;
			bis = von + 1;
		}
		Date datumV = new Date(von, 0, 1);
		Date datumB = new Date(bis, 0, 1);
		long vonD = datumV.getTime();
		long bisD = datumB.getTime();
		
		werte = new double[legendeV.size()];
		for (int i = 0; i < werte.length; i++) {
			werte[i] = di.getDatenFuerGrafikAbteilung(legendeV.get(i),
					vonD / 1000, bisD / 1000,budget);
		}
		legende = new String[legendeV.size()];
		for (int i = 0; i < legendeV.size(); i++)
			legende[i] = legendeV.get(i);

		status = false;
		erstelleLayout(welcheFarbe);
	}

	/**
	 * Die Methode erstelleLayout erstellt nun das Layout fuer das Diagramm
	 */
	public void erstelleLayout(int welcheFarbe) {
		titelL = new JLabel(titel, JLabel.CENTER); // Label erzeugen mit dem

		titelL.setFont(new Font("SansSerif", Font.BOLD, 30)); // Schrift
		// formatieren
		diagramm.anzeigen(werte, legende, welcheFarbe);
		legendeNeu = new Legende(legende, diagramm, werte, status);

		// BorderLayout erzeugen
		setLayout(new BorderLayout());

		// Label und Diagramm wird hinzugefuegt
		removeAll();
		validate();

		add(legendeNeu, BorderLayout.WEST);
		add(diagramm, BorderLayout.CENTER);
		add(titelL, BorderLayout.NORTH);

		validate();
		repaint();

	}

	public void actionPerformed(ActionEvent e) {

		String name = namen.getSelectedItem().toString();

		nachStatus("aktueller Stand",

		"sonderbudget", 3, name, aktuell);

		hilf.removeAll();
		validate();
		repaint();

	}

}
