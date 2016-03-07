package org.htl_hl.Logistikprogramm.TableViews;

import org.htl_hl.Logistikprogramm.Reference;
import org.htl_hl.Logistikprogramm.TableViews.AbstractTV;
import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

import static sql.generated.logistik_test.Tables.*;


public class PositionTV extends AbstractTV {

	private static final String[] columnNames =
			{"ID", "Material", "Menge", "Einheit", "Preis", "MwSt", "Status", "Kommentar"};
	private static final String[] propertyNames =
			{"id", "material", "menge", "einheit", "preis", "mwst", "status", "kommentar"};

	private Reference material;
	private double menge;
	private Reference einheit;
	private double preis;
	private int mwst;
	private Reference status;
	private String kommentar;

	public PositionTV(int id) {
		super(id, "pos01");
	}

	public PositionTV(int id, int materialId, String materialBezeichnung, double menge, int einheitId,
	                  String einheitBezeichnung, double preis, int mwst, int statusId, String statusBezeichnung,
	                  String kommentar) {
		super(id, "pos01");
		this.material = new Reference("mat01", materialId, materialBezeichnung);
		this.menge = menge;
		this.einheit = new Reference("ein01", einheitId, einheitBezeichnung);
		this.preis = preis;
		this.mwst = mwst;
		this.status = new Reference("bbs01", statusId, statusBezeichnung);
		this.kommentar = kommentar;
	}

	public Reference getMaterial() {
		return material;
	}

	public double getMenge() {
		return menge;
	}

	public Reference getEinheit() {
		return einheit;
	}

	public double getPreis() {
		return preis;
	}

	public int getMwst() {
		return mwst;
	}

	public Reference getStatus() {
		return status;
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
		return DSL.select(POSITION.ID, MATERIAL.ID, MATERIAL.BEZEICHNUNG, POSITION.MENGE, EINHEIT.ID,
		                  EINHEIT.BEZEICHNUNG, POSITION.PREIS, POSITION.MWST, BESTBANFSTATUS.ID,
		                  BESTBANFSTATUS.BEZEICHNUNG, POSITION.KOMMENTAR)
		          .from(POSITION)
		          .leftOuterJoin(MATERIAL)
		          .on(POSITION.MATERIAL_ID.equal(MATERIAL.ID))
		          .leftOuterJoin(EINHEIT)
		          .on(POSITION.EINHEIT_ID.equal(EINHEIT.ID))
		          .leftOuterJoin(BESTBANFSTATUS)
		          .on(POSITION.STATUS.equal(BESTBANFSTATUS.ID))
		          .getQuery();
	}
}
