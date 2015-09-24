package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;

/**
 * Hier werden alle Usergruppen im Mainpanel angezeigt Anzeige durch
 * AnzeigeTable. Auswahl der Daten durch String qry
 */

public class AnzUsergroups extends LayoutMainPanel {
	/**
	 * Zum Anzeigen der Daten in Tabelle
	 */
	private AnzTabelleA anzeige;

	public AnzUsergroups(UserImport user) {
		super(user);
		activate();
	}

	@Override
	public void activate() {
		String qry = "SELECT * FROM usergroup ORDER BY name";
		ResultSet rs = con.mysql_query(qry);
		String[] spalten = { "id", "name" };
		String[] spalten2 = { "Usergruppe-ID", "Name" };
		Class[] classes = { Integer.class, String.class };
		anzeige = new AnzTabelleA(spalten, spalten2, classes, rs, 1);
		removeMall();
		JScrollPane p = new JScrollPane(anzeige);
		setLayoutM(new GridLayout(1, 1));
		addM(p);

		repaint();
	}
}