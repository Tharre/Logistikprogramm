package Budget;

import javax.swing.*;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.util.Vector;

/**
 *In der Klasse Tabelle wird eine Tabelle erstellt
 *<p>
 * Title: Tabelle
 * 
 * @author Haupt, Liebhart
 **/
public class Tabelle extends JPanel {

	// Vector
	/**
	 * ein Vector= Zeile einer Tabelle aus dem Vector Daten, Hilfsvariable um
	 * das Object[][] erstellen zu koennen
	 **/
	private Vector zeile;
	/** Daten,die in die Tabelle geschrieben werden sollen **/
	private Vector daten;

	// sonstiges
	/** welche Zeilen markiert sind **/
	private int[] rows;
	/** Scrollpane **/
	private JScrollPane pane;
	/**
	 * ein Wort aus einer Zelle der Tabelle, Hilfsvariable um das Object[][]
	 * erstellen zu koennen
	 **/
	private String wort;

	/** Namen der Spalten **/
	private String[] spalten;

	/** Tabelle **/
	private JTable table;

	/** Object, in dem die Daten gespeichert werden **/
	private Object[][] dataO;

	/** TabelModel **/
	private MyTableModel model;

	/** TabelHeader **/
	private JTableHeader header;

	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;

	// Der TableRowSorter wird die Daten des Models sortieren
	TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();

	/**
	 * Konstruktor
	 * 
	 * @param spalten
	 *            Name der Spalten der Tabelle
	 * @param daten
	 *            Daten, die in die Tabelle geschrieben werden sollen
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 * 
	 **/
	public Tabelle(String[] spalten, Vector daten, Connection con,
			Connection conL) {

		this.spalten = spalten;
		this.daten = daten;

		di = new DatenImport(con, conL);

		// Vector Daten in ein Object[][] umwandeln
		umwandeln();

		// neue Tabelle erstellen
		model = new MyTableModel();

		table = new JTable(model);

		// Der Sorter muss dem JTable bekannt sein
		table.setRowSorter(sorter);

		// ... und der Sorter muss wissen, welche Daten er sortieren muss
		sorter.setModel(model);

		table.setRowHeight(21);

		table.getTableHeader().setReorderingAllowed(false);

		header = table.getTableHeader();

		header.setDefaultRenderer(new MyHeaderCellRenderer());
		table.setAutoCreateRowSorter(true);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {

				Component c = super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);

				row = sorter.convertRowIndexToModel(row);

				// Fuer ABC- Analyse
				if (dataO[row].length > 0) {
					if (dataO[row][0].toString().equals("A"))
						setBackground(new Color(245, 135, 129));

					if (dataO[row][0].toString().equals("B"))
						setBackground(Color.YELLOW);

					if (dataO[row][0].toString().equals("C"))
						setBackground(Color.GREEN);
				}
				// Fuer Bestellung
				if (dataO[row].length > 7) {

					try {
						if (dataO[row][4].toString()
								.equals("teilweise bezahlt"))
							setBackground(Color.YELLOW);

						else if (dataO[row][4].toString().equals(
								"komplett geliefert")
								|| dataO[row][6].toString().equals(
										"komplett geliefert")
								// wenn verfuegbar <0 ist
								|| Double.parseDouble(dataO[row][7].toString()) < 0)
							setBackground(new Color(245, 135, 129));

						else
							setBackground(Color.WHITE);

					} catch (NumberFormatException e) {
						setBackground(Color.WHITE);

					}
					// wenn verfuegbar <0 ist
				} else if (dataO[row].length > 6) {

					try {

						if (Double.parseDouble(dataO[row][6].toString()) < 0)

							setBackground(new Color(245, 135, 129));

						else {
							setBackground(Color.WHITE);

						}

					} catch (NumberFormatException e) {
						setBackground(Color.WHITE);

					}

				}// end if
				else if (dataO[row].length > 5) {

					try {
						if ((Double.parseDouble(dataO[row][5].toString())) < 0)
							setBackground(new Color(245, 135, 129));

						else
							setBackground(Color.WHITE);

					} catch (NumberFormatException e) {
						setBackground(Color.WHITE);

					}
				}

				if (isSelected)
					setBackground(new Color(153, 217, 234));

				return this;
			}

		});

	}

	/**
	 *Die innere Klasse MyHeaderCellRenderer formatiert die Spaltenkoepfe
	 *<p>
	 * Title: MyHeaderCellRenderer
	 * 
	 * @author Haupt, Liebhart
	 **/
	class MyHeaderCellRenderer extends JLabel implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			// text
			setText(value.toString());
			// normale schriftart
			setFont(new Font("SansSerif", Font.BOLD, 12));
			// der standard rahmen für spaltenköpfe
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			// text zentiriert darstellen
			setHorizontalAlignment(SwingConstants.CENTER);
			// undurchsichtig damit man die hintergrundfarbe sieht.
			setOpaque(true);
			// je nach spalte die hintergrundfarbe setzen
			for (int i = 0; i < spalten.length; i++)
				setBackground(Color.YELLOW);

			return this;
		}

	}

	/**
	 * ruft die Methode setValueAt auf
	 */
	public void aufrufen() {
		model.setValueAt(null, 0, 0);

	}

	/**
	 * liefert die Anzahl der Datensaezte
	 * 
	 * @return Anzahl der Datensaezte
	 */
	public int getDatensaetze() {
		return model.getRowCount();
	}

	/**
	 *In der inneren Klasse MyTableModel wird ein TableModel erstellt
	 *<p>
	 * Title: MyTableModel
	 * 
	 * @author Haupt, Liebhart
	 **/
	class MyTableModel extends AbstractTableModel {
		/**
		 * der Inhalt einer Zelle der Tabelle soll nicht editierbar sein
		 * 
		 * @param row
		 *            Zeilen
		 * @param col
		 *            Spalten
		 * @return false Zellen sollen nicht editierbar sein
		 */
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		/**
		 * der Inhalt der Tabelle soll nicht editierbar sein
		 * 
		 * @return false Zellen sollen nicht editierbar sein
		 */
		public boolean setCellEditable() {
			return true;
		}

		/**
		 *gibt die Anzahl der Spalten zurueck
		 * 
		 * @return Anzahl der Spalten
		 */
		public int getColumnCount() {
			return spalten.length;
		}

		/**
		 * gibt die Anzahl der Zeilen zurueck
		 * 
		 * @return Anzahl der Zeilen
		 **/
		public int getRowCount() {
			return dataO.length;
		}

		/**
		 * gibt die Namen der Spalten zurueck
		 * 
		 * @param col
		 *            die wieviele Spalte
		 * @return Name der Spalten
		 */
		public String getColumnName(int col) {
			return spalten[col];

		}

		/**
		 * gibt den Inhalt einer Zelle zurueck
		 * 
		 * @param row
		 *            in welcher Zeile
		 * @param col
		 *            von welcher Spalte
		 * @return Inhalt einer Zelle
		 */
		public Object getValueAt(int row, int col) {
			return dataO[row][col];

		}

		/**
		 * setzt neue Daten
		 * 
		 * @param value
		 *            Wertn, der sich geaendert hat
		 * @param row
		 *            Zeile
		 * @param col
		 *            Spalte
		 */
		public void setValueAt(Object value, int row, int col) {
			umwandeln(); // Vector wird in Object[][] umgewandelt

			fireTableStructureChanged(); // die Struktur der Tabelle wird
			// umgewandelt
			fireTableDataChanged(); // die Daten werden veraendert
		}// end setValue

	}// end myTabelModel

	/**
	 * setzt die veraenderten Daten
	 * 
	 * @param data
	 *            neuen Daten die angezeigt werden sollen
	 */
	public void setzeDaten(Vector data) {
		this.daten = data;

	}

	/**
	 * gibt eine JScrollpane mit einer Tabelle zurueck
	 * 
	 * @return pane Scrollpane mit der entsprechenden Tabelle
	 */
	public JScrollPane getTabelle() {

		pane = new JScrollPane(table);
		return pane;

	}

	/**
	 * wandelt einen Vector in ein Object[][] um
	 */
	public void umwandeln() {
		dataO = new Object[daten.size()][spalten.length]; // Zeilen der Tabelle
		// und die Anzahl
		// der Spalten

		for (int k = 0; k < daten.size(); k++) {
			zeile = new Vector();
			zeile = (Vector) daten.elementAt(k);// eine Zeile (Vector) des
			// Datenvector wird
			// herausgelesen
			for (int j = 0; j < zeile.size(); j++) {
				wort = zeile.elementAt(j).toString(); // alle Woerter der Zeile
				// werden herausgelesen

				dataO[k][j] = wort; // wort wird in das Object[][] gespeichert
			}
		}
	}// end umwandeln()

	/**
	 * gibt zurueck, ob genau eine Zeile markiert ist
	 * 
	 * @return true wenn genau eine Zeile markiert ist;false wenn entweder keine
	 *         oder mehrere Zeilen markiert sind
	 */
	public boolean welcheZeile() {
		rows = table.getSelectedRows(); // speicher die Nummern derer Zeilen,

		// die markiert sind in ein int[]

		// wenn mehrere Zeilen markiert sind
		if (rows.length > 1) {
			JOptionPane.showMessageDialog(this,
					"Bitte nur eine Zeile markieren");
			return false;
		}
		// wenn gar keine Zeile markiert ist
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this,
					"Bitte die zu bearbeitende Zeile markieren");
			return false;
		}

		return true;
	}

	/**
	 * gibt den Inhalt der Spalte Nummer in der markierten Zeile zurueck
	 * 
	 * @return nummer der gewaehlten Zeile
	 */
	public int getZeilennummer() {// Integer.parseInt = wandelt String in einen
		// Int um
		// table.getValue at = gibt Inhalt in einer bestimmten Zelle zurueck
		// wenn in der ersten Spalte keine Zahl steht (--> UT8), wird die
		// Methode getNummerZuSelbstNummer aufgerufen
		try {
			return (Integer.parseInt(table.getValueAt(rows[0], 0).toString()));
		} catch (NumberFormatException e) {
			return (di.getNummerZuSelbstNummer(table.getValueAt(rows[0], 0)
					.toString(), table.getValueAt(rows[0], 1).toString()));

		}

	}// end getZeilennummer

	/**
	 * gibt die Banfids der angewaehlten Banfpositionen zurueck
	 * 
	 * @param angeklickt
	 *            ausgewaehlte Banfpositionen
	 * @return Vector mit den banfids
	 */
	public Vector<Integer> getbanfIds(Vector angeklickt) {
		Vector<Integer> banf = new Vector<Integer>();

		for (int i = 0; i < angeklickt.size(); i++) {
			banf.add(Integer.parseInt(table.getValueAt(
					Integer.parseInt(angeklickt.get(i).toString()), 1)
					.toString()));
		}

		return banf;
	}

	public Vector<String> getMaterial(Vector angeklickt) {
		Vector<String> banf = new Vector<String>();

		for (int i = 0; i < angeklickt.size(); i++) {
			banf.add(table.getValueAt(
					Integer.parseInt(angeklickt.get(i).toString()), 4)
					.toString());
		}

		return banf;
	}

	/**
	 * gibt den Namen der Kostenstellen der Banfs zurueck
	 * 
	 * @param angeklickt
	 *            ausgewaehlte Banfpositionen
	 * @return Vector mit den Namen der Kostenstellen der gewaehlten
	 *         Banfpositionen
	 */
	public Vector<String> getKostenstelle(Vector angeklickt) {
		Vector<String> kostenstelle = new Vector<String>();

		for (int i = 0; i < angeklickt.size(); i++) {
			kostenstelle.add(table.getValueAt(
					Integer.parseInt(angeklickt.get(i).toString()), 3)
					.toString());
		}

		return kostenstelle;
	}

	/**
	 * gibt den Preis je Banfposition zurueck
	 * 
	 * @param angeklickt
	 *            ausgewaehlte Banfpositionen
	 * @return Vector mit dem Preis je Banfposition
	 */
	public Vector<Double> getPreisGes(Vector angeklickt) {
		Vector<Double> preis = new Vector<Double>();

		for (int i = 0; i < angeklickt.size(); i++) {
			preis.add(Double.parseDouble(table.getValueAt(
					Integer.parseInt(angeklickt.get(i).toString()), 10)
					.toString()));
		}

		return preis;
	}

	/**
	 * gibt einen Eintrag einer Zelle von der Tabelle zurueck
	 * 
	 * @param spalten
	 *            von welcher Spalte
	 * @return Inhalt der Zelle
	 */
	public String getDaten(int spalten) {

		return table.getValueAt(rows[0], spalten).toString();

	}

	/**
	 * gibt eine Tabelle ohne ScrollPane zurueck
	 * 
	 * @return table
	 */
	public JTable getNurTabelle() {
		return table;

	}

	/**
	 * gibt einen JTableHeader zurueck
	 * 
	 * @return JTableHeader
	 */
	public JTableHeader getHeader() {
		return header;

	}
}// end Klasse Tabelle

