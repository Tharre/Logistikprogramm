package Logistik;

import javax.swing.*;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.text.*;
import javax.swing.table.*;

import java.awt.*;

public class AnzTabelleA extends JTable {
	private LayoutModel model;
	private Class[] classes;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private String[] titles;
	public String[] spalten;
	private DBConnection con;
	private Object[][] dataO;
	private TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
	private int kennung;

	public AnzTabelleA(String[] spalten, String[] titles, Class[] classes,
			ResultSet rs, DBConnection con, int kennung) {

		this.titles = titles;
		this.spalten = spalten;
		this.con = con;
		this.kennung = kennung;
		this.classes = classes;

		model = new LayoutModel(titles, classes);
		setModel(model);

		getDataI(spalten, rs, titles);

		TableCellRenderer renderer = new ColorRenderer();
		setDefaultRenderer(Object.class, renderer);
		setDefaultRenderer(Integer.class, renderer);
		setDefaultRenderer(Double.class, renderer);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
		setAutoCreateRowSorter(true);
		// Der Sorter muss dem JTable bekannt sein
		setRowSorter(sorter);

		// ... und der Sorter muss wissen, welche Daten er sortieren muss
		sorter.setModel(model);

	}

	public AnzTabelleA(String[] spalten, String[] titles, Class[] classes,
			ResultSet rs, int kennung) { // kennung=0, Bestellungen anzeigen
		this.titles = titles;
		this.spalten = spalten;
		this.classes = classes;
		this.kennung = kennung;

		model = new LayoutModel(titles, classes);
		setModel(model);

		getData(spalten, rs);
		TableCellRenderer renderer = new ColorRenderer();
		setDefaultRenderer(Object.class, renderer);
		setDefaultRenderer(Integer.class, renderer);
		setDefaultRenderer(Double.class, renderer);

		setAutoCreateRowSorter(true);
		// Der Sorter muss dem JTable bekannt sein
		setRowSorter(sorter);

		// ... und der Sorter muss wissen, welche Daten er sortieren muss
		sorter.setModel(model);

	}

	public void addRow(LayoutRow r) {
		model.addRow(r);
	}

	public void getDataI(String[] spalten, ResultSet rs, String[] titles) {
		try {
			while (rs.next()) {
				LayoutRow r = new LayoutRow();
				for (int i = 0; i < titles.length; i++) {
					if (titles[i] == "Bundesnr") {
						r.add(bundesNr(rs.getInt(spalten[i])));
					} else if (rs.getString(spalten[i]) == null) {
						r.add("");
					} else if (classes[i] == Date.class) {
						r
								.add(sdf.format(new Date(
										rs.getLong(spalten[i]) * 1000)));
					} else if (classes[i] == Boolean.class) {
						r.add(rs.getBoolean(spalten[i]));
					} else {
						r.add(rs.getString(spalten[i]));
					}
				}
				addRow(r);
			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getData(String[] spalten, ResultSet rs) {
		Vector vec = new Vector();
		Vector zeile;

		try {
			while (rs.next()) {
				LayoutRow r = new LayoutRow();

				Vector vRow = new Vector();

				for (int i = 0; i < titles.length; i++) {
					vRow = new Vector();

					if (i < spalten.length) {
						if (rs.getString(spalten[i]) == null) {
							r.add("");
							// vRow.add("");
						} else if (classes[i] == Date.class) {
							r.add(sdf.format(new Date(
									rs.getLong(spalten[i]) * 1000)));
							// vRow.add(sdf.format(new
							// Date(rs.getLong(spalten[i])*1000)));
						} else if (classes[i] == Boolean.class) {
							r.add(rs.getBoolean(spalten[i]));
							// vRow.add(rs.getBoolean(spalten[i]));
						
						} else if (kennung == 2 && i == 4) {
							int status = Integer.parseInt(rs
									.getString(spalten[i]));
							if (status == 3) {
								r.add("nicht bestellt");
							} else if (status == 4) {
								r.add("in Bearbeitung");
							} else if (status == 5) {
								r.add("fertig");
							} else if (status == 6) {
								r.add("abgewiesen");
							}

						} else if (kennung == 0 && (i == 5 || i == 6)) {

							if (i == 5) {
								int status = Integer.parseInt(rs
										.getString(spalten[i]));

								if (status == 7) {
									r.add("richtig geliefert");
								} else if (status == 1) {
									r.add("nicht abgeschickt");
								} else if (status == 3) {
									r.add("abgeschickt");
								} else if (status == 15) {
									r.add("gelöscht");
								} else if (status == 4) {
									r.add("teilweise falsch/nicht geliefert");
								} else if (status == 6) {
									r.add("komplett - Teile nicht lieferbar");
								} else if (status == 5) {
									r.add("teilweise richtig geliefert");
								}

							}
							if (i == 6) {
								int statusb = Integer.parseInt(rs
										.getString(spalten[i]));
								if (statusb == 0) {
									r.add("keine Lieferung");
								} else if (statusb == 1) {
									r.add("teilweise geliefert");
								} else if (statusb == 2) {
									r.add("komplett geliefert");
								} else if (statusb == 3) {
									r.add("teilweise bezahlt");
								} else if (statusb == 4) {
									r.add("komplett bezahlt");
								}

							}

						} else {
							r.add(rs.getString(spalten[i]));
						}

						// vRow.add(rs.getString(spalten[i]));
					} else {
						r.add(new Boolean(false));
						// vRow.add(new Boolean(false));
					}
				}

				for (int i = 0; i < spalten.length; i++) {
					vRow.add(rs.getObject(spalten[i]));

				}

				vec.add(vRow);
				addRow(r);
			}

			dataO = new Object[vec.size()][spalten.length]; // Zeilen der
			// Tabelle
			// und die Anzahl
			// der Spalten

			for (int k = 0; k < vec.size(); k++) {
				zeile = new Vector();
				zeile = (Vector) vec.elementAt(k);// eine Zeile (Vector) des
				// Datenvector wird
				// herausgelesen

				for (int j = 0; j < zeile.size(); j++) {
					String wort = zeile.elementAt(j).toString(); // alle Woerter
					// der Zeile
					// werden herausgelesen
					dataO[k][j] = wort; // wort wird in das Object[][]
					// gespeichert

				}
			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LayoutRow[] getRows() {
		return model.getRows();
	}

	public Object[] getKlicked(String id, String cb) {
		ArrayList al = new ArrayList();
		int index = model.indexOf(id);
		int cbIndex = model.indexOf(cb);

		for (int i = 0; i < model.getRowCount(); i++) {
			if (getValueAt(i, cbIndex) == Boolean.TRUE) {
				al.add(getValueAt(i, index));
			}
		}
		Object[] ar = new Object[al.size()];
		for (int i = 0; i < al.size(); i++) {
			ar[i] = al.get(i);
		}
		return ar;
	}

	public Object[] getAllKlicked(String id, String cb) {
		ArrayList al = new ArrayList();
		int index = model.indexOf(id);
		int cbIndex = model.indexOf(cb);

		for (int i = 0; i < model.getRowCount(); i++) {
			if (getValueAt(i, cbIndex) == Boolean.TRUE) {
				al.add(getValueAt(i, index));
			} else {
				al.add(-1);
			}
		}
		Object[] ar = new Object[al.size()];
		for (int i = 0; i < al.size(); i++) {
			ar[i] = al.get(i);
		}
		return ar;
	}

	public String[] getEingabe(String id) {
		ArrayList al = new ArrayList();
		int index = model.indexOf(id);
		for (int i = 0; i < model.getRowCount(); i++) {
			al.add(getValueAt(i, index));
		}
		String[] ar = new String[al.size()];
		for (int i = 0; i < al.size(); i++) {
			ar[i] = al.get(i).toString();
		}
		return ar;
	}

	public String bundesNr(int bid) {
		try {
			String query = "Select * From bundesnr where id =" + bid;
			ResultSet rs = con.mysql_query(query);
			rs.next();
			String s = "" + rs.getInt("nr");
			int uid = rs.getInt("uebergruppe");
			while (!rs.wasNull()) {
				query = "Select * From bundesnr where id =" + uid;
				rs = con.mysql_query(query);
				rs.next();
				s += "." + rs.getInt("nr");
				uid = rs.getInt("uebergruppe");
			}
			rs.close();
			return s;
		} catch (Exception e) {
		}

		return "";
	}

	class ColorRenderer extends JLabel implements TableCellRenderer {
		public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (model.getColumnClass(column) == JButton.class) {
				return (Component) value;
			}
			Component renderer = DEFAULT_RENDERER
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
			((JLabel) renderer).setOpaque(true);
			Color foreground;
			Color c = model.getColor(row, column);
			if (isSelected) {
				c.darker();
			} else {
			}
			foreground = Color.BLACK;
			((JLabel) renderer).setForeground(foreground);
			// ((JLabel) renderer).setBackground(background);
			if (value != null) {
				((JLabel) renderer).setText(value.toString());
			}
			row = sorter.convertRowIndexToModel(row);

			try {

				if (dataO[row].length > 10)// BestPos bei Bezahlung
				{
					if (Integer.parseInt(dataO[row][7].toString()) == 3
							|| Integer.parseInt(dataO[row][7].toString()) == 6) {
						((JLabel) renderer).setBackground(Color.GREEN);
					} else if (Integer.parseInt(dataO[row][7].toString()) == 4) {
						((JLabel) renderer).setBackground(Color.YELLOW);

					} else {
						((JLabel) renderer).setBackground(Color.WHITE);
					}

				} else if (dataO[row].length > 6)// Bestellungen
				{

					if (Integer.parseInt(dataO[row][5].toString()) == 1
							|| Integer.parseInt(dataO[row][5].toString()) == 3) {
						((JLabel) renderer).setBackground(new Color(245, 135,
								129));
					} else if ((Integer.parseInt(dataO[row][5].toString()) == 4)
							|| Integer.parseInt(dataO[row][5].toString()) == 5) {
						((JLabel) renderer).setBackground(Color.YELLOW);
					} else if ((Integer.parseInt(dataO[row][5].toString()) == 7)
							|| Integer.parseInt(dataO[row][5].toString()) == 6) {
						((JLabel) renderer).setBackground(Color.GREEN);
					} else if (Integer.parseInt(dataO[row][5].toString()) == 15) {
						((JLabel) renderer).setBackground(new Color(151, 217,
								236));
					} else {
						((JLabel) renderer).setBackground(Color.WHITE);
					}

				}// if else// Banfs

				else if (dataO[row].length > 5) {

					if (Integer.parseInt(dataO[row][4].toString()) == 6) {
						((JLabel) renderer).setBackground(new Color(245, 135,
								129));
					} else if (Integer.parseInt(dataO[row][4].toString()) == 4
							|| Integer.parseInt(dataO[row][4].toString()) == 3) {
						((JLabel) renderer).setBackground(Color.YELLOW);
					} else if (Integer.parseInt(dataO[row][4].toString()) == 5) {
						((JLabel) renderer).setBackground(Color.GREEN);
					} else if (Integer.parseInt(dataO[row][4].toString()) == 15) {
						((JLabel) renderer).setBackground(new Color(151, 217,
								236));
					} else {
						((JLabel) renderer).setBackground(Color.WHITE);
					}

				}// else
			} catch (NumberFormatException e) {
				((JLabel) renderer).setBackground(Color.WHITE);

			}// catch

			if (isSelected) {
				((JLabel) renderer).setBackground(new Color(153, 217, 234));
			}

			return renderer;
		}
	}

	public void aktualisieren() {
		this.repaint();
	}
}
