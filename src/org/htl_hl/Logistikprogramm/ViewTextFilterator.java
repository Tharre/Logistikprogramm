package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.TextFilterator;

import java.util.List;

public class ViewTextFilterator<E extends Viewable> implements TextFilterator<E> {
    @Override
    public void getFilterStrings(List<String> baseList, E element) {
        for (int i = 0; i < element.getSize(); ++i) {
            if (element.getValue(i) != null) // literal database NULL
                baseList.add(element.getValue(i).toString());
        }
    }
}
