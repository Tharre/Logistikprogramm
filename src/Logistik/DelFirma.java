package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

/**
 * Alle Firmen werden aufgelistet am Ende jeder Zeile ist eine Checkbox, in der
 * man auswählen kann, ob ein Datensatz gelöscht werden soll bei Bestätigung wir
 * düberprüft, ob Firma schon verwendet wird -> keine Löschung
 */
public class DelFirma extends LayoutMainPanel implements ActionListener {
	public AnzTabelleA df;
	public JButton del;

	public DelFirma(UserImport user) {
		super(user);
		del = new JButton("Löschen");
		del.addActionListener(this);
		activate();
	}

	@Override
	public void activate() {
		removeMall();
		String qry = "SELECT * FROM firma ORDER BY firmenname";
		ResultSet rs = con.mysql_query(qry);
		String[] spalten = { "id", "firmenname", "erstellungsdatum", "plz",
				"ort", "strasse", "mail", "staat", "kundennr",
				"sachbearbeiter", "telefon", "fax", "homepage", "uid",
				"kondition", "umsNr", "araNr", "kreditorennummer",
				"einkaeufergruppe" };
		String[] spalten2 = { "id", "firmenname", "erstellungsdatum", "plz",
				"ort", "strasse", "mail", "staat", "kundennr",
				"sachbearbeiter", "telefon", "fax", "homepage", "uid",
				"kondition", "umsNr", "araNr", "kreditorennummer",
				"einkaeufergruppe", "löschen" };
		Class[] classes = { Integer.class, String.class, Date.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, Integer.class, String.class, String.class,
				Boolean.class };
		df = new AnzTabelleA(spalten, spalten2, classes, rs, 1);
		removeMall();
		JScrollPane p = new JScrollPane(df);
		setLayoutM(new BorderLayout());
		addM(p, BorderLayout.CENTER);
		addM(del, BorderLayout.SOUTH);
		repaint();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == del) {
			int i = JOptionPane.showConfirmDialog(null,
					"Wollen Sie die Firmen löschen?", "löschen",
					JOptionPane.YES_NO_OPTION);
			if (i == 0) {

				Object[] hakDelFirm = df.getKlicked("id", "löschen");

				for (int j = 0; j < hakDelFirm.length; j++) {
					try {
						// PRÜFVORGANG

						String sql1 = "SELECT b.firma FROM banf b,firma f WHERE b.firma="
								+ hakDelFirm[j] + " LIMIT 1";
						ResultSet rs1 = con.mysql_query(sql1);
						if (rs1.next()) {
							JOptionPane
									.showMessageDialog(
											null,
											"Mindestens eine der ausgewählten Firmen wird in BANFs verwendet und kann nicht gelöscht werden!");
							rs1.close();
							return;
						}
						rs1.close();
						String sql2 = "SELECT fm.firma FROM firma_material fm,firma f WHERE fm.firma="
								+ hakDelFirm[j] + " LIMIT 1";
						ResultSet rs2 = con.mysql_query(sql2);
						if (rs2.next()) {
							JOptionPane
									.showMessageDialog(
											null,
											"Mindestens eine der ausgewählten Firmen ist einem Material zugeordnet und kann nicht gelöscht werden!");
							rs2.close();
							return;
						}
						rs2.close();

						// LÖSCHVORGANG

						String sql = "DELETE FROM firma WHERE id = "
								+ hakDelFirm[j] + "";
						con.mysql_update(sql);
					} catch (Exception e3) {
						e3.getMessage();
					}
				}

				// zurückspringen
				JOptionPane.showMessageDialog(null, "Daten gelöscht");
			}

		}// if
		activate();
	}// actionPerformed

}