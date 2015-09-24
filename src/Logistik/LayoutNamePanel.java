package Logistik;

import javax.swing.*;
import java.awt.*;
import java.net.*;

/**
 * Zeigt das aktuelle Übermenu an
 */

public class LayoutNamePanel extends JPanel {
	private Image bg;
	private JLabel l_text;

	public LayoutNamePanel(String image, int height) {
		URL u = this.getClass().getResource("" + image);
		bg = new ImageIcon(u).getImage();
		setPreferredSize(new Dimension(1, height));
		l_text = new JLabel("");
		add(l_text);
	}

	public void setText(String text) {
		l_text.setText(text);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	}
}