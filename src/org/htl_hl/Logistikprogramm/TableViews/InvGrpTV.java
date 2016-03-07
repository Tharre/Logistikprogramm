package org.htl_hl.Logistikprogramm.TableViews;

import org.htl_hl.Logistikprogramm.TableViews.AbstractTV;
import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import static sql.generated.logistik_test.Tables.INVENTURGRUPPE;


public class InvGrpTV extends AbstractTV {

	private static final String[] columnNames = {"ID", "Bezeichnung"};
	private static final String[] propertyNames = {"id", "bezeichnung"};

	private String bezeichnung;

	public InvGrpTV(int id) {
		super(id, "inv01");
	}

	public InvGrpTV(int id, String bezeichnung) {
		super(id, "inv01");
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
		return DSL.select(INVENTURGRUPPE.ID, INVENTURGRUPPE.BEZEICHNUNG).from(INVENTURGRUPPE).getQuery();
	}
}
