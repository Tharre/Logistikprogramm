package Logistik;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;

/**
 * Hilfsklasse zum Darstellen von hierarchisch aufgebauten Daten
 */

public class LayoutLevel implements LayoutManager {

	protected Vector comps;
	protected Vector level;
	protected Vector buttons;
	protected Vector visible;

	public LayoutLevel() {
		level = new Vector();
		comps = new Vector();
		buttons = new Vector();
		visible = new Vector();
	}

	public void setVisible(int i, boolean b) {
		visible.setElementAt(new Boolean(b), i);
		((JComponent) comps.get(i)).setVisible(b);
	}

	public void addLayoutComponent(String s, Component comp) {
		level.addElement(s);
		comps.addElement(comp);
		visible.add(new Boolean(false));
	}

	public void removeLayoutComponent(Component comp) {
		int i = comps.indexOf(comp);
		if (i != -1) {
			comps.removeElementAt(i);
			level.removeElementAt(i);
			buttons.removeElementAt(i);
		}
	}

	public Dimension preferredLayoutSize(Container parent) {
		return minimumLayoutSize(parent);
	}

	public Dimension minimumLayoutSize(Container parent) {
		Component c;
		Dimension d = new Dimension();
		Dimension componentDim;

		for (int i = 0; i < parent.getComponentCount(); i++) {
			c = parent.getComponent(i);
			componentDim = c.getMinimumSize();
			d.width = Math.max(d.width, componentDim.width);
			d.height += componentDim.height;
		}
		return d;
	}

	private int getHeight(int zeile) {
		Component c = (Component) comps.get(zeile);
		return c.getPreferredSize().height;
	}

	public int getLvl(int z) {
		return Integer.parseInt((String) level.get(z));
	}

	public int lvlWidth() {
		return 30;
	}

	public void layoutContainer(Container parent) {
		Component c;
		int y = 0;
		int x = 0;
		int maxWidth = 0;
		for (int i = 0; i < parent.getComponentCount(); i++) {
			if (((Boolean) visible.get(i)).booleanValue() == Boolean.TRUE) {
				x = Integer.parseInt((String) level.get(i)) * lvlWidth();
				c = parent.getComponent(i);
				c.setBounds(x, y, c.getPreferredSize().width, c
						.getPreferredSize().height);
				y += getHeight(i) - 15;
				int w = x + c.getPreferredSize().width;
				if (w < maxWidth) {
					maxWidth = w;
				}
			}
		}
		parent.setPreferredSize(new Dimension(maxWidth, (int) (y * 1.5)));
	}

}
