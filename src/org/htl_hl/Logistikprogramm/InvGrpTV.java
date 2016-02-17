package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;

import java.beans.ConstructorProperties;
import java.util.List;

import static sql.generated.logistik_test.Tables.*;


public class InvGrpTV implements Viewable, Comparable<InvGrpTV> {

    private static final String[] columnNames = {"ID", "Bezeichnung"};

    private final int id;
    private final String bezeichnung;

    @ConstructorProperties({"id", "bezeichnung"})
    public InvGrpTV(int id, String bezeichnung) {
        this.id = id;
        this.bezeichnung = bezeichnung;
    }

    public int getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public String toString() {
        return bezeichnung;
    }

    @Override
    public int compareTo(InvGrpTV o) {
        return Integer.compare(this.id, o.id);
    }

    public static TableFormat<InvGrpTV> getTableFormat() {
        return new TableFormat<InvGrpTV>() {
            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public Object getColumnValue(InvGrpTV baseObject, int column) {
                switch (column) {
                    case 0: return baseObject.getId();
                    case 1: return baseObject.getBezeichnung();
                    default: throw new IllegalStateException();
                }
            }
        };
    }

    public static TextFilterator<InvGrpTV> getTextFilterator() {
        return new TextFilterator<InvGrpTV>() {
            @Override
            public void getFilterStrings(List<String> baseList, InvGrpTV element) {
                baseList.add(String.valueOf(element.getId()));
                baseList.add(String.valueOf(element.getBezeichnung()));
            }
        };
    }

    @Override
    public String getViewShortcut() {
        return "inv01";
    }

    @Override
    public String[] getViewArgs() {
        return new String[] {String.valueOf(id)};
    }
}
