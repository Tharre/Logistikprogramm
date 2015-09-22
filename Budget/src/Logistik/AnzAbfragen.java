package Logistik;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

/*
 * Zeigt die Abfragen je nach dem gewählten Typ an.
 * Der Typ wird beim Erzeugen des Objects festgelegt und
 * beschreibt in welchen Tabellen die Abfragen erfolgen.
 */
public class AnzAbfragen extends LayoutMainPanel implements ActionListener {
	private JButton ug1, ug2, ug3;
	private JButton u1, u2, u3, u5;
	private JButton f1, f2, f3, f5, f6, f7, f9, fa, f10;
	private JButton m1, m2, m3, m4, m5, m7, m8, m9, m10, m12, m11;
	private JButton mf1, mf2, mf3, mf4;
	private JButton r1, r2, r3;
	private JButton l1, l2, l3, l4, l5, l6, l7;
	private JButton b1, b2, b3;
	private UserImport user;

	public AnzAbfragen(String typ, UserImport user) {
		super(user);
		this.user = user;
		setLayoutM(new GridLayout(14, 1));

		if (typ == "rechte") {
			addM(new JLabel("\n"));
			r1 = new JButton("Alle Rechte anzeigen");
			r2 = new JButton("Welche Rechte hat die Usergruppe X?");
			r3 = new JButton("Welche Usergruppen haben das Recht X?");
			addM(r1);
			addM(r2);
			addM(r3);
			r1.addActionListener(this);
			r2.addActionListener(this);
			r3.addActionListener(this);
		}

		if (typ == "banf") {
			addM(new JLabel("\n"));
			b1 = new JButton("Alle Banfs von User X anzeigen");
			b2 = new JButton("Banfs zu einer Bestellung");
			b3 = new JButton("Bestellung zu einer Banf");
			addM(b1);
			addM(b2);
			addM(b3);
			b1.addActionListener(this);
			b2.addActionListener(this);
			b3.addActionListener(this);
		}

		if (typ == "usergruppe") {
			ug1 = new JButton("Welche Usergruppen sind vorhanden?");
			ug2 = new JButton("Welche User sind in der Gruppe X?");
			ug3 = new JButton("In welchen Gruppen ist User X?");
			addM(new JLabel("\n"));
			addM(ug1);
			addM(ug2);
			addM(ug3);
			ug1.addActionListener(this);
			ug2.addActionListener(this);
			ug3.addActionListener(this);
		}// if
		if (typ == "user") {
			u1 = new JButton("Welche User sind vorhanden?");
			u2 = new JButton("User Suchen");
			u3 = new JButton("Welche User sind in der Gruppe X?");
			u5 = new JButton("Welche User sind in der Abteilung X?");
			addM(new JLabel("\n"));
			addM(u1);
			addM(u2);
			addM(u3);
			addM(u5);
			// addM(u6);
			u1.addActionListener(this);
			u2.addActionListener(this);
			u3.addActionListener(this);
			u5.addActionListener(this);
		}// if
		if (typ == "firma") {
			f1 = new JButton("Welche Firmen gibt es?");

			fa = new JButton("Firma suchen");
			f2 = new JButton("Welche Firmen haben ihren Standort in PLZ X?");
			f3 = new JButton("Von welchen Firmen gibt es eine Homepage?");
			// f4 = new JButton ("Von welchen Firmen gibt es eine TelNummer?");
			f5 = new JButton("Wie lautet unsere Kundennummer bei der Firma X?");
			f6 = new JButton(
					"Wie lauten unsere Kundennummern bei allen Firmen?");
			f7 = new JButton(
					"Wer ist unser Ansprechpartner und dessen TelNummer bei der Firma X?");
			f9 = new JButton(
					"Welche Firmen wurden zwischen den Daten X bis Y erstellt?");

			addM(new JLabel("\n"));
			addM(f1);

			addM(fa);
			addM(f2);
			addM(f3);
			// addM(f4);
			addM(f5);
			addM(f6);
			addM(f7);
			addM(f9);

			f1.addActionListener(this);

			fa.addActionListener(this);
			f2.addActionListener(this);
			f3.addActionListener(this);
			// f4.addActionListener(this);
			f5.addActionListener(this);
			f6.addActionListener(this);
			f7.addActionListener(this);
			f9.addActionListener(this);

		}
		if (typ == "material") {
			m1 = new JButton("Welche Materialien gibt es?");
			m2 = new JButton(
					"Welche Materialien wurden zwischen den Daten X und Y erstellt?");
			m3 = new JButton("Welches Material hat die Bundesnummer x?");
			m4 = new JButton("Welche Materialien sind in der Inventurgruppe x?");
			m5 = new JButton("Material suchen");
			m7 = new JButton("Materialien zusammengefasst nach Bundesgruppen");

			m9 = new JButton("größter / kleinster Preis für Material X");
			m11 = new JButton("Material mit Inventurgruppe suchen");

			addM(new JLabel("\n"));
			addM(m1);
			addM(m2);
			addM(m3);
			addM(m4);
			addM(m5);
			addM(m7);
			addM(m9);
			// addM(m11); /*gelöscht, weil gleich wie m4*/

			m1.addActionListener(this);
			m2.addActionListener(this);
			m3.addActionListener(this);
			m4.addActionListener(this);
			m5.addActionListener(this);
			m7.addActionListener(this);
			m9.addActionListener(this);
			m11.addActionListener(this);

		}
		if (typ == "material-firma") {
			mf1 = new JButton("Welches Material hat die Artikelnummer X?");
			mf2 = new JButton("Welches Material hat Preis zwischen X und Y?");
			mf3 = new JButton(
					"Welche Materialien können bei der Firma X bestellt werden?");
			mf4 = new JButton(
					"Bei welchen Firmen kann man Material X bestellen?");
			addM(new JLabel("\n"));
			addM(mf1);
			addM(mf2);
			addM(mf3);
			addM(mf4);
			mf1.addActionListener(this);
			mf2.addActionListener(this);
			mf3.addActionListener(this);
			mf4.addActionListener(this);
		}
		if (typ == "lager") {
			l1 = new JButton(
					"Welche Materialien hat der User X ab- und zugebucht?");
			// l2 = new JButton("Artikel wertmäßig anzeigen");
			l4 = new JButton(
					"Ausgehändigte Materialien nach Material zusammengefasst");
			l3 = new JButton(
					"Ausgehändigte Materialien nach Kostenstellen zusammengefasst");
			//l7 = new JButton("Bestellungen nach Kostenstellen zusammengefasst");
			l5 = new JButton("Buchungen für Kostenstelle X");
			m10 = new JButton(
					"Welche Materialien haben den Meldebestand unterschritten?");
			m8 = new JButton("Welche Materialien sind nicht im Lager?");
			f10 = new JButton("Zurück an Firmen");
			m12 = new JButton("Inventur");
			l6 = new JButton("Von wem wurde ein bestimmter Artikel abgefasst?");

			addM(l1);
			l1.addActionListener(this);
			// addM(l2);
			// l2.addActionListener(this);
			addM(l4);
			l4.addActionListener(this);
			addM(l3);
			l3.addActionListener(this);
			/*addM(l7);
			l7.addActionListener(this);*/
			addM(l5);
			l5.addActionListener(this);
			addM(m10);
			m10.addActionListener(this);
			addM(m8);
			m8.addActionListener(this);
			addM(f10);
			f10.addActionListener(this);
			addM(m12);
			m12.addActionListener(this);
			addM(l6);
			l6.addActionListener(this);
		}

	}// AnzeigeAbfragen

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == r1) {
			r1();
		}

		if (e.getSource() == r2) {
			String a = "Usergruppe eingeben";
			String[] headss = { "name", "id" };
			// Frame,Angezeigter Text, Titel, veränderbare Spalte, angezeigte
			// Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Usergruppe", headss, "id",
					"name", "usergroup", con, false);
			String g = in.getWert();
			r2(g);

		}

		if (e.getSource() == r3) {
			String a = "Recht eingeben";
			String[] headss = { "id", "name" };
			InputFrame in = new InputFrame(null, a, headss);
			try {
				int g = Integer.parseInt(in.getWert());
				r3(g);
			} catch (NumberFormatException ex) {
			}
		}

		if (e.getSource() == ug1) {
			ug1();
		}

		if (e.getSource() == ug2 || e.getSource() == u3) {
			String a = "Usergruppe eingeben";
			String[] headss = { "name", "id" };
			// Frame,Angezeigter Text, Titel, veränderbare Spalte, angezeigte
			// Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Usergruppe", headss, "id",
					"name", "usergroup", con, false);
			String g = in.getWert();
			ug2(g);
		}// if

		if (e.getSource() == ug3) {
			String a = "User eingeben";
			String[] headss = { "name", "cn" };
			// Frame,Angezeigter Text, Titel, veränderbare Spalte, angezeigte
			// Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "User", headss, "cn", "cn",
					"ldap_user", con, false);
			String g = in.getWert();
			if (g != null) {
				ug3(g);
			}
		}// if

		if (e.getSource() == u1) {
			u1();
		}// if

		if (e.getSource() == u2) {
			String a = "User eingeben";
			String[] headss = { "name", "cn" };
			// Frame,Angezeigter Text, Titel, veränderbare Spalte, angezeigte
			// Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "User", headss, "cn", "cn",
					"ldap_user", con, false);
			String g = in.getWert();
			if (g != null) {
				u2(g);
			}
		}// if

		if (e.getSource() == u5) {
			String g = JOptionPane
					.showInputDialog("Abteilung eingeben: \n  WI\n  MB\n  EL\n  ET");
			if (g != null) {
				u5(g);
			}
		}// if

		if (e.getSource() == f1) {
			f1();
		}

		if (e.getSource() == fa) {
			String a = "Firmenname/ID eingeben";
			String[] headss = { "id", "firmenname", "plz", "ort" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Firma", headss, "id",
					"firmenname", "firma", con, false);
			String g = in.getWert();
			if (g != null) {
				fa(g);
			}
		}
		if (e.getSource() == f2) {
			String g = JOptionPane.showInputDialog("PLZ eingeben: ");
			if (g != null) {
				f2(g);
			}
		}

		if (e.getSource() == f3) {
			f3();
		}

		/*
		 * if(e.getSource() ==f4) { f4(); }
		 */

		if (e.getSource() == f5) {
			String a = "Firmenname/ID eingeben";
			String[] headss = { "id", "firmenname" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Firma", headss, "id",
					"firmenname", "firma", con, false);
			String g = in.getWert();
			if (g != null) {
				f5(g);
			}
		}

		if (e.getSource() == f6) {
			f6();
		}

		if (e.getSource() == f7) {
			String a = "Firmenname/ID eingeben";
			String[] headss = { "id", "firmenname" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Firma", headss, "id",
					"firmenname", "firma", con, false);
			String g = in.getWert();
			if (g != null) {
				f7(g);
			}

		}

		if (e.getSource() == f9) {
			long g1 = datumEinlesen("von Datum eingeben");
			if (g1 > -1) {
				long g2 = datumEinlesen("bis Datum eingeben");
				if (g2 > -1) {
					f9(g1, g2);
				}
			}
		}

		if (e.getSource() == f10) {
			String a = "Firmenname/ID eingeben";
			String[] headss = { "id", "firmenname", "plz", "ort" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Firma", headss, "id",
					"firmenname", "firma", con, false);
			String firma = in.getWert();
			f10(firma);

		}

		if (e.getSource() == m1) {/* Abfrage: Welche Materialien gibt es */
			m1();
		}
		if (e.getSource() == m12) {
			m12();
		}
		if (e.getSource() == m2) {/*
								 * Abfrage: Welche Materialien wurden zwischen
								 * den Daten X und Y erstellt
								 */
			long g1 = datumEinlesen("von Datum eingeben");
			if (g1 > -1) {
				long g2 = datumEinlesen("bis Datum eingeben");
				if (g2 > -1) {
					m2(g1, g2);
				}
			}
		}
		if (e.getSource() == m3) {/*
								 * Abfrage: Welches Material hat die
								 * Bundesnummer x
								 */
			String a = "Bundesgruppe wählen";
			InputFrame in = new InputFrame(null, a, "Bundesgruppe", con, 1);
			String g = in.getWert();
			if (g != null) {
				m3(g);
			}
		}
		if (e.getSource() == m4) {/*
								 * Abfrage: Welche Materialien sind in der
								 * Inventurgruppe x
								 */
			String a = "Inventurgruppe wählen";
			InputFrame in = new InputFrame(null, a, "Inventurgruppe", con);
			String g = in.getWert();
			if (g != null) {
				m4(g);
			}
		}

		if (e.getSource() == m5) {
			String a = "Material/ID eingeben";
			String[] headss = { "id", "bezeichnung", "bundesnr",
					"inventurgruppe", "stueck" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Material", headss, "id",
					"bezeichnung", "material", con, false);
			String g = in.getWert();
			if (g != null) {
				m5(g);
			}
		}

		if (e.getSource() == m7) {
			m7();
		}

		if (e.getSource() == m8) {
			m8();
		}

		if (e.getSource() == m9) {
			String a = "Material/ID eingeben";
			String[] headss = { "id", "bezeichnung" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Material", headss, "id",
					"bezeichnung", "material", con, true);
			String g = in.getWert();
			if (g != null) {
				m9(g);
			}
		}

		if (e.getSource() == m10) {
			m10();
		}
		if (e.getSource() == m11) {
			m11();
		}

		if (e.getSource() == mf1) {
			String g = JOptionPane.showInputDialog("Artikelnr. eingeben: ");
			if (g != null) {
				mf1(g);
			}
		}

		if (e.getSource() == mf2) {
			double p1 = Double.parseDouble(JOptionPane
					.showInputDialog("Preisuntergrenze: "));
			double p2 = Double.parseDouble(JOptionPane
					.showInputDialog("Preisobergrenze: "));
			mf2(p1, p2);
		}

		if (e.getSource() == mf3) {
			String a = "Firmenname/ID eingeben";
			String[] headss = { "id", "firmenname" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Firma", headss, "id",
					"firmenname", "firma", con, false);
			String g = in.getWert();
			if (g != null) {
				mf3(g);
			}
		}

		if (e.getSource() == mf4) {
			String a = "Material/ID eingeben";
			String[] headss = { "id", "bezeichnung" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Material", headss, "id",
					"bezeichnung", "material", con, false);
			String g = in.getWert();
			if (g != null) {
				mf4(g);
			}
		}

		if (e.getSource() == l1) {/*
								 * Abfrage: Welche Materialien hat der User X
								 * ab- und zugebucht
								 */
			String a = "User eingeben";
			String[] headss = { "name", "cn" };
			// Frame,Angezeigter Text, Titel, veränderbare Spalte, angezeigte
			// Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "User", headss, "cn", "cn",
					"ldap_user", con, false);
			String g = in.getWert();
			if (g != null) {
				l1(g);
			}
		}// if
		if (e.getSource() == l2) {
			l2();
		}// if
		if (e.getSource() == l3) {
			l3();
		}// if
		/*if (e.getSource() == l7) {
			l7();
		}// if*/
		if (e.getSource() == l4) {
			l4();
		}// if
		if (e.getSource() == l5) {
			String a = "Kostenstelle wählen";
			InputFrame in = new InputFrame(null, a, "Kostenstelle", con, user);
			String g = in.getKst();
			if (g != null) {
				l5(g);
			}
		}// if
		if (e.getSource() == l6) {
			String a = "Artikel eingeben";
			String[] headss = { "id", "bezeichnung" };
			// Frame,Angezeigter Text, Tabellennamen, veränderbare Spalte,
			// angezeigte Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "Material", headss, "id",
					"bezeichnung", "material", con, false);
			String g = in.getWert();
			if (g != null) {
				l6(g);
			}

		}
		if (e.getSource() == b1) {
			String a = "User eingeben";
			String[] headss = { "name", "cn" };
			// Frame,Angezeigter Text, Titel, veränderbare Spalte, angezeigte
			// Spalte, Tabelle, conection
			InputFrame in = new InputFrame(null, a, "User", headss, "cn", "cn",
					"ldap_user", con, false);
			String g = in.getWert();
			if (g != null) {
				b1(g);
			}
		}// if

		if (e.getSource() == b2) {
			String g = JOptionPane.showInputDialog("W-Nummer eingeben");
			if (g != null) {
				b2(g);
			}
		}// if

		if (e.getSource() == b3) {
			String g = JOptionPane.showInputDialog("Banf ID eingeben");
			int id = 0;
			try {
				id = Integer.parseInt(g);
			} catch (NumberFormatException n) {
			}
			if (g != null) {
				b3(id);
			}
		}// if
	}

	public void r1() {
		String[] sn = { "id", "Recht" };
		Class[] cl = { Integer.class, String.class };
		AnzRechteB r = new AnzRechteB();
		new AnzRechteA(sn, cl, r.getRechte());
	}

	public void r2(String g) {
		String query = "SELECT r.id, ug.name, r.recht FROM usergroup ug, rechte r WHERE ug.id=r.usergroup AND ug.name=\""
				+ g + "\" ORDER BY r.id ASC";
		ResultSet rs = con.mysql_query(query);
		Class[] classes = { String.class, String.class };
		AnzRechteB r = new AnzRechteB();
		String[] spalten = { "name", "recht" };
		String[] spaltennamen = { "Usergruppe", "Recht" };
		new AnzRechteA(spaltennamen, spalten, classes, rs, r.getRechte());
	}

	public void r3(int g) {
		String query = "SELECT ug.id, ug.name FROM usergroup ug, rechte r WHERE ug.id=r.usergroup AND r.recht="
				+ g + " ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spaltennamen = { "ID", "Name", "Recht" };
		Class[] classes = { Integer.class, String.class, String.class };
		String[] spalten = { "id", "name" };
		AnzRechteB r = new AnzRechteB();
		new AnzRechteA(spaltennamen, classes, r.getRechte(), rs, spalten, g);
	}

	public void ug1() {
		String query = "SELECT * FROM usergroup ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spaltennamen = { "ID", "Name" };
		Class[] classes = { Integer.class, String.class };
		new AnzTabelleB(spaltennamen, spaltennamen, classes, rs);
	}

	public void ug2(String g) {
		String query = "SELECT lu.name,lu.cn, ug.name as ugname FROM ldap_user lu,usergroup ug, user u WHERE ug.name=\""
				+ g
				+ "\" AND u.usergroup=ug.id AND u.cn=lu.cn ORDER BY ug.id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "name", "cn", "ugname" };
		String[] spaltennamen = { "Name", "Login", "Usergruppe" };
		Class[] classes = { String.class, String.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void ug3(String g) {
		String query = "SELECT lu.name, lu.cn, ug. name as ugname FROM usergroup ug, user u, ldap_user lu WHERE u.cn  LIKE \""
				+ g + "\" AND u.usergroup=ug.id AND u.cn=lu.cn";
		String[] spalten = { "Name", "cn", "ugname" };
		String[] spaltennamen = { "Name", "Login", "Usergruppe" };
		Class[] classes = { String.class, String.class, String.class };
		ResultSet rs = con.mysql_query(query);
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void u1() {
		String query = "SELECT name, cn, mail FROM ldap_user ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "Name", "cn", "mail" };
		String[] spaltennamen = { "Name", "Login", "Mail" };
		Class[] classes = { String.class, String.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	// alt
	public void u2(String g) {
		String query = "SELECT name, cn, mail FROM ldap_user WHERE cn  LIKE \""
				+ g + "\" ORDER BY cn ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "Name", "cn", "mail" };
		String[] spaltennamen = { "Name", "Login", "Mail" };
		Class[] classes = { String.class, String.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);

	}

	public void u5(String g) {
		String query = "SELECT name, cn, mail FROM ldap_user WHERE cn  LIKE \".%.%."
				+ g + ".HTBL\" ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "Name", "cn", "mail" };
		String[] spaltennamen = { "Name", "Login", "Mail" };
		Class[] classes = { String.class, String.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void f1() {
		String query = "SELECT * FROM firma ORDER BY firmenname ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Firmenname", "Staat", "PLZ", "Ort",
				"Strasse" };// , "mail","telefon", "fax", "homepage",
		// "sachbearbeiter", "kundennr", "uid",
		// "erstellungsdatum" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, String.class };
		new AnzTabelleB(spalten, spalten, classes, rs);
	}

	public void fa(String g) {
		String query = "SELECT * FROM firma WHERE firmenname LIKE \"" + g
				+ "\" ORDER BY firmenname ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "Firmenname", "Staat", "PLZ", "Ort", "Strasse",
				"Mail", "Telefon", "Fax", "Homepage", "Sachbearbeiter",
				"Kundennr", "UID", "Erstellungsdatum" };
		Class[] classes = { String.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, java.util.Date.class };
		new AnzTabelleB(spalten, spalten, classes, rs);

	}

	public void f2(String g) {
		String query = "SELECT * FROM firma WHERE plz LIKE \"" + g
				+ "\" ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Firmenname", "Staat", "PLZ", "Ort",
				"Strasse", "Erstellungsdatum" };// , "mail","telefon", "fax",
		// "homepage", "sachbearbeiter",
		// "kundennr", "uid" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, String.class, java.util.Date.class };
		new AnzTabelleB(spalten, spalten, classes, rs);// ,spaltennamen);
	}

	public void f3() {
		String query = "SELECT * FROM firma WHERE homepage NOT LIKE \"\" ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Firmenname", "Staat", "PLZ", "Ort",
				"Strasse", "Homepage" };// , "mail","telefon", "fax",
		// "sachbearbeiter", "kundennr", "uid",
		// "erstellungsdatum" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, String.class, String.class };
		new AnzTabelleB(spalten, spalten, classes, rs);// ,spaltennamen);
	}

	/*
	 * public void f4() { String query =
	 * "SELECT * FROM firma WHERE telefon NOT LIKE \"\" ORDER BY id ASC";
	 * ResultSet rs = con.mysql_query(query); String[] spalten =
	 * {"ID","Firmenname", "Staat", "PLZ","Ort","Strasse","Telefon"};//, "mail",
	 * "fax", "homepage", "sachbearbeiter", "kundennr", "uid",
	 * "erstellungsdatum" }; Class[] classes =
	 * {Integer.class,String.class,String
	 * .class,String.class,String.class,String.class,String.class}; new
	 * TabelleAnzeigen(spalten,spalten,classes,rs);//,spaltennamen); }
	 */

	public void f5(String g) {
		String query = "SELECT * FROM firma WHERE firmenname LIKE \"" + g
				+ "\" ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Kundennr", "Firmenname", "Staat", "PLZ",
				"Ort", "Strasse" };// , "mail", "fax", "homepage",
		// "sachbearbeiter", "kundennr", "uid",
		// "erstellungsdatum" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, String.class, String.class };
		new AnzTabelleB(spalten, spalten, classes, rs);
	}

	public void f7(String g) {
		String query = "SELECT * FROM firma WHERE firmenname LIKE \"" + g
				+ "\" ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Firmenname", "Staat", "PLZ", "Ort",
				"Strasse", "Sachbearbeiter" };// , "mail", "fax", "homepage",
		// "sachbearbeiter", "kundennr",
		// "uid", "erstellungsdatum" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, String.class, String.class };
		new AnzTabelleB(spalten, spalten, classes, rs);
	}

	public void f6() {
		String query = "SELECT * FROM firma  ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Kundennr", "Firmenname", "Staat", "PLZ",
				"Ort", "Strasse" };// , "mail", "fax", "homepage",
		// "sachbearbeiter", "kundennr", "uid",
		// "erstellungsdatum" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, String.class, String.class };
		new AnzTabelleB(spalten, spalten, classes, rs);
	}

	public void f9(long d1, long d2) {
		String query = "SELECT * FROM firma  WHERE erstellungsdatum >=" + d1
				+ " AND erstellungsdatum<=" + d2 + " ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Kundennr", "Firmenname", "Staat", "PLZ",
				"Ort", "Strasse", "Erstellungsdatum" };// , "mail", "fax",
		// "homepage",
		// "sachbearbeiter",
		// "kundennr", "uid" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				java.util.Date.class, String.class };
		new AnzTabelleB(spalten, spalten, classes, rs);

	}

	public void f10(String firma) {

		String query = "select bf.id, m.bezeichnung, bf.stk, bf.kst, bf.datum from buchungen_firma bf, firma f, material m where f.firmenname like '"
				+ firma + "' and bf.firma=f.id and m.id=bf.material";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Stk", "Kst", "Datum" };

		Class[] classes = { Integer.class, String.class, Integer.class,
				String.class, java.util.Date.class };
		new AnzTabelleB(spalten, spalten, classes, rs);

	}

	/* Abfrage: Welche Materialien gibt es */
	public void m1() {
		String query = "SELECT round((max(fm.preisExkl)+(max(fm.preisExkl)/100*fm.mwst)),3) as preis,m.id as id, m.bezeichnung as bezeichnung,bu.bezeichnung as bundesnr, m.erstellungsdatum as erstellungsdatum, i.bezeichnung as inventurgruppe, round(m.stueck,3) as stueck, fm.einheit FROM bundesnr bu, material m, firma_material fm , inventurgruppe i Where m.id =fm.material AND i.id=m.inventurgruppe AND bu.id=m.bundesnr Group By m.id";
		ResultSet rs = con.mysql_query(query);

		String[] spalten = { "ID", "Bezeichnung", "Bundesnr", "Inventurgruppe",
				"Erstellungsdatum", "Stueck", "preis" };
		String[] spaltennamen = { "ID", "Bezeichnung", "Bundesnr",
				"Inventurgruppe", "Erstellungsdatum", "Menge",
				"max. Preis/Einheit [inkl.]" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, java.util.Date.class, Double.class, Double.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	private String[] spalten2 = { "Bezeichnung", "Bundesnr", "Inventurgruppe",
			"Stueck", "preis", "ges" };
	private String[] spaltennamen2 = { "Bezeichnung", "Bundesnr",
			"Inventurgruppe", "Stück", "PreisInkl/Stk", "Gesamtpreis" };
	Class[] classes2 = { String.class, String.class, String.class,
			Integer.class, Integer.class, Integer.class };
	ResultSet rs2;

	public void m12() {
		String query = "SELECT max(fm.preisExkl*(1+(fm.mwst/100))) as preis, max(fm.preisExkl*(1+(fm.mwst/100)))*m.stueck as ges,m.id as id, m.bezeichnung as bezeichnung, bu.nr as bundesnr, m.erstellungsdatum as erstellungsdatum, i.bezeichnung as inventurgruppe, m.stueck as stueck FROM material m, firma_material fm , inventurgruppe i, bundesnr bu Where m.id =fm.material AND i.id=m.inventurgruppe AND bu.id=m.bundesnr AND stueck>0 AND fm.preisExkl>0 AND m.fixkosten like '' Group By m.id ORDER BY m.bundesnr, m.bezeichnung ASC";
		rs2 = con.mysql_query(query);

		new AnzTabelleB(spalten2, spaltennamen2, classes2, rs2, con);

	}

	/* Abfrage: Welche Materialien wurden zwischen den Daten X und Y erstellt */
	public void m2(long d1, long d2) {

		String query = "SELECT m.*, bu.bezeichnung, bu.nr, fm.einheit FROM material m, bundesnr bu, firma_material fm WHERE erstellungsdatum >="
				+ d1
				+ " AND erstellungsdatum<="
				+ d2
				+ "  AND bu.id=m.bundesnr AND m.id=fm.material ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "m.bezeichnung", "bu.nr", "bu.bezeichnung",
				"Erstellungsdatum", "stueck", "einheit" };// ,
		// "inventurgruppe"};
		String[] spaltennamen = { "Material-ID", "Material", "Bundesnr.",
				"Bundesnr.-Bezeichnung", "Erstellungsdatum", "Menge", "Einheit" };
		Class[] classes = { Integer.class, String.class, Integer.class,
				String.class, java.util.Date.class, String.class, Double.class,
				String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	/* Abfrage: Welches Material hat die Bundesnummer x */
	public void m3(String g) {
		String query = "SELECT m.id, m.bezeichnung,bu.bezeichnung as bundesnr,m.erstellungsdatum, m.stueck, m.meldebestand, i.bezeichnung as inventurgruppe, fm.einheit FROM material m , inventurgruppe i, bundesnr bu, firma_material fm WHERE bu.bezeichnung LIKE \""
				+ g
				+ "\" AND bu.id=m.bundesnr AND i.id=m.inventurgruppe AND m.id=fm.material ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Bundesnr", "stueck",
				"einheit" };// ,
		// "inventurgruppe"};
		String[] spaltennamen = { "ID", "Bezeichnung", "Bundesnr", "Menge",
				"Einheit" };
		Class[] classes = { Integer.class, String.class, String.class,
				Double.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	/* Abfrage: Welche Materialien sind in der Inventurgruppe x */
	public void m4(String g) {
		String query = "SELECT m.id, m.bezeichnung,bu.bezeichnung as bundesnr,m.erstellungsdatum, m.stueck, m.meldebestand, i.bezeichnung as inventurgruppe, fm.einheit FROM material m , inventurgruppe i, bundesnr bu, firma_material fm WHERE i.bezeichnung LIKE \""
				+ g
				+ "\" AND i.id=m.inventurgruppe AND bu.id=m.bundesnr AND m.id=fm.material ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Bundesnr", "Inventurgruppe",
				"erstellungsdatum", "stueck", "meldebestand", "einheit" };
		String[] spaltennamen = { "ID", "Bezeichnung", "Bundesnr",
				"Inventurgruppe", "Erstellungsdatum", "Menge", "Meldebestand",
				"Einheit" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, java.util.Date.class, Double.class, Double.class,
				String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	// Abfrage: Material suchen
	public void m5(String g) {
		String query = "SELECT m.id, m.bezeichnung,bu.bezeichnung as bundesnr,m.erstellungsdatum, m.stueck, m.meldebestand, i.bezeichnung as inventurgruppe, fm.einheit FROM material m , inventurgruppe i, bundesnr bu, firma_material fm WHERE m.bezeichnung LIKE \""
				+ g
				+ "\" AND i.id=m.inventurgruppe AND bu.id=m.bundesnr AND m.id=fm.material ORDER BY id ASC";

		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Bundesnr", "inventurgruppe",
				"Erstellungsdatum", "stueck", "meldebestand", "einheit" };
		String[] spaltennamen = { "ID", "Bezeichnung", "Bundesnr",
				"Inventurgruppe", "Erstellungsdatum", "Stück", "Meldebestand",
				"Einheit" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, java.util.Date.class, Double.class, Double.class,
				String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	// Bundesgruppen
	public void m7() {
		String query = "SELECT m.id, m.bezeichnung,bu.bezeichnung as bundesnr,m.erstellungsdatum, m.stueck, m.meldebestand, i.bezeichnung as inventurgruppe FROM material m , inventurgruppe i, bundesnr bu WHERE i.id=m.inventurgruppe AND bu.id=m.bundesnr ORDER BY bundesnr ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Bundesnr", "Inventurgruppe",
				"Stueck" };
		String[] spaltennamen = { "ID", "Bezeichnung", "Bundesnr",
				"Inventurgruppe", "Stück" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, Integer.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void m8() {
		String query = "SELECT m.id, m.bezeichnung,m.bundesnr,m.erstellungsdatum, m.stueck, m.meldebestand, i.bezeichnung as inventurgruppe FROM material m , inventurgruppe i WHERE i.id=m.inventurgruppe AND m.stueck=0 ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Inventurgruppe", "Stueck" };
		String[] spaltennamen = { "ID", "Bezeichnung", "Inventurgruppe",
				"Stück" };
		Class[] classes = { Integer.class, String.class, String.class,
				Integer.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void m10() {
		String query = "SELECT m.id, m.bezeichnung,m.bundesnr,m.erstellungsdatum, m.stueck, m.meldebestand, i.bezeichnung as inventurgruppe FROM material m , inventurgruppe i WHERE i.id=m.inventurgruppe AND m.stueck<=m.meldebestand ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Inventurgruppe", "Stueck",
				"meldebestand" };
		String[] spaltennamen = { "ID", "Bezeichnung", "Inventurgruppe",
				"Stück", "Meldebestand" };
		Class[] classes = { Integer.class, String.class, String.class,
				Integer.class, Integer.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void m9(String g) {
		String query = "SELECT m.id, m.bezeichnung, i.bezeichnung as inventurgruppe,  fm.preisExkl as preis, f.firmenname as firma FROM firma_material fm, material m, inventurgruppe i , firma f WHERE m.bezeichnung = \""
				+ g
				+ "\" AND i.id=m.inventurgruppe AND fm.material=m.id AND f.id=fm.firma";

		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Inventurgruppe", "Preis",
				"Firma" };
		Class[] classes = { Integer.class, String.class, String.class,
				Integer.class, String.class };
		new AnzTabelleB(spalten, spalten, classes, rs);
	}

	public void m11() {
		String a = "Inventurgruppe wählen";
		InputFrame in = new InputFrame(null, a, "Inventurgruppe", con);
		String s = in.getWert();

		String query = "SELECT m.id, m.bezeichnung,bu.bezeichnung as bundesnr, m.erstellungsdatum, m.stueck, m.meldebestand, i.bezeichnung as inventurgruppe  FROM material m, inventurgruppe i, bundesnr bu  WHERE m.inventurgruppe=i.id AND i.bezeichnung LIKE \""
				+ s + "\" ORDER BY i.id ASC";
		if (s != null) {
			ResultSet rs = con.mysql_query(query);

			new LagerumbuchungenB(con, rs, LagerumbuchungenB.ANSCHAUEN, user, 1);
		}
	}

	public void mf1(String g) {
		String query = "SELECT fm.id AS id, fm.preisExkl AS pre, fm.mwst AS mwst, fm.einheit AS einheit, fm.ArtNr AS ArtNr, m.bezeichnung AS material, f.firmenname AS fname FROM firma_material fm, material m, firma f	WHERE ArtNr LIKE \""
				+ g
				+ "\"	AND fm.material = m.id		AND fm.firma = f.id		ORDER BY id ASC";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "ID", "ArtNr", "fname", "Material", "pre", "mwst",
				"Einheit" };// , "inventurgruppe"};
		String[] spaltennamen = { "ID", "ArtNr", "Firmenname", "Material",
				"Preis Exkl.", "mwst", "Einheit" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, Double.class, Integer.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void mf2(double p1, double p2) {
		String query = "SELECT fm.id as id, fm.preisExkl as preisExkl, m.bezeichnung as material, f.firmenname as firma, fm.artnr as artnr, mwst, einheit FROM firma_material fm, material m, firma f Where preisExkl >= "
				+ p1
				+ " AND preisExkl <="
				+ p2
				+ " AND m.id=fm.material AND f.id=fm.firma";

		ResultSet rs = con.mysql_query(query);
		String[] spaltennamen = { "ID", "ArtNr", "Material", "Firmenname",
				"Preis Exkl.", "mwst", "Einheit" };
		String[] spalten = { "ID", "artnr", "material", "firma", "preisExkl",
				"mwst", "Einheit" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, Double.class, Integer.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void mf3(String f) {
		String query = "SELECT fm.id as id, fm.preisExkl as preisExkl, m.bezeichnung as material, f.firmenname as firma, fm.artnr as artnr, mwst, einheit FROM firma_material fm, material m, firma f Where f.firmenname LIKE \""
				+ f + "\" AND m.id=fm.material AND f.id=fm.firma";

		ResultSet rs = con.mysql_query(query);
		String[] spaltennamen = { "ID", "ArtNr", "Material", "Firmenname",
				"Preis Exkl.", "mwst", "Einheit" };
		String[] spalten = { "ID", "artnr", "material", "firma", "preisExkl",
				"mwst", "einheit" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, Double.class, Integer.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void mf4(String f) {
		String query = "SELECT fm.id as id, fm.preisExkl as preisExkl, m.bezeichnung as material, f.firmenname as firma, fm.artnr as artnr, mwst, einheit FROM firma_material fm, material m, firma f Where m.bezeichnung LIKE \""
				+ f + "\" AND m.id=fm.material AND f.id=fm.firma";

		ResultSet rs = con.mysql_query(query);
		String[] spaltennamen = { "ID", "ArtNr", "Material", "Firmenname",
				"Preis Exkl.", "mwst", "Einheit" };
		String[] spalten = { "ID", "artnr", "material", "firma", "preisExkl",
				"mwst", "einheit" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, Double.class, Integer.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	/* Abfrage: Welche Materialien hat der User X ab- und zugebucht */
	public void l1(String g) {

		String query = "Select b.id, m.bezeichnung, (-b.stk) as Stueck, (CASE WHEN b.firma=0 THEN 'keine Angabe' ELSE fm.preisExkl END) as Stueckpreis, b.datum as Datum,u.name as User, fm.einheit from firma_material fm, buchungen b, material m, ldap_user u Where m.id=b.material AND u.cn=b.user AND b.user LIKE '"
				+ g
				+ "' AND ((b.firma=fm.firma AND b.material=fm.material) OR (b.firma=0 AND b.material=fm.material)) GROUP BY b.id";

		ResultSet rs = con.mysql_query(query);
		String[] spaltennamen = { "ID", "Bezeichnung", "Menge", "Einheit",
				"Preis/Einheit", "Datum", "User" };
		String[] spalten = { "ID", "Bezeichnung", "Stueck", "einheit",
				"Stueckpreis", "Datum", "User" };
		Class[] classes = { Integer.class, String.class, Double.class,
				String.class, Double.class, java.util.Date.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	/* Abfrage: Artikel wertmäßig anzeigen */
	public void l2() {
		String query = "SELECT m.bezeichnung, round(sum(-stk),3) as stueck, round((sum(-stk)*max(fm.preisExkl)*(1+fm.mwst/100)),3) as ges FROM buchungen bu , firma_material fm, material m Where bu.material=fm.material AND m.id=bu.material Group by bu.material ORDER BY ges DESC";

		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "Bezeichnung", "ges" };
		String[] spaltennamen = { "Bezeichnung", "Gesamtpreis" };
		Class[] classes = { String.class, Integer.class, Integer.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	/*
	 * public void l4() { String query =
	 * "SELECT m.bezeichnung,sum(-bu.stk) as stueck FROM buchungen bu, material m Where m.id=bu.material Group by m.id"
	 * ;
	 * 
	 * ResultSet rs = con.mysql_query(query); String[] spalten = {
	 * "Bezeichnung", "stueck"}; String[] spaltennamen = { "Bezeichnung",
	 * "Stück"}; Class[] classes = { String.class, Integer.class}; new
	 * AnzTabelleB(spalten, spaltennamen, classes, rs); }
	 */

	/** ausgehändigte Materialien nach Material zusammengefasst **/
	public void l4() {
		String query = "SELECT m.bezeichnung,f.firmenname, sum(-bu.stk) as stueck,fm.einheit, round((fm.preisExkl)*(1+fm.mwst/100),3) as preiseinheit, round((fm.preisExkl)*(1+fm.mwst/100)*sum(-bu.stk),3) as preisgesamt FROM buchungen bu, material m, firma_material fm,firma f Where m.id=bu.material and m.id=fm.material and f.id=fm.firma Group by fm.id";

		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "bezeichnung", "firmenname", "stueck", "einheit",
				"preiseinheit", "preisgesamt" };
		String[] spaltennamen = { "Material", "Firma", "Menge", "Einheit",
				"Preis/Einheit", "Preis gesamt" };
		Class[] classes = { String.class, String.class, Double.class,
				String.class, Double.class, Double.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	/** ausgehändigte Materialien nach Kostenstellen zusammengefasst **/
	public void l3() {
		String query = "SELECT bu.kst, m.bezeichnung, f.firmenname, sum(-bu.stk) as stueck,fm.einheit, round((fm.preisExkl)*(1+fm.mwst/100),3) as preiseinheit, round((fm.preisExkl)*sum(-bu.stk)*(1+fm.mwst/100),3) as preisgesamt FROM buchungen bu, material m, firma_material fm, firma f WHERE m.id=bu.material AND m.id=fm.material AND f.id=fm.firma Group By fm.id, kst ORDER BY kst";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "bu.kst", "bezeichnung", "firmenname", "stueck",
				"einheit", "preiseinheit", "preisgesamt" };
		String[] spaltennamen = { "Kostenstelle", "Material", "Firma", "Menge",
				"Einheit", "Preis/Einheit", "Preis gesamt" };
		Class[] classes = { String.class, String.class, String.class,
				Double.class, String.class, Double.class, Double.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	/*
	public void l7() {
		String query = "SELECT b.kst, m.bezeichnung as material, m.id, sum(-stk) as stueck,round(max(fm.preisExkl)*(1+fm.mwst/100),3) as Euro, round((max(fm.preisExkl)*sum(-stk)*(1+fm.mwst/100)),3) as ges FROM buchungen b, material m, firma_material fm WHERE m.id=b.material AND m.id=fm.material Group By material, kst ORDER BY kst";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "kst", "material", "stueck", "euro", "ges" };
		String[] spaltennamen = { "Kostenstelle", "Bezeichnung", "Stück",
				"Euro/Stk", "Euro ges." };
		Class[] classes = { String.class, String.class, Integer.class,
				Integer.class, Integer.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}*/



	public void l5(String g) {
		String gKurz="";
        gKurz = g.substring(0, 20);
		
		String query = "SELECT material.bezeichnung AS material, -buchungen.stk AS Stk, round(firma_material.preisExkl*(1+firma_material.mwst/100),3) AS preisInkl, round(firma_material.preisExkl * (1+firma_material.mwst/100)  *(- buchungen.stk),3) AS Gesamtpreis,	buchungen.user AS User, buchungen.datum AS Datum FROM material JOIN buchungen ON material.id = buchungen.material JOIN firma_material ON material.id = firma_material.material WHERE buchungen.kst LIKE '"
				+ gKurz + "%' order by datum desc";
		
		String[] spalten = { "material", "stk", "preisInkl",
				"gesamtpreis", "user", "datum"};
		String[] spaltennamen = { "Material", "Stück",
				"Stückpreis inkl.", "Preis gesamt", "User", "Datum"};
		Class[] classes = { String.class, Double.class,
				Double.class, Double.class, String.class, java.util.Date.class};
		ResultSet rs;
		
		rs = con.mysql_query(query);
		
		new AnzTabelleB(spalten, spaltennamen, classes, rs, g);
	}

	public void l6(String g) {
		String query = "select b.user,b.datum from material m, buchungen b where b.material = m.id and m.bezeichnung like '"
				+ g + "'";

		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "user", "datum" };

		String[] spaltennamen = { "User", "Datum" };

		Class[] classes = { String.class, java.util.Date.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);

	}

	public void b1(String g) {
		String query = "SELECT banf.id as banfid, antragsteller, kostenstelle, firmenname from banf, firma where firma.id=banf.firma AND antragsteller LIKE'"
				+ g + "'";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "banfid", "antragsteller", "kostenstelle",
				"firmenname" };
		String[] spaltennamen = { "Banf", "Anragsteller", "Kostenstelle",
				"Firmenname" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void b2(String g) {

		String query = "select banf.id as \"BANF-ID\" , banf.antragsteller as antragsteller, banf.kostenstelle as kostenstelle, bestellung.wnummer as wnummer, firma.firmenname as firmenname, (((banfpos.mwst/100)+1)*banfpos.preisExkl)*banfpos.menge as \"gesamt\" from banf, bestellung, bestpos, banfpos, firma where wnummer like '"
				+ g
				+ "'  and bestpos.bestellId=bestellung.bestId  and banfpos.banfposnr=bestpos.banfposnr  and banf.id=banfpos.banf  and bestellung.firma=firma.id";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "BANF-ID", "antragsteller", "wnummer",
				"kostenstelle", "firmenname", "gesamt" };
		String[] spaltennamen = { "Banf", "Anragsteller", "W-Nummer",
				"Kostenstelle", "Firmenname", "Preis inkl." };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, Double.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public void b3(int g) {
		String query = "select distinct "
				+ g
				+ " as \"BANF-ID\", best.wnummer, best.budget, b.kostenstelle, b.antragsteller,f.firmenname from banf b,banfpos bp,bestpos bestp,bestellung best, firma f "
				+ "where ("
				+ g
				+ "=bp.banf) AND (bestp.banfposnr=bp.banfposnr) AND (bestp.bestellId=best.bestId) AND (best.firma=f.id) AND ("
				+ g + "=b.id);";
		ResultSet rs = con.mysql_query(query);
		String[] spalten = { "BANF-ID", "wnummer", "budget", "kostenstelle",
				"antragsteller", "firmenname" };
		String[] spaltennamen = { "Banf Id", "W-Nummer", "Budget",
				"Kostenstelle", "Antragsteller", "Firma" };
		Class[] classes = { Integer.class, String.class, String.class,
				String.class, String.class, String.class };
		new AnzTabelleB(spalten, spaltennamen, classes, rs);
	}

	public long datumEinlesen(String g) {
		int d1, m1, y1;
		String g1 = "";
		while (new StringTokenizer(g1, ".").countTokens() != 3 && g1 != null) {
			g1 = JOptionPane.showInputDialog(g + "\n im Format dd.mm.yyyy");
		}
		if (g1 == null) {
			return -1;
		}

		StringTokenizer datum1 = new StringTokenizer(g1, ".");

		d1 = Integer.parseInt(datum1.nextToken());
		m1 = Integer.parseInt(datum1.nextToken());
		y1 = Integer.parseInt(datum1.nextToken());

		GregorianCalendar c1 = new GregorianCalendar();

		c1.set(y1, m1 - 1, d1);
		return (c1.getTimeInMillis() / 1000);

	}

}// class
