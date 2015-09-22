package Logistik;

import java.awt.*;
import javax.swing.*;

import java.io.File;
import java.sql.*;
import java.awt.event.*;
import javax.swing.filechooser.FileFilter;

/*
 * Fenster, welches die Abfrageergebnisse in einer Tabelle anzeigt
 * und eine exportfunktion in Excel ermöglicht
 */
public class AnzTabelleB extends JFrame implements ActionListener {
	public JTable table;
	public AnzTabelleA table3;
	public JButton export = new JButton("exportieren");

	public AnzTabelleB(ResultSet r, String[] sn) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(600, 500);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		ResultSet rs = r;
		String[] spaltennamen = sn;
		String[][] daten;

		int anz = 0;
		try {
			rs.last();
			while (rs.previous()) {
				anz++;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		daten = new String[anz + 1][spaltennamen.length];
		try {
			int i = 0;
			while (rs.next()) {
				for (int j = 0; j < spaltennamen.length; j++) {
					daten[i][j] = rs.getString(spaltennamen[j]);
				}
				i++;
			}

			rs.close();
			r.close();

		} catch (Exception e) {
			e.getMessage();
		}

		table = new JTable(daten, spaltennamen);

		JScrollPane scrollpane = new JScrollPane(table);
		c.add(scrollpane, BorderLayout.CENTER);
		c.add(export, BorderLayout.SOUTH);
		export.addActionListener(this);
		this.repaint();
	}// TabelleAnzeigen

	public AnzTabelleB(ResultSet r, String[] sn, String[] s) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(600, 500);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		ResultSet rs = r;
		String[] spaltennamen = sn;
		String[][] daten;

		int anz = 0;
		try {
			rs.last();
			while (rs.previous()) {
				anz++;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		daten = new String[anz + 1][spaltennamen.length];
		try {
			int i = 0;
			while (rs.next()) {
				for (int j = 0; j < spaltennamen.length; j++) {
					daten[i][j] = rs.getString(spaltennamen[j]);
				}
				i++;
			}

			rs.close();
			r.close();
		} catch (Exception e) {
			e.getMessage();
		}
		table = new JTable(daten, s);

		JScrollPane scrollpane = new JScrollPane(table);
		c.add(scrollpane, BorderLayout.CENTER);
		c.add(export, BorderLayout.SOUTH);
		export.addActionListener(this);
		this.repaint();
	}

	public AnzTabelleB(String[] sn, String[] s, Class[] cl, ResultSet r) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(600, 500);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		table3 = new AnzTabelleA(sn, s, cl, r, 1);

		JScrollPane scrollpane = new JScrollPane(table3);
		c.add(scrollpane, BorderLayout.CENTER);
		c.add(export, BorderLayout.SOUTH);
		export.addActionListener(this);
		this.repaint();
	}

	public AnzTabelleB(String[] sn, String[] s, Class[] cl, ResultSet r,
			DBConnection con) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(600, 500);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		table3 = new AnzTabelleA(sn, s, cl, r, 1);

		JScrollPane scrollpane = new JScrollPane(table3);
		c.add(scrollpane, BorderLayout.CENTER);
		c.add(export, BorderLayout.SOUTH);
		export.addActionListener(this);
		this.repaint();
	}

	public AnzTabelleB(String[] sn, String[] s, Class[] cl, ResultSet r,
			String title) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(600, 500);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		table3 = new AnzTabelleA(sn, s, cl, r, 1);

		JScrollPane scrollpane = new JScrollPane(table3);
		c.add(scrollpane, BorderLayout.CENTER);
		c.add(export, BorderLayout.SOUTH);
		export.addActionListener(this);
		this.setTitle(title);
		this.repaint();
	}
	
	private String pfad = null;

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == export) {
			JFileChooser fc = new JFileChooser();

			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setSelectedFile(new File("Abfragen.xls"));
			fc.setDialogTitle("exportieren");
			fc.setApproveButtonText("exportieren");
			fc.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.isDirectory()
							|| f.getName().toLowerCase().endsWith(".xls");
				}

				@Override
				public String getDescription() {
					return "Excel";
				}
			});

			int state = fc.showOpenDialog(null);

			if (state == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				pfad = file.getAbsolutePath();
				if (!file.getName().toLowerCase().endsWith(".xls")) {
					pfad += ".xls";
				}
				final Laden l = new Laden();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {

							String excelFileName = pfad;
							// excelFileName += "Abfragen.xls";
							Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
							String excelDatabase = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=";
							excelDatabase += excelFileName.trim()
									+ ";DriverID=278;READONLY=false}";

							Connection excelCon = DriverManager
									.getConnection(excelDatabase);
							if (!excelCon.isClosed()) {
								System.out
										.println("Successfully Connected To Excel !!");
							}

							Statement excelStat;
							excelStat = excelCon.createStatement();

							String create = "create table abfragen2 ( ";
							for (int i = 0; i < table3.spalten.length; i++) {
								create += table3.spalten[i] + " Varchar(100) ";
								if (i < table3.spalten.length - 1) {
									create += ", ";
								}
							}
							create += " );";
							excelStat.executeUpdate(create);

							for (int rowI = 0; rowI < table3.getRowCount(); rowI++) {
								String sqlInsert = "Insert into abfragen2 values(";
								for (int i = 0; i < table3.spalten.length; i++) {
									sqlInsert += "'"
											+ table3.getValueAt(rowI, i) + "'";
									if (i < table3.spalten.length - 1) {
										sqlInsert += ", ";
									}
								}
								sqlInsert += ");";
								excelStat.executeUpdate(sqlInsert);

							}
							excelStat.close();
							excelCon.close();

							JOptionPane.showMessageDialog(null,
									"Export abgeschlossen!");

						} catch (Exception e1) {
							e1.getMessage();
							e1.printStackTrace();
						}
						l.dispose();
					}
				});

			} else {
				System.out.println("Auswahl abgebrochen");
			}

		}// export
	}// actionPerformed

}