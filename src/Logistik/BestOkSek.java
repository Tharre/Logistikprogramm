package Logistik;

import javax.swing.*;
import java.sql.*;

/**
 * wenn Sekretärin eine Bestellung erstellt Zuerst Statusänderung in BANF,
 * danach Speichern in der Datenbank und Mail an Mayer
 */
public class BestOkSek extends JPanel {
	/**
	 * Erfolgsmeldung
	 */
	public JLabel titel = new JLabel(
			"Bestellung wurde erstellt - noch nicht abgeschickt!");

	/**
	 * Datenbankverbindung
	 */
	public DBConnection con;

	/**
	 * Array mit Werten von Kopf-Daten
	 */
	public String[] kopf;

	/**
	 * Array mit Werten von Positions-Daten
	 */
	public String[][] pos;

	/**
	 * Array mit Bestellungen
	 */
	public Object[] best;

	public BestOkSek(UserImport user, String[] kopf, String[][] pos,
			Object[] hakBestPos) {
		add(titel);
		con = user.getConnection();
		this.kopf = kopf;
		this.pos = pos;
		best = hakBestPos;

		// SPEICHERN IN DB

		String updKopf = "INSERT INTO bestellung (datum,antragsteller,firma,status,statusbez,budget,wnummer,kommentar,anschriftRechnung) VALUES ("
				+ kopf[0]
				+ ",\""
				+ kopf[1]
				+ "\","
				+ kopf[2]
				+ ","
				+ kopf[4]
				+ ","
				+ kopf[5]
				+ ",\""
				+ kopf[6]
				+ "\",\""
				+ kopf[7]
				+ "\",\""
				+ kopf[8] + "\",\"" + kopf[9] + "\")";
		int y = con.mysql_update(updKopf);
		if (y == 0) {
			JOptionPane
					.showMessageDialog(
							null,
							"KRITISCHER FEHLER!\n Es wurden gleichzeitig 2 Bestellungen erstellt! Bitte erstellen Sie Ihre Bestellung neu, um eine neue W-Nummer zu bekommen!\n Ignorieren Sie die nachfolgende Erfolgsmeldung!!!");
			return;
		}

		String sqlBestId = "SELECT bestId FROM bestellung ORDER BY bestId DESC LIMIT 1";
		ResultSet rs = con.mysql_query(sqlBestId);
		int id = 0;
		try {
			while (rs.next()) {
				id += rs.getInt("bestId");
			}

			rs.close();

		} catch (Exception e) {
			e.getMessage();
		}

		for (int i = 0; i < pos.length; i++) {
			String updPos = "INSERT INTO bestpos (bezeichnung,menge,preisExkl,mwst,preisInkl,preisGesamt,einheit,lehrer,bestellId,status,statusbez,banfposnr) VALUES (\""
					+ pos[i][0]
					+ "\","
					+ pos[i][1]
					+ ","
					+ pos[i][2]
					+ ","
					+ pos[i][3]
					+ ","
					+ pos[i][4]
					+ ","
					+ pos[i][5]
					+ ",\""
					+ pos[i][6]
					+ "\",\""
					+ pos[i][7]
					+ "\","
					+ id
					+ ","
					+ pos[i][10] + "," + pos[i][11] + "," + pos[i][12] + ")";
			con.mysql_update(updPos);
		}

		String sqlW = "SELECT wnummer FROM bestellung ORDER BY datum DESC LIMIT 1";
		ResultSet rsW = con.mysql_query(sqlW);
		String w = "";
		try {
			while (rsW.next()) {
				w = rsW.getString("wnummer");
			}

			rsW.close();

		} catch (Exception e) {
			e.getMessage();
		}

		// BANFÄNDERUNG

		for (int j = 0; j < best.length; j++) {
			try {
				String sql = "UPDATE banfpos SET status=2 WHERE banfposnr = "
						+ best[j] + " AND status=1";
				con.mysql_update(sql);
			} catch (Exception e3) {
				e3.getMessage();
			}
		}

		int[] chgBanf = new int[best.length];
		int chgBanfLaenge = 0;
		String banfposnr = "";
		for (int k = 0; k < best.length - 1; k++) {
			banfposnr += "banfposnr=" + best[k] + " OR ";
		}
		banfposnr += "banfposnr=" + best[best.length - 1];

		try {
			String sql = "Select distinct banf from banfpos where ("
					+ banfposnr + ")";
			ResultSet rs2 = con.mysql_query(sql);
			while (rs2.next()) {
				chgBanf[chgBanfLaenge] = rs2.getInt("banf");
				chgBanfLaenge++;

			}

			rs2.close();

		} catch (Exception e4) {
			e4.getMessage();
		}

		for (int i = 0; i < chgBanfLaenge; i++) {

			int[] banf = new int[10000];
			int banfLaenge = 0;
			for (int z = 0; z < banf.length; z++) {
				banf[z] = 9;
			}

			try {
				String sql = "Select status from banfpos where banf="
						+ chgBanf[i];
				ResultSet rs3 = con.mysql_query(sql);
				while (rs3.next()) {
					banf[banfLaenge] = rs3.getInt("status");
					banfLaenge++;

				}

				rs3.close();

			} catch (Exception e4) {
				e4.getMessage();
			}

			boolean status3 = true;
			boolean status4 = false;
			boolean status5 = true;
			boolean status6 = true;

			int m;
			for (m = 0; m < banfLaenge; m++) {
				if (banf[m] != 1) {
					status3 = false;
				}
				if (banf[m] != 2) {
					status5 = false;
				}
				if (banf[m] != 3) {
					status6 = false;
				}
			}

			int stat = 0;
			if (!status3 && !status5 && !status6) {
				for (m = 0; m < banfLaenge; m++) {
					if (banf[m] == 1) {
						status4 = true;
					}
					;
				}
				if (status4 == false) {
					status5 = true;
				}
			}
			if (status3) {
				stat = 3;
			}
			if (status4) {
				stat = 4;
			}
			if (status5) {
				stat = 5;
			}
			if (status6) {
				stat = 6;
			}

			try {
				String sql = "UPDATE banf SET status=" + stat + " WHERE id = "
						+ chgBanf[i];
				con.mysql_update(sql);
			} catch (Exception e5) {
				e5.getMessage();
			}

		}// for

		// MAIL AN mayer: neue Bestellung (von Sekretärin)

		IMAP.sendMail(user.getMail(),
				new String[] { "leopold.mayer@htl-hl.ac.at" },
				new String[] { "karin.walka@htl-hl.ac.at" }, w,
				"Eine neue Bestellung wurde erstellt\n Die W-Nummer lautet: "
						+ w + " .", null);

	}

}