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

public class MehrfachaenderungMaterial extends LayoutMainPanel implements ActionListener {
	/**
	 * Zum Anzeigen der Daten in Tabelle
	 */
	private AnzTabelleA anzeige;
	/** Material ID **/
	private int material;

	JButton speichern = new JButton("Speichern");

	public MehrfachaenderungMaterial(UserImport user) {
		super(user);

		activate();
		speichern.addActionListener(this);

	}

	@Override
	public void activate() {
		String qry = "SELECT * FROM material,firma_material,firma,bundesnr WHERE material.bundesnr=bundesnr.id AND material.id=firma_material.material AND firma.id=firma_material.firma ORDER BY material.bezeichnung";
		ResultSet rs = con.mysql_query(qry);
		String[] spaltenDB = { "firma_material.id", "material.bezeichnung",
				"bundesnr.bezeichnung", "stueck", "preisExkl", "einheit",
				"artNr", "firmenname", "plz", "ort", "sachbearbeiter", "fixkosten" };
		String[] spaltenT = { "Fir-Mat ID", "Bezeichnung", "BundesNr", "Stück",
				"Preis exkl", "Einheit", "ArtikelNr", "Firma", "PLZ", "Ort",
				"Sachbearbeiter", "Fixkosten", "Material bearbeiten" };
		Class[] classes = { Integer.class, JTextField.class, String.class,
				JTextField.class, JTextField.class, JComboBox.class,
				JTextField.class, String.class, String.class, String.class,
				String.class, JTextField.class, Boolean.class};
		anzeige = new AnzTabelleA(spaltenDB, spaltenT, classes, rs, 1);

		removeMall();
		JScrollPane p = new JScrollPane(anzeige);
		setLayoutM(new BorderLayout());
		addM(p, BorderLayout.CENTER);
		addM(speichern, BorderLayout.SOUTH);
		repaint();
	}

	public void actionPerformed(ActionEvent arg0) {
		Object[] gewaehlt = anzeige.getKlicked("Fir-Mat ID", "Material bearbeiten");
		Object[] preis = anzeige.getKlicked("Preis exkl", "Material bearbeiten");
		Object[] bez = anzeige.getKlicked("Bezeichnung", "Material bearbeiten");
		Object[] artNr = anzeige.getKlicked("ArtikelNr", "Material bearbeiten");
		Object[] fixkosten = anzeige.getKlicked("Fixkosten", "Material bearbeiten");
		Object[] stueck = anzeige.getKlicked("Stück", "Material bearbeiten");

		if (gewaehlt.length == 0) {
			JOptionPane.showMessageDialog(this, "Es wurde nichts ausgewählt!");
		} else {
			int[] id = new int[gewaehlt.length];
			double[] preisA = new double[preis.length];
			String[] bezS = new String[bez.length];
			String[] artNrS = new String[artNr.length];
			String[] fixkostenS = new String[fixkosten.length];
			String[] stueckS = new String[stueck.length];
		
			for (int i = 0; i < gewaehlt.length; i++) {
				id[i] = Integer.parseInt(gewaehlt[i].toString());
				preisA[i] = Double.parseDouble((preis[i].toString()));
				bezS[i] = bez[i].toString();
				artNrS[i] = artNr[i].toString();
				fixkostenS[i] = fixkosten[i].toString();
				stueckS[i] = stueck[i].toString();
			}

			for (int i = 0; i < id.length; i++) {
				String query = "update firma_material set preisExkl="
						+ preisA[i] + ", artNr='" + artNrS[i] + "' where id="
						+ id[i];
				
				con.mysql_update(query);

				String query3 = "select material from firma_material where id="
						+ id[i];
				ResultSet rs3 = con.mysql_query(query3);
				

				try {
					while (rs3.next()) {
						material = rs3.getInt("material");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				String query2 = "update material set bezeichnung='" + bezS[i]
						+ "', fixkosten='"+fixkostenS[i]+"',stueck="+stueckS[i]+" where id=" + material;
				
				con.mysql_update(query2);
			}

			JOptionPane.showMessageDialog(this,
					"Änderung(en) gespeichert!");
		}

	}

}