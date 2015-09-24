package Logistik;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class MaterialExport extends LayoutMainPanel implements ActionListener {

	/** Workbook **/
	private HSSFWorkbook wb;
	/** Row **/
	private HSSFRow row;
	/** Sheet **/
	private HSSFSheet sheet;

	// Label
	/** Hinweis zum Pfad **/
	private JLabel hinweis = new JLabel(
			"Bitte geben Sie den Pfad ein. Bsp.: C:/Users/Name");
	/** Hinweis zur Datei **/
	private JLabel hinweis2 = new JLabel(
			"Bitte geben Sie den Namen der Datei an. Bsp.: test.xls");

	// TextField
	/** Eingabefeld fuer den Pfad **/
	private JTextField eingabefeld = new JTextField();
	/** Eingabefeld fuer den Namen der Datei **/
	private JTextField eingabefeldName = new JTextField();

	// Button
	/** Button, dass die Datei erstellt wird **/
	private JButton erstelle = new JButton("Excelsheet erstellen");

	/** Hilfsvector fuer einen einzelnen Datensatz **/
	private Vector zeile;
	private Vector namen;
	/** Vector mit den Daten **/
	private Vector daten = new Vector();
	private Vector rowV;

	private String outputFile;

	public MaterialExport(UserImport u) {
		super(u);
		setLayout(new GridLayout(15, 2));
		add(hinweis);
		add(eingabefeld);
		add(hinweis2);
		add(eingabefeldName);
		add(erstelle);

		erstelle.addActionListener(this);
	}

	/**
	 * erstellt das ExcelSheet mit den Daten
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
	 * erstellt ein Sheet in der Exceldatei
	 * 
	 * @param daten
	 *            Daten, die in Excel exportiert werden sollen
	 * @param sheetname
	 *            Name des Registers
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

	public void actionPerformed(ActionEvent arg0) {

		outputFile = eingabefeld.getText() + "/" + eingabefeldName.getText();

		wb = new HSSFWorkbook();
		daten = getDaten();

		namen = new Vector();
		namen.add("ID");
		namen.add("Bezeichnung");
		namen.add("BundesNr");
		namen.add("Inventurgruppe");
		namen.add("Stück");
		namen.add("Preis exkl");
		namen.add("MWSt");
		namen.add("Einheit");
		namen.add("ArtikelNr");
		namen.add("Firma");
		namen.add("PLZ");
		namen.add("Ort");
		namen.add("Sachbearbeiter");

		daten.add(0, namen);
		erstelleSheet(daten, "Material");
		erstelleExcelSheet();

	}

	public Vector getDaten() {

		daten = new Vector();
		String qry = "SELECT * FROM material,firma_material,firma,inventurgruppe,bundesnr WHERE material.bundesnr=bundesnr.id AND material.inventurgruppe=inventurgruppe.id AND material.id=firma_material.material AND firma.id=firma_material.firma ORDER BY material.bezeichnung";
		ResultSet rs = con.mysql_query(qry);

		try {
			while (rs.next()) {
				rowV = new Vector();
				rowV.add(rs.getString("material.id"));
				rowV.add(rs.getString("material.bezeichnung"));
				rowV.add(rs.getString("bundesnr.bezeichnung"));
				rowV.add(rs.getString("inventurgruppe.bezeichnung"));
				rowV.add(rs.getString("stueck"));
				rowV.add(rs.getString("preisExkl"));
				rowV.add(rs.getString("mwst"));
				rowV.add(rs.getString("einheit"));
				rowV.add(rs.getString("artNr"));
				rowV.add(rs.getString("firmenname"));
				rowV.add(rs.getString("plz"));
				rowV.add(rs.getString("ort"));
				rowV.add(rs.getString("sachbearbeiter"));

				daten.add(rowV);

			}

			rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return daten;

	}
}
