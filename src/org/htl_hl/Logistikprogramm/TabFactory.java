package org.htl_hl.Logistikprogramm;

import org.jooq.ResultQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.impl.DSL;

import java.util.HashMap;

import static sql.generated.logistik_test.Tables.*;


public class TabFactory {

	private LConnection server;
	private TabManager tm;
	private HashMap<Object, Tab> tabs = new HashMap<>();

	private <E extends TableRecord> void populateHelper(String name, String[] cn, ResultQuery<?> query,
	                                                    Table<E> table) {
		tabs.put(table, new JOOQTableView<>(server, tm, name, cn, query, table));
	}

	public TabFactory(LConnection server, TabManager tm) {
		this.server = server;
		this.tm = tm;

		populateHelper("Firma anzeigen",
		               new String[]{"ID", "Bezeichnung", "Erstelldatum", "Staat", "Ort", "PLZ", "Straße", "E-Mail",
		                            "Kunden Nr.", "Sachbearbeiter", "Telefon", "Fax", "Homepage", "Konditionen",
		                            "Kreditoren Nr.", "UMS", "ARA", "Kommentar"}, DSL.selectFrom(FIRMA).getQuery(),
		               FIRMA);

		populateHelper("Material anzeigen",
		               new String[]{"ID", "Bezeichnung", "Erstelldatum", "Bundesnr", "Inventurgruppe", "Ersteller",
		                            "Gefahrstufe"}, DSL.selectFrom(MATERIAL).getQuery(), MATERIAL);

		populateHelper("Inventurgruppe anzeigen", new String[]{"ID", "Bezeichnung"},
		               DSL.selectFrom(INVENTURGRUPPE).getQuery(), INVENTURGRUPPE);

		populateHelper("Bundesnr anzeigen", new String[]{"ID", "Nr.", "Bezeichnung"},
		               DSL.selectFrom(BUNDESNR).getQuery(), BUNDESNR);

		populateHelper("BANF anzeigen",
		               new String[]{"ID", "Antragsteller", "Kostenstelle", "Erstelldatum", "Wunschdatum", "Kommentar",
		                            "Status"},
		               DSL.select(BANF.ID, BANF.ANTRAGSTELLER_ID, BANF.KOSTENSTELLE_ID, BANF.ERSTELLDATUM,
		                          BANF.WUNSCHDATUM, BANF.KOMMENTAR,
		                          DSL.when(DSL.min(POSITION.STATUS).equal(1).and(DSL.max(POSITION.STATUS).equal(1)),
		                                   "nicht bearbeitet")
		                             .when(DSL.min(POSITION.STATUS)
		                                      .equal(1)
		                                      .and(DSL.max(POSITION.STATUS).greaterThan(1)), "teilweise bearbeitet")
		                             .when(DSL.min(POSITION.STATUS).equal(2).and(DSL.max(POSITION.STATUS).equal(2)),
		                                   "abgewiesen")
		                             .when(DSL.min(POSITION.STATUS)
		                                      .equal(2)
		                                      .and(DSL.max(POSITION.STATUS).greaterThan(2)), "teilweise angenommen")
		                             .when(DSL.min(POSITION.STATUS)
		                                      .greaterThan(2)
		                                      .and(DSL.max(POSITION.STATUS).greaterThan(2)), "vollständig angenommen"))
		                  .from(BANF)
		                  .leftOuterJoin(BANFPOSITION)
		                  .on(BANF.ID.equal(BANFPOSITION.BANF_ID))
		                  .leftOuterJoin(POSITION)
		                  .on(BANFPOSITION.POSITION_ID.equal(POSITION.ID))
		                  .groupBy(BANF.ID)
		                  .getQuery(), BANF);

		/*
		populateHelper("Test",
		               new String[]{"ID", "Antragsteller", "Kostenstelle", "Erstelldatum", "Wunschdatum", "Kommentar",
		                            "Status"},
		               DSL.selectFrom(FULLBANF).getQuery(), FULLBANF);
		*/

		populateHelper("Bestellung anzeigen",
		               new String[]{"ID", "Ersteller", "Firma", "Budget", "Anschrift", "Erstelldatum", "Wnummer",
		                            "Bezahlt", "Kommentar"}, DSL.selectFrom(BESTELLUNG).getQuery(), BESTELLUNG);

		populateHelper("Budget anzeigen", new String[]{"ID", "Bezeichnung"}, DSL.selectFrom(BUDGET).getQuery(), BUDGET);

		populateHelper("Einheit anzeigen", new String[]{"ID", "Bezeichnung"}, DSL.selectFrom(EINHEIT).getQuery(),
		               EINHEIT);

		populateHelper("Kostenstelle anzeigen", new String[]{"ID", "Bezeichnung"},
		               DSL.selectFrom(KOSTENSTELLE).getQuery(), KOSTENSTELLE);

		populateHelper("Staat anzeigen", new String[]{"ID", "Bezeichnung"}, DSL.selectFrom(STAAT).getQuery(), STAAT);

		populateHelper("User anzeigen", new String[]{"ID", "Vorname", "Nachname", "CN"},
		               DSL.select(USER.ID, USER.VORNAME, USER.NACHNAME, USER.CN).from(USER).getQuery(), USER);

		populateHelper("Anschrift anzeigen", new String[]{"ID", "Bezeichnung"}, DSL.selectFrom(ANSCHRIFT).getQuery(),
		               ANSCHRIFT);


		tabs.put(AbfragenGUI.class, new AbfragenGUI());
	}

	public Tab getTab(Object identifier) {
		return tabs.get(identifier);
	}
}
