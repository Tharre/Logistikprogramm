package org.htl_hl.Logistikprogramm;

import org.jooq.Query;
import org.jooq.Result;

import javax.swing.*;

public class TableView implements View {

    private LConnection server;
    private Query query;
    private String[] columnNames;
    private int[] hyperlinkColumns;
    private TabManager tm;

    public TableView(LConnection server, Query query, String[] columnNames, int[] hyperlinkColumns, TabManager tm) {
        this.server = server;
        this.query = query;
        this.columnNames = columnNames;
        this.hyperlinkColumns = hyperlinkColumns;
        this.tm = tm;
    }

    @Override
    public JPanel createJPanel(String[] args) {
        Result<?> result = server.create.fetch(query.getSQL());

        int scrollToRow = args != null && args.length > 0 ? Integer.parseInt(args[0]) : 0;
        return new MultiEdit(result, new RecordTableFormat(columnNames), hyperlinkColumns, tm, scrollToRow);
    }
}
