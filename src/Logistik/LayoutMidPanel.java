package Logistik;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.ResultSet;
import java.util.*;

/**
 * Panel welches die wichtigsten Methoden und das Layout der Übermenus zur
 * Verfügung stellt
 */

public class LayoutMidPanel extends JPanel implements ActionListener {
	private LayoutNaviPanel naviPanel;
	private JPanel mid;
	private CardLayout card;
	protected DBConnection con;
	private ArrayList panels;
	private ArrayList names;

	JToggleButton btnSuche = new JToggleButton("suche");
	JButton btnDetails = new JButton("Details anzeigen");
	AnzTabelleA det;

	public LayoutMidPanel(UserImport user) {
		con = user.getConnection();
		panels = new ArrayList();
		names = new ArrayList();
		setLayout(new BorderLayout());
		card = new CardLayout();
		mid = new JPanel(card);
		naviPanel = new LayoutNaviPanel();
		mid.add(new LayoutMainPanel(user), "blank");
		add(naviPanel, BorderLayout.NORTH);
		add(mid, BorderLayout.CENTER);

		btnDetails.addActionListener(this);
	}

	public void addNaviButton(String name) {

		JToggleButton btn = LayoutButtonCreator.createTextButton(name);
		naviPanel.addButton(btn);
		btn.addActionListener(this);
	}

	public void addNaviButton2(String name) {

		btnSuche = LayoutButtonCreator.createTextButton(name);
		naviPanel.addButton(btnSuche);
		btnSuche.addActionListener(this);
	}

	public void addShowPanel(LayoutMainPanel panel, String key) {

		mid.add(panel, key);
		names.add(key);
		panels.add(panel);
	}

	public void addShowPanelScroll(LayoutMainPanel panel, String key) {
		JScrollPane s = new JScrollPane(panel);
		mid.add(s, key);
		names.add(key);
		panels.add(panel);
	}

	public void actionPerformed(ActionEvent e) {

		try {
			if (e.getSource() == btnSuche)

			{
				String[] a = { "wnummer", "firmenname" };
				String auswahl = JOptionPane
						.showInputDialog(
								null,
								"Den Begriff \"wnummer\" oder \"firmenname\" eingeben!",
								"spezielle Suche", JOptionPane.QUESTION_MESSAGE);
				String query = "";
				if (auswahl.equals("wnummer")) {

					String eingabe = JOptionPane.showInputDialog(null,
							"WNummer eingeben: \n zB: w1-08", "Wnummer");
					query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND b.wnummer=\""
							+ eingabe + "\" ORDER BY datum DESC";

				} else if (auswahl.equals("firmenname")) {
					String eingabe = JOptionPane.showInputDialog(null,
							"Firmenname eingeben: \n zB: Prema", "Firmenname");
					query = "SELECT firmenname,cn,datum,bestId,status,statusbez,wnummer FROM bestellung b,user u,firma f WHERE u.cn=b.antragsteller AND f.id=b.firma AND f.firmenname=\""
							+ eingabe + "\" ORDER BY datum DESC";
				} else {
					btnSuche.doClick();
				}

				ResultSet rs = con.mysql_query(query);
				String[] spaltenDB = { "bestid", "wnummer", "datum", "cn",
						"firmenname", "status", "statusbez" };
				String[] spaltenT = { "Bestell-ID", "W-Nummer", "Datum",
						"Antragsteller", "Firma", "Status Lieferung",
						"Status Bezahlung", "Details" };
				Class[] klass = { Integer.class, String.class,
						java.util.Date.class, String.class, String.class,
						Integer.class, Integer.class, Boolean.class };

				JPanel sucheMitte = new JPanel(new BorderLayout());

				det = new AnzTabelleA(spaltenDB, spaltenT, klass, rs, 0);
				JScrollPane sc = new JScrollPane(det);
				sucheMitte.add(sc, BorderLayout.CENTER);
				sucheMitte.add(btnDetails, BorderLayout.SOUTH);
				mid.add(sucheMitte, "suchen");
				showMenu("suchen");

			} else {
				if (e.getSource() == btnDetails) {

					Object[] hakDet = det.getKlicked("Bestell-ID", "Details");
					AnzBestDetailNeu abd = new AnzBestDetailNeu(con, hakDet);
				} else {

					JToggleButton btn = (JToggleButton) e.getSource();
					showMenu(btn.getToolTipText());
				}
			}
		} catch (NullPointerException e1) {
			e1.getMessage();
		}
	}

	public void showMenu(String s) {
		LayoutMainPanel mp = (LayoutMainPanel) panels.get(getIndex(s));
		mp.activate();
		card.show(mid, s);
	}

	public int getIndex(String s) {
		for (int i = 0; i < names.size(); i++) {
			String s2 = (String) names.get(i);
			if (s2.equals(s)) {
				return i;
			}
		}
		return -1;
	}
}