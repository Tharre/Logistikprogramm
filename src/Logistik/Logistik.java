package Logistik;

import javax.swing.*;

import Budget.Anmeldung;
import Budget.Loading;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.*;

/**
 * Hauptklasse des Logistikprogramms Verwaltet den allegmeinen Aufbau des
 * Programms und Initialisiert die Buttons/Menus
 */

public class Logistik extends JFrame {

	private JPanel p_Head;
	private LayoutLinkPanel linkPanel;
	private LayoutNamePanel namePanel;
	private JPanel midPanel;
	private LayoutBottomPanel pBottom;
	private CardLayout center;
	public static final AnzRechteB rechte = new AnzRechteB();
	public DBConnection con;
	public static UserImport user;
	private AnimationPanel pAnim;
	private Anmeldung f;
	private boolean istBudget = false;

	public Logistik(String title, UserImport user) {
		super(title);
		Logistik.user = user;
		setSize(500, 500);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		con = user.getConnection();
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		Container c = getContentPane();

		pAnim = new AnimationPanel();

		pAnim.setLayout(new BorderLayout());
		p_Head = new JPanel(new BorderLayout());

		linkPanel = new LayoutLinkPanel(this, "../links.gif", 40);
		linkPanel.addButton("../user.gif", "Userverwaltung");
		linkPanel.addButton("../firma.gif", "Firmenverwaltung");
		linkPanel.addButton("../material.gif", "Materialverwaltung");
		linkPanel.addButton("../inventur.gif", "InvGruppeverwaltung");
		linkPanel.addButton("../bundesgr.gif", "BundesGruppeverwaltung");
		linkPanel.addButton("../banf.gif", "BANF");
		linkPanel.addButton("../bestellung.gif", "Bestellung");
		linkPanel.addButton("../abfragen.gif", "Abfragen");
		linkPanel.addButton("../lagerverw.gif", "Lagerverwaltung");
		linkPanel.addButton("../datenb.gif", "Datenbank");
		linkPanel.addButton("../budget.gif", "Budget");
		namePanel = new LayoutNamePanel("../namepanel.gif", 30);

		p_Head.add(linkPanel, BorderLayout.NORTH);
		p_Head.add(namePanel, BorderLayout.CENTER);

		center = new CardLayout();
		midPanel = new JPanel(center);
		addCenter(new Welcome(user), "welcome");
		addCenter(new VerUser(user), "Userverwaltung");
		addCenter(new VerMaterial(user), "Materialverwaltung");
		addCenter(new VerInventurgruppe(user), "InvGruppeverwaltung");
		addCenter(new VerBundesgruppe(user), "BundesGruppeverwaltung");
		addCenter(new VerFirmen(user), "Firmenverwaltung");
		addCenter(new VerBanf(user), "BANF");
		addCenter(new VerBestellung(user), "Bestellung");
		addCenter(new Abfragen(user), "Abfragen");
		addCenter(new VerLager(user), "Lagerverwaltung");
		addCenter(new VerDatenbank(user), "Datenbank");
		addCenter(new StartBudget(user), "Budget");

		pBottom = new LayoutBottomPanel("../navipanel.gif", 30, user, this);
		pAnim.add(p_Head, BorderLayout.NORTH);
		pAnim.add(midPanel, BorderLayout.CENTER);
		pAnim.add(pBottom, BorderLayout.SOUTH);
		c.add(pAnim);
		setMenu("Userverwaltung");
		setVisible(true);
		setMenu("welcome");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				con.close();
				dispose();
			}
		});
	}

	public void addCenter(Component c, String key) {
		midPanel.add(c, key);
	}

	public void setMenu(String menu) {
		if (menu.equals("Budget")) {
			if (user.hasRecht(Logistik.rechte.getRechtId("Budget"))) {
				f = new Anmeldung();
				f.pack();
				f.setLocationRelativeTo(null);
				URL u = this.getClass().getResource("../1cent.jpg");
				Image image = new ImageIcon(u).getImage();
				f.setIconImage(image);
				f.setVisible(true);

				istBudget = true;
			}

		} else {
			namePanel.setText(menu);
			center.show(midPanel, menu);
		}

	}

	public static void openPDF(String file) throws Exception {
		Desktop.getDesktop().open(new File(file));
	}

	public static void printPDF(String file) throws Exception {
		Desktop.getDesktop().open(new File(file));
	}

	public static void main(String[] args) {
		new Logistik("Logistik", new UserImport(".STAREK.VISITORS.HTBL",
				new DBConnection("logistik_2", "logistik1", "4ahwii"), null));
	}

	public String getBundesnr(String id) {
		ResultSet rs = null;
		String nr = "";
		try {
			String qry = "SELECT id,nr, uebergruppe FROM bundesnr WHERE id="
					+ id + ";";
			rs = con.mysql_query(qry);
			while (rs.next()) {
				qry = "SELECT id,nr, uebergruppe FROM bundesnr WHERE id=" + id
						+ ";";
				rs = con.mysql_query(qry);
				id = rs.getString("uebergruppe");
				nr = id + nr;
			}

			rs.close();

		} catch (Exception e) {
			return null;
		}
		return nr;
	}

	public void schliesse() {

		if (istBudget) {
			f.schliesse();
		}
	}

	class AnimationPanel extends JPanel implements Runnable {
		private Thread runner;
		private long time;

		public AnimationPanel() {
			/*
			 * runner = new Thread(this); runner.start(); heads = new Image[3];
			 * URL u = this.getClass().getResource("../jakob.gif"); heads[0] = new
			 * ImageIcon(u).getImage(); u =
			 * this.getClass().getResource("../starek.gif"); heads[1] = new
			 * ImageIcon(u).getImage(); u =
			 * this.getClass().getResource("../zach.gif"); heads[2] = new
			 * ImageIcon(u).getImage(); addMouseListener(new MouseAdapter() {
			 * public void mouseClicked(MouseEvent e) { if (e.getX() >
			 * getWidth() - 10 && e.getY() < 10) new Sudoku(); } });
			 */
		}

		public void run() {
			long sleepTime = 10000;
			try {
				Thread.sleep(30000);
			} catch (Exception e) {
			}
			time = System.currentTimeMillis();
			while (runner != null) {
				try {
					Thread.sleep(sleepTime);
				} catch (Exception e) {
				}
				if (sleepTime > 1000) {
					if ((int) (Math.random() * 10) == 1) {

						sleepTime = 50;
					}
				} else {

					repaint();

				}
			}
		}
	}
}
