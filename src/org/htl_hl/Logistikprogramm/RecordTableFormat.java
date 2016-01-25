package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.gui.TableFormat;
import org.jooq.Record;

public class RecordTableFormat implements TableFormat {

    private final String[] propertyNames;

    public RecordTableFormat(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }

    public int getColumnCount() {
        return propertyNames.length;
    }

    public String getColumnName(int column) {
        return propertyNames[column];
    }

    public Object getColumnValue(Object baseObject, int column) {
        Record record = (Record) baseObject;

        return record.getValue(column);
    }
}
