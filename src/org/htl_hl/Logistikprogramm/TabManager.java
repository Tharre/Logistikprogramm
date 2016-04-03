package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TabManager {

	private static final Color colors[] =
			{new Color(240, 40, 40), new Color(240, 140, 10), new Color(195, 195, 5), new Color(20, 195, 35),
			 new Color(60, 60, 230), new Color(160, 25, 170)};
	private final JTabbedPane tabbedPane;

	public TabManager() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setUI(new MyTabbedPaneUI());
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void addTab(Tab tab) {
		add(tab.getName(), tab.getContent());
	}

	public void add(String label, JPanel panel) {
		int count = tabbedPane.getTabCount() % colors.length;
		int tabCount = tabbedPane.getTabCount() - 1;

		for (int i = 0; i <= tabCount; i++) {
			if (tabbedPane.getComponentAt(i).getName().equals(label))
				return;
		}

		tabbedPane.addTab(label, panel);

		tabCount = tabbedPane.getTabCount() - 1;
		tabbedPane.getComponentAt(tabCount).setName(label);

		final JPanel pnlTab = new JPanel(new BorderLayout());
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

		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				int i = tabbedPane.indexOfTabComponent(pnlTab);
				if (i != -1)
					tabbedPane.remove(i);
			}
		});

		tabCount = tabbedPane.getTabCount() - 1;
		tabbedPane.setSelectedIndex(tabCount);
	}
}
