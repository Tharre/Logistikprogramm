package Logistik;

import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

/*
 * Das Fenster zeigt die Materialdaten, welche für die Buchungenen beim Aushaendigen 
 * von Material wichtig sind, an und ermöglicht alle wichtigen Eingaben mit Hilfe von Auswahlmöglichkeiten.
 * Wird bei Aushaendigen1 aufgerufen. Ist das Fenster "Material buchen".
 */
public class LagerumbuchungenC extends JDialog implements ActionListener {

	// JButtons
	/** Button "Abbrechen" **/
	private JButton bAbbrechen = new JButton("Abbrechen");
	/** Button "Speichern" **/
	private JButton bSpeichern = new JButton("Speichern");

	// JLabel
	/**
	 * Überschiften: ID, Bezeichnung, Inventurgruppe, Bundesnummer, Firma,
	 * Lagerstk, Meldebestand, Einheit, Lagerort
	 **/
	private JLabel lMatIdTitle, lMatBezTitle, lMatInvGrTitle,
			lMatBundesnrTitle, lFirmennameTitle, lLagerstkTitle,
			lMeldebestandTitle, lEinheitTitle, lLagerortTitle, lLehrerTitle,
			lKstTitle, lStkTitle;
	/**
	 * Label für die Daten id, bezeichnung, inventurgruppe, bundesnummer,
	 * firmenname, lagerstk, meldebestand, einheit, lagerort
	 **/
	private JLabel lMatId, lMatBez, lMatInvGr, lMatBundesnr, lFirmenname,
			lLagerstk, lMeldebestand, lEinheit, lLagerort;

	// Eingabefelder
	/** Feld für den Lehrer, der das Material holt **/
	private Input lehrer;
	/** Feld für Kostenstelle, zu der das Material geht **/
	private Input kst;
	/** Feld für Anzahl, wie viel Stück ausgehändigt werden **/
	private Input stk;

	// Fragezeichen neben den Eingabefeldern
	/** Fragezeichen, bei dem eine Liste mit allen Lehrern erscheint **/
	private SelectInput lehrerSI;
	/** Fragezeichen, bei dem eine Liste mit allen Kostenstellen erscheint **/
	private SelectInput kstSI;

	/** um die Überschriften schöner zu gliedern **/
	private String zeilenfueller = "        ";

	// Daten

	/** Material-ID **/
	private Object materialId;

	/** Firmen-ID **/
	private int firmaId;

	/** Bestell-ID **/
	private int bestId;

	/** ZUBUCHEN = 0, ABBUCHEN = 1, AN_FIRMA = 2, ANSCHAUEN = 3 **/
	private int typ;

	/** Meldebestand **/
	private int meldebestand;

	/** Stückanzahl auf Lager **/
	private double lagerstk;

	/** Preis - um gesperrt/ausgegeben umzubuchen **/
	private double betrag;

	// String
	/** Materialbezeichnung **/
	private String bezeichnung;

	/** Inventurgruppe **/
	private String inventur;

	/** Bundesnummer **/
	private String bundesnr;

	/** Einheit **/
	private String einheit;

	/** Lagerort */
	private String lagerort;

	/** Kostenstelle **/
	private String kostenst;

	/** Firmenname **/
	private String firmenname;

	// JPanel
	/** Panel auf dem die Buttons "Speichern" und "Abbrechen" gespeichert werden **/
	private JPanel pButtons;

	/**
	 * 2-zeiliges Panel auf dem Titel und Daten zu Lagerstk, Meldebestand,
	 * Einheit, Lagerort und Firma gespeichert werden
	 **/
	private JPanel pLagerdaten;

	/**
	 * 2-zeiliges Panel auf dem Titel und Daten zu MaterialId, Bezeichnung,
	 * Inventurgruppe und Bundesnr gespeichert werden
	 **/
	private JPanel pMaterialdaten;

	// sonstiges
	private int firmaInt;

	/**
	 * false = die angegebene KST hat Material nicht selbst bestellt; true = die
	 * angegebene KST hat Material selbst bestellt
	 **/
	private boolean bestellt = true;

	/** Datenbankverbindung zur Logistik-DB **/
	private DBConnection con;

	/** Datenbankverbindung zur Budget-DB **/
	private DBConnection conb;

	/**
	 * Konstruktor
	 * 
	 * @param id
	 *            Material-ID
	 * @param typ
	 *            ZUBUCHEN = 0, ABBUCHEN = 1, AN_FIRMA = 2, ANSCHAUEN = 3
	 * @param title
	 *            Fenstertitel "Material buchen"
	 * @param con
	 *            Datenbankverbindung zur Budget-DB
	 * @param user
	 *            User
	 * @param iD
	 */
	public LagerumbuchungenC(Object id, int typ, String title,
			DBConnection con, UserImport user, int iD) {
		super(new JFrame(), title, true);

		if (iD != 0) {

			bestellt = ueberpruefeSelbstBestellt();
			System.out.println("bestellt " + bestellt);

		}

		conb = user.getConnectionKst();
		this.materialId = id;
		this.typ = typ;
		this.con = con;
		JOptionPane pane = new JOptionPane();
		pane.createDialog(null, "abbuchen");

		// Daten auslesen
		String query = "Select fm.firma,m.id, m.lagerort, m.bezeichnung, i.bezeichnung as inventurgruppe, m.bundesnr, m.stueck, m.meldebestand, fm.einheit, fm.preisExkl as preis, f.firmenname from material m,firma f, firma_material fm, inventurgruppe i where fm.firma = f.id and m.id=fm.material AND  m.inventurgruppe=i.id AND fm.preisExkl >= ALL(SELECT fmm.preisExkl from firma_material fmm where material ="
				+ id + ") AND m.id=" + id;
		ResultSet rs = con.mysql_query(query);

		try {
			rs.next();
			bestId = rs.getInt("id");
			bezeichnung = rs.getString("bezeichnung");
			inventur = rs.getString("inventurgruppe");
			bundesnr = rs.getString("bundesnr");
			lagerstk = rs.getDouble("stueck");
			meldebestand = rs.getInt("meldebestand");
			einheit = rs.getString("einheit");
			lagerort = rs.getString("lagerort");
			firmenname = rs.getString("firmenname");
			firmaId = rs.getInt("firma");
			rs.close();

		} catch (SQLException e) {
			e.getStackTrace();
		}

		// Materialdaten
		lMatIdTitle = new JLabel(zeilenfueller + "ID");
		lMatId = new JLabel(zeilenfueller + bestId);
		lMatBezTitle = new JLabel(zeilenfueller + "Bezeichnung");
		lMatBez = new JLabel(zeilenfueller + bezeichnung);
		lMatInvGrTitle = new JLabel(zeilenfueller + "Inventurgruppe");
		lMatInvGr = new JLabel(zeilenfueller + inventur);
		lMatBundesnrTitle = new JLabel(zeilenfueller + "Bundesnummer");
		lMatBundesnr = new JLabel(zeilenfueller + bundesnr);

		// Lagerdaten
		lLagerstkTitle = new JLabel("Stk im Lager");
		lLagerstk = new JLabel("" + lagerstk);
		lMeldebestandTitle = new JLabel("Meldebestand ");
		lMeldebestand = new JLabel("" + meldebestand);
		lEinheitTitle = new JLabel("Einheit");
		lEinheit = new JLabel("" + einheit);
		lLagerortTitle = new JLabel("Lagerort");
		lLagerort = new JLabel(lagerort);

		// Eingaben
		lLehrerTitle = new JLabel("Lehrer");
		lFirmennameTitle = new JLabel("Firma");
		lFirmenname = new JLabel(firmenname);
		lKstTitle = new JLabel("Kostenstelle");
		lStkTitle = new JLabel("Stück");
		
		stk = new Input(10, "stkI");
		lehrer = new Input(100, "lehrerI");
		kst = new Input(100, "kstI");


		String[] headl = { "id", "name", "cn" };

		// Felder mit Fragezeichen
		lehrerSI = new SelectInput(con, lehrer, "ldap_user", headl, "cn",
				"name", user);
		kstSI = new SelectInput(conb, kst, "werkstaette", new String[] {
				"Bereichsnr", "Bereichsname" }, null, "", user);

		// Panels füllen
		pButtons = new JPanel();
		pButtons.add(bSpeichern);
		pButtons.add(bAbbrechen);

		pLagerdaten = new JPanel();
		pLagerdaten.setLayout(new GridLayout(2, 5));
		pLagerdaten.add(lLagerstkTitle);
		pLagerdaten.add(lMeldebestandTitle);
		pLagerdaten.add(lEinheitTitle);
		pLagerdaten.add(lLagerortTitle);
		pLagerdaten.add(lFirmennameTitle);
		pLagerdaten.add(lLagerstk);
		pLagerdaten.add(lMeldebestand);
		pLagerdaten.add(lEinheit);
		pLagerdaten.add(lLagerort);
		pLagerdaten.add(lFirmenname);

		pMaterialdaten = new JPanel();
		GroupLayout layout = new GroupLayout(pMaterialdaten);
		pMaterialdaten.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(lMatIdTitle)
								.addComponent(lMatBezTitle)
								.addComponent(lMatInvGrTitle)
								.addComponent(lMatBundesnrTitle))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(lMatId).addComponent(lMatBez)
								.addComponent(lMatInvGr)
								.addComponent(lMatBundesnr)));
		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.TRAILING)
								.addComponent(lMatIdTitle).addComponent(lMatId))
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(lMatBezTitle)
								.addComponent(lMatBez))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(lMatInvGrTitle)
								.addComponent(lMatInvGr))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.TRAILING)
								.addComponent(lMatBundesnrTitle)
								.addComponent(lMatBundesnr)));

		GridBagLayout gbl = new GridBagLayout();
		Container c = getContentPane();
		c.removeAll();
		c.setLayout(gbl);

		// x y w h wx wy

		addComponent(c, gbl, new JLabel("\n"), 0, 0, 5, 1, 0, 0);

		addComponent(c, gbl, pMaterialdaten, 1, 1, 4, 1, 0, 1.0);

		addComponent(c, gbl, new JLabel("\n"), 0, 2, 5, 1, 0, 0);

		addComponent(c, gbl, pLagerdaten, 1, 3, 3, 1, 0, 0);

		addComponent(c, gbl, new JLabel("\n"), 0, 4, 5, 1, 0, 0);

		addComponent(c, gbl, lStkTitle, 1, 5, 1, 1, 0, 0);
		addComponent(c, gbl, stk, 1, 6, 1, 1, 0, 0);

		addComponent(c, gbl, lLehrerTitle, 2, 5, 1, 1, 0, 0);

		addComponent(c, gbl, lehrerSI, 2, 6, 1, 1, 1.0, 0);

		/*if (typ == LagerumbuchungenB.ABBUCHEN) {
			addComponent(

			c,

			gbl,

			// new JLabel(
			// "<html>Keine Kostenstelle eingeben, wenn<br> die Kostenstelle das Material<br> direkt bestellt hat<br> und das Lager nur als Zwischenlager dient!</html>"),
					new JLabel(""), 3, 7, 1, 1, 0, 0);
		}*/
		
		addComponent(c, gbl, new JLabel("\n"), 0, 7, 3, 1, 0, 0);

		addComponent(c, gbl, new JLabel("\n"), 4, 7, 1, 1, 0, 0);

		addComponent(c, gbl, pButtons, 0, 10, 2, 1, 0, 0);

		addComponent(c, gbl, new JLabel("\n"), 3, 8, 3, 1, 0, 0);

		addComponent(c, gbl, new JLabel("\n"), 0, 9, 5, 1, 0, 0);
		
		
		if(bestellt==false)
		{
					addComponent(c, gbl, lKstTitle, 3, 5, 1, 1, 0, 0);		
					addComponent(c, gbl, kstSI, 3, 6, 1, 1, 1.0, 0);
		}

		bSpeichern.addActionListener(this);
		bAbbrechen.addActionListener(this);

		if (typ != LagerumbuchungenB.ABBUCHEN) {
			setSize(680, 293);
		} else {
			setSize(680, 325);
		}

		setLocationRelativeTo(null);

		setVisible(true);

	}

	static void addComponent(Container cont, GridBagLayout gbl, Component c,
			int x, int y, int width, int height, double weightx, double weighty) {

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}// addComponent

	public int getAnzahl() {

		int anz;
		try {
			anz = Integer.parseInt(stk.getValue());
			return anz;
		} catch (NumberFormatException nfe) {
			return -1;
		}

	}// getAnzahl

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == bAbbrechen) {

			dispose();

		}

		if (e.getSource() == bSpeichern) {

			double st = 0;

			String s = stk.getValue().substring(1,

			stk.getValue().length() - 1);

			if (s.charAt(0) == '-') {

				JOptionPane.showMessageDialog(null,

				"Die Stückzahl darf nicht kleiner 0 sein!");

				return;

			}

			if (lehrerSI.getValue() == "") {

				JOptionPane.showMessageDialog(null, "Lehrer/Firma eingeben!");

				return;

			}

			// firmaInt = Integer.parseInt(firmaSI.getValue());

			try {

				st = Double.parseDouble(s);

				String sql = "";

				if (typ == LagerumbuchungenB.ABBUCHEN

				|| typ == LagerumbuchungenB.AN_FIRMA) {

					if (st > lagerstk) {

						JOptionPane.showMessageDialog(null,

						"Es können max. "

						+ lagerstk

						+ " Stück ausgehändigt werden!\n Es sind nur mehr "

						+ lagerstk + " Stück im Lager!");

						return;

					} else {

						sql = "UPDATE material set stueck=stueck-" + st
								+ " where id = " + materialId + "";

						if (meldebestand >= lagerstk - st) {
							JOptionPane.showMessageDialog(null,

							"Meldebestand von "

							+ meldebestand

							+ " Einheiten unterschritten.\n nur mehr "

							+ (lagerstk - st)

							+ " im Lager!");
						}

					}

				} else if (typ == LagerumbuchungenB.ZUBUCHEN) {
					sql = "UPDATE material set stueck=stueck+" + st

					+ " where id = " + materialId + "";
				}

				con.mysql_update(sql);

				long datum = System.currentTimeMillis() / 1000;

				if (typ == LagerumbuchungenB.ABBUCHEN) {

					try {

						String sel = "Select cn from ldap_user where name Like "

								+ lehrer.getValue();

						ResultSet rs = con.mysql_query(sel);

						rs.next();

						String usern = rs.getString("cn");

						String insert = "INSERT INTO buchungen (material , stk , user , kst , datum,firma ) VALUES ("

								+ materialId

								+ ", "

								+ (-st)

								+ ", '"

								+ usern

								+ "','"

								+ kstSI.getValue()

								+ "', "

								+ datum

								+ ","

								+ firmaId + ")";

						con.mysql_update(insert);

						kostenst = kstSI.getValue();

						if (!bestellt) {

							sel = "Select ((fm.preisExkl)+(fm.preisExkl/100*fm.mwst)) as preis from firma_material fm WHERE fm.material="

									+ materialId + " AND fm.firma=" + firmaId;

							rs = con.mysql_query(sel);

							rs.next();

							betrag = rs.getDouble("preis");

							betrag = betrag * st;

							String query = "UPDATE kostenstelleut8 set ausgegeben=ausgegeben-"

									+ betrag

									+ " where nummerSelbst like '1.1.1.4'";

							conb.mysql_update(query);

							String query2 = "UPDATE kostenstelleut8 set ausgegeben=ausgegeben+"

									+ betrag

									+ " where name like '"

									+ kostenst

									+ "'";

							conb.mysql_update(query2);

						}

						rs.close();

					} catch (SQLException ex) {

					}

				} else if (typ == LagerumbuchungenB.AN_FIRMA) {

					int fid;
					String kostenstelle;

					if (!bestellt)
						kostenstelle = "Lager-zentral";

					else if (kstSI.getValue() == "") {

						JOptionPane.showMessageDialog(null,

						"Kostenstelle eingeben!");

						return;

					}

					else
						kostenstelle = kstSI.getValue();

					try {
						String sel;
						ResultSet rs;
						/*
						 * String sel = "Select id From  where id ="
						 * 
						 * + firmaId;
						 * 
						 * ResultSet rs = con.mysql_query(sel);
						 * 
						 * rs.next();
						 * 
						 * fid = rs.getInt("id");
						 */

						String insert = "INSERT INTO buchungen_firma (material , stk , firma , kst , datum ) VALUES ("

								+ materialId

								+ ", "

								+ st

								+ ", "

								+ firmaId

								+ ", '"

								+ kostenstelle + "', " + datum + ")";

						con.mysql_update(insert);

						sel = "Select max(fm.preisexkl)*(1+fm.mwst/100) as preis from firma_material fm WHERE fm.material="

								+ materialId;

						rs = con.mysql_query(sel);

						rs.next();

						betrag = rs.getDouble("preis");

						betrag = betrag * st;

						String query = "UPDATE kostenstelleut8 set ausgegeben=ausgegeben-"

								+ betrag

								+ " where name like '"

								+ kostenstelle

								+ "'";

						conb.mysql_update(query);

					} catch (Exception ex) {

					}

				} else if (typ == LagerumbuchungenB.ZUBUCHEN) {

					if (kstSI.getValue() == "") {

						JOptionPane.showMessageDialog(null,

						"Kostenstelle eingeben!");

						return;

					}

					try {

						String sel = "Select cn from ldap_user where name Like "

								+ lehrer.getValue();

						ResultSet rs = con.mysql_query(sel);

						rs.next();

						String usern = rs.getString("cn");

						String insert = "INSERT INTO buchungen (material , stk , user , kst , datum,firma ) VALUES ("

								+ materialId

								+ ", "

								+ st

								+ ", '"

								+ usern

								+ "','"

								+ kstSI.getValue()

								+ "', "

								+ datum

								+ ","

								+ firmaInt + ")";

						con.mysql_update(insert);

						kostenst = kstSI.getValue();

						if (kostenst.length() != 0) {

							sel = "Select max(fm.preisexkl)*(1+fm.mwst/100) as preis from firma_material fm WHERE fm.material="

									+ materialId;

							rs = con.mysql_query(sel);

							rs.next();

							betrag = rs.getDouble("preis");

							betrag = betrag * st;

							String query = "UPDATE kostenstelleut8 set ausgegeben=ausgegeben+"

									+ betrag

									+ " where nummerSelbst like '1.1.1.4'";

							conb.mysql_update(query);

							String query2 = "UPDATE kostenstelleut8 set ausgegeben=ausgegeben-"

									+ betrag

									+ " where name like '"

									+ kostenst

									+ "'";

							conb.mysql_update(query2);

						}

					} catch (SQLException ex) {

					}

				}

				JOptionPane.showMessageDialog(this,

				"Die Daten wurde erfolgreich gespeichert.");

				dispose();

			} catch (NumberFormatException nfe) {

				JOptionPane.showMessageDialog(null,

				"Bei Abbuchungsstück keine Zahl eingegeben!");

			}

		}

	}

	public boolean ueberpruefeSelbstBestellt() {
		int pruefeSelbstBestellt = JOptionPane
				.showConfirmDialog(
						null,
						"Hat die angegebene Kostenstelle das Material selbst bestellt?",
						"Abfrage", JOptionPane.YES_NO_OPTION);

		if (pruefeSelbstBestellt == 1) {

			bestellt = false;
		} else if (pruefeSelbstBestellt == 0) {
			bestellt = true;
		} else // Fenster geschlossen, speichern=-1
		{

			JOptionPane.showMessageDialog(null, "Sie haben nichts ausgewählt!");
			ueberpruefeSelbstBestellt();
		}

		return bestellt;
	}
}
