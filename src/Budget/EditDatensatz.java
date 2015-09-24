package Budget;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *In der Klasse Datensatz kann man einzelne Werte eines Datensatzes aendern
 *<p>
 * Title: Datensatz
 * 
 * @author Haupt, Liebhart
 **/
public class EditDatensatz extends JFrame implements ActionListener, KeyListener {

	// TextField
	/** Eingabefeld fuer den Namen **/
	private JTextField tfName = new JTextField();
	/** Eingabefeld fuer die Raumnummer **/
	private JTextField tfRaumnr = new JTextField();
	/** Eingabefeld fuer die Kurzbezeichnung **/
	private JTextField tfKurzbezeichnung = new JTextField();
	/** Eingabefelder fuer die Spalten der Projekte **/
	private JTextField[] projekt = new JTextField[7];

	// String
	/** Daten, die bereits in dem Datensatz gespeichert sind **/
	private String[] eintraege;
	/** Hinweise zu den Eintraegen **/
	private String[] namenZuEintraege = { "Nummer", "Name", "Lehrer",
			"Kurzname", "Klasse", "Datum", "Teilnehmer" };

	// sonstiges
	/** Button Speichern, um die Aenderung in der Datenbank zu speichern **/
	private JButton speichern = new JButton("Speichern");
	/** Zeilennummer des Eintrages **/
	private int zeilennummer;
	/** ComboBox mit den Abteilungen **/
	private JComboBox abteilungen = new JComboBox();
	/** Objekt von der Klasse DatenUpdate **/
	private DatenUpdate du;
	/** Objekt von der Klasse DatenImport **/
	private DatenImport di;
	/** Kennnummer der Tabelle **/
	private int kennnummer;

	private NewTextdatei txt = new NewTextdatei();

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 * @param zeilennummer
	 *            Nummer des Datensaetzes
	 * @param kennnummer
	 *            Kennnummer der Tabelle
	 */
	public EditDatensatz(Connection con, Connection conL, int zeilennummer,
			int kennnummer) {

		super("Datensatz ändern");

		this.kennnummer = kennnummer;
		this.zeilennummer = zeilennummer;

		speichern.addActionListener(this);
		speichern.addKeyListener(this);

		speichern.setToolTipText("Speichern der Änderung");

		du = new DatenUpdate(con, conL);
		di = new DatenImport(con, conL);

		if (kennnummer == 8) {
			setLayout(new GridLayout(4, 2));

			tfName.setText(di.getEintragZuNummer(zeilennummer, kennnummer, 0));
			tfRaumnr
					.setText(di.getEintragZuNummer(zeilennummer, kennnummer, 1));
			tfKurzbezeichnung.setText(di.getEintragZuNummer(zeilennummer,
					kennnummer, 2));

			add(new JLabel("Name: "));
			add(tfName);
			add(new JLabel("Raumnummer: "));
			add(tfRaumnr);
			add(new JLabel("Kurzbezeichnung: "));
			add(tfKurzbezeichnung);

		} else if (kennnummer == 2) {
			setLayout(new GridLayout(9, 2));

			eintraege = di.getEintragZuProjekt(zeilennummer);

			abteilungen.addItem("WI");
			abteilungen.addItem("MI");
			abteilungen.addItem("EL");
			abteilungen.addItem("ET");

			for (int i = 0; i < eintraege.length - 1; i++) {

				{
					add(new JLabel(namenZuEintraege[i]));
					projekt[i] = new JTextField();
					projekt[i].setText(eintraege[i]);
					add(projekt[i]);
				}
			}

			add(new JLabel("Abteilung: "));
			add(abteilungen);

		} else {

			setLayout(new GridLayout(2, 2));

			tfName.setText(di.getEintragZuNummer(zeilennummer, kennnummer, 0));
			add(new JLabel("Name: "));
			add(tfName);

		}

		add(speichern);
	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == speichern) {

			// kostenstelleut8
			if (kennnummer == 8) {

				if (ueberpruefeEingabeKostenstelle(tfName.getText(),
						tfKurzbezeichnung.getText(), tfRaumnr.getText()))

				{
					String[] kurzbezeichnung = txt.getkurzbezUt8();
					String[] name = txt.getKostenstellenut8();
					String[] raum = txt.getraumnummerUt8();

					for (int i = 0; i < name.length; i++) {
						if (name[i].equals(di.getEintragZuNummer(zeilennummer,
								kennnummer, 0))
								&& kurzbezeichnung[i].equals(di
										.getEintragZuNummer(zeilennummer,
												kennnummer, 2))
								&& raum[i].equals(di.getEintragZuNummer(
										zeilennummer, kennnummer, 1))) {
							name[i] = tfName.getText();
							kurzbezeichnung[i] = tfKurzbezeichnung.getText();
							raum[i] = tfRaumnr.getText();
							txt.schreibeInTextdatei(name,
									"kostenstellenut8.txt");
							txt.schreibeInTextdatei(raum, "raumnummerUt8.txt");
							txt.schreibeInTextdatei(kurzbezeichnung,
									"kurzbezUt8.txt");
							break;
						}

					}
					du.aendereDatensatz(zeilennummer, tfRaumnr.getText(),
							kennnummer, "Raumnummer");

					du.aendereDatensatz(zeilennummer, tfName.getText(),
							kennnummer, "Name");

					du.aendereDatensatz(zeilennummer, tfKurzbezeichnung
							.getText(), kennnummer, "Kurzbezeichn");

					// JOptionPane.showMessageDialog(null,
					// "Die Daten wurden geändert.");
					dispose();
				}
			}

			else

			// projekt
			if (kennnummer == 2) {

				if (ueberpruefeEingabeProjekt(projekt)) {

					String[] projektName = txt.getProjektName();
					String[] projektDatum = txt.getProjektDatum();
					String[] projektNummerSelbst = txt.getProjektNummer();

					String[] projektTeilnehmer = txt.getProjektTeilnehmer();
					String[] projektLehrer = txt.getProjektLehrer();
					String[] projektKurzz = txt.getProjektKurzz();
					String[] projektKlasse = txt.getProjektKlasse();
					String[] projektAbteilung = txt.getProjektAbteilung();

					String[] eintraege = di.getEintraegeZuProjekt(zeilennummer);

					for (int i = 0; i < projektName.length; i++) {
						if (projektNummerSelbst[i].equals(eintraege[0])
								&& projektName[i].equals(eintraege[1])
								&& projektLehrer[i].equals(eintraege[2])
								&& projektKurzz[i].equals(eintraege[3])
								&& projektKlasse[i].equals(eintraege[4])
								&& projektDatum[i].equals(eintraege[5])
								&& projektTeilnehmer[i].equals(eintraege[6])
								&& projektAbteilung[i].equals(eintraege[7])) {

							projektNummerSelbst[i] = projekt[0].getText();
							projektName[i] = projekt[1].getText();
							projektLehrer[i] = projekt[2].getText();
							projektKurzz[i] = projekt[3].getText();
							projektKlasse[i] = projekt[4].getText();
							projektDatum[i] = projekt[5].getText();
							projektTeilnehmer[i] = projekt[6].getText();
							projektAbteilung[i] = abteilungen.getSelectedItem()
									.toString();

							txt.schreibeInTextdatei(projektNummerSelbst,
									"projektNummer.txt");
							txt.schreibeInTextdatei(projektName,
									"projektName.txt");
							txt.schreibeInTextdatei(projektLehrer,
									"projektLehrer.txt");
							txt.schreibeInTextdatei(projektKurzz,
									"projektkurzz.txt");
							txt.schreibeInTextdatei(projektKlasse,
									"projektKlasse.txt");
							txt.schreibeInTextdatei(projektDatum,
									"projektDatum.txt");
							txt.schreibeInTextdatei(projektTeilnehmer,
									"projektTeilnehmer.txt");
							txt.schreibeInTextdatei(projektAbteilung,
									"projektAbteilung.txt");
							break;
						}

					}

					du.aendereDatensatz(zeilennummer, projekt[2].getText(),
							kennnummer, "lehrer");

					du.aendereDatensatz(zeilennummer, projekt[3].getText(),
							kennnummer, "kurzz");

					du.aendereDatensatz(zeilennummer, projekt[4].getText(),
							kennnummer, "klasse");

					du.aendereDatensatz(zeilennummer, projekt[5].getText(),
							kennnummer, "datum");

					du.aendereDatensatz(zeilennummer, projekt[6].getText(),
							kennnummer, "teilnehmer");

					du.aendereDatensatz(zeilennummer, abteilungen
							.getSelectedItem().toString(), kennnummer,
							"abteilung");

					du.aendereDatensatz(zeilennummer, projekt[1].getText(),
							kennnummer, "Name");

					du.aendereDatensatz(zeilennummer, projekt[0].getText(),
							kennnummer, "nummerSelbst");

					JOptionPane.showMessageDialog(null,
							"Die Daten wurden geändert.");
					dispose();
				}

			} else {

				if (ueberpruefeEingabe(tfName.getText())) {

					String[] daten = null;
					String name = null;
					switch (kennnummer) {
					case 5: {
						daten = txt.getHauptbereicheut8();
						name = "hauptbereichut8.txt";
					}
						break;
					case 6: {
						daten = txt.getBereicheut8();
						name = "bereichut8.txt";
					}
						break;
					case 7: {
						daten = txt.getHauptkostenstelleut8();
						name = "hauptkostenstelleut8.txt";
					}

					case 4: {
						daten = txt.getSonderbudget();
						name = "sonderbudget.txt";

					}

					}

					for (int i = 0; i < daten.length; i++) {

						if (daten[i].equals(di.getEintragZuNummer(zeilennummer,
								kennnummer, 0))) {
							{
								daten[i] = tfName.getText();
								txt.schreibeInTextdatei(daten, name);
								break;
							}

						}
					}

					du.aendereDatensatz(zeilennummer, tfName.getText(),
							kennnummer, "Name");

					JOptionPane.showMessageDialog(null,
							"Die Daten wurden geändert.");
					dispose();
				}

			}

		}
	}

	/**
	 * ueberprueft die Eingaben vom User, wenn die Daten einer Kostenstelle
	 * geaendert wurden
	 * 
	 * @param name
	 *            Name der Kostenstelle
	 * @param kurz
	 *            Kurzbezeichnung
	 * @param raumnummer
	 *            Raumnummer der Kostenstelle
	 **/
	public boolean ueberpruefeEingabeKostenstelle(String name, String kurz,
			String raumnummer) {

		if (name.length() == 0 || name.length() > 50 || kurz.length() == 0
				|| kurz.length() > 10 || raumnummer.length() == 0
				|| raumnummer.length() > 30)

		{
			JOptionPane.showMessageDialog(null,
					"Bitte beachten Sie die Länge Ihrer Eingabe");

			return false;
		}
		return true;
	}// endUeberpruefeEingabe

	/**
	 * ueberprueft die Eingaben des Users, wenn die Daten eines Projektes
	 * geaendert wurden
	 * 
	 * @param projekt
	 *            Daten des Projektes
	 * @return true, wenn die Eingabe in Ordnung war; false, wenn die Eingabe
	 *         nicht in Ordnung war
	 */
	public boolean ueberpruefeEingabeProjekt(JTextField[] projekt) {

		if ((((projekt[0].getText()).length()) > 11)

		|| (((projekt[1].getText()).length()) > 50)

		|| (((projekt[2].getText()).length()) > 50)

		|| (((projekt[3].getText()).length()) > 30)

		|| (((projekt[4].getText()).length() > 10))

		|| (((projekt[5].getText()).length() > 10))

		|| ((projekt[6].getText().length() > 50)))

		{
			JOptionPane.showMessageDialog(this,
					"Bitte beachten Sie die Laenge Ihrer Eingabe");
			return false;
		}

		if ((((projekt[0].getText()).length()) == 0)
				|| (((projekt[1].getText()).length()) == 0)
				|| (((projekt[2].getText()).length() == 0))
				|| (((projekt[3].getText()).length()) == 0)
				|| (((projekt[4].getText()).length() == 0))
				|| (((projekt[5].getText()).length() == 0))
				|| (((projekt[6].getText()).length() == 0))) {
			JOptionPane.showMessageDialog(this,
					"Sie haben ein Eingabefeld leer gelassen");
			return false;
		}
		return true;
	}

	public void wennButtonGedrueckt() {

	}

	/**
	 * ueberprueft den Namen einer Eingabe
	 * 
	 * @param name
	 *            Name, der geaendert wurde
	 * @return true, wenn die Eingabe in Ordnung war; false, wenn die Eingabe
	 *         nicht in Ordnung war
	 */
	public boolean ueberpruefeEingabe(String name) {
		if (name.length() == 0 || name.length() > 50) {
			JOptionPane.showMessageDialog(null,
					"Bitte beachten Sie die Länge Ihrer Eingabe");

			return false;
		}
		return true;
	}

	public void keyPressed(KeyEvent arg0) {
		// kostenstelleut8
		if (kennnummer == 8) {

			if (ueberpruefeEingabeKostenstelle(tfName.getText(),
					tfKurzbezeichnung.getText(), tfRaumnr.getText()))

			{

				String[] kurzbezeichnung = txt.getkurzbezUt8();
				String[] name = txt.getKostenstellenut8();
				String[] raum = txt.getraumnummerUt8();

				for (int i = 0; i < name.length; i++) {
					if (name[i].equals(tfName.getText())
							&& kurzbezeichnung[i].equals(tfKurzbezeichnung
									.getText())
							&& raum[i].equals(tfRaumnr.getText())) {
						{
							name[i] = tfName.getText();
							kurzbezeichnung[i] = tfKurzbezeichnung.getText();
							raum[i] = tfRaumnr.getText();
							txt.schreibeInTextdatei(name,
									"kostenstellenut8.txt");
							txt.schreibeInTextdatei(raum, "raumnummerUt8.txt");
							txt.schreibeInTextdatei(kurzbezeichnung,
									"kurzbezUt8.txt");
							break;
						}

					}
					du.aendereDatensatz(zeilennummer, tfRaumnr.getText(),
							kennnummer, "Raumnummer");

					du.aendereDatensatz(zeilennummer, tfName.getText(),
							kennnummer, "Name");

					du.aendereDatensatz(zeilennummer, tfKurzbezeichnung
							.getText(), kennnummer, "Kurzbezeichn");
					JOptionPane.showMessageDialog(null,
							"Die Daten wurden geändert.");
					dispose();
				}

			} else

			// projekt
			if (kennnummer == 2) {

				if (ueberpruefeEingabeProjekt(projekt)) {

					du.aendereDatensatz(zeilennummer, projekt[2].getText(),
							kennnummer, "lehrer");

					du.aendereDatensatz(zeilennummer, projekt[3].getText(),
							kennnummer, "kurzz");

					du.aendereDatensatz(zeilennummer, projekt[4].getText(),
							kennnummer, "klasse");

					du.aendereDatensatz(zeilennummer, projekt[5].getText(),
							kennnummer, "datum");

					du.aendereDatensatz(zeilennummer, projekt[6].getText(),
							kennnummer, "teilnehmer");

					du.aendereDatensatz(zeilennummer, abteilungen
							.getSelectedItem().toString(), kennnummer,
							"abteilung");

					du.aendereDatensatz(zeilennummer, projekt[1].getText(),
							kennnummer, "Name");

					du.aendereDatensatz(zeilennummer, projekt[0].getText(),
							kennnummer, "nummerSelbst");

					JOptionPane.showMessageDialog(null,
							"Die Daten wurden geändert.");
					dispose();
				}

			} else {

				if (ueberpruefeEingabe(tfName.getText())) {

					String[] daten = null;
					String name = null;
					switch (kennnummer) {
					case 5: {
						daten = txt.getHauptbereicheut8();
						name = "hauptbereichut8.txt";
					}
						break;
					case 6: {
						daten = txt.getBereicheut8();
						name = "bereichut8.txt";
					}
						break;
					case 7: {
						daten = txt.getHauptkostenstelleut8();
						name = "hauptkostenstelleut8.txt";
					}

					}

			

					for (int i = 0; i < daten.length; i++) {

						if (daten[i].equals(di.getEintragZuNummer(zeilennummer,
								kennnummer, 0))) {
							{
								daten[i] = tfName.getText();
								txt.schreibeInTextdatei(daten, name);
								break;
							}

						}
					}
					du.aendereDatensatz(zeilennummer, tfName.getText(),
							kennnummer, "Name");

					JOptionPane.showMessageDialog(null,
							"Die Daten wurden geändert.");
					dispose();
				}

			}
		}
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
