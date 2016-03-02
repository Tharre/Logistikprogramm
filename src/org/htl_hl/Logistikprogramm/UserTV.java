package org.htl_hl.Logistikprogramm;

import org.jooq.DSLContext;
import org.jooq.Query;

import static sql.generated.logistik_test.Tables.*;


public class UserTV extends AbstractTV {

	private static final String[] columnNames = {"ID", "Vorname", "Nachname", "CN"};
	private static final String[] propertyNames = {"id", "vorname", "nachname", "cn", "perm_mask"};

	private String vorname;
	private String nachname;
	private String cn;
	private int perm_mask;

	public UserTV(int id) {
		super(id, "usr01");
	}

	public UserTV(int id, String vorname, String nachname, String cn, int perm_mask) {
		super(id, "usr01");
		this.vorname = vorname;
		this.nachname = nachname;
		this.cn = cn;
		this.perm_mask = perm_mask;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public String getCn() {
		return cn;
	}

	public int getPerm_mask() {
		return perm_mask;
	}

	@Override
	public String[] getColumnNames() {
		return columnNames;
	}

	@Override
	public String[] getPropertyNames() {
		return propertyNames;
	}

	@Override
	public Query getQuery(DSLContext ctx) {
		return ctx.select(USER.ID, USER.VORNAME, USER.NACHNAME, USER.CN, USER.PERM_MASK)
		          .from(USER)
		          .getQuery();
	}
}
