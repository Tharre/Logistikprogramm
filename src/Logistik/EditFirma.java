package Logistik;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Klasse zum Bearbeiten von Firmendaten In einem SelectInput kann die
 * gewünschte Firma ausgewählt werden. Nach dem Klick auf den "OK"-Button werden
 * die Daten in die Eingabemaske übernommen Nach dem Klick auf den speichern
 * Button werden die neuen Daten in die DB gespeichert
 */

public class EditFirma extends LayoutMainPanel implements ActionListener {
	private JPanel head;
	private NewFirma mid;
	private SelectInput firma;
	private JButton ok;

	public EditFirma(UserImport user) {
		super(user);
		head = new JPanel();
		firma = new SelectInput(con, new Input(16, ""), "firma", new String[] {
				"id", "firmenname" }, "id", "firmenname", user);
		firma.setTextEnabled(false);
		ok = LayoutButtonCreator.createButton("ok.gif", "ok");
		ok.addActionListener(this);
		head.add(new JLabel("Firma:"));
		head.add(firma);
		head.add(ok);
		mid = new NewFirma(user, null);
		mid.setOpaque(false);
		JScrollPane sp = new JScrollPane(mid);
		setLayoutM(new BorderLayout());
		addM(head, BorderLayout.NORTH);
		addM(sp, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if (firma.getValue().equals("")) {
				new MessageError("Bitte eine Firma auswählen!");
				return;
			}
			mid.setFirma(firma.getValue());
		}
	}
}
