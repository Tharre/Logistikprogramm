package org.htl_hl.Logistikprogramm;

public abstract class AbstractTV<T extends AbstractTV<T>> implements Viewable, Comparable<T> {

    protected final int id;
    private final String[] columnNames;
    private final String shortcut;

    public AbstractTV(int id, String[] columnNames, String shortcut) {
        this.id = id;
        this.columnNames = columnNames;
        this.shortcut = shortcut;
    }

    @Override
    public int compareTo(T o) {
        return Integer.compare(this.id, o.id);
    }

    @Override
    public int getSize() {
        return columnNames.length;
    }

    @Override
    public String getViewShortcut() {
        return shortcut;
    }

    @Override
    public String[] getViewArgs() {
        return new String[]{String.valueOf(id)};
    }
}
