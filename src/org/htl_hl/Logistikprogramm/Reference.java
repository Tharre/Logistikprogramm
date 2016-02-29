package org.htl_hl.Logistikprogramm;

import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.jooq.DSLContext;
import org.jooq.Query;


public class Reference implements Viewable, Comparable<Reference> {

	private String shortcut;
	private String name;
	private int id;

	public Reference(String shortcut, String name, int id) {
		this.shortcut = shortcut;
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	@Override
	public String getViewShortcut() {
		return shortcut;
	}

	@Override
	public String[] getViewArgs() {
		return new String[] {String.valueOf(id)};
	}

	@Override
	public int compareTo(Reference o) {
		return name.compareTo(o.getName());
	}

	@Override
	public String[] getColumnNames() {
		throw new NotImplementedException("Can't supply column names from Reference");
	}

	@Override
	public String[] getPropertyNames() {
		throw new NotImplementedException("Can't supply property names from Reference");
	}

	@Override
	public Query getQuery(DSLContext ctx) {
		throw new NotImplementedException("Can't supply query from Reference");
	}
}
