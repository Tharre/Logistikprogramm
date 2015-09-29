package Logistik;

import javax.swing.*;

import java.awt.*;

import javax.swing.table.*;

import java.text.*;

import java.awt.event.*;

import java.util.*;

import java.sql.*;

/**
 * 
 *
 */

public class LagerAushaendigungN extends LayoutMainPanel implements ActionListener {

	private SelectInput ws, name;

	private JButton ok;

	private JLabel date;

	private JPanel p_c;

	private JPanel p_c2;

	private ArrayList<LayoutRow> rows;

	private JTextField summe;

	private JButton btn_neueZeile, btn_reset, btn_abschicken;

	private JPanel bottom;

	private JCheckBox keep;

	private Input lehrerI = new Input(100, "lehrerI");

	private String[] headl = { "id", "name", "cn" };

	private int materialID;

	private double lagerstk;

	private double meldebestand;

	private String einheits;

	private String usern = "";

	private double preisInkl;

	private DBConnection conb;

	private boolean kstIstLeer = false;

	private boolean antrIstLeer = false;

	private String sFirma;
	private boolean stueckOK = true;

	public LagerAushaendigungN(UserImport user) {

		super(user);

		conb = user.getConnectionKst();

		setLayoutM(new BorderLayout());

		rows = new ArrayList();

		JPanel p = new JPanel(new LayoutBanf(4));

		JLabel lbl = new JLabel("Antragsteller", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p.add(lbl, "20%");

		lbl = new JLabel("Werkstätte/Bereich", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p.add(lbl, "30%");

		lbl = new JLabel("Datum", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p.add(lbl, "*");

		p.add(new JLabel(" ", JLabel.CENTER), "20");

		name = new SelectInput(con, lehrerI, "ldap_user", headl, "cn", "name",

		user);

		p.add(name, "");

		ws = new SelectInput(user.getConnectionKst(), new Input(10,

		"werkstaette"), "werkstaette", new String[] { "Bereichsnr",

		"RaumNr", "Bereichsname" }, null, "", user);

		p.add(ws, "");

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		date = new JLabel(sdf.format(System.currentTimeMillis()), JLabel.CENTER);

		date.setBackground(new Color(230, 230, 230));

		date.setOpaque(true);

		p.add(date, "");

		ok = LayoutButtonCreator.createButton("res/ok.gif", "ok");

		p.add(ok, "");

		ok.addActionListener(this);

		addM(p, BorderLayout.NORTH);

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

	public void addLine() {

		final int i = rows.size();

		Input mfId = new Input(10, "");
		mfId.setEditable(false);
		Input id = new Input(10, "");
		id.setEditable(false);
		Input mate = new Input(10, "");
		mate.setEditable(false);
		Input inv = new Input(10, "");
		inv.setEditable(false);
		Input lagerst = new Input(10, "");
		lagerst.setEditable(false);
		Input einheit = new Input(10, "");
		einheit.setEditable(false);
		Input preisInkl = new Input(10, "");
		preisInkl.setEditable(false);
		Input artNr = new Input(10, "");
		artNr.setEditable(false);
		Input firma = new Input(10, "");
		firma.setEditable(false);
		Input abbuchstk = new Input(10, 10, Input.NUMBER, "");
		LayoutNaviButton clear = LayoutButtonCreator.createButton("res/del.gif",
				"löschen");
		JTextField[] fi = new JTextField[] { mfId, id, mate, lagerst, einheit,
				preisInkl, firma };
		SelectInput mat = new SelectInput(con, mate, fi);
		mat.setTextEnabled(false);
		// p_c.add(mfId,"");
		p_c.add(id, "");
		p_c.add(mat, "");
		p_c.add(lagerst, "");
		p_c.add(einheit, "");
		p_c.add(preisInkl, "");
		p_c.add(firma, "");
		p_c.add(abbuchstk, "");
		p_c.add(clear, "");
		LayoutRow r = new LayoutRow();
		// r.add(mfId);
		r.add(id);
		r.add(mat);
		r.add(lagerst);
		r.add(einheit);
		r.add(preisInkl);
		r.add(firma);
		r.add(abbuchstk);
		r.add(clear);
		rows.add(r);
		revalidate();
		repaint();
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
		btn_abschicken = new JButton("fertig");
		btn_abschicken.addActionListener(this);
		keep = new JCheckBox("Daten beibehalten:");
		bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottom.add(btn_neueZeile);
		bottom.add(btn_reset);
		bottom.add(btn_abschicken);
		JPanel p_cd = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ok.setEnabled(false);
		p_c = new JPanel();
		JScrollPane sp = new JScrollPane(p_c);
		p_c2 = new JPanel(new BorderLayout());
		p_c2.add(sp, BorderLayout.CENTER);
		addM(p_c2, BorderLayout.CENTER);
		addM(bottom, BorderLayout.SOUTH);
		p_c.setLayout(new LayoutBanf(8));
		JLabel lbl = new JLabel("MatID", JLabel.CENTER);
		lbl.setPreferredSize(new Dimension(30, 24));
		lbl.setBackground(new Color(200, 200, 200));
		lbl.setOpaque(true);
		p_c.add(lbl, "3%");

		lbl = new JLabel("Material", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p_c.add(lbl, "*");

		lbl = new JLabel("Lagerstk.", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p_c.add(lbl, "10%");

		lbl = new JLabel("Einheit", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p_c.add(lbl, "7%");

		lbl = new JLabel("Preis inkl.", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p_c.add(lbl, "7%");

		lbl = new JLabel("Firma", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p_c.add(lbl, "20%");

		lbl = new JLabel("abzubuchende Stk.", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p_c.add(lbl, "10%");

		lbl = new JLabel(" ", JLabel.CENTER);

		lbl.setBackground(new Color(200, 200, 200));

		lbl.setOpaque(true);

		p_c.add(lbl, "20");

		addLine();

	}

	public void neuStart() {

		rows = new ArrayList();

		ws.setEnabled(true);

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

	public double getRoundedValue(double d) {

		double v = d * 100;

		int i = (int) Math.round(v);

		return (double) i / 100;

	}

	private boolean bestellt = true;

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == ok) {

			checkHead();

			if (!kstIstLeer && !antrIstLeer) {

				bestellt = ueberpruefeSelbstBestellt();
				System.out.println("bestellt "+bestellt);

			}

		}

		if (e.getSource() == btn_neueZeile) {

			addLine();

		}

		if (e.getSource() == btn_reset) {

			int sure = JOptionPane.showConfirmDialog(null,

			"Wollen Sie den Vorgang wirklich beenden?", "Reset",

			JOptionPane.YES_NO_OPTION);

			if (sure == 1)// nein

			{

				JOptionPane

				.showMessageDialog(null, "Reset-Vorgang abgebrochen");

			} else// ja

			{

				removeAll();

				ws.clear();

				name.clear();

			}

		}

		if (e.getSource() == btn_abschicken) {

			int sure = JOptionPane

			.showConfirmDialog(

			null,

			"Die Materialien werden hiermit abgebucht. Sind Sie sicher?",

			"Fertig", JOptionPane.YES_NO_OPTION);

			if (sure == 1)// nein

			{

				JOptionPane.showMessageDialog(null,

				"Abbuch-Vorgang abgebrochen");

			} else// ja

			{

				abbuchen();

			}

		}

	}

	public void checkHead() {

		kstIstLeer = false;

		antrIstLeer = false;

		if (ws.getValue().equals("")) {

			kstIstLeer = true;

			new MessageError("Bitte wählen Sie eine Kostenstelle aus!");

			return;

		}

		if (name.getValue().equals("")) {

			antrIstLeer = true;

			new MessageError("Bitte wählen Sie einen Antragssteller aus!");

			return;

		} else {
			startCenter();
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

	public void update() {

		for (int i = 0; i < rows.size(); i++) {

			final int u = i;

			LayoutRow r = rows.get(i);

			/*
			 * 
			 * JLabel lbl = (JLabel) r.getData(0); lbl.setText(i + "");
			 */

			JButton del = (JButton) r.getData(7);

			del.removeActionListener(del.getActionListeners()[0]);

			del.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					removeLine(u);

				}

			});

		}

	}

	public void removeLine(int l) {

		LayoutRow r = rows.get(l);

		for (int i = 0; i < 8; i++) {

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

	public void repaint2(Graphics g) {

		if (p_c != null) {
			p_c.setPreferredSize(new Dimension(getWidth() - 90, p_c

			.getPreferredSize().height));
		}

	}

	public void abbuchen() {

		double st = 0;

		int groesse = rows.size();

		stueckOK = true;

		for (int i = 0; i < groesse; i++) {
			LayoutRow r = rows.get(i);

			materialID = Integer.parseInt(((JTextField) r.getData(0))

			.getText());

			String query = "Select m.id, m.lagerort, m.bezeichnung, i.bezeichnung as inventurgruppe, m.bundesnr, m.stueck, m.meldebestand, fm.einheit from material m, firma_material fm, inventurgruppe i where m.id=fm.material AND  m.inventurgruppe=i.id AND m.id="
					+ materialID;

			ResultSet rs = con.mysql_query(query);
			try {
				while (rs.next()) {

					lagerstk = rs.getDouble("stueck");

				}

				rs.close();

			} catch (Exception e) {

			}

			st = Double.parseDouble(((JTextField) r.getData(6)).getText());

			if (st > lagerstk) {
				stueckOK = false;
				JOptionPane.showMessageDialog(null, "Es können max. "

				+ lagerstk

				+ " Stück ausgehändigt werden!\n Es sind nur mehr "

				+ lagerstk + " Stück im Lager!");

				return;
			}
			if (st <= 0) {

				stueckOK = false;

				JOptionPane.showMessageDialog(null,

				"Die Stückzahl darf nicht kleiner, gleich 0 sein!");

				return;

			}
		}

		if (stueckOK) {
			for (int i = 0; i < groesse; i++) {

				try {

					LayoutRow r = rows.get(i);

					st = Double.parseDouble(((JTextField) r.getData(6))
							.getText());

					materialID = Integer.parseInt(((JTextField) r.getData(0))

					.getText());

					preisInkl = Double.parseDouble(((JTextField) r.getData(4))

					.getText());

					sFirma = ((JTextField) r.getData(5)).getText();

					String query = "Select m.id, m.lagerort, m.bezeichnung, i.bezeichnung as inventurgruppe, m.bundesnr, m.stueck, m.meldebestand, fm.einheit from material m, firma_material fm, inventurgruppe i where m.id=fm.material AND  m.inventurgruppe=i.id AND m.id="
							+ materialID;

					ResultSet rs = con.mysql_query(query);

					while (rs.next()) {

						lagerstk = rs.getDouble("stueck");

						meldebestand = rs.getInt("meldebestand");

						einheits = rs.getString("einheit");

					}

					rs.close();

					String sql = "";

					sql = "UPDATE material set stueck=stueck-" + st

					+ " where id = " + materialID + "";

					if (meldebestand >= lagerstk - st) {
						JOptionPane.showMessageDialog(null, "Meldebestand von "

						+ meldebestand

						+ " Einheiten unterschritten.\n nur mehr "

						+ (lagerstk - st) + " im Lager!");
					}

					con.mysql_update(sql);

					long datum = System.currentTimeMillis() / 1000;

					String sel = "Select cn from ldap_user where name like "

					+ lehrerI.getValue();

					ResultSet rs2 = con.mysql_query(sel);

					while (rs2.next()) {

						usern = rs2.getString("cn");

					}

					rs2.close();

					int firm = 0;

					String kostenst = ws.getValue();

					String queryFirma = "select id from firma where firmenname like '"
							+ sFirma + "'";
					ResultSet rsFirma = con.mysql_query(queryFirma);

					while (rsFirma.next()) {
						firm = rsFirma.getInt("id");
					}

					String insert = "INSERT INTO buchungen (material , stk , user , kst , datum, firma ) VALUES ("

							+ materialID

							+ ", "

							+ (-st)

							+ ", '"

							+ usern

							+ "','"

							+ ws.getValue() + "', " + datum + "," + firm + ")";

					con.mysql_update(insert);

					if (!bestellt) {

						String query3 = "UPDATE kostenstelleut8 set ausgegeben=ausgegeben-"

								+ (preisInkl * st)
								+ " where nummerSelbst like '1.1.1.4'";

						conb.mysql_update(query3);

						String query2 = "UPDATE kostenstelleut8 set ausgegeben=ausgegeben+"

								+ (preisInkl * st)
								+ " where name like '"
								+ kostenst + "'";

						conb.mysql_update(query2);

					}

				}

				catch (SQLException ex) {

					ex.printStackTrace();

				} catch (NumberFormatException nfe) {

					nfe.printStackTrace();

					JOptionPane.showMessageDialog(null,

					"Bitte füllen Sie das Feld \"abzubuchende Stück\" aus");

				}

			}
		}

		JOptionPane.showMessageDialog(null,

		"Die Daten wurden erfolgreich umgebucht!");

		removeAll();

		ws.clear();

		name.clear();

	}
	
	public boolean ueberpruefeSelbstBestellt()
	{
		int speichern = JOptionPane

				.showConfirmDialog(

						null,

						"Hat die angegebene Kostenstelle das Material selbst bestellt?",

						"Abfrage", JOptionPane.YES_NO_OPTION);

		if (speichern == 1)// nein

		{

			bestellt= false;
			

		} else if (speichern == 0)// ja

		{

			bestellt= true;

		} else //Fenster geschlossen, speichern=-1
		{
			
			JOptionPane.showMessageDialog(null, "Sie haben nichts ausgewählt!");
			ueberpruefeSelbstBestellt();
		}
		return bestellt;
	}

}
