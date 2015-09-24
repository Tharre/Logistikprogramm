package Budget;

import java.awt.*;
import javax.swing.*;
import java.util.Random;

/**
 *In der Klasse DiagrammPanel wird ein Diagramm erstellt
 *<p>
 * Title: DiagrammPanel
 * 
 * @author Haupt, Liebhart
 **/

public class DiagrammPanel extends JPanel {
	// double[]
	/** Werte, die im Diagramm dargestellt werden **/
	private double[] werte;
	/** die Grade der einzelnen Kreissektoren **/
	private double[] grad;

	// sonstiges
	/** Farbenarray für das Diagramm **/
	private Color[] farben;
	/** Zufall **/
	private Random r;
	/** String[] mit den Namen fuer die Legende **/
	private String[] legende;
	/** y-Koordinaten, wo die Legende gezeichnet wird **/
	private int[] hilfy;

	// int
	/** Werte, der bisher gezeichneten Grade **/
	private int hilf;
	/** x-Koordinate, wo die Legende gezeichnet wird **/
	private int hilfx = 50;
	/** Wert,an dem begonnen wird, die Legende zu zeichnen **/
	private int startwert;
	/** Laufvariable **/
	private int k;
	/** Grade eines Kreises **/
	private int gradKreis = 360;

	// double
	/** Summe der Grade **/
	private double summeDerWerte;
	/** Hilfsvariable, mit der die Grad berechnet werden **/
	private double mulitplikative;

	/**
	 * Konstruktor
	 */
	public DiagrammPanel() {

	}

	/**
	 * In der Methode anzeigen werden die Groessen der Arrays definiert und
	 * andere Methoden aufgerufen
	 * 
	 * @param werte
	 *            Werte, die im Diagramm dargestellt werden
	 * @param legende
	 *            Legende fuer das Diagramm
	 */
	public void anzeigen(double[] werte, String[] legende, int welcheFarbe) {
		// Werte werden uebergeben
		this.werte = werte;
		this.legende = legende;

		grad = new double[werte.length]; // Gradearray soll genauso gross wie
		// das Wertearray sein
		hilfy = new int[grad.length]; // hilyarray soll genauso gross wie das
		// Gradarray sein
		farben = new Color[werte.length]; // man benoetigt soviel farben, wie
		// Werte

		summeDerWerte = 0;

		befuelleArrays();
		
		if (welcheFarbe == 0)
			erzeugeZufallsFarbe();
		else if (welcheFarbe == 1)
			erzeugeFarbeUT3();
		else
			erzeugeFarbeLMB();
		
		berechneGrad();
	}

	/**
	 * Die Methode paintComponent zeichnet das Diagramm und die Legende
	 * 
	 * @param g
	 *            von der Klasse Graphics
	 **/
	public void paintComponent(Graphics g) {
		for (k = 0; k < grad.length - 1; k++) {
			g.setColor(farben[k]);
			g
					.fillArc((int) (getWidth() / 4), 70, 250, 250, hilf,
							(int) grad[k]);

			// die Grade werden immer aufsummiert
			hilf = hilf + (int) grad[k];

			g.setFont(new Font("SansSerif", Font.BOLD, 20));

		}

		// der letzte Sektor soll den Kreis fuellen (wegen Rundungsfehler wird
		// der Kreis sonst nicht ganz voll)
		g.setColor(farben[k]);
		g.fillArc((int) (getWidth() / 4), 70, 250, 250, hilf, (360 - hilf));

		hilf = 0;
	}

	/**
	 * Die Methode erzeugeZufalssFarbe erzeugt Zufallsfarben
	 **/
	private void erzeugeZufallsFarbe() {
		r = new Random();

		for (int y = 0; y < grad.length; y++)
			farben[y] = new Color(r.nextInt(256), r.nextInt(256), r
					.nextInt(256)); // Frabenarray wird befuellt
	}

	private void erzeugeFarbeUT3() {
		farben[0] = new Color(234,208,30);
		farben[1] = Color.RED;
		farben[2] = Color.GREEN;
		farben[3] = Color.BLUE;

		r = new Random();

		for (int y = 4; y < grad.length; y++)
			farben[y] = new Color(r.nextInt(256), r.nextInt(256), r
					.nextInt(256)); // Frabenarray wird befuellt

	}

	private void erzeugeFarbeLMB() {
		farben[0] = new Color(234,208,30);
		farben[1] = Color.RED;
		farben[2] = Color.GREEN;
		farben[3] = Color.BLUE;
		farben[4] = new Color(171,14,21);

		r = new Random();

		for (int y = 5; y < grad.length; y++)
			farben[y] = new Color(r.nextInt(256), r.nextInt(256), r
					.nextInt(256)); // Frabenarray wird befuellt

	}

	/**
	 * Die Methode berechneMultiplikative berechnet einen
	 * Hilfswert(Multiplikative) um die Grad der einzelnen Sektoren zu berechnen
	 **/
	private void berechneMultiplikative() {
		mulitplikative = gradKreis / summeDerWerte; // 360° dividiert durch die
		// Summer der aller Werte
	}

	/**
	 * Die Methode berechneGrad berechnet die Grade der einzelnen Sektoren
	 **/
	private void berechneGrad() {
		berechneMultiplikative(); // zuerst muss die Multiplikative berechnet
		// werden

		for (int j = 0; j < grad.length; j++)

			grad[j] = mulitplikative * werte[j]; // Grade werden berechnet

	}

	/**
	 * Die Methode befuelleArrays befuellt Arrays
	 **/
	private void befuelleArrays() {
		startwert = 0;
		for (int i = 0; i < hilfy.length; i++) {
			startwert = startwert + 50; // dass jede Zeile weiter unten ist
			hilfy[i] = startwert; // befuellt das hilfy- Array mit y-
			// Koordinaten
		}

		for (int i = 0; i < werte.length; i++)
			summeDerWerte = summeDerWerte + werte[i]; // berechnet die Summe

		// der Werte
	}

	/**
	 * Gibt das Farben-Array mit den Zufallsfarben zurück
	 * 
	 * @return Ein Farben-Array
	 */
	public Color[] getFarben() {

		return farben;
	}
}
