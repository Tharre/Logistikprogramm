package Logistik;

import javax.swing.*;

/**
 * Meldung bei Abbruch des Löschvorganges. Verwendet bei Betsellablauf
 */
public class DelCancel extends JPanel {
	/**
	 * Textausgabe
	 */
	public JLabel titel = new JLabel("Der Löschvorgang wurde abgebrochen!");

	public DelCancel() {
		add(titel);
	}
}