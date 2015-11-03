package Logistik;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

public class SelectDBEntry<E extends Record, T> extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = 5657891678987671604L;

	private final JComboBox<String> box;
	private final JButton button;
	private final EventList<T> list;
	private final LConnection server;
	private final Table<E> table;
	private final Class<E> typeParameterClass;

	public SelectDBEntry(final LConnection server, Table<E> table, TableField<E, T> wanted,
			Class<E> typeParameterClass) {
		this.table = table;
		this.typeParameterClass = typeParameterClass;
		this.server = server;
		list = GlazedLists.eventList(server.getTableField(this.table, wanted));

		setLayout(new BorderLayout());
		box = new JComboBox<String>();
		button = new JButton("Suchen"); // TODO(Tharre): button pic?
		button.addActionListener(this);
		add(box, BorderLayout.WEST);
		add(button, BorderLayout.EAST);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AutoCompleteSupport<T> auto = AutoCompleteSupport.install(box, list);
				auto.setFilterMode(TextMatcherEditor.CONTAINS);
				auto.setStrict(true); // don't allow wrong strings
			}
		});
	}

	public String getValue() {
		return box.getSelectedItem().toString();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			// TODO(Tharre): what should happen if we switch to another window?
			String[] propertyNames = { "id", "bezeichnung", "erstellungsdatum", "bundesnr", "inventurgruppe", "stueck",
					"meldebestand", "lagerort", "erfasser", "fixkosten", "gefahr" };
			SelectDBEntryDialog k = new SelectDBEntryDialog(server, table, typeParameterClass, propertyNames);

			k.addObserver(this);
			System.out.println("He pressed the button");
		}
	}

	@Override
	public void update(Observable o, Object obj) {
		System.out.println("Observed: " + obj);
		box.setSelectedItem(obj);
	}
}
