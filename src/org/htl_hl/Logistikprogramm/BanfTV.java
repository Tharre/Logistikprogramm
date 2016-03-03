package org.htl_hl.Logistikprogramm;

import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import java.sql.Timestamp;

import static sql.generated.logistik_test.Tables.*;


public class BanfTV extends AbstractTV {

	private static final String[] columnNames =
			{"ID", "Antragsteller", "Kostenstelle", "Erstelldatum", "Wunschdatum", "Kommentar", "Status"};
	private static final String[] propertyNames =
			{"id", "antragsteller", "kostenstelle", "erstelldatum", "wunschdatum", "kommentar", "status"};

	private Reference antragsteller;
	private Reference kostenstelle;
	private Timestamp erstelldatum;
	private Timestamp wunschdatum;
	private String kommentar;
	private String status;

	public BanfTV(int id) {
		super(id, "bnf01");
	}

	public BanfTV(int id, int antragstellerId, String antragstellerBezeichnung, int kostenstelleId,
	              String kostenstelleBezeichnung, Timestamp erstelldatum, Timestamp wunschdatum, String kommentar,
	              String status) {
		super(id, "bnf01");
		this.antragsteller = new Reference("usr01", antragstellerId, antragstellerBezeichnung);
		this.kostenstelle = new Reference("kos01", kostenstelleId, kostenstelleBezeichnung);
		this.erstelldatum = erstelldatum;
		this.wunschdatum = wunschdatum;
		this.kommentar = kommentar;
		this.status = status;
	}

	public Reference getAntragsteller() {
		return antragsteller;
	}

	public Reference getKostenstelle() {
		return kostenstelle;
	}

	public Timestamp getErstelldatum() {
		return erstelldatum;
	}

	public Timestamp getWunschdatum() {
		return wunschdatum;
	}

	public String getKommentar() {
		return kommentar;
	}

	public String getStatus() {
		return status;
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
	public ResultQuery<?> getQuery() {
		return DSL.select(BANF.ID, USER.ID, USER.CN, KOSTENSTELLE.ID, KOSTENSTELLE.BEZEICHNUNG, BANF.ERSTELLDATUM,
		                  BANF.WUNSCHDATUM, BANF.KOMMENTAR, DSL.when(POSITION.STATUS.equal(1), "nicht bestellt")
		                                                       .when(POSITION.STATUS.equal(2), "abgewiesen")
		                                                       .when(POSITION.STATUS.greaterThan(1), "fertig"))
		          .from(BANF)
		          .leftOuterJoin(USER)
		          .on(BANF.ANTRAGSTELLER_ID.equal(USER.ID))
		          .leftOuterJoin(KOSTENSTELLE)
		          .on(BANF.KOSTENSTELLE_ID.equal(KOSTENSTELLE.ID))
		          .leftOuterJoin(BANFPOSITION)
		          .on(BANF.ID.equal(BANFPOSITION.BANF_ID))
		          .leftOuterJoin(POSITION)
		          .on(BANFPOSITION.POSITION_ID.equal(POSITION.ID))
		          .groupBy(BANF.ID)
		          .getQuery();
	}
}
