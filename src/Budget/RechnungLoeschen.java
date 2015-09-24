package Budget;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RechnungLoeschen extends JPanel implements ActionListener {

	private JLabel hinweis = new JLabel(
			"Welche Rechnungen möchten Sie löschen?");
	private JLabel von = new JLabel("Von welchen Datum");
	private JLabel bis = new JLabel("bis zu welchem Datum");
	private JTextField vonTagTF = new JTextField("TT");
	private JTextField bisTagTF = new JTextField("TT");
	private JTextField vonMonatTF = new JTextField("MM");
	private JTextField bisMonatTF = new JTextField("MM");
	private JTextField vonJahrTF = new JTextField("JJJJ");
	private JTextField bisJahrTF = new JTextField("JJJJ");
	private JButton btn = new JButton("Buchungen löschen");
	private JButton btnR = new JButton("Rechnungen löschen");
	private JLabel hinweisBuchung = new JLabel(
			"Welche Buchungen möchten Sie löschen?");
	private JLabel wNummer = new JLabel("W- Nummer");
	private JTextField wNummerTF = new JTextField();
	int vonTag = 0;
	int vonMonat = 0;
	int vonJahr = 0;

	int bisTag = 0;
	int bisMonat = 0;
	int bisJahr = 0;

	private DatenUpdate du;

	public RechnungLoeschen(Connection con, Connection conL) {

		du = new DatenUpdate(con, conL);
		btn.addActionListener(this);
		btnR.addActionListener(this);

	}

	public void anzeigenBuchungen() {

		add(hinweisBuchung);
		add(von);
		add(vonTagTF);
		add(vonMonatTF);
		add(vonJahrTF);
		add(bis);
		add(bisTagTF);
		add(bisMonatTF);
		add(bisJahrTF);

		add(btn);

	}

	public void anzeigenRechnungen() {

		setLayout(new GridLayout(10, 10));
		add(hinweis);
		add(wNummer);
		add(wNummerTF);

		add(btnR);

		btn.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn)

		{
			try {
				vonTag = Integer.parseInt(vonTagTF.getText());
				vonMonat = Integer.parseInt(vonMonatTF.getText());
				vonJahr = Integer.parseInt(vonJahrTF.getText());

				bisTag = Integer.parseInt(bisTagTF.getText());
				bisMonat = Integer.parseInt(bisMonatTF.getText());
				bisJahr = Integer.parseInt(bisJahrTF.getText());
			} catch (NumberFormatException n) {
				JOptionPane.showMessageDialog(this,
						"Bitte geben Sie nur Zahlen ein.");

			}

			du.loescheBuchungen(vonTag, vonMonat, vonJahr, bisTag, bisMonat,
					bisJahr);

			JOptionPane.showMessageDialog(this,
					"Die Buchungen wurden gelöscht.");

		}

		if (e.getSource() == btnR) {

			du.loescheRechnung(wNummerTF.getText());
			JOptionPane.showMessageDialog(this, "Die Rechnung wurde gelöscht.");

		}
	}
}
