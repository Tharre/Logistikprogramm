package Budget;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *In der Klasse EDVHAT wird der EDV- HAT- Anteil (EDV- Haustechnik- Anteil) vom
 * Benutzer eingetragen und in die Datenbank gespeichert
 *<p>
 * Title: EDVHAT
 * 
 * @author Haupt, Liebhart 
 **/
public class BezEDVHAT extends JFrame implements ActionListener, KeyListener {

	// sonstiges
	/** Connection zur Budgetdatenbank **/
	private Connection con;
	/** Button ok um die Anteile in die Datenbank zu speichern **/
	private JButton ok = new JButton("OK");
	/** Felder fuer die Eingabe **/
	private JTextField[] felder = new JTextField[2];
	/** Statement **/
	private Statement stmt;
	/**
	 * Hilfsvariabel, die Auskunft gibt, ob es einen Fehler bei der Eingabe
	 * gegeben hat
	 **/
	private boolean keinFehler;
	/** Objekt von der Klasse Abteilungsanteile **/
	private BezAbteilungsanteile aa;

	// double
	/** HAT- Anteil in Prozent **/
	private double prozentHAT;
	/** EDV- Anteil in Prozent **/
	private double prozentEDV;

	// String
	/** Befehl 1 **/
	private String query;
	/**
	 * EDV und HAT Anteil in Prozent mit einem Beistrich getrennt (EDV- Anteil,
	 * HAT- Anteil)
	 **/
	private String anteile;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 */
	public BezEDVHAT(Connection con, Connection conL) {
		super("EDV-HAT- Anteil");
		this.con = con;

		setLayout(new GridLayout(3, 2));
		ok.addActionListener(this);
		ok.addKeyListener(this);
		ok.setToolTipText("Speichern des EDV-HAT- Anteils");

		// in der Klasse Abteilungsanteile werden die genauen Anteile fuer die
		// Abteilungen berechnet und in die Datenbank gespeichert
		aa = new BezAbteilungsanteile(con, conL);

		felder[0] = new JTextField();
		felder[1] = new JTextField();

		add(new JLabel("EDV Anteil in Prozent:"));
		add(felder[0]);
		add(new JLabel("HAT Anteil in Prozent:"));
		add(felder[1]);
		add(ok);

	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		keinFehler = true;
		try {

			prozentEDV = Double.parseDouble(felder[0].getText());
			prozentHAT = Double.parseDouble(felder[1].getText());

			if (prozentEDV > 100 || prozentEDV < 0 || prozentHAT > 100
					|| prozentHAT < 0) {
				JOptionPane.showMessageDialog(null,
						"Sie müssen eine Zahl zwischen 0 und 100 eingeben!");
				keinFehler = false;

			}

		} catch (NumberFormatException f) {
			JOptionPane.showMessageDialog(null,
					"Bitte geben Sie nur Zahlen ein.");
			keinFehler = false;

		}

		if (keinFehler) {
			anteile = "" + prozentEDV + "," + prozentHAT;

			try {
				stmt = con.createStatement();

				query = "update abteilungut3 set edvhatAnteil='" + anteile
						+ "' where nummer=1";

				stmt.execute(query);

				aa.berechneAnteileUT3();

			} catch (SQLException e1) {
				System.out
						.println("FEHLER beim Speichern des EDV- HAT- Anteils in die Datenbank");
				e1.printStackTrace();
			}

		}
		dispose();
	}

	public void keyPressed(KeyEvent k) {
		
	if(k.getKeyCode()==10)
	{keinFehler = true;
	try {

		prozentEDV = Double.parseDouble(felder[0].getText());
		prozentHAT = Double.parseDouble(felder[1].getText());

		if (prozentEDV > 100 || prozentEDV < 0 || prozentHAT > 100
				|| prozentHAT < 0) {
			JOptionPane.showMessageDialog(null,
					"Sie müssen eine Zahl zwischen 0 und 100 eingeben!");
			keinFehler = false;

		}

	} catch (NumberFormatException f) {
		JOptionPane.showMessageDialog(null,
				"Bitte geben Sie nur Zahlen ein.");
		keinFehler = false;

	}

	if (keinFehler) {
		anteile = "" + prozentEDV + "," + prozentHAT;

		try {
			stmt = con.createStatement();

			query = "update abteilungut3 set edvhatAnteil='" + anteile
					+ "' where nummer=1";

			stmt.execute(query);

			aa.berechneAnteileUT3();

		} catch (SQLException e1) {
			System.out
					.println("FEHLER beim Speichern des EDV- HAT- Anteils in die Datenbank");
			e1.printStackTrace();
		}

	}
	dispose();
		
	}
		
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
