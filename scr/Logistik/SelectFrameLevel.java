package Logistik;

import javax.swing.*;

import java.sql.*;
import java.awt.event.*;

/**
 * Fenster, welches beim Klicken des Auswahlbuttons bei SelectInputLevels
 * angezeigt wird Beinhaltet die Daten einer Tabelle und einen Auswahlbutton in
 * hierarchischer Form Beim Klicken des Auswahlbuttons werden die Daten der
 * gewählten Reihe ins SelectInput übernommen
 */

public class SelectFrameLevel extends JDialog {
	private SelectInputLevel field;
	private String table;
	private String[] head;
	private LayoutLevelPanel p;
	private JScrollPane sp;
	private String nr;
	private String uber;
	private DBConnection con;
	private boolean vis = true;
	public int bgId=0;

	public SelectFrameLevel(SelectInputLevel field, String table,
			String[] head, String nr, String uber, DBConnection con) {
		super(new JFrame(), "Select", true);

		setSize(500, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.field = field;
		this.table = table;
		this.head = head;
		this.con = con;
		this.nr = nr;
		this.uber = uber;
		p = new LayoutLevelPanel();
		addSelects();
		sp = new JScrollPane(p);
		add(sp);
		setVisible(true);
	}

	public void setVis(boolean b) {
		vis = b;
	}

	ResultSet rs2;
	
	public void addSelect(ResultSet rs, int lvl) throws Exception {

		while (rs.next()) {
		

			JPanel pn = new JPanel();
			for (int i = 0; i < head.length; i++) {
				if (!head[i].equals(""+nr) && !head[i].equals(""+uber)) {
					JLabel lbl = new JLabel(rs.getString(""+nr) + "  "
							+ rs.getString(head[i]));
					
					lbl.setBorder(null);
					pn.add(lbl);
					
				}
			}
			final String ids = rs.getString(nr);
			
			final int bgId=rs.getInt("id");
		

			JButton btn = LayoutButtonCreator.createButton("ok.gif", "auswählen");
			btn.setVisible(vis);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					field.setText(ids);
					field.setText(ids, ""+bgId);
					
					
					dispose();
				}
			});
			
			btn.setBorder(null);

			pn.add(btn);
			p.add(pn, lvl);
			String qry = "SELECT id,";
			for (int i = 0; i < head.length; i++) {
				qry += head[i];
				if (i < head.length - 1) {
					qry += ",";
				}
			}
			qry += "  FROM " + table + " WHERE " + uber + "="
					+ rs.getInt("id") + " order by "+head[1]+" asc;";
			rs2 = con.mysql_query(qry);
			addSelect(rs2, lvl + 1);

			rs2.close();
		}//while

		//rs2.close();
		rs.close();
	}

	public void addSelects() {
		int lvl = 0;
		try {
			String qry = "SELECT id, ";
			for (int i = 0; i < head.length; i++) {
				qry += head[i];
				if (i < head.length - 1) {
					qry += ",";
				}
			}
			qry += "  FROM " + table + " WHERE " + uber + " IS null;";
			
			
			ResultSet rs = con.mysql_query(qry);
			addSelect(rs, lvl);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	public int getBgId()
	{
		return bgId;
	}
}