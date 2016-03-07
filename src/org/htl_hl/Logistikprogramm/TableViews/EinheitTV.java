package org.htl_hl.Logistikprogramm.TableViews;

import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import static sql.generated.logistik_test.Tables.EINHEIT;


public class EinheitTV extends AbstractTV {

	private static final String[] columnNames = {"ID", "Bezeichnung"};
	private static final String[] propertyNames = {"id", "bezeichnung"};

	private String bezeichnung;

	public EinheitTV(int id) {
		super(id, "ein01");
	}

	public EinheitTV(int id, String bezeichnung) {
		super(id, "ein01");
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
		return DSL.select(EINHEIT.ID, EINHEIT.BEZEICHNUNG).from(EINHEIT).getQuery();
	}
}
