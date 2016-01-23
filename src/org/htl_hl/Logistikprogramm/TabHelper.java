package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.*;


public class TabHelper {
	private static int index;
	private static Color colors[] =
			{new Color(240, 40, 40), new Color(240, 140, 10), new Color(185, 195, 15), new Color(20, 210, 50),
			 new Color(60, 60, 230), new Color(160, 25, 170)};

	static void add(JTabbedPane tabbedPane, String label, JPanel panel) {
		int count = tabbedPane.getTabCount() % colors.length;
		int tabCount = tabbedPane.getTabCount() - 1;
		JButton btnTab = new JButton("Neuen Tab öffnen");

		if (label.contains("Neuer Tab")) {
			index++;
			label = "Neuer Tab " + index;
		}

		MyActionHandler actionhandler = new MyActionHandler(tabbedPane, label);
		btnTab.addActionListener(actionhandler);

		for (int i = 0; i <= tabCount; i++) {
			if (tabbedPane.getComponentAt(i).getName().equals(label))
				return;
		}

		panel.add(btnTab);
		tabbedPane.addTab(label, panel);

		tabCount = tabbedPane.getTabCount() - 1;
		tabbedPane.getComponentAt(tabCount).setName(label);

		JPanel pnlTab = new JPanel(new BorderLayout());
		pnlTab.setOpaque(false);
		JLabel lbllabel = new JLabel(label);
		lbllabel.setForeground(colors[count]);
		MyButton btnClose = new MyButton("x");

		pnlTab.add(lbllabel, BorderLayout.WEST);

		btnClose.setBorder(null);
		btnClose.setFont(new Font("Arial", Font.BOLD, 14));
		btnClose.setPreferredSize(new Dimension(34, 16));
		Container c = new Container();
		c.setLayout(new GridLayout(1, 1));
		c.add(btnClose);
		pnlTab.add(c, BorderLayout.CENTER);

		tabbedPane.setTabComponentAt(tabCount, pnlTab);

		btnClose.addActionListener(actionhandler);

		tabCount = tabbedPane.getTabCount() - 1;
		tabbedPane.setSelectedIndex(tabCount);
	}
}
