package org.htl_hl.Logistikprogramm;

import org.jooq.Result;

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
	
	public static void main(String[] args){
		Timestamp t1 = new Timestamp((2015-1900), 9, 8, 0, 0, 0, 0);
		Timestamp t2 = new Timestamp((2015-1900), 9, 9, 0, 0, 0, 0);
		String bn = "0";
		int ig = 128;
		int mats = 6;
		int firma = 2;
		int user = 19;
		int best = 495;
		int banf = 0;
		try {
//			System.out.println(selectMatsWhDateIsBtwXnY(t1, t2));
//			System.out.println(selectMatsWhBnIsX(bn));
//			System.out.println(selectMatsWhIgIsX(ig));
//			System.out.println(selectPriceWhMatsIsX(mats));
//			System.out.println(selectMatsWhFirmaIsX(firma));
//			System.out.println(selectFirmaWhMatsIsX(mats));
//			System.out.println(selectBanfsWhUserIsX(user));
//			System.out.println(selectBanfWhBestIsX(best)); // nicht so wies sein soll
//			System.out.println(selectBestWhBanfIsX(banf)); // nicht so wies sein soll
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Material
	// Welche Materialien gibt es?
	public static Result selectMats() throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR.as("Bundesnummer"),
		                                  MATERIAL.INVENTURGRP_ID, INVENTURGRUPPE.BEZEICHNUNG.as("Inventurgruppe"), 
		                                  MATERIAL.ERFASSER_ID, USER.VORNAME, MATERIAL.GEFAHRSTUFE)
		                          .from(MATERIAL)
		                          .leftOuterJoin(BUNDESNR)
		                          .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
		                          .leftOuterJoin(INVENTURGRUPPE)
		                          .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
		                          .leftOuterJoin(USER)
		                          .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
		                          .fetch();

		return r;
	}
	
	// Welche Materialien wurden zwischen den Daten x und y erstellt?
	public static Result selectMatsWhDateIsBtwXnY(Timestamp x, Timestamp y) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR.as("Bundesnummer"),
										MATERIAL.INVENTURGRP_ID, INVENTURGRUPPE.BEZEICHNUNG.as("Inventurgruppe"),
										MATERIAL.ERFASSER_ID, USER.VORNAME, MATERIAL.GEFAHRSTUFE)
						        .from(MATERIAL)
						        .leftOuterJoin(BUNDESNR)
						        .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
						        .leftOuterJoin(INVENTURGRUPPE)
						        .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
						        .leftOuterJoin(USER)
						        .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
						        .where(MATERIAL.ERSTELLDATUM.between(x, y))
						        .fetch();

		
		return r;
	}
	
	// Welches Material hat die Bundesnummer x?
	public static Result selectMatsWhBnIsX(String x) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR.as("Bundesnummer"),
										MATERIAL.INVENTURGRP_ID, INVENTURGRUPPE.BEZEICHNUNG.as("Inventurgruppe"),
										MATERIAL.ERFASSER_ID, USER.VORNAME, MATERIAL.GEFAHRSTUFE)
		                          .from(MATERIAL)
		                          .leftOuterJoin(BUNDESNR)
		                          .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
		                          .leftOuterJoin(INVENTURGRUPPE)
		                          .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
		                          .leftOuterJoin(USER)
		                          .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
		                          .where(BUNDESNR.NR.equal(x))
		                          .fetch();

		return r;
	}
	
	// Welche Materialen sind in der Inventurgruppe x?
	public static Result selectMatsWhIgIsX(int x) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM, BUNDESNR.NR.as("Bundesnummer"),
										MATERIAL.INVENTURGRP_ID, INVENTURGRUPPE.BEZEICHNUNG.as("Inventurgruppe"),
										MATERIAL.ERFASSER_ID, USER.VORNAME, MATERIAL.GEFAHRSTUFE)
		                          .from(MATERIAL)
		                          .leftOuterJoin(BUNDESNR)
		                          .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
		                          .leftOuterJoin(INVENTURGRUPPE)
		                          .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
		                          .leftOuterJoin(USER)
		                          .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
		                          .where(INVENTURGRUPPE.ID.equal(x))
		                          .fetch();

		return r;
	}
	
	// größter / kleinster Preis für Material x (+Firma zu Preis)
	public static Result selectPriceWhMatsIsX(int x) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG.as("Material"), FIRMA_MATERIAL.FIRMA_ID, FIRMA.BEZEICHNUNG.as("Firma"),
										FIRMA_MATERIAL.PREIS.max().as("Preis"), EINHEIT.BEZEICHNUNG.as("Einheit"))
		                          .from(FIRMA_MATERIAL)
		                          .leftOuterJoin(MATERIAL)
		                          .on(MATERIAL.ID.equal(FIRMA_MATERIAL.MATERIAL_ID))
		                          .leftOuterJoin(FIRMA)
		                          .on(FIRMA.ID.equal(FIRMA_MATERIAL.FIRMA_ID))
		                          .leftOuterJoin(EINHEIT)
		                          .on(EINHEIT.ID.equal(FIRMA_MATERIAL.EINHEIT_ID))
		                          .where(FIRMA_MATERIAL.MATERIAL_ID.equal(x))
		                          .groupBy(FIRMA_MATERIAL.EINHEIT_ID)
		                          .union(server.create.select(MATERIAL.ID, MATERIAL.BEZEICHNUNG.as("Material"), FIRMA_MATERIAL.FIRMA_ID,
		                        		  FIRMA.BEZEICHNUNG.as("Firma"), FIRMA_MATERIAL.PREIS.min(), EINHEIT.BEZEICHNUNG.as("Einheit"))
				                          .from(FIRMA_MATERIAL)
				                          .leftOuterJoin(MATERIAL)
				                          .on(MATERIAL.ID.equal(FIRMA_MATERIAL.MATERIAL_ID))
				                          .leftOuterJoin(FIRMA)
				                          .on(FIRMA.ID.equal(FIRMA_MATERIAL.FIRMA_ID))
				                          .leftOuterJoin(EINHEIT)
				                          .on(EINHEIT.ID.equal(FIRMA_MATERIAL.EINHEIT_ID))
				                          .where(FIRMA_MATERIAL.MATERIAL_ID.equal(x))
				                          .groupBy(FIRMA_MATERIAL.EINHEIT_ID))
		                          .orderBy(EINHEIT.BEZEICHNUNG.as("Einheit"))
		                          .fetch();

		return r;
	}
	
	// Firma-Material
	// Welche Materialien können bei der Firma x bestellt werden?
	public static Result selectMatsWhFirmaIsX(int x) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(FIRMA_MATERIAL.FIRMA_ID, FIRMA_MATERIAL.MATERIAL_ID, MATERIAL.BEZEICHNUNG, MATERIAL.ERSTELLDATUM,
										BUNDESNR.NR.as("Bundesnummer"), MATERIAL.INVENTURGRP_ID, INVENTURGRUPPE.BEZEICHNUNG.as("Inventurgruppe"),
										MATERIAL.ERFASSER_ID, USER.VORNAME, MATERIAL.GEFAHRSTUFE, EINHEIT.BEZEICHNUNG.as("Bestelleinheit"))
		                          .from(FIRMA_MATERIAL)
		                          .leftOuterJoin(MATERIAL)
		                          .on(MATERIAL.ID.equal(FIRMA_MATERIAL.MATERIAL_ID))
		                          .leftOuterJoin(USER)
		                          .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
		                          .leftOuterJoin(BUNDESNR)
		                          .on(MATERIAL.BUNDESNR_ID.equal(BUNDESNR.ID))
		                          .leftOuterJoin(INVENTURGRUPPE)
		                          .on(MATERIAL.INVENTURGRP_ID.equal(INVENTURGRUPPE.ID))
		                          .leftOuterJoin(EINHEIT)
		                          .on(EINHEIT.ID.equal(FIRMA_MATERIAL.EINHEIT_ID))
		                          .where(FIRMA_MATERIAL.FIRMA_ID.equal(x))
		                          .fetch();

		return r;
	}
	
	// Bei welchen Firmen kann man Material x bestellen?
	public static Result selectFirmaWhMatsIsX(int x) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(FIRMA_MATERIAL.FIRMA_ID, FIRMA_MATERIAL.MATERIAL_ID, FIRMA.BEZEICHNUNG.as("Firma"),
										FIRMA_MATERIAL.PREIS, EINHEIT.BEZEICHNUNG.as("Einheit"))
		                          .from(FIRMA_MATERIAL)
		                          .leftOuterJoin(FIRMA)
		                          .on(FIRMA.ID.equal(FIRMA_MATERIAL.FIRMA_ID))
		                          .leftOuterJoin(MATERIAL)
		                          .on(MATERIAL.ID.equal(FIRMA_MATERIAL.MATERIAL_ID))
		                          .leftOuterJoin(USER)
		                          .on(MATERIAL.ERFASSER_ID.equal(USER.ID))
		                          .leftOuterJoin(EINHEIT)
		                          .on(FIRMA_MATERIAL.EINHEIT_ID.equal(EINHEIT.ID))
		                          .where(FIRMA_MATERIAL.MATERIAL_ID.equal(x))
		                          .fetch();

		return r;
	}
	
	// Banf/Best.
	// Alle Banfs von User x anzeigen
	public static Result selectBanfsWhUserIsX(int x) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(BANF.ID, BANF.ANTRAGSTELLER_ID, BANF.KOSTENSTELLE_ID, 
										KOSTENSTELLE.BEZEICHNUNG.as("Kostenstelle"), BANF.ERSTELLDATUM, BANF.WUNSCHDATUM)
		                          .from(BANF)
		                          .leftOuterJoin(KOSTENSTELLE)
		                          .on(KOSTENSTELLE.ID.equal(BANF.KOSTENSTELLE_ID))
		                          .where(BANF.ANTRAGSTELLER_ID.equal(x))
		                          .fetch();

		return r;
	}
	
	// Banfs zu einer Bestellung
	public static Result selectBanfWhBestIsX(int x) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(BANFPOSITION.BANF_ID, BANFPOSITION.POSITION_ID, POSITION.MATERIAL_ID, POSITION.MENGE,
										EINHEIT.BEZEICHNUNG.as("Einheit"), POSITION.PREIS, POSITION.STATUS, BANF.ANTRAGSTELLER_ID, 
										BANF.KOSTENSTELLE_ID, KOSTENSTELLE.BEZEICHNUNG.as("Kostenstelle"), BANF.ERSTELLDATUM,
										BANF.WUNSCHDATUM)
		                          .from(BANFPOSITION)
		                          .leftOuterJoin(BANF)
		                          .on(BANF.ID.equal(BANFPOSITION.BANF_ID))
		                          .leftOuterJoin(KOSTENSTELLE)
		                          .on(KOSTENSTELLE.ID.equal(BANF.KOSTENSTELLE_ID))
		                          .leftOuterJoin(POSITION)
		                          .on(POSITION.ID.equal(BANFPOSITION.POSITION_ID))
		                          .leftOuterJoin(BESTELLPOSITION)
		                          .on(POSITION.ID.equal(BESTELLPOSITION.POSITION_ID))
		                          .leftOuterJoin(EINHEIT)
		                          .on(EINHEIT.ID.equal(POSITION.EINHEIT_ID))
		                          .where(BESTELLPOSITION.BESTELL_ID.equal(x))
		                          .fetch();

		return r;
	}
	
	// Bestellung zu einer Banf
	public static Result selectBestWhBanfIsX(int x) throws Exception {
		LConnection server = new LConnection();
		Result r = server.create.select(BANFPOSITION.BANF_ID, BANFPOSITION.POSITION_ID, POSITION.MATERIAL_ID,
										MATERIAL.BEZEICHNUNG.as("Material"), POSITION.MENGE, POSITION.EINHEIT_ID,
										POSITION.PREIS, POSITION.STATUS, BESTELLUNG.ERSTELLER_ID, USER.VORNAME,
										BESTELLUNG.FIRMA_ID, FIRMA.BEZEICHNUNG.as("Firma"), BESTELLUNG.BUDGET_ID,
										BUDGET.BEZEICHNUNG, BESTELLUNG.ANSCHRIFT_ID, 
										ANSCHRIFT.BEZEICHNUNG.as("Anschrift"), BESTELLUNG.ERSTELLDATUM, 
										BESTELLUNG.WNUMMER, BESTELLUNG.BEZAHLT, BESTELLUNG.KOMMENTAR)
		                          .from(BANFPOSITION)
		                          .leftOuterJoin(POSITION)
		                          .on(POSITION.ID.equal(BANFPOSITION.POSITION_ID))
		                          .leftOuterJoin(MATERIAL)
		                          .on(MATERIAL.ID.equal(POSITION.MATERIAL_ID))
		                          .leftOuterJoin(BESTELLPOSITION)
		                          .on(POSITION.ID.equal(BESTELLPOSITION.POSITION_ID))
		                          .leftOuterJoin(BESTELLUNG)
		                          .on(BESTELLUNG.ID.equal(BESTELLPOSITION.BESTELL_ID))
		                          .leftOuterJoin(USER)
		                          .on(BESTELLUNG.ERSTELLER_ID.equal(USER.ID))
		                          .leftOuterJoin(FIRMA)
		                          .on(BESTELLUNG.FIRMA_ID.equal(FIRMA.ID))
		                          .leftOuterJoin(BUDGET)
		                          .on(BESTELLUNG.BUDGET_ID.equal(BUDGET.ID))
		                          .leftOuterJoin(ANSCHRIFT)
		                          .on(BESTELLUNG.ANSCHRIFT_ID.equal(ANSCHRIFT.ID))
		                          .where(BANFPOSITION.BANF_ID.equal(x))
		                          .fetch();

		return r;
	}
}

