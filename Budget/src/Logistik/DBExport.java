package Logistik;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;

/**
 * Datenbank wird gesichert. Pfad muss angegeben werden. Danach speichern in
 * Textdokument
 */
public class DBExport extends LayoutMainPanel implements ActionListener {

	/**
	 * Button zum Durchführen des Exports
	 */
	public JButton exportB = new JButton("exportieren");

	/**
	 * Überschrift
	 */
	private JLabel titel = new JLabel("DATENBANK EXPORTIEREN");

	public DBExport(UserImport user) {
		super(user);

		titel.setFont(new Font("Arial", Font.BOLD, 20));

		setLayoutM(new BorderLayout());
		addM(titel, BorderLayout.NORTH);
		addM(exportB, BorderLayout.CENTER);

		exportB.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exportB) {
			try {

				java.util.Date heute = new java.util.Date();
				String name = "" + (heute.getYear() + 1900) + "-"
						+ (heute.getMonth() + 1) + "-" + heute.getDate()
						+ ".txt";
				String pfad = "";

				JFileChooser fc = new JFileChooser();

				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setSelectedFile(new File(name));
				fc.setDialogTitle("Speichern");
				fc.setApproveButtonText("Speichern");
				fc.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.isDirectory()
								|| f.getName().toLowerCase().endsWith(".txt");
					}

					@Override
					public String getDescription() {
						return "Textdatei";
					}
				});

				int state = fc.showOpenDialog(null);

				if (state == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();

					pfad = file.getAbsolutePath();
				} else {
					return;
				}

				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
						pfad)));

				// Text zum Anlegen der Struktur

				// BANF
				bw.write("DROP TABLE IF EXISTS `banf`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `banf` (`id` int(11) NOT NULL auto_increment,`antragsteller` varchar(30) collate latin1_general_ci NOT NULL,`kostenstelle` varchar(50) collate latin1_general_ci NOT NULL default '0',`firma` int(10) NOT NULL,`status` tinyint(4) NOT NULL default '0',`datum` int(20) NOT NULL, `kommentar` varchar(300) collate latin1_general_ci, PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlBANF = "SELECT * FROM banf";
				ResultSet rsBANF = con.mysql_query(sqlBANF);
				while (rsBANF.next()) {
					String a = rsBANF.getString(1);
					String b = rsBANF.getString(2);
					String c = rsBANF.getString(3);
					String d = rsBANF.getString(4);
					String f = rsBANF.getString(5);
					String g = rsBANF.getString(6);
					String h = rsBANF.getString(7);

					bw.write("INSERT INTO `banf` VALUES (" + a + ", \'" + b
							+ "\', \'" + c + "\', " + d + ", " + f + ", " + g
							+ ", \'" + h + "\');");
					bw.newLine();
				}

				rsBANF.close();

				// BANFPOS
				bw.write("DROP TABLE IF EXISTS `banfpos`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `banfpos` (`menge` int(10) NOT NULL default '0',`bezeichnung` int(10) NOT NULL default '0',`preisExkl` float NOT NULL default '0',`mwst` int(2) NOT NULL default '0',`banfposnr` int(10) NOT NULL auto_increment,`kommentar1` varchar(50) collate latin1_general_ci NOT NULL default '',`status` int(11) NOT NULL default '0',`banf` int(10) NOT NULL default '0',`einheit` varchar(10) collate latin1_general_ci NOT NULL,PRIMARY KEY  (`banfposnr`));");
				bw.newLine();

				String sqlBANFPOS = "SELECT * FROM banfpos";
				ResultSet rsBANFPOS = con.mysql_query(sqlBANFPOS);
				while (rsBANFPOS.next()) {
					String a = rsBANFPOS.getString(1);
					String b = rsBANFPOS.getString(2);
					String c = rsBANFPOS.getString(3);
					String d = rsBANFPOS.getString(4);
					String f = rsBANFPOS.getString(5);
					String g = rsBANFPOS.getString(6);
					String h = rsBANFPOS.getString(7);
					String i = rsBANFPOS.getString(8);
					String j = rsBANFPOS.getString(9);

					bw.write("INSERT INTO `banfpos` VALUES (" + a + ", " + b
							+ ", " + c + ", " + d + ", " + f + ", \'" + g
							+ "\', " + h + ", " + i + ", \'" + j + "\');");
					bw.newLine();
				}

				rsBANFPOS.close();

				// BESTELLUNG
				bw.write("DROP TABLE IF EXISTS `bestellung`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `bestellung` (`datum` int(20) NOT NULL default '0',`antragsteller` varchar(30) collate latin1_general_ci NOT NULL default '0',`firma` int(5) NOT NULL default '0',`bestId` int(10) NOT NULL auto_increment,`status` tinyint(4) NOT NULL default '0',`statusbez` tinyint(4) NOT NULL default '0',`budget` varchar(25) collate latin1_general_ci NOT NULL default '0',`wnummer` varchar(40) collate latin1_general_ci NOT NULL default 'w',`kommentar` varchar(200) collate latin1_general_ci NOT NULL default '',`anschriftRechnung` varchar(300) collate latin1_general_ci NOT NULL default '0',PRIMARY KEY  (`bestId`));");
				bw.newLine();

				String sqlBEST = "SELECT * FROM bestellung";
				ResultSet rsBEST = con.mysql_query(sqlBEST);
				while (rsBEST.next()) {
					String a = rsBEST.getString(1);
					String b = rsBEST.getString(2);
					String c = rsBEST.getString(3);
					String d = rsBEST.getString(4);
					String f = rsBEST.getString(5);
					String g = rsBEST.getString(6);
					String h = rsBEST.getString(7);
					String i = rsBEST.getString(8);
					String j = rsBEST.getString(9);
					String k = rsBEST.getString(10);

					bw.write("INSERT INTO `bestellung` VALUES (" + a + ", \'"
							+ b + "\', " + c + ", " + d + ", " + f + ", " + g
							+ ", \'" + h + "\', \'" + i + "\', \'" + j
							+ "\', \'" + k + "\');");
					bw.newLine();
				}

				rsBEST.close();

				// BESTPOS
				bw.write("DROP TABLE IF EXISTS `bestpos`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `bestpos` (`bezeichnung` int(10) NOT NULL default '0',`menge` int(19) NOT NULL default '0',`preisExkl` float NOT NULL default '0',`mwst` int(2) NOT NULL default '0',`preisInkl` float NOT NULL default '0',`preisGesamt` float NOT NULL default '0',`einheit` varchar(20) collate latin1_general_ci NOT NULL default '0',`lehrer` varchar(50) collate latin1_general_ci NOT NULL default '0',`id` int(11) NOT NULL auto_increment,`bestellId` int(11) NOT NULL default '0',`status` int(2) NOT NULL default '0',`statusbez` int(2) NOT NULL default '0',`banfposnr` int(11) NOT NULL default '0',`geliefert` int(11) NOT NULL default '0',`bezahlt` double(19,3) NOT NULL default '0',`lieferkommentar` varchar(100) collate latin1_general_ci NOT NULL default '0',PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlBESTPOS = "SELECT * FROM bestpos";
				ResultSet rsBESTPOS = con.mysql_query(sqlBESTPOS);
				while (rsBESTPOS.next()) {
					String a = rsBESTPOS.getString(1);
					String b = rsBESTPOS.getString(2);
					String c = rsBESTPOS.getString(3);
					String d = rsBESTPOS.getString(4);
					String f = rsBESTPOS.getString(5);
					String g = rsBESTPOS.getString(6);
					String h = rsBESTPOS.getString(7);
					String i = rsBESTPOS.getString(8);
					String j = rsBESTPOS.getString(9);
					String k = rsBESTPOS.getString(10);
					String l = rsBESTPOS.getString(11);
					String m = rsBESTPOS.getString(12);
					String n = rsBESTPOS.getString(13);
					String o = rsBESTPOS.getString(14);
					String p = rsBESTPOS.getString(15);
					String q = rsBESTPOS.getString(16);

					bw.write("INSERT INTO `bestpos` VALUES (" + a + ", " + b
							+ ", " + c + ", " + d + ", " + f + ", " + g
							+ ", \'" + h + "\', \'" + i + "\', " + j + ", " + k
							+ ", " + l + ", " + m + ", " + n + ", " + o + ", "
							+ p + ", \'" + q + "\');");
					bw.newLine();
				}

				rsBESTPOS.close();

				// BUCHUNGEN
				bw.write("DROP TABLE IF EXISTS `buchungen`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `buchungen` (`id` int(11) NOT NULL auto_increment,`material` int(11) NOT NULL default '0',`stk` decimal(11,3) NOT NULL default '0',`user` varchar(30) collate latin1_general_ci NOT NULL default '0',`kst` varchar(50) collate latin1_general_ci NOT NULL default '0',`datum` int(20) NOT NULL default '0', `firma` int(11) NOT NULL default '0', PRIMARY KEY (`id`));");
				bw.newLine();

				String sqlBUCH = "SELECT * FROM buchungen";
				ResultSet rsBUCH = con.mysql_query(sqlBUCH);
				while (rsBUCH.next()) {
					String a = rsBUCH.getString(1);
					String b = rsBUCH.getString(2);
					String c = rsBUCH.getString(3);
					String d = rsBUCH.getString(4);
					String f = rsBUCH.getString(5);
					String g = rsBUCH.getString(6);
					String h = rsBUCH.getString(7);

					bw.write("INSERT INTO `buchungen` VALUES (" + a + ", " + b
							+ ", " + c + ", \'" + d + "\', \'" + f + "\', " + g
							+ ", " + h + ");");
					bw.newLine();
				}

				rsBUCH.close();

				// BUCHUNGEN_FIRMA
				bw.write("DROP TABLE IF EXISTS `buchungen_firma`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `buchungen_firma` (`id` int(11) NOT NULL auto_increment,`material` int(11) NOT NULL default '0',`stk` int(11) NOT NULL default '0',`firma` int(11) NOT NULL default '0',`kst` varchar(50) collate latin1_general_ci NOT NULL default '0',`datum` int(20) NOT NULL default '0', PRIMARY KEY (`id`));");
				bw.newLine();

				String sqlBUCHFIR = "SELECT * FROM buchungen_firma";
				ResultSet rsBUCHFIR = con.mysql_query(sqlBUCHFIR);
				while (rsBUCHFIR.next()) {
					String a = rsBUCHFIR.getString(1);
					String b = rsBUCHFIR.getString(2);
					String c = rsBUCHFIR.getString(3);
					String d = rsBUCHFIR.getString(4);
					String f = rsBUCHFIR.getString(5);
					String g = rsBUCHFIR.getString(6);

					bw.write("INSERT INTO `buchungen_firma` VALUES (" + a
							+ ", " + b + ", " + c + ", " + d + ", \'" + f
							+ "\', " + g + ");");
					bw.newLine();
				}

				rsBUCHFIR.close();

				// BUNDESNR
				bw.write("DROP TABLE IF EXISTS `bundesnr`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `bundesnr` (`id` int(5) NOT NULL auto_increment,`bezeichnung` varchar(100) collate latin1_general_ci NOT NULL default '0',`uebergruppe` int(5) default NULL,`nr` int(1) NOT NULL default '0',PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlBNR = "SELECT * FROM bundesnr";
				ResultSet rsBNR = con.mysql_query(sqlBNR);
				while (rsBNR.next()) {
					String a = rsBNR.getString(1);
					String b = rsBNR.getString(2);
					String c = rsBNR.getString(3);
					String d = rsBNR.getString(4);

					bw.write("INSERT INTO `bundesnr` VALUES (" + a + ", \'" + b
							+ "\', " + c + ", " + d + ");");
					bw.newLine();
				}

				rsBNR.close();

				// EINHEITEN
				bw.write("DROP TABLE IF EXISTS `einheiten`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `einheiten` (`id` int(10) NOT NULL auto_increment,`einheit` varchar(10) collate latin1_general_ci NOT NULL default '0',PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlEIN = "SELECT * FROM einheiten";
				ResultSet rsEIN = con.mysql_query(sqlEIN);
				while (rsEIN.next()) {
					String a = rsEIN.getString(1);
					String b = rsEIN.getString(2);

					bw.write("INSERT INTO `einheiten` VALUES (" + a + ", \'"
							+ b + "\');");
					bw.newLine();
				}

				rsEIN.close();

				// FIRMA
				bw.write("DROP TABLE IF EXISTS `firma`;");
				bw.newLine();
				bw
						.write("CREATE TABLE `firma` ( `id` int(5) NOT NULL auto_increment, `firmenname` varchar(200) collate latin1_general_ci NOT NULL default '0', `erstellungsdatum` int(20) NOT NULL default '0', `plz` varchar(10) collate latin1_general_ci NOT NULL default '0', `ort` varchar(30) collate latin1_general_ci NOT NULL default '0', `strasse` varchar(50) collate latin1_general_ci NOT NULL default '0', `mail` varchar(30) collate latin1_general_ci default NULL,  `staat` varchar(10) collate latin1_general_ci NOT NULL default '0', `kundennr` varchar(50) collate latin1_general_ci default NULL,  `sachbearbeiter` varchar(100) collate latin1_general_ci default NULL, `telefon` varchar(25) collate latin1_general_ci default NULL,  `fax` varchar(25) collate latin1_general_ci default NULL,	  `homepage` varchar(50) collate latin1_general_ci default NULL,  `uid` varchar(20) collate latin1_general_ci default NULL, `kondition` varchar(100) collate latin1_general_ci default NULL,  PRIMARY KEY  (`id`))");
				bw.newLine();

				String sqlFIR = "SELECT * FROM firma";
				ResultSet rsFIR = con.mysql_query(sqlFIR);
				while (rsFIR.next()) {
					String a = rsFIR.getString(1);
					String b = rsFIR.getString(2);
					String c = rsFIR.getString(3);
					String d = rsFIR.getString(4);
					String f = rsFIR.getString(5);
					String g = rsFIR.getString(6);
					String h = rsFIR.getString(7);
					String i = rsFIR.getString(8);
					String j = rsFIR.getString(9);
					String k = rsFIR.getString(10);
					String l = rsFIR.getString(11);
					String m = rsFIR.getString(12);
					String n = rsFIR.getString(13);
					String o = rsFIR.getString(14);
					String p = rsFIR.getString(15);

					bw.write("INSERT INTO `firma` VALUES (" + a + ", \'" + b
							+ "\', " + c + ", \'" + d + "\', \'" + f + "\', \'"
							+ g + "\', \'" + h + "\', \'" + i + "\', \'" + j
							+ "\', \'" + k + "\', \'" + l + "\', \'" + m
							+ "\', \'" + n + "\', \'" + o + "\',\'" + p
							+ "\');");
					bw.newLine();
				}

				rsFIR.close();

				// FIRMA_MATERIAL
				bw.write("DROP TABLE IF EXISTS `firma_material`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `firma_material` (`id` int(10) NOT NULL auto_increment,`firma` int(5) NOT NULL default '0',`material` int(5) NOT NULL default '0',`preisExkl` decimal(10,2) NOT NULL default '0',`mwst` int(4) NOT NULL default '0',`einheit` varchar(10) collate latin1_general_ci default NULL,`artNr` varchar(30) collate latin1_general_ci NOT NULL default '0',PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlFIRMAT = "SELECT * FROM firma_material";
				ResultSet rsFIRMAT = con.mysql_query(sqlFIRMAT);
				while (rsFIRMAT.next()) {
					String a = rsFIRMAT.getString(1);
					String b = rsFIRMAT.getString(2);
					String c = rsFIRMAT.getString(3);
					String d = rsFIRMAT.getString(4);
					String f = rsFIRMAT.getString(5);
					String g = rsFIRMAT.getString(6);
					String h = rsFIRMAT.getString(7);

					bw.write("INSERT INTO `firma_material` VALUES (" + a + ", "
							+ b + ", " + c + ", \'" + d + "\', " + f + ", \'"
							+ g + "\', \'" + h + "\');");
					bw.newLine();
				}

				rsFIRMAT.close();

				// INVENTURGRUPPE
				bw.write("DROP TABLE IF EXISTS `inventurgruppe`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `inventurgruppe` (`id` int(5) NOT NULL auto_increment,`bezeichnung` varchar(100) collate latin1_general_ci NOT NULL default '0',`uebergruppe` int(5) default NULL,PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlINV = "SELECT * FROM inventurgruppe";
				ResultSet rsINV = con.mysql_query(sqlINV);
				while (rsINV.next()) {
					String a = rsINV.getString(1);
					String b = rsINV.getString(2);
					String c = rsINV.getString(3);

					bw.write("INSERT INTO `inventurgruppe` VALUES (" + a
							+ ", \'" + b + "\', " + c + ");");
					bw.newLine();
				}

				rsINV.close();

				// LDAP_USER
				bw.write("DROP TABLE IF EXISTS `ldap_user`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `ldap_user` (`id` int(5) NOT NULL auto_increment,`cn` varchar(30) collate latin1_general_ci NOT NULL default '0',`mail` varchar(100) collate latin1_general_ci NOT NULL default '0',`name` varchar(100) collate latin1_general_ci NOT NULL default '0', PRIMARY KEY(`id`));");
				bw.newLine();

				String sqlLDAP = "SELECT * FROM ldap_user";
				ResultSet rsLDAP = con.mysql_query(sqlLDAP);
				while (rsLDAP.next()) {
					String a = rsLDAP.getString(1);
					String b = rsLDAP.getString(2);
					String c = rsLDAP.getString(3);
					String d = rsLDAP.getString(4);

					bw.write("INSERT INTO `ldap_user` VALUES (" + a + ", \'"
							+ b + "\', \'" + c + "\', \'" + d + "\');");
					bw.newLine();
				}

				rsLDAP.close();

				// LOGIN
				bw.write("DROP TABLE IF EXISTS `login`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `login` (`id` int(5) NOT NULL auto_increment,`uid` int(5) NOT NULL default '0',`hash` varchar(35) collate latin1_general_ci NOT NULL default '',`time` int(20) NOT NULL default '0',PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlLOG = "SELECT * FROM login";
				ResultSet rsLOG = con.mysql_query(sqlLOG);
				while (rsLOG.next()) {
					String a = rsLOG.getString(1);
					String b = rsLOG.getString(2);
					String c = rsLOG.getString(3);
					String d = rsLOG.getString(4);

					bw.write("INSERT INTO `login` VALUES (" + a + ", " + b
							+ ", \'" + c + "\', " + d + ");");
					bw.newLine();
				}

				rsLOG.close();

				// MATERIAL
				bw.write("DROP TABLE IF EXISTS `material`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `material` (`id` int(10) NOT NULL auto_increment,`bezeichnung` varchar(100) collate latin1_general_ci NOT NULL default '0',`erstellungsdatum` int(20) NOT NULL default '0',`bundesnr` int(5) NOT NULL default '0',`inventurgruppe` int(5) default NULL,`stueck` decimal(11,3) NOT NULL default '0',`meldebestand` decimal(11,3) NOT NULL default '0',`lagerort` varchar(20) collate latin1_general_ci NOT NULL default '0', `erfasser` varchar(100) collate latin1_general_ci NOT NULL default '0', PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlMAT = "SELECT * FROM material";
				ResultSet rsMAT = con.mysql_query(sqlMAT);
				while (rsMAT.next()) {
					String a = rsMAT.getString(1);
					String b = rsMAT.getString(2);
					String c = rsMAT.getString(3);
					String d = rsMAT.getString(4);
					String f = rsMAT.getString(5);
					String g = rsMAT.getString(6);
					String h = rsMAT.getString(7);
					String i = rsMAT.getString(8);
					String j = rsMAT.getString(9);

					bw.write("INSERT INTO `material` VALUES (" + a + ", \'" + b
							+ "\', " + c + ", " + d + ", " + f + ", " + g
							+ ", " + h + ", \'" + i + "\', \'" + j + "\');");
					bw.newLine();
				}

				rsMAT.close();

				// RECHTE
				bw.write("DROP TABLE IF EXISTS `rechte`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `rechte` (`id` int(5) NOT NULL auto_increment,`usergroup` int(5) NOT NULL default '0',`recht` int(5) NOT NULL default '0',PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlREC = "SELECT * FROM rechte";
				ResultSet rsREC = con.mysql_query(sqlREC);
				while (rsREC.next()) {
					String a = rsREC.getString(1);
					String b = rsREC.getString(2);
					String c = rsREC.getString(3);

					bw.write("INSERT INTO `rechte` VALUES (" + a + ", " + b
							+ ", " + c + ");");
					bw.newLine();
				}

				rsREC.close();

				// USER
				bw.write("DROP TABLE IF EXISTS `user`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `user` (`cn` varchar(30) collate latin1_general_ci NOT NULL default '0',`usergroup` int(6) NOT NULL default '0',PRIMARY KEY  (`cn`));");
				bw.newLine();

				String sqlUSER = "SELECT * FROM user";
				ResultSet rsUSER = con.mysql_query(sqlUSER);
				while (rsUSER.next()) {
					String a = rsUSER.getString(1);
					String b = rsUSER.getString(2);

					bw.write("INSERT INTO `user` VALUES (\'" + a + "\', " + b
							+ ");");
					bw.newLine();
				}

				rsUSER.close();

				// USERGROUP
				bw.write("DROP TABLE IF EXISTS `usergroup`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `usergroup` (`id` int(5) NOT NULL auto_increment,`name` varchar(50) collate latin1_general_ci NOT NULL default '',PRIMARY KEY  (`id`));");
				bw.newLine();

				String sqlUG = "SELECT * FROM usergroup";
				ResultSet rsUG = con.mysql_query(sqlUG);
				while (rsUG.next()) {
					String a = rsUG.getString(1);
					String b = rsUG.getString(2);

					bw.write("INSERT INTO `usergroup` VALUES (" + a + ", \'"
							+ b + "\');");
					bw.newLine();
				}

				rsUG.close();

				bw.close();

				JOptionPane.showMessageDialog(null,
						"Exportvorgang erfolgreich durchgeführt!");

			} catch (Exception ex) {
				ex.getMessage();
			}

		}// if

	}// actionPerformed
}