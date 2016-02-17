package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.*;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.ThreadedMatcherEditor;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class MultiEdit extends JPanel {

	public <E> MultiEdit(List<E> list, TextFilterator<E> textFilterator, TableFormat<E> tf,
										TabManager tm, int[] hyperlinkColumns, int scrollToRow) {
		EventList<E> eventList = GlazedLists.eventList(list);
		SortedList<E> sortedList = new SortedList<>(eventList);

		JTextField filterEdit = new JTextField();
		MatcherEditor<E> tmEditor = new TextComponentMatcherEditor<>(filterEdit, textFilterator);
		FilterList<E> textFilteredRecords = new FilterList<>(sortedList, new ThreadedMatcherEditor<>(tmEditor));

		AdvancedTableModel<E> tableModel = GlazedListsSwing.eventTableModelWithThreadProxyList(textFilteredRecords, tf);

		final JTable t = new JTable(tableModel);

		for (int col : hyperlinkColumns) {
			HyperlinkRenderer renderer = new HyperlinkRenderer(col, tm);
			t.getColumnModel().getColumn(col).setCellRenderer(renderer);

			t.addMouseListener(renderer);
			t.addMouseMotionListener(renderer);
		}

		// TODO(Tharre): improve, improve, improve
		long startTime = System.nanoTime();
		t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(t);
		tca.adjustColumns();
		long endTime = System.nanoTime();
		System.out.println("Resize loading time: " + (endTime - startTime) / 1000000000.0 + "s");

		// TODO(Tharre): multiple columns?
		TableComparatorChooser.install(t, sortedList, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE_WITH_UNDO);

		setLayout(new GridBagLayout());
		add(new JLabel("Filter: "),
		    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                           new Insets(5, 5, 5, 5), 0, 0));
		add(filterEdit, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                                       new Insets(5, 5, 5, 5), 0, 0));
		add(new JScrollPane(t),
		    new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                           new Insets(5, 5, 5, 5), 0, 0));

		// scroll to the specified line
		for (int i = 0; i < tableModel.getRowCount(); ++i) {
			if (tableModel.getValueAt(i, 0).equals(scrollToRow)) {
				scrollToVisible(t, i, 0);
				t.setRowSelectionInterval(i, i);
				break;
			}
		}
	}

	public static void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
		if (!(table.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport)table.getParent();

		// This rectangle is relative to the table where the
		// northwest corner of cell (0,0) is always (0,0).
		Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

		// The location of the viewport relative to the table
		Point pt = viewport.getViewPosition();

		// Translate the cell location so that it is relative
		// to the view, assuming the northwest corner of the
		// view is (0,0)
		rect.setLocation(rect.x-pt.x, rect.y-pt.y);

		table.scrollRectToVisible(rect);

		// Scroll the area into view
		//viewport.scrollRectToVisible(rect);
	}
}
