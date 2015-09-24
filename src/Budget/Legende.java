package Budget;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

/**
 *Es wird die Legende erstellt für das Diagramm bei den Abfragen UT8-, UT3-,
 * LMB1- und LMB2- nach Status und die Abfragen UT3-, LMB1- und LMB2- nach
 * Abteilungen
 *<p>
 * Title: Legende
 * 
 *@author Haupt Marianne, Liebhart Stefanie
 **/
public class Legende extends JPanel {

	/** die Bezeichnungen, was die Diagrammstuecke bedeuten **/
	private String[] namen;
	/** die Ueberschriften der Spalten in der Legende **/
	private String[] ueberschriften = { "Farbe", "Bezeichnung", "Wert" };
	/**
	 * das DiagrammPanel-Objekt, auf dem unter anderem die Zufallsfarben
	 * gespeichert sind
	 **/
	private DiagrammPanel diagramm;
	/** ein Farbarray mit den Zufallsfarben fuer das Diagramm und die Legende **/
	private Color[] farben;
	/** die Werte fuer das Diagramm **/
	private double[] werte;
	/** ein Hilfs-Label **/
	private JLabel leer = new JLabel();
	/**
	 * true: es wird eine Abfrage "nach Status" ausgefuehrt; false: es wird eine
	 * Abfrage "nach Abteilungen" ausgefuehrt
	 **/
	private boolean status;

	/**
	 * Konstruktor
	 * 
	 * @param namen
	 *            die Bezeichnungen, was die Diagrammstuecke bedeutenn
	 * @param diagramm
	 *            das DiagrammPanel-Objekt, auf dem unter anderem die
	 *            Zufallsfarben gespeichert sind
	 * @param werte
	 *            die Werte fuer das Diagramm
	 * @param status
	 *            true: es wird eine Abfrage "nach Status" ausgefuehrt; false:
	 *            es wird eine Abfrage "nach Abteilungen" ausgefuehrt
	 */
	public Legende(String[] namen, DiagrammPanel diagramm, double[] werte,
			boolean status) {
		this.diagramm = diagramm;
		this.namen = namen;
		this.werte = werte;
		this.status = status;

		removeAll();

		farben = diagramm.getFarben();

		if (status)
			layoutStatus();
		else
			layoutNichtStatus();

	}

	/**
	 * wird aufgerufen, wenn eine Abfrage "... nach Status" ausgefuehrt wird
	 */
	public void layoutStatus() {
		removeAll();
		validate();
		repaint();
		setLayout(new GridLayout(namen.length * 3, 5));

		for (int i = 0; i < namen.length - 1; i++) {
			add(new JLabel(""));
			add(new JLabel(""));
			add(new JLabel(""));
			add(new JLabel(""));
			add(new JLabel(""));
		}

		// Ueberschriften
		add(new JLabel(""));

		for (int i = 0; i < ueberschriften.length; i++) {
			JLabel ueberschrift = new JLabel("" + ueberschriften[i],
					JLabel.CENTER);
			ueberschrift.setFont(new Font("Sans Serif", Font.BOLD, 15));
			ueberschrift.setBorder(new BevelBorder(2));
			add(ueberschrift);
		}
		add(new JLabel(""));

		// Farben, Bezeichnungen und Werte
		for (int i = 0; i < namen.length; i++) {
			add(new JLabel(""));

			JLabel farbe = new JLabel();
			farbe.setBackground(farben[i]);
			farbe.setOpaque(true);
			add(farbe);

			JLabel bez = new JLabel("" + namen[i], JLabel.CENTER);
			bez.setForeground(farben[i]);
			bez.setBorder(new LineBorder(farben[i], 2));
			bez.setFont(new Font("Sans Serif", Font.CENTER_BASELINE, 15));
			add(bez);

			JLabel wert = new JLabel("" + werte[i], JLabel.CENTER);
			wert.setForeground(farben[i]);
			wert.setBorder(new LineBorder(farben[i], 2));
			wert.setFont(new Font("Sans Serif", Font.CENTER_BASELINE, 15));
			add(wert);

			add(new JLabel(""));
		}

		for (int i = 0; i < namen.length; i++) {
			add(new JLabel(""));
			add(new JLabel(""));
			add(new JLabel(""));
			add(new JLabel(""));
			add(new JLabel(""));
		}

		validate();
		repaint();
	}

	/**
	 * wird aufgerufen, wenn eine Abfrage "... nach Abteilungen" ausgefuehrt
	 * wird
	 */
	public void layoutNichtStatus() {
		removeAll();

		setLayout(new GridLayout(namen.length + 2, 5));

		for (int i = 0; i < 5; i++) {
			add(new JLabel());
		}

		// Ueberschriften
		add(new JLabel(""));
		for (int i = 0; i < ueberschriften.length; i++) {
			JLabel ueberschrift = new JLabel("" + ueberschriften[i],
					JLabel.CENTER);
			ueberschrift.setFont(new Font("Sans Serif", Font.BOLD, 20));
			ueberschrift.setBorder(new BevelBorder(2));
			add(ueberschrift);
		}
		add(new JLabel(""));

		// Farben, Bezeichnungen und Werte
		for (int i = 0; i < namen.length; i++) {
			add(new JLabel(""));

			JLabel farbe = new JLabel();
			farbe.setBackground(farben[i]);
			farbe.setOpaque(true);
			add(farbe);

			JLabel bez = new JLabel("" + namen[i], JLabel.CENTER);
			bez.setForeground(farben[i]);
			bez.setBorder(new LineBorder(farben[i], 2));
			bez.setFont(new Font("Sans Serif", Font.CENTER_BASELINE, 15));
			add(bez);

			JLabel wert = new JLabel("" + werte[i], JLabel.CENTER);
			wert.setForeground(farben[i]);
			wert.setBorder(new LineBorder(farben[i], 2));
			wert.setFont(new Font("Sans Serif", Font.CENTER_BASELINE, 15));
			add(wert);

			add(new JLabel(""));
		}

		validate();
		repaint();

	}

}
