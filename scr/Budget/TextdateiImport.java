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
 * Importieren einer Textdatei. Pfad muss angegeben werden. Erfolgsmeldung, wenn
 * erfolgreich
 */
public class TextdateiImport extends JFrame implements ActionListener {

	/**
	 * Button zum Durchführen des Imports
	 */
	public JButton importB = new JButton("importieren");
	private Connection con;
	private Connection conB_alt;
	/**
	 * 1=Import zum Jahresende; 0 = Import
	 */
	private int kennung;
	private Jahresende je;

	/**
	 * Überschrift
	 */
	private JLabel titel = new JLabel("DATENBANK IMPORTIEREN");

	public TextdateiImport(Connection con, int kennung, Jahresende je) {
		this.con = con;
		this.kennung = kennung;
		this.je = je;

		titel.setFont(new Font("Arial", Font.BOLD, 20));
		setLayout(new BorderLayout());
		add(titel, BorderLayout.NORTH);
		add(importB, BorderLayout.CENTER);

		importB.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == importB) {
			try {

				String pfad = "";

				JFileChooser fc = new JFileChooser();

				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setSelectedFile(new File(""));
				fc.setDialogTitle("Öffnen");
				fc.setApproveButtonText("Öffnen");
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

				BufferedReader br = new BufferedReader(new FileReader(new File(
						pfad)));

				String s;
				String sql = "";
				int anz = 0;
				while ((s = br.readLine()) != null) {
					sql = s;

					Statement stmt = con.createStatement();
					stmt.executeUpdate(sql);

					anz++;
				}

				br.close();


				JOptionPane.showMessageDialog(null,
						"Importvorgang erfolgreich durchgeführt!");
				dispose();
				
				if(kennung==1)
				{
					je.betraegeLoeschen();
				}

			} catch (Exception ex) {
				System.out.println("ERROR imp: " + ex.getMessage());
				JOptionPane.showMessageDialog(null,
				"Fehler! Daten konnten nicht importiert werden!");
				dispose();
			}

		}// if

	}// actionPerformed

}