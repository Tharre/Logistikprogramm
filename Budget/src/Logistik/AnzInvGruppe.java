package Logistik;

import java.sql.*;
import javax.swing.*;

import java.awt.*;

/**
 * Zeigt die Daten aller Inventurgruppen in hierarchischer Struktur an
 */

public class AnzInvGruppe extends LayoutMainPanel {

	private LayoutLevelPanel p;

	public AnzInvGruppe(UserImport user) {
		super(user);
		activate();
	}

	@Override
	public void activate() {
		removeMall();
		setLayoutM(new GridLayout());

		p = new LayoutLevelPanel();
		addSelects();
		addM(p);
		revalidate();
		repaint();
	}

	public void addSelect(ResultSet rs, int lvl) throws Exception {
		while (rs.next()) {
			JPanel pn = new JPanel();
			JLabel lbl = new JLabel(rs.getString("id"));
			lbl.setBorder(null);
			pn.add(lbl);
			lbl = new JLabel(rs.getString("bezeichnung"));
			lbl.setBorder(null);
			pn.add(lbl);
			p.addVisible(pn, lvl, true);
			;
			String qry = "SELECT id, bezeichnung FROM inventurgruppe WHERE uebergruppe="
					+ rs.getString("id") + " order by bezeichnung;";
			ResultSet rs2 = con.mysql_query(qry);
			addSelect(rs2, lvl + 1);
			rs2.close();
		}

		rs.close();
	}

	public void addSelects() {
		int lvl = 0;
		try {
			String qry = "SELECT id, bezeichnung FROM inventurgruppe WHERE uebergruppe IS null order by bezeichnung;";
			ResultSet rs = con.mysql_query(qry);
			addSelect(rs, lvl);
			rs.close();

		} catch (Exception e) {
			e.getMessage();
		}
	}
}