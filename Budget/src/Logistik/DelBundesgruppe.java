package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Klasse zum Löschen von Bundesgruppen Listet alle Bundesgruppen auf + Checkbox
 * Nach dem klick auf den Löschen-Button werden die angehakten gelöscht
 */

public class DelBundesgruppe extends LayoutMainPanel implements ActionListener {
	public AnzTabelleA df;
	public JButton del;

	public DelBundesgruppe(UserImport user) {
		super(user);
		del = new JButton("Löschen");
		del.addActionListener(this);
		activate();
	}

	@Override
	public void activate() {
		removeMall();
		String qry = "SELECT * FROM bundesnr ORDER BY bezeichnung";
		ResultSet rs = con.mysql_query(qry);
		String[] spalten = { "id", "bezeichnung" };
		String[] spalten2 = { "id", "bezeichnung", "löschen" };
		Class[] classes = { Integer.class, String.class, Boolean.class };
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
			Object[] hakDelFirm = df.getKlicked("id", "löschen");
			if (hakDelFirm.length == 0) {
				new MessageError("Mindestens eine Bundesgruppe auswählen!");
				return;
			}
			int i = JOptionPane.showConfirmDialog(null,
					"Wollen Sie die Bundesgruppe(n) löschen?", "löschen",
					JOptionPane.YES_NO_OPTION);
			if (i == 0) {

				for (int j = 0; j < hakDelFirm.length; j++) {
					try {
						// PRÜFVORGANG

						String sql1 = "SELECT bundesnr FROM material WHERE bundesnr="
								+ hakDelFirm[j] + " LIMIT 1";
						ResultSet rs1 = con.mysql_query(sql1);
						if (rs1.next()) {
							JOptionPane
									.showMessageDialog(
											null,
											"Mindestens eine der ausgewählten Bundesgruppen wird bei den Materialien verwendet und kann nicht gelöscht werden!");
							rs1.close();
							return;
						}
						rs1.close();

						// LÖSCHVORGANG

						String sql = "DELETE FROM bundesnr WHERE id = "
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