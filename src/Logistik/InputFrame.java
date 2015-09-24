package Logistik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

/*
 * Ein Fenster zum Einlesen aller Daten.
 * Beinhaltet ein Auswahlfeld um um die Daten auswählen zu können.
 */
public class InputFrame implements ActionListener, WindowListener {
	private JDialog dialog;
	private static String eingabe = "";
	private String[] heads;
	private String[] invgrupHeads = { "id", "bezeichnung" };
	private String[] bundesgrupHeads = { "nr","bezeichnung"};
	private SelectInput auswahl;
	private SelectInputLevel invgruppe;
	private Input input;
	private JLabel nix = new JLabel("a");
	private JButton abbrechen = new JButton("Abbrechen");
	private JButton ok = new JButton("   OK    ");
	private JLabel anz;
	private int kosten = 0;
	private UserImport user;
	private int unterscheidungKorrekturAnUserVonUser;

	public static void main(String[] args) {
	}

	/**
	 * Konstruktor 1
	 * 
	 * @param f
	 * @param anzeige
	 * @param title
	 * @param con
	 */
	public InputFrame(JFrame f, String anzeige, String title, DBConnection con) {
		input = new Input(20, "inventurgruppe");
		invgruppe = new SelectInputLevel(con, input, "inventurgruppe",
				invgrupHeads, "id", "bezeichnung", "uebergruppe");
		setSichtbar(anzeige, f, con, title, 1);
	}

	/**
	 * Konstruktor 2
	 * 
	 * @param f
	 * @param anzeige
	 * @param title
	 * @param con
	 */
	public InputFrame(JFrame f, String anzeige, String title, DBConnection con,
			UserImport user) {
		input = new Input(20, "Kostenstelle");
		auswahl = new SelectInput(user.getConnectionKst(), input,
				"werkstaette", new String[] { "Bereichsnr", "Bereichsname" },
				null, "", user);
		kosten = 1;
		setSichtbar(anzeige, f, con, title, 2);
	}

	/**
	 * Konstruktor 3
	 * 
	 * @param f
	 * @param anzeige
	 * @param title
	 * @param con
	 */
	public InputFrame(JFrame f, String anzeige, String title, DBConnection con,
			int q) {
		input = new Input(20, " bundesnr");
		invgruppe = new SelectInputLevel(con, input, "bundesnr",
				bundesgrupHeads, "nr", "bezeichnung", "uebergruppe");
		setSichtbar(anzeige, f, con, title, 1);
	}

	/**
	 * Konstruktor 4
	 * 
	 * @param f
	 * @param anzeige
	 * @param title
	 * @param con
	 */
	public InputFrame(JFrame f, String anzeige, String[] heads) {
		input = new Input(20, "Recht");
		AnzRechteB r = new AnzRechteB();
		auswahl = new SelectInput(input, heads, r.getRechte());
		setSichtbar(anzeige, f, null, null, 2);
	}

	/**
	 * Konstruktor 5; wird bei Logistik-Abfragen aufgerufen
	 * 
	 * @param f
	 * @param anzeige
	 * @param title
	 * @param con
	 */
	public InputFrame(JFrame f, String anzeige, String title, String[] head,
			String speicherung, String anzeigen, String table,
			DBConnection con, Boolean pruefen) {
		input = new Input(100, "input");
		heads = head;
		auswahl = new SelectInput(con, input, table, heads, speicherung,
				anzeigen, pruefen, con, this, null, 0);

		setSichtbar(anzeige, f, con, title, 2);

	}

	/**
	 * Konstruktor 5 1/2; wird bei "Aushaendigen1" aufgerufen, und bei Korrektur
	 * 
	 * @param f
	 * @param anzeige
	 * @param title
	 * @param con
	 * @param korr
	 *            true: wird von "Korrektur" aufgerufen, false: wird von
	 *            Aushaendigen1 aufgerufen
	 */
	public InputFrame(JFrame f, String anzeige, String title, String[] head,
			String speicherung, String anzeigen, String table,
			DBConnection con, Boolean pruefen, UserImport u, int unterscheidungKorrekturAnUserVonUser) {
		user = u;
		DBConnection c = u.getConnectionKst();
		input = new Input(100, "input");
		heads = head;
		this.unterscheidungKorrekturAnUserVonUser = unterscheidungKorrekturAnUserVonUser;

		auswahl = new SelectInput(con, input, table, heads, speicherung,
				anzeigen, pruefen, c, this, user, unterscheidungKorrekturAnUserVonUser);

		setSichtbar(anzeige, f, con, title, 2);
		// dialog.dispose();

	}

	/**
	 * Konstruktor 6
	 * 
	 * @param f
	 * @param anzeige
	 * @param title
	 * @param con
	 */
	private void setSichtbar(String anzeige, JFrame f, DBConnection con,
			String title, int i) {
		anz = new JLabel(anzeige);
		anz.setHorizontalAlignment(SwingConstants.CENTER);

		JOptionPane pane = new JOptionPane();
		dialog = pane.createDialog(f, title);
		// dialog=new JFrame(title);

		GridBagLayout gbl = new GridBagLayout();

		Container c = dialog.getContentPane();
		c.removeAll();
		c.setLayout(gbl);
		ok.addActionListener(this);
		abbrechen.addActionListener(this);
		// x y w h wx wy

		// addComponent( c, gbl, new JButton("1"), 0, 0, 5, 1, 1.0 , 0 );
		addComponent(c, gbl, new JLabel("\n"), 0, 0, 8, 1, 1.0, 0);

		addComponent(c, gbl, new JLabel(" "), 0, 1, 1, 1, 0, 1.0);
		addComponent(c, gbl, anz, 1, 1, 6, 1, 1.0, 1.0);
		addComponent(c, gbl, new JLabel(" "), 7, 1, 1, 1, 0, 1.0);

		addComponent(c, gbl, new JLabel(" "), 0, 2, 1, 1, 0, 0);
		// addComponent( c, gbl, input , 1, 2, 5, 1, 1.0 , 0 );
		if (i == 2) {
			addComponent(c, gbl, auswahl, 1, 2, 6, 1, 0, 0);
		}
		if (i == 1) {
			addComponent(c, gbl, invgruppe, 1, 2, 6, 1, 0, 0);
		}
		addComponent(c, gbl, new JLabel(" "), 7, 2, 1, 1, 0, 0);

		addComponent(c, gbl, new JLabel("\n"), 0, 3, 8, 1, 1.0, 0);

		addComponent(c, gbl, new JLabel(" "), 0, 4, 1, 1, 0, 0);
		addComponent(c, gbl, new JLabel("                "), 3, 4, 1, 1, 0, 0);
		addComponent(c, gbl, new JLabel(" "), 7, 4, 1, 1, 0, 0);
		addComponent(c, gbl, new JLabel(" "), 5, 4, 1, 1, 1.0, 0);
		addComponent(c, gbl, new JLabel(" "), 6, 4, 1, 1, 0, 0);
		addComponent(c, gbl, new JLabel(" "), 1, 4, 1, 1, 1.0, 0);
		addComponent(c, gbl, ok, 2, 4, 1, 1, 0, 0);
		addComponent(c, gbl, abbrechen, 4, 4, 1, 1, 0, 0);

		// c.setLayout(new FlowLayout());
		// c.add(new JLabel("Test"));

		dialog.setSize(400, 200);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		ok.setOpaque(true);

		dialog.setVisible(true);
		dialog.addWindowListener(this);
	}

	public static String getEingabe(JFrame f, String anzeige, String title,
			String[] head, String speicherung, String anzeigen, String table,
			DBConnection con, Boolean pruefen) {
		new InputFrame(f, anzeige, title, head, speicherung, anzeigen, table,
				con, pruefen);
		return InputFrame.eingabe;
	}

	public static String getWert() {
		try {
			Pattern.compile("[']").split(eingabe);
			return eingabe.substring(1, eingabe.length() - 1);
		} catch (Exception e) {
			e.getStackTrace();
			return "";
		}
	}

	public static String getKst() {
		return eingabe;
	}

	public void windowClosing(WindowEvent event) {
		eingabe = null;
		dialog.dispose();
	}

	public void windowClosed(WindowEvent event) {
		eingabe = null;
		dialog.dispose();
	}

	public void windowDeiconified(WindowEvent event) { /* Empty */
	}

	public void windowIconified(WindowEvent event) { /* Empty */
	}

	public void windowActivated(WindowEvent event) { /* Empty */
	}

	public void windowDeactivated(WindowEvent event) { /* Empty */
	}

	public void windowOpened(WindowEvent event) { /* Empty */
	}

	static void addComponent(Container cont, GridBagLayout gbl, Component c,

	int x, int y, int width, int height, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if (kosten == 0) {
				eingabe = input.getValue();
				dialog.dispose();
			} else {
				eingabe = auswahl.getValue();
				dialog.dispose();
			}
			dialog.dispose();

		}

		if (e.getSource() == abbrechen) {
			eingabe = null;
			dialog.dispose();
		}

	}

	public void schliessen() {
		dialog.dispose();
	}
}
