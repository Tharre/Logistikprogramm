package Logistik;

import java.sql.*;
import javax.swing.*;

import java.awt.*;

/**
 * Zeigt die Daten alle Bundesgruppen in Hierarchischer Struktur an
 */

public class AnzBundesGruppe extends LayoutMainPanel {

	private LayoutLevelPanel p;

	public AnzBundesGruppe(UserImport user) {
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
			JLabel lbl = new JLabel(rs.getString("nr"));
			lbl.setBorder(null);
			pn.add(lbl);
			lbl = new JLabel(rs.getString("bezeichnung"));
			lbl.setBorder(null);
			pn.add(lbl);
			p.addVisible(pn, lvl, true);
			;
			String qry = "SELECT id,nr, bezeichnung FROM bundesnr WHERE uebergruppe="
					+ rs.getString("id") + ";";
			ResultSet rs2 = con.mysql_query(qry);
			addSelect(rs2, lvl + 1);
			rs2.close();
		}

		rs.close();
	}

	public void addSelects() {
		int lvl = 0;
		try {
			String qry = "SELECT id,nr, bezeichnung FROM bundesnr WHERE uebergruppe IS null;";
			ResultSet rs = con.mysql_query(qry);
			addSelect(rs, lvl);
		} catch (Exception e) {
		e.getMessage();
		}
	}
}