package Logistik;

import javax.swing.*;
import java.sql.*;

/**
 * Abweisen von Bestellanforderungspositionen die Abweisung wurde bestätigt und
 * wird durchgeführt dazu werden zustzlich die Stati der BANF+Position geändert
 */
public class DelPos extends JPanel {

	public DelPos(DBConnection con, Object[] hakDelPos) {

		Object[] delete = new Object[hakDelPos.length];
		for (int x = 0; x < hakDelPos.length; x++) {
			delete[x] = hakDelPos[x];
		}

		for (int j = 0; j < hakDelPos.length; j++) {
			try {
				String sql = "UPDATE banfpos SET status=3 WHERE banfposnr = "
						+ hakDelPos[j] + " AND status=1";
				con.mysql_update(sql);
			} catch (Exception e3) {
				e3.getMessage();
			}
		}

		int[] chgBanf = new int[hakDelPos.length];
		int chgBanfLaenge = 0;
		String banfposnr = "";
		for (int k = 0; k < hakDelPos.length - 1; k++) {
			banfposnr += "banfposnr=" + hakDelPos[k] + " OR ";
		}
		banfposnr += "banfposnr=" + hakDelPos[hakDelPos.length - 1];

		try {
			String sql = "Select distinct banf from banfpos where ("
					+ banfposnr + ")";
			ResultSet rs = con.mysql_query(sql);
			while (rs.next()) {
				chgBanf[chgBanfLaenge] = rs.getInt("banf");
				chgBanfLaenge++;
			}

			rs.close();

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
				ResultSet rs = con.mysql_query(sql);
				while (rs.next()) {
					banf[banfLaenge] = rs.getInt("status");
					banfLaenge++;

				}

				rs.close();

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

		JOptionPane.showMessageDialog(null, "Datensaetze gelöscht!");

	}
}