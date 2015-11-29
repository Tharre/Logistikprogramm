package Logistik;

import static sql.generated.logistik_2.Tables.MATERIAL;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jooq.Record;
import org.jooq.Table;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.ThreadedMatcherEditor;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

public class SelectDBEntryDialog extends Observable {

	// TODO(Tharre): can we do better than passing propertyNames?
	public <E extends Record> SelectDBEntryDialog(LConnection server, Table<E> table,
			String[] propertyNames) {
		@SuppressWarnings("unchecked")
		Class<E> typeParameterClass = (Class<E>) table.getRecordType();
		EventList<E> list = GlazedLists.eventList(server.getTableData(table));
		SortedList<E> sortedList = new SortedList<>(list);

		String[] columnLabels = propertyNames;

		JTextField filterEdit = new JTextField();
		MatcherEditor<E> textMatcherEditor = new TextComponentMatcherEditor<>(filterEdit,
				GlazedLists.textFilterator(typeParameterClass, propertyNames));
		FilterList<E> textFilteredRecords = new FilterList<>(sortedList,
				new ThreadedMatcherEditor<>(textMatcherEditor));

		TableFormat<E> tf = GlazedLists.tableFormat(typeParameterClass, propertyNames, columnLabels);
		AdvancedTableModel<E> tm = GlazedListsSwing.eventTableModelWithThreadProxyList(textFilteredRecords, tf);

		final JFrame f = new JFrame();
		final JTable t = new JTable(tm);
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = t.rowAtPoint(e.getPoint());
					setChanged();
					notifyObservers(t.getModel().getValueAt(row, 1));
					f.setVisible(false);
					f.dispose();
				}
			}
		});

		// TODO(Tharre): improve, improve, improve
		long startTime = System.nanoTime();
		t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(t);
		tca.adjustColumns();
		long endTime = System.nanoTime();
		System.out.println("test loading time: " + (endTime - startTime) / 1000000000.0 + "s");

		// TODO(Tharre): multiple columns?
		TableComparatorChooser.install(t, sortedList, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE_WITH_UNDO);

		f.setLayout(new GridBagLayout());
		f.add(new JLabel("Filter: "), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		f.add(filterEdit, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		f.add(new JScrollPane(t), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		f.pack();
		// TODO(Tharre): we should probably position ourselves relative to the
		// root JFrame
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	// TODO(Tharre): for testing purposes
	public static void main(String[] args) throws Exception {
		LConnection server = new LConnection();

		new SelectDBEntryDialog(server, MATERIAL, new String[] { "id", "bezeichnung", "erstellungsdatum", "bundesnr",
				"inventurgruppe", "stueck", "meldebestand", "lagerort", "erfasser", "fixkosten", "gefahr" });
	}
}
