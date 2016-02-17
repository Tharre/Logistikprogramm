package org.htl_hl.Logistikprogramm;

import org.jooq.Query;

import java.util.HashMap;
import java.util.Map;

import static sql.generated.logistik_test.Tables.*;


public class TabManager {

    private Map<String, SubProgram> knownApplications = new HashMap<>();
    private TabHelper tabHelper;

    public TabManager(LConnection server, TabHelper tabHelper) {
        this.tabHelper = tabHelper;
        Query query;
        int[] hyperlinkColumns;

        // built-in subprograms
        // Material
        query = server.create
                .select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR, INVENTURGRUPPE.ID,
                        INVENTURGRUPPE.BEZEICHNUNG, USER.CN, MATERIAL.GEFAHRSTUFE)
                .from(MATERIAL)
                .leftOuterJoin(BUNDESNR)
                .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
                .leftOuterJoin(INVENTURGRUPPE)
                .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
                .leftOuterJoin(USER)
                .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
                .getQuery();
        hyperlinkColumns = new int[] {4};
        knownApplications.put("ma01", new SubProgram("Material anzeigen", "ma01",
                new TableView<>(server, query, MatTV.class, MatTV.getTextFilterator(), MatTV.getTableFormat(), this,
                        hyperlinkColumns)));

        // Inventurgruppe
        query = server.create
                .select(INVENTURGRUPPE.ID, INVENTURGRUPPE.BEZEICHNUNG)
                .from(INVENTURGRUPPE)
                .getQuery();
        hyperlinkColumns = new int[] {};
        knownApplications.put("inv01", new SubProgram("Inventurgruppe anzeigen", "inv01",
                new TableView<>(server, query, InvGrpTV.class, InvGrpTV.getTextFilterator(), InvGrpTV.getTableFormat(),
                        this, hyperlinkColumns)));

        // external subprograms
        // load all sorts of external subprograms here as well if you like ...
    }

    public void addTab(String shortcut, String[] args) {
        SubProgram s = knownApplications.get(shortcut);
        tabHelper.add(s.getIdentifier(), s.createJPanel(args));
    }
}
