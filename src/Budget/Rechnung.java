package Budget;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import javax.swing.SwingUtilities;

/**
 *In der Klasse Rechnung wird das Layout fuer das "Rechnung suchen" erstellt
 *<p>
 * Title: Rechnung
 * 
 * @author Haupt, Liebhart
 **/
public class Rechnung extends JPanel implements ActionListener {
	// Labels
	/** Nach was gesucht werden soll **/
	private JLabel hinweis;
	/** Aufforderung, dass man die Nummer eingeben soll **/
	private JLabel aufforderung;

	// int
	/** die eingegebene Nummer **/
	private int nummer = 0;
	/**
	 * nach welcher nummer gesucht werden soll (wNummer, externe Nummer,
	 * Inventarnummer)
	 **/
	private int nachWas = 0;

	// sonstiges
	/** ob die eingegebenen Zahl wirklich eine Zahl ist (dann true) **/
	private boolean istZahl;
	/** Feld, in dem die gesuchte Nummer eingegeben wird **/
	private JTextField rechnungsnummer;
	/** Objekt von der Klasse RechnungsAnzeige **/
	private RechnungAnzeige ra;
	private RechnungKorrigieren rk;
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;
	/** Query fuer die Datenbank **/
	private String query;

	// Vector
	/** Vector auf dem die ausgelesenen Daten gespeichert werden **/
	private Vector daten = new Vector();
	/** Hilfsvector fuer einzelne Datensaetze **/
	private Vector row = new Vector();

	// Buttons
	/** ButtonGroup fuer die RadioButtons **/
	private ButtonGroup bg = new ButtonGroup();
	/** Button suchen **/
	private JButton suchen = new JButton("Suchen");
	private JButton korrigieren = new JButton("Rechnung korrigieren");
	/** RadioButton, wenn nach der wNummer gesucht werden soll **/
	private JRadioButton wNummer = new JRadioButton("W- Nummer");
	/** RadioButton, wenn nach der Inventarnummer gesucht werden soll **/
	private JRadioButton inventarnummer = new JRadioButton("Inventarnummer");
	/** RadioButton, wenn nach der externen Nummer gesucht werden soll **/
	private JRadioButton externeNummer = new JRadioButton("externe Nummer");
	private JRadioButton bhBeleg = new JRadioButton("BH- Beleg");
	/** Button um den Pfad, wo die Rechnungen liegen zu ändern **/
	private JButton pfadEingeben = new JButton(
			"Ort, wo Rechnungen abgespeichert sind, ändern");

	private Connection con;
	private Connection conL;
	boolean k;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Datenbank Budget
	 * @param conL
	 *            Connection zur Datenbank Logistik
	 */
	public Rechnung(Connection con, Connection conL, int nummer) {

		this.con = con;
		this.conL = conL;

		di = new DatenImport(con, conL);

		setLayout(new GridLayout(20, 3));

		hinweis = new JLabel("Nach was wollen Sie suchen:");
		aufforderung = new JLabel("Geben Sie hier nun die Nummer ein:");

		rechnungsnummer = new JTextField();

		bg.add(wNummer);
		bg.add(inventarnummer);
		bg.add(externeNummer);
		bg.add(bhBeleg);

		wNummer.setSelected(true);

		hinweis.setFont(new Font("SansSerif", Font.BOLD, 15));
		aufforderung.setFont(new Font("SansSerif", Font.BOLD, 15));

		// ActionListener
		suchen.addActionListener(this);
		wNummer.addActionListener(this);
		inventarnummer.addActionListener(this);
		externeNummer.addActionListener(this);
		pfadEingeben.addActionListener(this);
		bhBeleg.addActionListener(this);
		korrigieren.addActionListener(this);

		// ToolTipText
		suchen.setToolTipText("Rechnungen suchen");
		wNummer.setToolTipText("nach W-Nummer suchen");
		inventarnummer.setToolTipText("nach Inventarnummer suchen");
		externeNummer.setToolTipText("nach externer Nummer suchen");

		add(hinweis);
		add(wNummer);
		add(inventarnummer);
		add(externeNummer);
		add(bhBeleg);
		add(new JLabel(""));
		add(aufforderung);
		add(rechnungsnummer);
		add(new JLabel(""));
		if (nummer == 0)
			add(suchen);
		else
			add(korrigieren);
		add(new JLabel(""));
		if (nummer == 0)
			add(pfadEingeben);
		setVisible(true);

	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == suchen) {
			suche(true);
		}
		if (e.getSource() == korrigieren) {
			suche(false);

		}

		if (e.getSource() == wNummer) {
			nachWas = 0;

		}
		if (e.getSource() == inventarnummer) {
			nachWas = 1;

		}
		if (e.getSource() == externeNummer) {
			nachWas = 2;

		}
		if (e.getSource() == bhBeleg) {
			nachWas = 3;

		}

		if (e.getSource() == pfadEingeben) {
			JFrame rp = new RechnungPfad();
			rp.pack();
			rp.setVisible(true);
		}
	}

	public void suche(boolean k) {

		daten = new Vector();
		if (nachWas != 0 && nachWas != 3) {
			try {

				nummer = Integer.parseInt(rechnungsnummer.getText());
				istZahl = true;
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this,
						"Sie müssen eine Zahl eingeben");
				istZahl = false;
			}
			if (istZahl) {
				if (nachWas == 1)
					query = "select * from rechnung where Inventarnummer ="
							+ nummer;
				else
					query = "select * from rechnung where externeNummer" + " ="
							+ nummer;

				daten = di.sucheRechnung(query);
			}
		}

		else if (nachWas == 0) {
			query = "select * from rechnung where wNummer like '"
					+ rechnungsnummer.getText() + "'";

			daten = di.sucheRechnung(query);
		} else if (nachWas == 3) {
			query = "select * from rechnung where buchhaltungsbelege like '"
					+ rechnungsnummer.getText() + "'";

			daten = di.sucheRechnung(query);

		}

		if (daten.size() == 0) {
			JOptionPane.showMessageDialog(this,
					"Zu dem Suchkriterium wurde keine Rechnung gefunden");

		} else if (k) {

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (int i = 0; i < daten.size(); i++) {
						row = (Vector) daten.get(i);
						ra = new RechnungAnzeige(row);
						ra.pack();
						ra.setLocationRelativeTo(null);
						ra.setVisible(true);
					}

				}
			});
		}

		else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (int i = 0; i < daten.size(); i++) {
						row = (Vector) daten.get(i);

						rk = new RechnungKorrigieren(row, con, conL);
						rk.pack();
						rk.setLocationRelativeTo(null);
						rk.setVisible(true);
					}
				}
			});

		}
	}
}
