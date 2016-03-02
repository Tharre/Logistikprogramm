package org.htl_hl.Logistikprogramm;

import org.jooq.DSLContext;
import org.jooq.Query;

import static sql.generated.logistik_test.Tables.*;


public class BundesnrTV extends AbstractTV {

	private static final String[] columnNames = {"ID", "Bezeichnung", "Nr"};
	private static final String[] propertyNames = {"id", "bezeichnung", "nr"};

	private String bezeichnung;
	private String nr;

	public BundesnrTV(int id) {
		super(id, "bnr01");
	}

	public BundesnrTV(int id, String bezeichnung, String nr) {
		super(id, "bnr01");
		this.bezeichnung = bezeichnung;
		this.nr = nr;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public String getNr() {
		return nr;
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
	public Query getQuery(DSLContext ctx) {
		return ctx.select(BUNDESNR.ID, BUNDESNR.BEZEICHNUNG, BUNDESNR.NR)
		          .from(BUNDESNR)
		          .getQuery();
	}
}
