package org.htl_hl.Logistikprogramm;

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

	private JTree tree;
	private TabManager tabManager;

	// Menüleiste
	private JMenuBar menubar;

	// Menüleiste Elemente
	private JMenu datei;
	private JMenu hilfe;

	// Datei
	private JMenuItem oeffnen;
	private JMenuItem schliessen;

	// Hilfe
	private JMenuItem faq;
	private JMenuItem about;

	public GUI() {
		super("Login");
		Dimension dim = new Dimension(1024, 748);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());

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

		add(menubar, BorderLayout.NORTH);

		// Auswahlbaum links
		JPanel auswahlbaum = new JPanel();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode();

		tabManager = new TabManager();
		try {
			LConnection server = new LConnection();
			TabFactory tabFactory = new TabFactory(server, tabManager);

			// Firmenverwaltung
			DefaultMutableTreeNode firmenverwaltung = new DefaultMutableTreeNode("Firmenverwaltung");
			firmenverwaltung.add(new DefaultMutableTreeNode(tabFactory.getTab(FIRMA)));
			root.add(firmenverwaltung);

			// Materialverwaltung
			DefaultMutableTreeNode materialverwaltung = new DefaultMutableTreeNode("Materialverwaltung");
			materialverwaltung.add(new DefaultMutableTreeNode(tabFactory.getTab(MATERIAL)));
			root.add(materialverwaltung);

			// Inventurgruppenverwaltung
			DefaultMutableTreeNode invGrp = new DefaultMutableTreeNode("Inventurgruppenverwaltung");
			invGrp.add(new DefaultMutableTreeNode(tabFactory.getTab(INVENTURGRUPPE)));
			root.add(invGrp);

			// Bundesnrverwaltung
			DefaultMutableTreeNode bundesnrverwaltung = new DefaultMutableTreeNode("Bundesnrverwaltung");
			bundesnrverwaltung.add(new DefaultMutableTreeNode(tabFactory.getTab(BUNDESNR)));
			root.add(bundesnrverwaltung);

			// BANF
			DefaultMutableTreeNode banfverwaltung = new DefaultMutableTreeNode("BANF");
			banfverwaltung.add(new DefaultMutableTreeNode(tabFactory.getTab(BANF)));
			//banfverwaltung.add(new DefaultMutableTreeNode(tabFactory.getTab(FULLBANF)));
			root.add(banfverwaltung);

			// Bestellung
			DefaultMutableTreeNode bestellung = new DefaultMutableTreeNode("Bestellung");
			bestellung.add(new DefaultMutableTreeNode(tabFactory.getTab(BESTELLUNG)));
			root.add(bestellung);

			// Abfragen
			DefaultMutableTreeNode abfragen = new DefaultMutableTreeNode("Abfragen");
			abfragen.add(new DefaultMutableTreeNode(tabFactory.getTab(AbfragenGUI.class)));
			root.add(abfragen);

			// Lagerverwaltung
			DefaultMutableTreeNode lagerverwaltung = new DefaultMutableTreeNode("Lagerverwaltung");
			lagerverwaltung.add(new DefaultMutableTreeNode("Platzhalter"));
			root.add(lagerverwaltung);
		} catch (Exception e) {
			e.printStackTrace();
		}

		tree = new JTree(root);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);

		auswahlbaum.setLayout(new GridLayout());
		auswahlbaum.add(new JScrollPane(tree));

		Dimension dim2 = new Dimension(280, 1080);
		auswahlbaum.setPreferredSize(dim2);
		auswahlbaum.setMaximumSize(dim2);

		add(auswahlbaum, BorderLayout.WEST);
		add(tabManager.getTabbedPane(), BorderLayout.CENTER);

		tree.addMouseListener(this);
		setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() != 2)
			return;

		TreePath currentPath = tree.getPathForLocation(e.getX(), e.getY());
		if (currentPath == null)
			return;

		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
		if (!treeNode.isLeaf())
			return;

		if (treeNode.getUserObject() instanceof Tab)
			tabManager.addTab((Tab) treeNode.getUserObject());
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

	public static void main(String[] args) {
		new GUI();
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