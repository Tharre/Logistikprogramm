 package Budget;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * In dieser Klasse wird die Eingabemaske erzeugt, die erscheint, wenn man ein
 * Budget bearbeiten will
 *<p>
 * Title: TextField
 * 
 *@author Haupt, Liebhart
 */
public class EditBudget extends JFrame implements ActionListener {
	

	// Labels
	/** Es wird die Kostenstelle angezeigt, die bearbeitet wird **/
	private JLabel lKostenstelle;
	/** das Label "geplantes Budget:" **/
	private JLabel lBudget = new JLabel("geplantes Budget: ");

	// String
	/** Hier wird das Budget gespeichert, in dem die Kostenstelle ist **/
	private String strBudget;
	/** die Tabelle, in der die Kostenstelle gespeichert ist **/
	private String tabelle;
	/** Hier wird der Name der Kostenstelle gespeichert, die geaendert wird **/
	private String name;

	// int
	/** gibt an, um welches Budget es sich handelt **/
	private int kennnummer;
	/** die Nummer der Kostenstelle **/
	private int zeilennummer;

	// sonstiges
	/** der neue Betrag **/
	private double budget;
	/**
	 * true: die Werte passen; false: der neue Werte kann nicht gespeichert
	 * werden
	 **/
	private boolean geprueft;
	/** Hilfsvector fuer die Nummern bei UT8**/
	private Vector nummern;
	/** der Button "Speichern" **/
	private JButton btnSpeichern = new JButton("Speichern");
	/** das Textfeld, in dem man den neuen Betrag eingeben kann **/
	private JTextField txtGeplantesBudget = new JTextField(12);

	// Objekte von eigenen Klassen
	/** wird benoetigt, um die neuen Daten in die Datenbank zu schreiben **/
	private DatenUpdate up;
	/** ein DatenImport-Objekt **/
	private DatenImport di;
	/** wird verwendet, um den eingegebenen Wert zu ueberpruefen **/
	private UeberpruefungBudget pruefe;

	/**
	 * Kostruktor
	 * 
	 * @param title
	 *            der Titel des Eingabe-Frames
	 * @param kennnummer
	 *            welches Budget wird verwendet: 1=UT3, 2=Projekt, 3=LMB1,
	 *            4=Sonderbudget, 5=UT8 Hb, 6=UT8 B, 7=UT8 HKst, 8=UT8 Kst,
	 *            9=LMB2
	 * @param zeilennummer die Nummer des zu bearbeitenden Feldes
	 * @param con Verbindung zur Budget-Datenbank
	 * @param conL Verbindung zur Logistik-Datenbank
	 * @param nummern Hilfsvector fuer die Nummern bei UT8
	 */
	public EditBudget(String title, int kennnummer, int zeilennummer,
			Connection con, Connection conL, Vector nummern) {
		super(title);

		this.kennnummer = kennnummer;
		this.zeilennummer = zeilennummer;
		this.nummern = nummern;

		up = new DatenUpdate(con, conL);
		di = new DatenImport(con, conL);

		switch (kennnummer) {
		case 1:
			strBudget = "UT3";
			tabelle = "abteilungut3";
			break;
		case 2:
			strBudget = "Projekt";
			tabelle = "projekt";
			break;
		case 3:
			strBudget = "LMB";
			tabelle = "lmb";
			break;
		case 4:
			strBudget = "Sonderbudget";
			tabelle = "sonderbudget";
			break;
		case 5:
			strBudget = "UT8";
			tabelle = "hauptbereichut8";
			break;
		case 6:
			strBudget = "UT8";
			tabelle = "bereichut8";
			break;
		case 7:
			strBudget = "UT8";
			tabelle = "hauptkostenstelleut8";
			break;
		case 8:
			strBudget = "UT8";
			tabelle = "kostenstelleut8";
			break;
		case 9:
			strBudget = "LMB1_2011";
			tabelle = "lmb";
			break;
		}

		name = di.auslesenName(zeilennummer, tabelle);

		strBudget += " - " + name;

		pruefe = new UeberpruefungBudget(tabelle, zeilennummer, con);

		Container c = getContentPane();
		c.setLayout(new GridLayout(5, 3));

		btnSpeichern.addActionListener(this);

		lKostenstelle = new JLabel(strBudget, JLabel.CENTER);
		lKostenstelle.setFont(new Font("SansSerif", Font.BOLD, 15));

		c.add(new JLabel());
		c.add(new JLabel());
		c.add(new JLabel());

		c.add(new JLabel());
		c.add(lKostenstelle);
		c.add(new JLabel());

		c.add(new JLabel());
		c.add(new JLabel());
		c.add(new JLabel());

		c.add(lBudget);
		c.add(txtGeplantesBudget);
		c.add(btnSpeichern);

		c.add(new JLabel());
		c.add(new JLabel());
		c.add(new JLabel());

		c.setVisible(true);
	}

	/**
	 * wird aufgerufen, wenn auf einen Button geklickt wurde
	 * 
	 * @param e ein ActionEvent-Objekt
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSpeichern) {
			if (ueberpruefeEingabe()) {
				if (kennnummer == 3) {
					geprueft = pruefe.ueberpruefeLMB(budget, 1);
				} else if (kennnummer == 9) {
					geprueft = pruefe.ueberpruefeLMB(budget, 2);
				} else if (kennnummer == 4) {
					geprueft = true;
				} else
				{
					geprueft = pruefe.ueberpruefeWert(budget, nummern);
				}

				if ((geprueft) || (zeilennummer == 1 && kennnummer == 1)
						|| (zeilennummer == 1 && kennnummer == 5)
						|| (zeilennummer == 1 && kennnummer == 3)
						|| (zeilennummer == 2 && kennnummer == 9)) {
					up.update(tabelle, zeilennummer, budget);
					txtGeplantesBudget.setText(null);
				} else {
					JOptionPane
							.showMessageDialog(
									this,
									"Der Betrag ist zu hoch! Sie können nicht mehr Geld verplanen, als Ihnen für dieses Budget zusteht.");
					txtGeplantesBudget.setText(null);
				}

				dispose();
			}

			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});
		}
	}

	/**
	 * prueft, ob der Wert eine negative Zahl oder gar keine Zahl war
	 * @return ein boolean-Wert; true: die Eingabe war korrekt; false: es ist ein Fehler aufgetreten
	 */
	public boolean ueberpruefeEingabe() {
		try {
			budget = Double.parseDouble(txtGeplantesBudget.getText());
			if (budget < 0) {
				JOptionPane.showMessageDialog(this,
						"Sie muessen eine positive Zahl eingeben");
				return false;
			} else
				return true;
		} catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(this,
					"Sie muessen eine Zahl eingeben");
			return false;
		}
	}
}
