package Logistik;

import javax.swing.*;

import Budget.DatenImport;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

/**
 * neues Fenster für Detialansicht bei Bestellanforderungen verwendetes Layout:
 * BanfLayout Anzeige aller ausgewählten Bestellanforderungen in Detailansicht
 */
public class AnzBanfDetail extends JFrame implements ActionListener {
	/**
	 * Array mit angehakten Bestellungen
	 */
	public Object[] hak;

	/**
	 * Datenbankverbindung
	 */
	private DBConnection con;

	/**
	 * Array mit BANFs
	 */
	private JPanel[] eineBanf;

	/**
	 * Array mit BANFkopf-Daten
	 */
	private JPanel[] einKopf;
	private double summe;

	/**
	 * Array mit BANFpositions-Daten
	 */
	private JPanel[] einPos;

	private JPanel all = new JPanel();
	private JButton kommentar[];
	private Object[] banfID;

	public AnzBanfDetail(DBConnection con, Object[] hakDet) {
		Container c = getContentPane();

		this.con = con;
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pack();
		this.setSize(1000, 800);

		Object[] hak = new Object[hakDet.length];
		banfID = new Object[hakDet.length];

		for (int x = 0; x < hakDet.length; x++) {
			hak[x] = hakDet[x];
			banfID[x] = hak[x];
		}

		all.setLayout(new GridLayout(hak.length, 1));
		c.setLayout(new GridLayout(1, 1));
		eineBanf = new JPanel[hak.length];
		einKopf = new JPanel[hak.length];
		einPos = new JPanel[hak.length];
		JLabel[] linie = new JLabel[hak.length];

		for (int y = 0; y < hak.length; y++) {

			linie[y] = new JLabel(
					"<html><div align=\"center\"><hr size=\"2\" width=\"3000\"> </div></html>\n");
			linie[y].setFont(new Font("Arial", Font.BOLD, 20));
			eineBanf[y] = new JPanel();
			eineBanf[y].setLayout(new BorderLayout());
			eineBanf[y].add(linie[y], BorderLayout.SOUTH);
			einKopf[y] = new JPanel();
			einKopf[y].setLayout(new LayoutBanf(7));
			einPos[y] = new JPanel();
			einPos[y].setLayout(new LayoutBanf(7));
		}

		kommentar = new JButton[banfID.length];

		try {
			for (int i = 0; i < hak.length; i++) {
				summe = 0;
				String query1 = "SELECT b.id,u.cn,b.kostenstelle,f.firmenname,b.status,b.datum FROM banf b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND b.id="
						+ hak[i] + " ORDER BY datum DESC";
				ResultSet rs1 = con.mysql_query(query1);
				rs1.next();

				JLabel banfidT = new JLabel("Banf ID");
				banfidT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel banfid = new JLabel(rs1.getString("b.id"));
				banfid.setFont(new Font("Arial", Font.PLAIN, 13));
				JLabel lehrerT = new JLabel("Antragsteller");
				lehrerT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel lehrer = new JLabel(rs1.getString("u.cn"));
				lehrer.setFont(new Font("Arial", Font.PLAIN, 13));
				JLabel kstT = new JLabel("Kostenstelle");
				kstT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel kst = new JLabel(rs1.getString("b.kostenstelle"));
				kst.setFont(new Font("Arial", Font.PLAIN, 13));
				JLabel firmaT = new JLabel("Firma");
				firmaT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel firma = new JLabel(rs1.getString("f.firmenname"));
				firma.setFont(new Font("Arial", Font.PLAIN, 13));
				JLabel statusT = new JLabel("Status");
				statusT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel status = new JLabel(rs1.getString("b.status"));
				status.setFont(new Font("Arial", Font.PLAIN, 13));
				status.setOpaque(true);
				JLabel datumT = new JLabel("Datum");
				datumT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel datum = new JLabel(new java.util.Date(Long.parseLong(rs1
						.getString("b.datum")) * 1000).toString());
				datum.setFont(new Font("Arial", Font.PLAIN, 13));

				kommentar[i] = new JButton("Kommentar");
				kommentar[i].addActionListener(this);

				String s = rs1.getString("b.status");

				einKopf[i].add(banfidT, "60");
				einKopf[i].add(lehrerT, "20%");
				einKopf[i].add(kstT, "100");
				einKopf[i].add(firmaT, "23%");
				einKopf[i].add(statusT, "100");
				einKopf[i].add(datumT, "195");
				einKopf[i].add(kommentar[i], "80");
				einKopf[i].add(banfid, "");
				einKopf[i].add(lehrer, "");
				einKopf[i].add(kst, "");
				einKopf[i].add(firma, "");
				einKopf[i].add(status, "");
				einKopf[i].add(datum, "");
				einKopf[i].add(new JLabel(), "");

				if (s.equals("6") || s.equals("abgewiesen")) {
					status.setText("abgewiesen");
					status.setBackground(Color.RED);
				}
				if (s.equals("5") || s.equals("fertig")) {
					status.setText("fertig");
					status.setBackground(Color.GREEN);
				}
				if (s.equals("4") || s.equals("in Bearbeitung")) {
					status.setText("in Bearbeitung");
					status.setBackground(Color.YELLOW);
				}
				if (s.equals("3") || s.equals("nicht bestellt")) {
					status.setText("nicht bestellt");
					status.setBackground(Color.YELLOW);
				}

				eineBanf[i].add(einKopf[i], BorderLayout.NORTH);

				String sql2 = "SELECT * FROM banfpos bp, material m, inventurgruppe i WHERE bp.banf="
						+ hak[i]
						+ " AND "
						+ "bp.bezeichnung=m.id AND m.inventurgruppe=i.id";
				ResultSet rs2 = con.mysql_query(sql2);

				int y = 0;

				JLabel banfposT = new JLabel("BPos-nr");
				banfposT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel bezT = new JLabel("Bezeichnung");
				bezT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel inventGr = new JLabel("Inventurgruppe");
				inventGr.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel mengeT = new JLabel("Menge");
				mengeT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel preisT = new JLabel("Preis exkl");
				preisT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel mwstT = new JLabel("MWSt");
				mwstT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel statuspT = new JLabel("Status");
				statuspT.setFont(new Font("Arial", Font.BOLD, 14));

				einPos[i].add(banfposT, "65");
				einPos[i].add(bezT, "28%");
				einPos[i].add(inventGr, "20%");
				einPos[i].add(mengeT, "70");
				einPos[i].add(preisT, "100");
				einPos[i].add(mwstT, "100");
				einPos[i].add(statuspT, "170");

				while (rs2.next()) {

					JLabel banfpos = new JLabel(rs2.getString("banfposnr"));
					banfpos.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel bez = new JLabel(rs2.getString("m.bezeichnung"));
					bez.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel inv = new JLabel(rs2.getString("i.bezeichnung"));
					bez.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel menge = new JLabel(rs2.getString("menge"));
					menge.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel preis = new JLabel(rs2.getString("preisExkl"));
					preis.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel mwst = new JLabel(rs2.getString("mwst"));
					mwst.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel statusp = new JLabel(rs2.getString("status"));
					statusp.setFont(new Font("Arial", Font.PLAIN, 13));
					statusp.setOpaque(true);

					s = rs2.getString("status");

					einPos[i].add(banfpos, "");
					einPos[i].add(bez, "");
					einPos[i].add(inv, "");
					einPos[i].add(menge, "");
					einPos[i].add(preis, "");
					einPos[i].add(mwst, "");
					einPos[i].add(statusp, "");

					if (s.equals("1") || s.equals("nicht bestellt")) {
						statusp.setText("nicht bestellt");
						statusp.setBackground(Color.YELLOW);
					}
					if (s.equals("2") || s.equals("bestellt")) {
						statusp.setText("bestellt");
						statusp.setBackground(Color.GREEN);
					}
					if (s.equals("3") || s.equals("abgewiesen")) {
						statusp.setText("abgewiesen");
						statusp.setBackground(Color.RED);
					}

					double preisE = rs2.getDouble("preisExkl");
					double mwstD = rs2.getDouble("mwst");
					double mengeD = rs2.getDouble("menge");

					summe = summe + ((preisE * mwstD / 100) + preisE) * mengeD;

					y++;

				}

				DatenImport di = new DatenImport();
				Double summeGerundet = di.runde(summe);

				JPanel neu = new JPanel();
				neu.setLayout(new GridLayout(2, 1));
				neu.add(einPos[i]);
				neu.add(new JLabel("Summe: " + summeGerundet, JLabel.CENTER));

				eineBanf[i].add(neu, BorderLayout.CENTER);
				// /eineBanf[i].add(einPos[i], BorderLayout.CENTER);
				all.add(eineBanf[i]);

				rs1.close();
				rs2.close();

			}// für jede Banf

		} catch (Exception ex) {
			System.out.println("ERROR anzBanfDet: " + ex.getMessage());
		}

		setVisible(true);

		JScrollPane sc;
		sc = new JScrollPane(all);

		c.add(sc);

	}

	public void repaint2(Graphics g) {
		for (int i = 0; i < hak.length; i++) {
			if (einKopf[i] != null) {
				einKopf[i].setPreferredSize(new Dimension(getWidth() - 90,
						einKopf[i].getPreferredSize().height));
			}
			if (einPos[i] != null) {
				einPos[i].setPreferredSize(new Dimension(getWidth() - 90,
						einPos[i].getPreferredSize().height));
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < banfID.length; i++) {
			if (e.getSource() == kommentar[i]) {
				Kommentar kommentarEingabe = new Kommentar("Kommentar",
						banfID[i], con);
				kommentarEingabe.setSize(300, 200);
				kommentarEingabe.setLocationRelativeTo(null);
				kommentarEingabe.setVisible(true);
			}
		}

	}
}