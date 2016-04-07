package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.DSL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Observable;
import java.util.Observer;

import static sql.generated.logistik_test.Tables.BUNDESNR;


public class SelectDBEntry<E extends TableRecord, T> extends JPanel implements ActionListener, Observer {

	private class RecordFormat extends Format {
		@Override
		public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
			if (obj != null)
				toAppendTo.append(((Record) obj).getValue(1));

			return toAppendTo;
		}

		@Override
		public Object parseObject(String source, ParsePosition pos) {
			throw new UnsupportedOperationException("not implemented");
		}
	}

	private static final long serialVersionUID = 5657891678987671604L;

	private final JComboBox<E> box;
	private final JButton button;
	private final EventList<E> list;
	private final LConnection server;
	private final Table<E> table;

	public SelectDBEntry(LConnection server, TableField<E, T> wanted) {
		this.table = wanted.getTable();
		this.server = server;
		list = GlazedLists.eventList(server.getTableData(table));

		box = new JComboBox<>();

		button = new JButton(new ImageIcon("res/lookup_icon.png")); // TODO(Tharre): button pic?
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.addActionListener(this);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;

		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		add(box, c);

		c.anchor = GridBagConstraints.EAST;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 1;
		add(button, c);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AutoCompleteSupport<E> auto = AutoCompleteSupport.install(box, list, null, new RecordFormat());
				auto.setFilterMode(TextMatcherEditor.CONTAINS);
				auto.setStrict(true); // don't allow wrong strings
			}
		});
	}

	public String getValue() {
		return box.getSelectedItem().toString();
	}

	public int getId() {
		Record rec = (Record) box.getSelectedItem();
		return (int) rec.getValue(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			// TODO(Tharre): what should happen if we switch to another window?
			/*
			String[] propertyNames =
					{"id", "bezeichnung", "erstellungsdatum", "bundesnr", "inventurgruppe", "stueck", "meldebestand",
					 "lagerort", "erfasser", "fixkosten", "gefahr"};

			SelectDBEntryDialog k = new SelectDBEntryDialog(server, table, propertyNames);
			*/

			//TabFactory tabFactory = new TabFactory(server);


			JFrame f = new JFrame();
			//f.add(tabFactory.getTab(table).getContent());
			f.add(new TestTableView<>(server, "Name", new String[]{"ID", "Bezeichnung", "Nr."},
			                          DSL.selectFrom(BUNDESNR).getQuery(), table).getContent());
			f.pack();
			f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			f.setLocationRelativeTo(null);
			f.setVisible(true);

			//k.addObserver(this);

			/*
			populateHelper("Bundesnr anzeigen",
			               DSL.selectFrom(BUNDESNR).getQuery(), BUNDESNR);
			tabs.put(table, new JOOQTableView<>(server, tm, name, cn, query, table));
			*/

			System.out.println("He pressed the button");
		}
	}

	@Override
	public void update(Observable o, Object obj) {
		System.out.println("Observed: " + obj);
		box.setSelectedItem(obj);
	}
}
