package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import org.jooq.DSLContext;
import org.jooq.Query;

import static sql.generated.logistik_test.Tables.*;

public class StaatTV extends AbstractTV {

    private static final String[] columnNames = {"ID", "Bezeichnung"};

    private final String bezeichnung;

    public StaatTV(int id, String bezeichnung) {
        super(id, columnNames, "sta01");
        this.bezeichnung = bezeichnung;
    }

    @Override
    public Object getValue(int column) {
        switch (column) {
            case 0: return id;
            case 1: return bezeichnung;
            default: throw new IllegalStateException();
        }
    }

    public String toString() {
        return bezeichnung;
    }

    public static TableFormat<StaatTV> getTableFormat() {
        return new ViewTableFormat<>(columnNames);
    }

    public static TextFilterator<StaatTV> getTextFilterator() {
        return new ViewTextFilterator<>();
    }

    public static Query getQuery(DSLContext ctx) {
        return ctx.select(STAAT.ID, STAAT.BEZEICHNUNG)
                .from(STAAT)
                .getQuery();
    }
}
