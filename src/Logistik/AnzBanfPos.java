package Logistik;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

/**
 * Ansicht beim Betsellablauf Alle ausgewählten BanfPositionen werden angezeigt.
 * Diese können per RadioButton abgewiesen/bestellt werden
 */
public class AnzBanfPos extends JPanel implements ActionListener {
	/**
	 * RadioButton fürs Abweisen von BANFPositionen
	 */
	public JRadioButton del = new JRadioButton("löschen");

	/**
	 * RadioButton fürs Bestellen von BANFPositionen
	 */
	public JRadioButton bestellen = new JRadioButton("bestellen");

	/**
	 * Buttongroupo für abweisen/bestellen
	 */
	public ButtonGroup bg = new ButtonGroup();

	public String aktion = "nix";
	public AnzTabelleA abp;
	public DBConnection con;

	public AnzBanfPos(DBConnection con, Object[] banf) {

		this.con = con;
		String[] spaltenT = { "Banfpos-nr", "Bezeichnung", "Menge",
				"preisExkl", "MWSt", "bearbeiten" };
		String[] spaltenDB = { "banfposnr", "m.bezeichnung", "Menge",
				"preisExkl", "MWSt" };
		Object[] banfid = new Object[banf.length];
		for (int x = 0; x < banf.length; x++) {
			banfid[x] = banf[x];
		}
		String id = "";

		for (int i = 0; i < banfid.length - 1; i++) {
			id += "bp.banf=" + banfid[i] + " OR ";
		}
		id += "bp.banf=" + banfid[banfid.length - 1];

		JScrollPane sc = null;
		try {
			String sql = "SELECT * FROM banfpos bp, material m WHERE (" + id
					+ ") AND status = 1 AND bp.bezeichnung=m.id";
			ResultSet rs = con.mysql_query(sql);
			Class[] klass = { Integer.class, String.class, Integer.class,
					String.class, String.class, Boolean.class };
			sc = new JScrollPane(abp = new AnzTabelleA(spaltenDB, spaltenT,
					klass, rs, 0));
			LayoutRow[] detR = abp.getRows();
			for (int i = 0; i < detR.length; i++) {
				detR[i].setColor(Color.GREEN);
			}
		} catch (Exception e) {
			e.getMessage();
		}

		bg.add(del);
		bg.add(bestellen);

		setLayout(new BorderLayout());
		JPanel radbut = new JPanel();
		radbut.setLayout(new GridLayout(1, 2));
		radbut.add(del);
		radbut.add(bestellen);

		add(sc, BorderLayout.CENTER);
		add(radbut, BorderLayout.SOUTH);

		del.addActionListener(this);
		bestellen.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {

	}

}