package Logistik;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Klasse zum Bearbeiten von Usergruppen In einem SelectInput kann die
 * gewünschte Usergruppe ausgewählt werden. Nach dem Klick auf den "OK"-Button
 * werden die Daten in die Eingabemaske übernommen Nach dem Klick auf den
 * speichern Button werden die neuen Daten in die DB gespeichert
 */

public class EditUsergruppe extends LayoutMainPanel implements ActionListener {
	private JPanel head;
	private NewUsergruppe mid;
	private SelectInput ug;
	private JButton ok;

	public EditUsergruppe(UserImport user) {
		super(user);

		head = new JPanel();
		ug = new SelectInput(con, new Input(15, ""), "usergroup", new String[] {
				"id", "name" }, "id", "name", user);
		ug.setTextEnabled(false);
		ok = LayoutButtonCreator.createButton("ok.gif", "ok");
		ok.addActionListener(this);
		head.add(new JLabel("Usergruppe:"));
		head.add(ug);
		head.add(ok);
		mid = new NewUsergruppe(user);
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