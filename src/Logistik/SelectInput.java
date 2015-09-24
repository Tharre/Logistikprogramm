package Logistik;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import java.sql.*;

import java.util.Vector;

public class SelectInput extends JPanel implements ActionListener,
		FocusListener {

	private String qry;
	private String v = "";
	private String text;
	private String[] heads, data;
	private DBConnection con;
	private String id;
	private String firma;
	// private String firmenname;

	private boolean kst;
	private boolean check = true;
	private boolean aush1 = true;
	private Boolean pruefen = true;
	private Boolean pruefenauf = true;

	private JTextField[] fields = null;
	private JButton btn, btn1;

	private int mat = -1;
	private int hilf;
	private int hilf2;
	private int unterscheidungKorrekturAnUserVonUser;

	public Input input;
	private UserImport user;

	/**
	 * Konstruktor 1; wird unter anderem in der Klasse "LagerumbuchungenC" aufgerufen
	 * 
	 * @param con
	 * @param in
	 * @param qry
	 * @param heads
	 * @param id
	 * @param text
	 * @param user
	 */
	public SelectInput(DBConnection con, Input in, String qry, String[] heads,
			String id, String text, UserImport user) {

		setLayout(new BorderLayout());

		input = in;
		this.qry = qry;
		this.heads = heads;
		this.id = id;
		this.text = text;
		this.con = con;
		this.user = user;

		if (id == null) {
			kst = true;
			input.setEditable(false);
			input.setFocusable(false);
		}

		btn = LayoutButtonCreator.createButton("help.gif", "suchen");
		add(input, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);
		input.addFocusListener(this);
		btn.addActionListener(this);
	}

	/**
	 * Konstruktor 2; wird von EditFirmaMaterial aufgerufen
	 * 
	 * @param con
	 * @param in
	 * @param qry
	 * @param heads
	 * @param id
	 * @param text
	 * @param user
	 * @param hilf
	 */
	public SelectInput(DBConnection con, Input in, String qry, String[] heads,
			String id, String text, UserImport user, int hilf) {

		setLayout(new BorderLayout());

		input = in;

		this.qry = qry;
		this.heads = heads;
		this.id = id;
		this.text = text;
		this.con = con;
		this.user = user;
		this.hilf = hilf;

		if (id == null) {
			kst = true;
			input.setEditable(false);
			input.setFocusable(false);
		}

		btn = LayoutButtonCreator.createButton("help.gif", "suchen");
		add(input, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);
		input.addFocusListener(this);
		btn.addActionListener(this);

	}

	/**
	 * Konstruktor 3;
	 * 
	 * @param con
	 * @param in
	 * @param qry
	 * @param heads
	 * @param id
	 * @param text
	 * @param mat
	 */
	public SelectInput(DBConnection con, Input in, String qry, String[] heads,
			String id, String text, int mat) {

		setLayout(new BorderLayout());

		input = in;
		this.mat = mat;
		this.qry = qry;
		this.heads = heads;
		this.id = id;
		this.text = text;
		this.con = con;

		input.setEditable(false);
		input.setFocusable(false);
		btn = LayoutButtonCreator.createButton("help.gif", "suchen");
		add(input, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);
		input.addFocusListener(this);
		btn.addActionListener(this);
	}

	/**
	 * Konsturktor 4; wird bei Aushaendigen1 und bei einigen Logistik-Abfragen
	 * aufgerufen (z.B.: Material suchen)
	 * 
	 * @param con
	 * @param in
	 * @param qry
	 * @param heads
	 * @param id
	 * @param text
	 * @param pruefen
	 * @param c
	 *            DBVerbindung zur Budget-Datenbank (u.getConnectionKst)
	 * @param iF
	 * @param u
	 * @param hilf2
	 *            -1: der Konstruktor wird von der Funktion "Aushaendigen"
	 *            aufgerufen; 0: er wird von einer anderen Funktion aufgerufen
	 */
	public SelectInput(DBConnection con, Input in, String qry, String[] heads,
			String id, String text, Boolean pruefen, DBConnection c,
			InputFrame iF, UserImport u,
			int unterscheidungKorrekturAnUserVonUser) {

		this.pruefen = pruefen;
		setLayout(new BorderLayout());
		input = in;
		this.qry = qry;
		this.heads = heads;
		this.id = id;
		this.text = text;
		this.con = con;
		user = u;
		hilf2 = unterscheidungKorrekturAnUserVonUser;

		if (id == null) {

			kst = true;
			input.setEditable(false);
			input.setFocusable(false);

		}

		aush1 = true;
		btn = LayoutButtonCreator.createButton("help.gif", "suchen");
		add(input, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);
		input.addFocusListener(this);
		btn.addActionListener(this);

	}

	/**
	 * Konstruktor 5; wird bei einer Recht-Abfrage verwendet
	 * 
	 * @param in
	 * @param heads
	 * @param data
	 */
	public SelectInput(Input in, String[] heads, String[] data) {

		setLayout(new BorderLayout());

		input = in;
		this.heads = heads;
		this.data = data;

		btn1 = LayoutButtonCreator.createButton("help.gif", "suchen");
		add(input, BorderLayout.CENTER);
		add(btn1, BorderLayout.EAST);
		input.addFocusListener(this);
		btn1.addActionListener(this);
	}

	/**
	 * Konstruktor 6; wird in NewBanf aufgerufen; Materialauswahl
	 * 
	 * @param con
	 * @param in
	 * @param fields
	 * @param firma
	 */
	public SelectInput(DBConnection con, Input in, JTextField[] fields,
			String firma) {

		setLayout(new BorderLayout());

		input = in;
		this.fields = fields;
		this.con = con;
		this.firma = firma;
		hilf2 = -2;

		btn = LayoutButtonCreator.createButton("help.gif", "suchen");
		add(input, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);
		input.addFocusListener(this);
		btn.addActionListener(this);
	}

	/**
	 * Konstruktor 7; wird bei der Materialauswahl bei AushaendigenN aufgerufen
	 * 
	 * @param con
	 * @param in
	 * @param fields
	 */
	public SelectInput(DBConnection con, Input in, JTextField[] fields) {

		setLayout(new BorderLayout());

		input = in;
		this.fields = fields;
		this.con = con;
		hilf2 = -4;

		aush1 = false;
		btn = LayoutButtonCreator.createButton("help.gif", "suchen");
		add(input, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);

		input.addFocusListener(this);
		btn.addActionListener(this);

	}

	public void setCheck(boolean b) {
		check = b;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn1) {
			new SelectFrame(this, heads, data);
			return;
		}

		// Aushaendigen1 und AushaendigenN(Material) und Zubuchen (-3)
		if (hilf2 == -1 || hilf2 == -3 || hilf2 == -4 || hilf2 == -5) {
			new SelectAushaendigen(con, fields, this, hilf2, user);

			return;

		}

		if (fields != null) {
			new SelectFrameBanf(con, fields, firma, this);

		} else {
			String q;
			String query;
			Vector<Integer> usergroup = new Vector<Integer>();
			boolean fertig = false;
			DBConnection conb = new DBConnection("logistik_2", "logistik1",
					"4ahwii");

			query = "SELECT * from rechte where recht=45";
			ResultSet rs = conb.mysql_query(query);
			try {
				while (rs.next()) {
					usergroup.add(rs.getInt("usergroup"));
				}

				rs.close();

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			if (hilf == -2) {
				q = "SELECT fm.id,f.firmenname, m.bezeichnung FROM firma f, firma_material fm, material m WHERE fm.firma = f.id    AND m.id = fm.material";

			} else if (mat == -1) {
				q = "SELECT " + getHeads() + " FROM " + qry + " ORDER BY " + id;

			} else {
				q = "SELECT f.id, f.firmenname FROM firma f, firma_material fm WHERE fm.firma=f.id AND fm.material="
						+ mat;

			}
			if (kst) {
				int userGroup = user.getUserGroup();

				for (int i = 0; i < usergroup.size(); i++) {
					if (userGroup == usergroup.get(i)) {

						q = "select raumnummer as Raumnummer,name as Name from abteilungut3 where nummer<>1 UNION";
						q += " select raumnummer as Raumnummer,name as Name from lmb where nummer<>1 and nummer<>2 and kennung=1 UNION";
						q += " select raumnummer as Raumnummer,name as Name from lmb where nummer<>1 and nummer<>2 and kennung=2 UNION";
						q += " select raumnummer as Raumnummer,name as Name from projekt UNION";
						q += " select raumnummer as Raumnnummer,name as Name from kostenstelleut8 ORDER BY name;";

						fertig = true;
					}

				}// end for

				if (!fertig) {

					q = "select raumnummer as Raumnummer,name as Name from projekt UNION";
					q += " select raumnummer as Raumnnummer,name as Name from kostenstelleut8 ORDER BY name;";
				}

				heads = new String[] { "Raumnummer", "Name" };
				id = "Name";

			}
			new SelectFrame(this, q, heads, id, con, kst);

		}// end else

	}// end actionPerformed

	public String getHeads() {

		String s = "";

		for (int i = 0; i < heads.length - 1; i++) {

			s += heads[i] + ", ";

		}

		s += heads[heads.length - 1];

		return s;

	}

	public void setText(String t, String s) {

		v = t;

		input.setText(s);

		input.repaint();

	}

	public boolean checkText() {

		return setText(v);

	}

	public void clear() {

		v = "";

		input.setText("");

		input.repaint();

	}

	public void setT(String t) {

		input.setText(t);

	}

	public boolean setText(String t) {

		pruefenauf = true;

		v = t;

		if (t.equals("")) {
			return false;
		}

		String q = "SELECT " + text + " FROM " + qry + " WHERE " + id

		+ " LIKE '" + t + "'";

		ResultSet rs1 = con.mysql_query(q);

		try {

			rs1.next();

			input.setText(rs1.getString(text));

			rs1.close();

			return true;

		} catch (Exception e) {

			e.getMessage();
			e.printStackTrace();

			if (pruefen && check) {

				new MessageError("Falsche ID!");

			}

			pruefenauf = false;

			return false;

		}

	}

	@Override
	public void setEnabled(boolean b) {

		input.setEditable(b);

		btn.setEnabled(b);

		input.setFocusable(b);

	}

	public void focusGained(FocusEvent e) {

		pruefenauf = true;

		input.setText(v);

	}

	public void focusLost(FocusEvent e) {

		if (pruefenauf) {
			setText(input.getText());
		}

		if (!pruefen && v.length() == 1 && v.charAt(0) == '%') {
			input.setText("%");
		}

	}

	public String getValue() {

		return v;

	}

	public void setTextEnabled(boolean b) {

		input.setEditable(b);

		input.setFocusable(b);

	}

	public String getText() {

		return input.getText();

	}

	public void setValue(String r) {

		v = r;

	}

}
