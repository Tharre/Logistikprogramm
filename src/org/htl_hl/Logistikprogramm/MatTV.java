package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.sql.Timestamp;

import static sql.generated.logistik_test.Tables.*;

public class MatTV extends AbstractTV {

    private static final String[] columnNames =
            {"ID", "Bezeichnung", "Erstelldatum", "Nr", "Inventurgruppe", "Ersteller", "Gefahrstufe"};

    private final String bezeichnung;
    private final Timestamp erstelldatum;
    private final int nr;
    private final InvGrpTV inventurgruppe;
    private final String ersteller;
    private final int gefahrstufe;

    public MatTV(int id, String bezeichnung, Timestamp erstelldatum, int nr, int inventurgruppeId,
                 String inventurgruppeBezeichnung, String ersteller, int gefahrstufe) {
        super(id, columnNames, "ma01");
        this.bezeichnung = bezeichnung;
        this.erstelldatum = erstelldatum;
        this.nr = nr;
        this.inventurgruppe = new InvGrpTV(inventurgruppeId, inventurgruppeBezeichnung);
        this.ersteller = ersteller;
        this.gefahrstufe = gefahrstufe;
    }

    @Override
    public Object getValue(int column) {
        switch (column) {
            case 0: return id;
            case 1: return bezeichnung;
            case 2: return erstelldatum;
            case 3: return nr;
            case 4: return inventurgruppe;
            case 5: return ersteller;
            case 6: return gefahrstufe;
            default: throw new IllegalStateException();
        }
    }

    public static TableFormat<MatTV> getTableFormat() {
        return new ViewTableFormat<>(columnNames);
    }

    public static TextFilterator<MatTV> getTextFilterator() {
        return new ViewTextFilterator<>();
    }

    public static Query getQuery(DSLContext ctx) {
        return ctx.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR, INVENTURGRUPPE.ID,
                INVENTURGRUPPE.BEZEICHNUNG, USER.CN, MATERIAL.GEFAHRSTUFE)
                .from(MATERIAL)
                .leftOuterJoin(BUNDESNR)
                .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
                .leftOuterJoin(INVENTURGRUPPE)
                .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
                .leftOuterJoin(USER)
                .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
                .getQuery();
    }
}
