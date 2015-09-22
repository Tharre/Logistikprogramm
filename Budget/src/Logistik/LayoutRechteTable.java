package Logistik;

import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.text.*;
import javax.swing.table.*;
import java.awt.*;

public class LayoutRechteTable extends JTable {
	private LayoutModel model;
	private Class[] classes;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private String[] titles;

	public LayoutRechteTable(String[] spalten, String[] titles, Class[] classes,
			ResultSet rs, String[] r, int z) {
		this.titles = titles;
		model = new LayoutModel(titles, classes);
		setModel(model);
		this.classes = classes;
		if (spalten == null) {
			getData(r);
		} else if (z == 0) {
			getData(spalten, rs, z, r);
		} else if (z == -1) {
			getData(spalten, rs, r);
		} else {
			getData(spalten, rs, r, z);
		}

		TableCellRenderer renderer = new ColorRenderer();
		setDefaultRenderer(Object.class, renderer);
		setDefaultRenderer(Integer.class, renderer);
		setDefaultRenderer(Double.class, renderer);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();

		// Der Sorter muss dem JTable bekannt sein
		setRowSorter(sorter);

		// ... und der Sorter muss wissen, welche Daten er sortieren muss
		sorter.setModel(model);
	}

	public void addRow(LayoutRow r) {
		model.addRow(r);
	}

	public void getData(String[] spalten, ResultSet rs, String[] rechte) {
		AnzRechteB re = new AnzRechteB();
		try {
			while (rs.next()) {
				LayoutRow r = new LayoutRow();
				for (int i = 0; i < titles.length; i++) {
					if (titles[i] == "Recht") {
						r.add(rechte[rs.getInt("recht")]);
					/*} else if (titles[i] == "ID Recht") {
						r.add(re.getRechtId(rechte[rs.getInt("recht")]));*/
					} else if (classes[i] == String.class) {
						r.add(rs.getString(spalten[i]));
					} else {
						r.add(rs.getInt(spalten[i]));
					}
				}
				addRow(r);
			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getData(String[] spalten, ResultSet rs, String[] rechte, int z) {
		try {
			while (rs.next()) {
				LayoutRow r = new LayoutRow();
				for (int i = 0; i < titles.length; i++) {
					if (i < spalten.length) {
						if (rs.getString(spalten[i]) == null) {
							r.add("");
						} else if (classes[i] == Date.class) {
							r.add(sdf.format(new Date(
									rs.getLong(spalten[i]) * 1000)));
						} else if (classes[i] == Boolean.class) {
							r.add(rs.getBoolean(spalten[i]));
						} else {
							r.add(rs.getString(spalten[i]));
						}
					} else {
						r.add(rechte[z]);
					}
				}
				addRow(r);
			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getData(String[] spalten, ResultSet rs, int z, String[] rechte) {
		try {
			while (rs.next()) {
				LayoutRow r = new LayoutRow();
				for (int i = 0; i < titles.length; i++) {
					if (i < spalten.length) {
						if (titles[i] == "Recht") {
							r.add(z);
						} else if (rs.getString(spalten[i]) == null) {
							r.add("");
						} else if (classes[i] == Date.class) {
							r.add(sdf.format(new Date(
									rs.getLong(spalten[i]) * 1000)));
						} else if (classes[i] == Boolean.class) {
							r.add(rs.getBoolean(spalten[i]));
						} else {
							r.add(rs.getString(spalten[i]));
						}
					}// else r.add(rechte[rs.getInt(spalten[i])]);
				}
				addRow(r);
			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getData(String[] rechte) {
		for (int j = 0; j < rechte.length; j++) {
			LayoutRow r = new LayoutRow();
			for (int i = 0; i < titles.length; i++) {
				if (classes[i] == String.class) {
					r.add(rechte[j]);
				} else if (classes[i] == Integer.class) {
					r.add(j);
				}

			}
			addRow(r);
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

	class ColorRenderer extends JLabel implements TableCellRenderer {
		public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			// if(model.getColumnClass(column)==JButton.class)
			// return (Component) value;
			Component renderer = DEFAULT_RENDERER
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
			((JLabel) renderer).setOpaque(true);
			Color foreground, background;
			Color c = model.getColor(row, column);
			if (isSelected) {
				background = c.darker();
			} else {
				background = c;
			}
			foreground = Color.BLACK;
			((JLabel) renderer).setForeground(foreground);
			((JLabel) renderer).setBackground(background);
			if (value != null) {
				((JLabel) renderer).setText(value.toString());
			}
			return renderer;

		}

	}

}