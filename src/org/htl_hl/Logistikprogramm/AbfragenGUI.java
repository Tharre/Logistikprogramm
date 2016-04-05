package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.*;


public class AbfragenGUI implements Tab {
	private final String[] SABF =
			{"Welche Materialien gibt es?", "Welche Materialien wurden zwischen den Daten x und y erstellt?",
			 "Welches Material hat die Bundesnummer x?", "Welche Materialen sind in der Inventurgruppe x?",
			 "größter / kleinster Preis für Material x", "Welche Materialien können bei der Firma x bestellt werden?",
			 "Bei welchen Firmen kann man Material x bestellen?", "Alle Banfs von User x anzeigen",
			 "Banfs zu einer Bestellung", "Bestellung zu einer Banf"}; // Standardstrings zu den Abfragen
	private JLabel[][] platzhalter;
	private final int SPALTEN = 2; // Frei wählbarer Wert, wieviel Spalten man haben möchte
	private final int GRIDX = SPALTEN * 2 + 1; // Breite des Rasters

	public String toString() {
		return getName();
	}

	@Override
	public String getName() {
		return "Abfragen";
	}

	@Override
	public JPanel getContent() {
		final int GRIDY = (SABF.length * 2 + 2) / SPALTEN; // Höhe des Rasters
		platzhalter = new JLabel[GRIDX][GRIDY];
		JButton[][] btAbf = new JButton[SPALTEN][(SABF.length + 1) / SPALTEN];

		for (int i = 0; i < SABF.length; i++) {
			SABF[i] = "<html><font size=\"5\">" + SABF[i] + "</font></html>";
			// damit die Buttons von alleine mehrzeilig werden können, und die Schrift größer ist
			System.out.println(SABF[i]);
		}

		int si = 0;
		for (int i = 0; i < SPALTEN; i++) {
			for (int j = 0; j < btAbf[0].length; j++) {
				btAbf[i][j] = new JButton(SABF[si]);
				si++;
			}
		}

		// Buttons zu den Abfragen
		JPanel panel = new JPanel();
		Container c = panel;
		c.setLayout(new GridLayout(GRIDY, GRIDX));

		for (int i = 0; i < GRIDY; i++) {
			if (i % 2 == 0)
				zeileZeichnen(c, i);
			else
				for (int j = 0; j < GRIDX; j++) {
					if (j % 2 == 0)
						elementZeichnen(c, i, j);
					else
						c.add(btAbf[j / 2][i / 2]);
				}
		}

		return panel;
	}

	private void zeileZeichnen(Container c, int y) {
		for (int j = 0; j < GRIDX; j++) {
			elementZeichnen(c, y, j);
		}
	}

	private void elementZeichnen(Container c, int y, int x) {
		platzhalter[x][y] = new JLabel();
		c.add(platzhalter[x][y]);
	}
}
