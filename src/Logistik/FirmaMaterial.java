package Logistik;

import javax.swing.*;

import java.awt.event.*;

/**
 * Klasse zum Erzeugen von Firma-Material-Verbindungen In der Eingabemaske
 * können die Daten eingegeben werden Nach dem Klick auf den speichern Button
 * werden die Daten in die DB gespeichert
 */

public class FirmaMaterial extends LayoutMainPanel implements ActionListener {
	private JButton save;
	private JButton del;
	private LayoutForm f;
	private SelectInput firma, material, einheit;
	private Input preisExkl, mwst, artNr;

	public FirmaMaterial(UserImport user) {
		super(user);
		f = new LayoutForm();
		save = new JButton("Speichern");
		del = new JButton("Löschen");
		Input fi = new Input(20, "");
		Input mat = new Input(20, "");
		firma = new SelectInput(con, fi, "firma", new String[] { "id",
				"firmenname" }, "id", "firmenname", user);
		einheit = new SelectInput(con, new Input(10, ""), "einheiten",
				new String[] { "id", "einheit" }, "id", "einheit", user);
		material = new SelectInput(con, mat, "material", new String[] { "id",
				"bezeichnung" }, "id", "bezeichnung", user);
		preisExkl = new Input(20, 12, Input.NUMBER, "preisExkl");
		mwst = new Input(4, 4, Input.NUMBER, "mwst");
		artNr = new Input(15, "artNr");
		f.addRight(new JLabel("Firma:"));
		f.addLeftSelect(firma);
		f.br();
		f.addRight(new JLabel("Material:"));
		f.addLeftSelect(material);
		f.br();
		f.addRight(new JLabel("PreisExkl:"));
		f.addLeftInput(preisExkl);
		f.br();
		f.addRight(new JLabel("MWST:"));
		f.addLeftInput(mwst);
		f.br();
		f.addRight(new JLabel("Einheit:"));
		f.addLeftSelect(einheit);
		f.br();
		f.addRight(new JLabel("ArtNr:"));
		f.addLeftInput(artNr);
		f.br();
		f.addRight(save);
		f.addLeft(del);
		addM(f);
		save.addActionListener(this);
		del.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == save) {
			String qry = "INSERT INTO firma_material(firma,material,preisExkl,mwst,einheit,artNr)"
					+ "VALUES("
					+ firma.getValue()
					+ ","
					+ material.getValue()
					+ ","
					+ preisExkl.getValue()
					+ ","
					+ mwst.getValue()
					+ ",'"
					+ einheit.getText() + "'," + artNr.getValue() + ");";
			if (con.mysql_update(qry) != -1) {
				new MessageSucess("Verbindung hergestellt!");
				// firma.setValue("");
				material.clear();
				einheit.clear();
				firma.clear();
				// material.setText("");
				// einheit.setText("");
				preisExkl.setText("");
				mwst.setText("");
				artNr.setText("");
			}

			JOptionPane.showMessageDialog(this,
					"Die Daten wurden erfolgreich gespeichert.");

		}
		if (e.getSource() == del) {
			material.clear();
			einheit.clear();
			firma.clear();
			// material.setText("");
			// einheit.setText("");
			preisExkl.setText("");
			mwst.setText("");
			artNr.setText("");
			// f.clear();
		}
	}
}