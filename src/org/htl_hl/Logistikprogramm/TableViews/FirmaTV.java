package org.htl_hl.Logistikprogramm.TableViews;

import org.htl_hl.Logistikprogramm.Reference;
import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import java.sql.Timestamp;

import static sql.generated.logistik_test.Tables.FIRMA;
import static sql.generated.logistik_test.Tables.STAAT;


public class FirmaTV extends AbstractTV {

	private static final String[] columnNames =
			{"ID", "Bezeichnung", "Erstelldatum", "Staat", "Ort", "PLZ", "Straße", "E-Mail", "Kunden Nr.",
			 "Sachbearbeiter", "Telefon", "Fax", "Homepage", "Konditionen", "Kreditoren Nr.", "UMS", "ARA",
			 "Kommentar"};

	private static final String[] propertyNames =
			{"id", "bezeichnung", "erstelldatum", "staat", "ort", "plz", "strasse", "mail", "kundennr",
			 "sachbearbeiter", "telefon", "fax", "homepage", "konditionen", "kreditorennr", "umsNr", "araNr",
			 "kommentar"};

	private String bezeichnung;
	private Timestamp erstelldatum;
	private Reference staat;
	private String ort;
	private String plz;
	private String strasse;
	private String mail;
	private String kundennr;
	private String sachbearbeiter;
	private String telefon;
	private String fax;
	private String homepage;
	private String konditionen;
	private String kreditorennr;
	private String umsNr;
	private String araNr;
	private String kommentar;

	public FirmaTV(int id) {
		super(id, "fma01");
	}

	public FirmaTV(int id, String bezeichnung, Timestamp erstelldatum, int staatId, String staatBezeichnung, String ort,
	               String plz, String strasse, String mail, String kundennr, String sachbearbeiter, String telefon,
	               String fax, String homepage, String konditionen, String kreditorennr, String umsNr, String araNr,
	               String kommentar) {
		super(id, "fma01");

		this.bezeichnung = bezeichnung;
		this.erstelldatum = erstelldatum;
		this.staat = new Reference("sta01", staatId, staatBezeichnung);
		this.ort = ort;
		this.plz = plz;
		this.strasse = strasse;
		this.mail = mail;
		this.kundennr = kundennr;
		this.sachbearbeiter = sachbearbeiter;
		this.telefon = telefon;
		this.fax = fax;
		this.homepage = homepage;
		this.konditionen = konditionen;
		this.kreditorennr = kreditorennr;
		this.umsNr = umsNr;
		this.araNr = araNr;
		this.kommentar = kommentar;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public Timestamp getErstelldatum() {
		return erstelldatum;
	}

	public Reference getStaat() {
		return staat;
	}

	public String getOrt() {
		return ort;
	}

	public String getPlz() {
		return plz;
	}

	public String getStrasse() {
		return strasse;
	}

	public String getMail() {
		return mail;
	}

	public String getKundennr() {
		return kundennr;
	}

	public String getSachbearbeiter() {
		return sachbearbeiter;
	}

	public String getTelefon() {
		return telefon;
	}

	public String getFax() {
		return fax;
	}

	public String getHomepage() {
		return homepage;
	}

	public String getKonditionen() {
		return konditionen;
	}

	public String getKreditorennr() {
		return kreditorennr;
	}

	public String getUmsNr() {
		return umsNr;
	}

	public String getAraNr() {
		return araNr;
	}

	public String getKommentar() {
		return kommentar;
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
		return DSL.select(FIRMA.ID, FIRMA.BEZEICHNUNG, FIRMA.ERSTELLDATUM, STAAT.ID, STAAT.BEZEICHNUNG, FIRMA.ORT,
		                  FIRMA.PLZ, FIRMA.STRASSE, FIRMA.MAIL, FIRMA.KUNDENNR, FIRMA.SACHBEARBEITER, FIRMA.TELEFON,
		                  FIRMA.FAX, FIRMA.HOMEPAGE, FIRMA.KONDITIONEN, FIRMA.KREDITORENNR, FIRMA.UMSNR, FIRMA.ARANR,
		                  FIRMA.KOMMENTAR)
		          .from(FIRMA)
		          .leftOuterJoin(STAAT)
		          .on(FIRMA.STAAT_ID.equal(STAAT.ID))
		          .getQuery();
	}
}
