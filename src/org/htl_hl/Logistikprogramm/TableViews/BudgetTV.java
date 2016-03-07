package org.htl_hl.Logistikprogramm.TableViews;

import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import static sql.generated.logistik_test.Tables.BUDGET;


public class BudgetTV extends AbstractTV {

	private static final String[] columnNames = {"ID", "Bezeichnung"};
	private static final String[] propertyNames = {"id", "bezeichnung"};

	private String bezeichnung;

	public BudgetTV(int id) {
		super(id, "bug01");
	}

	public BudgetTV(int id, String bezeichnung) {
		super(id, "bug01");
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
		return DSL.select(BUDGET.ID, BUDGET.BEZEICHNUNG).from(BUDGET).getQuery();
	}
}
