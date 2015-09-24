package Logistik;

import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.Date;
import java.io.*;
import javax.swing.filechooser.FileFilter;

/**
 * neues Fenster für Detialansicht bei Bestellungen, welche die WL abschicken
 * kann verwendetes Layout: BanfLayout Anzeige aller ausgewählten Bestellungen
 * in Detailansicht Jede Bestellung kann bestellt -> an Firma senden Änderungen
 * in Datenbank Mail an Firma, Lager wird gesendet PDF wird erstellt und kann
 * ausgedruckt werden Betrag wird im Budgetprogramm gesperrt
 */

public class AnzBestDetail extends JFrame implements ActionListener {
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

	/**
	 * Button zum Bestellen
	 */
	public JButton next = new JButton("abschicken");

	/**
	 * Button zum Erstellen eines PDF
	 */
	public JButton print = new JButton("PDF");
	/**
	 * Datenbankverbindung Logistik
	 */
	private DBConnection con;

	/**
	 * Datenbankverbindung Budget
	 */
	private DBConnection conb;

	/**
	 * Checkobox-Array für die Auswahl von Bestellungen
	 */
	public JCheckBox[] bestellen;

	private Container c;
	public int l = 0;
	public String[] bestellenID;
	public String wnr = "";

	private Date datum;
	private int jahr;

	public AnzBestDetail(UserImport user, Object[] hakDet) {

		c = getContentPane();

		datum = new Date();
		jahr = datum.getYear() + 1900;

		/*
		 * if (jahr % 2 == 0) conb = new DBConnection("budget4", "budget2009",
		 * "cafelatte"); else
		 */
		conb = new DBConnection("budget2", "budget2009", "cafelatte");

		con = user.getConnection();
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(getToolkit().getScreenSize().width, 700);

		Object[] hak = new Object[hakDet.length];
		for (int x = 0; x < hakDet.length; x++) {
			hak[x] = hakDet[x];
		}

		l = hak.length;
		c.setLayout(new BorderLayout());
		eineBest = new JPanel[hak.length];
		einKopf = new JPanel[hak.length];
		einPos = new JPanel[hak.length];
		JPanel all = new JPanel(new GridLayout(hak.length, 1));
		JLabel[] linie = new JLabel[hak.length];

		for (int y = 0; y < hak.length; y++) {
			linie[y] = new JLabel(
					"<html><div align=\"center\"><hr size=\"2\" width=\"3000\"> </div></html>\n");
			linie[y].setFont(new Font("Arial", Font.BOLD, 20));
			eineBest[y] = new JPanel();
			einKopf[y] = new JPanel();
			einPos[y] = new JPanel();
			eineBest[y] = new JPanel();
			eineBest[y].setLayout(new BorderLayout());
			eineBest[y].add(linie[y], BorderLayout.SOUTH);
			einKopf[y].setLayout(new LayoutBanf(7)); // kopf
			einPos[y].setLayout(new LayoutBanf(11)); // PosDaten
		}

		bestellen = new JCheckBox[hak.length];
		bestellenID = new String[hak.length];

		try {
			for (int i = 0; i < hak.length; i++) {
				String query1 = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer,budget FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND bestId="
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
				JLabel bestellenT = new JLabel("bestellen/pdf");
				bestellenT.setFont(new Font("Arial", Font.BOLD, 14));
				bestellen[i] = new JCheckBox();
				bestellenID[i] = rs1.getString("bestID");

				String s = rs1.getString("status");

				einKopf[i].add(bestidT, "60");
				einKopf[i].add(datumT, "200");
				einKopf[i].add(lehrerT, "135");
				einKopf[i].add(firmaT, "21%");
				einKopf[i].add(statusT, "132");
				einKopf[i].add(statusbT, "120");
				einKopf[i].add(bestellenT, "100");
				einKopf[i].add(bestid, "");
				einKopf[i].add(datum, "");
				einKopf[i].add(lehrer, "");
				einKopf[i].add(firma, "");
				einKopf[i].add(status, "");
				einKopf[i].add(statusb, "");
				einKopf[i].add(bestellen[i], "");

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
					status.setBackground(Color.GREEN);
				}
				if (s.equals("5") || s.equals("teilw richtig geliefert")) {
					status.setText("teilw richtig geliefert");
					status.setBackground(Color.YELLOW);
				}
				if (s.equals("6") || s.equals("falsch geliefert")) {
					status.setText("komplett - Teile nicht lieferbar");
					status.setBackground(Color.YELLOW);
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

				if (s.equals("15") || s.equals("gelöscht")) {
					statusb.setText("gelöscht");
					statusb.setBackground(new Color(151, 217, 236));
				}

				eineBest[i].add(einKopf[i], BorderLayout.NORTH);

				String query2 = "SELECT m.bezeichnung,bep.menge,bep.preisExkl,bep.mwst,bep.preisInkl,bep.preisGesamt,bep.einheit,bep.lehrer,bep.id,bep.status,bep.statusbez FROM bestpos bep,material m WHERE bep.bestellId="
						+ hak[i] + " AND bep.bezeichnung=m.id ORDER BY id ASC"; // lehrername!?!
				ResultSet rs2 = con.mysql_query(query2);

				JLabel bestposT = new JLabel("ID");
				bestposT.setFont(new Font("Arial", Font.BOLD, 14));
				JLabel bezT = new JLabel("Bezeichnung");
				bezT.setFont(new Font("Arial", Font.BOLD, 14));
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

				einPos[i].add(bestposT, "35");
				einPos[i].add(bezT, "20%");
				einPos[i].add(mengeT, "60");
				einPos[i].add(preiseT, "75");
				einPos[i].add(mwstT, "47");
				einPos[i].add(preisiT, "75");
				einPos[i].add(preisgT, "75");
				einPos[i].add(einheitT, "40");
				einPos[i].add(lehrerpT, "135");
				einPos[i].add(statuspT, "110");
				einPos[i].add(statuspbT, "110");

				while (rs2.next()) {

					JLabel bestpos = new JLabel(rs2.getString("id"));
					bestpos.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel bez = new JLabel(rs2.getString("m.bezeichnung"));
					bez.setFont(new Font("Arial", Font.PLAIN, 13));
					JLabel menge = new JLabel(rs2.getString("menge"));
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

					einPos[i].add(bestpos, "");
					einPos[i].add(bez, "");
					einPos[i].add(menge, "");
					einPos[i].add(preise, "");
					einPos[i].add(mwst, "");
					einPos[i].add(preisi, "");
					einPos[i].add(preisg, "");
					einPos[i].add(einheit, "");
					einPos[i].add(lehrerp, "");
					einPos[i].add(statusp, "");
					einPos[i].add(statuspb, "");

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
				}

				rs1.close();
				rs2.close();

				eineBest[i].add(einPos[i], BorderLayout.CENTER);
				all.add(eineBest[i]);

			}// für jede Bestellung

		} catch (Exception ex) {
			ex.getMessage();
		}

		setVisible(true);
		JPanel p = new JPanel(new GridLayout(1, 2));
		p.add(next);
		p.add(print);
		c.add(p, BorderLayout.SOUTH);

		JScrollPane sc;
		sc = new JScrollPane(all);

		c.add(sc, BorderLayout.CENTER);

		next.addActionListener(this);
		print.addActionListener(this);

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
		if (e.getSource() == next) {
			for (int i = 0; i < l; i++) {
				if (bestellen[i].isSelected()) {

					// in DB ändern
					try {
						String sql = "UPDATE bestellung SET status=3 WHERE bestId = "
								+ bestellenID[i];
						con.mysql_update(sql);
						String sql2 = "UPDATE bestpos SET status=2 WHERE bestellId = "
								+ bestellenID[i];
						con.mysql_update(sql2);

					} catch (Exception e3) {
						e3.getMessage();
						e3.printStackTrace();
					}

					// ERSTELLLEN + DRUCKEN DES PDFs
					drucken(bestellenID[i]);

					// MAIL SENDEN

					// String firmaMail = "juergen.zach@stud.wi.htl-hl.ac.at";
					// // mail
					// der
					// firma
					// (select);
					// mail
					// an
					// Lager
					//String betreff = "Bestellung der HTL Hollabrunn";
					//String text = "Dies ist eine Testbestellung";
					//File f = new File(FTP.temp() + wnr + ".pdf");

					/*
					 * IMAP.sendMail(user.getMail(), new String[] { firmaMail },
					 * new String[] { "christian.starek@stud.wi.htl-hl.ac.at",
					 * "jakob.muellebner@stud.wi.htl-hl.ac.at" }, betreff, text,
					 * f);
					 */

					// BETRAG SPERREN
					try {
						String sqlKST = "SELECT banf.kostenstelle,bestpos.preisGesamt FROM bestpos, banfpos, banf WHERE bestpos.bestellId ="
								+ bestellenID[i]
								+ " AND bestpos.banfposnr = banfpos.banfposnr AND banfpos.banf = banf.id";
						ResultSet rsKST = con.mysql_query(sqlKST);
						while (rsKST.next()) {
							String kst = rsKST.getString("banf.kostenstelle");
							String pGes = rsKST
									.getString("bestpos.preisGesamt");
							double p = Double.parseDouble(pGes);

							String sqlBUD = "SELECT budget FROM bestellung WHERE bestId="
									+ bestellenID[i];
							ResultSet rsBUD = con.mysql_query(sqlBUD);
							rsBUD.next();
							String bud = rsBUD.getString("budget");

							if ((isProjekt(kst) && ((bud.equals("UT8"))
									|| (bud.equals("LMB1"))
									|| (bud.equals("LMB2")) || (bud
									.equals("UT3"))))) {
								betragSperren1(bud, kst, p);
							} else if ((bud.equals("UT8"))
									|| (bud.equals("LMB1"))
									|| (bud.equals("LMB2"))
									|| (bud.equals("UT3"))) {
								betragSperren2(bud, kst, p);
							} else {
								betragSperrenSonder(bud, p);
							}
							betragSperrenHauptbudget(bud, p);

							rsBUD.close();

						}// while

						rsKST.close();

					} catch (Exception ex) {
						ex.getMessage();
					}

				}// bestellen

			}// for
			setVisible(false);
		}
		if (e.getSource() == print) {
			for (int i = 0; i < l; i++) {
				if (bestellen[i].isSelected()) {
					JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							return f.getName().toLowerCase().endsWith(".pdf");
						}

						public String getDescription() {
							return "PDFs (*.pdf)";
						}
					});
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fc.setDialogTitle("PDF speichern unter");
					fc.setVisible(true);
					int returnVal = fc.showSaveDialog(this);
					File f;
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						f = fc.getSelectedFile();
						if (!f.getName().toLowerCase().endsWith(".pdf")) {
							f = new File(f.getAbsolutePath() + ".pdf");
						}
						if (f.exists()) {
							f.delete();
						}
						drucken(bestellenID[i], f.getAbsolutePath());
					} else {
						System.out.println("Speichern fehlgeschlagen");
					}
				}
			}
		}

	}// actionPerformed

	public void drucken(String bestid) {
		drucken(bestid, null);
	}

	public void drucken(String bestid, String fName) {
		System.out.println("AnBestDetail.drucken");
		
		try {
			String sqlBest = "SELECT * FROM bestellung,firma WHERE bestId="
					+ bestid + " AND bestellung.firma=firma.id";
			ResultSet rsBest = con.mysql_query(sqlBest);
			rsBest.next();
			wnr = rsBest.getString("wnummer");
			String budget = rsBest.getString("budget");
			boolean elternverein = false;

			if (budget.equals("LMB1") || budget.equals("LMB2"))
				elternverein = true;

			String[] auftrag = rsBest.getString("kommentar").split(";");
			String aS;
			String aNr;
			String lBed;
			String lFri;
			String vArt;
			String bem;

			if (auftrag.length < 1) {
				aNr = "-";
				aS = "-";
				lBed = "-";
				lFri = "-";
				vArt = "-";
				bem = "-";
			} else if (auftrag.length < 2) {
				aNr = auftrag[0];
				aS = "-";
				lBed = "-";
				lFri = "-";
				vArt = "-";
				bem = "-";
			} else if (auftrag.length < 3) {
				aNr = auftrag[0];
				aS = auftrag[1];
				lBed = "-";
				lFri = "-";
				vArt = "-";
				bem = "-";
			} else if (auftrag.length < 4) {
				aNr = auftrag[0];
				aS = auftrag[1];
				lBed = auftrag[2];
				lFri = "-";
				vArt = "-";
				bem = "-";
			} else if (auftrag.length < 5) {
				aNr = auftrag[0];
				aS = auftrag[1];
				lBed = auftrag[2];
				lFri = auftrag[3];
				vArt = "-";
				bem = "-";
			} else if (auftrag.length < 6) {
				aNr = auftrag[0];
				aS = auftrag[1];
				lBed = auftrag[2];
				lFri = auftrag[3];
				vArt = auftrag[4];
				bem = "-";
			} else {
				aNr = auftrag[0];
				aS = auftrag[1];
				lBed = auftrag[2];
				lFri = auftrag[3];
				vArt = auftrag[4];
				bem = auftrag[5];
			}

			java.util.Date j = new java.util.Date(Long.parseLong(rsBest
					.getString("datum")) * 1000);
			String bestDat = "" + (j.getDate()) + "." + (j.getMonth() + 1)
					+ "." + (j.getYear() + 1900);

			PDFMaker d;
			if (fName == null) {
				d = new PDFMaker(FTP.temp() + rsBest.getString("wnummer")
						+ ".pdf", budget);
			} else {
				d = new PDFMaker(fName, budget);
			}
			d.setAddress(rsBest.getString("anschriftRechnung"));
			d.addFirma(rsBest.getString("firmenname"), rsBest
					.getString("strasse"), rsBest.getString("plz") + " "
					+ rsBest.getString("ort"));
			d.addAuftrag(rsBest.getString("wnummer"), bestDat, rsBest
					.getString("kundennr"), rsBest.getString("uid"), rsBest
					.getString("kreditorennummer"), rsBest
					.getString("einkaeufergruppe"));
			d.addAngebot(rsBest.getString("wnummer"), aNr, aS);

			String sqlBestpos = "SELECT bestpos.*,material.bezeichnung,firma_material.* FROM bestpos,material,firma_material WHERE bestpos.bestellId="
					+ bestid
					+ " AND bestpos.bezeichnung=material.id AND bestpos.bezeichnung=firma_material.material AND firma_material.firma="
					+ rsBest.getString("firma.id");
			ResultSet rsBestpos = con.mysql_query(sqlBestpos);
			int i = 1;
			while (rsBestpos.next()) {
				i++;
				d.addRow(rsBestpos.getString("material.bezeichnung"), rsBestpos
						.getString("firma_material.artNr"), rsBestpos
						.getString("bestpos.menge"), rsBestpos
						.getString("firma_material.einheit"), rsBestpos
						.getString("bestpos.preisExkl"), rsBestpos
						.getString("bestpos.mwst"), rsBestpos
						.getString("bestpos.preisGesamt"));

			}
			String sqlU = "SELECT name FROM ldap_user WHERE cn=\'"
					+ rsBest.getString("antragsteller") + "\'";
			ResultSet rsU = con.mysql_query(sqlU);
			rsU.next();

			d.addFooter(lBed, lFri, vArt, "-", rsU.getString("name"), rsBest
					.getString("anschriftRechnung"), bem, elternverein);
			d.close();

			File f = new File(FTP.temp() + rsBest.getString("wnummer") + ".pdf");
			try {System.out.println("AnzBestDetail.drucken File FTP.upload");
				FTP.upload(f, "BESTELLUNG");
			} catch (Exception e) {System.out.println("AnzBestDetail.drucken File FTP.upload - catch");
				e.getMessage();
				e.printStackTrace();
			}

			if (fName == null) {
				Logistik.openPDF(FTP.temp() + rsBest.getString("wnummer")
						+ ".pdf");
				System.out.println("Logistik.openPDF "+FTP.temp() + rsBest.getString("wnummer")
						+ ".pdf");
			} else {
				Logistik.openPDF(fName);
				System.out.println("Logistik.openPDF "+fName);
			}

			rsBest.close();
			rsBestpos.close();
			rsU.close();

		} catch (Exception ex) {
			ex.getMessage();
		}

		// BEMERKUNG HINZUFÜGEN
	}

	/**
	 * Wenn die Kostenstelle ein Projekt ist und kein Sonderbudget belastet
	 * wird, wird in dieser Methode der Betrag auf der gewaehlten Kostenstelle
	 * gesperrt
	 * 
	 * @param budget
	 *            Welches Budget wird belastet
	 * @param kst
	 *            Name der Kostenstelle, die belastet wird
	 * @param betrag
	 *            Der Betrag, der gesperrt wird
	 */
	public void betragSperren1(String budget, String kst, double betrag) {

		try {

			String sql = "";
			String sql2 = "";
			String abteilung = "";
			int hauptnr = 1;
			boolean exist;
			int nummer = 1;

			String sql3 = "SELECT * from projekt where name like '" + kst
					+ "';";
			ResultSet rs = conb.mysql_query(sql3);
			while (rs.next()) {
				abteilung = rs.getString("abteilung");
				nummer = rs.getInt("NummerSelbst");
			}

			rs.close();

			if (budget.equals("UT8")) {

				// pruefeBetrag("kostenstelleut8", kst, betrag);

				if (abteilung.equals("MI")) {
					hauptnr = 11;
				}
				if (abteilung.equals("WI")) {
					hauptnr = 12;
				}
				if (abteilung.equals("ET")) {
					hauptnr = 13;
				}
				if (abteilung.equals("EL")) {
					hauptnr = 14;
				}

				exist = existiertProjekt(kst, budget);
				if (!exist) {

					pruefeBetragProjekt(hauptnr, kst, betrag, false);

					sql2 = "INSERT INTO kostenstelleut8(nummerSelbst,hauptnummer,projektkennung,name,gesperrt) VALUES("
							+ nummer
							+ ","
							+ hauptnr
							+ ", 1, '"
							+ kst
							+ "',"
							+ betrag + ");";

					conb.mysql_update(sql2);
				} else {

					pruefeBetragProjekt(hauptnr, kst, betrag, true);
					sql = "UPDATE kostenstelleut8 set gesperrt=gesperrt+"
							+ betrag + " WHERE name like '" + kst + "';";
					conb.mysql_update(sql);
				}
			} else if (budget.equals("UT3")) {

				exist = existiertProjekt(kst, budget);
				if (!exist) {

					pruefeBetragProjekt(3, kst, betrag, false);

					sql2 = "INSERT INTO abteilungut3(name,gesperrt,projektkennung, festgeplant) VALUES('"
							+ kst + "'," + betrag + ",1," + nummer + ");";
					conb.mysql_update(sql2);
				} else {

					pruefeBetragProjekt(3, kst, betrag, true);

					sql = "UPDATE abteilungut3 set gesperrt=gesperrt+" + betrag
							+ " WHERE name like '" + kst + "';";
					System.out.println("QUERY " + sql);
					conb.mysql_update(sql);
				}
			}

			else if (budget.equals("LMB1")) {
				exist = existiertProjekt(kst, budget);
				if (!exist) {

					pruefeBetragProjekt(1, kst, betrag, false);

					sql2 = "INSERT INTO lmb(name,gesperrt,projektkennung,kennung,festgeplant) VALUES('"
							+ kst + "'," + betrag + ",1,1," + nummer + ");";
					conb.mysql_update(sql2);
				} else {

					pruefeBetragProjekt(1, kst, betrag, true);

					sql = "UPDATE lmb set gesperrt=gesperrt+" + betrag
							+ " WHERE name like '" + kst + "' AND kennung=1;";
					conb.mysql_update(sql);
				}
			}

			else if (budget.equals("LMB2")) {
				exist = existiertProjekt(kst, budget);
				if (!exist) {

					pruefeBetragProjekt(2, kst, betrag, false);

					sql2 = "INSERT INTO lmb(name,gesperrt,projektkennung,kennung,festgeplant) VALUES('"
							+ kst + "'," + betrag + ",1,2," + nummer + ");";
					conb.mysql_update(sql2);
				} else {

					pruefeBetragProjekt(2, kst, betrag, true);

					sql = "UPDATE lmb set gesperrt=gesperrt+" + betrag
							+ " WHERE name like '" + kst + "' AND kennung=2;";
					conb.mysql_update(sql);
				}
			}

		} catch (Exception pe) {
			pe.printStackTrace();
		}
	}

	/**
	 * Wenn die Kostenstelle kein Projekt ist und kein Sonderbudget belastet
	 * wird, wird in dieser Methode der Betrag auf der gewaehlten Kostenstelle
	 * gesperrt
	 * 
	 * @param budget
	 *            Welches Budget wird belastet
	 * @param kst
	 *            Name der Kostenstelle, die belastet wird
	 * @param betrag
	 *            Der Betrag, der gesperrt wird
	 */
	public void betragSperren2(String budget, String kst, double betrag) {
		try {

			String sql = "";

			if (budget.equals("UT8")) {
				pruefeBetrag("kostenstelleut8", kst, betrag, 0);

				sql = "UPDATE kostenstelleut8 set gesperrt=gesperrt+" + betrag
						+ " WHERE name like '" + kst + "';";
			} else if (budget.equals("UT3")) {
				pruefeBetrag("abteilungut3", kst, betrag, 0);

				sql = "UPDATE abteilungut3 set gesperrt=gesperrt+" + betrag
						+ " WHERE name like '" + kst + "';";
			} else if (budget.equals("LMB1")) {
				pruefeBetrag("lmb", kst, betrag, 1);

				sql = "UPDATE lmb set gesperrt=gesperrt+" + betrag
						+ " WHERE name like '" + kst + "' AND kennung=1;";
			} else if (budget.equals("LMB2")) {
				pruefeBetrag("lmb", kst, betrag, 2);

				sql = "UPDATE lmb set gesperrt=gesperrt+" + betrag
						+ " WHERE name like '" + kst + "' AND kennung=2;";
			}

			conb.mysql_update(sql);

		} catch (Exception pe) {
			pe.getMessage();
		}

	}

	/**
	 * Wenn eine Bestellung mit einem Sonderbudget bezahlt wird, wird in dieser
	 * Methode der Betrag gesperrt
	 * 
	 * @param budget
	 *            Der Name des Sonderbudgets, mit dem bezahlt wird
	 * @param betrag
	 *            Der Betrag, der gesperrt wird
	 */
	public void betragSperrenSonder(String budget, double betrag) {
		try {
			pruefeBetrag("sonderbudget", budget, betrag, 0);

			String sql = "UPDATE sonderbudget SET gesperrt=gesperrt+" + betrag
					+ " WHERE name like '" + budget + "';";
			conb.mysql_update(sql);

		} catch (Exception ex) {
			ex.getMessage();
		}

	}

	/**
	 * Es wird ueberprueft, ob die Kostenstelle, die belastet wird, ein Projekt
	 * ist, oder nicht
	 * 
	 * @param kst
	 *            Der Name der Kostenstelle
	 * @return true: wenn die Kostenstelle ein Projekt ist; false: wenn die
	 *         Kostenstelle kein Projekt ist
	 */
	public boolean isProjekt(String kst) {
		String sql = "SELECT * from projekt where name like '" + kst + "';";
		ResultSet rs = conb.mysql_query(sql);
		try {
			while (rs.next()) {
				return true;
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Es wird ueberprueft, ob das Projekt, das belastet wird, schon in dem
	 * gewaehlten Budget angelegt wurde
	 * 
	 * @param kst
	 *            Der Name des Projektes
	 * @param budget
	 *            in welchem Budget wird ueberprueft
	 * @return true: wenn das Projekt schon existiert; false: wenn das Projekt
	 *         noch nicht existiert
	 */
	public boolean existiertProjekt(String kst, String budget) {
		String sql = "";

		if (budget.equals("UT8")) {
			sql = "select * from  kostenstelleut8 where name like '" + kst
					+ "';";
		} else if (budget.equals("UT3")) {
			sql = "select * from  abteilungut3 where name like '" + kst + "';";
		} else if (budget.equals("LMB1")) {
			sql = "select * from  lmb where name like '" + kst
					+ "' and kennung=1";
		} else if (budget.equals("LMB2")) {
			sql = "select * from  lmb where name like '" + kst
					+ "' and kennung=2";
		}

		ResultSet rs = conb.mysql_query(sql);
		try {
			while (rs.next()) {
				return true;
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Der Betrag wird in der ersten Zeile (im Hauptbudget) gesperrt;
	 * 
	 * @param budget
	 *            in welchem Budget wird der Betrag gesperrt
	 * @param betrag
	 *            der Betrag, der gesperrt wird
	 */
	public void betragSperrenHauptbudget(String budget, double betrag) {
		String sql = "";

		if (budget.equals("UT3")) {
			sql = "UPDATE abteilungut3 set gesperrt=gesperrt+" + betrag
					+ " WHERE nummer=1";
			conb.mysql_update(sql);
		} else if (budget.equals("LMB1")) {
			sql = "UPDATE lmb set gesperrt=gesperrt+" + betrag
					+ " WHERE nummer=1 AND kennung=1;";
			conb.mysql_update(sql);
		} else if (budget.equals("LMB2")) {
			sql = "UPDATE lmb set gesperrt=gesperrt+" + betrag
					+ " WHERE nummer=2 AND kennung=2;";
			conb.mysql_update(sql);
		}
	}

	/**
	 * Wird aufgerufen, wenn die Kostenstelle kein Projekt ist; Es wird
	 * ueberprueft, ob der gesperrte Betrag groeßer als der verfuegbare ist
	 * 
	 * @param budget
	 *            aus welcher Tabelle sollen die Daten ausgelesen werden
	 * @param kst
	 *            die Kostenstelle, an der der Betrag gesperrt wird
	 * @param betrag
	 *            der Betrag, der gesperrt wird
	 * @param zahl
	 *            0=pruefe die Kst direkt (bei UT8,Sonderbudget,UT3), 1=pruefe
	 *            Hauptbudget (bei LMB1), 2=pruefe Hauptbudget (bei LMB2)
	 */
	public void pruefeBetrag(String budget, String kst, double betrag, int zahl) {

		double verfuegbar = 0;
		double geplant = 0;
		double gesperrt = 0;
		double ausgegeben = 0;

		String sql = "";

		if (zahl == 0) {
			sql = "select * from " + budget + " where name like '" + kst + "';";
		} else if (zahl == 1) {
			sql = "select * from " + budget
					+ " where kennung=1 AND name like '" + kst + "';";
		} else if (zahl == 2) {
			sql = "select * from " + budget
					+ " where kennung=2 AND name like '" + kst + "';";
		}

		ResultSet rs = conb.mysql_query(sql);
		try {
			while (rs.next()) {
				geplant = rs.getDouble("geplant");
				gesperrt = rs.getDouble("gesperrt");
				ausgegeben = rs.getDouble("ausgegeben");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (zahl == 0) {
			sql = "select * from " + budget + " where name like '" + kst + "';";
		} else if (zahl == 1) {
			sql = "select * from " + budget + " where nummer=1;";
		} else if (zahl == 2) {
			sql = "select * from " + budget + " where nummer=2;";
		}

		verfuegbar = geplant - gesperrt - ausgegeben;

		if (verfuegbar < betrag) {
			JOptionPane
					.showMessageDialog(
							this,
							"WARNUNG! Der Betrag, den Sie gesperrt haben, ist hoeher als der Betrag, der Ihnen zur Verfuegung steht!");
		}

	}

	/**
	 * Wird aufgerufen von der Methode betragSperren1, also wenn die
	 * Kostenstelle ein Projekt ist; Es wird ueberprueft, ob der gesperrte
	 * Betrag groeßer als der verfuegbare ist
	 * 
	 * @param nummer
	 *            gibt an, welches budget gewaehlt wurde: 1=LMB1, 2=LMB2, 3=UT3,
	 *            sonstige: Hauptnummer der Hauptkostenstelle, die zur
	 *            UT8-Kostenstelle gehoert
	 * @param kst
	 *            die Kostenstelle, an der der Betrag gesperrt werden soll
	 * @param betrag
	 *            der Betrag der gesperrt wird
	 * @param exist
	 *            true=das Projekt existiert bereits als Kostenstelle in diesem
	 *            Budget; false=es existiert noch nicht in diesem Budget
	 */
	public void pruefeBetragProjekt(int nummer, String kst, double betrag,
			boolean exist) {

		String sql = "";
		double verfuegbar = 0;
		double geplant = 0;
		double gesperrt = 0;
		double ausgegeben = 0;

		try {

			if (nummer == 1) {
				sql = "select * from lmb where nummer=1;";
			} else if (nummer == 2) {
				sql = "select * from lmb where nummer=2;";
			} else if (nummer == 3) {
				sql = "select * from abteilungut3 where nummer=1;";
			} else {
				sql = "select * from hauptkostenstelleut8 where nummer="
						+ nummer;
			}

			ResultSet rs = conb.mysql_query(sql);

			while (rs.next()) {
				geplant = rs.getDouble("geplant");

				if (!exist && (nummer == 1) && (nummer == 2) && (nummer == 3)) {
					ausgegeben = rs.getDouble("ausgegeben");
					gesperrt = rs.getDouble("gesperrt");
				}
			}

			if ((!exist) && (nummer != 1) && (nummer != 2) && (nummer != 3)) {
				String sql2 = "select sum(gesperrt),sum(ausgegeben) from kostenstelleut8 where hauptnummer="
						+ nummer;

				ResultSet rs2 = conb.mysql_query(sql2);

				while (rs.next()) {
					ausgegeben = rs.getDouble("sum(ausgegeben)");
					gesperrt = rs.getDouble("sum(gesperrt)");
				}
			}

			if (exist) {
				if (nummer == 1) {
					sql = "select * from lmb where name like '" + kst
							+ "' AND kennung=1;";
				} else if (nummer == 2) {
					sql = "select * from lmb where name like '" + kst
							+ "' AND kennung=2;";
				} else if (nummer == 3) {
					sql = "select * from abteilungut3 where name like '" + kst
							+ "'";
				} else {
					sql = "select * from kostenstelleut8 where name like '"
							+ kst + "'";
				}

				rs = conb.mysql_query(sql);
				while (rs.next()) {
					ausgegeben = rs.getDouble("ausgegeben");
					gesperrt = rs.getDouble("gesperrt");
				}
			}

			verfuegbar = geplant - ausgegeben - gesperrt;

			if (verfuegbar < betrag) {
				JOptionPane
						.showMessageDialog(
								this,
								"WARNUNG! Der Betrag, den Sie sperren, ist hoeher als der Betrag, der Ihnen zur Verfuegung steht!");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}