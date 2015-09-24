package Logistik;

import java.util.*;
import java.awt.*;

/**
 * Beinhaltet die Daten einer Reihe einer Tabelle
 */

public class LayoutRow {
	private Vector data;
	private Color color;

	public LayoutRow() {
		data = new Vector();
		color = Color.WHITE;
	}

	public Object getData(int index) {
		if (index > data.size() - 1) {
			return null;
		}
		return data.get(index);
	}

	public void setValue(Object obj, int index) {
		Object[] ob = new Object[data.size()];
		for (int i = 0; i < ob.length; i++) {
			ob[i] = data.get(i);
		}
		data = null;
		data = new Vector();
		ob[index] = obj;
		for (int i = 0; i < ob.length; i++) {
			data.add(ob[i]);
		}
	}

	public void setColor(Color c) {
		color = c;
	}

	public void add(Object o) {
		data.add(o);
	}

	public Color getColor() {
		return color;
	}

	public int size() {
		return data.size();
	}
}