package Logistik;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;

/**
 * Importieren einer Textdatei. Pfad muss angegeben werden. Erfolgsmeldung, wenn
 * erfolgreich
 */
public class DBImport extends LayoutMainPanel implements ActionListener {

	/**
	 * Button zum Durchführen des Imports
	 */
	public JButton importB = new JButton("importieren");

	/**
	 * Überschrift
	 */
	private JLabel titel = new JLabel("DATENBANK IMPORTIEREN");

	public DBImport(UserImport user) {
		super(user);

		titel.setFont(new Font("Arial", Font.BOLD, 20));

		setLayoutM(new BorderLayout());
		addM(titel, BorderLayout.NORTH);
		addM(importB, BorderLayout.CENTER);

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

					con.mysql_update(sql);

					anz++;
				}

				br.close();

				JOptionPane.showMessageDialog(null,
						"Importvorgang erfolgreich durchgeführt!");

			} catch (Exception ex) {
				ex.getMessage();
			}

		}// if

	}// actionPerformed

}