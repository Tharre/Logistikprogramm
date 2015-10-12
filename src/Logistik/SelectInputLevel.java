package Logistik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Beinhaltet ein Input und einen Auswahlbutton Beim Klicken des Auswahlbuttons
 * wird ein SelectFrameLevel angezeigt
 */

public class SelectInputLevel extends JPanel implements ActionListener,
		FocusListener {
	public Input input;
	private JButton btn;
	private String table;
	private String[] heads;
	private DBConnection con;
	private String nr;
	private String v = "";
	private String text;
	private String uber;
	private NewBundesgruppe g = null;
	private SelectFrameLevel sfl;
	private int bgId=0;
	int l=0;

	/**
	 * unter anderem von NewMaterial aufgerufen
	 * @param con
	 * @param in
	 * @param table
	 * @param heads
	 * @param nr
	 * @param text
	 * @param uber
	 */
	public SelectInputLevel(DBConnection con, Input in, String table,
			String[] heads, String nr, String text, String uber) {
		setLayout(new BorderLayout());
	
		input = in;
		this.table = table;
		this.heads = heads;
		this.nr = nr;
		this.text = text;
		this.con = con;
		this.uber = uber;
		btn = LayoutButtonCreator.createButton("../help.gif", "suchen");
		add(input, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);
		input.addFocusListener(this);
		btn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		sfl = new SelectFrameLevel(this, table, heads, nr, uber, con);
		bgId=sfl.getBgId();
		
	}

	public String getHeads() {
		String s = "";
		for (int i = 0; i < heads.length - 1; i++) {
			s += heads[i] + ", ";
		}
		s += heads[heads.length - 1];
		return s;
	}

	public boolean setText(String t, String s) {
		v = t;
		l=Integer.parseInt(s);
		if (g != null) {
			g.setBG(t);
		}
	
		if (t.equals("")) {
			return false;
		}
		String q = "SELECT " + text + " FROM " + table + " WHERE id ="+s;
		ResultSet rs = con.mysql_query(q);
		try {
			rs.next();
			input.setText(rs.getString(text));
			input.repaint();
			rs.close();

			return true;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			new MessageError("Falsche ID!");
			return false;
		}
		
	}

	public boolean checkText() {
		return setText(v);
	}

	public void clear() {
		v = "";
		input.setText("");
		input.repaint();
	}

	public void setBg(NewBundesgruppe nb) {
		g = nb;
	}

	public boolean setText(String t) {
		if (g != null) {
			g.setBG(t);
		}
		v = t;
		if (t.equals("")) {
			return false;
		}
		String q = "SELECT " + text + " FROM " + table + " WHERE " + nr + " ='"
				+ t + "'";
		ResultSet rs = con.mysql_query(q);
		try {
			rs.next();
			input.setText(rs.getString(text));
			rs.close();

			return true;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			new MessageError("Falsche ID!");
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
		input.setText(v);
	}

	public void focusLost(FocusEvent e) {
		setText(input.getText());
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
	
	public int getBgId()
	{
		return l;
	}
}