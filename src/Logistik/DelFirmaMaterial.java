package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Klasse zum L�schen von Firma-Material-Verbindungen Listet alle
 * Firma-Material-Verbindungen auf + Checkbox Nach dem Klick auf den
 * L�schen-Button werden die angehakten gel�scht
 */

public class DelFirmaMaterial extends LayoutMainPanel implements ActionListener {
	public AnzTabelleA df;
	public JButton del;

	public DelFirmaMaterial(UserImport user) {
		super(user);
		del = new JButton("L�schen");
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
				"materialname", "l�schen" };
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
							"Wollen Sie die Firmen-Material-Verbindung l�schen?\nDies kann zu Fehlfunktionen in der BANF f�hren! (Sollte diese Verbindung dort verwendet werden)",
							"l�schen", JOptionPane.YES_NO_OPTION);
			if (i == 0) {

				Object[] hakDelFirm = df.getKlicked("id", "l�schen");

				for (int j = 0; j < hakDelFirm.length; j++) {
					try {
						// L�SCHVORGANG

						String sql = "DELETE FROM firma_material WHERE id = "
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