package Logistik;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Klasse zum Bearbeiten von Firma-Material-Verbindungen In einem SelectInput
 * kann die gewünschte Firma-Material-Verbindung ausgewählt werden. Nach dem
 * Klick auf den "OK"-Button werden die Daten in die Eingabemaske übernommen
 * Nach dem Klick auf den speichern Button werden die neuen Daten in die DB
 * gespeichert
 */

public class EditFirmaMaterial extends LayoutMainPanel implements ActionListener {
	private JPanel head;
	private NewEditFirmaMaterial mid;
	private SelectInput firma;
	private JButton ok;

	public EditFirmaMaterial(UserImport user) {
		super(user);
		head = new JPanel();
		firma = new SelectInput(con, new Input(15, ""), "firma_material",
				new String[] { "id", "firmenname", "bezeichnung" }, "id", "id",
				user, -2);
		firma.setTextEnabled(false);
		ok = LayoutButtonCreator.createButton("ok.gif", "ok");
		ok.addActionListener(this);
		head.add(new JLabel("Firma-Material Beziehung:"));
		head.add(firma);
		head.add(ok);
		mid = new NewEditFirmaMaterial(user);
		mid.setOpaque(false);
		setLayoutM(new BorderLayout());
		addM(head, BorderLayout.NORTH);
		addM(mid, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if (firma.getValue().equals("")) {
				new MessageError(
						"Bitte eine Firmen-Material-Beziehung auswählen!");
				return;
			}
			mid.setEdit(firma.getValue());
		}
	}
}
