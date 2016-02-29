package org.htl_hl.Logistikprogramm;

import org.jooq.DSLContext;
import org.jooq.Query;


import static sql.generated.logistik_test.Tables.*;

public class StaatTV extends AbstractTV {

    private static final String[] columnNames = {"ID", "Bezeichnung"};
    private static final String[] propertyNames = {"id", "bezeichnung"};

    private String bezeichnung;

    public StaatTV(int id) {
        super(id, "sta01");
    }

    public StaatTV(int id, String bezeichnung) {
        super(id, "sta01");
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
    public Query getQuery(DSLContext ctx) {
        return ctx.select(STAAT.ID, STAAT.BEZEICHNUNG)
                .from(STAAT)
                .getQuery();
    }
}
