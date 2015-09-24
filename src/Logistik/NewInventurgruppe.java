package Logistik;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Hier kann man Inventurgruppen erstellen Daten wird eingegeben
 * SelectInputLevel: Eingabe mit Hilfefenster
 */

public class NewInventurgruppe extends LayoutMainPanel implements
		ActionListener {
	/**
	 * Textfelder für die Eingabe
	 */
	private Input bezeichnung, uebergr;
	/**
	 * Textfelder für die Eingabe mit Hilfefenstern
	 */
	private SelectInputLevel ueber;
	/**
	 * Button zum Speichern/Löschen
	 */
	private JButton save, clear;
	private LayoutForm f;
	private String edit = null;

	public NewInventurgruppe(UserImport user) {
		super(user);
		setLayoutM(new FlowLayout(FlowLayout.LEFT));
		f = new LayoutForm();

		bezeichnung = new Input(20, "Bezeichnung");
		bezeichnung.setKey(true);
		uebergr = new Input(20, "Uebergruppe");
		save = new JButton("Speichern");
		clear = new JButton("Löschen");

		String[] invgrupHeads = { "id", "bezeichnung", "uebergruppe" };
		ueber = new SelectInputLevel(con, uebergr, "inventurgruppe",
				invgrupHeads, "id", "bezeichnung", "uebergruppe");

		f.addRight(new JLabel("Bezeichnung:"));
		f.addLeftInput(bezeichnung);
		f.br();
		f.addRight(new JLabel("Uebergruppe:"));
		f.addLeftSelectL(ueber);
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

	public void setEdit(String edit) {
		this.edit = edit;
		String qry = "SELECT bezeichnung, uebergruppe FROM inventurgruppe WHERE id="
				+ edit + " order by bezeichnung;";
		try {
			ResultSet rs = con.mysql_query(qry);
			rs.next();
			bezeichnung.setText(rs.getString("bezeichnung"));
			ueber.setValue(rs.getString("uebergruppe"));

			rs.close();

		} catch (Exception e) {
		}
	}

	public void clear() {
		f.clear();
	}

	public void save() {
		if (edit == null) {

			String s = ueber.getValue();
			if (s == null || s.equals("")) {
				s = "null";
			}
			String upd = "INSERT INTO inventurgruppe (bezeichnung,uebergruppe) VALUES ("
					+ bezeichnung.getValue() + "," + s + ")";
			con.mysql_update(upd);

			new MessageSucess("Neue Inventurgruppe angelegt!");
			bezeichnung.setText("");
			uebergr.setText("");
		} else {
			String sql = "UPDATE inventurgruppe SET bezeichnung = "
					+ bezeichnung.getValue() + ", uebergruppe="
					+ ueber.getValue() + " WHERE id = " + edit + ";";
			try {
				con.mysql_update(sql);
				new MessageSucess("Inventurgruppe bearbeitet!");
				bezeichnung.setText("");
				uebergr.setText("");
			} catch (Exception e) {
			}
		}
	}
}
