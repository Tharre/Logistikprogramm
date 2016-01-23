package org.htl_hl.Logistikprogramm;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;


public class MyTabbedPaneUI extends BasicTabbedPaneUI {

	private static final int ADDED_TAB_WIDTH = 10;
	private static final Color TAB_COLOR = new Color(230, 230, 250);
	private static final Color SELECTED_TAB_COLOR = new Color(200, 220, 255);

	protected void installDefaults() {
		super.installDefaults();

		tabInsets.right = tabInsets.right - ADDED_TAB_WIDTH;
	}

	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
	                                  boolean isSelected) {
		g.setColor((!isSelected) ? TAB_COLOR : SELECTED_TAB_COLOR);
		switch (tabPlacement) {
			case LEFT:
				g.fillRect(x + 1, y + 1, w - 1, h - 3);
				break;
			case RIGHT:
				g.fillRect(x, y + 1, w - 2, h - 3);
				break;
			case BOTTOM:
				g.fillRect(x + 1, y, w - 3, h - 1);
				break;
			case TOP:
			default:
				g.fillRect(x + 1, y + 1, w - 3, h - 1);
		}
	}
}