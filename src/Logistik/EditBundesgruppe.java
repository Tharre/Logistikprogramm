package Logistik;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Klasse zum Bearbeiten von Bundesgruppen In einem SelectInput kann die
 * gewünschte Bundesgruppe ausgewählt werden. Nach dem Klick auf den "OK"-Button
 * werden die Daten in die Eingabemaske übernommen Nach dem Klick auf den
 * speichern Button werden die neuen Daten in die DB gespeichert
 */

public class EditBundesgruppe extends LayoutMainPanel implements ActionListener {
	private JPanel head;
	private NewBundesgruppe mid;
	private SelectInputLevel ug;
	private JButton ok;

	public EditBundesgruppe(UserImport user) {
		super(user);
		head = new JPanel();
		String[] invgrupHeads = { "id", "nr", "bezeichnung", "uebergruppe" };
		ug = new SelectInputLevel(con, new Input(20, ""), "bundesnr",
				invgrupHeads, "id", "bezeichnung", "uebergruppe");
		ug.setTextEnabled(false);
		ok = LayoutButtonCreator.createButton("images/ok.gif", "ok");
		ok.addActionListener(this);
		head.add(new JLabel("Bundesgruppe:"));
		head.add(ug);
		head.add(ok);
		mid = new NewBundesgruppe(user);
		mid.setOpaque(false);
		setLayoutM(new BorderLayout());
		addM(head, BorderLayout.NORTH);
		addM(mid, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if (ug.getValue().equals("")) {
				new MessageError("Bitte eine Usergruppe auswählen!");
				return;
			}
			mid.setEdit(ug.getValue());
		}
	}
}