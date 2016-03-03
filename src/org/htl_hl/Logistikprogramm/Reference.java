package org.htl_hl.Logistikprogramm;

import org.jooq.ResultQuery;


public class Reference implements Viewable, Comparable<Reference> {

	private String shortcut;
	private String name;
	private int id;

	public Reference(String shortcut, int id, String name) {
		this.shortcut = shortcut;
		this.id = id;
		this.name = name;
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
		return new String[]{String.valueOf(id)};
	}

	@Override
	public int compareTo(Reference o) {
		return name.compareTo(o.getName());
	}

	@Override
	public String[] getColumnNames() {
		throw new UnsupportedOperationException("Can't supply column names from Reference");
	}

	@Override
	public String[] getPropertyNames() {
		throw new UnsupportedOperationException("Can't supply property names from Reference");
	}

	@Override
	public ResultQuery getQuery() {
		throw new UnsupportedOperationException("Can't supply query from Reference");
	}
}
