package Logistik;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Hier werden alle Materialien im Mainpanel angezeigt Anzeige durch
 * AnzeigeTable. Auswahl der Daten durch String qry
 */

public class AnzMaterialien extends LayoutMainPanel implements ActionListener {
	/**
	 * Zum Anzeigen der Daten in Tabelle
	 */
	private AnzTabelleA anzeige;
	private JButton aktu = new JButton("Aktualisieren");

	public AnzMaterialien(UserImport user) {
		super(user);
		aktu.addActionListener(this);
		activate();
	}

	@Override
	public void activate() {
		try{

		String qry = "SELECT * FROM material,firma_material,firma,inventurgruppe,bundesnr WHERE material.bundesnr=bundesnr.id AND material.inventurgruppe=inventurgruppe.id AND material.id=firma_material.material AND firma.id=firma_material.firma ORDER BY material.bezeichnung";
		ResultSet rs = con.mysql_query(qry);
		String[] spaltenDB = { "material.id","material.bezeichnung",
				"bundesnr.bezeichnung", "inventurgruppe.bezeichnung", "stueck",
				"preisExkl", "mwst", "einheit", "artNr", "firmenname", "plz",
				"ort", "sachbearbeiter", "fixkosten"};
		String[] spaltenT = { "Material-ID","Bezeichnung", "BundesNr",
				"Inventurgruppe", "Stueck", "PreisExkl", "MWSt", "Einheit",
				"ArtNr", "Firma", "PLZ", "Ort", "Sachbearbeiter", "Fixkosten"};
		Class[] classes = { Integer.class,String.class, String.class,
				String.class, Integer.class, String.class, Integer.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class};
		anzeige = new AnzTabelleA(spaltenDB, spaltenT, classes, rs, 1);
		removeMall();
		JScrollPane p = new JScrollPane(anzeige);

		setLayoutM(new BorderLayout());
		addM(p, BorderLayout.CENTER);
		addM(aktu, BorderLayout.SOUTH);
		validate();
		repaint();
		
		}catch(Exception e)
		{
			e.getStackTrace();
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		activate();
	}
}