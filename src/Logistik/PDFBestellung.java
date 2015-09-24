package Logistik;

import java.io.File;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import Budget.DatenImport;

public class PDFBestellung extends JFrame {

	/**
	 * Datenbankverbindung Logistik
	 */
	private DBConnection con;

	private int l = 0;

	/**
	 * Checkobox-Array für die Auswahl von Bestellungen
	 */
	private JCheckBox[] bestellen;

	private String[] bestellenID;

	private String status = "";
	private String wnr = "";
	private String queryCount = "";
	private String query1;
	private String query2;
	public JButton[][] arrayButton;
	private int[][] iBestPosIds;
	public int anzBestPos = 0;
	private double summe = 0;
	public String sqlBest="";
	public String budget;
	public boolean elternverein;

	/**
	 * Konstruktor PDFBestellung
	 * 
	 * @param user
	 * @param hakDet
	 */
	public PDFBestellung() {
		System.out.println("PDFBestellung");
	}

	public void printPDF(UserImport user, Object[] hakDet) {
		System.out.println("PDFBestellung.printPDF hakDet length: "
				+ hakDet.length);

		con = user.getConnection();

		Object[] hak = new Object[hakDet.length];
		for (int x = 0; x < hakDet.length; x++) {
			hak[x] = hakDet[x];
		}

		l = hak.length;

		// bestellen = new JCheckBox[hak.length];
		// bestellenID = new String[hak.length];

		try {
			for (int i = 0; i < hak.length; i++) {

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
					drucken(hak[i], f.getAbsolutePath());
					
				} else {
					System.out.println("Speichern fehlgeschlagen");
				}
			}
		} catch (Exception ex) {
			ex.getMessage();
			ex.printStackTrace();
		}

	}


	
	
	public void drucken(Object bestid, String fName) {

		System.out.println("PDFBestellung.drucken fName; " + fName);

		try {
			String sqlBest = "SELECT * FROM bestellung,firma WHERE bestId="
					+ bestid + " AND bestellung.firma=firma.id";
			ResultSet rsBest = con.mysql_query(sqlBest);
			rsBest.next();
			
			wnr = rsBest.getString("wnummer");
			budget = rsBest.getString("budget");
			elternverein = false;

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
			d.addFirma(rsBest.getString("firmenname"),
					rsBest.getString("strasse"), rsBest.getString("plz") + " "
							+ rsBest.getString("ort"));
			d.addAuftrag(rsBest.getString("wnummer"), bestDat,
					rsBest.getString("kundennr"), rsBest.getString("uid"),
					rsBest.getString("kreditorennummer"),
					rsBest.getString("einkaeufergruppe"));
			d.addAngebot(rsBest.getString("wnummer"), aNr, aS);

			String sqlBestpos = "SELECT bestpos.*,material.bezeichnung,firma_material.* FROM bestpos,material,firma_material WHERE bestpos.bestellId="
					+ bestid
					+ " AND bestpos.bezeichnung=material.id AND bestpos.bezeichnung=firma_material.material AND firma_material.firma="
					+ rsBest.getString("firma.id");
			ResultSet rsBestpos = con.mysql_query(sqlBestpos);
			int i = 1;
			while (rsBestpos.next()) {
				i++;
				d.addRow(rsBestpos.getString("material.bezeichnung"),
						rsBestpos.getString("firma_material.artNr"),
						rsBestpos.getString("bestpos.menge"),
						rsBestpos.getString("firma_material.einheit"),
						rsBestpos.getString("bestpos.preisExkl"),
						rsBestpos.getString("bestpos.mwst"),
						rsBestpos.getString("bestpos.preisGesamt"));

			}
			String sqlU = "SELECT name FROM ldap_user WHERE cn=\'"
					+ rsBest.getString("antragsteller") + "\'";
			ResultSet rsU = con.mysql_query(sqlU);
			rsU.next();

			d.addFooter(lBed, lFri, vArt, "-", rsU.getString("name"),
					rsBest.getString("anschriftRechnung"), bem, elternverein);
			d.close();

			File f = new File(FTP.temp() + rsBest.getString("wnummer") + ".pdf");
			try {
				System.out.println("PDFBestellung.drucken File FTP.upload");
				// FTP.upload(f, "BESTELLUNG");
			} catch (Exception e) {
				System.out
						.println("PDFBestellung.drucken File FTP.upload - catch");
				e.getMessage();
				e.printStackTrace();
			}

			if (fName == null) {
				Logistik.openPDF(FTP.temp() + rsBest.getString("wnummer")
						+ ".pdf");
				System.out.println("PDFBestellung.drucken Logistik.openPDF "
						+ FTP.temp() + rsBest.getString("wnummer") + ".pdf");
			} else {
				Logistik.openPDF(fName);
				System.out.println("PDFBestellung.drucken Logistik.openPDF "
						+ fName);
			}

			rsBest.close();
			rsBestpos.close();
			rsU.close();

		} catch (Exception ex) {
			System.out.println("PDFBestellung.drucken - catch");
			ex.getMessage();
			ex.printStackTrace();
		}
	}

}
