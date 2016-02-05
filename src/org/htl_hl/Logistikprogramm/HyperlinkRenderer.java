package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;


public class HyperlinkRenderer extends DefaultTableCellRenderer implements MouseListener, MouseMotionListener {

	private int[] hyperlinkColumns;
	private TabManager tm;
	private int row = -1;
	private int col = -1;
	private boolean isRollover;

	public HyperlinkRenderer(int[] hyperlinkColumns, TabManager tm) {
		this.hyperlinkColumns = hyperlinkColumns;
		this.tm = tm;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	                                               int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
		String str = Objects.toString(value, "");

		if (!table.isEditing() && this.row == row && this.col == column && this.isRollover) {
			setText("<html><u><font color='blue'>" + str);
		} else if (hasFocus) {
			setText("<html><font color='blue'>" + str);
		} else {
			setText(str);
		}

		return this;
	}

	private boolean isHyperlinkColumn(JTable table, int column) {
		for (int u : hyperlinkColumns) {
			if (u == column)
				return true;
		}

		return false;
		//return column >= 0 && table.getColumnClass(column).equals(String.class);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		JTable table = (JTable) e.getComponent();
		Point pt = e.getPoint();
		int prevRow = row;
		int prevCol = col;
		boolean prevRollover = isRollover;
		row = table.rowAtPoint(pt);
		col = table.columnAtPoint(pt);
		isRollover = isHyperlinkColumn(table, col);

		if (row == prevRow && col == prevCol && isRollover == prevRollover || !isRollover && !prevRollover)
			return;

		Rectangle repaintRect;
		if (isRollover) {
			table.setCursor(new Cursor(Cursor.HAND_CURSOR));
			Rectangle r = table.getCellRect(row, col, false);
			repaintRect = prevRollover ? r.union(table.getCellRect(prevRow, prevCol, false)) : r;
		} else {
			table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			repaintRect = table.getCellRect(prevRow, prevCol, false);
		}
		table.repaint(repaintRect);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JTable table = (JTable) e.getComponent();
		if (isHyperlinkColumn(table, col)) {
			table.repaint(table.getCellRect(row, col, false));
			row = -1;
			col = -1;
			isRollover = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JTable table = (JTable) e.getComponent();
		Point pt = e.getPoint();
		int col = table.columnAtPoint(pt);
		if (isHyperlinkColumn(table, col)) {
			int row = table.rowAtPoint(pt);
			System.out.println(table.getValueAt(row, col));
			tm.addTab("inv01", new String[] {"89"});
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }
}
