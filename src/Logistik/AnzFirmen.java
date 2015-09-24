package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * Hier werden alle Firmen im Mainpanel angezeigt Anzeige durch AnzeigeTable.
 * Auswahl der Daten durch String qry
 */

public class AnzFirmen extends LayoutMainPanel {
	/**
	 * Zum Anzeigen der Daten in Tabelle
	 */
	private AnzTabelleA anzeige;

	public AnzFirmen(UserImport user) {
		super(user);
		activate();
	}

	@Override
	public void activate() {
		String qry = "SELECT * FROM firma ORDER BY firmenname";
		ResultSet rs = con.mysql_query(qry);
		String[] spalten = { "id","firmenname", "erstellungsdatum", "plz",
				"ort", "strasse", "mail", "staat", "kundennr",
				"sachbearbeiter", "telefon", "fax", "homepage", "uid",
				"kondition" , "umsNr", "araNr","kreditorennummer","einkaeufergruppe"};
		String[] spalten2 = { "Firmen-ID","Firmenname", "Erstellungsdatum", "PLZ",
				"Ort", "Straﬂe", "Mail", "Staat", "Kundennr.",
				"Sachbearbeiter", "Telefon", "Fax", "Homepage", "UID",
				"Kondition" , "UMS-Nr.", "ARA-Nr.","Kreditorennr.","Eink‰ufergr."};
		Class[] classes = { Integer.class, String.class, Date.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, String.class, String.class, String.class, String.class,Integer.class };
		anzeige = new AnzTabelleA(spalten, spalten2, classes, rs, 1);
		removeMall();
		JScrollPane p = new JScrollPane(anzeige);
		setLayoutM(new GridLayout(1, 1));
		addM(p);

		repaint();
	}
}