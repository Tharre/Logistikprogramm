package org.htl_hl.Logistikprogramm.TableViews;

import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import static sql.generated.logistik_test.Tables.BESTBANFSTATUS;


public class BestbanfstatusTV extends AbstractTV {

	private static final String[] columnNames = {"ID", "Bezeichnung"};
	private static final String[] propertyNames = {"id", "bezeichnung"};

	private String bezeichnung;

	public BestbanfstatusTV(int id) {
		super(id, "bbs01");
	}

	public BestbanfstatusTV(int id, String bezeichnung) {
		super(id, "bbs01");
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
		return DSL.select(BESTBANFSTATUS.ID, BESTBANFSTATUS.BEZEICHNUNG).from(BESTBANFSTATUS).getQuery();
	}
}
