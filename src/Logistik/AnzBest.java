package Logistik;

import java.awt.*;
import javax.swing.*;

import java.sql.*;
import java.util.Date;

//import javax.mail.*;
//import javax.mail.internet.*;

/**
 * Bestellung zusammenfügen. Eingegeben werden muss nur mehr das Budget und der
 * Kommentar Alle anderen Daten werden aus der Datenbank ausgelesen und
 * angezeigt. Die W-Nummer wird berechnet
 */
public class AnzBest extends JPanel {

	/**
	 * Anzeige von Datem
	 */
	public AnzTabelleA ab;

	/**
	 * Datenbankverbindung Budget
	 */
	private DBConnection userB;

	/**
	 * Daten der Bestellungen
	 */
	public String[][] bestDaten;

	/**
	 * Gesamtpreis der Bestellung
	 */
	public double preisKomplett = 0;

	private JPanel o2 = new JPanel();
	public JPanel unten = new JPanel();
	public Object[] best;
	public String[] kopfdaten = new String[10];
	public JComboBox budget;
	public JTextField kommen;
	public Date datum = new Date();

	public AnzBest(UserImport user, DBConnection con, Object[] hak) {

		userB = user.getConnectionKst();
		Object[] best = new Object[hak.length];
		for (int x = 0; x < hak.length; x++) {
			best[x] = hak[x];
		}

		// BESTELLVORGANG

		String sql = "SELECT firma FROM banf,banfpos WHERE banfposnr="
				+ best[0] + " AND banf=id";
		ResultSet rs2 = con.mysql_query(sql);
		try {
			while (rs2.next()) {
				kopfdaten[1] = user.getCn();
				kopfdaten[2] = rs2.getString("firma");
			}
			rs2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		java.util.Date heute = new java.util.Date();
		long d = heute.getTime() / 1000;

		kopfdaten[0] = "" + d;

		String sqlf = "SELECT firmenname FROM firma WHERE id=" + kopfdaten[2];
		ResultSet rs2f = con.mysql_query(sqlf);
		String firmenname = "";
		try {
			while (rs2f.next()) {
				firmenname = rs2f.getString("firmenname");
			}
		} catch (Exception e) {
			e.getMessage();
		}

		kopfdaten[3] = "0";
		kopfdaten[4] = "" + 1;
		kopfdaten[5] = "" + 0;
		kopfdaten[8] = "";

		JPanel o1 = new JPanel();
		o1.setLayout(new GridLayout(1, 1));
		o2.setLayout(new LayoutBanf(5));

		JLabel lehrerT = new JLabel("Antragsteller");
		lehrerT.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel lehrer = new JLabel(kopfdaten[1]);
		lehrer.setFont(new Font("Arial", Font.PLAIN, 13));
		JLabel firmaT = new JLabel("Firma");
		firmaT.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel firma = new JLabel(firmenname);
		firma.setFont(new Font("Arial", Font.PLAIN, 13));
		JLabel datumT = new JLabel("Datum");
		datumT.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel lDatum = new JLabel(heute.toString());
		lDatum.setFont(new Font("Arial", Font.PLAIN, 13));
		JLabel budgetT = new JLabel("Budget");
		budgetT.setFont(new Font("Arial", Font.BOLD, 14));
		budget = new JComboBox();
		budget.addItem("UT3");
		budget.addItem("UT8");
		budget.addItem("LMB1");
		datum = new Date();
		budget.addItem("LMB2");
		String sqlBud = "SELECT * FROM sonderbudget";
		ResultSet rsBud = userB.mysql_query(sqlBud);
		try {
			while (rsBud.next()) {
				budget.addItem(rsBud.getString("name"));
			}
		} catch (Exception eb) {
			eb.getMessage();
		}
		budget.setFont(new Font("Arial", Font.PLAIN, 13));
		JLabel wnummerT = new JLabel("W-Nummer");
		wnummerT.setFont(new Font("Arial", Font.BOLD, 14));
		JLabel wnummer = new JLabel(kopfdaten[7]);
		wnummer.setFont(new Font("Arial", Font.PLAIN, 13));
		JLabel kommenT = new JLabel("Kommentar");
		kommenT.setFont(new Font("Arial", Font.BOLD, 14));
		kommenT
				.setToolTipText("Reihenfolge: Angebotsnummer;Sachbearbeiter;Lieferbedingungen;Lieferfrist;Verpackungsart");
		kommen = new JTextField(kopfdaten[8]);
		kommen.setFont(new Font("Arial", Font.PLAIN, 13));
		kommen
				.setToolTipText("Reihenfolge: Angebotsnummer;Sachbearbeiter;Lieferbedingungen;Lieferfrist;Verpackungsart");


		o2.add(lehrerT, "100");
		o2.add(firmaT, "20%");
		o2.add(datumT, "190");
		o2.add(budgetT, "120");
		// o2.add(wnummerT, "80");
		o2.add(kommenT, "*");
		o2.add(lehrer, "");
		o2.add(firma, "");
		o2.add(lDatum, "");
		o2.add(budget, "");
		// o2.add(wnummer, "");
		o2.add(kommen, "");
		o1.add(o2);

		// UNTEN
		bestDaten = new String[best.length][13];

		String[] spaltenT = { "Banfpos-nr", "Bezeichnung", "Menge",
				"preisExkl", "MWSt" };
		String[] spaltenDB = { "banfposnr", "material.bezeichnung", "Menge",
				"preisExkl", "MWSt" };
		String id = "";

		String sqlbestposID = "SELECT id FROM bestpos ORDER BY id DESC LIMIT 1";

		for (int i = 0; i < best.length - 1; i++) {
			id += "banfposnr=" + best[i] + " OR ";
		}
		id += "banfposnr=" + best[best.length - 1];
		JScrollPane sc = null;
		try {
			String sql5 = "SELECT * FROM banfpos,material WHERE (" + id
					+ ") AND banfpos.bezeichnung=material.id";
			ResultSet rs1 = con.mysql_query(sql5);

			String matBezeichnung;
			int zeile = 0;
			JPanel untenBest = new JPanel();
			untenBest.setLayout(new LayoutBanf(6));

			JLabel bezT = new JLabel("Bezeichnung");
			bezT.setFont(new Font("Arial", Font.BOLD, 13));
			untenBest.add(bezT, "35%");
			JLabel mengeT = new JLabel("Menge");
			mengeT.setFont(new Font("Arial", Font.BOLD, 13));
			untenBest.add(mengeT, "10%");
			JLabel preisT = new JLabel("Preis/Stück");
			preisT.setFont(new Font("Arial", Font.BOLD, 13));
			untenBest.add(preisT, "11%");
			JLabel preisGesT = new JLabel("Gesamtpreis");
			preisGesT.setFont(new Font("Arial", Font.BOLD, 13));
			untenBest.add(preisGesT, "13%");
			JLabel einheitT = new JLabel("Einheit");
			einheitT.setFont(new Font("Arial", Font.BOLD, 13));
			untenBest.add(einheitT, "10%");
			JLabel userT = new JLabel("Antragsteller");
			userT.setFont(new Font("Arial", Font.BOLD, 13));
			untenBest.add(userT, "16%");

			while (rs1.next()) {
				matBezeichnung = rs1.getString("material.bezeichnung");
				bestDaten[zeile][0] = rs1.getString("banfpos.bezeichnung");
				bestDaten[zeile][1] = rs1.getString("menge");
				bestDaten[zeile][2] = rs1.getString("preisExkl");
				bestDaten[zeile][3] = rs1.getString("mwst");
				double preisInkl = Double.parseDouble(bestDaten[zeile][3])
						/ 100 * Double.parseDouble(bestDaten[zeile][2])
						+ Double.parseDouble(bestDaten[zeile][2]);
				bestDaten[zeile][4] = "" + preisInkl;
				double preisGes = preisInkl
						* Double.parseDouble(bestDaten[zeile][1]) * 100;

				long hilf1 = Math.round(preisGes);

				preisGes = (hilf1) / 100.0;

				preisKomplett += preisGes;
				bestDaten[zeile][5] = "" + preisGes;
				bestDaten[zeile][6] = rs1.getString("einheit");
				bestDaten[zeile][7] = user.getCn();
				bestDaten[zeile][8] = "0";
				bestDaten[zeile][9] = "0";
				bestDaten[zeile][10] = "" + 1;
				bestDaten[zeile][11] = "" + 0;
				bestDaten[zeile][12] = best[zeile].toString();

				untenBest.add(new JLabel(matBezeichnung), "");
				untenBest.add(new JLabel(bestDaten[zeile][1]), "");
				untenBest.add(new JLabel("" + preisInkl), "");
				untenBest.add(new JLabel("" + preisGes), "");
				untenBest.add(new JLabel(bestDaten[zeile][6]), "");
				untenBest.add(new JLabel(bestDaten[zeile][7]), "");

				zeile++;

			}

			unten.setLayout(new GridLayout(1, 1));
			unten.add(untenBest);

		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(
							null,
							"Fehler! Es ist etwas schief gelaufen!"
									+ "\nBitte kontrollieren Sie die Bestellung und wenden Sie sich an den Systemadministrator.");
		}

		long hilf2 = Math.round(preisKomplett * 100);
		preisKomplett = hilf2 / 100.0;

		setLayout(new BorderLayout());
		JLabel ges = new JLabel("Preis insgesamt: " + preisKomplett);
		ges.setFont(new Font("Arial", Font.BOLD, 15));

		add(ges, BorderLayout.SOUTH);
		add(o1, BorderLayout.NORTH);
		add(unten, BorderLayout.CENTER);
		repaint();

	}// constructor

	public void repaint2(Graphics g) {
		if (o2 != null) {
			o2.setPreferredSize(new Dimension(getWidth() - 90, o2
					.getPreferredSize().height));
		}
	}

}