package org.htl_hl.Logistikprogramm.TableViews;

import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import static sql.generated.logistik_test.Tables.ANSCHRIFT;


public class AnschriftTV extends AbstractTV {

	private static final String[] columnNames = {"ID", "Bezeichnung"};
	private static final String[] propertyNames = {"id", "bezeichnung"};

	private String bezeichnung;

	public AnschriftTV(int id) {
		super(id, "ans01");
	}

	public AnschriftTV(int id, String bezeichnung) {
		super(id, "ans01");
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	@Override
	public String[] getColumnNames() {
		return columnNames;
	}

	@Override
	public String[] getPropertyNames() {
		return propertyNames;
	}

	@Override
	public ResultQuery getQuery() {
		return DSL.select(ANSCHRIFT.ID, ANSCHRIFT.BEZEICHNUNG).from(ANSCHRIFT).getQuery();
	}
}
