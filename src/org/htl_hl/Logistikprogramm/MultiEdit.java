package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.*;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.ThreadedMatcherEditor;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import org.jooq.Record;
import org.jooq.Result;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static sql.generated.logistik_test.Tables.*;


public class MultiEdit extends JPanel {

    public <E extends Record> MultiEdit(Result<E> result, TableFormat tf, int[] hyperlinkColumns) {
        EventList<E> list = GlazedLists.eventList(result);
        SortedList<E> sortedList = new SortedList<>(list);

        JTextField filterEdit = new JTextField();
        MatcherEditor<E> tmEditor = new TextComponentMatcherEditor<>(filterEdit, new TextFilterator<E>() {
            @Override
            public void getFilterStrings(List<String> baseList, E element) {
                for (int i = 0; i < element.size(); ++i) {
                    if (element.getValue(i) != null) // literal database NULL
                        baseList.add(element.getValue(i).toString());
                }
            }
        });
        FilterList<E> textFilteredRecords = new FilterList<>(sortedList, new ThreadedMatcherEditor<>(tmEditor));

        @SuppressWarnings("unchecked") AdvancedTableModel<E> tm =
                GlazedListsSwing.eventTableModelWithThreadProxyList(textFilteredRecords, tf);

        final JTable t = new JTable(tm);
        HyperlinkRenderer renderer = new HyperlinkRenderer(hyperlinkColumns);
        t.getColumnModel().getColumn(4).setCellRenderer(renderer);
        //t.setDefaultRenderer(URL.class, renderer);
        t.addMouseListener(renderer);
        t.addMouseMotionListener(renderer);

        // TODO(Tharre): improve, improve, improve
        long startTime = System.nanoTime();
        t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnAdjuster tca = new TableColumnAdjuster(t);
        tca.adjustColumns();
        long endTime = System.nanoTime();
        System.out.println("Resize loading time: " + (endTime - startTime) / 1000000000.0 + "s");

        // TODO(Tharre): multiple columns?
        TableComparatorChooser.install(t, sortedList, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE_WITH_UNDO);

        setLayout(new GridBagLayout());
        add(new JLabel("Filter: "),
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 5, 5, 5), 0, 0));
        add(filterEdit, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));
        add(new JScrollPane(t),
                new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 5, 5, 5), 0, 0));
    }

    // TODO(Tharre): for testing purposes
    public static void main(String[] args) throws Exception {
        LConnection server = new LConnection();

        Result<?> result = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR,
                INVENTURGRUPPE.BEZEICHNUNG, USER.CN, MATERIAL.GEFAHRSTUFE)
                .from(MATERIAL).leftOuterJoin(BUNDESNR)
                .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID)).leftOuterJoin(INVENTURGRUPPE)
                .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID)).leftOuterJoin(USER)
                .on(MATERIAL.ERFASSER_ID.equal(USER.ID)).fetch();

        String[] propertyNames =
                {"ID", "Bezeichnung", "Erstelldatum", "Nr", "Inventurgruppe", "Ersteller", "Gefahrstufe"};
        int[] hyperlinkColumns = {4};

        JFrame f = new JFrame();
        f.add(new MultiEdit(result, new RecordTableFormat(propertyNames), hyperlinkColumns));
        f.pack();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
