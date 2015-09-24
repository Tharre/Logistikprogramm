package Logistik;

import javax.swing.*;

import java.awt.*;

/**
 * Fenster, welches beim Laden der Daten angezeigt wird
 */

public class Laden extends JFrame {
	private int num;
	private JLabel lbl;

	public Laden() {
		setSize(200, 50);
		setLayout(new GridLayout());
		num = 5;
		lbl = new JLabel(getString(), SwingConstants.CENTER);
		add(lbl);
		getContentPane().setBackground(Color.BLACK);
		lbl.setBackground(Color.BLACK);
		lbl.setForeground(Color.WHITE);
		lbl.setOpaque(true);
		setUndecorated(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public String getString() {
		String s = "Bitte warten...";
		for (int i = 0; i < num; i++) {
			s += ".";
		}
		return s;
	}
}