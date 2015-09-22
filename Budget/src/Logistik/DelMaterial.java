package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Alle Materialien werden aufgelistet am Ende jeder Zeile ist eine Checkbox, in
 * der man auswählen kann, ob ein Datensatz gelöscht werden soll bei Bestätigung
 * wir düberprüft, ob Material schon verwendet wird -> keine Löschung
 */

public class DelMaterial extends LayoutMainPanel implements ActionListener {
	public AnzTabelleA df;
	public JButton del;

	public DelMaterial(UserImport user) {
		super(user);
		del = new JButton("Löschen");

		del.addActionListener(this);
	}

	@Override
	public void activate() {
		removeMall();
		String qry = "SELECT * FROM material,firma_material,firma,inventurgruppe WHERE material.inventurgruppe=inventurgruppe.id AND material.id=firma_material.material AND firma.id=firma_material.firma ORDER BY material.bezeichnung";
		ResultSet rs = con.mysql_query(qry);
		String[] spaltenDB = { "material.id", "bezeichnung", "bundesnr",
				"inventurgruppe.bezeichnung", "stueck", "preisExkl", "einheit",
				"artNr", "firmenname", "ort" };
		String[] spaltenT = { "ID", "Bezeichnung", "BundesNr",
				"Inventurgruppe", "Stück", "Preis exkl", "Einheit",
				"ArtikelNr", "Firma", "Ort", "löschen" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, Integer.class, String.class, String.class,
				String.class, String.class, String.class, Boolean.class };
		df = new AnzTabelleA(spaltenDB, spaltenT, classes, rs, 1);
		JScrollPane p = new JScrollPane(df);
		setLayoutM(new BorderLayout());
		addM(p, BorderLayout.CENTER);
		addM(del, BorderLayout.SOUTH);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == del) {
			int i = JOptionPane.showConfirmDialog(null,
					"Wollen Sie die Materialien löschen?", "löschen",
					JOptionPane.YES_NO_OPTION);
			if (i == 0) {

				Object[] hakDelMat = df.getKlicked("ID", "löschen");

				for (int j = 0; j < hakDelMat.length; j++) {
					try {
						// PRÜFVORGANG

						String sql1 = "SELECT bp.bezeichnung FROM banfpos bp,material f WHERE bp.bezeichnung="
								+ hakDelMat[j] + " LIMIT 1";
						ResultSet rs1 = con.mysql_query(sql1);
						if (rs1.next()) {
							JOptionPane
									.showMessageDialog(
											null,
											"Mindestens eine der ausgewählten Materialien wird in BANFs verwendet und kann nicht gelöscht werden!");
							rs1.close();
							return;
						}
						rs1.close();

						// LÖSCHVORGANG

						String delFM = "DELETE FROM firma_material WHERE material = "
								+ hakDelMat[j];
						con.mysql_update(delFM);
						String delM = "DELETE FROM material WHERE id = "
								+ hakDelMat[j];
						con.mysql_update(delM);

					} catch (Exception e3) {
						e3.getMessage();
					}
				}

				JOptionPane.showMessageDialog(null, "Daten gelöscht");
			}

		}// if
		activate();
	}// actionPerformed

}