package Logistik;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.sql.*;

/**
 * Klasse zum Bearbeiten von Materialien Die Daten werden von EditMaterial
 * übergeben und in die Eingabefelder übernommen
 */

public class NewEditMaterial extends LayoutMainPanel implements ActionListener {
	private Input lagerort, bezeichnung, bundesnr, inventurgruppe,
			meldebestand, fixkosten;
	private SelectInputLevel invgruppe, bundesgr;
	private JButton save, clear;
	private LayoutForm f;
	private String edit = null;
	private String bezAlt;
	private ButtonGroup bgFK = new ButtonGroup();
	private JRadioButton rbFKja = new JRadioButton("ja");
	private JRadioButton rbFKnein = new JRadioButton("nein");
	private String fkBoolean;
	private int bundesNr;
	private JLabel info1;

	public NewEditMaterial(UserImport user) {
		super(user);
		setLayoutM(new FlowLayout(FlowLayout.LEFT));
		f = new LayoutForm();

		bezeichnung = new Input(30, "bezeichnung");
		bezeichnung.setKey(true);
		bundesnr = new Input(20, "bundesnr");
		inventurgruppe = new Input(20, "inventurgruppe");
		// meldebestand = new Input(5, 5, Input.NUMBER, "meldebestand");
		meldebestand = new Input(5, "meldebestand");
		save = new JButton("Speichern");
		clear = new JButton("Abbrechen");
		lagerort = new Input(25, "lagerort");
		String[] invgrupHeads = { "id", "bezeichnung" };
		invgruppe = new SelectInputLevel(con, inventurgruppe, "inventurgruppe",
				invgrupHeads, "id", "bezeichnung", "uebergruppe");
		String[] bundesgrupHeads = { "nr", "bezeichnung" };
		bundesgr = new SelectInputLevel(con, bundesnr, "bundesnr",
				bundesgrupHeads, "nr", "bezeichnung", "uebergruppe");
		fixkosten = new Input(5, "fixkosten");

		info1 = new JLabel(
				"Zählt das Material nicht zu den Fixkosten - bitte das Feld leer lassen!");

		f.addRight(new JLabel("Bezeichnung:"));
		f.addLeftInput(bezeichnung);
		f.br();
		f.addRight(new JLabel("BundesNummer:"));
		f.addLeftSelectL(bundesgr);
		f.br();
		f.addRight(new JLabel("Inventurgruppe:"));
		f.addLeftSelectL(invgruppe);
		f.br();
		f.addRight(new JLabel("Meldebestand:"));
		f.addLeftInput(meldebestand);
		f.br();
		f.addRight(new JLabel("Lagerort:"));
		f.addLeftInput(lagerort);
		f.br();
		f.addRight(new JLabel("Fixkosten:"));
		f.addLeftInput(fixkosten);
		f.br();
		f.br();
		f.addLeft(new JLabel(""));
		f.addRight(info1);
		f.br();
		f.br();
		f.br();
		f.br();
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
		} else if (e.getSource() == save) {
			save();
		}

	}

	public void setEdit(String e) {
		edit = e;
		try {
			String q = "SELECT m.bezeichnung, m.inventurgruppe, m.meldebestand,m.lagerort,m.fixkosten, bu.nr FROM material m, bundesnr bu WHERE m.id="
					+ edit + ";";

			ResultSet rs = con.mysql_query(q);
			rs.next();
			bezeichnung.setText(rs.getString("bezeichnung"));
			bundesgr.setText(rs.getString("nr"));
			invgruppe.setText(rs.getString("inventurgruppe"));
			meldebestand.setText(rs.getString("meldebestand"));
			lagerort.setText(rs.getString("lagerort"));
			bezAlt = bezeichnung.getText();
			fixkosten.setText(rs.getString("fixkosten"));

			rs.close();

		} catch (Exception ex) {
			ex.getMessage();
			ex.printStackTrace();
		}
	}

	public void clear() {
		bezeichnung.setText("");
		bundesgr.setText("");
		bundesnr.setText("");
		invgruppe.setText("");
		inventurgruppe.setText("");
		meldebestand.setText("");
		lagerort.setText("");
		bundesgr.setValue("");
		invgruppe.setValue("");
		fixkosten.setText("");
		revalidate();
		repaint();
	}

	public void save() {
		if (edit == null) {
			return;
		}
		try {
			String qry = "SELECT bezeichnung FROM material WHERE bezeichnung LIKE "
					+ bezeichnung.getValue() + ";";
			ResultSet rs = con.mysql_query(qry);
			if (rs.next() && !bezAlt.equals(bezeichnung.getText())) {
				new MessageError(
						"Es besteht bereits ein Material mit diesem Namen");
				return;
			}

			String qryBundesNr = "Select id from bundesnr where bezeichnung like '"
					+ bundesgr.getValue() + "'";
			ResultSet rsBn = con.mysql_query(qryBundesNr);
			if (rs.next()) {
				bundesNr = rs.getInt("id");
			}

			String insMat = "UPDATE material SET bezeichnung = "
					+ bezeichnung.getValue() + ", bundesnr="
					+ bundesgr.getValue() + ", inventurgruppe= "
					+ invgruppe.getValue() + ", meldebestand= "
					+ meldebestand.getValue() + ",lagerort= "
					+ lagerort.getValue() + ",fixkosten="
					+ fixkosten.getValue() + " WHERE id=" + edit + ";";
			con.mysql_update(insMat);

			rs.close();

		} catch (Exception e) {
			e.getMessage();
			e.getStackTrace();
		}
		clear();
		new MessageSucess("Material bearbeitet!");
	}
}
