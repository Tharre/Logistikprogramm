package Logistik;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

import Budget.DatenImport;

/**
 * neues Fenster für Detialansicht bei Bestellungen verwendetes Layout:
 * BanfLayout Anzeige aller ausgewählten Bestellungen in Detailansicht
 */
public class AnzBestDetailNeu extends JFrame implements ActionListener {
	/**
	 * Array mit angehakten Bestellungen
	 */
	public Object[] hak;

	/**
	 * Array mit Bestellungen
	 */
	public JPanel[] eineBest;

	/**
	 * Array mit Bestellkopf-Daten
	 */
	public JPanel[] einKopf;

	/**
	 * Array mit Bestellpositions-Daten
	 */
	public JPanel[] einPos;

	public String query, query1, query2;
	private double summe;
	private JButton bKommentar;

	public JButton[][] arrayButton;
	private int[][] iBestPosIds;
	public int anzBestPos = 0;
	private DBConnection con;

	public AnzBestDetailNeu(DBConnection con, Object[] hakDet) {

		this.con = con;

		Container c = getContentPane();

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(getToolkit().getScreenSize().width, 700);

		Object[] hak = new Object[hakDet.length];
		for (int x = 0; x < hakDet.length; x++) {
			hak[x] = hakDet[x];

		}

		c.setLayout(new GridLayout(1, 1));
		eineBest = new JPanel[hak.length];
		einKopf = new JPanel[hak.length];
		einPos = new JPanel[hak.length];
		iBestPosIds = new int[hak.length][];
		arrayButton = new JButton[hak.length][];

		JPanel all = new JPanel(new GridLayout(hak.length, 1));
		JLabel[] linie = new JLabel[hak.length];

		for (int y = 0; y < hak.length; y++) {
			linie[y] = new JLabel(
					"<html><div align=\"center\"><hr size=\"4\" width=\"3000\"> </div></html>\n");
			linie[y].setFont(new Font("Arial", Font.BOLD, 20));
			eineBest[y] = new JPanel();
			einKopf[y] = new JPanel();
			einPos[y] = new JPanel();
			eineBest[y] = new JPanel();
			eineBest[y].setLayout(new BorderLayout());
			eineBest[y].add(linie[y], BorderLayout.SOUTH);
			// einKopf[y].setLayout(new BanfLayout(6)); //kopf
			einKopf[y].setLayout(new LayoutBanf(7)); // kopf
			einPos[y].setLayout(new LayoutBanf(13)); // PosDaten

		}

		try {
			boolean falGel = false;

			for (int i = 0; i < hak.length; i++) {
				summe = 0;

				query = "select count(*) from bestpos where bestellId ="
						+ hak[i];
				ResultSet rs = con.mysql_query(query);
				rs.next();

				anzBestPos = rs.getInt("count(*)");

				iBestPosIds[i] = new int[anzBestPos];
				arrayButton[i] = new JButton[anzBestPos];

				query1 = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer, budget FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND bestId="
						+ hak[i] + " ORDER BY datum DESC";
				ResultSet rs1 = con.mysql_query(query1);
				rs1.next();

				JLabel bestidT = new JLabel("ID");
				bestidT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel bestid = new JLabel(rs1.getString("wnummer"));
				bestid.setFont(new Font("Arial", Font.PLAIN, 13));
				JLabel datumT = new JLabel("Datum");
				datumT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel datum = new JLabel(new java.util.Date(Long.parseLong(rs1
						.getString("datum")) * 1000).toString());
				datum.setFont(new Font("Arial", Font.PLAIN, 13));
				JLabel lehrerT = new JLabel("Antragsteller");
				lehrerT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel lehrer = new JLabel(rs1.getString("cn"));
				lehrer.setFont(new Font("Arial", Font.PLAIN, 13));

				JLabel firmaT = new JLabel("Firma");
				firmaT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel firma = new JLabel(rs1.getString("firmenname"));
				firma.setFont(new Font("Arial", Font.PLAIN, 13));

				JLabel budget = new JLabel("Budget");
				firmaT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel budgetL = new JLabel(rs1.getString("budget"));
				firma.setFont(new Font("Arial", Font.PLAIN, 13));

				JLabel statusT = new JLabel("Status Lief");
				statusT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel status = new JLabel(rs1.getString("status"));
				status.setFont(new Font("Arial", Font.PLAIN, 13));
				status.setOpaque(true);
				JLabel statusbT = new JLabel("Status Bez");
				statusbT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel statusb = new JLabel(rs1.getString("statusbez"));
				statusb.setFont(new Font("Arial", Font.PLAIN, 13));
				statusb.setOpaque(true);

				String s = rs1.getString("status");

				einKopf[i].add(bestidT, "60");
				einKopf[i].add(datumT, "200");
				einKopf[i].add(lehrerT, "20%");
				einKopf[i].add(firmaT, "23%");
				einKopf[i].add(budget, "130");
				einKopf[i].add(statusT, "200");// war 130
				einKopf[i].add(statusbT, "120");
				einKopf[i].add(bestid, "");
				einKopf[i].add(datum, "");
				einKopf[i].add(lehrer, "");
				einKopf[i].add(firma, "");
				einKopf[i].add(budgetL, "");
				einKopf[i].add(status, "");
				einKopf[i].add(statusb, "");

				if (s.equals("1") || s.equals("nicht abgeschickt")) {
					status.setText("nicht abgeschickt");
					status.setBackground(Color.RED);
				}
				if (s.equals("3") || s.equals("abgeschickt")) {
					status.setText("abgeschickt");
					status.setBackground(Color.RED);
				}
				if (s.equals("4") || s.equals("teilw falsch geliefert")) {
					status.setText("teilw falsch/nicht geliefert");
					status.setBackground(Color.YELLOW);
				}
				if (s.equals("5") || s.equals("teilw richtig geliefert")) {
					status.setText("teilw richtig geliefert");
					status.setBackground(Color.YELLOW);
				}
				if (s.equals("6") || s.equals("falsch geliefert")) {
					status.setText("komplett - Teile nicht lieferbar");
					status.setBackground(Color.GREEN);
					falGel = true;
				}
				if (s.equals("7") || s.equals("richtig geliefert")) {
					status.setText("richtig geliefert");
					status.setBackground(Color.GREEN);
				}

				if (s.equals("15") || s.equals("gelöscht")) {
					status.setText("gelöscht");
					status.setBackground(new Color(151, 217, 236));
				}

				String sb = rs1.getString("statusbez");

				if (sb.equals("0") || sb.equals("keine Lieferung")) {
					statusb.setText("keine Lieferung");
					statusb.setBackground(Color.RED);
				}
				if (sb.equals("1") || sb.equals("teilw geliefert")) {
					statusb.setText("teilw geliefert");
					statusb.setBackground(Color.RED);
				}
				if (sb.equals("2") || sb.equals("komplett geliefert")) {
					statusb.setText("komplett geliefert");
					statusb.setBackground(Color.RED);
				}
				if (sb.equals("3") || sb.equals("teilw bezahlt")) {
					statusb.setText("teilw bezahlt");
					statusb.setBackground(Color.YELLOW);
				}
				if (sb.equals("4") || sb.equals("komplett bezahlt")) {
					statusb.setText("komplett bezahlt");
					statusb.setBackground(Color.GREEN);
				}

				eineBest[i].add(einKopf[i], BorderLayout.NORTH);

				query2 = "SELECT m.bezeichnung,bep.menge,bep.preisExkl,bep.mwst,bep.preisInkl,bep.preisGesamt,"
						+ "bep.einheit,bep.lehrer,bep.id,bep.status,bep.statusbez,bep.lieferkommentar,i.bezeichnung FROM  "
						+ "bestpos bep,material m,inventurgruppe i WHERE bep.bestellId="
						+ hak[i]
						+ " AND bep.bezeichnung=m.id AND "
						+ "m.inventurgruppe = i.id ORDER BY id ASC"; // lehrername!?!
				ResultSet rs2 = con.mysql_query(query2);

				JLabel bestposT = new JLabel("ID");
				bestposT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel bezT = new JLabel("Bezeichnung");
				bezT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel inventGr = new JLabel("Inventurgruppe");
				inventGr.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel mengeT = new JLabel("Menge");
				mengeT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel preiseT = new JLabel("Preis exkl");
				preiseT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel mwstT = new JLabel("MWSt");
				mwstT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel preisiT = new JLabel("Preis inkl");
				preisiT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel preisgT = new JLabel("Preis ges");
				preisgT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel einheitT = new JLabel("Einheit");
				einheitT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel lehrerpT = new JLabel("Antragsteller");
				lehrerpT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel statuspT = new JLabel("Status Lief");
				statuspT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel statuspbT = new JLabel("Status Bez");
				statuspbT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel lKommentar = new JLabel("");

				einPos[i].add(bestposT, "30");
				einPos[i].add(bezT, "20%");
				einPos[i].add(inventGr, "140");
				einPos[i].add(mengeT, "60");
				einPos[i].add(preiseT, "75");
				einPos[i].add(mwstT, "47");
				einPos[i].add(preisiT, "75");
				einPos[i].add(preisgT, "75");
				einPos[i].add(einheitT, "50");
				einPos[i].add(lehrerpT, "135");
				einPos[i].add(statuspT, "100");
				einPos[i].add(statuspbT, "100");
				einPos[i].add(lKommentar, "100");

				int zaehler = 0;
				while (rs2.next()) {

					JLabel bestpos = new JLabel(rs2.getString("id"));
					bestpos.setFont(new Font("Arial", Font.PLAIN, 13));
					iBestPosIds[i][zaehler] = (rs2.getInt("id"));
					JLabel bez = new JLabel(rs2.getString("m.bezeichnung"));
					// bez.setToolTipText(rs2.getString("lieferkommentar"));
					bez.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel inv = new JLabel(rs2.getString("i.bezeichnung"));
					inv.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel menge = new JLabel(rs2.getString("menge"));
					// menge.setToolTipText("geliefert:"+rs2.getString("geliefert"));
					menge.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel preise = new JLabel(rs2.getString("preisExkl"));
					preise.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel mwst = new JLabel(rs2.getString("mwst"));
					mwst.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel preisi = new JLabel(rs2.getString("preisInkl"));
					preisi.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel preisg = new JLabel(rs2.getString("preisGesamt"));
					preisg.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel einheit = new JLabel(rs2.getString("einheit"));
					einheit.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel lehrerp = new JLabel(rs2.getString("lehrer"));
					lehrerp.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel statusp = new JLabel(rs2.getString("status"));
					statusp.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel statuspb = new JLabel(rs2.getString("statusbez"));
					statuspb.setFont(new Font("Arial", Font.PLAIN, 13));
					statusp.setOpaque(true);
					statuspb.setOpaque(true);
					arrayButton[i][zaehler] = new JButton("Kommentar");
					arrayButton[i][zaehler].addActionListener(this);
					// arrayButton[i].setName(""+i);
					arrayButton[i][zaehler].setActionCommand("" + i + ""
							+ zaehler);

					einPos[i].add(bestpos, "");
					einPos[i].add(bez, "");
					einPos[i].add(inv, "");
					einPos[i].add(menge, "");
					einPos[i].add(preise, "");
					einPos[i].add(mwst, "");
					einPos[i].add(preisi, "");
					einPos[i].add(preisg, "");
					einPos[i].add(einheit, "");
					einPos[i].add(lehrerp, "");
					einPos[i].add(statusp, "");
					einPos[i].add(statuspb, "");
					einPos[i].add(arrayButton[i][zaehler], "");
					zaehler++;

					String sp = rs2.getString("status");

					if (sp.equals("1") || sp.equals("nicht abgeschickt")) {
						statusp.setText("nicht abgeschickt");
						statusp.setBackground(Color.RED);
					}
					if (sp.equals("2") || sp.equals("abgeschickt")) {
						statusp.setText("abgeschickt");
						statusp.setBackground(Color.RED);
					}
					if (sp.equals("4") || sp.equals("abweichend geliefert")) {
						statusp.setText("abweichend geliefert");
						statusp.setBackground(Color.YELLOW);
					}
					if (sp.equals("3") || sp.equals("richtig geliefert")) {
						statusp.setText("richtig geliefert");
						statusp.setBackground(Color.GREEN);
					}
					if (sp.equals("5") || sp.equals("nicht lieferbar")) {
						statusp.setText("nicht lieferbar");
						statusp.setBackground(Color.RED);
					}
					if (sp.equals("6")
							|| sp.equals("komplett - Teile nicht lieferbar")) {
						statusp.setText("komplett - Teile nicht lieferbar");
						statusp.setBackground(Color.GREEN);
					}

					if (s.equals("15") || s.equals("gelöscht")) {
						statusp.setText("gelöscht");
						statusp.setBackground(new Color(151, 217, 236));
					}

					String spb = rs2.getString("statusbez");

					if (spb.equals("0") || spb.equals("keine Lieferung")) {
						statuspb.setText("keine Lieferung");
						statuspb.setBackground(Color.RED);
					}
					if (spb.equals("1") || spb.equals("teilw geliefert")) {
						statuspb.setText("teilw geliefert");
						statuspb.setBackground(Color.RED);
					}
					if (spb.equals("2") || spb.equals("komplett geliefert")) {
						statuspb.setText("komplett geliefert");
						statuspb.setBackground(Color.RED);
					}
					if (spb.equals("3") || spb.equals("teilw bezahlt")) {
						statuspb.setText("teilw bezahlt");
						statuspb.setBackground(Color.YELLOW);
					}
					if (spb.equals("4") || spb.equals("komplett bezahlt")) {
						statuspb.setText("komplett bezahlt");
						statuspb.setBackground(Color.GREEN);
					}
					double preisE = rs2.getDouble("preisExkl");
					double mwstD = rs2.getDouble("mwst");
					double mengeD = rs2.getDouble("menge");

					summe = summe + ((preisE * mwstD / 100) + preisE) * mengeD;

				}

				DatenImport di = new DatenImport();
				Double summeGerundet = di.runde(summe);

				JPanel neu = new JPanel();
				neu.setLayout(new GridLayout(2, 1));
				neu.add(einPos[i]);
				neu.add(new JLabel("Summe: " + summeGerundet, JLabel.CENTER));

				eineBest[i].add(neu, BorderLayout.CENTER);
				all.add(eineBest[i]);

				rs1.close();
				rs2.close();

			}// für jede Bestellung
		} catch (Exception ex) {

			ex.getMessage();
			ex.printStackTrace();
		}

		setVisible(true);

		JScrollPane sc;
		sc = new JScrollPane(all);

		c.add(sc);
		c.repaint();
	}

	public void actionPerformed(ActionEvent e) {

		for (int i = 0; i < arrayButton.length; i++) {
			for (int j = 0; j < arrayButton[i].length; j++) {
				if (e.getActionCommand().equals("" + i + "" + j)) {
					Kommentar frame = new Kommentar(iBestPosIds[i][j], con);
					frame.setSize(300, 200);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);

				}
			}

		}

	}
}