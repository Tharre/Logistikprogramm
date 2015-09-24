package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Klasse zum Löschen von Firma-Material-Verbindungen Listet alle
 * Firma-Material-Verbindungen auf + Checkbox Nach dem Klick auf den
 * Löschen-Button werden die angehakten gelöscht
 */

public class DelFirmaMaterial extends LayoutMainPanel implements ActionListener {
	public AnzTabelleA df;
	public JButton del;

	public DelFirmaMaterial(UserImport user) {
		super(user);
		del = new JButton("Löschen");
		del.addActionListener(this);
		activate();
	}

	@Override
	public void activate() {
		removeMall();
		String qry = "SELECT * FROM firma_material fm, firma f, material m WHERE fm.firma=f.id and fm.material=m.id ORDER BY firmenname";
		ResultSet rs = con.mysql_query(qry);
		String[] spalten = { "id", "fm.firma", "f.firmenname", "fm.material",
				"m.bezeichnung" };
		String[] spalten2 = { "id", "firmenid", "firmenname", "materialid",
				"materialname", "löschen" };
		Class[] classes = { Integer.class, Integer.class, String.class,
				Integer.class, String.class, Boolean.class };
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
			int i = JOptionPane
					.showConfirmDialog(
							null,
							"Wollen Sie die Firmen-Material-Verbindung löschen?\nDies kann zu Fehlfunktionen in der BANF führen! (Sollte diese Verbindung dort verwendet werden)",
							"löschen", JOptionPane.YES_NO_OPTION);
			if (i == 0) {

				Object[] hakDelFirm = df.getKlicked("id", "löschen");

				for (int j = 0; j < hakDelFirm.length; j++) {
					try {
						// LÖSCHVORGANG

						String sql = "DELETE FROM firma_material WHERE id = "
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