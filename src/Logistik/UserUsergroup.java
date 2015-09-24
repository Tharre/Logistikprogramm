package Logistik;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Zum zuordnen eines Users zu einer Usergruppe
 */

public class UserUsergroup extends LayoutMainPanel implements ActionListener {
	private SelectInput i;
	private SelectInput fs;
	private JButton save, clear;

	public UserUsergroup(UserImport user) {
		super(user);
		LayoutForm f = new LayoutForm();
		Input u = new Input(15, "");
		i = new SelectInput(con, u, "ldap_user", new String[] { "cn", "name" },
				"cn", "cn", user);
		Input g = new Input(15, "");
		fs = new SelectInput(con, g, "usergroup",
				new String[] { "id", "name" }, "id", "name", user);
		fs.setTextEnabled(false);
		i.setTextEnabled(false);
		save = new JButton("Speichern");
		clear = new JButton("Abbrechen");
		save.addActionListener(this);
		clear.addActionListener(this);
		f.addRight(new JLabel("User"));
		f.addLeftSelect(i);
		f.br();
		f.addRight(new JLabel("Usergroup"));
		f.addLeftSelect(fs);
		f.br();
		f.addRight(save);
		f.addLeft(clear);
		addM(f);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clear) {
			clear();
		}
		if (e.getSource() == save) {
			save();
		}
	}

	public void clear() {
		i.setText("", "");
		fs.setText("", "");
	}

	public void save() {
		if (i.getValue().equals("") || fs.getValue().equals("")) {
			new MessageError("Bitte alle Felder ausfüllen!");
			return;
		}
		String qry = "SELECT cn FROM user WHERE cn LIKE '" + i.getValue()
				+ "';";
		ResultSet rs = con.mysql_query(qry);
		try {
			String q2 = "";
			if (rs.next()) {
				q2 = "UPDATE user SET usergroup=" + fs.getValue()
						+ " WHERE cn LIKE '" + i.getValue() + "';";
			} else {
				q2 = "INSERT INTO user (cn, usergroup) VALUES ('"
						+ i.getValue() + "', " + fs.getValue() + ");";
			}
			if (con.mysql_update(q2) == -1) {
				new MessageError("Fehler beim Setzen der Usergruppe");
			} else {
				clear();
				new MessageSucess("Usergruppe erfolgreich zugewiesen");
			}

			rs.close();
		} catch (Exception e) {
		}
	}
}