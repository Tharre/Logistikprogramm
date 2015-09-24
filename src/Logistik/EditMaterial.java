package Logistik;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Klasse zum Bearbeiten von Materialien In einem SelectInput kann das
 * gewünschte Material ausgewählt werden. Nach dem Klick auf den "OK"-Button
 * werden die Daten in die Eingabemaske übernommen Nach dem Klick auf den
 * speichern Button werden die neuen Daten in die DB gespeichert
 */

public class EditMaterial extends LayoutMainPanel implements ActionListener {
	private JPanel head;
	private NewEditMaterial mid;
	private SelectInput firma;
	private JButton ok;

	public EditMaterial(UserImport user) {
		super(user);
		head = new JPanel();
		firma = new SelectInput(con, new Input(16, ""), "material",
				new String[] { "id", "bezeichnung" }, "id", "bezeichnung", user);
		firma.setTextEnabled(false);
		ok = LayoutButtonCreator.createButton("ok.gif", "ok");
		ok.addActionListener(this);
		head.add(new JLabel("Material:"));
		head.add(firma);
		head.add(ok);
		mid = new NewEditMaterial(user);
		mid.setOpaque(false);
		setLayoutM(new BorderLayout());
		addM(head, BorderLayout.NORTH);
		addM(mid, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if (firma.getValue().equals("")) {
				new MessageError("Bitte ein Material auswählen!");
				return;
			}
			mid.setEdit(firma.getValue());
		}
	}
}
