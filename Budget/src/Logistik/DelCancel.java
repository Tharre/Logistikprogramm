package Logistik;

import javax.swing.*;

/**
 * Meldung bei Abbruch des L�schvorganges. Verwendet bei Betsellablauf
 */
public class DelCancel extends JPanel {
	/**
	 * Textausgabe
	 */
	public JLabel titel = new JLabel("Der L�schvorgang wurde abgebrochen!");

	public DelCancel() {
		add(titel);
	}
}