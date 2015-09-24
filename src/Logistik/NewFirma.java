package Logistik;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Hier kann man Firmen erstellen Daten werden eingegeben
 */

public class NewFirma extends LayoutMainPanel implements ActionListener {
	/**
	 * Textfelder für die Eingabe
	 */
	private Input name, plz, ort, str, mail, staat, knr, sachbearbeiter,
			telefon, fax, homepage, uid, kondition, date, ums, ara,
			kreditorennr, einkaeufergr;
	/**
	 * Button zum Speichern/Löschen
	 */
	private JButton save, clear;
	private LayoutForm f;
	private String edit = null;

	public NewFirma(UserImport user, String edit) {
		super(user);
		f = new LayoutForm();
		this.edit = edit;

		name = new Input(20, "firmenname");
		name.setKey(true);
		plz = new Input(5, "plz");
		ort = new Input(20, "ort");
		str = new Input(20, "strasse");
		mail = new Input(20, "mail");
		mail.setKey(true);
		mail.mustHave(new char[] { '@', '.' });
		staat = new Input(25, "staat");
		knr = new Input(15, "kundennr");
		sachbearbeiter = new Input(25, "sachbearbeiter");
		telefon = new Input(25, "telefon");
		fax = new Input(25, "fax");
		homepage = new Input(25, "homepage");
		uid = new Input(20, "uid");
		kondition = new Input(15, "kondition");
		date = new Input(25, "erstellungsdatum");
		ums = new Input(5, "ums");
		ara = new Input(15, "ara");
		kreditorennr = new Input(15, "kreditorennr");
		einkaeufergr = new Input(15, "einkaeufergr");

		save = new JButton("Speichern");
		clear = new JButton("Löschen");
		f.addHiddenInput(date);
		f.addRight(new JLabel("Firmenname:"));
		f.addLeftInput(name);
		f.br();
		f.addRight(new JLabel("PLZ:"));
		f.addLeftInput(plz);
		f.br();
		f.addRight(new JLabel("Ort:"));
		f.addLeftInput(ort);
		f.br();
		f.addRight(new JLabel("Straße/Hausnummer:"));
		f.addLeftInput(str);
		f.br();
		f.addRight(new JLabel("Mailadresse:"));
		f.addLeftInput(mail);
		f.br();
		f.addRight(new JLabel("Staat:"));
		f.addLeftInput(staat);
		f.br();
		f.addRight(new JLabel("Kundennummer:"));
		f.addLeftInput(knr);
		f.br();
		f.addRight(new JLabel("Sachbearbeiter:"));
		f.addLeftInput(sachbearbeiter);
		f.br();
		f.addRight(new JLabel("Telefon:"));
		f.addLeftInput(telefon);
		f.br();
		f.addRight(new JLabel("Fax:"));
		f.addLeftInput(fax);
		f.br();
		f.addRight(new JLabel("Homepage:"));
		f.addLeftInput(homepage);
		f.add(new JLabel("Ist keine Homepage vorhanden, Feld leer lassen."));
		f.br();
		f.addRight(new JLabel("UmsatzsteuerID:"));
		f.addLeftInput(uid);
		f.br();
		f.addRight(new JLabel("Kondition:"));
		f.addLeftInput(kondition);
		f.br();
		f.addRight(new JLabel("UMS-Nr.:"));
		f.addLeftInput(ums);
		f.br();
		f.addRight(new JLabel("ARA-Nr.:"));
		f.addLeftInput(ara);
		f.br();
		/*
		 * f.addRight(new JLabel("Kreditorennummer:"));
		 * f.addLeftInput(kreditorennr); f.br(); f.addRight(new
		 * JLabel("Einkäufergruppe:")); f.addLeftInput(einkaeufergr); f.br();
		 */
		f.addRight(save);
		f.addLeft(clear);
		if (edit != null) {
			setFirma(edit);
		}
		addM(f);

		clear.addActionListener(this);
		save.addActionListener(this);
	}

	public void setFirma(String firma) {
		edit = firma;
		String qry = "SELECT * FROM firma WHERE id=" + firma + ";";
		try {
			ResultSet rs = con.mysql_query(qry);
			if (rs.next()) {
				name.setText(rs.getString("firmenname"));
				name.canHave(rs.getString("firmenname"));
				plz.setText(rs.getString("plz"));
				ort.setText(rs.getString("ort"));
				str.setText(rs.getString("strasse"));
				mail.setText(rs.getString("mail"));
				mail.canHave(rs.getString("mail"));
				staat.setText(rs.getString("staat"));
				knr.setText(rs.getString("kundennr"));
				sachbearbeiter.setText(rs.getString("sachbearbeiter"));
				telefon.setText(rs.getString("telefon"));
				fax.setText(rs.getString("fax"));
				homepage.setText(rs.getString("homepage"));
				uid.setText(rs.getString("uid"));
				kondition.setText(rs.getString("kondition"));
				date.setText(rs.getString("erstellungsdatum"));
				ums.setText(rs.getString("umsNr"));
				ara.setText(rs.getString("araNr"));
				// kreditorennr.setText(rs.getString("kreditorennummer"));
				// einkaeufergr.setText(rs.getString("einkaeufergruppe"));
				kreditorennr.setText("-");
				einkaeufergr.setText("-");

			}

			rs.close();

		} catch (Exception e) {
		}
	}

	public NewFirma(UserImport user) {
		this(user, null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clear) {
			clear();
		}
		if (e.getSource() == save) {
			save();
		}
	}

	public void clear() {
		f.clear();
	}

	public void save() {
		if (edit == null) {
			date.setText("" + System.currentTimeMillis() / 1000);
			if (!f.check(con, "firma")) {
				return;
			}
			String qry = "INSERT INTO firma (firmenname, plz, ort, strasse, mail, staat, kundennr, sachbearbeiter, telefon, fax, homepage, uid, kondition, erstellungsdatum, umsNr, araNr,kreditorennummer, einkaeufergruppe) ";
			qry += "VALUES (" + name.getValue() + "," + plz.getValue() + ","
					+ ort.getValue() + "," + str.getValue() + ","
					+ mail.getValue() + "," + staat.getValue() + ","
					+ knr.getValue() + "," + sachbearbeiter.getValue() + ","
					+ telefon.getValue() + "," + fax.getValue() + ","
					+ homepage.getValue() + "," + uid.getValue() + ","
					+ kondition.getValue() + "," + date.getValue() + ","
					+ ums.getValue() + "," + ara.getValue() + ","
					+ kreditorennr.getValue() + "," + einkaeufergr.getValue()
					+ ");";
			if (con.mysql_update(qry) == -1) {
				new MessageError("Fehler beim Eintragen der neuen Firma!");
			}
			f.clear();
			new MessageSucess("Firma neu angelegt!");
			return;
		}
		if (!f.check(con, "firma")) {
			return;
		}
		String qry = "UPDATE firma SET firmenname=" + name.getValue()
				+ " ,plz=" + plz.getValue() + " ,ort=" + ort.getValue()
				+ ", strasse=" + str.getValue() + ", mail=" + mail.getValue()
				+ ", staat=" + staat.getValue() + ", kundennr="
				+ knr.getValue() + ", sachbearbeiter="
				+ sachbearbeiter.getValue() + ", telefon=" + telefon.getValue()
				+ ", fax=" + fax.getValue() + ", homepage="
				+ homepage.getValue() + ", uid=" + uid.getValue()
				+ ", kondition=" + kondition.getValue() + ", umsNr="
				+ ums.getValue() + ", araNr=" + ara.getValue()
				+ ", kreditorennummer=" + kreditorennr.getValue()
				+ ", einkaeufergruppe=" + einkaeufergr.getValue()
				+ " WHERE id=" + edit + ";";
		if (con.mysql_update(qry) == -1) {
			new MessageError("Fehler beim Ändern der Firma!");
			return;
		}
		f.clear();
		new MessageSucess("Firmendaten geändert!");
	}
}
