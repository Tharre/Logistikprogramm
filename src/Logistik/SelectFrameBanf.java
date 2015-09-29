package Logistik;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.sql.*;
import java.util.ArrayList;
import java.awt.event.*;

/**
 * Fenster, welches beim Klicken des Auswahlbuttons bei SelectInputs in der Banf
 * angezeigt wird Beinhaltet die Daten einer Tabelle und einen Auswahlbutton
 * Beim Klicken des Auswahlbuttons werden die Daten der gewählten Reihe ins
 * SelectInput übernommen
 */

public class SelectFrameBanf extends JDialog {
	private JTextField[] fields;
	private JPanel p;
	private JScrollPane sp;
	private DBConnection con;
	private String firma;
	private SelectInput ip;
	private JComboBox box;
	private JTextField search;
	private ArrayList rows;
	private String[] head = { "Material", "ArtNr", "Inventurgruppe" };

	public SelectFrameBanf(DBConnection con, JTextField[] fields, String firma,
			SelectInput ip) {
		setSize(500, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.fields = fields;
		this.firma = firma;
		this.con = con;
		this.ip = ip;
		p = new JPanel();
		setLayout(new BorderLayout());
		sp = new JScrollPane(p);
		setVisible(true);

		box = new JComboBox(head);
		search = new JTextField(10);
		JPanel headP = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headP.add(new JLabel("Suchen:"));
		headP.add(search);
		headP.add(box);
		box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateS();
			}
		});
		search.getDocument().addDocumentListener(new MyListener());
		rows = new ArrayList();

		addSelects();

		add(headP, BorderLayout.NORTH);
		add(sp, BorderLayout.CENTER);

	}

	public void addSelects() {
		p.setLayout(null);
		p.setLayout(new LayoutBanf(4));

		p.add(new JLabel("Bezeichnung", SwingConstants.CENTER), "*");
		p.add(new JLabel("ArtikelNr", SwingConstants.CENTER), "120");
		p.add(new JLabel("Inventurgruppe", SwingConstants.CENTER), "120");
		p.add(new JLabel(""), "20");
		try {
			String qry = "SELECT material as mID,m.bezeichnung as Material, artnr as ArtNr, einheit, preisExkl, mwst, inventurgruppe, ig.bezeichnung as Inventurgruppe, ig.id FROM firma_material fm, material m, inventurgruppe ig WHERE firma= "
					+ firma
					+ " AND m.id = fm.material and ig.id= m.inventurgruppe order by m.bezeichnung;";
			ResultSet rs = con.mysql_query(qry);
			while (rs.next()) {
				LayoutRow r = new LayoutRow();
				for (int i = 0; i < head.length; i++) {
					JLabel lbl = new JLabel(rs.getString(head[i]));
					lbl.setSize(30, 10);
					p.add(lbl, "");

					r.add(lbl);
				}

				JButton btn = LayoutButtonCreator.createButton("res/ok.gif",
						"auswählen");
				r.add(btn);
				rows.add(r);

				String artN = rs.getString("ArtNr");
				String einhei = rs.getString("einheit");
				String preisExk = rs.getString("preisExkl");
				String mws = rs.getString("mwst");

				String id = rs.getString("mID");
				final String bezeichnung = rs.getString("Material");
				final String artNr = artN;
				final String einheit = einhei;
				final String preisExkl = "" + preisExk;
				final String mwst = "" + mws;
				final String is = id;

				p.add(btn, "");
				btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						ip.setValue(is);
						fields[0].setText(bezeichnung);
						fields[1].setText(artNr);
						fields[2].setText(einheit);
						fields[3].setText(preisExkl);
						fields[4].setText(mwst + "%");
						fields[5].setText(runde(Double.parseDouble(preisExkl)
								+ ((Double.parseDouble(preisExkl) * Integer
										.parseInt(mwst)) / 100))
								+ "");
						dispose();
					}
				});
			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	public double runde(double zahl) {
		zahl = zahl * 100;
		zahl = Math.round(zahl);
		zahl = zahl / 100;
		return zahl;

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

	class MyListener implements DocumentListener {
		public void changedUpdate(DocumentEvent documentEvent) {
			updateS();
		}

		public void insertUpdate(DocumentEvent documentEvent) {
			updateS();
		}

		public void removeUpdate(DocumentEvent documentEvent) {
			updateS();
		}
	}
}