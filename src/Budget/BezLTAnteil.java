package Budget;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *In der Klasse LTAnteil wird der LT eingetragen und in die Datenbank gespeichert
 *<p>
 * Title: LTAnteil
 * 
 * @author Haupt Marianne, Liebhart Stefanie
 **/

public class BezLTAnteil extends JFrame implements ActionListener {
	
	/** Button ok, um den Eintrag zu speichern **/
	private JButton ok = new JButton("OK");
	
	/** Textfeld fuer die Eingabe **/
	private JTextField feld = new JTextField();
	
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	
	/** Kennummer der Tabelle **/
	private int kennnummer;

	/** Anteil in Prozent **/
	private double prozent;

	/** Statement **/
	private Statement stmt;

	/** Befehl 1 **/
	private String query;
	
	/**Hilfsvariabel**/
	private boolean keinFehler;
	
	/**Objekt von der Klasse Abteilungsanteile**/
	private BezAbteilungsanteile aa;

	/**
	 * Konstruktor
	 * 
	 * @param kennnummer
	 *            Kennnummer, um welche Tabelle es sich handelt
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 */
	public BezLTAnteil(int kennnummer, Connection con, Connection conL) {
		super("LT- Anteil");
		this.con = con;
		this.kennnummer = kennnummer;

		aa = new BezAbteilungsanteile(con, conL);

		ok.addActionListener(this);
		ok.setToolTipText("Ändern des Lebensmittelanteiles");

		setLayout(new GridLayout(2, 2));
		add(new JLabel("LT- Anteil in Prozent:"));
		add(feld);
		add(ok);

	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {

		try {

			prozent = Double.parseDouble(feld.getText());
			keinFehler = true;
			if (prozent > 100 || prozent < 0) {
				JOptionPane.showMessageDialog(null,
						"Sie müssen eine Zahl zwischen 0 und 100 eingeben.");
				keinFehler = false;

			}

		} catch (NumberFormatException f) {
			JOptionPane.showMessageDialog(null,
					"Bitte geben Sie nur Nummern ein.");
			keinFehler = false;

		}
		if (keinFehler) {

			try {
				stmt = con.createStatement();
				if (kennnummer == 9)
					query = "update lmb set festgeplant=" + prozent
							+ " where nummer=2";
				else
					query = "update lmb set festgeplant=" + prozent
							+ " where nummer=1";

				stmt.execute(query);

				aa.berechneAnteileLMB(kennnummer);

			} catch (SQLException e1) {
				System.out.println("FEHLER");
				e1.printStackTrace();
			}

		}
		dispose();
	}

}
