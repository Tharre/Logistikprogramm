package org.htl_hl.Logistikprogramm;

import org.jooq.Result;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static sql.generated.logistik_test.Tables.*;


public class GUI extends JFrame implements MouseListener, ActionListener {

	RootPanel rootpanel;
	Container c = this;
	TreePath currentPath;
	TabHelper tabHelper = new TabHelper(new JTabbedPane());

	// Menüleiste
	JMenuBar menubar;

	// Menüleiste Elemente
	JMenu datei;
	JMenu hilfe;

	// Datei
	JMenuItem oeffnen;
	JMenuItem schliessen;

	// Hilfe
	JMenuItem faq;
	JMenuItem about;

	public GUI() {
		super("Login");
		Dimension dim = new Dimension(1024, 748);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		c.setLayout(new BorderLayout());

		// Menüleiste erzeugen
		menubar = new JMenuBar();

		// Menüelemente erzeugen
		datei = new JMenu("Datei");
		hilfe = new JMenu("Hilfe");

		// Untermenüelemente erzeugen
		oeffnen = new JMenuItem("öffnen");
		oeffnen.addActionListener(this);
		schliessen = new JMenuItem("schliessen");
		schliessen.addActionListener(this);
		faq = new JMenuItem("F.A.Q.");
		faq.addActionListener(this);
		about = new JMenuItem("Über");
		about.addActionListener(this);

		// Menüelemente hinzufügen
		menubar.add(datei);
		menubar.add(hilfe);

		// Untermenüelemente hinzufügen
		datei.add(oeffnen);
		datei.add(schliessen);
		hilfe.add(faq);
		hilfe.add(about);

		c.add(menubar, BorderLayout.NORTH);

		rootpanel = new RootPanel();
		c.add(rootpanel, BorderLayout.WEST);
		c.add(tabHelper.getTabbedPane(), BorderLayout.CENTER);

		rootpanel.tree.addMouseListener(this);
		setVisible(true);
	}

	private String toString(TreePath path) {
		String hilfsString = path.toString();
		String leaf = "";
		int laenge = 0;
		for (int i = hilfsString.length() - 1; i >= 0; i--) {
			if (hilfsString.charAt(i) != ',')
				laenge++;
			else
				break;
		}

		for (int i = hilfsString.length() - laenge + 1; i < hilfsString.length() - 1; i++) {
			leaf += hilfsString.charAt(i);
		}

		return leaf;
	}

	private JPanel toJPanel(String s) {
		if (s.equals("Materialien anzeigen")) {
			try {
				LConnection server = new LConnection();
				Result<?> result = server.create
						.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR,
								INVENTURGRUPPE.BEZEICHNUNG, USER.CN, MATERIAL.GEFAHRSTUFE)
						.from(MATERIAL)
						.leftOuterJoin(BUNDESNR)
						.on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
						.leftOuterJoin(INVENTURGRUPPE)
						.on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
						.leftOuterJoin(USER)
						.on(MATERIAL.ERFASSER_ID.equal(USER.ID))
						.fetch();

				String[] propertyNames =
						{"ID", "Bezeichnung", "Erstelldatum", "Nr", "Inventurgruppe", "Ersteller", "Gefahrstufe"};
				int[] hyperlinkColumns = {4};
				
				return new MultiEdit(result, new RecordTableFormat(propertyNames), hyperlinkColumns);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			JPanel p = new JPanel();
			p.add(new JLabel(s));
			return p;
		}
	}

	public static void main(String[] args) {
		new GUI();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() != 2)
			return;

		currentPath = rootpanel.tree.getPathForLocation(e.getX(), e.getY());
		if (currentPath == null)
			return;

		String s = toString(currentPath);
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
		if (treeNode.isLeaf()) {
			JPanel p = toJPanel(s);
			tabHelper.add(s, p);
		}
	}

	public void actionPerformed(ActionEvent object) {
		if (object.getSource() == oeffnen) {
			System.out.println("öffnen wurde angeklickt");
		} else if (object.getSource() == schliessen) {
			System.out.println("beenden wurde angeklickt");
		} else if (object.getSource() == faq) {
			System.out.println("faq wurde angeklickt");
		} else if (object.getSource() == about) {
			System.out.println("über wurde angeklickt");
		}
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) { }

	@Override
	public void mouseReleased(MouseEvent mouseEvent) { }

	@Override
	public void mouseEntered(MouseEvent mouseEvent) { }

	@Override
	public void mouseExited(MouseEvent mouseEvent) { }
}