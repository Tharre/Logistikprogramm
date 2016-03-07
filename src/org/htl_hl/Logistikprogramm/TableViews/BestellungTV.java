package org.htl_hl.Logistikprogramm.TableViews;

import org.htl_hl.Logistikprogramm.Reference;
import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import java.sql.Timestamp;

import static sql.generated.logistik_test.Tables.*;


public class BestellungTV extends AbstractTV {

	private static final String[] columnNames =
			{"ID", "Ersteller", "Firma", "Budget", "Anschrift", "Erstelldatum", "Wnummer", "Bezahlt", "Kommentar"};
	private static final String[] propertyNames =
			{"id", "ersteller", "firma", "budget", "anschrift", "erstelldatum", "wnummer", "bezahlt", "kommentar"};

	private Reference ersteller;
	private Reference firma;
	private Reference budget;
	private Reference anschrift;
	private Timestamp erstelldatum;
	private String wnummer;
	private double bezahlt;
	private String kommentar;

	public BestellungTV(int id) {
		super(id, "bst01");
	}

	public BestellungTV(int id, int erstellerId, String erstellerBezeichnung, int firmaId, String firmaBezeichnung,
	                    int budgetId, String budgetBezeichnung, int anschriftId, String anschriftBezeichnung,
	                    Timestamp erstelldatum, String wnummer, double bezahlt, String kommentar) {
		super(id, "bst01");
		this.ersteller = new Reference("usr01", erstellerId, erstellerBezeichnung);
		this.firma = new Reference("fma01", firmaId, firmaBezeichnung);
		this.budget = new Reference("bug01", budgetId, budgetBezeichnung);
		this.anschrift = new Reference("ans01", anschriftId, anschriftBezeichnung);
		this.erstelldatum = erstelldatum;
		this.wnummer = wnummer;
		this.bezahlt = bezahlt;
		this.kommentar = kommentar;
	}

	public Reference getErsteller() {
		return ersteller;
	}

	public Reference getFirma() {
		return firma;
	}

	public Reference getBudget() {
		return budget;
	}

	public Reference getAnschrift() {
		return anschrift;
	}

	public Timestamp getErstelldatum() {
		return erstelldatum;
	}

	public String getWnummer() {
		return wnummer;
	}

	public double getBezahlt() {
		return bezahlt;
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
		return DSL.select(BESTELLUNG.ID, USER.ID, USER.CN, FIRMA.ID, FIRMA.BEZEICHNUNG, BUDGET.ID, BUDGET.BEZEICHNUNG,
		                  ANSCHRIFT.ID, ANSCHRIFT.BEZEICHNUNG, BESTELLUNG.ERSTELLDATUM, BESTELLUNG.WNUMMER, BESTELLUNG.BEZAHLT,
		                  BESTELLUNG.KOMMENTAR)
				.from(BESTELLUNG)
				.leftOuterJoin(USER)
				.on(BESTELLUNG.ERSTELLER_ID.equal(USER.ID))
				.leftOuterJoin(FIRMA)
				.on(BESTELLUNG.FIRMA_ID.equal(FIRMA.ID))
				.leftOuterJoin(BUDGET)
				.on(BESTELLUNG.BUDGET_ID.equal(BUDGET.ID))
				.leftOuterJoin(ANSCHRIFT)
				.on(BESTELLUNG.ANSCHRIFT_ID.equal(ANSCHRIFT.ID))
				.getQuery();
	}
}
