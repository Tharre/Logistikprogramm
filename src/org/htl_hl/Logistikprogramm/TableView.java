package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import org.jooq.Query;

import javax.swing.*;
import java.util.List;

public class TableView<E> implements View {

    private LConnection server;
    private Query query;
    private Class<E> typeClass;
    private TextFilterator<E> textFilterator;
    private AdvancedTableFormat<E> tableFormat;
    private TabManager tm;

    public TableView(LConnection server, Query query, Class<E> typeClass, TextFilterator<E> textFilterator,
                     AdvancedTableFormat<E> tableFormat, TabManager tm) {
        this.server = server;
        this.query = query;
        this.typeClass = typeClass;
        this.textFilterator = textFilterator;
        this.tableFormat = tableFormat;
        this.tm = tm;
    }

    @Override
    public JPanel createJPanel(String[] args) {
        List<E> result = server.create.fetch(query.getSQL()).into(typeClass);

        int scrollToRow = args != null && args.length > 0 ? Integer.parseInt(args[0]) : 0;
        return new MultiEdit(result, textFilterator, tableFormat, tm, scrollToRow);
    }
}
