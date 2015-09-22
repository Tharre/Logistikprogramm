package Logistik;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/*
 * Zum bearbeiten von Firma-Material-Beziehungen
 * Die Daten werden von EditFirmaMaterial übergeben und in die Eingabefelder übernommen
 */

public class NewEditFirmaMaterial extends LayoutMainPanel implements
		ActionListener {
	private Input mat, firma, preisExkl, mwst, artNr, einh;
	private SelectInput firm, material, einheit;
	private JButton save, clear;
	private LayoutForm f;
	private String edit = null;

	public NewEditFirmaMaterial(UserImport user) {
		super(user);
		setLayoutM(new FlowLayout(FlowLayout.LEFT));
		f = new LayoutForm();

		mat = new Input(30, "bezeichnung");
		firma = new Input(20, "firma");
		preisExkl = new Input(20, 12, Input.NUMBER, "preisExkl");
		mwst = new Input(5, "mwst");
		artNr = new Input(20, "artNr");
		save = new JButton("Speichern");
		clear = new JButton("Abbrechen");
		einh = new Input(10, "");
		einheit = new SelectInput(con, einh, "einheiten", new String[] { "id",
				"einheit" }, "id", "einheit", user);

		String[] firmaHeads = { "id", "firmenname", "plz", "ort",
				"sachbearbeiter" };
		firm = new SelectInput(con, firma, "firma", firmaHeads, "id",
				"firmenname", user);
		String[] matHeads = { "id", "bezeichnung" };
		material = new SelectInput(con, mat, "material", matHeads, "id",
				"bezeichnung", user);

		f.addRight(new JLabel("Material:"));
		f.addLeftSelect(material);
		f.br();
		f.addRight(new JLabel("Firma"));
		f.addLeftSelect(firm);
		f.br();
		f.addRight(new JLabel("Preis exkl MWSt.(Punkt als Komma):"));
		f.addLeftInput(preisExkl);
		f.br();
		f.addRight(new JLabel("MWSt:"));
		f.addLeftInput(mwst);
		f.br();
		f.addRight(new JLabel("Einheit:"));
		f.addLeftSelect(einheit);
		f.br();
		f.addRight(new JLabel("Artikelnummer:"));
		f.addLeftInput(artNr);
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

	public void setEdit(String s) {
		edit = s;
		try {
			String q = "SELECT material,firma,preisExkl,mwst,einheit,artNr FROM firma_material WHERE id="
					+ edit + ";";
			ResultSet rs = con.mysql_query(q);
			rs.next();
			material.setText(rs.getString("material"));
			material.setEnabled(false);
			firm.setText(rs.getString("firma"));
			firm.setEnabled(false);
			preisExkl.setText(rs.getString("preisExkl"));
			mwst.setText(rs.getString("mwst"));
			einheit.setText(rs.getString("einheit"), rs.getString("einheit"));
			einheit.setCheck(false);
			artNr.setText(rs.getString("artNr"));

			rs.close();

		} catch (Exception ex) {
		}
	}

	public void clear() {
		material.setText("");
		firm.setText("");
		firma.setText("");
		mat.setText("");
		preisExkl.setText("");
		mwst.setText("");
		einh.setText("");
		artNr.setText("");
	}

	public void save() {
		if (edit == null) {
			return;
		}

		String insMat = "UPDATE firma_material set preisExkl="
				+ preisExkl.getValue() + ",mwst=" + mwst.getValue()
				+ ", einheit=" + einh.getValue() + ",artNr=" + artNr.getValue()
				+ " WHERE id = " + edit + ";";

		con.mysql_update(insMat);
		clear();
		new MessageSucess("Firma-Material-Beziehung geändert!");
	}
}
