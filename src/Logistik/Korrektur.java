package Logistik;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

/*
 * Zeigt die gewählten Materialien an
 * und ermöglicht die eingabe des korrigierten Lagerstandes.
 */
public class Korrektur extends JFrame implements ActionListener {
	private AnzTabelleA table;
	private DBConnection con;
	private JButton speichern = new JButton("Speichern");
	private JButton abbrechen = new JButton("Abbrechen");
	private int id;
	private String stueck[];
	private double stk[];
	private ResultSet rs;
	private String[] pos;
	private JPanel buttons = new JPanel();

	public Korrektur(DBConnection con, String s) {
		Container c = getContentPane();
		this.con = con;
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(700, 700);

		c.setLayout(new BorderLayout());
		buttons.add(speichern);
		buttons.add(abbrechen);

		c.add(buttons, BorderLayout.SOUTH);

		speichern.addActionListener(this);
		abbrechen.addActionListener(this);

		try {
			id = Integer.parseInt(s);

		} catch (NumberFormatException ne) {
		}

		String query = "SELECT m.id as id, m.bezeichnung as bezeichnung,m.bundesnr, m.stueck as stueck, m.meldebestand, i.bezeichnung as inventurgruppe FROM material m , inventurgruppe i WHERE m.bezeichnung LIKE \""
				+ s + "\" AND i.id=m.inventurgruppe ORDER BY id ASC";

		rs = con.mysql_query(query);
		String[] spalten = { "ID", "Bezeichnung", "Inventurgruppe", "stueck" };
		String[] spaltennamen = { "ID", "Bezeichnung", "Inventurgruppe",
				"Stück" };
		Class[] classes = { Integer.class, String.class, String.class,
				JTextField.class };
		JScrollPane sc = new JScrollPane(table = new AnzTabelleA(spalten,
				spaltennamen, classes, rs, 1));
		c.add(sc, BorderLayout.CENTER);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == abbrechen) {
			dispose();
		}

		if (e.getSource() == speichern) {
			Boolean isZahl = true;

			stueck = table.getEingabe("Stück");
			stk = new double[stueck.length];
			pos = table.getEingabe("ID");
			for (int i = 0; i < stueck.length && isZahl; i++) {
				try {

					stk[i] = Double.parseDouble(stueck[i]);
				} catch (NumberFormatException ex) {
					isZahl = false;
					JOptionPane.showMessageDialog(null, "Bitte bei Material "
							+ pos[i] + " eine Stückzahl eingeben!",
							"keine Zahl", JOptionPane.ERROR_MESSAGE);
				}// fehlermeldung
			}

			if (isZahl) {
				for (int i = 0; i < stueck.length; i++) {
					String sql = "UPDATE material SET stueck =" + stueck[i]
							+ " WHERE id=" + pos[i];
					con.mysql_update(sql);
				}
				dispose();
			}

		}

	}

}
