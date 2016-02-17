package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.gui.TableFormat;

public class ViewTableFormat<E extends Viewable> implements TableFormat<E> {

    private final String[] propertyNames;

    public ViewTableFormat(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }

    public int getColumnCount() {
        return propertyNames.length;
    }

    public String getColumnName(int column) {
        return propertyNames[column];
    }

    public Object getColumnValue(E object, int column) {
        return object.getValue(column);
    }
}
