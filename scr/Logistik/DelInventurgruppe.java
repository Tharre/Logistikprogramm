package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Klasse zum L�schen von Inventurgruppen Listet alle Inventurgruppen auf +
 * Checkbox Nach dem Klick auf den L�schen-Button werden die angehakten gel�scht
 */

public class DelInventurgruppe extends LayoutMainPanel implements
		ActionListener {
	public AnzTabelleA df;
	public JButton del;

	public DelInventurgruppe(UserImport user) {
		super(user);
		del = new JButton("L�schen");
		del.addActionListener(this);
		activate();
	}

	@Override
	public void activate() {
		removeMall();
		String qry = "SELECT * FROM inventurgruppe ORDER BY bezeichnung";
		ResultSet rs = con.mysql_query(qry);
		String[] spalten = { "id", "bezeichnung" };
		String[] spalten2 = { "id", "bezeichnung", "l�schen" };
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
			Object[] hakDelFirm = df.getKlicked("id", "l�schen");
			if (hakDelFirm.length == 0) {
				new MessageError("Mindestens eine Inventurgruppe ausw�hlen!");
				return;
			}
			int i = JOptionPane.showConfirmDialog(null,
					"Wollen Sie die Inventurgruppe(n) l�schen?", "l�schen",
					JOptionPane.YES_NO_OPTION);
			if (i == 0) {

				for (int j = 0; j < hakDelFirm.length; j++) {
					try {
						// PR�FVORGANG

						String sql1 = "SELECT inventurgruppe FROM material WHERE inventurgruppe="
								+ hakDelFirm[j] + " LIMIT 1";
						ResultSet rs1 = con.mysql_query(sql1);
						if (rs1.next()) {
							JOptionPane
									.showMessageDialog(
											null,
											"Mindestens eine der ausgew�hlten Inventurgruppen wird in den Materialien verwendet und kann nicht gel�scht werden!");
							rs1.close();
							return;
						}
						rs1.close();

						// L�SCHVORGANG

						String sql = "DELETE FROM inventurgruppe WHERE id = "
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