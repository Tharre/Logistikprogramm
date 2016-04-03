package org.htl_hl.Logistikprogramm;

import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Table;
import org.jooq.TableRecord;

import javax.swing.*;
import java.awt.*;


public class JOOQTableView<E extends TableRecord> implements Tab {

	private LConnection server;
	private TabManager tm;
	private String name;
	private String[] cn;
	private ResultQuery query;
	private Table<E> table;

	public JOOQTableView(LConnection server, TabManager tm, String name, String[] cn, ResultQuery query,
	                     Table<E> table) {
		this.server = server;
		this.tm = tm;
		this.name = name;
		this.cn = cn;
		this.query = query;
		this.table = table;
	}

	public String toString() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public JPanel getContent() {
		Result<?> recordResult = server.create.fetch(query);

		JPanel c = new JPanel();
		JOOQTableModel<?> tableModel = new JOOQTableModel<>(server, cn, recordResult, table);

		JTable t = new JTable(tableModel);
		t.setCellSelectionEnabled(true);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TabFactory tabFactory = new TabFactory(server, tm);
		HyperlinkRenderer renderer = new HyperlinkRenderer(tm, tabFactory);
		t.setDefaultRenderer(Reference.class, renderer);
		t.addMouseListener(renderer);
		t.addMouseMotionListener(renderer);

		// TODO(Tharre): improve, improve, improve
		long startTime = System.nanoTime();
		t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(t);
		tca.adjustColumns();
		long endTime = System.nanoTime();
		System.out.println("Resize loading time: " + (endTime - startTime) / 1000000000.0 + "s");

		JTextField filterEdit = new JTextField();
		c.setLayout(new GridBagLayout());
		c.add(new JLabel("Filter: "),
		      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                             new Insets(5, 5, 5, 5), 0, 0));
		c.add(filterEdit,
		      new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                             new Insets(5, 5, 5, 5), 0, 0));
		c.add(new JScrollPane(t),
		      new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                             new Insets(5, 5, 5, 5), 0, 0));

		return c;
	}
}
