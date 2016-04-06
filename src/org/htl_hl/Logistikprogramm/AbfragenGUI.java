package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import org.jooq.Table;
import org.jooq.TableRecord;

import static sql.generated.logistik_test.Tables.*;


public class AbfragenGUI implements Tab, ActionListener {
	private final String[] SABF =
			{"selectMats","selectMatsWhDateIsBtwXnY","selectMatsWhBnIsX","selectMatsWhIgIsX","selectPriceWhMatIsX",
					"selectMatsWhFirmaIsX","selectFirmaWhMatsIsX","selectBanfWhUserIsX","selectBanfWhBestIsX",
					"selectBestWhBanfIsX"
			}; // Kurzbezeichnungen der Abfragen, zugleich die Methodennamen in der Abfragenklasse
	private final String[] SAT =
			{"Welche Materialien gibt es?", "Welche Materialien wurden zwischen den Daten x und y erstellt?",
					"Welches Material hat die Bundesnummer x?", "Welche Materialen sind in der Inventurgruppe x?",
					"größter / kleinster Preis für Material x",
					"Welche Materialien können bei der Firma x bestellt werden?",
					"Bei welchen Firmen kann man Material x bestellen?", "Alle Banfs von User x anzeigen",
					"Banfs zu einer Bestellung", "Bestellung zu einer Banf"
			}; // AnzeigeText zu den Abfragen
	private final int SPALTEN = 2; // Frei wählbarer Wert, wieviel Spalten man haben möchte
	private final int GRIDX = SPALTEN * 2 + 1; // Breite des Rasters

	private JLabel[][] platzhalter;
	private LConnection server;
	private TabManager tm;
	private int iTV = 1;

	public AbfragenGUI(LConnection server, TabManager tm) {
		this.server = server;
		this.tm = tm;
	}

	public String toString() {
		return getName();
	}

	@Override
	public String getName() {
		return "Abfragen";
	}

	@Override
	public JPanel getContent() {
		final int GRIDY = (SABF.length * 2 + 2) / SPALTEN; // Höhe des Rasters
		platzhalter = new JLabel[GRIDX][GRIDY];
		AbfrageButton[][] btAbf = new AbfrageButton[SPALTEN][(SABF.length + 1) / SPALTEN];

		for (int i = 0; i < SAT.length; i++) {
			SAT[i] = "<html><font size=\"5\">" + SAT[i] + "</font></html>";
			// damit die Buttons automatisch mehrzeilig werden können, und die Schrift größer ist
		}

		int si = 0;
		for (int i = 0; i < SPALTEN; i++) {
			for (int j = 0; j < btAbf[0].length; j++) {
				btAbf[i][j] = new AbfrageButton(SABF[si], SAT[si]);
				btAbf[i][j].addActionListener(this);
				si++;
			}
		}

		// Buttons zu den Abfragen
		JPanel panel = new JPanel();
		Container c = panel;
		c.setLayout(new GridLayout(GRIDY, GRIDX));

		for (int i = 0; i < GRIDY; i++) {
			if (i % 2 == 0)
				zeileZeichnen(c, i);
			else
				for (int j = 0; j < GRIDX; j++) {
					if (j % 2 == 0)
						elementZeichnen(c, i, j);
					else
						c.add(btAbf[j / 2][i / 2]);
				}
		}

		return panel;
	}

	private void zeileZeichnen(Container c, int y) {
		for (int j = 0; j < GRIDX; j++) {
			elementZeichnen(c, y, j);
		}
	}

	private void elementZeichnen(Container c, int y, int x) {
		platzhalter[x][y] = new JLabel();
		c.add(platzhalter[x][y]);
	}

	public void actionPerformed(ActionEvent e) {
		AbfrageButton bt = (AbfrageButton)e.getSource();

		try {
			if (bt.getAbfrage().equals(SABF[0])) {
				ResultWithCn r = Abfragen.selectMats();
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), MATERIAL);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[1])) {
				String s1 = JOptionPane.showInputDialog(null, "Datum(von-bis):", "2016_11_1-2016_12_31");
				String s2 = "";
				for(int i = s1.length()-1; i >= 0 && s1.charAt(i) != '-'; i--){
					s2 = s1.charAt(i) + s2;
				}
				Timestamp t1 = toTimestamp(s1);
				Timestamp t2 = toTimestamp(s2);
				ResultWithCn r = Abfragen.selectMatsWhDateIsBtwXnY(t1, t2);
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), MATERIAL);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[2])) {
				ResultWithCn r = Abfragen.selectMatsWhBnIsX(JOptionPane.showInputDialog(null, "Bundesnummer:"));
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), MATERIAL);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[3])) {
				int ig;
				try {
				ig = Integer.parseInt(JOptionPane.showInputDialog(null, "Inventurgruppe:"));
				} catch (NumberFormatException ex) {
					ig = 1;
				}
				ResultWithCn r = Abfragen.selectMatsWhIgIsX(ig);
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), MATERIAL);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[4])) {
				int mat;
				try{
				mat = Integer.parseInt(JOptionPane.showInputDialog(null, "Material:"));
				} catch (NumberFormatException ex) {
					mat = 1;
				}
				ResultWithCn r = Abfragen.selectPriceWhMatIsX(mat);
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), FIRMA_MATERIAL);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[5])) {
				int firma;
				try{
				firma = Integer.parseInt(JOptionPane.showInputDialog(null, "Firma:"));
				} catch (NumberFormatException ex) {
					firma = 1;
				}
				ResultWithCn r = Abfragen.selectMatsWhFirmaIsX(firma);
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), FIRMA_MATERIAL);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[6])) {
				int mat;
				try{
				mat = Integer.parseInt(JOptionPane.showInputDialog(null, "Material:"));
				} catch (NumberFormatException ex) {
					mat = 1;
				}
				ResultWithCn r = Abfragen.selectFirmaWhMatsIsX(mat);
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), FIRMA_MATERIAL);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[7])) {
				int user;
				try{
					user = Integer.parseInt(JOptionPane.showInputDialog(null, "User:"));
				} catch (NumberFormatException ex) {
					user = 1;
				}
				ResultWithCn r = Abfragen.selectBanfWhUserIsX(user);
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), BANF);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[8])) {
				int best;
				try{
				best = Integer.parseInt(JOptionPane.showInputDialog(null, "Bestellung:"));
				} catch (NumberFormatException ex) {
					best = 1;
				}
				ResultWithCn r = Abfragen.selectBanfWhBestIsX(best);
				System.out.println(r.getQuery());
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), BANFPOSITION);
				tm.addTab(tv);
			}
			else if (bt.getAbfrage().equals(SABF[9])) {
				int banf;
				try{
				banf = Integer.parseInt(JOptionPane.showInputDialog(null, "Banf:"));
				} catch (NumberFormatException ex) {
					banf = 1;
				}
				ResultWithCn r = Abfragen.selectBestWhBanfIsX(banf);
				JOOQTableView tv = new JOOQTableView(server, tm, "Abfrageergebnis " + iTV++, r.getCoullumnNames(),
						r.getQuery(), BANFPOSITION);
				tm.addTab(tv);
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private Timestamp toTimestamp(String s) {
		int[] time = new int[3];
		String hs;
		int j = 0;
		for(int i = 0; i < time.length; i++){
			hs = "";
			for(; j < s.length() && s.charAt(j) != '_' && s.charAt(j) != '-'; j++){
				hs += s.charAt(j);
			}
			j++;
			try {
				time[i] = Integer.parseInt(hs);
			} catch (NumberFormatException e) {
				if(i == 2)
				time[i] = 1;
			}
		}
		return new Timestamp(time[0]-1900, time[1]-1, time[2], 0, 0, 0, 0);
	}
}