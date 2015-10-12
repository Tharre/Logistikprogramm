package Budget;

import javax.swing.*;

import java.awt.*;
import java.net.URL;

/**
 *In der Klasse Startseite wird die Startseite erstellt
 *<p>
 * Title: Startseite
 * 
 * @author Haupt, Liebhart
 **/
public class Startseite extends JPanel {
	/** Namen der Programmiererinnen **/
	private JLabel namen;
	/** Titel des Projektes **/
	private JLabel titel;
	/** Bild, das im Hintergrund erscheint */
	private Image image;
	/** URL **/
	private URL u;

	/**
	 * Konstruktor
	 */
	public Startseite() {
		setOpaque(true);
		u = this.getClass().getResource("../Hintergrund.png");
		image = new ImageIcon(u).getImage();

		namen = new JLabel("Marianne Haupt, Stefanie Liebhart", JLabel.CENTER);
		titel = new JLabel("BUDGETPROGRAMM", JLabel.CENTER);

		titel.setFont(new Font("SansSerif", Font.BOLD, 50)); // Schrift
		// formatieren
		namen.setFont(new Font("Arial", Font.PLAIN, 25)); // Schrift formatieren

		setLayout(new GridLayout(4, 1)); // Layout
		add(new JLabel(""));
		add(titel);
		add(namen);

	}

	/**
	 * zeichnet das Bild im Hintergrund
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

}
