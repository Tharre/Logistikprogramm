package org.htl_hl.Logistikprogramm;


import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.sql.Timestamp;

import static sql.generated.logistik_test.Tables.*;

public class FirmaTV extends AbstractTV {

    private static final String[] columnNames = {"ID", "Bezeichnung", "Erstelldatum", "Staat", "Ort", "PLZ", "Straße",
            "E-Mail", "Kunden Nr.", "Sachbearbeiter", "Telefon", "Fax", "Homepage", "Konditionen", "Kreditoren Nr.",
            "UMS", "ARA", "Kommentar"};

    private final int id;
    private final String bezeichnung;
    private final Timestamp erstelldatum;
    private final StaatTV staat;
    private final String ort;
    private final String plz;
    private final String strasse;
    private final String mail;
    private final String kundennr;
    private final String sachbearbeiter;
    private final String telefon;
    private final String fax;
    private final String homepage;
    private final String konditionen;
    private final String kreditorennr;
    private final String umsNr;
    private final String araNr;
    private final String kommentar;

    public FirmaTV(int id, String bezeichnung, Timestamp erstelldatum, int staatId, String staatBezeichnung, String ort,
                   String plz, String strasse, String mail, String kundennr, String sachbearbeiter, String telefon,
                   String fax, String homepage, String konditionen, String kreditorennr, String umsNr, String araNr,
                   String kommentar) {
        super(id, columnNames, "fma01");

        this.id = id;
        this.bezeichnung = bezeichnung;
        this.erstelldatum = erstelldatum;
        this.staat = new StaatTV(staatId, staatBezeichnung);
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

    @Override
    public Object getValue(int column) {
        switch (column) {
            case 1: return id;
            case 2: return bezeichnung;
            case 3: return erstelldatum;
            case 4: return staat;
            case 5: return ort;
            case 6: return plz;
            case 7: return strasse;
            case 8: return mail;
            case 9: return kundennr;
            case 10: return sachbearbeiter;
            case 11: return telefon;
            case 12: return fax;
            case 13: return homepage;
            case 14: return konditionen;
            case 15: return kreditorennr;
            case 16: return umsNr;
            case 17: return araNr;
            case 18: return kommentar;
            default: throw new IllegalStateException();
        }
    }

    public static TableFormat<FirmaTV> getTableFormat() {
        return new ViewTableFormat<>(columnNames);
    }

    public static TextFilterator<FirmaTV> getTextFilterator() {
        return new ViewTextFilterator<>();
    }

    public static Query getQuery(DSLContext ctx) {
        return ctx.select(INVENTURGRUPPE.ID, INVENTURGRUPPE.BEZEICHNUNG)
                .from(INVENTURGRUPPE)
                .getQuery();
    }
}
