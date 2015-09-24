package Logistik;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Date;
import java.sql.*;

/**
 * Mit RadioButton, Button und ConfirmDialog muss bestätigt werden, ob Daten
 * wirklich gelöscht werden sollen Es werden nur BANF, BANFPositionen,
 * Bestellungen, Bestellpositionen aus dem Vorjahr gelöscht
 */
public class DBLeeren extends LayoutMainPanel implements ActionListener {
	/**
	 * RadioButton für Bestätigung des Löschvorgangs
	 */
	public JRadioButton leerenRB = new JRadioButton();

	/**
	 * Button für Bestätigung des Löschvorgangs
	 */
	public JButton leerenB = new JButton("leeren");
	JTextField tag = new JTextField("TT");
	JTextField monat = new JTextField("MM");
	JTextField jahr = new JTextField("JJJJ");
	JTextField wNummer = new JTextField("Wxx-xx");
	JTextField bestId = new JTextField("Bestell-ID");

	ButtonGroup buttongroup = new ButtonGroup();

	JRadioButton datum = new JRadioButton("Datum");
	JRadioButton wNummerB = new JRadioButton("W- Nummer");
	JRadioButton bestIDB = new JRadioButton("Bestell- ID");

	private int tagI;
	private int monatI;
	private int jahrI;
	private String wNummerS;
	private int bestID;
	private int kennzahl;
	private String query;
	private String datumS;
	private Date datumD;
	private int status;

	/**
	 * Überschrift
	 */
	private JLabel titel = new JLabel("DATENBANK LEEREN");

	public DBLeeren(UserImport user) {
		super(user);

		titel.setFont(new Font("Arial", Font.BOLD, 20));

		JPanel bg = new JPanel();
		JPanel datumP = new JPanel();

		/*
		 * tag.setEditable(false); monat.setEditable(false);
		 * jahr.setEditable(false); wNummer.setEditable(false);
		 * bestId.setEditable(false);
		 */

		buttongroup.add(datum);
		buttongroup.add(wNummerB);
		buttongroup.add(bestIDB);

		bg.setLayout(new GridLayout(2, 1));

		bg.add(new JLabel(
				"Bis zu welchen Datum wollen Sie die Datenbank leeren?"));
		datumP.add(new JLabel("Tag"));
		datumP.add(tag);
		datumP.add(new JLabel("Monat"));
		datumP.add(monat);
		datumP.add(new JLabel("Jahr"));
		datumP.add(jahr);
		bg.add(datumP);

		JPanel mitte = new JPanel();
		mitte.setLayout(new GridLayout(4, 2));

		mitte
				.add(new JLabel(
						"Nach welchen Kriterium möchten Sie eine oder mehrere Bestellung(en) löschen?"));
		mitte.add(new JLabel(""));

		mitte.add(datum);
		mitte.add(bg);
		mitte.add(wNummerB);
		mitte.add(wNummer);
		mitte.add(bestIDB);
		mitte.add(bestId);

		setLayoutM(new BorderLayout());
		addM(titel, BorderLayout.NORTH);
		addM(mitte, BorderLayout.CENTER);
		addM(leerenB, BorderLayout.SOUTH);

		leerenB.addActionListener(this);
		datum.addActionListener(this);
		wNummerB.addActionListener(this);
		bestIDB.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == leerenB) {

			if (datum.isSelected()) {

				tag.setEditable(true);
				monat.setEditable(true);
				jahr.setEditable(true);

				try {

					tagI = Integer.parseInt(tag.getText());
					monatI = Integer.parseInt(monat.getText());
					jahrI = Integer.parseInt(jahr.getText());
					kennzahl = 1;

				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,
							"Sie dürfen nur Zahlen eintragen");

				}
			}
			if (bestIDB.isSelected()) {

				bestId.setEditable(true);

				try {

					bestID = Integer.parseInt(bestId.getText());
					kennzahl = 2;
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,
							"Sie dürfen nur Zahlen eintragen");

				}
			}
			if (wNummerB.isSelected()) {

				wNummer.setEditable(true);

				wNummerS = wNummer.getText();
				kennzahl = 3;
			}
			int sure;

			if (kennzahl == 1) {
				datumS = "" + tagI + "." + monatI + "." + jahrI;
				sure = JOptionPane.showConfirmDialog(null,
						"Wollen Sie die Datenbank bis zum  " + datumS
								+ "  sicher leeren?", "DB leeren",
						JOptionPane.YES_NO_OPTION);
			} else if (kennzahl == 2) {

				sure = JOptionPane
						.showConfirmDialog(
								null,
								"Wollen Sie die Bestellung, Bestellpositionen, Banfs, Banfposition mit der BestellID "
										+ bestID + " löschen?", "DB leeren",
								JOptionPane.YES_NO_OPTION);
			} else {
				sure = JOptionPane
						.showConfirmDialog(
								null,
								"Wollen Sie die Bestellung, Bestellpositionen, Banfs, Banfposition mit der W-Nummer "
										+ wNummerS + " löschen?", "DB leeren",
								JOptionPane.YES_NO_OPTION);

			}

			if (sure == 1) {
				JOptionPane
						.showMessageDialog(null, "Löschvorgang abgebrochen!");
				return;
			}
			if (sure == 0) {

				if (kennzahl == 1) {

					datumD = new Date(jahrI - 1900, monatI - 1, tagI);

					query = "select bestId,statusbez from bestellung where datum<"
							+ (datumD.getTime() / 1000);
				}
				if (kennzahl == 2) {
					query = "select bestId,statusbez from bestellung where bestId="
							+ bestID;

				}
				if (kennzahl == 3) {

					query = "select bestId,statusbez from bestellung where wnummer like'"
							+ wNummerS + "'";

				}

				ResultSet rsL = con.mysql_query(query);
				Vector<Integer> bestId = new Vector<Integer>();

				try {
					while (rsL.next()) {
						bestId.add(rsL.getInt("bestId"));
						status = rsL.getInt("statusbez");

					}

					rsL.close();

				} catch (SQLException e1) {

					e1.printStackTrace();
				}
				Vector<Integer> bestellposID = new Vector<Integer>();
				Vector<Integer> banfposID = new Vector<Integer>();
				Vector<Integer> banfID = new Vector<Integer>();

				for (int i = 0; i < bestId.size(); i++) {
					bestellposID = getBestellposIDsZuBestellID(bestId.get(i));

					for (int j = 0; j < bestellposID.size(); j++) {
						banfposID = getBanfposIDsZuBestellposID(bestellposID
								.get(j));

						for (int k = 0; k < banfposID.size(); k++) {

							banfID = getBanfIDsZuBanfposID(banfposID.get(k));

							for (int l = 0; l < banfID.size(); l++) {
								query = "update banf set status=3 where id="
										+ banfID.get(l);
								con.mysql_update(query);

							}

							query = "update banfpos set status=1 where banfposnr="
									+ banfposID.get(k);

							con.mysql_update(query);

						}
						query = "update bestpos set status=15 where id="
								+ bestellposID.get(j);
						con.mysql_update(query);

					}

					query = "update bestellung set status=15 where bestId="
							+ bestId.get(i);
					con.mysql_update(query);

					int schonAbgeschickt = sure = JOptionPane
							.showConfirmDialog(
									null,
									"Wurde die Bestellung bereits abgeschickt?",
									"Bestellung", JOptionPane.YES_NO_OPTION);

					if (schonAbgeschickt == 0) {
						vermindereGesperrt(
								getKostenstelleZuBestellId(bestId.get(i)),
								getBetragZuBestellId(bestId.get(i)),
								getTabellennameZuBudget(getBudget(bestId.get(i))),
								getBudget(bestId.get(i)));
					}

					JOptionPane.showMessageDialog(null,
							"Löschvorgang erfolgreich durchgeführt!");
				}

			}
		}

	}

	private void vermindereGesperrt(String kostenstelle, double betrag,
			String tabellenname, String budget) {

		if (budget.equals("LMB1")) {
			query = "update lmb set gesperrt=gesperrt-" + betrag
					+ " where name like '" + kostenstelle + "' and kennung=1";
		} else if (budget.equals("LMB1_2011")) {
			query = "update lmb set gesperrt=gesperrt-" + betrag
					+ " where name like '" + kostenstelle + "' and kennung=2";
		} else {
			query = "update " + tabellenname + " set gesperrt=gesperrt-"
					+ betrag + " where name like '" + kostenstelle + "'";
		}

		conB.mysql_update(query);

	}

	private void vermindereAusgegeben(String kostenstelle, double betrag,
			String tabellenname, String budget) {
		int kennung;

		if (budget.equals("LMB1")) {
			query = "update lmb set ausgegeben=ausgegeben-" + betrag
					+ " where name like '" + kostenstelle + "' and kennung=1";
		} else if (budget.equals("LMB1_2011")) {
			query = "update lmb set ausgegeben=ausgegeben-" + betrag
					+ " where name like '" + kostenstelle + "' and kennung=2";
		} else {
			query = "update " + tabellenname + " set ausgegeben=ausgegeben-"
					+ betrag + " where name like '" + kostenstelle + "'";
		}

		conB.mysql_update(query);

	}

	private String getTabellennameZuBudget(String budget) {

		if (budget.equals("UT8")) {
			return "kostenstelleut8";
		} else if (budget.equals("UT3")) {
			return "abteilungut3";
		} else if (budget.equals("LMB1")) {
			return "lmb";
		} else if (budget.equals("LMB1_2011")) {
			return "lmb";
		} else {
			return "sonderbudget";
		}
	}

	private String getKostenstelleZuBestellId(int bestID) {
		query = "select kostenstelle from banf b, banfpos fp, bestpos bp"
				+ " where b.id=fp.banf" + " AND fp.banfposnr=bp.banfposnr"
				+ " AND bp.bestellId=" + bestID;

		String kostenstelle = "";
		ResultSet rs = con.mysql_query(query);
		try {
			while (rs.next()) {
				kostenstelle = rs.getString("kostenstelle");
			}

			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return kostenstelle;
	}

	private String getBudget(int bestID) {
		query = "select budget from bestellung where bestId=" + bestID;

		String budget = "";
		ResultSet rs = con.mysql_query(query);
		try {
			while (rs.next()) {
				budget = rs.getString("budget");
			}

			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return budget;
	}

	private double getBetragZuBestellId(int bestID) {
		query = "select sum(preisGesamt) from bestpos where bestellId="
				+ bestID;

		double betrag = 0;
		ResultSet rs = con.mysql_query(query);
		try {
			while (rs.next()) {
				betrag = rs.getDouble("sum(preisGesamt)");
			}

			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return betrag;
	}

	private Vector<Integer> getBestellposIDsZuBestellID(int bestID) {
		Vector<Integer> bestposIds = new Vector<Integer>();

		query = "select id from bestpos where bestellId = " + bestID;
		ResultSet rsB = con.mysql_query(query);

		try {
			while (rsB.next()) {
				bestposIds.add(rsB.getInt("id"));

			}

			rsB.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bestposIds;

	}

	private Vector<Integer> getBanfposIDsZuBestellposID(int bestposID) {
		Vector<Integer> banfposIds = new Vector<Integer>();

		query = "select banfposnr from bestpos where id = " + bestposID;
		ResultSet rsB = con.mysql_query(query);

		try {
			while (rsB.next()) {
				banfposIds.add(rsB.getInt("banfposnr"));

			}

			rsB.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return banfposIds;

	}

	private Vector<Integer> getBanfIDsZuBanfposID(int banfposID) {
		Vector<Integer> banfIds = new Vector<Integer>();

		query = "select banf from banfpos where banfposnr = " + banfposID;
		ResultSet rsB = con.mysql_query(query);

		try {
			while (rsB.next()) {
				banfIds.add(rsB.getInt("banf"));

			}

			rsB.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return banfIds;
	}

}