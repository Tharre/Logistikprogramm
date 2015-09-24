package Budget;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *fuegt die Checkboxen zur Tabelle mit den Bestellpositionen, die man bezahlen
 * moechte
 *<p>
 * Title: TabelleMitButtons
 * 
 *@author Haupt, Liebhart
 **/
public class TabelleButtons extends JPanel implements ItemListener {

	// GridBagLayout
	/** GridBagLayout - Objekt **/
	private GridBagLayout gridbag = new GridBagLayout();
	/** wird fuer das Verwenden eines GridBag-Layouts benoetigt **/
	private GridBagConstraints c;

	// Labels
	/** Hilfs-Label **/
	private JLabel leer = new JLabel("");
	/** das Label "Auswahl" **/
	private JLabel leer2 = new JLabel("Auswahl");

	// boolean
	/**
	 * true: die Checkbox wurde 2 mal angeklickt und wird somit demarkiert;
	 * false: die Checkbox wurde nur 1 mal geklickt und wird markiert
	 **/
	private boolean geloescht;
	/**
	 * true: die Bestellposition ist lieferbar; false: die BestPos ist nicht
	 * lieferbar
	 **/
	private boolean lieferUnlieferbar;

	// int
	/** wie viele Bestellpositionen gibt es zu der Bestellung **/
	private int anzahl;
	/** gibt an, wie viele Bestellpositionen bezahlbar sind **/
	private int anzahlZuBezahlen = 0;
	/** welchen Bezahl-Status hat die jeweilige Bestellposition **/
	private int statusBez;

	// Vectoren
	/** gibt an, wo die Bestellpositionen stehen, die angeklickt wurden **/
	private Vector angeklickt = new Vector();
	/**
	 * die Bestellposition an dieser Stelle ist lieferbar; false: die BestPos an
	 * dieser Stelle ist nicht lieferbar
	 **/
	private Vector lieferbar = new Vector();

	// sonstiges
	/**
	 * ein Checkboxarray, dessen Groeße davon abhaengt, wie viele
	 * Bestellpositionen es zu der Bestellung gibt
	 **/
	private JCheckBox check[];

	// JScrollPane scroll = new JScrollPane();

	/**
	 * Kostruktor
	 */
	public TabelleButtons() {
		// this.scroll = scroll;

		setLayout(gridbag);
		leer2.setFont(new Font("Sans Serif", Font.BOLD, 15));
		leer2.setOpaque(true);

	}

	/**
	 * zeigt Bestellpositionen einer komplett noch nicht bezahlten Bestellung an
	 * 
	 * @param anzahl
	 *            wie viele Bestellpositionen gibt es zu der Bestellung
	 * @param lieferbar
	 *            true: die Bestellposition an dieser Stelle ist lieferbar;
	 *            false: die BestPos an dieser Stelle ist nicht lieferbar
	 */
	public void anzeigen(int anzahl, Vector lieferbar) {
		removeAll();
		angeklickt.clear();
		
		//Anzahl der Bestellpositionen
		this.anzahl = anzahl; 
		this.lieferbar = lieferbar;
		c = new GridBagConstraints();

		// GridBagLayout festlegen 
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;

		gridbag.setConstraints(leer2, c);
		add(leer2);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;

		check = new JCheckBox[anzahl];

		// Checkboxes anklickbar oder unanklickbar setzen
		// und einfügen
		for (int i = 0; i < anzahl; i++) {
			anzahlZuBezahlen++;
			check[i] = new JCheckBox();
			check[i].addItemListener(this);
			gridbag.setConstraints(check[i], c);
			add(check[i]);

			lieferUnlieferbar = Boolean.parseBoolean(lieferbar.get(i)
					.toString());
			if (!lieferUnlieferbar) // nicht lieferbar
			{
				check[i].setEnabled(false);
				anzahlZuBezahlen--;
			}
		}

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(leer, c);
		add(leer);

		setVisible(true);
		repaint();

	}

	/**
	 * ueberprueft, welche Bestellpositionen angeklickt wurden
	 * 
	 * @return ein Vector mit Zahlen, an welcher Stelle die angeklickten
	 *         Bestellpositionen stehen
	 */
	public Vector ueberpruefe() {
		return angeklickt;
	}

	/**
	 * lauscht, ob eine Checkbox angeklickt wurde
	 * 
	 * @param e
	 *            ein ItemEvent-Objekt
	 */
	public void itemStateChanged(ItemEvent e) {

		for (int i = 0; i < anzahl; i++) {
			if (e.getSource() == check[i]) {
				geloescht = false;
				for (int k = 0; k < angeklickt.size(); k++) {
					if (angeklickt.get(k).toString().equals("" + i)) {
						angeklickt.remove(k);
						geloescht = true;
					}
				}
				if (!geloescht)
					angeklickt.add(i);
			}
		}
	}

	/**
	 * markiert alle Bestellpositionen, die man zahlen kann
	 * 
	 * @param anzahl
	 *            wie viele Bestellpositionen sind vorhanden und auch zahlbar
	 */
	public void alleMarkieren(int anzahl) {
		angeklickt.clear();
		for (int i = 0; i < anzahl; i++) {
			check[i].setSelected(false);

		}
		angeklickt.clear();
		for (int i = 0; i < anzahl; i++) {
			if (check[i].isEnabled()) {
				check[i].setSelected(true);
			}
		}
	}

	/**
	 * prueft, ob alle Positionen markiert wurden oder ob welche uebrig bleiben
	 * 
	 * @return true: Gesamtrechnung; false: Teilrechnung
	 */
	public boolean teilGesamt() {
		if (angeklickt.size() == anzahlZuBezahlen) {
			return true; // Gesamtrechnung
		} else {
			return false; // Teilrechnung
		}
	}

	/**
	 * fuegt die Checkboxes zu Bestellpositionen hinzu, die zu einer teilweise
	 * bezahlten Bestellung gehoeren
	 * 
	 * @param anzahl
	 *            wie viele Bestellpositionen gibt es
	 * @param bezahlt
	 *            wo stehen die Bestellpositionen, die schon bezahlt wurden
	 * @param lieferbar
	 *            wo stehen die Bestellpositionen, die nicht lieferbar sind
	 */
	public void anzeigenTW(int anzahl, Vector bezahlt, Vector lieferbar) {
		removeAll();
		angeklickt.clear();
		this.anzahl = anzahl;
		c = new GridBagConstraints();

		// Buttons hinzufuegen
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;

		gridbag.setConstraints(leer2, c);
		add(leer2);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;

		check = new JCheckBox[anzahl];

		// Checkboxes anklickbar oder unanklickbar machen
		for (int i = 0; i < anzahl; i++) {
			anzahlZuBezahlen++;
			check[i] = new JCheckBox();
			check[i].addItemListener(this);
			gridbag.setConstraints(check[i], c);
			add(check[i]);

			statusBez = Integer.parseInt(bezahlt.get(i).toString());
			lieferUnlieferbar = Boolean.parseBoolean(lieferbar.get(i)
					.toString());
			if ((statusBez == 4) || (statusBez == 0) || (!lieferUnlieferbar)) // komplett
			// bezahlt
			// oder
			// nicht
			// lieferbar
			{
				anzahlZuBezahlen--;
				check[i].setEnabled(false);
			}
		}

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(leer, c);
		add(leer);

		setVisible(true);
		repaint();
	}
}
