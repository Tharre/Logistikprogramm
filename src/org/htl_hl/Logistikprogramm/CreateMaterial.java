package org.htl_hl.Logistikprogramm;

import sql.generated.logistik_test.tables.records.MaterialRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import static sql.generated.logistik_test.Tables.BUNDESNR;
import static sql.generated.logistik_test.Tables.INVENTURGRUPPE;
import static sql.generated.logistik_test.tables.Material.MATERIAL;


public class CreateMaterial implements Tab {

	private LConnection server;

	private final JTextField bezeichnung;
	private final SelectDBEntry bundesnr;
	private final SelectDBEntry inventurgruppe;
	private final JTextField gefahrstufe;
	private final JLabel status;

	CreateMaterial(LConnection server) {
		this.server = server;

		bezeichnung = new JTextField();
		bundesnr = new SelectDBEntry<>(server, BUNDESNR.BEZEICHNUNG);
		inventurgruppe = new SelectDBEntry<>(server, INVENTURGRUPPE.BEZEICHNUNG);
		gefahrstufe = new JTextField();
		status = new JLabel("");
	}

	public String toString() {
		return getName();
	}

	@Override
	public String getName() {
		return "Material anlegen";
	}

	@Override
	public JPanel getContent() {
		JPanel panel = new JPanel();
		JPanel middle = new JPanel(new GridBagLayout());

		int i = 0;

		middle.add(new JLabel("Bezeichnung:"), getConstraintsFor(0, i));
		middle.add(bezeichnung, getConstraintsFor(1, i));
		i++;

		middle.add(new JLabel("Bundesnr:"), getConstraintsFor(0, i));
		middle.add(bundesnr, getConstraintsFor(1, i));
		i++;

		middle.add(new JLabel("Inventurgruppe:"), getConstraintsFor(0, i));
		middle.add(inventurgruppe, getConstraintsFor(1, i));
		i++;

		middle.add(new JLabel("Gefahrstufe:"), getConstraintsFor(0, i));
		middle.add(gefahrstufe, getConstraintsFor(1, i));
		i++;

		JPanel bottom = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridy = i;
		JButton save = new JButton("Speichern");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		bottom.add(save, c);

		c.gridx = 1;
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		bottom.add(reset, c);

		JPanel top = new JPanel();
		top.add(status);


		JPanel both = new JPanel(new BorderLayout());

		both.add(top, BorderLayout.NORTH);
		both.add(middle, BorderLayout.CENTER);
		both.add(bottom, BorderLayout.SOUTH);

		panel.add(both);

		return panel;
	}

	private void reset() {
		bezeichnung.setText("");
		gefahrstufe.setText("");
		status.setText("");
	}

	private void save() {
		MaterialRecord material = server.create.newRecord(MATERIAL);

		material.setBezeichnung(bezeichnung.getText());
		material.setErstelldatum(new Timestamp(System.currentTimeMillis()));
		material.setBundesnrId(bundesnr.getId());
		material.setInventurgrpId(inventurgruppe.getId());
		material.setErfasserId(null); // TODO: get from LConnection()?
		material.setGefahrstufe(Integer.parseInt(gefahrstufe.getText()));

		material.store();
		System.out.println("Created material with the id " + material.getId());

		reset();
		status.setText("Gespeichert!");
	}

	private GridBagConstraints getConstraintsFor(int x, int y) {
		final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
		final Insets EAST_INSETS = new Insets(5, 5, 5, 0);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;

		c.fill = (x == 0) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
		c.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;

		c.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
		return c;
	}
}
