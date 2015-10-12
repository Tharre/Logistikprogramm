package Logistik;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.text.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import java.io.*;

/**
 * Klasse zum Erstellen von Bestellanforderungen Als erstes müssen Kostenstelle
 * und Firma angegeben werden Danach werden die Bestellpositionen erstellt Nach
 * dem Klicken des Speichern-Buttons werden die Daten in die Datenbank
 * gespeichert
 */

public class NewBanf extends LayoutMainPanel implements ActionListener {
	private SelectInput ws, firma;
	private JButton ok;
	private JLabel name, date;
	private JPanel p_c;
	private JPanel p_c2;
	private ArrayList<LayoutRow> rows;
	private JTextField summe;
	private JButton btn_neueZeile, btn_reset, btn_abschicken;
	private JPanel bottom;
	private JCheckBox keep;

	public NewBanf(UserImport user) {
		super(user);

		setLayoutM(new BorderLayout());
		rows = new ArrayList();
		JPanel p = new JPanel(new LayoutBanf(5));
		JLabel lbl = new JLabel("Antragsteller", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p.add(lbl, "20%");
		lbl = new JLabel("Werkstätte/Bereich", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p.add(lbl, "30%");
		lbl = new JLabel("Firma", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p.add(lbl, "*");
		lbl = new JLabel("Datum", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p.add(lbl, "15%");
		p.add(new JLabel(" ", JLabel.CENTER), "20");
		name = new JLabel(user.getName(), JLabel.CENTER);
		name.setBackground(new Color(230, 230, 230));
		name.setOpaque(true);
		p.add(name, "");
		ws = new SelectInput(user.getConnectionKst(), new Input(10,
				"werkstaette"), "werkstaette", new String[] { "Bereichsnr",
				"RaumNr", "Bereichsname" }, null, "", user);
		p.add(ws, "");
		firma = new SelectInput(con, new Input(10, "firma"), "firma",
				new String[] { "id", "firmenname" }, "id", "firmenname", user);
		p.add(firma, "");
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		date = new JLabel(sdf.format(System.currentTimeMillis()), JLabel.CENTER);
		date.setBackground(new Color(230, 230, 230));
		date.setOpaque(true);
		p.add(date, "");
		ok = LayoutButtonCreator.createButton("../ok.gif", "ok");
		p.add(ok, "");
		ok.addActionListener(this);
		addM(p, BorderLayout.NORTH);
	}

	public void setBestellung(int id) {
		String qry = "SELECT firma,datum FROM bestellung WHERE id=" + id + ";";
	}

	public void addLine() {
		updateSumme();
		JLabel lbl = new JLabel("" + rows.size(), JLabel.CENTER);
		final int i = rows.size();
		lbl.setBackground(new Color(230, 230, 230));
		lbl.setOpaque(true);
		p_c.add(lbl, "");
		Input bestNr = new Input(10, "");
		bestNr.setEditable(false);
		Input menge = new Input(5, 5, Input.NUMBER, "");
		menge.setText("0");
		Input einheit = new Input(10, "");
		einheit.setEditable(false);
		Input preisExkl = new Input(10, 10, Input.NUMBER, "");
		Input mwst = new Input(10, "");
		mwst.setEditable(false);
		Input preisInkl = new Input(12, 12, Input.NUMBER, "");
		Input ges = new Input(10, "");
		ges.setEditable(false);
		ges.setText("0");
		LayoutNaviButton clear = LayoutButtonCreator.createButton("../del.gif", "löschen");
		Input mate = new Input(10, "");
		JTextField[] fi = new JTextField[] { mate, bestNr, einheit, preisExkl,
				mwst, preisInkl };
		SelectInput mat = new SelectInput(con, mate, fi, firma.getValue());
		mat.setTextEnabled(false);
		p_c.add(mat, "");
		p_c.add(bestNr, "");
		p_c.add(menge, "");
		p_c.add(einheit, "");
		p_c.add(preisExkl, "");
		p_c.add(mwst, "");
		p_c.add(preisInkl, "");
		p_c.add(ges, "");
		p_c.add(clear, "");
		LayoutRow r = new LayoutRow();
		r.add(lbl);
		r.add(mat);
		r.add(bestNr);
		r.add(menge);
		r.add(einheit);
		r.add(preisExkl);
		r.add(mwst);
		r.add(preisInkl);
		r.add(ges);
		r.add(clear);
		rows.add(r);
		revalidate();
		repaint();
		menge.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				updatePreise(i);
			}
		});
		preisExkl.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				updatePreisInkl(i);
			}
		});
		preisInkl.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				updatePreisExkl(i);
			}
		});
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeLine(i);
			}
		});
	}

	public void startCenter() {
		btn_neueZeile = new JButton("neue Zeile");
		btn_neueZeile.addActionListener(this);
		btn_reset = new JButton("reset");
		btn_reset.addActionListener(this);
		btn_abschicken = new JButton("abschicken");
		btn_abschicken.addActionListener(this);
		keep = new JCheckBox("Daten beibehalten:");
		bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottom.add(btn_neueZeile);
		bottom.add(btn_reset);
		bottom.add(btn_abschicken);
		summe = new JTextField(10);
		summe.setEditable(false);
		JLabel s = new JLabel("Summe: ");
		JPanel p_cd = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_cd.add(s);
		p_cd.add(summe);
		ok.setEnabled(false);
		p_c = new JPanel();
		JScrollPane sp = new JScrollPane(p_c);
		p_c2 = new JPanel(new BorderLayout());
		p_c2.add(p_cd, BorderLayout.SOUTH);
		p_c2.add(sp, BorderLayout.CENTER);
		addM(p_c2, BorderLayout.CENTER);
		addM(bottom, BorderLayout.SOUTH);
		p_c.setLayout(new LayoutBanf(10));
		JLabel lbl = new JLabel("Nr", JLabel.CENTER);
		lbl.setPreferredSize(new Dimension(30, 24));
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "3%");
		lbl = new JLabel("Material", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "*");
		lbl = new JLabel("Best.Nr", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "10%");
		lbl = new JLabel("Menge", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "7%");
		lbl = new JLabel("Einheit", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "7%");
		lbl = new JLabel("Preis Exkl", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "10%");
		lbl = new JLabel("MWST", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "7%");
		lbl = new JLabel("Preis Inkl", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "10%");
		lbl = new JLabel("Preis Ges", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "12%");
		lbl = new JLabel(" ", JLabel.CENTER);
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "20");
		addLine();
	}

	public void neuStart() {
		rows = new ArrayList();
		ws.setEnabled(true);
		firma.setEnabled(true);
		ws.setTextEnabled(false);
		removeM(p_c2);
		removeM(bottom);
		ok.setEnabled(true);
	}

	public void removeAll() {
		for (int i = rows.size() - 1; i >= 0; i--) {
			removeLine(i);
		}
	}

	public void updatePreise(int e)

	{
		LayoutRow r = rows.get(e);

		JTextField menge = (JTextField) r.getData(3);
		JTextField preisInkl = (JTextField) r.getData(7);
		JTextField ges = (JTextField) r.getData(8);
		if (menge.getText().equals("") || preisInkl.getText().equals("")
				|| ges.getText().equals("")) {
			ges.setText("0");
			updateSumme();
			return;
		}
		double m = Double.parseDouble(menge.getText());
		double pi = Double.parseDouble(preisInkl.getText());
		double pg = m * pi;
		BigDecimal d = new BigDecimal(pg);
		d = d.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
		ges.setText(d + "");
		updateSumme();
	}

	public void updatePreisInkl(int e) {

		LayoutRow r = rows.get(e);
		JTextField preisInkl = (JTextField) r.getData(7);
		JTextField preisExkl = (JTextField) r.getData(5);
		JTextField mwst = (JTextField) r.getData(6);
		if (preisExkl.getText().equals("") || mwst.getText().equals("")) {
			return;
		}
		double pe = Double.parseDouble(preisExkl.getText());
		double mws = Double.parseDouble(mwst.getText().substring(0,
				mwst.getText().length() - 1));
		double pi = pe + pe * (mws / 100);
		;

		preisInkl.setText(getRoundedValue(pi) + "");
		updatePreise(e);
	}

	public void updatePreisExkl(int e) {

		LayoutRow r = rows.get(e);
		JTextField preisInkl = (JTextField) r.getData(7);
		JTextField preisExkl = (JTextField) r.getData(5);
		JTextField mwst = (JTextField) r.getData(6);
		if (preisInkl.getText().equals("") || mwst.getText().equals("")) {
			return;
		}
		double pi = Double.parseDouble(preisInkl.getText());
		double mws = Double.parseDouble(mwst.getText().substring(0,
				mwst.getText().length() - 1));
		double pe = 100 * pi / (100 + mws);
		preisExkl.setText(getRoundedValue(pe) + "");
		updatePreise(e);
	}

	public double getRoundedValue(double d) {
		double v = d * 100;
		int i = (int) Math.round(v);
		return (double) i / 100;
	}

	public void updateSumme() {
		double sum = 0;
		for (int i = 0; i < rows.size(); i++) {
			LayoutRow r = rows.get(i);
			double d = Double
					.parseDouble(((JTextField) r.getData(8)).getText());
			sum += d;
		}
		summe.setText(getRoundedValue(sum) + "");
	}

	public boolean checkAll() {
		boolean b = true;
		for (int i = 0; i < rows.size(); i++) {
			if (!checkLine(i)) {
				b = false;
			}
		}
		if (!b) {
			new MessageError("Bitte alle Zeilen korrekt ausfüllen!");
		}
		return b;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			checkHead();
		}
		if (e.getSource() == btn_neueZeile) {
			addLine();
		}
		if (e.getSource() == btn_reset) {
			removeAll();
			ws.clear();
			firma.clear();
		}
		if (e.getSource() == btn_abschicken) {
			if (checkAll()) {
				save();
			}
		}
	}

	public void checkHead() {
		if (ws.getValue().equals("")) {
			new MessageError("Bitte alle Felder ausfüllen!");
			return;
		}
		if (firma.checkText()) {
			ws.setEnabled(false);
			firma.setEnabled(false);
			startCenter();
		} else {
			new MessageError("Bitte alle Felder ausfüllen!");
		}
	}

	class ComponentRenderer implements TableCellRenderer {
		public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (value.getClass() != String.class) {
				JComponent c = (JComponent) value;
				c.setOpaque(true);
				return c;
			}
			return null;
		}

	}

	public boolean checkLine(int l) {
		LayoutRow r = rows.get(l);
		SelectInput c = (SelectInput) r.getData(1);
		if (c.getText().equals("")) {
			return false;
		}
		JTextField f = (JTextField) r.getData(8);
		try {
			if (Double.parseDouble(f.getText()) < 0) {
				return false;
			}
			f = (JTextField) r.getData(3);
			if (Double.parseDouble(f.getText()) <= 0) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void update() {
		for (int i = 0; i < rows.size(); i++) {
			final int u = i;
			LayoutRow r = rows.get(i);
			JLabel lbl = (JLabel) r.getData(0);
			lbl.setText(i + "");
			JButton del = (JButton) r.getData(9);
			del.removeActionListener(del.getActionListeners()[0]);
			del.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeLine(u);
				}
			});
		}
		updateSumme();
	}

	public void removeLine(int l) {

		LayoutRow r = rows.get(l);
		for (int i = 0; i < 10; i++) {
			p_c.remove((Component) r.getData(i));
		}
		rows.remove(l);
		update();
		if (rows.size() == 0) {
			neuStart();
		}
		revalidate();
		repaint();
	}

	public void save() {
		String antragsteller = user.getCn();
		String kostenstelle = ws.getValue();
		String firm = firma.getValue();
		int status = 3;
		long datum = System.currentTimeMillis() / 1000;
		String qry = "INSERT INTO banf (antragsteller, kostenstelle,firma,status,datum) VALUES";
		qry += " ('" + antragsteller + "', '" + kostenstelle + "', " + firm
				+ ", " + status + ", " + datum + ");";
		String banf = "";
		if (con.mysql_update(qry) == -1) {
			new MessageError("Anlegen fehlgeschlagen!");
			return;
		}

		ResultSet fS = null;
		PDFMaker d = null;
		try {
			String f = "SELECT id,firmenname, plz,ort,strasse FROM firma WHERE id = "
					+ firm + ";";
			fS = con.mysql_query(f);
			fS.next();
			String idQry = "SELECT id FROM banf ORDER BY id DESC LIMIT 1";
			ResultSet rs = con.mysql_query(idQry);
			rs.next();
			banf = rs.getString("id");

			d = new PDFMaker(FTP.temp() + banf + ".pdf","");
			d.setAddress("BANF");
			d.addFirma(fS.getString("firmenname"), fS.getString("strasse"), fS
					.getString("plz")
					+ " " + fS.getString("ort"));
			d.addAuftrag("-", "-", "-", "-","-","-");
			d.addAngebot("-", "-", "-");

			fS.close();
			rs.close();

		} catch (Exception e) {
			e.getMessage();
		}
		for (int i = 0; i < rows.size(); i++) {
			try {
				LayoutRow r = rows.get(i);
				String menge = ((JTextField) r.getData(3)).getText();
				String bezeichnung = ((SelectInput) r.getData(1)).getValue();
				double preisExkl = getRoundedValue(Double
						.parseDouble(((JTextField) r.getData(5)).getText()));
				String mwst = (((JTextField) r.getData(6)).getText())
						.substring(0, (((JTextField) r.getData(6)).getText())
								.length() - 1);
				String st = "1";

				String einheit = ((JTextField) r.getData(4)).getText();
				String q = "INSERT INTO banfpos (menge, bezeichnung, preisExkl, mwst, status, banf, einheit) VALUES (";
				q += menge + ", " + bezeichnung + ", " + preisExkl + ", "
						+ mwst + ", " + st + ", " + banf + ", '" + einheit
						+ "');";
				String qq = "SELECT firma,material,artNr FROM firma_material WHERE firma="
						+ firm + " and material=" + bezeichnung + ";";
				ResultSet rr = con.mysql_query(qq);
				rr.next();
				String qqq = "SELECT id,bezeichnung FROM material WHERE id = "
						+ bezeichnung + ";";
				ResultSet rrr = con.mysql_query(qqq);
				rrr.next();
				d.addRow(rrr.getString("bezeichnung"), rr.getString("artNr"),
						"" + menge, "" + einheit, "" + preisExkl, "" + mwst, ""
								+ (double) (Double.parseDouble(menge)
										* preisExkl * (1 + Double
										.parseDouble(mwst) / 100)));
				if (con.mysql_update(q) == -1) {
					String q2 = "DELETE FROM banf WHERE id=" + banf + ";";
					con.mysql_update(q2);
					new MessageError("Anlegen fehlgeschlagen!");
					return;
				}

				rr.close();
				rrr.close();

			} catch (Exception e) {
			}
		}
		IMAP.sendMail(user.getMail(),
				new String[] { "karin.walka@htl-hl.ac.at" },
				new String[] { "leopold.mayer@htl-hl.ac.at" }, "neue Banf",
				"Es ist eine neue Banf von " + user.getName()
						+ " erstellt worden.\n\nBanf-ID: " + banf, null);
		d.addFooter("-", "-", "-", "-", user.getName(), "-", "-",false);
		d.close();
		File f = new File(FTP.temp() + banf + ".pdf");
		try {
			FTP.upload(f, "BANF");
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		if (!keep.isSelected()) {
			removeAll();
			ws.clear();
			firma.clear();
		}
		new MessageSucess("neue Banf angelegt!");
	}

	public void repaint2(Graphics g) {
		if (p_c != null) {
			p_c.setPreferredSize(new Dimension(getWidth() - 90, p_c
					.getPreferredSize().height));
		}
	}

}