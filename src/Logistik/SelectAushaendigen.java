package Logistik;

import java.awt.*;

import javax.swing.*;

import java.sql.*;

import java.awt.event.*;

import Budget.DatenImport;

/**
 * 
 * Fenster, welches beim Klicken des Auswahlbuttons bei SelectInputs in der Banf
 * 
 * angezeigt wird Beinhaltet die Daten einer Tabelle und einen Auswahlbutton
 * 
 * Beim Klicken des Auswahlbuttons werden die Daten der gewählten Reihe ins
 * 
 * SelectInput übernommen
 */

public class SelectAushaendigen extends JDialog implements ActionListener {

	private JTextField[] fields;

	private JScrollPane sp;

	private DBConnection con;

	private JButton btn = new JButton("Material wählen");

	private AnzTabelleA anzeige;

	private boolean aush1 = true;

	private UserImport user;
	private int unterscheidungKorrekturAnUserVonUser;

	public SelectAushaendigen(DBConnection con, JTextField[] fields,

	SelectInput ip, int unterscheidungKorrekturAnUserVonUser, UserImport u) {

		// super("Select");

		setSize(1290, 600);

		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.fields = fields;
		this.con = con;
		this.unterscheidungKorrekturAnUserVonUser = unterscheidungKorrekturAnUserVonUser;
		user = u;

		btn.addActionListener(this);

		setLayout(new BorderLayout());

		addSelects();

		setModal(true);

		setVisible(true);

	}

	public void addSelects() {

		try {

			String qry = "SELECT artnr,einheit,preisExkl*((mwst/100)+1) as \"preis\",material,stueck,material.bezeichnung,material.id,"
					+

					"inventurgruppe.bezeichnung,firmenname,firma_material.id"
					+

					" FROM material,firma_material,firma,inventurgruppe,bundesnr "
					+

					"WHERE material.bundesnr=bundesnr.id AND material.inventurgruppe=inventurgruppe.id "
					+

					"AND material.id=firma_material.material AND firma.id=firma_material.firma "
					+

					"ORDER BY material.bezeichnung";

			ResultSet rs = con.mysql_query(qry);

			String[] spaltenDB = { "firma_material.id", "material.id",
					"material.bezeichnung",

					"inventurgruppe.bezeichnung", "material.stueck",

					"firma_material.einheit", "preis", "firma_material.artNr",

					"firma.firmenname" };

			String[] spaltenT = { "Material-Firmen ID", "Material ID",
					"Bezeichnung",

					"Inventurgruppe", "Stück", "Einheit", "Preis",

					"ArtikelNr", "Firma", "Auswahl" };

			Class[] classes = { Integer.class, Integer.class, String.class,

			String.class, Integer.class, String.class,

			Double.class, String.class, String.class, Boolean.class };

			anzeige = new AnzTabelleA(spaltenDB, spaltenT, classes, rs, 1);

			sp = new JScrollPane(anzeige);

			this.add(sp, BorderLayout.CENTER);

			this.add(btn, BorderLayout.SOUTH);

			rs.close();

		} catch (Exception e) {

			e.printStackTrace();

			e.getMessage();

		}

	}

	public void actionPerformed(ActionEvent arg0) {

		Object[] gewaehlt = anzeige.getKlicked("Material ID", "Auswahl");

		Object[] materialFirma = anzeige.getKlicked("Material-Firmen ID",
				"Auswahl");

		Object[] preisExkl = anzeige.getKlicked("Preis", "Auswahl");

		// Object[] is = anzeige.getKlicked("Material ID", "Auswahl");

		Object[] bezeichnung = anzeige.getKlicked("Bezeichnung", "Auswahl");

		Object[] lagerstk = anzeige.getKlicked("Stück", "Auswahl");

		Object[] einheit = anzeige.getKlicked("Einheit", "Auswahl");

		Object[] firma = anzeige.getKlicked("Firma", "Auswahl");

		// NumberFormat formatter = new DecimalFormat("#0.00");

		DatenImport di = new DatenImport();

		if (gewaehlt.length == 0)

		{

			JOptionPane.showMessageDialog(this, "Es wurde nichts ausgewählt!");

		}

		else if (gewaehlt.length > 1)

		{

			JOptionPane.showMessageDialog(this,
					"Es kann nur ein Material ausgewählt werden!");

		}

		else

		{
			String e = einheit[0].toString();
			String b = bezeichnung[0].toString();
			String g = gewaehlt[0].toString();
			String f = materialFirma[0].toString();
			double p = di.runde(Double.parseDouble(preisExkl[0].toString()));
			String firm = firma[0].toString();

			if (unterscheidungKorrekturAnUserVonUser == -1) {
				dispose();
				new LagerumbuchungenC(Integer.parseInt(gewaehlt[0].toString()),
						LagerumbuchungenB.ABBUCHEN, "Material buchen", con, user,
						1);
			} else if (unterscheidungKorrekturAnUserVonUser == -3) {
				dispose();
				new LagerumbuchungenC(Integer.parseInt(gewaehlt[0].toString()),
						LagerumbuchungenB.ZUBUCHEN, "Material buchen", con, user,
						0);
			} else if (unterscheidungKorrekturAnUserVonUser == -5) {
				dispose();
				new LagerumbuchungenC(Integer.parseInt(gewaehlt[0].toString()),
						LagerumbuchungenB.AN_FIRMA, "Material buchen", con, user,
						2);
			} else {
				fields[0].setText(f);
				fields[1].setText(g);
				fields[2].setText(b);
				fields[3].setText(""
						+ Double.parseDouble(lagerstk[0].toString()));
				fields[4].setText(e);
				fields[5].setText("" + p);
				fields[6].setText("" + firm);
			}

			System.gc();
			dispose();

		}

	}

}
