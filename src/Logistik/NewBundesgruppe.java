package Logistik;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Hier kann man Bundesnummern erstellen Daten wird eingegeben SelectInputLevel:
 * Eingabe mit Hilfefenster
 */

public class NewBundesgruppe extends LayoutMainPanel implements ActionListener {
	/**
	 * Textfelder für die Eingabe
	 */
	private Input bezeichnung, uebergr, nr;
	/**
	 * Textfelder für die Eingabe mit Hilfefenstern
	 */
	private SelectInputLevel ueber;
	/**
	 * Button zum Speichern/Löschen
	 */
	private JButton save, clear;
	private LayoutForm f;
	private String edit;
	private String nrOld;
	private String ueNr;

	public NewBundesgruppe(UserImport user) {
		super(user);
		setLayoutM(new FlowLayout(FlowLayout.LEFT));
		f = new LayoutForm();

		bezeichnung = new Input(20, "Bezeichnung");
		bezeichnung.setKey(true);
		uebergr = new Input(20, "Uebergruppe");
		save = new JButton("Speichern");
		clear = new JButton("Löschen");
		nr = new Input(10, "");

		String[] bundesgrupHeads = { "nr", "id", "bezeichnung", "uebergruppe" };
		ueber = new SelectInputLevel(con, uebergr, "bundesnr", bundesgrupHeads,
				"id", "bezeichnung", "uebergruppe");

		f.addRight(new JLabel("Bezeichnung:"));
		f.addLeftInput(bezeichnung);
		f.br();
		f.addRight(new JLabel("Uebergruppe:"));
		f.addLeftSelectL(ueber);
		ueber.setBg(this);
		f.br();
		f.addRight(new JLabel("Nummer"));
		f.addLeft(nr);
		f.br();
		f.addRight(save);
		f.addLeft(clear);

		addM(f);

		clear.addActionListener(this);
		save.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clear) {
			clear();
		}
		if (e.getSource() == save) {
			save();
		}
	}

	public void clear() {
		bezeichnung.setText("");
		uebergr.setText("");
		nr.setText("");
	}

	public void setBG(String s) {
		if (ueNr == null || !ueber.getValue().equals(ueNr)) {
			ueNr = ueber.getValue();
			String st = getNextBundesnr(s);
			nr.setText(st);
		}
	}

	public void setEdit(String id) {
		edit = id;
		String q = "SELECT id, nr, uebergruppe, bezeichnung FROM bundesnr WHERE id = "
				+ edit + ";";
		try {
			ResultSet rs = con.mysql_query(q);
			rs.next();
			bezeichnung.setText(rs.getString("bezeichnung"));
			ueber.setText(rs.getString("uebergruppe"));
			nr.setText(rs.getString("nr"));
			nrOld = rs.getString("nr");
			revalidate();
			repaint();

			rs.close();

		} catch (Exception e) {
		}
	}

	public void save() {
		try {
			String s = ueber.getValue();
			String q;
			if (s == null || s.equals("")) {
				s = "null";
				q = "SELECT nr, uebergruppe FROM bundesnr WHERE nr="
						+ nr.getText() + " and uebergruppe IS null;";
			} else {
				q = "SELECT nr, uebergruppe FROM bundesnr WHERE nr="
						+ nr.getText() + " and uebergruppe=" + s + ";";
			}
			ResultSet rs = con.mysql_query(q);
			if (rs.next()) {
				if (edit != null && nr.getText().equals(nrOld)) {
					;
				} else {
					new MessageError(
							"Es besteht bereits eine Bundesnummer mit dieser Nummer!");
					rs.close();
					return;
				}
			}
			rs.close();
			if (edit == null) {
				String upd = "INSERT INTO bundesnr (bezeichnung,uebergruppe,nr) VALUES ("
						+ bezeichnung.getValue()
						+ ","
						+ s
						+ ","
						+ nr.getValue() + ")";
				con.mysql_update(upd);

				new MessageSucess("Neue Bundesgruppe angelegt!");
			} else {
				String upd = "UPDATE bundesnr SET bezeichnung="
						+ bezeichnung.getValue() + ",uebergruppe=" + s
						+ ", nr=" + nr.getValue() + " WHERE id = " + edit + ";";
				con.mysql_update(upd);

				new MessageSucess("Bundesgruppe bearbeitet!");
			}
			bezeichnung.setText("");
			ueber.setValue("");
			uebergr.setText("");
			nr.setText("");
		} catch (Exception ex) {
			ex.getMessage();
		}
	}

	public String getNextBundesnr(String id) {
		ResultSet rs;
		String nr = "";
		try {
			String qry = "SELECT count(*) as num FROM bundesnr WHERE uebergruppe = "
					+ id + ";";
			rs = con.mysql_query(qry);
			rs.next();
			nr = rs.getString("num");

			rs.close();

		} catch (Exception e) {
			return "0";
		}
		return "" + (Integer.parseInt(nr));
	}
}
