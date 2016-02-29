package org.htl_hl.Logistikprogramm;

public abstract class AbstractTV<T extends AbstractTV<T>> implements Viewable, Comparable<T> {

    private final int id;
    private final String shortcut;

    public AbstractTV(int id, String shortcut) {
        this.id = id;
        this.shortcut = shortcut;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(T o) {
        return Integer.compare(id, o.getId());
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
