package Logistik;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;

/**
 * Klasse zum Darstellen von tabellenähnlichen Strukturen in JPanels
 */

public class LayoutBanf implements LayoutManager {

	protected Vector comps;
	protected Vector size;
	protected int spalten;

	public LayoutBanf(int spalten) {
		this.spalten = spalten;
		size = new Vector();
		comps = new Vector();
	}

	public void addLayoutComponent(String s, Component comp) {
		size.add(s);
		comps.addElement(comp);
		((JComponent) comp).setBorder(BorderFactory.createLineBorder(new Color(
				180, 180, 180)));
	}

	public void removeLayoutComponent(Component comp) {
		int i = comps.indexOf(comp);
		if (i != -1) {
			comps.removeElementAt(i);
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

	private int getRestWidth(Container parent) {
		int width = 0;
		for (int i = 0; i < size.size(); i++) {
			String s = (String) size.get(i);
			if (s.indexOf('*') == -1) {
				width += getWidthOf(i, parent);
			}
		}
		return parent.getWidth() - width;
	}

	private int getWidthOf(int i, Container parent) {
		String w = (String) size.get(i);
		if (w.equals("")) {
			return 0;
		}
		if (w.indexOf('*') != -1) {
			return getRestWidth(parent);
		}
		if (w.indexOf('%') == -1) {
			return Integer.parseInt(w);
		}
		if (w.indexOf('*') == -1) {
			int z = Integer.parseInt(w.substring(0, w.length() - 1));
			return (parent.getWidth()) * z / 100;
		}
		return 0;
	}

	private int getHeight(int zeile) {
		int maxHeight = 0;
		for (int i = zeile * spalten; i < zeile * spalten + spalten; i++) {
			if (i < comps.size() - 1) {
				Component c = (Component) comps.get(i);
				if (c.getPreferredSize().height > maxHeight) {
					maxHeight = c.getPreferredSize().height;
				}
			}
		}
		return maxHeight;
	}

	public void layoutContainer(Container parent) {
		Component c;
		int y = 0;
		int x = 0;
		int u = 1;
		int h = 0;
		int zeile = 0;
		for (int i = 0; i < parent.getComponentCount(); i++) {
			c = parent.getComponent(i);
			int w = getWidthOf(u - 1, parent);
			h = getHeight(zeile);
			c.setBounds(x, y, w, h);
			x += w;
			if (u == spalten) {
				u = 0;
				zeile++;
				x = 0;
				y += h;
			}
			u++;
		}
		parent.setPreferredSize(new Dimension(parent.getWidth(), y + h));
	}

}
