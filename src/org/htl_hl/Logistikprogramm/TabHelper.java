package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.*;


public class TabHelper {
	private int index;
	private Color colors[] =
			{new Color(240, 40, 40), new Color(240, 140, 10), new Color(195, 195, 5), new Color(20, 195, 35),
			 new Color(60, 60, 230), new Color(160, 25, 170)};
	private JTabbedPane tabbedPane;
	
	public TabHelper(JTabbedPane tabbedPane){
		this.tabbedPane = tabbedPane;
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setUI(new MyTabbedPaneUI());
	}

	public void add(String label, JPanel panel) {
		int count = tabbedPane.getTabCount() % colors.length;
		int tabCount = tabbedPane.getTabCount() - 1;

		if (label.contains("Neuer Tab")) {
			index++;
			label = "Neuer Tab " + index;
		}

		for (int i = 0; i <= tabCount; i++) {
			if (tabbedPane.getComponentAt(i).getName().equals(label))
				return;
		}

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

		btnClose.addActionListener(new MyActionHandler(this, label));

		tabCount = tabbedPane.getTabCount() - 1;
		tabbedPane.setSelectedIndex(tabCount);
	}
	
	public JTabbedPane getTabbedPane(){
		return tabbedPane;
	}
}
