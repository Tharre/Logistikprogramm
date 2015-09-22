package Budget;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import Logistik.Laden;

/**
 *In der Klasse Fenster wird ein JFrame, in dem verschiedene Tabellen
 * dargestellt werden, erstellt
 *<p>
 * Title: Fenster
 * 
 * @author Haupt, Liebhart
 **/
public class AnzAbfrage extends JFrame implements ActionListener {

	// sonstiges
	/** Tabelle **/
	private Tabelle table;
	/** Scrollpane fuer die Tabelle **/
	private JScrollPane scroll = new JScrollPane();
	/** Button um die Daten exportieren zu koennen **/
	private JButton exportieren = new JButton("in Excel exportieren");
	/** Objekt von der Klasse DatenExport **/
	private AbfrageExportExcel de;
	/** Vector mit den Daten, die in der Tabelle dargestellt werden sollen **/
	private Vector daten;
	/** Namen der Spalten der Tabelle **/
	private String[] spalten;

	// Connections
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	/** Connection zur Logistikdatenbank **/
	private Connection conL;

	/**
	 * Konstruktor
	 * 
	 * @param titel
	 *            Titel des Fensters
	 * @param spalten
	 *            Spaltennamen der Tabelle
	 * @param daten
	 *            Daten der Tabelle
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 */
	public AnzAbfrage(String titel, String[] spalten, Vector daten,
			Connection con, Connection conL) {
		super(titel);

		this.con = con;
		this.conL = conL;
		this.daten = daten;
		this.spalten = spalten;

		table = new Tabelle(spalten, daten, con, conL);
		scroll = table.getTabelle();
		add(scroll, BorderLayout.CENTER);
		add(exportieren, BorderLayout.SOUTH);

		exportieren.addActionListener(this);
	}

	private Laden load;
	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == exportieren) {
			load = new Laden();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					de = new AbfrageExportExcel(con, conL, daten, spalten);
					de.pack();
					de.setLocationRelativeTo(null);
					de.setVisible(true);
					load.dispose();
				}
			});
		}
	}

}
