package Budget;

import javax.swing.*;

import java.awt.*;
import java.net.URL;

/**
 *In der Klasse Namensleiste wird die Leiste erstellt, in der man erkennen kann,
 * wo man sich gerade befindet
 *<p>
 * Title: Namensleiste
 * 
 *@author Haupt Marianne, Liebhart Stefanie
 **/
public class Namensleiste extends JPanel {
	/** Label mit den Namen **/
	private JLabel nameL;
	/** Bild als Hintergrund **/
	private Image image;
	/** URL **/
	private URL u;

	/**
	 * Konstruktor
	 */
	public Namensleiste() {
		setBackground(Color.RED); // Hintergrundfarbe

		nameL = new JLabel("Startseite", JLabel.CENTER); // erscheint gleich zu
		// Beginn
		u = this.getClass().getResource("res/navipanel.gif");
		image = new ImageIcon(u).getImage();

		add(nameL); // Label hinzufügen

	}

	/**
	 * Methode, um den Namen in der Titelleiste zu aendern
	 * 
	 * @param name
	 *            Ueberschrift, wo man sich befindet
	 **/
	public void aendereName(String name) {
		nameL.setText(name);
	}

	/**
	 * Methode um das Image hinzuzugefuegen
	 * 
	 * @param g
	 *            von der Klasse Graphics
	 **/
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
}
