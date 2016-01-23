package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MyActionHandler implements ActionListener {

	private String tabName;
	JTabbedPane tabbedPane;

	public MyActionHandler(JTabbedPane tabbedPane, String tabName) {
		this.tabName = tabName;
		this.tabbedPane = tabbedPane;
	}

	public String getTabName() {
		return tabName;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Neuen Tab öffnen") {
			TabHelper.add(tabbedPane, "Neuer Tab", new JPanel());
		} else {
			int index = tabbedPane.indexOfTab(getTabName());
			if (index >= 0) {
				tabbedPane.removeTabAt(index);
			}
		}
	}
}   