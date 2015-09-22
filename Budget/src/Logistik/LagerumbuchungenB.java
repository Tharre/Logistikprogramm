package Logistik;

import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

/*
 * Das Fenster zeigt alle Materialien an die auf die Auswahl zutreffen.
 * Auswahl der Materialien für die Buchungen durchgeführt werden sollen.
 */
public class LagerumbuchungenB extends JDialog implements ActionListener {
	public final static int ZUBUCHEN = 0;
	public final static int ABBUCHEN = 1;
	public final static int AN_FIRMA = 2;
	public final static int ANSCHAUEN = 3;
	private AnzTabelleA table;
	private DBConnection con;
	private JButton anzeigen = new JButton("Auswahl");
	private JButton abbrechen = new JButton("Abbrechen");

	private Object auswahl[];
	private JPanel buttons = new JPanel();
	private int typ;
	private LagerumbuchungenC abbuchen;
	private UserImport u;
	private int iD;

	public LagerumbuchungenB(DBConnection con, ResultSet rs, int typ, UserImport u, int iD) {
		this.typ = typ;
		this.u = u;
		Container c = getContentPane();
		this.con = con;
		this.iD = iD;

		c.setLayout(new BorderLayout());

		if (typ != ANSCHAUEN) {
			buttons.add(anzeigen);
			buttons.add(abbrechen);
			c.add(buttons, BorderLayout.SOUTH);
		}
		anzeigen.addActionListener(this);
		abbrechen.addActionListener(this);
		JScrollPane sc;

		if (typ == ANSCHAUEN) {
			String[] spalten = { "ID", "Bezeichnung", "Bundesnr",
					"Erstellungsdatum", "inventurgruppe", "stueck",
					"meldebestand" };
			String[] spaltennamen = { "ID", "Bezeichnung", "Bundesnr",
					"Erstellungsdatum", "Inventurgruppe", "Stück",
					"Meldebestand" };
			Class[] classes = { Integer.class, String.class, String.class,
					java.util.Date.class, String.class, Integer.class,
					Integer.class };

			sc = new JScrollPane(table = new AnzTabelleA(spalten,
					spaltennamen, classes, rs, 1));

		} else {
			String[] spalten = { "ID", "Bezeichnung", "Inventurgruppe",
					"stueck" };
			String[] spaltennamen = { "ID", "Bezeichnung", "Inventurgruppe",
					"Stück", "Auswahl" };
			Class[] classes = { Integer.class, String.class, String.class,
					Double.class, Boolean.class };
			sc = new JScrollPane(table = new AnzTabelleA(spalten,
					spaltennamen, classes, rs, 1));
		}
		c.add(sc, BorderLayout.CENTER);

		setModal(true);
		pack();
		setVisible(true);
		this.setSize(700, 700);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == abbrechen) {
			dispose();
		}

		if (e.getSource() == anzeigen) {
			auswahl = table.getKlicked("ID", "Auswahl");

			for (int i = 0; i < auswahl.length; i++) {
				abbuchen = new LagerumbuchungenC(auswahl[i], typ, "Material buchen",
						con, u, iD);
			}
			dispose();
		}

	}

}