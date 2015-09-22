package Budget;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.sql.*;
import java.io.File.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;

/**
 * Datenbank wird gesichert. Pfad muss angegeben werden. Danach speichern in
 * Textdokument
 */
public class DBExport extends JFrame implements ActionListener {

	/**
	 * Button zum Durchführen des Exports
	 */
	public JButton exportB = new JButton("exportieren");

	/**
	 * Überschrift
	 */
	private JLabel titel = new JLabel("Budget-Datenbank exportieren",
			JLabel.CENTER);
	/**
	 * Connection zur Budget-Datenbank
	 */
	private Connection con;
	/**
	 * 1=Export zum Jahresende; 0 = Export;
	 */
	private int kennung;
	private TextdateiImport ti;
	/**
	 * Connection zur Vorjahres-Datenbank
	 */
	private Connection conB_alt;
	private Jahresende je;

	public DBExport(Connection con, Connection conB_alt,
			int kennung, Jahresende je) {
		// super(user);

		this.con = con;
		this.conB_alt = conB_alt;
		this.kennung = kennung;
		this.je = je;

		titel.setFont(new Font("Arial", Font.BOLD, 20));

		setLayout(new BorderLayout());
		add(titel, BorderLayout.NORTH);
		add(exportB, BorderLayout.CENTER);

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

				// UT3
				bw.write("DROP TABLE IF EXISTS `abteilungut3`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `abteilungut3` (`nummer` int(11) NOT NULL AUTO_INCREMENT,"
								+ "`name` varchar(50) COLLATE latin1_general_ci NOT NULL,"
								+ "`edvhatAnteil` varchar(20) COLLATE latin1_general_ci NOT NULL,"
								+ "`geplant` double NOT NULL,"
								+ "`gesperrt` double NOT NULL,"
								+ "`ausgegeben` double NOT NULL,"
								+ "`projektkennung` int(11) NOT NULL,"
								+ "`festgeplant` double NOT NULL,"
								+ "`raumnummer` varchar(30) COLLATE latin1_general_ci NOT NULL,"
								+ "PRIMARY KEY (`nummer`)) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;");
				bw.newLine();

				String sqlUT3 = "SELECT * FROM abteilungut3";
				Statement stmt = con.createStatement();
				ResultSet rsUT3 = stmt.executeQuery(sqlUT3);
				while (rsUT3.next()) {
					String a = rsUT3.getString(1);
					String b = rsUT3.getString(2);
					String c = rsUT3.getString(3);
					String d = rsUT3.getString(4);
					String f = rsUT3.getString(5);
					String g = rsUT3.getString(6);
					String h = rsUT3.getString(7);
					String i = rsUT3.getString(8);
					String j = rsUT3.getString(9);

					bw.write("INSERT INTO `abteilungut3` VALUES (" + a + ", \'"
							+ b + "\', \'" + c + "\', " + d + ", " + f + ", "
							+ g + ", " + h + ", " + i + ", \'" + j + "\');");
					bw.newLine();
				}

				rsUT3.close();

				// BereichUT8
				bw.write("DROP TABLE IF EXISTS `bereichut8`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `bereichut8` (`hauptnummer` int(11) NOT NULL,`nummer` int(11) NOT NULL auto_increment,`name` varchar(50) collate latin1_general_ci NOT NULL,`geplant` double NOT NULL,`nummerSelbst` varchar(30) collate latin1_general_ci NOT NULL,PRIMARY KEY  (`nummer`));");
				bw.newLine();

				String sqlBereich = "SELECT * FROM bereichut8";
				Statement stmtBereich = con.createStatement();
				ResultSet rsBereich = stmtBereich.executeQuery(sqlBereich);
				while (rsBereich.next()) {
					String a = rsBereich.getString(1);
					String b = rsBereich.getString(2);
					String c = rsBereich.getString(3);
					String d = rsBereich.getString(4);
					String f = rsBereich.getString(5);

					bw.write("INSERT INTO `bereichut8` VALUES (" + a + ", " + b
							+ ", \'" + c + "\', " + d + ", \'" + f + "\');");
					bw.newLine();
				}

				rsBereich.close();

				// HauptbereichUT8
				bw.write("DROP TABLE IF EXISTS `hauptbereichut8`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `hauptbereichut8` (`nummer` int(11) NOT NULL auto_increment,`name` varchar(50) collate latin1_general_ci NOT NULL,`geplant` double NOT NULL,`nummerSelbst` varchar(30) collate latin1_general_ci NOT NULL,PRIMARY KEY  (`nummer`));");
				bw.newLine();

				String sqlHauptbereich = "SELECT * FROM hauptbereichut8";
				Statement stmtHauptbereich = con.createStatement();
				ResultSet rsHauptbereich = stmtHauptbereich
						.executeQuery(sqlHauptbereich);
				while (rsHauptbereich.next()) {
					String a = rsHauptbereich.getString(1);
					String b = rsHauptbereich.getString(2);
					String c = rsHauptbereich.getString(3);
					String d = rsHauptbereich.getString(4);

					bw.write("INSERT INTO `hauptbereichut8` VALUES (" + a
							+ ", \'" + b + "\', " + c + ", \'" + d + "\');");
					bw.newLine();
				}

				rsHauptbereich.close();

				// HauptkostenstelleUT8
				bw.write("DROP TABLE IF EXISTS `hauptkostenstelleut8`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `hauptkostenstelleut8` (`hauptnummer` int(11) NOT NULL, `nummer` int(11) NOT NULL auto_increment,`name` varchar(50) collate latin1_general_ci NOT NULL,`geplant` double NOT NULL,`nummerSelbst` varchar(30) collate latin1_general_ci NOT NULL,PRIMARY KEY  (`nummer`));");
				bw.newLine();

				String sqlHKst = "SELECT * FROM hauptkostenstelleut8";
				Statement stmtHKst = con.createStatement();
				ResultSet rsHKst = stmtHKst.executeQuery(sqlHKst);
				while (rsHKst.next()) {
					String a = rsHKst.getString(1);
					String b = rsHKst.getString(2);
					String c = rsHKst.getString(3);
					String d = rsHKst.getString(4);
					String f = rsHKst.getString(5);

					bw.write("INSERT INTO `hauptkostenstelleut8` VALUES (" + a
							+ ", " + b + ", \'" + c + "\', " + d + ", \'" + f
							+ "\');");
					bw.newLine();
				}

				rsHKst.close();

				// KostenstelleUT8
				bw.write("DROP TABLE IF EXISTS `kostenstelleut8`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `kostenstelleut8` (`hauptnummer` int(11) NOT NULL,`nummer` int(11) NOT NULL AUTO_INCREMENT,`raumnummer` varchar(30) collate latin1_general_ci NOT NULL,`projektkennung` int(11) NOT NULL,`name` varchar(50) collate latin1_general_ci NOT NULL,`geplant` double NOT NULL,`gesperrt` double NOT NULL,`ausgegeben` double NOT NULL,`nummerSelbst` varchar(30) collate latin1_general_ci NOT NULL,`kurzbezeichn` varchar(10) collate latin1_general_ci NOT NULL,PRIMARY KEY (`nummer`),KEY `hauptnummer` (`hauptnummer`))");
				bw.newLine();

				String sqlKst = "SELECT * FROM kostenstelleut8";
				Statement stmtKst = con.createStatement();
				ResultSet rsKst = stmtKst.executeQuery(sqlKst);
				while (rsKst.next()) {
					String a = rsKst.getString(1); // hauptnummer
					String b = rsKst.getString(2); // nummer
					String c = rsKst.getString(3); // raumnummer
					String d = rsKst.getString(4); // projektkennung
					String f = rsKst.getString(5); // name
					String g = rsKst.getString(6); // geplant
					String h = rsKst.getString(7); // gesperrt
					String i = rsKst.getString(8); // ausgegeben
					String j = rsKst.getString(9); // nummerSelbst
					String k = rsKst.getString(10);// Kurzbezeichnung

					bw.write("INSERT INTO `kostenstelleut8` VALUES (" + a
							+ ", " + b + ", \'" + c + "\'," + d + "," + "\'"
							+ f + "\', " + g + ", " + h + ", " + i + ", \'" + j
							+ "\', \'" + k + "\');");
					bw.newLine();
				}

				rsKst.close();

				// LMB 1
				bw.write("DROP TABLE IF EXISTS `lmb`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `lmb` (`nummer` int(11) NOT NULL AUTO_INCREMENT,`name` varchar(50) collate latin1_general_ci NOT NULL,`kennung` int(1) NOT NULL,`geplant` double NOT NULL,`gesperrt` double NOT NULL,`ausgegeben` double NOT NULL,`projektkennung` int(11) NOT NULL,`festgeplant` double NOT NULL,`raumnummer` varchar(30) collate latin1_general_ci NOT NULL, PRIMARY KEY (`nummer`))  AUTO_INCREMENT=55");
				bw.newLine();

				String sqlLmb = "SELECT * FROM lmb";
				Statement stmtLmb = con.createStatement();
				ResultSet rsLmb = stmtLmb.executeQuery(sqlLmb);
				while (rsLmb.next()) {
					String a = rsLmb.getString(1);
					String b = rsLmb.getString(2);
					String c = rsLmb.getString(3);
					String d = rsLmb.getString(4);
					String f = rsLmb.getString(5);
					String g = rsLmb.getString(6);
					String h = rsLmb.getString(7);
					String i = rsLmb.getString(8);
					String j = rsLmb.getString(9);

					bw.write("INSERT INTO `lmb` VALUES (" + a + ", \'" + b
							+ "\', " + c + ", " + d + ", " + f + ", " + g
							+ ", " + h + ", " + i + ", \'" + j + "\');");
					bw.newLine();
				}

				rsLmb.close();

				// Projekte
				bw.write("DROP TABLE IF EXISTS `projekt`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `projekt` (`nummer` int(11) NOT NULL AUTO_INCREMENT, `nummerSelbst` int(11) NOT NULL,`name` varchar(50) collate latin1_general_ci NOT NULL,`lehrer` varchar(50) collate latin1_general_ci NOT NULL,  `kurzz` varchar(30) collate latin1_general_ci NOT NULL,	  `klasse` varchar(10) collate latin1_general_ci NOT NULL,`datum` varchar(10) collate latin1_general_ci NOT NULL,`teilnehmer` varchar(50) collate latin1_general_ci NOT NULL,  `abteilung` varchar(30) collate latin1_general_ci NOT NULL,`raumnummer` varchar(30) collate latin1_general_ci NOT NULL, PRIMARY KEY (`nummer`)	)");
				bw.newLine();

				String sqlProjekt = "SELECT * FROM projekt";
				Statement stmtProjekt = con.createStatement();
				ResultSet rsProjekt = stmtProjekt.executeQuery(sqlProjekt);
				while (rsProjekt.next()) {
					String a = rsProjekt.getString(1);
					String b = rsProjekt.getString(2);
					String c = rsProjekt.getString(3);
					String d = rsProjekt.getString(4);
					String f = rsProjekt.getString(5);
					String g = rsProjekt.getString(6);
					String h = rsProjekt.getString(7);
					String i = rsProjekt.getString(8);
					String j = rsProjekt.getString(9);
					String k = rsProjekt.getString(10);

					bw.write("INSERT INTO `projekt` VALUES (" + a + ", " + b
							+ ", \'" + c + "\', \'" + d + "\', \'" + f
							+ "\', \'" + g + "\', \'" + h + "\', \'" + i
							+ "\', \'" + j + "\', \'" + k + "\');");
					bw.newLine();
				}

				rsProjekt.close();

				// Sonderbudget
				bw.write("DROP TABLE IF EXISTS `sonderbudget`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `sonderbudget` (`nummer` int(11) NOT NULL AUTO_INCREMENT, `name` varchar(50) collate latin1_general_ci NOT NULL,`geplant` double NOT NULL,`gesperrt` double NOT NULL,`ausgegeben` double NOT NULL, PRIMARY KEY(`nummer`))");

				bw.newLine();

				String sqlSonder = "SELECT * FROM sonderbudget";
				Statement stmtSonder = con.createStatement();
				ResultSet rsSonder = stmtSonder.executeQuery(sqlSonder);
				while (rsSonder.next()) {
					String a = rsSonder.getString(1);
					String b = rsSonder.getString(2);
					String c = rsSonder.getString(3);
					String d = rsSonder.getString(4);
					String f = rsSonder.getString(5);

					bw.write("INSERT INTO `sonderbudget` VALUES (" + a + ", \'"
							+ b + "\', " + c + "," + d + "," + f + ");");
					bw.newLine();
				}

				rsSonder.close();

				// Rechnungen
				bw.write("DROP TABLE IF EXISTS `rechnung`;");
				bw.newLine();
				bw
						.write("CREATE TABLE IF NOT EXISTS `rechnung` ( `nummer` int(11) NOT NULL AUTO_INCREMENT,  `rechnungsbetrag` double NOT NULL,  `bestellbetrag` double NOT NULL,`skonto` double NOT NULL, `externeNummer` varchar(20) collate latin1_general_ci NOT NULL,`interneNummer` varchar(20) collate latin1_general_ci NOT NULL,`zahlart` varchar(20) collate latin1_general_ci NOT NULL, `wNummer` varchar(100) collate latin1_general_ci NOT NULL,  `buchhaltungsbelege` varchar(15) collate latin1_general_ci NOT NULL,`inventarnummer` varchar(15) collate latin1_general_ci NOT NULL,`rechnungsstatus` int(1) NOT NULL,`sonderabzug` double NOT NULL,PRIMARY KEY (`nummer`))");
				bw.newLine();

				String sqlRechnungen = "SELECT * FROM rechnung";
				Statement stmtRechnungen = con.createStatement();
				ResultSet rsRechnungen = stmtRechnungen
						.executeQuery(sqlRechnungen);
				while (rsRechnungen.next()) {
					String a = rsRechnungen.getString(1);
					String b = rsRechnungen.getString(2);
					String c = rsRechnungen.getString(3);
					String d = rsRechnungen.getString(4);
					String f = rsRechnungen.getString(5);
					String g = rsRechnungen.getString(6);
					String h = rsRechnungen.getString(7);
					String i = rsRechnungen.getString(8);
					String j = rsRechnungen.getString(9);
					String k = rsRechnungen.getString(10);
					String l = rsRechnungen.getString(11);
					String m = rsRechnungen.getString(12);

					bw.write("INSERT INTO `rechnung` VALUES (" + a + ", " + b
							+ ", " + c + ", " + d + ", \'" + f + "\', \'" + g
							+ "\', \'" + h + "\', \'" + i + "\', \'" + j
							+ "\', \'" + k + "\', " + l + ", " + m + ");");
					bw.newLine();
				}

				rsRechnungen.close();

				bw.close();
				
				

				JOptionPane.showMessageDialog(null,
						"Exportvorgang erfolgreich durchgeführt!");
				dispose();

				if (kennung == 1)// Aufruf der Klasse von Jahresende
				{
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							ti = new TextdateiImport(conB_alt, kennung, je);
							ti.pack();
							ti.setLocationRelativeTo(null);
							ti.setVisible(true);
						}

					});
				}

			} catch (Exception ex) {
				System.out.println("ERROR exp: " + ex.getMessage());
				JOptionPane.showMessageDialog(null,
						"Fehler! Daten konnten nicht exportiert werden!");
				dispose();
			}

		}// if

	}// actionPerformed
}