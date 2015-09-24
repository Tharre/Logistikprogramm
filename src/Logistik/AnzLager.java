package Logistik;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Vector;

/*
 * Zeigt die Details der ausgewählten, noch nicht gelieferten Bestellungen an
 * und ermöglicht die Auswahl der der Stati der Bestellpositionen.
 */
public class AnzLager extends JFrame implements ActionListener {
	public Object[] hak;
	private Object[][][] liefern;
	public JPanel[][] alle;
	// private String stueck[][];
	private String geliefertAnzahl[];
	// private String komment[][];
	private String komment[];
	private AnzTabelleA kat[];
	private AnzTabelleA pat[];
	private DBConnection con;
	private JButton speichern = new JButton("speichern");
	private int bestID = 0;
	private double stueckI;
	private int bestIDvorher = 0;
	private DBConnection conB;
	private JPanel center;
	private Vector<Integer> bestIDs = new Vector<Integer>();

	public AnzLager(DBConnection con, Object[] hakDet, DBConnection conB) {
		this.con = con;
		this.conB = conB;

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		center = new JPanel();

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(getToolkit().getScreenSize().width, 700);

		Object[] hak = new Object[hakDet.length];
		for (int x = 0; x < hakDet.length; x++) {
			hak[x] = hakDet[x];
		}

		// c.setLayout(new GridLayout(hak.length + 1, 1));
		center.setLayout(new GridLayout(hak.length, 1));
		alle = new JPanel[hak.length][3];
		JLabel[] linie = new JLabel[hak.length];
		for (int y = 0; y < hak.length; y++) {
			alle[y][0] = new JPanel();
			alle[y][1] = new JPanel();
			alle[y][2] = new JPanel();
			// alle[y][3] = new JPanel();
			/*
			 * linie[y] = new JLabel(
			 * "<html><div align=\"center\"><hr size=\"1\" width=\"3000\"> </div></html>"
			 * ); linie[y].setFont(new Font("Arial", Font.BOLD, 20));
			 */
			// alle[y][0].setLayout(new GridLayout(3, 1)); //
			// gesamt,untereinander
			alle[y][0].setLayout(new GridLayout(3, 1));
			alle[y][1].setLayout(new GridLayout(1, 1)); // kopf
			// alle[y][1].setSize(getToolkit().getScreenSize().width, 50);
			alle[y][2].setLayout(new GridLayout(1, 1)); // PosDaten
		}

		JScrollPane[] sc1 = new JScrollPane[hak.length];
		JScrollPane[] sc2 = new JScrollPane[hak.length];
		kat = new AnzTabelleA[hak.length];
		pat = new AnzTabelleA[hak.length];

		for (int i = 0; i < hak.length; i++) {

			String query1 = "SELECT f.firmenname,u.name,b.datum,b.bestId,b.status,b.statusbez FROM bestellung b,ldap_user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND bestId="
					+ hak[i] + " ORDER BY datum DESC";
			ResultSet rs1 = con.mysql_query(query1);
			String[] spaltenDB1 = { "bestID", "datum", "name", "firmenname",
					"status", "statusbez" };
			String[] spaltenT1 = { "Bestell ID", "Datum", "Antragsteller",
					"Firma", "Status Lieferung", "Status Bezahlung" };
			Class[] klass1 = { Integer.class, Date.class, String.class,
					String.class, Integer.class, Integer.class }; // EIG DATE ->
			// UMWANDELN
			sc1[i] = new JScrollPane(kat[i] = new AnzTabelleA(spaltenDB1,
					spaltenT1, klass1, rs1, 1));
			LayoutRow[] detR = kat[i].getRows();
			for (int x = 0; x < detR.length; x++) {
				String s = detR[x].getData(4).toString();
				if (s.equals("1") || s.equals("nicht abgeschickt")) {
					kat[i].setValueAt("nicht abgeschickt", x, 4);
					detR[x].setColor(Color.RED);
				}
				if (s.equals("3") || s.equals("abgeschickt")) {
					kat[i].setValueAt("abgeschickt", x, 4);
					detR[x].setColor(Color.RED);
				}
				if (s.equals("4") || s.equals("teilw falsch/nicht geliefert")) {
					kat[i].setValueAt("teilw falsch/nicht geliefert", x, 4);
					detR[x].setColor(Color.YELLOW);
				}
				if (s.equals("5") || s.equals("teilw richtig geliefert")) {

					kat[i].setValueAt("teilw richtig geliefert", x, 4);
					detR[x].setColor(Color.YELLOW);
				}
				if (s.equals("6") || s.equals("falsch geliefert")) {
					kat[i].setValueAt("komplett - Teile nicht lieferbar", x, 4);
					detR[x].setColor(Color.GREEN);
				}
				if (s.equals("7") || s.equals("richtig geliefert")) {
					kat[i].setValueAt("richtig geliefert", x, 4);
					detR[x].setColor(Color.GREEN);
				}
				if (s.equals("15") || s.equals("gelöscht")) {
					kat[i].setValueAt("gelöscht", x, 4);
					detR[x].setColor(new Color(151, 217, 236));
				}

				String sb = detR[x].getData(5).toString();
				if (sb.equals("0") || sb.equals("keine Lieferung")) {
					kat[i].setValueAt("keine Lieferung", x, 5);
				}
				if (sb.equals("1") || sb.equals("teilw geliefert")) {
					kat[i].setValueAt("teilw geliefert", x, 5);
				}
				if (sb.equals("2") || sb.equals("komplett geliefert")) {
					kat[i].setValueAt("komplett geliefert", x, 5);
				}
				if (sb.equals("3") || sb.equals("teilw bezahlt")) {
					kat[i].setValueAt("teilw bezahlt", x, 5);
				}
				if (sb.equals("4") || sb.equals("komplett bezahlt")) {
					kat[i].setValueAt("komplett bezahlt", x, 5);
				}
				if (s.equals("15") || s.equals("gelöscht")) {
					kat[i].setValueAt("gelöscht", x, 5);
				}
			}
			alle[i][1].add(sc1[i]);
			alle[i][0].add(alle[i][1]);

			String query2 = "SELECT m.bezeichnung,bep.menge,bep.preisExkl,bep.mwst,bep.preisInkl,bep.preisGesamt,bep.einheit,u.name,bep.id,bep.status,bep.statusbez, bep.geliefert, bep.lieferkommentar FROM bestpos bep, ldap_user u, material m  WHERE bep.lehrer = u.cn AND bep.bezeichnung=m.id AND bep.bestellId="
					+ hak[i]
					+ " AND (Status=2 OR Status=4) AND (bep.geliefert<bep.menge) ORDER BY id ASC";
			ResultSet rs2 = con.mysql_query(query2);
			String[] spaltenDB2 = { "id", "bezeichnung", "menge", "preisExkl",
					"mwst", "preisInkl", "preisGesamt", "einheit", "name",
					"status", "statusbez", "einheit", "einheit", "einheit",
					"einheit", "geliefert", "lieferkommentar" };
			String[] spaltenT2 = { "ID", "Bezeichnung", "Menge", "Preis exkl",
					"MWSt", "Preis Inkl", "Preis gesamt", "Einheit", "Name",
					"Status Lieferung", "Status Bezahlung", "geliefert",
					"abweichend geliefert", "Rest kommt nicht mehr",
					"nicht lieferbar", "Stück geliefert", "Kommentar" };
			Class[] klass2 = { Integer.class, String.class, Double.class,
					String.class, String.class, String.class, String.class,
					String.class, String.class, Integer.class, Integer.class,
					Boolean.class, Boolean.class, Boolean.class, Boolean.class,
					JTextField.class, JTextField.class };
			sc2[i] = new JScrollPane(pat[i] = new AnzTabelleA(spaltenDB2,
					spaltenT2, klass2, rs2, 1));
			LayoutRow[] detR2 = pat[i].getRows();
			for (int y = 0; y < detR2.length; y++) {
				String s = detR2[y].getData(9).toString();
				if (s.equals("1") || s.equals("nicht abgeschickt")) {
					pat[i].setValueAt("nicht abgeschickt", y, 9);
					detR2[y].setColor(Color.RED);
				}
				if (s.equals("2") || s.equals("abgeschickt")) {
					pat[i].setValueAt("abgeschickt", y, 9);
					detR2[y].setColor(Color.RED);
				}
				if (s.equals("4") || s.equals("abweichend geliefert")) {
					pat[i].setValueAt("abweichend geliefert", y, 9);
					detR2[y].setColor(Color.YELLOW);
				}
				if (s.equals("3") || s.equals("richtig geliefert")) {
					pat[i].setValueAt("richtig geliefert", y, 9);
					detR2[y].setColor(Color.GREEN);
				}
				if (s.equals("5") || s.equals("nicht lieferbar")) {
					pat[i].setValueAt("nicht lieferbar", y, 9);
					detR2[y].setColor(Color.RED);
				}
				if (s.equals("6")
						|| s.equals("komplett - Teile nicht lieferbar")) {
					pat[i].setValueAt("komplett - Teile nicht lieferbar", y, 9);
					detR2[y].setColor(Color.GREEN);
				}

				String sb = detR2[y].getData(10).toString();

				if (sb.equals("0") || sb.equals("keine Lieferung")) {
					pat[i].setValueAt("keine Lieferung", y, 10);
				}
				if (sb.equals("1") || sb.equals("teilw geliefert")) {
					pat[i].setValueAt("teilw geliefert", y, 10);
				}
				if (sb.equals("2") || sb.equals("komplett geliefert")) {
					pat[i].setValueAt("komplett geliefert", y, 10);
				}
				if (sb.equals("3") || sb.equals("teilw bezahlt")) {
					pat[i].setValueAt("teilw bezahlt", y, 10);
				}
				if (sb.equals("4") || sb.equals("komplett bezahlt")) {
					pat[i].setValueAt("komplett bezahlt", y, 10);
				}
			}
			alle[i][2].add(sc2[i]);
			alle[i][0].add(alle[i][2]);
			// alle[i][0].add(linie[i]);

			// c.add(alle[i][0]);
			center.add(alle[i][0]);

		}// für jede Bestellung
		/*
		 * JPanel p = new JPanel(); p.add(speichern); c.add(p);
		 */
		c.add(center, BorderLayout.CENTER);
		c.add(speichern, BorderLayout.SOUTH);
		speichern.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == speichern) {

			speichern.setEnabled(false);
			liefern = new Object[pat.length][4][];
			// liefern.length = Anzahl der ausgewählten ausständigen
			// Bestellungen

			for (int i = 0; i < pat.length; i++) {
				liefern[i][0] = pat[i].getAllKlicked("ID", "geliefert");
				liefern[i][1] = pat[i].getAllKlicked("ID",
						"abweichend geliefert");
				liefern[i][2] = pat[i].getAllKlicked("ID",
						"Rest kommt nicht mehr");
				liefern[i][3] = pat[i].getAllKlicked("ID", "nicht lieferbar");

			}

			String q = "";

			// !!geliefert
			for (int i = 0; i < liefern.length; i++) {
				// richtig geliefert pos

				komment = new String[liefern[i][1].length];

				for (int j = 0; j < liefern[i][0].length; j++) {

					komment[j] = pat[i].getValueAt(j, 16).toString();

					if (Integer.parseInt(liefern[i][0][j].toString()) != (-1)) {
						q = "Select bestellId from bestpos where id="
								+ liefern[i][0][j];

						ResultSet rs2345 = con.mysql_query(q);
						try {
							rs2345.next();
							{

								bestID = rs2345.getInt("bestellId");
								if (bestIDs.size() != 0) {
									bestIDvorher = bestIDs.lastElement();
									if (bestID != bestIDvorher) {
										bestIDs.add(rs2345.getInt("bestellId"));
									}
								} else {
									bestIDs.add(rs2345.getInt("bestellId"));
								}

							}

							rs2345.close();

						} catch (SQLException e4) {
							e4.printStackTrace();
						}

						try {

							String qrybp = "SELECT statusbez FROM bestpos WHERE id="
									+ liefern[i][0][j];

							ResultSet rsbp = con.mysql_query(qrybp);
							rsbp.next();
							int bezStatBp = rsbp.getInt("statusbez");

							String query;

							if (bezStatBp != 3 && bezStatBp != 4) {
								// komplett
								// bezahlt
								query = "UPDATE bestpos SET status=3, statusbez=2 Where id="
										+ liefern[i][0][j];
							} else {
								query = "UPDATE bestpos SET status=3 Where id="
										+ liefern[i][0][j];
							}

							con.mysql_update(query);

							String query2 = "select menge from bestpos where id="
									+ liefern[i][0][j];

							ResultSet rs = con.mysql_query(query2);
							double menge = 0;

							while (rs.next()) {
								menge = rs.getDouble("menge");

							}

							query = "UPDATE material SET stueck=(stueck+(SELECT menge FROM bestpos WHERE id="
									+ liefern[i][0][j]
									+ ")-(SELECT geliefert FROM bestpos WHERE id="
									+ liefern[i][0][j]
									+ ")) Where id=(SELECT bezeichnung FROM bestpos WHERE id="
									+ liefern[i][0][j] + ")";
							con.mysql_update(query);

							query = "update bestpos set lieferkommentar='"
									+ komment[j] + "', geliefert=" + menge
									+ " where id=" + liefern[i][0][j];
							con.mysql_update(query);

							rsbp.close();
							rs.close();

						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}// end if
				}
			}// end richtig geliefert

			// ---------------------------------------------------------------------------------------------

			// !! teilweise geliefert

			// komment = new String[pat.length];
			for (int i = 0; i < liefern.length; i++) {

				geliefertAnzahl = new String[liefern[i][1].length];
				komment = new String[liefern[i][1].length];

				for (int j = 0; j < liefern[i][1].length; j++) {

					komment[j] = pat[i].getValueAt(j, 16).toString();

					if (Integer.parseInt(liefern[i][1][j].toString()) != (-1)) {
						q = "Select bestellId from bestpos where id="
								+ liefern[i][1][j];

						ResultSet rs234 = con.mysql_query(q);
						try {
							rs234.next();
							{

								bestID = rs234.getInt("bestellId");
								if (bestIDs.size() != 0) {
									bestIDvorher = bestIDs.lastElement();
									if (bestID != bestIDvorher) {
										bestIDs.add(rs234.getInt("bestellId"));
									}
								} else {
									bestIDs.add(rs234.getInt("bestellId"));
								}

							}
							rs234.close();
						} catch (SQLException e4) {
							e4.printStackTrace();
						}
					}

					geliefertAnzahl[j] = pat[i].getValueAt(j, 15).toString();

					if (!liefern[i][1][j].toString().equals("-1")) {
						double s = 0;
						try {
							s = Double.parseDouble(geliefertAnzahl[j]);
						} catch (NumberFormatException ne) {
							s = 0;
							JOptionPane
									.showMessageDialog(
											this,
											"Die Stückzahl muss eine positive Zahl sein!",
											"Fehlermeldung",
											JOptionPane.ERROR_MESSAGE);
							break;
						}

						// Anzahl gelieferte Menge überprüfen
						double geliefert = 0;
						double menge = 0;
						String queryMenge = "SELECT menge,geliefert from bestpos where id="
								+ liefern[i][1][j];
						ResultSet rsMenge = con.mysql_query(queryMenge);
						try {
							while (rsMenge.next()) {
								geliefert = rsMenge.getDouble("geliefert");
								menge = rsMenge.getDouble("menge");
							}
							rsMenge.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						if (s > (menge - geliefert)) {
							JOptionPane.showMessageDialog(this,
									"Die eingegebene Stückzahl ist zu groß! Es können nicht mehr als "
											+ (menge - geliefert)
											+ " Stück eingegeben werden!",
									"Fehlermeldung", JOptionPane.ERROR_MESSAGE);
							s = 0;
							break;
						} else if (s < 0) {
							JOptionPane.showMessageDialog(this,
									"Die Stückzahl muss positiv sein!",
									"Fehlermeldung", JOptionPane.ERROR_MESSAGE);
							s = 0;
							break;
						} else {

							try {
								String qrybp = "SELECT statusbez FROM bestpos WHERE id="
										+ liefern[i][1][j];
								ResultSet rsbp = con.mysql_query(qrybp);
								rsbp.next();
								int bezStatBp = rsbp.getInt("statusbez");

								String query = "UPDATE material SET stueck=(stueck+"
										+ s
										+ ") Where id=(SELECT bezeichnung FROM bestpos WHERE id="
										+ liefern[i][1][j] + ")";
								con.mysql_update(query);

								int statusbez = bezStatBp;
								int status = 0;
								if (((s + geliefert) == menge)
										&& (bezStatBp != 3) && (bezStatBp != 4)) {
									statusbez = 2;
									status = 3;
								} else if ((s + geliefert) == menge) {
									status = 3;
								} else if ((bezStatBp != 3) && (bezStatBp != 4)) {
									statusbez = 1;
									status = 4;
								} else {
									status = 4;
								}

								query = "UPDATE bestpos SET status=" + status
										+ ", statusbez=" + statusbez
										+ ", geliefert=geliefert+" + s
										+ ", lieferkommentar='" + komment[j]
										+ "' Where id=" + liefern[i][1][j];

								con.mysql_update(query);

								rsbp.close();

							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}// else
					}// if
				}// for j
			}// for i

			// ---------------------------------------------------------------------------------------------

			// !! Rest kommt nicht mehr

			for (int i = 0; i < liefern.length; i++) {

				komment = new String[liefern[i][2].length];

				for (int j = 0; j < liefern[i][2].length; j++) {

					komment[j] = pat[i].getValueAt(j, 16).toString();

					if (Integer.parseInt(liefern[i][2][j].toString()) != (-1)) {
						q = "Select bestellId from bestpos where id="
								+ liefern[i][2][j];

						ResultSet rs2 = con.mysql_query(q);
						try {
							rs2.next();
							{

								bestID = rs2.getInt("bestellId");
								if (bestIDs.size() != 0) {
									bestIDvorher = bestIDs.lastElement();
									if (bestID != bestIDvorher) {
										bestIDs.add(rs2.getInt("bestellId"));
									}
								} else {
									bestIDs.add(rs2.getInt("bestellId"));
								}

							}
							rs2.close();
						} catch (SQLException e4) {
							e4.printStackTrace();
						}
					}

					// geliefertAnzahl[j] = pat[i].getValueAt(j, 15).toString();

					if (!liefern[i][2][j].toString().equals("-1")) {

						try {
							String qStatusbez = "SELECT statusbez, bezahlt, geliefert FROM bestpos WHERE id="
									+ liefern[i][2][j];
							ResultSet rs7 = con.mysql_query(qStatusbez);
							rs7.next();
							int statusbez = rs7.getInt("statusbez");
							int geliefert = rs7.getInt("geliefert");
							double bezahlt = rs7.getDouble("bezahlt");

							if (statusbez == 1) {
								statusbez = 2;
							} else if (statusbez == 3) {
								if (geliefert == bezahlt)
									statusbez = 4;
							}

							String queryUp = "UPDATE bestpos SET statusbez="
									+ statusbez + ", lieferkommentar='"
									+ komment[j] + "', status=6 where id="
									+ liefern[i][2][j];

							con.mysql_update(queryUp);

							gesperrtMinus(Integer.parseInt(liefern[i][2][j]
									.toString()), geliefert);
							rs7.close();

						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}// if
				}// for j
			}// for i

			// ---------------------------------------------------------------------------------------------

			// !! nicht lieferbar
			for (int i = 0; i < liefern.length; i++) {

				komment = new String[liefern[i][3].length];

				for (int j = 0; j < liefern[i][3].length; j++) {

					komment[j] = pat[i].getValueAt(j, 16).toString();

					if (Integer.parseInt(liefern[i][3][j].toString()) != (-1)) {
						String qBestId = "Select bestellId from bestpos where id="
								+ liefern[i][3][j];

						ResultSet rs23 = con.mysql_query(qBestId);
						try {
							rs23.next();
							{
								bestID = rs23.getInt("bestellId");
								if (bestIDs.size() != 0) {
									bestIDvorher = bestIDs.lastElement();
									if (bestID != bestIDvorher) {
										bestIDs.add(rs23.getInt("bestellId"));
									}
								} else {
									bestIDs.add(rs23.getInt("bestellId"));
								}

							}
							rs23.close();
						} catch (SQLException e4) {
							e4.printStackTrace();
						}
						gesperrtMinus(Integer.parseInt(liefern[i][3][j]
								.toString()), 0);

						try {
							String qrybp = "SELECT statusbez FROM bestpos WHERE id="
									+ liefern[i][3][j];
							ResultSet rsbp = con.mysql_query(qrybp);
							rsbp.next();
							int bezStatBp = rsbp.getInt("statusbez");
							String query;

							if (bezStatBp != 3 && bezStatBp != 4) {
								query = "UPDATE bestpos SET status=5, statusbez=0, lieferkommentar='"
										+ komment[j]
										+ "' Where id="
										+ liefern[i][3][j];
							} else {
								query = "UPDATE bestpos SET status=5, lieferkommentar='"
										+ komment[j]
										+ "' Where id="
										+ liefern[i][3][j];
							}

							con.mysql_update(query);
							rsbp.close();
						} catch (Exception e3) {
							e3.printStackTrace();
						}
					}// end if
				}// for j
			}// for i

			for (int k = 0; k < bestIDs.size(); k++) {
				setBestellung(bestIDs.get(k));
			}

			dispose();
		}// if
	}// actionPerformed

	public void gesperrtMinus(int bestPosNr, int geliefert) {
		String queryOben;
		String kostenstelle = "";
		String tabelle;
		/** Stückpreis inkl. MWSt **/
		double preisInkl = 0;
		double preis = 0;
		/** Bestellmenge einer Bestellposition **/
		int menge = 0;
		String budget = "";
		DBConnection conb;
		Date datum = new Date();
		int jahr = datum.getYear() + 1900;

		/*
		 * if (jahr % 2 == 0) conb = new DBConnection("budget2", "budget2009",
		 * "cafelatte"); else
		 */
		conb = new DBConnection("budget2", "budget2009", "cafelatte");

		String query = "select * from banf b, banfpos bfp, bestpos bp, bestellung best where"
				+ " bp.id="
				+ bestPosNr
				+ " AND bp.bestellid=best.bestId AND bp.banfposnr=bfp.banfposnr AND "
				+ "bfp.banf=b.id;";

		ResultSet rs = con.mysql_query(query);

		try {
			while (rs.next()) {
				kostenstelle = rs.getString("b.kostenstelle");
				budget = rs.getString("best.budget");
				preisInkl = rs.getDouble("bp.preisInkl");
				menge = rs.getInt("bp.menge");
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// bei nicht lieferbar: geliefert = 0
		preis = preisInkl * (menge - geliefert);

		if (budget.equals("UT3")) {
			tabelle = "abteilungut3";
			queryOben = "UPDATE " + tabelle + " set gesperrt=gesperrt-" + preis
					+ " where name like '" + budget + "';";
			conb.mysql_update(queryOben);

		} else if (budget.equals("UT8")) {
			tabelle = "kostenstelleut8";
			queryOben = "UPDATE " + tabelle + " set gesperrt=gesperrt-" + preis
					+ " where name like '" + budget + "';";
			conb.mysql_update(queryOben);

		} else if (budget.equals("LMB1") || budget.equals("LMB2")) {
			tabelle = "lmb";
			queryOben = "UPDATE " + tabelle + " set gesperrt=gesperrt-" + preis
					+ " where name like '" + budget + "';";
			conb.mysql_update(queryOben);

		} else {
			tabelle = "sonderbudget";
		}

		query = "UPDATE " + tabelle + " set gesperrt=gesperrt-" + preis
				+ " where name like '" + kostenstelle + "';";

		conb.mysql_update(query);
	}

	public void setBestellung(int bestellID) {

		Vector<Integer> bestposIds = new Vector();
		int anzahl = 0;
		int nichtLieferbar = 0;
		int nichtOderGanz = 0;
		int falsch = 0;
		int statusBest = 0;
		int statusBezBest = 0;
		int nichtRichtig = 0;
		int geschickt = 0;
		int restKommtNicht = 0;
		int bezahlt = 0;
		String queryUpdate = "";

		try {
			// Status u Statusbez von Bestellung
			String q = "select status,statusbez from bestellung where bestId="
					+ bestellID;
			ResultSet r = con.mysql_query(q);

			while (r.next()) {
				statusBest = r.getInt("status");
				statusBezBest = r.getInt("statusbez");

			}

			r.close();

			// Anzahl der Bestellpositionen
			String query = "select count(id) as anz from bestpos where bestellId="
					+ bestellID;
			ResultSet rs = con.mysql_query(query);

			while (rs.next()) {
				anzahl = rs.getInt("anz");
			}

			rs.close();

			// IDs aller BestPos zu der Bestellung
			String query1 = "select id from bestpos where bestellId="
					+ bestellID;
			ResultSet rs1 = con.mysql_query(query1);

			while (rs1.next()) {
				bestposIds.add(rs1.getInt("id"));
			}

			rs1.close();

			// Anzahl nicht lieferbarer BestPos
			String query2 = "SELECT count(id) as anz FROM bestpos WHERE bestellid="
					+ bestellID + " AND status=5";
			ResultSet rs2 = con.mysql_query(query2);

			while (rs2.next()) {
				nichtLieferbar = rs2.getInt("anz");
			}

			rs2.close();

			// nicht lieferbare und komplett gelieferte BestPos und teilweise
			// geliefert(Rest kommt nicht mehr)
			String query3 = "SELECT count(id) as anz FROM bestpos WHERE bestellid="
					+ bestellID
					+ " AND (status=5 OR status=3 or (status=4 && statusbez=2))";
			ResultSet rs3 = con.mysql_query(query3);

			while (rs3.next()) {
				nichtOderGanz = rs3.getInt("anz");
			}

			rs3.close();

			// abweichend geliefert und nicht lieferbare BestPos
			String query5 = "SELECT count(id) as anz FROM bestpos WHERE bestellid="
					+ bestellID + " AND (status=4 OR status=5)";
			ResultSet rs5 = con.mysql_query(query5);

			while (rs5.next()) {
				falsch = rs5.getInt("anz");
			}

			rs5.close();

			// alle nicht "richtig geliefert"
			String query6 = "SELECT count(id) as anz FROM bestpos WHERE bestellid="
					+ bestellID + " AND status <>3";
			ResultSet rs6 = con.mysql_query(query6);

			while (rs6.next()) {
				nichtRichtig = rs6.getInt("anz");
			}

			rs6.close();

			// nicht abgeschickte oder abgeschickte BestPos --> alle noch nicht
			// gelieferten
			String query7 = "SELECT count(id) as anz FROM bestpos WHERE bestellid="
					+ bestellID + " AND (status=1 OR status=2)";
			ResultSet rs7 = con.mysql_query(query7);

			while (rs7.next()) {
				geschickt = rs7.getInt("anz");
			}

			rs7.close();

			// Bestellpositionen, bei denen "kein Rest" angehakt wurde
			// (BestPosStatus=6)
			String query8 = "SELECT count(id) as anz FROM bestpos WHERE bestellid="
					+ bestellID + " AND (status=6)";
			ResultSet rs8 = con.mysql_query(query8);

			while (rs8.next()) {
				restKommtNicht = rs8.getInt("anz");
			}

			rs8.close();

			// Bestellpositionen, die komplett bezahlt sind
			String query9 = "SELECT count(id) as anz FROM bestpos WHERE bestellid="
					+ bestellID + " AND (statusbez=4)";
			ResultSet rs9 = con.mysql_query(query9);

			while (rs9.next()) {
				bezahlt = rs9.getInt("anz");
			}

			rs9.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// richtig = nichtOderGanz - nichtLieferbar;

		if ((nichtLieferbar != 0) || (restKommtNicht != 0))// mindestens eine
		// nicht lieferbar
		{
			if (nichtLieferbar == anzahl)// alle nicht lieferbar
			{
				queryUpdate = "UPDATE bestellung set status=6, statusbez=0 where bestId="
						+ bestellID;

			} else if ((anzahl - nichtOderGanz - restKommtNicht) == 0)// nur
			// nicht
			// lieferbar
			// und richtig
			// gelieferte
			{
				if ((statusBezBest != 3) && (statusBezBest != 4)) {
					queryUpdate = "UPDATE bestellung set status=6, statusbez=2 where bestId="
							+ bestellID;

				} else if ((statusBezBest == 0) || (statusBezBest == 4)) {
					queryUpdate = "UPDATE bestellung set status=6, statusbez=4 where bestId="
							+ bestellID;
					aendereStatusRechnung(bestID);

				} else if ((anzahl - nichtLieferbar - bezahlt) == 0) {
					queryUpdate = "UPDATE bestellung set status=6, statusbez=4 where bestId="
							+ bestellID;
				} else {
					queryUpdate = "UPDATE bestellung set status=6 where bestId="
							+ bestellID;
				}

			} else {
				if ((statusBezBest != 3) && (statusBezBest != 4)) {
					queryUpdate = "UPDATE bestellung set status=4, statusbez=1 where bestId="
							+ bestellID;

				} else {
					queryUpdate = "UPDATE bestellung set status=4 where bestId="
							+ bestellID;

				}

			}

		} else // keine nicht lieferbar
		{
			if (nichtOderGanz == anzahl)// alle richtig geliefert
			{
				if ((statusBezBest != 3) && (statusBezBest != 4)) {
					queryUpdate = "UPDATE bestellung set status=7, statusbez=2 where bestId="
							+ bestellID;
				} else {
					queryUpdate = "UPDATE bestellung set status=7 where bestId="
							+ bestellID;
				}
			} else if ((falsch != 0)
					|| ((nichtOderGanz != 0) && (geschickt != 0)))// teilweise
			// richtig
			// geliefert
			{
				if ((statusBezBest != 3) && (statusBezBest != 4)) {
					queryUpdate = "UPDATE bestellung set status=5, statusbez=1 where bestId="
							+ bestellID;
				} else {
					queryUpdate = "UPDATE bestellung set status=5 where bestId="
							+ bestellID;
				}
			} else {
				System.out.println("ACHTUNG: nichts trifft zu!!");
			}
		}

		con.mysql_update(queryUpdate);

	}

	public void aendereStatusRechnung(int bestID) {
		String wnummer = "";
		int anzRechnung = 0;

		String query = "select wnummer from bestellung where bestId=" + bestID;
		ResultSet rs = con.mysql_query(query);

		try {
			while (rs.next()) {
				wnummer = rs.getString("wnummer");
			}

			query = "select count(*) from rechnung where wNummer like '"
					+ wnummer + "'";
			ResultSet rs2 = conB.mysql_query(query);

			while (rs2.next()) {
				anzRechnung = rs2.getInt("count(*)");
			}

			if (anzRechnung == 1) {
				query = "update rechnung set rechnungsstatus=2 where wNummer like '"
						+ wnummer + "'";
				conB.mysql_update(query);
			}

			rs.close();
			rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
