package Logistik;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

import javax.swing.*;

/**
 * Hier findet der Bestellablauf statt mit CardLayout wird der Prozess
 * abgewickelt es werden angehakte Datensätze übernommen und in neue Klassen
 * übernommen je nach Fortschritt wird die zuständige Klasse aufgerufen
 */
public class NewBestellung extends LayoutMainPanel implements ActionListener {
	/**JOptionPane.showMessageDialog(null,
							"Sie müssen etwas anhaken!");
	 * Laoyut für den Bestellablauf
	 */
	public CardLayout layout;

	/**
	 * Panel, in dem der Ablauf stattfindet
	 */
	public JPanel centerp;

	/**
	 * Zähler für den Prozessablauf
	 */
	public int z = 1;

	/**
	 * Button zum Durchklicken des Prozesses
	 */
	public JButton next = new JButton("weiter");

	/**
	 * Button zum Zuürückspringen zur Startseite
	 */
	public JButton begin = new JButton("zum Start");

	/**
	 * speichert angehakte Werte
	 */
	public Object[] hakBanf;

	/**
	 * speichert angehakte Werte
	 */
	public Object[] hakBanfCheck;

	/**
	 * speichert angehakte Werte
	 */
	public Object[] hakDelPos;

	/**
	 * speichert angehakte Werte
	 */
	public Object[] hakBestPos;

	public AnzBanf a = new AnzBanf(con);
	public AnzBanfPos b;
	public DelPos c1;
	public DelCancel c2 = new DelCancel();
	public AnzBest d;
	public BestOkSek f;
	private int zahl = 0;

	public NewBestellung(UserImport user) {
		super(user);
		setLayoutM(new BorderLayout());

		layout = new CardLayout();
		centerp = new JPanel(layout);

		centerp.add(a, "a");
		centerp.add(c2, "c2");

		layout.show(centerp, "a");
		revalidate();
		repaint();

		addM(centerp, BorderLayout.CENTER);
		JPanel bts = new JPanel();
		bts.setLayout(new GridLayout(1, 2));
		bts.add(begin);
		bts.add(next);
		addM(bts, BorderLayout.SOUTH);
		next.addActionListener(this);
		begin.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == begin) {
			a = new AnzBanf(con);
			centerp.add(a, "a");
			layout.show(centerp, "a");
			z = 1;

		}

		if (e.getSource() == next) {
			if (z == 1) {
				hakBanfCheck = a.aba.getKlicked("Firma", "bearbeiten");
				if (hakBanfCheck.length == 0) {
					JOptionPane.showMessageDialog(null,
							"Sie müssen etwas anhaken!");
					return;
				}

				BestCheckFirma cf = new BestCheckFirma(con, hakBanfCheck);
				if (!cf.eine) {
					JOptionPane.showMessageDialog(null,
							"es darf nur bei EINER Firma bestellt werden!");
					return;
				}
				if (cf.eine) {

					hakBanf = a.aba.getKlicked("ID", "bearbeiten");
					if (hakBanf.length == 0) {
						JOptionPane.showMessageDialog(null,
								"Sie müssen etwas anhaken!");
						return;
					}

					b = new AnzBanfPos(con, hakBanf);
					centerp.add(b, "b");
					layout.show(centerp, "b");
				}
			}
			if (z == 2) {

				String aktion = "nix";
				if (b.del.isSelected()) {
					int i = JOptionPane.showConfirmDialog(null,
							"Wollen Sie die Daten löschen?", "löschen",
							JOptionPane.YES_NO_OPTION);

					if (i == 0) {
						aktion = "del";

						hakDelPos = b.abp
								.getKlicked("Banfpos-nr", "bearbeiten");
						if (hakDelPos.length == 0) {
							JOptionPane.showMessageDialog(null,
									"Sie müssen etwas anhaken!");
							return;
						}

						c1 = new DelPos(con, hakDelPos);
						centerp.add(c1, "c1");
						z = 7;
					}

					if (i == 1) {
						aktion = "delCancel";

						z = 4;
					}
				}

				if (b.bestellen.isSelected()) {
					aktion = "bestellen";

					hakBestPos = b.abp.getKlicked("Banfpos-nr", "bearbeiten");
					if (hakBestPos.length == 0) {
						JOptionPane.showMessageDialog(null,
								"Sie müssen etwas anhaken!");
						return;
					}

					d = new AnzBest(user, con, hakBestPos);
					centerp.add(d, "d");

					z = 5;
				}

				if (aktion.equals("nix")) {
					JOptionPane.showMessageDialog(null,
							"löschen oder bestellen anklicken!");
					z--;
				}

			}
			if (z == 1) {
				z++;
			}
			if (z == 7) {
				a = new AnzBanf(con);
				centerp.add(a, "a");
				layout.show(centerp, "a");
				z = 1;
			}
			if (z == 4) {
				z = 7;
				layout.show(centerp, "c2");
			}
			if (z == 6) {
				String[] kopf = d.kopfdaten;
				kopf[6] = d.budget.getSelectedItem().toString();
				java.util.Date heute = new java.util.Date();
				try {
					String jahr = "" + heute.getYear();

					String jahrNeu = "" + jahr.charAt(1) + jahr.charAt(2);

					String wNeu = "";
					String sqlW1 = "SELECT wnummer FROM bestellung WHERE wnummer LIKE '%-"
							+ jahrNeu + "-%' LIMIT 1";
					ResultSet rsW1 = con.mysql_query(sqlW1);

					ResultSet rsW2;
					if (!rsW1.next()) {
						String sqlW2 = "SELECT wnummer FROM bestellung WHERE wnummer LIKE '%-"
								+ jahrNeu + "' LIMIT 1";
						rsW2 = con.mysql_query(sqlW2);

						if (!rsW2.next()) {
							wNeu = "W1-" + jahrNeu+"-"+d.budget.getSelectedItem().toString();
						}
					} else {

						String sqlW = "SELECT wnummer FROM bestellung WHERE wnummer LIKE '%-"
								+ jahrNeu + "-%' ORDER BY datum DESC LIMIT 1";
						ResultSet rsW = con.mysql_query(sqlW);
						rsW.next();
						String w = rsW.getString("wnummer");

						if (w.charAt(1) != '-') {
							{
								zahl = Integer.parseInt("" + w.charAt(1));

							}
							if (w.charAt(2) != '-') {
								zahl *= 10;
								zahl += Integer.parseInt("" + w.charAt(2));

								if (w.charAt(3) != '-') {
									zahl *= 10;
									zahl += Integer.parseInt("" + w.charAt(3));

									if (w.charAt(4) != '-') {
										zahl *= 10;
										zahl += Integer.parseInt(""
												+ w.charAt(4));

										if (w.charAt(5) != '-') {
											JOptionPane
													.showMessageDialog(
															null,
															"MAXIMALE ANZAHL BESTELLUNGEN ERREICHT! \nMelden Sie sich beim Systemadministrator!");
										}
									}

								}
							}
						} else {
							JOptionPane
									.showMessageDialog(null,
											"FEHLER BEI WNUMMER! \nMelden Sie sich beim Systemadministrator!");
						}
						zahl++;

						wNeu = "W" + zahl + "-" + jahrNeu + "-"
								+ d.budget.getSelectedItem().toString();

					}// else

					d.kopfdaten[7] = wNeu;
				} catch (Exception ex) {

					ex.getMessage();
					ex.printStackTrace();
				}

				kopf[8] = d.kommen.getText();
				if (kopf[6].startsWith("LMB")) {
					kopf[9] = "Elternverein der HTL Hollabrunn";
				} else if (kopf[6].startsWith("TRB")) {
					kopf[9] = "Teilrechtsfaehiger Bereich der HTL Hollabrunn";
				} else if (kopf[6].startsWith("LOF")) {
					kopf[9] = "Kuratorium der HTL Hollabrunn";
				} else if (kopf[6].startsWith("KUR")) {
					kopf[9] = "Kuratorium der HTL Hollabrunn";
				} else {
					kopf[9] = "Hoehere Technische Bundeslehranstalt Hollabrunn";
				}

				String[][] pos = d.bestDaten;
				f = new BestOkSek(user, kopf, pos, hakBestPos);
				centerp.add(f, "f");
				layout.show(centerp, "f");
				z = 7;
			}
			if (z == 5) {
				layout.show(centerp, "d");
				z++;
			}

		}

		revalidate();
		repaint();
	}

}
