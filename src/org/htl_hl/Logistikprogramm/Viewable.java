package org.htl_hl.Logistikprogramm;

import org.jooq.Record;
import org.jooq.ResultQuery;


// TODO: used by HyperlinkRenderer to open a new tab
public interface Viewable<E extends Record> {

	String getViewShortcut();

	String[] getViewArgs();

	// TODO: These 3 Methods should really be static, however sadly this is only supported in Java >= 8
	String[] getColumnNames();

	String[] getPropertyNames();

	ResultQuery<E> getQuery();
}
