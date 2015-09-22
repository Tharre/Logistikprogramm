package Logistik;

import java.sql.*;
import java.util.Date;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Hier werden alle Materialien im Mainpanel angezeigt Anzeige durch
 * AnzeigeTable. Auswahl der Daten durch String qry
 */

public class MehrfachaenderungFirma extends LayoutMainPanel implements ActionListener {
	/**
	 * Zum Anzeigen der Daten in Tabelle
	 */
	private AnzTabelleA anzeige;
	/** Material ID **/
	private int firmaID;

	JButton speichern = new JButton("Speichern");

	public MehrfachaenderungFirma(UserImport user) {
		super(user);

		activate();
		speichern.addActionListener(this);

	}

	@Override
	public void activate() {
		String qry = "SELECT * FROM firma ORDER BY firmenname";
		ResultSet rs = con.mysql_query(qry);
		String[] spalten = { "id", "firmenname", "plz",
				"ort", "strasse", "staat", "kundennr",
				"sachbearbeiter", "uid",
				"kondition" , "umsNr", "araNr","kreditorennummer","einkaeufergruppe"};
		String[] spalten2 = { "ID", "Firmenname", "PLZ",
				"Ort", "Straﬂe", "Staat", "Kundennr.",
				"Sachbearbeiter", "UID",
				"Kondition" , "UMS-Nr.", "ARA-Nr.", "Kreditorennr.","Eink‰ufergr.","Firma bearbeiten"};
		Class[] classes = { Integer.class, JTextField.class, 
				JTextField.class, JTextField.class, JTextField.class, JTextField.class,
				JTextField.class, JTextField.class, JTextField.class, JTextField.class,
				JTextField.class, JTextField.class, JTextField.class, JTextField.class, Boolean.class };
		anzeige = new AnzTabelleA(spalten, spalten2, classes, rs, 1);
		removeMall();
		JScrollPane p = new JScrollPane(anzeige);
		setLayoutM(new BorderLayout());
		addM(p, BorderLayout.CENTER);
		addM(speichern, BorderLayout.SOUTH);
		repaint();
	}

	public void actionPerformed(ActionEvent arg0) {
		Object[] gewaehlt = anzeige.getKlicked("ID", "Firma bearbeiten");
		Object[] firmenname = anzeige.getKlicked("Firmenname", "Firma bearbeiten");
		Object[] plz = anzeige.getKlicked("PLZ", "Firma bearbeiten");
		Object[] ort = anzeige.getKlicked("Ort", "Firma bearbeiten");
		Object[] strasse = anzeige.getKlicked("Straﬂe", "Firma bearbeiten");
		Object[] staat = anzeige.getKlicked("Staat", "Firma bearbeiten");
		Object[] kundennr = anzeige.getKlicked("Kundennr.", "Firma bearbeiten");
		Object[] sachbearbeiter = anzeige.getKlicked("Sachbearbeiter", "Firma bearbeiten");
		Object[] uid = anzeige.getKlicked("UID", "Firma bearbeiten");
		Object[] kondition = anzeige.getKlicked("Kondition", "Firma bearbeiten");
		Object[] umsnr = anzeige.getKlicked("UMS-Nr.", "Firma bearbeiten");
		Object[] aranr = anzeige.getKlicked("ARA-Nr.", "Firma bearbeiten");
		Object[] kreditorennr = anzeige.getKlicked("Kreditorennr.", "Firma bearbeiten");
		Object[] einkaeufergr = anzeige.getKlicked("Eink‰ufergr.", "Firma bearbeiten");
		
		if (gewaehlt.length == 0) {
			JOptionPane.showMessageDialog(this, "Es wurde nichts ausgew‰hlt!");
		} else {
			int[] id = new int[gewaehlt.length];
			String[] firmennameS = new String[firmenname.length];
			String[] plzS = new String[plz.length];
			String[] ortS = new String[ort.length];
			String[] strasseS = new String[strasse.length];
			String[] staatS = new String[staat.length];
			String[] kundennrS = new String[kundennr.length];
			String[] sachbearbeiterS = new String[sachbearbeiter.length];
			String[] uidS = new String[uid.length];
			String[] konditionS = new String[kondition.length];
			String[] umsnrS = new String[umsnr.length];
			int[] aranrI = new int[aranr.length];
			String[] kreditorennrS = new String[kreditorennr.length];
			String[] einkaeufergrS = new String[einkaeufergr.length];
			
			for (int i = 0; i < gewaehlt.length; i++) {
				id[i] = Integer.parseInt(gewaehlt[i].toString());
				firmennameS[i] = firmenname[i].toString();
				plzS[i] = plz[i].toString();
				ortS[i] = ort[i].toString();
				strasseS[i] = strasse[i].toString();
				staatS[i] = staat[i].toString();
				kundennrS[i] = kundennr[i].toString();
				sachbearbeiterS[i] = sachbearbeiter[i].toString();
				uidS[i] = uid[i].toString();
				konditionS[i] = kondition[i].toString();
				umsnrS[i] = umsnr[i].toString();
				aranrI[i] = Integer.parseInt(aranr[i].toString());
				kreditorennrS[i] = kreditorennr[i].toString();
				einkaeufergrS[i] = einkaeufergr[i].toString();
			}

			for (int i = 0; i < id.length; i++) {
				String query = "update firma set firmenname='"+firmennameS[i]+"', plz='"+plzS[i]+"', ort='"+ortS[i]+"', strasse='"+strasseS[i]+"', staat='"+staatS[i]+"', kundennr='"+kundennrS[i]+"', sachbearbeiter='"+sachbearbeiterS[i]+"', uid='"+uidS[i]+"', kondition='"+konditionS[i]+"', umsNr='" + umsnrS[i] + "', araNr="+aranrI[i]+", kreditorennummer='"+ kreditorennrS[i]+ "',einkaeufergruppe='"+einkaeufergrS[i]+"' where id="
						+ id[i];
				con.mysql_update(query);
			}

			JOptionPane.showMessageDialog(this,
					"ƒnderung(en) gespeichert!");
		}

	}

}