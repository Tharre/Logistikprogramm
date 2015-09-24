package Logistik;

import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

/**
 * Ansicht in MainPanel je nach Auswahl der Abfrage, werden Kopfdaten von
 * Bestellabfragen angezeigt Farbliche Abstimmung, je nach Status der
 * Bestellanforderung
 */
public class AnzBanfA extends LayoutMainPanel implements ActionListener {
	/**
	 * Buttons zum Starten verschiedener Abfragen
	 */
	public JButton start1, start2, start3, start4;

	/**
	 * Button zum Starten der Detailansicht
	 */
	public JButton anzeigen = new JButton("Details anzeigen");

	/**
	 * Button zum aktualisieren der Ansicht
	 */
	public JButton refresh = new JButton("aktualisieren");

	public JButton drucken = new JButton("Drucken");

	/**
	 * Array mit angehakten BANFs
	 */
	public Object[] hakDet;

	public AnzTabelleA det;
	public String refWhat = "";
	public JScrollPane sc;
	public JPanel bts = new JPanel();

	public AnzBanfA(String typ, UserImport user) {
		super(user);

		anzeigen.setEnabled(false);
		refresh.setEnabled(false);
		drucken.setEnabled(false);
		setLayoutM(new BorderLayout());

		if (typ == "alle") {
			start1 = new JButton("Abfrage \"alle\" starten");
			addM(start1, BorderLayout.NORTH);
			start1.addActionListener(this);
		}// if

		if (typ == "fertig") {
			start2 = new JButton("Abfrage \"fertig\" starten");
			addM(start2, BorderLayout.NORTH);
			start2.addActionListener(this);
		}// if

		if (typ == "abgewiesen") {
			start3 = new JButton("Abfrage \"abgewiesen\" starten");
			addM(start3, BorderLayout.NORTH);
			start3.addActionListener(this);
		}// if

		if (typ == "zuBestellen") {
			start4 = new JButton("Abfrage \"zuBestellen\" starten");
			addM(start4, BorderLayout.NORTH);
			start4.addActionListener(this);
		}// if

		refWhat = typ;

		addM(new JLabel("\n"), BorderLayout.CENTER);
		bts.setLayout(new GridLayout(1, 3));
		bts.add(refresh);
		bts.add(anzeigen);
		bts.add(drucken);

		addM(bts, BorderLayout.SOUTH);

		anzeigen.addActionListener(this);
		refresh.addActionListener(this);
		drucken.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start1) {
			String query = "SELECT b.id,u.cn,b.kostenstelle,f.firmenname,b.status,b.datum FROM banf b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "b.id", "u.cn", "b.kostenstelle",
					"f.firmenname", "b.status", "b.datum" };
			String[] spaltenT = { "Banf ID", "Antragsteller", "Kostenstelle",
					"Firma", "Status", "Datum", "Details" };
			Class[] klass = { Integer.class, String.class, String.class,
					String.class, Integer.class, java.util.Date.class,
					Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 2);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start1.setVisible(false);
		}
		if (e.getSource() == start2) {
			String query = "SELECT b.id,u.cn,b.kostenstelle,f.firmenname,b.status,b.datum FROM banf b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND status=5 ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "b.id", "u.cn", "b.kostenstelle",
					"f.firmenname", "b.status", "b.datum" };
			String[] spaltenT = { "Banf ID", "Antragsteller", "Kostenstelle",
					"Firma", "Status", "Datum", "Details" };
			Class[] klass = { Integer.class, String.class, String.class,
					String.class, Integer.class, java.util.Date.class,
					Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 2);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start2.setVisible(false);
		}
		if (e.getSource() == start3) {
			String query = "SELECT b.id,u.cn,b.kostenstelle,f.firmenname,b.status,b.datum FROM banf b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND status=6 ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "b.id", "u.cn", "b.kostenstelle",
					"f.firmenname", "b.status", "b.datum" };
			String[] spaltenT = { "Banf ID", "Antragsteller", "Kostenstelle",
					"Firma", "Status", "Datum", "Details" };
			Class[] klass = { Integer.class, String.class, String.class,
					String.class, Integer.class, java.util.Date.class,
					Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 2);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start3.setVisible(false);
		}
		if (e.getSource() == start4) {
			String query = "SELECT b.id,u.cn,b.kostenstelle,f.firmenname,b.status,b.datum FROM banf b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND (status=3 OR status=4) ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "b.id", "u.cn", "b.kostenstelle",
					"f.firmenname", "b.status", "b.datum" };
			String[] spaltenT = { "Banf ID", "Antragsteller", "Kostenstelle",
					"Firma", "Status", "Datum", "Details" };
			Class[] klass = { Integer.class, String.class, String.class,
					String.class, Integer.class, java.util.Date.class,
					Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 2);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start4.setVisible(false);
		}

		/*
		 * repaint(); revalidate();
		 */
		/*
		 * Row[] detR = det.getRows(); for (int i = 0; i < detR.length; i++) {
		 * String s = detR[i].getData(4).toString(); if (s.equals("6") ||
		 * s.equals("abgewiesen")) { det.setValueAt("abgewiesen", i, 4);
		 * 
		 * } if (s.equals("5") || s.equals("fertig")) { det.setValueAt("fertig",
		 * i, 4);
		 * 
		 * } if (s.equals("4") || s.equals("in Bearbeitung")) {
		 * det.setValueAt("in Bearbeitung", i, 4);
		 * 
		 * } if (s.equals("3") || s.equals("nicht bestellt")) {
		 * det.setValueAt("nicht bestellt", i, 4);
		 * 
		 * } if (s.equals("15") || s.equals("gelöscht")) {
		 * det.setValueAt("gelöscht", i, 4);
		 * 
		 * }
		 * 
		 * }
		 */

		anzeigen.setEnabled(true);
		refresh.setEnabled(true);
		drucken.setEnabled(true);

		if (e.getSource() == anzeigen) {
			hakDet = det.getKlicked("Banf ID", "Details");
			new AnzBanfDetail(con, hakDet);

		}

		if (e.getSource() == refresh) {
			removeMall();
			addM(bts, BorderLayout.SOUTH);
			if (refWhat.equals("alle")) {
				start1.doClick();
			}
			if (refWhat.equals("fertig")) {
				start2.doClick();
			}
			if (refWhat.equals("abgewiesen")) {
				start3.doClick();
			}
			if (refWhat.equals("zuBestellen")) {
				start4.doClick();
			}

			repaint();
			revalidate();
		}

		if (e.getSource() == drucken) {
			hakDet = det.getKlicked("Banf ID", "Details");
			new Drucken(new AnzBanfDetail(con, hakDet));

		}
	}

}// AnzeigeBanf