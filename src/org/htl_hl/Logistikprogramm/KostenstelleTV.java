package org.htl_hl.Logistikprogramm;

import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import static sql.generated.logistik_test.Tables.KOSTENSTELLE;


public class KostenstelleTV extends AbstractTV {

	private static final String[] columnNames = {"ID", "Bezeichnung"};
	private static final String[] propertyNames = {"id", "bezeichnung"};

	private String bezeichnung;

	public KostenstelleTV(int id) {
		super(id, "kos01");
	}

	public KostenstelleTV(int id, String bezeichnung) {
		super(id, "kos01");
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
		return DSL.select(KOSTENSTELLE.ID, KOSTENSTELLE.BEZEICHNUNG).from(KOSTENSTELLE).getQuery();
	}
}
