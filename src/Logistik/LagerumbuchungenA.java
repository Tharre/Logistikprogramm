package Logistik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/*
 * Auswahl der Aushändigungs-, Rückgabemöglichkeiten bzw. Rücksendung an Firma
 * jeweils nach Material oder Inventurgruppe
 */
public class LagerumbuchungenA extends LayoutMainPanel implements ActionListener {
	private JButton anUser, vonUser, anFirma, korrektur;
	private JButton anUserI, vonUserI, anFirmaI;
	private JPanel buttons, pan;
	private JLabel text;

	public static final int AUSHAENDIGEN = 0;
	public static final int KORREKTUR = 1;
	public static final int ZUBUCHEN_USER = 2;
	public static final int FIRMA = 3;
	public static final int MEHREREAUSHAENDIGEN = 4;
	private UserImport u;

	public LagerumbuchungenA(UserImport user, int typ) {
		super(user);

		buttons = new JPanel();
		pan = new JPanel(new GridLayout(3, 1));
		u = user;
		setLayoutM(new FlowLayout());
		if (typ == AUSHAENDIGEN)/* Aushaendigen1 */{

			text = new JLabel("Material an User Aushändigen");
			JPanel panelOben = new JPanel();
			panelOben.add(text);
			pan.add(panelOben);
			pan.add(new JLabel("\n"));
			anUser = new JButton("Material auswählen");
			anUserI = new JButton("Inventurgruppe auswählen");
			buttons.add(anUser);
			buttons.add(anUserI);
			pan.add(buttons);
			addM(pan);
			anUser.addActionListener(this);
			anUserI.addActionListener(this);

		} else if (typ == MEHREREAUSHAENDIGEN) /* AushaendigenN */{
			removeAll();
			LagerAushaendigungN aN = new LagerAushaendigungN(u);
			add(aN);

		} else if (typ == KORREKTUR) {
			text = new JLabel("Materialien korrigieren");
			JPanel panelOben = new JPanel();
			panelOben.add(text);
			pan.add(panelOben);
			pan.add(new JLabel("\n"));
			korrektur = new JButton("Material auswählen");
			buttons.add(korrektur);
			pan.add(buttons);
			addM(pan);

			korrektur.addActionListener(this);
		}

		else if (typ == ZUBUCHEN_USER) {
			text = new JLabel("Material von User zubuchen");
			JPanel panelOben = new JPanel();
			panelOben.add(text);
			pan.add(panelOben);
			pan.add(new JLabel("\n"));
			vonUser = new JButton("Material auswählen");
			vonUserI = new JButton("Inventurgruppe auswählen");
			buttons.add(vonUser);
			buttons.add(vonUserI);
			pan.add(buttons);
			addM(pan);
			vonUser.addActionListener(this);
			vonUserI.addActionListener(this);
		} else if (typ == FIRMA) {
			text = new JLabel("Material an Frima zurück senden");
			JPanel panelOben = new JPanel();
			panelOben.add(text);
			pan.add(panelOben);
			pan.add(new JLabel("\n"));
			anFirma = new JButton("Material auswählen");
			anFirmaI = new JButton("Inventurgruppe auswählen");
			addM(buttons, BorderLayout.CENTER);
			buttons.add(anFirma);
			buttons.add(anFirmaI);
			pan.add(buttons);
			addM(pan);

			anFirma.addActionListener(this);
			anFirmaI.addActionListener(this);
		}

	}

	public void actionPerformed(ActionEvent e) {

		String s = null;
		String query = null;
		if (e.getSource() == vonUser || e.getSource() == anUser
				|| e.getSource() == anFirma || e.getSource() == korrektur) {

			String a = "Material eingeben!";
			String[] headss = { "ID", "Bezeichnung" };
			// Frame,Angezeigter Text, Titel, veränderbare Spalte, angezeigte
			// Spalte, Tabelle, conection

			int unterscheidungKorrekturAnUserVonUser;

			if (e.getSource() == korrektur) {
				unterscheidungKorrekturAnUserVonUser = 0; // Korrektur
			} else if (e.getSource() == vonUser) {
				unterscheidungKorrekturAnUserVonUser = -3; // Zubuchen
			} else if (e.getSource() == anFirma)
				unterscheidungKorrekturAnUserVonUser = -5; // Zurück an Firma
			else
				unterscheidungKorrekturAnUserVonUser = -1; // LagerumbuchungenC

			new InputFrame(null, a, "Material", headss, "id", "bezeichnung",
					"material", con, false, u,
					unterscheidungKorrekturAnUserVonUser);

			if (InputFrame.getWert().length() != 0) {
				s = InputFrame.getWert();
				query = "SELECT m.id, m.bezeichnung, i.bezeichnung as inventurgruppe, m.stueck  FROM material m, inventurgruppe i  WHERE m.inventurgruppe=i.id AND m.bezeichnung LIKE \""
						+ s + "\" ORDER BY m.id ASC";
			}

		}
		if (e.getSource() == korrektur) {
			;
			if (s != null) {
				new Korrektur(con, s);
			}

			return;
		}
		if (e.getSource() == anUser) {
			if (s != null) {

				ResultSet rs = con.mysql_query(query);

				new LagerumbuchungenB(con, rs, LagerumbuchungenB.ABBUCHEN, u, 1);
			}

		}

		if (e.getSource() == vonUser) {
			if (s != null) {

				ResultSet rs = con.mysql_query(query);
				new LagerumbuchungenB(con, rs, LagerumbuchungenB.ZUBUCHEN, u, 0);
			}

		}

		if (e.getSource() == anFirma) {
			if (s != null) {
				ResultSet rs = con.mysql_query(query);

				new LagerumbuchungenB(con, rs, LagerumbuchungenB.AN_FIRMA, u, 1);
			}

		}
		if (e.getSource() == vonUserI || e.getSource() == anUserI
				|| e.getSource() == anFirmaI) {

			String a = "Inventurgruppe wählen!";
			new InputFrame(null, a, "InvGruppe!", con);
			s = InputFrame.getWert();
			query = "SELECT m.id, m.bezeichnung, i.bezeichnung as inventurgruppe, m.stueck  FROM material m, inventurgruppe i  WHERE m.inventurgruppe=i.id AND i.bezeichnung LIKE \""
					+ s + "\" ORDER BY i.id ASC";
		}

		if (e.getSource() == anUserI) {
			if (s != null) {

				ResultSet rs = con.mysql_query(query);

				new LagerumbuchungenB(con, rs, LagerumbuchungenB.ABBUCHEN, u, 1);
			}

		}

		if (e.getSource() == vonUserI) {
			if (s != null) {
				ResultSet rs = con.mysql_query(query);
				new LagerumbuchungenB(con, rs, LagerumbuchungenB.ZUBUCHEN, u, 1);
			}

		}

		if (e.getSource() == anFirmaI) {
			if (s != null) {
				ResultSet rs = con.mysql_query(query);

				new LagerumbuchungenB(con, rs, LagerumbuchungenB.AN_FIRMA, u, 1);
			}

		}

	}

}
