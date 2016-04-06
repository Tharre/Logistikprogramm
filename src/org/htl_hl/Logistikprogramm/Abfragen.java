package org.htl_hl.Logistikprogramm;

import org.jooq.ResultQuery;

import sql.generated.logistik_2.tables.Banfpos;

import static sql.generated.logistik_test.Tables.*;

import java.sql.Timestamp;
import java.util.Calendar;

// Mats = materials
// Wh = where
// Btw = between
// n = and
// St = sortiert nach
// X, Y = Parameter
// Bn = Bundesnummer
// Ig = Inventurgruppe
// Best = Bestellung

public class Abfragen {

	private static final String[] CNMATS = {"ID","Material","Erstelldatum","Bundesnummer",
			"Inventurgruppe","Erfasser","Gefahrstufe"};
	
	// Material
	// Welche Materialien gibt es?
	public static ResultWithCn selectMats() throws Exception {
		LConnection server = new LConnection();
		ResultQuery r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, MATERIAL.BUNDESNR_ID,
				MATERIAL.INVENTURGRP_ID, MATERIAL.ERFASSER_ID, MATERIAL.GEFAHRSTUFE)
				.from(MATERIAL)
				.getQuery();

		return new ResultWithCn(r, CNMATS);
	}
	
	// Welche Materialien wurden zwischen den Daten x und y erstellt?
	public static ResultWithCn selectMatsWhDateIsBtwXnY(Timestamp x, Timestamp y) throws Exception {
		LConnection server = new LConnection();
		ResultQuery r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, MATERIAL.BUNDESNR_ID,
				MATERIAL.INVENTURGRP_ID, MATERIAL.ERFASSER_ID, MATERIAL.GEFAHRSTUFE)
				.from(MATERIAL)
				.where(MATERIAL.ERSTELLDATUM.between(x, y))
				.getQuery();


		return new ResultWithCn(r, CNMATS);
	}
	
	// Welches Material hat die Bundesnummer x?
	public static ResultWithCn selectMatsWhBnIsX(String x) throws Exception {
		LConnection server = new LConnection();
		ResultQuery r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, MATERIAL.BUNDESNR_ID,
				MATERIAL.INVENTURGRP_ID, MATERIAL.ERFASSER_ID, MATERIAL.GEFAHRSTUFE)
				.from(MATERIAL)
				.leftOuterJoin(BUNDESNR)
				.on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
				.where(BUNDESNR.NR.equal(x))
				.getQuery();

		return new ResultWithCn(r, CNMATS);
	}
	
	// Welche Materialen sind in der Inventurgruppe x?
	public static ResultWithCn selectMatsWhIgIsX(int x) throws Exception {
		LConnection server = new LConnection();
		ResultQuery r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, MATERIAL.BUNDESNR_ID,
				MATERIAL.INVENTURGRP_ID, MATERIAL.ERFASSER_ID, MATERIAL.GEFAHRSTUFE)
				.from(MATERIAL)
				.leftOuterJoin(INVENTURGRUPPE)
				.on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
				.where(INVENTURGRUPPE.ID.equal(x))
				.getQuery();

		return new ResultWithCn(r, CNMATS);
	}
	
	// größter / kleinster Preis für Material x (+Firma zu Preis)
	public static ResultWithCn selectPriceWhMatIsX(int x) throws Exception {
		LConnection server = new LConnection();
		String[] cn = {"ID","Material","Firma","Preis","Einheit"};
		ResultQuery r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, FIRMA_MATERIAL.FIRMA_ID,
				FIRMA_MATERIAL.PREIS.max(), FIRMA_MATERIAL.EINHEIT_ID)
				.from(FIRMA_MATERIAL)
				.leftOuterJoin(MATERIAL)
				.on(MATERIAL.ID.equal(FIRMA_MATERIAL.MATERIAL_ID))
				.where(FIRMA_MATERIAL.MATERIAL_ID.equal(x))
				.groupBy(FIRMA_MATERIAL.EINHEIT_ID)
				.union(server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, FIRMA_MATERIAL.FIRMA_ID,
						FIRMA_MATERIAL.PREIS.min(), FIRMA_MATERIAL.EINHEIT_ID)
						.from(FIRMA_MATERIAL)
						.leftOuterJoin(MATERIAL)
						.on(MATERIAL.ID.equal(FIRMA_MATERIAL.MATERIAL_ID))
						.where(FIRMA_MATERIAL.MATERIAL_ID.equal(x))
						.groupBy(FIRMA_MATERIAL.EINHEIT_ID))
				.orderBy(FIRMA_MATERIAL.EINHEIT_ID)
				.getQuery();

		return new ResultWithCn(r, cn);
	}
	
	// Firma-Material
	// Welche Materialien können bei der Firma x bestellt werden?
	public static ResultWithCn selectMatsWhFirmaIsX(int x) throws Exception {
		LConnection server = new LConnection();
		String[] cn = {"Firma","Material","Bestelleinheit","Erstelldatum","Bundesnummer",
						"Inventurgruppe","Erfasser","Gefahrstufe"};
		ResultQuery r = server.create.select(FIRMA_MATERIAL.FIRMA_ID, FIRMA_MATERIAL.MATERIAL_ID,
				FIRMA_MATERIAL.EINHEIT_ID, MATERIAL.ERSTELLDATUM, MATERIAL.BUNDESNR_ID, MATERIAL.INVENTURGRP_ID,
				MATERIAL.ERFASSER_ID, MATERIAL.GEFAHRSTUFE)
				.from(FIRMA_MATERIAL)
				.leftOuterJoin(MATERIAL)
				.on(MATERIAL.ID.equal(FIRMA_MATERIAL.MATERIAL_ID))
				.where(FIRMA_MATERIAL.FIRMA_ID.equal(x))
				.getQuery();

		return new ResultWithCn(r, cn);
	}
	
	// Bei welchen Firmen kann man Material x bestellen?
	public static ResultWithCn selectFirmaWhMatsIsX(int x) throws Exception {
		LConnection server = new LConnection();
		String[] cn = {"Firma","Material","Preis","Einheit"};
		ResultQuery r = server.create.select(FIRMA_MATERIAL.FIRMA_ID, FIRMA_MATERIAL.MATERIAL_ID,
				FIRMA_MATERIAL.PREIS, FIRMA_MATERIAL.EINHEIT_ID)
				.from(FIRMA_MATERIAL)
				.where(FIRMA_MATERIAL.MATERIAL_ID.equal(x))
				.getQuery();

		return new ResultWithCn(r, cn);
	}
	
	// Banf/Best.
	// Alle Banfs von User x anzeigen
	public static ResultWithCn selectBanfWhUserIsX(int x) throws Exception {
		LConnection server = new LConnection();
		String[] cn = {"ID","Antragssteller","Kostenstelle","Erstelldatum","Wunschdatum"};
		ResultQuery r = server.create.select(BANF.ID, BANF.ANTRAGSTELLER_ID, BANF.KOSTENSTELLE_ID, 
				BANF.ERSTELLDATUM, BANF.WUNSCHDATUM)
				.from(BANF)
				.where(BANF.ANTRAGSTELLER_ID.equal(x))
				.getQuery();

		return new ResultWithCn(r, cn);
	}
	
	// Banfs zu einer Bestellung
	public static ResultWithCn selectBanfWhBestIsX(int x) throws Exception {
		LConnection server = new LConnection();
		String[] cn = {"BANF","Position","Material","Menge","Preis","Einheit","Status","Antragssteller",
						"Kostenstelle","Erstelldatum","Wunschdatum"};
		ResultQuery r = server.create.select(BANFPOSITION.BANF_ID, BANFPOSITION.POSITION_ID, POSITION.MATERIAL_ID,
				POSITION.MENGE,	POSITION.PREIS, POSITION.EINHEIT_ID, POSITION.STATUS, BANF.ANTRAGSTELLER_ID,
				BANF.KOSTENSTELLE_ID, BANF.ERSTELLDATUM, BANF.WUNSCHDATUM)
				.from(BANFPOSITION)
				.leftOuterJoin(BANF)
				.on(BANF.ID.equal(BANFPOSITION.BANF_ID))
				.leftOuterJoin(POSITION)
				.on(POSITION.ID.equal(BANFPOSITION.POSITION_ID))
				.leftOuterJoin(BESTELLPOSITION)
				.on(POSITION.ID.equal(BESTELLPOSITION.POSITION_ID))
				.where(BESTELLPOSITION.BESTELL_ID.equal(x))
				.getQuery();

		return new ResultWithCn(r, cn);
	}
	
	// Bestellung zu einer Banf
	public static ResultWithCn selectBestWhBanfIsX(int x) throws Exception {
		LConnection server = new LConnection();
		String[] cn = {"BANF","Position","Material","Menge","Preis","Einheit","Status","Ersteller","Firma",
				"Budget","Anschrift","Erstelldatum","Wnummer","bezahlt","Kommentar"};
		ResultQuery r = server.create.select(BANFPOSITION.BANF_ID, BANFPOSITION.POSITION_ID, POSITION.MATERIAL_ID,
				POSITION.MENGE, POSITION.PREIS, POSITION.EINHEIT_ID, POSITION.STATUS, BESTELLUNG.ERSTELLER_ID,
				BESTELLUNG.FIRMA_ID, BESTELLUNG.BUDGET_ID, BESTELLUNG.ANSCHRIFT_ID, BESTELLUNG.ERSTELLDATUM,
				BESTELLUNG.WNUMMER, BESTELLUNG.BEZAHLT, BESTELLUNG.KOMMENTAR)
				.from(BANFPOSITION)
				.leftOuterJoin(POSITION)
				.on(POSITION.ID.equal(BANFPOSITION.POSITION_ID))
				.leftOuterJoin(BESTELLPOSITION)
				.on(POSITION.ID.equal(BESTELLPOSITION.POSITION_ID))
				.leftOuterJoin(BESTELLUNG)
				.on(BESTELLUNG.ID.equal(BESTELLPOSITION.BESTELL_ID))
				.where(BANFPOSITION.BANF_ID.equal(x))
				.getQuery();

		return new ResultWithCn(r, cn);
	}
}

