package Logistik;

import javax.swing.*;
import java.awt.*;
import java.net.*;

/**
 * Beinhaltet Navibuttons zum Navigieren zwischen den Menus
 */

public class LayoutNaviPanel extends JPanel {
	private Image bg;
	private ButtonGroup g;

	public LayoutNaviPanel() {
		g = new ButtonGroup();
		URL u = this.getClass().getResource("navipanel.gif");
		bg = new ImageIcon(u).getImage();
		setPreferredSize(new Dimension(1, 30));
	}

	public void addButton(JToggleButton btn) {
		g.add(btn);
		add(btn);
	}
	
	public void addButtonOhneToggle(JButton btn)
	{
		g.add(btn);
		add(btn);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	}
}