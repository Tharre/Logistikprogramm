package org.htl_hl.Logistikprogramm;

public interface Viewable {

    Object getValue(int column);

    int getSize();

    String getViewShortcut();

    String[] getViewArgs();

}
