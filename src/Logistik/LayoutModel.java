package Logistik;

import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.*;

/**
 * Klasse, welche das aussehen und die Funktionsweise von JTables verwaltet
 */

public class LayoutModel implements TableModel {
	private Vector rows = new Vector();
	private Class[] classes;
	private Vector listeners = new Vector();
	private String[] titles;

	public LayoutModel(String[] titles, Class[] classes) {
		this.titles = titles;
		this.classes = classes;
	}

	public void addRow(LayoutRow row) {
		// Das wird der Index des Vehikels werden
		int index = rows.size();
		rows.add(row);

		// Jetzt werden alle Listeners benachrichtigt

		// Zuerst ein Event, "neue Row an der Stelle index" herstellen
		TableModelEvent e = new TableModelEvent(this, index, index,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);

		// Nun das Event verschicken
		for (int i = 0, n = listeners.size(); i < n; i++) {
			((TableModelListener) listeners.get(i)).tableChanged(e);
		}
	}

	// Die Anzahl Columns
	public int getColumnCount() {
		return titles.length;
	}

	public int indexOf(String s) {
		for (int i = 0; i < titles.length; i++) {
			if (titles[i].equals(s)) {
				return i;
			}
		}
		return -1;
	}

	// Die Anzahl Vehikel
	public int getRowCount() {
		return rows.size();
	}

	// Die Titel der einzelnen Columns
	public String getColumnName(int column) {
		return titles[column];
	}

	// Der Wert der Zelle (rowIndex, columnIndex)
	public Object getValueAt(int rowIndex, int columnIndex) {
		LayoutRow row = (LayoutRow) rows.get(rowIndex);
		if (classes[columnIndex] == JTextField.class) {
			if (row.getData(columnIndex).toString() == "false") {
				return "";
			}
		}

		return row.getData(columnIndex);
	}

	// Eine Angabe, welchen Typ von Objekten in den Columns angezeigt werden
	// soll
	public Class getColumnClass(int columnIndex) {
		if (classes[columnIndex] == Date.class) {
			return String.class;
		}
		if (classes[columnIndex] == JTextField.class) {
			return String.class;
		}
		return classes[columnIndex];
	}

	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (classes[columnIndex] == Boolean.class) {
			return true;
		}
		if (classes[columnIndex] == JTextField.class) {
			return true;
		}
		return false;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		LayoutRow r = (LayoutRow) rows.get(rowIndex);
		if (getColumnClass(columnIndex) == Boolean.class) {
			for (int i = 0; i < getColumnCount(); i++) {
				if (columnIndex != i && getColumnClass(i) == Boolean.class
						&& getValueAt(rowIndex, i) == Boolean.TRUE) {
					r.setValue(Boolean.FALSE, i);
				}
			}
		}
		r.setValue(aValue, columnIndex);
	}

	public LayoutRow[] getRows() {
		LayoutRow[] r = new LayoutRow[rows.size()];
		for (int i = 0; i < r.length; i++) {
			r[i] = (LayoutRow) rows.get(i);
		}
		return r;
	}

	public Color getColor(int row, int column) {
		LayoutRow rowz = (LayoutRow) rows.get(row);
		return rowz.getColor();
	}
}