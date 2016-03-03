package org.htl_hl.Logistikprogramm;

import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import java.sql.Timestamp;

import static sql.generated.logistik_test.Tables.*;


public class MatTV extends AbstractTV {

	private static final String[] columnNames =
			{"ID", "Bezeichnung", "Erstelldatum", "Bundesnr", "Inventurgruppe", "Ersteller", "Gefahrstufe"};
	private static final String[] propertyNames =
			{"id", "bezeichnung", "erstelldatum", "bundesnr", "inventurgruppe", "ersteller", "gefahrstufe"};

	private String bezeichnung;
	private Timestamp erstelldatum;
	private Reference bundesnr;
	private Reference inventurgruppe;
	private Reference ersteller;
	private int gefahrstufe;

	public MatTV(int id) {
		super(id, "mat01");
	}

	public MatTV(int id, String bezeichnung, Timestamp erstelldatum, int bundesnrId, String bundesnrNr,
	             int inventurgruppeId, String inventurgruppeBezeichnung, int userId, String userVorname,
	             int gefahrstufe) {
		super(id, "mat01");
		this.bezeichnung = bezeichnung;
		this.erstelldatum = erstelldatum;
		this.bundesnr = new Reference("bnr01", bundesnrId, bundesnrNr);
		this.inventurgruppe = new Reference("inv01", inventurgruppeId, inventurgruppeBezeichnung);
		this.ersteller = new Reference("usr01", userId, userVorname);
		this.gefahrstufe = gefahrstufe;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public Timestamp getErstelldatum() {
		return erstelldatum;
	}

	public Reference getBundesnr() {
		return bundesnr;
	}

	public Reference getInventurgruppe() {
		return inventurgruppe;
	}

	public Reference getErsteller() {
		return ersteller;
	}

	public String getGefahrstufe() {
		if (gefahrstufe == 0)
			return "Ungefährlich";
		else
			return "Gefährlich";
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
	public ResultQuery getQuery() {
		return DSL.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.ID, BUNDESNR.NR,
		                  INVENTURGRUPPE.ID, INVENTURGRUPPE.BEZEICHNUNG, USER.ID, USER.VORNAME, MATERIAL.GEFAHRSTUFE)
		          .from(MATERIAL)
		          .leftOuterJoin(BUNDESNR)
		          .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
		          .leftOuterJoin(INVENTURGRUPPE)
		          .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
		          .leftOuterJoin(USER)
		          .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
		          .getQuery();
	}
}