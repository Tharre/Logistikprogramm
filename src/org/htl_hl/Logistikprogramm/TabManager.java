package org.htl_hl.Logistikprogramm;

import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.Map;


public class TabManager {

    private Map<String, SubProgram> knownApplications = new HashMap<>();
    private TabHelper tabHelper;

    public TabManager(LConnection server, TabHelper tabHelper) {
        this.tabHelper = tabHelper;
        DSLContext ctx = server.create;

        // built-in subprograms
        // Material
        knownApplications.put("ma01", new SubProgram("Material anzeigen", "ma01",
                new TableView<>(server, MatTV.getQuery(ctx), MatTV.class, MatTV.getTextFilterator(),
                        MatTV.getTableFormat(), this, new int[] {4})));

        // Inventurgruppe
        knownApplications.put("inv01", new SubProgram("Inventurgruppe anzeigen", "inv01",
                new TableView<>(server, InvGrpTV.getQuery(ctx), InvGrpTV.class, InvGrpTV.getTextFilterator(),
                        InvGrpTV.getTableFormat(), this, new int[] {})));

        // external subprograms
        // load all sorts of external subprograms here as well if you like ...
    }

    public void addTab(String shortcut, String[] args) {
        SubProgram s = knownApplications.get(shortcut);
        tabHelper.add(s.getIdentifier(), s.createJPanel(args));
    }
}
