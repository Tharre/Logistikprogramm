package Logistik;

import java.awt.*;
import javax.swing.*;

import java.sql.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;

public class SelectFrame extends JDialog implements ActionListener {
	private SelectInput field;
	private String qry;
	private String[] head, data;
	private JPanel p;
	private JScrollPane sp;
	private String id;
	private DBConnection con;
	private boolean kst;
	private ArrayList rows;
	private JTextField search;
	private JComboBox box;
	int anzahlUt3;
	int anzahlLmb1;
	int anzahlLmb2;
	int anzahlProjekt;
	int anzahlUt8;
	JButton btnSearch;
	String suchErgebnis;

	/**
	 * Konstruktor 1; wird von SelectInput aufgerufen bei diversen
	 * Logistik-BudgetAbfragen
	 * 
	 * @param field
	 * @param qry
	 * @param head
	 * @param id
	 * @param con
	 * @param b
	 */
	public SelectFrame(SelectInput field, String qry, String[] head, String id,
			DBConnection con, boolean b) {
		// super(null,"Select");

		setSize(700, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		box = new JComboBox(head);
		search = new JTextField(10);
		btnSearch = new JButton("Go");
		btnSearch.addActionListener(this);
		JPanel headP = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headP.add(new JLabel("Suchen:"));
		headP.add(search);
		headP.add(box);
		headP.add(btnSearch);
		//box.addActionListener(this);

		// search.getDocument().addDocumentListener(new MyListener());
		rows = new ArrayList();
		this.field = field;
		this.qry = qry;
		this.head = head;
		this.con = con;
		this.id = id;
		kst = b;

		p = new JPanel();
		addSelects();
		sp = new JScrollPane(p);
		setLayout(new BorderLayout());
		add(headP, BorderLayout.NORTH);
		add(sp, BorderLayout.CENTER);
		setModal(true);
		setVisible(true);

	}

	public SelectFrame(SelectInput field, String[] head, String[] data) {
		this.field = field;
		this.head = head;
		this.data = data;
		setSize(500, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		p = new JPanel();
		addRechte();
		sp = new JScrollPane(p);
		add(sp);
		setModal(true);
		setVisible(true);
	}

	public SelectFrame(SelectInput field, String qry, String[] head, String id,
			DBConnection con) {
		this(field, qry, head, id, con, false);
	}

	public SelectFrame(DBConnection con, JTextField[] fields, String firma,
			SelectInput ip, String[] head, String[] data) {
		this.head = head;
		this.data = data;
		setSize(500, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		p = new JPanel();
		addRechte();
		sp = new JScrollPane(p);
		add(sp);
		setModal(true);
		setVisible(true);
	}

	public void setKst(boolean b) {
		kst = b;
	}

	public void addRechte() {
		p.setLayout(null);
		p.setLayout(new LayoutBanf(head.length + 1));
		for (int i = 0; i < head.length; i++) {
			p.add(new JLabel(head[i], SwingConstants.CENTER), ""
					+ ((getWidth() - 45) / head.length));
		}
		p.add(new JLabel(""), "20");
		for (int i = 0; i < data.length; i++) {
			p.add(new JLabel("" + i), "");
			p.add(new JLabel(data[i]), "");
			final int z = i;
			JButton btn = LayoutButtonCreator.createButton("ok.gif",
					"auswählen");
			p.add(btn, "");
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					field.setT("" + z);
					dispose();
				}
			});
		}

	}

	public void updateS() {
		String s = search.getText().toLowerCase();

		p = null;
		p = new JPanel();
		p.setLayout(new LayoutBanf(head.length + 1));
		for (int i = 0; i < head.length; i++) {
			p.add(new JLabel(head[i], SwingConstants.CENTER), ""
					+ ((getWidth() - 45) / head.length));
		}
		p.add(new JLabel(""), "20");
		for (int i = 0; i < rows.size(); i++) {
			LayoutRow r = (LayoutRow) rows.get(i);
			if (s.equals("")
					|| (((((JLabel) r.getData(box.getSelectedIndex())))
							.getText()).toLowerCase()).indexOf(s) != -1) {
				for (int z = 0; z < r.size(); z++) {
					p.add((JComponent) r.getData(z), "");
				}
			}
		}
		sp.setViewportView(p);

		repaint();
	}

	public void addSelects() {
		p.setLayout(null);

		try {
			p.setLayout(new LayoutBanf(head.length + 1));
			for (int i = 0; i < head.length; i++) {
				p.add(new JLabel(head[i], SwingConstants.CENTER), ""
						+ ((getWidth() - 45) / head.length));
			}
			p.add(new JLabel(""), "20");

			ResultSet rs = con.mysql_query(qry);
			while (rs.next()) {
				LayoutRow r = new LayoutRow();
				for (int i = 0; i < head.length; i++) {
					JLabel lbl = new JLabel(rs.getString(head[i]));
					lbl.setSize(30, 10);
					p.add(lbl, "");

					r.add(lbl);
				}
				final String ids = rs.getString(id);
				String s3 = "";
				if (kst) {
					s3 = rs.getString("Name");
				}
				final String tx = s3;
				JButton btn = LayoutButtonCreator.createButton("ok.gif",
						"auswählen");
				// JButton btn = new JButton("");
				p.add(btn, "");
				r.add(btn);
				rows.add(r);
				btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (kst) {
							field.setText(ids, tx);
						} else {
							field.setText(ids);
						}
						dispose();
					}
				});
			}
			validate();
			repaint();

			rs.close();

		} catch (Exception e) {
			e.getMessage();
		}
	}

	/*class MyListener implements DocumentListener {
		public void changedUpdate(DocumentEvent documentEvent) {
			updateS();
		}

		public void insertUpdate(DocumentEvent documentEvent) {
			updateS();
		}

		public void removeUpdate(DocumentEvent documentEvent) {
			updateS();
		}
	}*/

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSearch) {
			updateS();
		}
	}
}
