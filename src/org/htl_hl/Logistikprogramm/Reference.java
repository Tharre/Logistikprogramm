package org.htl_hl.Logistikprogramm;


public class Reference implements Comparable<Reference> {

	private Object identifier;
	private String name;
	private Object id;

	public Reference(Object identifier, Object id, String name) {
		this.identifier = identifier;
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public Object getIdentifier() {
		return identifier;
	}

	public Object getId() {
		return id;
	}

	@Override
	public int compareTo(Reference o) {
		return name.compareTo(o.getName());
	}
}
