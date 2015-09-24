package Budget;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RechnungKorrigieren extends JFrame implements ActionListener {

	private JButton speichern = new JButton("Änderung speichern");
	private JButton zahlbetragBerechnen = new JButton("Zahlbetrag berechnen");
	private JButton skontoBerechnen = new JButton("Skonto berechnen");
	private String[] namen = { "W-Nummer", "Bestellbetrag", "Rechnungsbetrag",
			"externe Nummer", "interne Nummer", "Inventarnummer",
			"Buchhaltungsbelege", "Sonderabzug", "Skonto", "Zahlart",
			"Zahlbetrag", "Rechnungsstatus" };
	private DatenUpdate du;
	private Vector daten;

	private JTextField[] felder;
	private JTextField skonto = new JTextField("Skonto in Euro");
	private JTextField zahlbetrag = new JTextField("Zahlbetrag gerechnet");

	public RechnungKorrigieren(Vector daten, Connection con, Connection conL) {

		this.daten = daten;
		setLayout(new GridLayout(13, 3));

		du = new DatenUpdate(con, conL);

		skonto.setEditable(false);
		zahlbetrag.setEditable(false);

		felder = new JTextField[daten.size() - 1];

		for (int i = 1; i < daten.size(); i++) {
			add(new JLabel(namen[i - 1]));
			felder[i - 1] = new JTextField(daten.get(i).toString());
			add(felder[i - 1]);
			if (i == 9)
				add(skonto);
			else if (i == 11) {
				add(zahlbetrag);
				felder[i - 1].setEditable(false);
			} else
				add(new JLabel(""));
		}

		felder[felder.length - 1].setEditable(false);

		speichern.addActionListener(this);
		zahlbetragBerechnen.addActionListener(this);
		skontoBerechnen.addActionListener(this);
		add(speichern);
		add(zahlbetragBerechnen);
		add(skontoBerechnen);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == speichern)

		{
			boolean ok = false;

			try {
				ok = du.korrigiereRechnung(Integer.parseInt(daten.get(0)
						.toString()), felder[0].getText(), Double
						.parseDouble(felder[1].getText()), Double
						.parseDouble(felder[2].getText()), Double
						.parseDouble(felder[8].getText()), felder[3].getText(),
						felder[4].getText(), felder[9].getText(), felder[6]
								.getText(), felder[5].getText(), Double
								.parseDouble(felder[7].getText()));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null,
						"Sie dürfen in manchen Zeilen nur Zahlen eingeben.");
			}

			if (ok)
				JOptionPane.showMessageDialog(null, "Rechnung korrigiert!");
			else
				JOptionPane.showMessageDialog(null,
						"Fehler beim Korrigieren der Rechnung.");
			dispose();
		}
		if (e.getSource() == skontoBerechnen) {

			

			try {
				skonto.setText(""
						+ berechneSkonto(Double.parseDouble(daten.get(8)
								.toString()), Double.parseDouble(daten.get(3)
								.toString()), Double.parseDouble(daten.get(9)
								.toString())));
			} catch (NumberFormatException n) {
				JOptionPane.showMessageDialog(null,
						"Es gab einen Fehler bei der Berechnung!");

			}

		}

		if (e.getSource() == zahlbetragBerechnen) {
			double skonto = berechneSkonto(Double.parseDouble(daten.get(8)
					.toString()), Double.parseDouble(daten.get(3).toString()),
					Double.parseDouble(daten.get(9).toString()));

			try {
				zahlbetrag.setText(""
						+ getZahlbetrag(Double.parseDouble(daten.get(2)
								.toString()), Double.parseDouble(daten.get(8)
								.toString()), skonto));
			} catch (NumberFormatException n) {
				JOptionPane.showMessageDialog(null,
						"Es gab einen Fehler bei der Berechnung!");

			}
		}
	}

	public double berechneSkonto(double sonderabzug, double rechnungsbetrag,
			double skontoProzent) {

		double skontoBetrag = runde((rechnungsbetrag - sonderabzug)
				* (skontoProzent / 100));

		return skontoBetrag;

	}

	public double getZahlbetrag(double rechnungsbetrag,
			double summeSonderabzuege, double skonto) {
		double zahlbetragEuro = runde(rechnungsbetrag - summeSonderabzuege
				- skonto);

		return zahlbetragEuro;
	}

	public double runde(double zahl) {
		zahl = zahl * 100;
		zahl = Math.round(zahl);
		zahl = zahl / 100;
		return zahl;

	}

}
