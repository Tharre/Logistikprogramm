package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

/**
 * Alle Firmen werden aufgelistet am Ende jeder Zeile ist eine Checkbox, in der
 * man ausw�hlen kann, ob ein Datensatz gel�scht werden soll bei Best�tigung wir
 * d�berpr�ft, ob Firma schon verwendet wird -> keine L�schung
 */
public class DelFirma extends LayoutMainPanel implements ActionListener {
	public AnzTabelleA df;
	public JButton del;

	public DelFirma(UserImport user) {
		super(user);
		del = new JButton("L�schen");
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
				"einkaeufergruppe", "l�schen" };
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
					"Wollen Sie die Firmen l�schen?", "l�schen",
					JOptionPane.YES_NO_OPTION);
			if (i == 0) {

				Object[] hakDelFirm = df.getKlicked("id", "l�schen");

				for (int j = 0; j < hakDelFirm.length; j++) {
					try {
						// PR�FVORGANG

						String sql1 = "SELECT b.firma FROM banf b,firma f WHERE b.firma="
								+ hakDelFirm[j] + " LIMIT 1";
						ResultSet rs1 = con.mysql_query(sql1);
						if (rs1.next()) {
							JOptionPane
									.showMessageDialog(
											null,
											"Mindestens eine der ausgew�hlten Firmen wird in BANFs verwendet und kann nicht gel�scht werden!");
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
											"Mindestens eine der ausgew�hlten Firmen ist einem Material zugeordnet und kann nicht gel�scht werden!");
							rs2.close();
							return;
						}
						rs2.close();

						// L�SCHVORGANG

						String sql = "DELETE FROM firma WHERE id = "
								+ hakDelFirm[j] + "";
						con.mysql_update(sql);
					} catch (Exception e3) {
						e3.getMessage();
					}
				}

				// zur�ckspringen
				JOptionPane.showMessageDialog(null, "Daten gel�scht");
			}

		}// if
		activate();
	}// actionPerformed

}