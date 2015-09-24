package Logistik;

import java.awt.*;

import javax.swing.*;
import java.sql.*;

/**
 * Anzeige aller verfügbaren Bestellanforderungen beim Betsellablauf Farbliche
 * Kennzeichnung der Stati
 */
public class AnzBanf extends JPanel {
	/**
	 * Anzeige der Daten in Tabelle
	 */
	public AnzTabelleA aba;

	public AnzBanf(DBConnection con) {
		String sql = "SELECT b.id as bid,kostenstelle,cn,firmenname,status FROM banf b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND (status=3 OR status=4) order by b.id asc";
		String[] spaltenT = { "ID", "Kostenstelle", "Antragsteller", "Firma",
				"Status", "bearbeiten" };
		String[] spaltenDB = { "bid", "kostenstelle", "cn", "firmenname",
				"status" };
		ResultSet rs = con.mysql_query(sql);
		Class[] klass = { Integer.class, String.class, String.class,
				String.class, Integer.class, Boolean.class };
		aba = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 2);
		JScrollPane sc = new JScrollPane(aba);
		/*
		 * Row[] detR = aba.getRows(); for (int i = 0; i < detR.length; i++) {
		 * int s = Integer.parseInt(detR[i].getData(4).toString());
		 * 
		 * if (s == 1) { aba.setValueAt("im Lager", i, 4);
		 * detR[i].setColor(Color.YELLOW); } if (s == 3) {
		 * aba.setValueAt("nicht bestellt", i, 4);
		 * detR[i].setColor(Color.YELLOW); } if (s == 4) {
		 * aba.setValueAt("in Bearbeitung", i, 4);
		 * detR[i].setColor(Color.YELLOW); } if (s == 5) {
		 * aba.setValueAt("fertig", i, 4); detR[i].setColor(Color.GREEN); } if
		 * (s == 6) { aba.setValueAt("abgewiesen", i, 4); detR[i].setColor(new
		 * Color(245, 135, 129)); } }
		 */
		setLayout(new GridLayout(1, 1));
		add(sc);
	}
}