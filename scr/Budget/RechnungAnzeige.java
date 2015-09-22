package Budget;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


/**
 *In der Klasse RechnungsAnzeige wird ein JFrame, mit den Inhalten der Rechnung,
 * erstellt 
 *<p>
 * Title: RechnungsAnzeige
 * 
 * @author Haupt, Liebhart
 * 
 **/
public class RechnungAnzeige extends JFrame implements ActionListener {

	// Label[]
	/** Labels mit den Hinweisen **/
	private JLabel[] namenL;
	/** Label[] mit den ausgelesenen Daten **/
	private JLabel[] ausgeleseneDaten;

	// sonstiges
	/** Hinweise fuer die Darstellung **/
	private String[] namen = { "W-Nummer", "Bestellbetrag", "Rechnungsbetrag",
			"externe Nummer", "interne Nummer", "Inventarnummer",
			"Buchhaltungsbelege", "Sonderabzug", "Skonto", "Zahlart","Zahlbetrag",
			"Rechnungsstatus" };
	/** Button um das PDF zu oeffnen **/
	private JButton pdfoeffnen = new JButton("PDF öffnen");
	/** Vector mit den Daten der Rechnung **/
	private Vector daten;
	

	/**
	 * Konstruktor
	 * 
	 * @param daten
	 *            welche Daten angezeigt werden sollen
	 */
	public RechnungAnzeige(Vector daten) {
		super("Rechnung");

		this.daten = daten;
		
		setLayout(new GridLayout(13, 2));

		namenL = new JLabel[namen.length];
		ausgeleseneDaten = new JLabel[daten.size()];

		for (int i = 0; i < ausgeleseneDaten.length - 1; i++) {
			ausgeleseneDaten[i] = new JLabel((daten.get(i + 1)).toString(),
					JLabel.CENTER);

			namenL[i] = new JLabel(namen[i]);

			if (i % 2 != 0) {
				ausgeleseneDaten[i].setBackground(Color.ORANGE);
				ausgeleseneDaten[i].setOpaque(true);

				namenL[i].setBackground(Color.orange);
				namenL[i].setOpaque(true);

			}

			namenL[i].setFont(new Font("SansSerif", Font.BOLD, 15));
			add(namenL[i]);
			add(ausgeleseneDaten[i]);

		}

		add(pdfoeffnen);

		pdfoeffnen.addActionListener(this);
	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {

		String pfad = auslesenDatei();
		try {
			Runtime.getRuntime().exec(
					"rundll32 url.dll,FileProtocolHandler "
							+ pfad + daten.get(1).toString() + ".pdf");
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
	}
	
	private String auslesenDatei() {
		String s;
		String text = "";

		Reader r_in;
		try {
			r_in = new FileReader(
					new File(
							"C:/Projekt_Textdateien/PfadRechnungen.txt"));

			BufferedReader br = new BufferedReader(r_in);

			try {
				while ((s = br.readLine()) != null)
					text = s;

				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Fehler", "Fehler beim Öffnen der Textdatei 'PfadRechnungen.txt'!", JOptionPane.ERROR_MESSAGE);

		}

		return text;

	}

}
