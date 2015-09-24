package Budget;

import java.sql.*;

import java.util.Vector;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;

import Logistik.Laden;
import Logistik.Login;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javax.swing.JTextField;

/**
 * Mit der Klasse AbfrageExportExcel können Exceldateien erstellt und
 * überschrieben werden. Diese Funktion wird für den Export von einzelnen
 * Abfragen und für den Export aller Budget-Tabellen verwendet.
 * <p>
 * Title: AbfrageExportExcel
 * 
 * @author Haupt, Liebhart
 **/

public class AbfrageExportExcel extends JFrame implements ActionListener {
	// sonstiges
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;

	// Excel Sheet
	/** Namen der Spalten in einem Excel Sheet **/
	private String[] spalten;
	/** Excel Workbook **/
	private HSSFWorkbook wb;
	/** Reihe im Excel Sheet **/
	private HSSFRow row;
	/**
	 * Excel Sheets, zugehörig zu einem Workbook; pro Tabelle wird 1 Sheet
	 * erstellt
	 **/
	private HSSFSheet sheet;

	// Label
	/** Hinweis zum Pfad, wo die Datei gespeichert werden soll **/
	private JLabel hinweis = new JLabel(
			"Bitte geben Sie den Pfad ein. Bsp.: C:/Users/Name");
	/** Hinweis zum Namen der Exceldatei **/
	private JLabel hinweis2 = new JLabel(
			"Bitte geben Sie den Namen der Datei an. Bsp.: test.xls");

	// TextField
	/** Eingabefeld für den Pfad, wo die Datei gespeichert werden soll **/
	private JTextField eingabefeld = new JTextField();
	/** Eingabefeld für den Namen der Exceldatei **/
	private JTextField eingabefeldName = new JTextField();

	// Button
	/** Button zum Erstellen der Exceldatei **/
	private JButton erstelle = new JButton("Excelsheet erstellen");

	// String
	/** Name der Exceldatei **/
	private String outputFile;

	// Vector
	/** Daten der Tabelle abteilungut3 **/
	private Vector ut3 = new Vector();
	/** Daten der Tabelle hauptbereichut8 **/
	private Vector ut8hb = new Vector();
	/** Daten der Tabelle bereichut8 **/
	private Vector ut8b = new Vector();
	/** Daten der Tabelle hauptkostenstelleut8 **/
	private Vector ut8hk = new Vector();
	/** Daten der Tabelle kostenstelleut8 **/
	private Vector ut8k = new Vector();
	/** Daten der Tabelle lmb mit der Kennung 1 **/
	private Vector lmb1 = new Vector();
	/** Daten der Tabelle lmb mit der Kennung 2 **/
	private Vector lmb2 = new Vector();
	/** Daten der Tabelle projekt **/
	private Vector projekt = new Vector();
	/** Daten der Tabelle sonderbudget **/
	private Vector sonderbudget = new Vector();
	/** Daten der Tabelle rechnung **/
	private Vector rechnungen = new Vector();
	/** die einzelnen Spaltenüberschriften **/
	private Vector tabelleUeberschrift = new Vector();
	/** alle Spaltenüberschriften - nach Sheet sortiert - gesammelt **/
	private Vector ueberschrift = new Vector();
	/** Hilfsvector für einen einzelnen Datensatz **/
	private Vector zeile;
	/** Daten, die exportiert werden sollen **/
	private Vector daten = new Vector();

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 * @param daten
	 *            Daten, die exportiert werden sollen
	 * @param spalten
	 *            Namen der Spalten in einem Excel Sheet
	 */
	public AbfrageExportExcel(Connection con, Connection conL, Vector daten,
			String[] spalten) {

		super("Daten exportieren");

		this.daten = daten;
		this.spalten = spalten;

		di = new DatenImport(con, conL);

		erstelle.addActionListener(this);

		erstelle.setToolTipText("Erstellen der Exceldatei");

		setLayout(new GridLayout(3, 2));
		add(hinweis);
		add(eingabefeld);
		add(hinweis2);
		add(eingabefeldName);
		add(erstelle);
	}

	/**
	 * erstellt ein Excel Sheet
	 */
	public void erstelleExcelSheet() {

		try {

			FileOutputStream fOut = new FileOutputStream(outputFile);

			// schreibt das Excel-Sheet
			wb.write(fOut);
			fOut.flush();
			fOut.close();

			JOptionPane
					.showMessageDialog(this, "Die Exceldatei wurde erstellt");

		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(this,
							"Sie haben eine falsche Eingabe gemacht. Bitte beachten Sie die Richtlinien.");
		}

	}

	/**
	 * erstellt ein Sheet (Registerkarte) in der Exceldatei
	 * 
	 * @param daten
	 *            Daten, die in Excel exportiert werden sollen
	 * @param sheetname
	 *            Name des Sheets
	 * 
	 **/
	public void erstelleSheet(Vector daten, String sheetname) {

		sheet = wb.createSheet(sheetname);

		for (int j = 0; j < daten.size(); j++) {

			row = sheet.createRow((short) j);

			zeile = new Vector();

			zeile = (Vector) daten.elementAt(j);

			for (int i = 0; i < zeile.size(); i++) {
				HSSFCell cell = row.createCell((short) i);

				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell.setCellValue(zeile.elementAt(i).toString());
			}
		}

	}

	/**
	 * speichert alle Spaltenüberschriften - sortiert nach Sheet/Tabelle - auf
	 * einen Vector
	 */
	public void erstelleUeberschriften() {

		ueberschrift.clear();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("EDV/ HAT Anteil");
		tabelleUeberschrift.add("fest geplant");
		tabelleUeberschrift.add("geplant");
		tabelleUeberschrift.add("ausgegeben");
		tabelleUeberschrift.add("gesperrt");
		tabelleUeberschrift.add("verfügbar");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("geplant");
		tabelleUeberschrift.add("ausgegeben");
		tabelleUeberschrift.add("gesperrt");
		tabelleUeberschrift.add("verfügbar");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("geplant");
		tabelleUeberschrift.add("ausgegeben");
		tabelleUeberschrift.add("gesperrt");
		tabelleUeberschrift.add("verfügbar");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("geplant");
		tabelleUeberschrift.add("ausgegeben");
		tabelleUeberschrift.add("gesperrt");
		tabelleUeberschrift.add("verfügbar");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("Raumnummer");
		tabelleUeberschrift.add("Kurzbezeichnung");
		tabelleUeberschrift.add("geplant");
		tabelleUeberschrift.add("ausgegeben");
		tabelleUeberschrift.add("gesperrt");
		tabelleUeberschrift.add("verfügbar");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("fest geplant");
		tabelleUeberschrift.add("geplant");
		tabelleUeberschrift.add("ausgegeben");
		tabelleUeberschrift.add("gesperrt");
		tabelleUeberschrift.add("verfügbar");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("fest geplant");
		tabelleUeberschrift.add("geplant");
		tabelleUeberschrift.add("ausgegeben");
		tabelleUeberschrift.add("gesperrt");
		tabelleUeberschrift.add("verfügbar");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("geplant");
		tabelleUeberschrift.add("ausgegeben");
		tabelleUeberschrift.add("gesperrt");
		tabelleUeberschrift.add("verfügbar");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Name");
		tabelleUeberschrift.add("Lehrer");
		tabelleUeberschrift.add("Kurzzeichen");
		tabelleUeberschrift.add("Klasse");
		tabelleUeberschrift.add("Datum");
		tabelleUeberschrift.add("Teilnehmer");
		tabelleUeberschrift.add("Abteilung");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

		tabelleUeberschrift.add("Nummer");
		tabelleUeberschrift.add("Rechnungsbetrag");
		tabelleUeberschrift.add("Bestellbetrag");
		tabelleUeberschrift.add("Skonto");
		tabelleUeberschrift.add("externe Nummer");
		tabelleUeberschrift.add("interne Nummer");
		tabelleUeberschrift.add("Zahlart");
		tabelleUeberschrift.add("W-Nummer");
		tabelleUeberschrift.add("Buchhaltungsbelege");
		tabelleUeberschrift.add("Inventarnummer");
		tabelleUeberschrift.add("Rechnungsstatus");
		tabelleUeberschrift.add("Sonderabzug");

		ueberschrift.add(tabelleUeberschrift);

		tabelleUeberschrift = new Vector();

	}

	/**
	 * wandelt das String[] mit den Spaltennamen in einen Vector um
	 * 
	 * @param spalten
	 *            Namen der Spalten
	 * @return tabelleUeberschrift Vector mit den Spaltenüberschriften
	 */
	public Vector erstelleUberschriftausArray(String[] spalten) {
		tabelleUeberschrift.clear();

		for (int i = 0; i < spalten.length; i++)
			tabelleUeberschrift.add(spalten[i]);

		return tabelleUeberschrift;

	}

	/**
	 * liesst die Daten aus den folgenden Tabellen in der Budget-Datenbank aus:
	 * ut3, ut8hb, ut8b, ut8hk, ut8k, lmb1, lmb2, sonderbudget, projekt,
	 * rechnungen. Pro Tabelle wird 1 Sheet (Registerkarte) erstellt.
	 */
	public void leseDatenAus() {
		erstelleUeberschriften();

		ut3 = di.auslesenDaten("abteilungut3", 1);
		ut3.add(0, ueberschrift.elementAt(0));
		erstelleSheet(ut3, "UT3");

		ut8hb = di.auslesenDaten("hauptbereichut8", 5);
		ut8hb.add(0, ueberschrift.elementAt(1));
		erstelleSheet(ut8hb, "UT8 HB");

		ut8b = di.auslesenDaten("bereichut8", 6);
		ut8b.add(0, ueberschrift.elementAt(2));
		erstelleSheet(ut8b, "UT8 B");

		ut8hk = di.auslesenDaten("hauptkostenstelleut8", 7);
		ut8hk.add(0, ueberschrift.elementAt(3));
		erstelleSheet(ut8hk, "UT8 HK");

		ut8k = di.auslesenDaten("kostenstelleut8", 8);
		ut8k.add(0, ueberschrift.elementAt(4));
		erstelleSheet(ut8k, "UT8 K");

		lmb1 = di.auslesenDatenMitWhereKlausel("lmb", "kennung", 1, 3);
		lmb1.add(0, ueberschrift.elementAt(5));
		erstelleSheet(lmb1, "LMB1");

		lmb2 = di.auslesenDatenMitWhereKlausel("lmb", "kennung", 2, 3);
		lmb2.add(0, ueberschrift.elementAt(6));
		erstelleSheet(lmb2, "LMB1_2011");

		sonderbudget = di.auslesenDaten("sonderbudget", 4);
		sonderbudget.add(0, ueberschrift.elementAt(7));
		erstelleSheet(sonderbudget, "Sonderbudget");

		projekt = di.auslesenDaten("projekt", 2);
		projekt.add(0, ueberschrift.elementAt(8));
		erstelleSheet(projekt, "Projekt");

		rechnungen = di.auslesenRechnungen();
		rechnungen.add(0, ueberschrift.elementAt(9));
		erstelleSheet(rechnungen, "Rechnungen");
	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public Laden load;

	public void actionPerformed(ActionEvent e) {
		// Exportieren von allen Daten in ausgewählten Tabellen (siehe Methode
		// "leseDatenAus")
		if (e.getSource() == erstelle && daten.size() == 0) {
			outputFile = eingabefeld.getText() + "/"
					+ eingabefeldName.getText();

			wb = new HSSFWorkbook();
			leseDatenAus();
			erstelleExcelSheet();
			this.dispose();

		}
		// Exportieren einzelner Abfragen
		if (e.getSource() == erstelle && daten.size() != 0) {
			outputFile = eingabefeld.getText() + "/"
					+ eingabefeldName.getText();

			wb = new HSSFWorkbook();
			Vector namen = erstelleUberschriftausArray(spalten);
			daten.add(0, namen);
			erstelleSheet(daten, "Abfrage");
			erstelleExcelSheet();
			this.dispose();

		}

	}

}
