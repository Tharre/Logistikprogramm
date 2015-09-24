package Logistik;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.sql.*;

/**
 * Hier kann man Materialien erstellen Daten werden eingegeben. SelectInput:
 * Eingabe mit Hilfefenster SelectInputLevel: Eingabe mit Hilfefenster
 */

public class NewMaterial extends LayoutMainPanel implements ActionListener {
	/**
	 * Textfelder für die Eingabe
	 */
	private Input lagerort, bezeichnung, bundesnr, inventurgruppe, firma,
			preisExkl, mwst, artNr, date, meldebestand, fixkosten;
	/**
	 * Textfelder für die Eingabe mit Hilfefenstern
	 */
	private SelectInput firm, einheit;
	/**
	 * Textfelder für die Eingabe mit Hilfefenstern (hierarchisch geordnet)
	 */
	private SelectInputLevel invgruppe, bundesgr;
	/**
	 * Button zum Speichern/Löschen
	 */
	private JButton save, clear;
	private LayoutForm f;

	private JLabel info;
	private UserImport u;

	private JPanel ueberpruefe;
	private JButton btn = new JButton("Artikel vorhanden?");

	private JLabel lText = new JLabel("", SwingConstants.CENTER);
	private boolean ok = true;

	private int bundesNr;
	private int uebergr;

	public NewMaterial(UserImport user) {
		super(user);
		u = user;

		setLayoutM(new FlowLayout(FlowLayout.LEFT));
		f = new LayoutForm();

		ueberpruefe = new JPanel();
		ueberpruefe.setLayout(new FlowLayout());

		bezeichnung = new Input(30, "bezeichnung");
		bezeichnung.setKey(true);
		bundesnr = new Input(20, "bundesnr");
		inventurgruppe = new Input(20, "inventurgruppe");
		firma = new Input(20, "firma");
		preisExkl = new Input(20, 12, Input.NUMBER, "preisExkl");
		meldebestand = new Input(5, 5, Input.NUMBER, "meldebestand");
		mwst = new Input(5, "mwst");
		einheit = new SelectInput(con, new Input(10, "einheiten"), "einheiten",
				new String[] { "id", "einheit" }, "id", "einheit", user);
		artNr = new Input(20, "artNr");
		date = new Input(20, "erstellungsdatum");
		lagerort = new Input(20, "lagerort");
		fixkosten = new Input(20, "fixkosten");

		save = new JButton("Speichern");
		clear = new JButton("Löschen");

		String[] firmaHeads = { "id", "firmenname", "plz", "ort",
				"sachbearbeiter" };
		firm = new SelectInput(con, firma, "firma", firmaHeads, "id",
				"firmenname", user);

		String[] bundesgrupHeads = { "nr", "bezeichnung" };
		bundesgr = new SelectInputLevel(con, bundesnr, "bundesnr",
				bundesgrupHeads, "nr", "bezeichnung", "uebergruppe");

		String[] invgrupHeads = { "id", "bezeichnung" };
		invgruppe = new SelectInputLevel(con, inventurgruppe, "inventurgruppe",
				invgrupHeads, "id", "bezeichnung", "uebergruppe");

		info = new JLabel(
				"Sie müssen mit Hilfe der Tabulator-Tast alle Felder verlassen haben!");

		btn.addActionListener(this);
		ueberpruefe.add(btn);
		ueberpruefe.add(lText);

		f.addHiddenInput(date);
		f.addRight(new JLabel("Bezeichnung:"));
		f.addLeftInput(bezeichnung);
		f.addLeft(ueberpruefe);
		f.br();
		f.addRight(new JLabel("Inventurgruppe:"));
		f.addLeftSelectL(invgruppe);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("BundesNummer:"));
		f.addLeftSelectL(bundesgr);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("Firma"));
		f.addLeftSelect(firm);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("Preis exkl MWSt:"));
		f.addLeftInput(preisExkl);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("MWSt:"));
		f.addLeftInput(mwst);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("Einheit:"));
		f.addLeftSelect(einheit);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("Artikelnummer:"));
		f.addLeftInput(artNr);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("Meldebestand:"));
		f.addLeftInput(meldebestand);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("Lagerort:"));
		f.addLeftInput(lagerort);
		f.addLeft(new JLabel(""));
		f.br();
		f.addRight(new JLabel("Fixkosten:"));
		f.addLeftInput(fixkosten);
		f.addLeft(new JLabel(""));
		f.br();

		f.addLeft(new JLabel(""));
		f.addRight(info);
		f.addLeft(new JLabel(""));
		f.br();

		f.addRight(save);
		f.addLeft(clear);
		f.addLeft(new JLabel(""));

		addM(f);

		bundesNr = bundesgr.getBgId();

		clear.addActionListener(this);
		save.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clear) {
			clear();
		} else if (e.getSource() == save) {
			save();
		} else if (e.getSource() == btn) {
			ok = pruefe();
			if (ok) {
				lText.setText("Artikel noch nicht vorhanden");
			}

			else {
				lText.setText("Artikel vorhanden");
			}
		}
	}

	public void clear() {
		bezeichnung.setText("");
		bundesgr.setValue("");
		invgruppe.setValue("");
		bundesgr.setText("");
		bundesnr.setText("");
		invgruppe.setText("");
		inventurgruppe.setText("");
		firm.clear();
		preisExkl.setText("");
		mwst.setText("");
		einheit.clear();
		artNr.setText("");
		meldebestand.setText("");
		lagerort.setText("");
		fixkosten.setText("");
	}

	public void clear2() {
		bezeichnung.setText("");
		artNr.setText("");
		meldebestand.setText("");
		lagerort.setText("");
	}

	public void save() {
		try {

			String qry = "SELECT bezeichnung FROM material WHERE bezeichnung LIKE "
					+ bezeichnung.getValue() + ";";
			ResultSet rs = con.mysql_query(qry);
			if (rs.next()) {
				new MessageError(
						"Es besteht bereits ein Material mit diesem Namen");
				rs.close();
				return;
			}
			date.setText("" + System.currentTimeMillis() / 1000);

			if (checkeDaten(meldebestand.getValue(), lagerort.getValue()))

			{

				String insMat = "INSERT INTO material (bezeichnung,erstellungsdatum,bundesnr,inventurgruppe, meldebestand, lagerort,erfasser, fixkosten) VALUES ("
						+ bezeichnung.getValue()
						+ ","
						+ date.getText()
						+ ","
						+ bundesgr.getBgId()
						+ ","
						+ invgruppe.getValue()
						+ ","
						+ meldebestand.getValue()
						+ ","
						+ lagerort.getValue()
						+ ",'"
						+ u.getName()
						+ "',"
						+ fixkosten.getValue()
						+ ")";

				con.mysql_update(insMat);

				JOptionPane.showMessageDialog(this,
						"Die Materialdaten wurden erfolgreich gespeichert.");
				new MessageSucess("Neues Material hinzugefügt");

			}

			else
				new MessageError("Alle Felder müssen ausgefüllt werden!");

			String sqlNewMat = "SELECT id FROM material ORDER BY id DESC LIMIT 1";
			rs = con.mysql_query(sqlNewMat);
			rs.next();
			int i = rs.getInt("id");
			String id = "" + i;

			String insMatFirm = "INSERT INTO firma_material (firma,material,preisExkl,mwst,einheit,artNr) VALUES ("
					+ firm.getValue()
					+ ","
					+ id
					+ ","
					+ preisExkl.getText()
					+ ","
					+ mwst.getText()
					+ ",'"
					+ einheit.getText()
					+ "',"
					+ artNr.getValue() + ")";

			con.mysql_update(insMatFirm);

			//new SuccessMessage("Neues Material hinzugefügt");
			rs.close();

		} catch (Exception e) {
			e.getMessage();
			JOptionPane.showMessageDialog(this,
					"Fehler beim Speichern der Daten!");
		}
		clear2();

	}

	public boolean pruefe() {
		ok = true;
		String bez = bezeichnung.getValue();

		String query = "select * from material where bezeichnung like " + bez;
		ResultSet rs = con.mysql_query(query);
		try {
			while (rs.next()) {
				ok = false;
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ok;
	}

	public boolean checkeDaten(String meldebestand, String lagerort) {
		boolean passt = true;
		try {
			if (meldebestand.length() == 0 || lagerort.length() == 0) {
				passt = false;
			} else
				passt = true;
		} catch (NumberFormatException e) {
			new MessageError("Der Meldebestand muss eine Zahl sein!!");
		}

		return passt;
	}
}
