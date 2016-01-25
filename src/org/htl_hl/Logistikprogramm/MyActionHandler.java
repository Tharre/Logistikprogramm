package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MyActionHandler implements ActionListener {

	private String tabName;
	TabHelper tabHelper;

	public MyActionHandler(TabHelper tabHelper, String tabName) {
		this.tabName = tabName;
		this.tabHelper = tabHelper;
	}

	public String getTabName() {
		return tabName;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Neuen Tab öffnen") {
			tabHelper.add("Neuer Tab", new JPanel());
		} else {
			int index = tabHelper.getTabbedPane().indexOfTab(getTabName());
			if (index >= 0) {
				tabHelper.getTabbedPane().removeTabAt(index);
			}
		}
	}
}   