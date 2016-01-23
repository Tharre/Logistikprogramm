package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;


public class RootPanel extends JPanel {

	public JTree tree;

	public RootPanel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Verwaltung");

		DefaultMutableTreeNode userverwaltung = new DefaultMutableTreeNode("Userverwaltung");
		for (String i : new String[]{"Usergruppe anzeigen", "Usergruppe anlegen", "Usergruppe bearbeiten",
		                             "User einer Usergruppe zuweisen", "Usergruppe löschen"}) {
			userverwaltung.add(new DefaultMutableTreeNode(i));
		}
		root.add(userverwaltung);

		DefaultMutableTreeNode firmenverwaltung = new DefaultMutableTreeNode("Firmenverwaltung");
		for (String i : new String[]{"Firmen anzeigen", "Firma anlegen", "Firma bearbeiten", "Mehrfachänderung",
		                             "Firma löschen"}) {
			firmenverwaltung.add(new DefaultMutableTreeNode(i));
		}
		root.add(firmenverwaltung);

		DefaultMutableTreeNode materialverwaltung = new DefaultMutableTreeNode("Materialverwaltung");
		for (String i : new String[]{"Materialien anzeigen", "Materialien exportieren", "Material anlegen",
		                             "Material bearbeiten", "Mehrfachänderung", "Firma-Material bearbeiten",
		                             "Firma Material zuweisen", "Material löschen", "Firma Material löschen"}) {
			materialverwaltung.add(new DefaultMutableTreeNode(i));
		}
		root.add(materialverwaltung);

		DefaultMutableTreeNode invGruppenverwaltung = new DefaultMutableTreeNode("Inventurgruppenverwaltung");
		for (String i : new String[]{"Inventurgruppen anzeigen", "Inventurgruppe anlegen", "Inventurgruppe löschen"}) {
			invGruppenverwaltung.add(new DefaultMutableTreeNode(i));
		}
		root.add(invGruppenverwaltung);

		DefaultMutableTreeNode bundesGruppenverwaltung = new DefaultMutableTreeNode("Bundesgruppenverwaltung");
		for (String i : new String[]{"Bundesgruppen anzeigen", "Bundesgruppe anlegen", "Bundesgruppe löschen"}) {
			bundesGruppenverwaltung.add(new DefaultMutableTreeNode(i));
		}
		root.add(bundesGruppenverwaltung);

		DefaultMutableTreeNode banf = new DefaultMutableTreeNode("BANF");
		for (String i : new String[]{"Banfs anzeigen", "Banf anlegen", "fertig", "zu bestellen", "abgewiesen"}) {
			banf.add(new DefaultMutableTreeNode(i));
		}
		root.add(banf);

		DefaultMutableTreeNode bestellung = new DefaultMutableTreeNode("Bestellung");
		for (String i : new String[]{"Bestellungen anzeigen", "heuer", "Bestellung anlegen", "nicht abgeschickt",
		                             "abgeschickt", "ausständig", "komplett geliefert", "fehlerhaft", "fertig",
		                             "suchen"}) {
			bestellung.add(new DefaultMutableTreeNode(i));
		}
		root.add(bestellung);

		DefaultMutableTreeNode abfragen = new DefaultMutableTreeNode("Abfragen");
		for (String i : new String[]{"Usergruppe", "User", "Rechte", "Firma", "Material", "Material-Firma", "Lager",
		                             "Banf/Best."}) {
			abfragen.add(new DefaultMutableTreeNode(i));
		}
		root.add(abfragen);

		DefaultMutableTreeNode lagerverwaltung = new DefaultMutableTreeNode("Lagerverwaltung");
		for (String i : new String[]{"Lieferung", "Aushändigung1", "AushändigungN", "Zubuchen", "Zurück zu Firma",
		                             "Korrektur"}) {
			lagerverwaltung.add(new DefaultMutableTreeNode(i));
		}
		root.add(lagerverwaltung);

		DefaultMutableTreeNode datenbank = new DefaultMutableTreeNode("Datenbank");
		for (String i : new String[]{"DB leeren", "DB exportieren", "DB importieren"}) {
			datenbank.add(new DefaultMutableTreeNode(i));
		}
		root.add(datenbank);

		DefaultMutableTreeNode budgetprogramm = new DefaultMutableTreeNode("Budgetprogramm");
		root.add(budgetprogramm);

		tree = new JTree(root);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);

		setLayout(new GridLayout());
		JScrollPane pane = new JScrollPane(tree);
		add(pane);

		Dimension dim = new Dimension(280, 1080);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}
}