package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import org.jooq.ResultQuery;

import javax.swing.*;
import java.util.List;


public class TableView<E> implements View {

	private LConnection server;
	private ResultQuery query;
	private Class<E> typeClass;
	private TextFilterator<E> textFilterator;
	private AdvancedTableFormat<E> tableFormat;
	private TabManager tm;

	public TableView(LConnection server, ResultQuery query, Class<E> typeClass, TextFilterator<E> textFilterator,
	                 AdvancedTableFormat<E> tableFormat, TabManager tm) {
		this.server = server;
		this.query = query;
		this.typeClass = typeClass;
		this.textFilterator = textFilterator;
		this.tableFormat = tableFormat;
		this.tm = tm;
	}

	@Override
	public JPanel createJPanel(String[] args) {
		// TODO: make typesafe
		List<E> result = server.create.fetch(query).into(typeClass);

		int scrollToRow = args != null && args.length > 0 ? Integer.parseInt(args[0]) : 0;
		return new MultiEdit(result, textFilterator, tableFormat, tm, scrollToRow);
	}
}
