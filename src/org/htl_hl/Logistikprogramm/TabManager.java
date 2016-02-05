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
        String[] columnNames;
        int[] hyperlinkColumns;
        TableView tv;

        // built-in subprograms
        // Material
        query = server.create
                .select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR,
                        INVENTURGRUPPE.BEZEICHNUNG, USER.CN, MATERIAL.GEFAHRSTUFE)
                .from(MATERIAL)
                .leftOuterJoin(BUNDESNR)
                .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
                .leftOuterJoin(INVENTURGRUPPE)
                .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
                .leftOuterJoin(USER)
                .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
                .getQuery();
        columnNames = new String[] {"ID", "Bezeichnung", "Erstelldatum", "Nr", "Inventurgruppe", "Ersteller", "Gefahrstufe"};
        hyperlinkColumns = new int[] {4};
        tv = new TableView(server, query, columnNames, hyperlinkColumns, this);
        knownApplications.put("ma01", new SubProgram("Material anzeigen", "ma01", tv));

        // Inventurgruppe
        query = server.create
                .select(INVENTURGRUPPE.ID, INVENTURGRUPPE.BEZEICHNUNG)
                .from(INVENTURGRUPPE)
                .getQuery();
        columnNames = new String[] {"ID", "Bezeichnung"};
        hyperlinkColumns = new int[] {};
        tv = new TableView(server, query, columnNames, hyperlinkColumns, this);
        knownApplications.put("inv01", new SubProgram("Inventurgruppe anzeigen", "inv01", tv));

        // external subprograms
        // load all sorts of external subprograms here as well if you like ...
    }

    public void addTab(String shortcut, String[] args) {
        SubProgram s = knownApplications.get(shortcut);
        tabHelper.add(s.getIdentifier(), s.createJPanel(args));
    }
}
