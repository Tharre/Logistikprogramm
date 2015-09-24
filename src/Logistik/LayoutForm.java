package Logistik;

import javax.swing.*;
import java.util.*;
import java.awt.*;

/**
 * Klasse zum Erstellen von Formularen In einem Formular kann die Ausrichtung
 * (links,mittig,rechts) festgelegt werden. Die Klasse stellt auch eine Methode
 * zum überprüfen der Daten und zum leeren des Formulars bereit
 */

public class LayoutForm extends JPanel {
	private int spalten;
	private int zeilen;
	private ArrayList comps;
	private ArrayList align;
	private ArrayList inputs;
	private int alignAct;

	public LayoutForm() {
		spalten = 1;
		zeilen = 1;
		setLayout(new GridLayout(zeilen, spalten));
		comps = new ArrayList();
		align = new ArrayList();
		inputs = new ArrayList();
	}

	public void addRadioButton(ButtonGroup bg)
	{
		left();
		comps.add(bg);
	}
	public void addItem(Component c) {
		comps.add(c);
		align.add(new Integer(alignAct));
		spalten = spalten + 1;
		resetLayout();
	}

	public void addInput(JComponent i) {
		inputs.add(i);
		addItem(i);
	}

	public void addLeftInput(Input i) {
		left();
		addInput(i);
	}

	public void addLeftSelect(SelectInput i) {
		left();
		addInput(i);
	}

	public void addLeftSelectL(SelectInputLevel i) {
		left();
		addInput(i);
	}

	public void addRightInput(Input i) {
		right();
		addInput(i);
	}

	public void addCenterInput(Input i) {
		center();
		addInput(i);
	}

	public void br() {
		zeilen = zeilen + 1;
	}

	public void resetLayout() {
		removeAll();
		setLayout(null);
		setLayout(new GridLayout(zeilen, spalten));
		for (int i = 0; i < comps.size(); i++) {
			Integer a = (Integer) (align.get(i));
			JPanel p = new JPanel(new FlowLayout(a));
			p.add((Component) (comps.get(i)));
			add(p);
		}
	}

	public void right() {
		alignAct = FlowLayout.RIGHT;
	}

	public void center() {
		alignAct = FlowLayout.CENTER;
	}

	public void left() {
		alignAct = FlowLayout.LEFT;
	}

	public void addLeft(Component c) {
		left();
		addItem(c);
	}

	public void addRight(Component c) {
		right();
		addItem(c);
	}

	public void addCenter(Component c) {
		center();
		addItem(c);
	}

	public int mysql_insert(DBConnection con, String table) {
		String qry = "INSERT INTO " + table + " (";
		for (int i = 0; i < inputs.size(); i++) {
			Input in = (Input) inputs.get(i);
			if (in.check(con, table) == false) {
				return -1;
			}
			qry += in.getName();
			if (i < inputs.size() - 1) {
				qry += " , ";
			}
		}
		qry += ") VALUES (";
		for (int i = 0; i < inputs.size(); i++) {
			Input in = (Input) inputs.get(i);
			qry += in.getValue();
			if (i < inputs.size() - 1) {
				qry += " , ";
			}
		}
		qry += ");";
		return con.mysql_update(qry);
	}

	public boolean check(DBConnection con, String table) {
		boolean b = true;
		for (int i = 0; i < inputs.size(); i++) {
			Input in = (Input) inputs.get(i);
			if (in.check(con, table) == false) {
				b = false;
			}
		}
		return b;
	}

	public int mysql_update(DBConnection con, String table, String edit) {

		String qry = "UPDATE " + table + " SET";
		for (int i = 0; i < inputs.size(); i++) {
			Input in = (Input) inputs.get(i);
			if (in.check(con, table) == false) {
				return -1;
			}
			qry += in.getName() + " = " + in.getValue();
			if (i < inputs.size() - 1) {
				qry += " , ";
			}
		}
		qry += ") VALUES (";
		for (int i = 0; i < inputs.size(); i++) {
			Input in = (Input) inputs.get(i);
			qry += in.getValue();
			if (i < inputs.size() - 1) {
				qry += " , ";
			}
		}
		qry += ");";
		return con.mysql_update(qry);
	}

	public void clear() {
		for (int i = 0; i < inputs.size(); i++) {
			Input in = (Input) inputs.get(i);
			in.clear();
		}
	}

	public void addHiddenInput(Input i) {
		inputs.add(i);
	}
}