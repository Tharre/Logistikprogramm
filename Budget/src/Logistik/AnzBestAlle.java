package Logistik;

import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.Date;

/**
 * Ansicht in MainPanel je nach Auswahl der Abfrage, werden Kopfdaten von
 * Bestellungen angezeigt Farbliche Abstimmung, je nach Status der Bestellung
 */
public class AnzBestAlle extends LayoutMainPanel implements ActionListener {
	/**
	 * Buttons zum Starten verschiedener BudgetAbfragen
	 */
	public JButton start1, start2, start3, start4, start5, start6, start7,
			start8, start9;

	/**
	 * Button zum Starten der Detailansicht
	 */
	public JButton anzeigen = new JButton("Details anzeigen");
	
	/**
	 * Button zum aktualisieren der Ansicht
	 */
	public JButton refresh = new JButton("aktualisieren");
	
	/**
	 * Button zum Starten der Detailansicht
	 */
	public JButton pdfDrucken = new JButton("PDF drucken");

	/**
	 * Array mit angehakten Bestellungen
	 */
	public Object[] hakDet;

	/**
	 * zur Anzeige von Daten in Tabelle
	 */
	public AnzTabelleA det;

	/**
	 * Wert, der die Zuständigkeit speichert
	 */
	public int zustaendig = 0;

	public String refWhat = "";
	public JScrollPane sc;
	public JPanel bts = new JPanel();

	public AnzBestAlle(String typ, UserImport user) {
		super(user);

		anzeigen.setEnabled(false);
		refresh.setEnabled(false);
		pdfDrucken.setEnabled(false);
		
		setLayoutM(new BorderLayout());



		if (typ == "alle") {
			start1 = new JButton("Abfrage \"alle\" starten");
			addM(start1, BorderLayout.NORTH);
			start1.addActionListener(this);
			
			bts.setLayout(new GridLayout(1, 3));
			bts.add(refresh);
			bts.add(anzeigen);
			bts.add(pdfDrucken);
			pdfDrucken.addActionListener(this);
		}// if

		if (typ == "nicht abgeschickt") {
			start2 = new JButton("Abfrage \"nicht abgeschickt\" starten");
			addM(start2, BorderLayout.NORTH);
			start2.addActionListener(this);
			
			bts.setLayout(new GridLayout(1, 2));
			bts.add(refresh);
			bts.add(anzeigen);
		}// if

		if (typ == "abgeschickt") {
			start3 = new JButton("Abfrage \"abgeschickt\" starten");
			addM(start3, BorderLayout.NORTH);
			start3.addActionListener(this);
			
			bts.setLayout(new GridLayout(1, 2));
			bts.add(refresh);
			bts.add(anzeigen);
		}// if

		if (typ == "ausständig") {
			start4 = new JButton("Abfrage \"ausständig\" starten");
			addM(start4, BorderLayout.NORTH);
			start4.addActionListener(this);
			
			bts.setLayout(new GridLayout(1, 2));
			bts.add(refresh);
			bts.add(anzeigen);
		}// if

		if (typ == "fertig") {
			start5 = new JButton("Abfrage \"fertig\" starten");
			addM(start5, BorderLayout.NORTH);
			start5.addActionListener(this);
			
			bts.setLayout(new GridLayout(1, 2));
			bts.add(refresh);
			bts.add(anzeigen);
		}// if

		if (typ == "fehlerhaft") {
			start6 = new JButton("Abfrage \"fehlerhaft\" starten");
			addM(start6, BorderLayout.NORTH);
			start6.addActionListener(this);
			
			bts.setLayout(new GridLayout(1, 2));
			bts.add(refresh);
			bts.add(anzeigen);
		}// if

		/*if (typ == "suchen") {
			start7 = new JButton("Abfrage \"suchen\" starten");
			addM(start7, BorderLayout.NORTH);
			start7.addActionListener(this);
		}// if*/

		if (typ == "heuer") {
			start8 = new JButton("Abfrage \"heuer\" starten");
			addM(start8, BorderLayout.NORTH);
			start8.addActionListener(this);
			
			bts.setLayout(new GridLayout(1, 2));
			bts.add(refresh);
			bts.add(anzeigen);
		}//if
		
		if (typ == "komplett geliefert") {
			start9 = new JButton("Abfrage \"komplett geliefert\" starten");
			addM(start9, BorderLayout.NORTH);
			start9.addActionListener(this);
			
			bts.setLayout(new GridLayout(1, 2));
			bts.add(refresh);
			bts.add(anzeigen);
		}//if

		refWhat = typ;

		addM(new JLabel("\n"), BorderLayout.CENTER);
		addM(bts, BorderLayout.SOUTH);

		anzeigen.addActionListener(this);
		refresh.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent e) {
		//int laenge = 0;
		if (e.getSource() == start1) {
			String query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND (status=1 OR status=3 OR status=4 OR status=5 OR status=6 OR status=7 OR status=15) ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);

			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start1.setVisible(false);
			//laenge = klass.length;
			zustaendig = 0;
			
			pdfDrucken.setEnabled(true);
		}
		if (e.getSource() == start2) {
			String query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND status=1 ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start2.setVisible(false);
			//laenge = klass.length;
			zustaendig = 1;
		}
		if (e.getSource() == start3) {
			String query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND status=3 ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start3.setVisible(false);
			//laenge = klass.length;
			zustaendig = 0;
		}
		if (e.getSource() == start4) {
			String query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND ((status=3 AND statusbez=0) OR (status=4 AND (statusbez=1 OR statusbez=3)) OR (status=5 AND (statusbez=1 OR statusbez=3))) ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start4.setVisible(false);
			//laenge = klass.length;
			zustaendig = 2;
		}
		if (e.getSource() == start5) {
			String query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND (statusbez=4 AND (status=6 OR status=7)) ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start5.setVisible(false);
			//laenge = klass.length;
			zustaendig = 0;
		}
		if (e.getSource() == start6) {
			String query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND (status=6 AND (statusbez=2 OR statusbez=3 OR statusbez=4)) ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start6.setVisible(false);
			//laenge = klass.length;
			zustaendig = 0;
		}
		/*if (e.getSource() == start7) {
			try {
			String[] a = { "wnummer", "firmenname" };
			String auswahl = JOptionPane.showInputDialog(null,
					"Den Begriff \"wnummer\" oder \"firmenname\" eingeben!",
					"spezielle Suche", JOptionPane.QUESTION_MESSAGE);
			String query = "";
			if (auswahl.equals("wnummer")) {
				

					String eingabe = JOptionPane.showInputDialog(null,
							"WNummer eingeben: \n zB: w1-08", "Wnummer");
					query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND b.wnummer=\""
							+ eingabe + "\" ORDER BY datum DESC";
				
			} else if (auswahl.equals("firmenname")) {
					String eingabe = JOptionPane.showInputDialog(null,
							"Firmenname eingeben: \n zB: Prema", "Firmenname");
					query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND f.firmenname=\""
							+ eingabe + "\" ORDER BY datum DESC";
			} else {
				start7.doClick();
			}

			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start7.setVisible(false);
			laenge = klass.length;
			zustaendig = 0;
			} catch (NullPointerException e1) {
				System.out.println("NullPointerException bei start7");
			}
		}*/

		if (e.getSource() == start8) {

			Date datum = new Date();
			int jahr = datum.getYear();
			Date datumD = new Date(jahr, 0, 0);

			String query1 = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND b.datum>"
					+ (datumD.getTime() / 1000)
					+ " and f.id=b.firma AND (status=1 OR status=3 OR status=4 OR status=5 OR status=6 OR status=7 OR status=15) ORDER BY datum DESC";

			ResultSet rs = con.mysql_query(query1);
			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start8.setVisible(false);
			//laenge = klass.length;
			zustaendig = 0;

		}
		if (e.getSource() == start9) {
			String query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND ((status=6 AND (statusbez=2 OR statusbez=3)) OR (status=7 AND (statusbez=2 OR statusbez=3))) ORDER BY datum DESC";
			ResultSet rs = con.mysql_query(query);
			String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
					"firmenname", "status", "statusbez" };
			String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
					"Antragsteller", "Firma", "Status Lieferung",
					"Status Bezahlung", "Details" };
			Class[] klass = { Integer.class, String.class,
					java.util.Date.class, String.class, String.class,
					Integer.class, Integer.class, Boolean.class };
			det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
			sc = new JScrollPane(det);
			addM(sc, BorderLayout.CENTER);
			start9.setVisible(false);
			//laenge = klass.length;
			zustaendig = 0;
		}

		anzeigen.setEnabled(true);
		refresh.setEnabled(true);

		if (e.getSource() == anzeigen) {

			hakDet = det.getKlicked("Bestell-ID", "Details");

			switch (zustaendig) {
			case 1:
				AnzBestDetail am = new AnzBestDetail(user, hakDet);
				break;
			case 2:// Lieferung ausständig
				AnzLager al = new AnzLager(con, hakDet, user.getConnectionKst());
				break;
			case 0:
				AnzBestDetailNeu abd = new AnzBestDetailNeu(con, hakDet);
				break;
			}

		}

		if (e.getSource() == refresh) {

			removeMall();

			addM(bts, BorderLayout.SOUTH);
			if (refWhat.equals("alle")) {
				start1.doClick();
			}
			if (refWhat.equals("nicht abgeschickt")) {
				start2.doClick();
			}
			if (refWhat.equals("abgeschickt")) {
				start3.doClick();
			}
			if (refWhat.equals("ausständig")) {
				start4.doClick();
			}
			if (refWhat.equals("fertig")) {
				start5.doClick();
			}
			if (refWhat.equals("fehlerhaft")) {
				start6.doClick();
			}
			if (refWhat.equals("suchen")) {
				start7.doClick();
			}
			if (refWhat.equals("heuer")) {
				start8.doClick();
			}
			if (refWhat.equals("komplett geliefert")) {
				start9.doClick();
			}

			repaint();
			revalidate();
		}
		
		if(e.getSource()==pdfDrucken)
		{
			hakDet = det.getKlicked("Bestell-ID", "Details");
			PDFBestellung pdf = new PDFBestellung();
			pdf.printPDF(user, hakDet);
			
		}
	}
}// AnzeigeBest

