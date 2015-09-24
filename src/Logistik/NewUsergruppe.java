package Logistik;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

/**
 * Hier kann man Usergruppen erstellen Name wird eingegeben Danach können mit
 * Checkboxen Rechte ausgewählt werden
 */

public class NewUsergruppe extends LayoutMainPanel implements ActionListener {
	/**
	 * Textfeld für Eingabe
	 */
	private Input name;
	/**
	 * Button zum Speichern/Löschen
	 */
	private JButton save, clear;
	/**
	 * Button für Auswahl aller/keiner Rechte
	 */
	private JButton alle, keine;
	private LayoutForm f;
	/**
	 * CkeckBox-Array für die Auswahl von Rechten
	 */
	private JCheckBox[] boxen;
	private String edit;

	public NewUsergruppe(UserImport user, String edit) {
		super(user);
		this.edit = edit;
		f = new LayoutForm();
		name = new Input(15, "name");
		save = new JButton("Speichern");
		alle = new JButton("Alle auswählen");
		keine = new JButton("Alle abwählen");
		clear = new JButton("Löschen");
		String[] rechte = Logistik.rechte.getRechte();
		boxen = new JCheckBox[rechte.length];
		f.addRight(new JLabel("Name"));
		f.addLeftInput(name);
		f.br();
		f.addItem(new JLabel(""));
		f.addItem(new JLabel(""));
		int i = 0;
		for (i = 0; i < rechte.length; i++) {
			f.addRight(new JLabel(rechte[i]));
			boxen[i] = new JCheckBox();
			f.addLeft(boxen[i]);
			if (i % 2 == 0) {
				f.br();
			}
		}
		int s = i % 4;
		for (i = 0; i <= s; i++) {
			f.addItem(new JLabel(""));
		}
		f.br();
		f.addRight(alle);
		f.addCenter(keine);
		f.addCenter(save);
		// f.addLeft(clear);

		// scroll = new JScrollPane(f);
		// scroll.setVisible(true);
		addM(f);

		clear.addActionListener(this);
		save.addActionListener(this);
		alle.addActionListener(this);
		keine.addActionListener(this);
	}

	public NewUsergruppe(UserImport user) {
		this(user, null);
	}

	public void setEdit(String edit) {
		this.edit = edit;
		String qry = "SELECT name FROM usergroup WHERE id=" + edit + ";";
		try {
			ResultSet rs = con.mysql_query(qry);
			rs.next();
			name.setText(rs.getString("name"));
			name.canHave(rs.getString("name"));
			for (int i = 0; i < boxen.length; i++) {
				String q = "SELECT id FROM rechte WHERE recht=" + i
						+ " AND usergroup=" + edit + ";";
				ResultSet rs2 = con.mysql_query(q);
				boxen[i].setSelected(rs2.next());

				rs2.close();
			}

			rs.close();

		} catch (Exception e) {
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clear) {
			clear();
		}
		if (e.getSource() == save) {
			save();
		}
		if (e.getSource() == alle) {
			for (int i = 0; i < boxen.length; i++) {
				boxen[i].setSelected(true);
			}
		}
		if (e.getSource() == keine) {
			for (int i = 0; i < boxen.length; i++) {
				boxen[i].setSelected(false);
			}
		}
	}

	public void clear() {
		f.clear();
	}

	public void save() {
		if (!f.check(con, "usergroup")) {
			return;
		}
		if (edit == null) {
			String qry = "INSERT INTO usergroup (name) VALUES ("
					+ name.getValue() + ");";
			if (con.mysql_update(qry) == -1) {
				new MessageError("Fehler beim Eintragen der Usergruppe");
				return;
			}
			int[] check = getChecked();
			qry = "SELECT id FROM usergroup WHERE name=" + name.getValue()
					+ ";";
			try {
				ResultSet rs = con.mysql_query(qry);
				rs.next();

				int group = rs.getInt("id");
				for (int i = 0; i < check.length; i++) {
					qry = "INSERT INTO rechte (usergroup,recht) VALUES ("
							+ group + "," + check[i] + ")";
					con.mysql_update(qry);
				}
				new MessageSucess("Neue Usergruppe angelegt!");

				rs.close();

				return;
			} catch (Exception e) {
			}
		}
		String qry = "UPDATE usergroup SET name=" + name.getValue()
				+ " WHERE id=" + edit + ";";
		if (con.mysql_update(qry) == -1) {
			new MessageError("Fehler beim Ändern der Usergruppe!");
			return;
		}
		try {
			for (int i = 0; i < boxen.length; i++) {
				String q = "SELECT id FROM rechte WHERE recht=" + i
						+ " AND usergroup=" + edit + ";";
				ResultSet rs = con.mysql_query(q);
				if (boxen[i].isSelected() && !rs.next()) {
					String d = "INSERT INTO rechte(recht, usergroup) VALUES ("
							+ i + "," + edit + ");";
					con.mysql_update(d);
				}
				if (!boxen[i].isSelected() && rs.next()) {
					String d = "DELETE FROM rechte WHERE recht=" + i
							+ " AND usergroup=" + edit + ";";
					con.mysql_update(d);
				}

				rs.close();

			}
			new MessageSucess("Usergruppe erfolgreich bearbeitet!");

		} catch (Exception e) {
		}
	}

	public int[] getChecked() {
		ArrayList ch = new ArrayList();
		for (int i = 0; i < boxen.length; i++) {
			if (boxen[i].isSelected()) {
				ch.add(new Integer(i));
			}
		}
		int[] is = new int[ch.size()];
		for (int i = 0; i < ch.size(); i++) {
			Integer inz = (Integer) ch.get(i);
			is[i] = inz.intValue();
		}
		return is;

	}
}
