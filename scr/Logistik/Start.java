package Logistik;

import javax.swing.*;

/**
 * Startet das Programm Wird vom Browser aufgerufen
 */

public class Start extends JApplet {
	@Override
	public void init() {
		final Laden l = new Laden();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login("Login", /* c.getCodeBase()+"" */"");
				l.dispose();
			}
		});
	}

}
