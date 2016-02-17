package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static sql.generated.logistik_test.Tables.*;

public class MatTV implements Viewable, Comparable<MatTV> {

    private static final String[] columnNames =
            {"ID", "Bezeichnung", "Erstelldatum", "Nr", "Inventurgruppe", "Ersteller", "Gefahrstufe"};

    private final int id;
    private final String bezeichnung;
    private final Timestamp erstelldatum;
    private final int nr;
    private final InvGrpTV inventurgruppe;
    private final String ersteller;
    private final int gefahrstufe;

    public MatTV(int id, String bezeichnung, Timestamp erstelldatum, int nr, int inventurgruppeId,
                 String inventurgruppeBezeichnung, String ersteller, int gefahrstufe) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.erstelldatum = erstelldatum;
        this.nr = nr;
        this.inventurgruppe = new InvGrpTV(inventurgruppeId, inventurgruppeBezeichnung);
        this.ersteller = ersteller;
        this.gefahrstufe = gefahrstufe;
    }

    public int getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public Timestamp getErstelldatum() {
        return erstelldatum;
    }

    public int getNr() {
        return nr;
    }

    public InvGrpTV getInventurgruppe() {
        return inventurgruppe;
    }

    public String getErsteller() {
        return ersteller;
    }

    public int getGefahrstufe() {
        return gefahrstufe;
    }

    @Override
    public int compareTo(MatTV o) {
        return Integer.compare(this.id, o.id);
    }

    public static TableFormat<MatTV> getTableFormat() {
        return new TableFormat<MatTV>() {
            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public Object getColumnValue(MatTV baseObject, int column) {
                switch (column) {
                    case 0: return baseObject.getId();
                    case 1: return baseObject.getBezeichnung();
                    case 2: return baseObject.getErstelldatum();
                    case 3: return baseObject.getNr();
                    //case 4: return baseObject.getInventurgruppe().getBezeichnung();
                    case 4: return baseObject.getInventurgruppe();
                    case 5: return baseObject.getErsteller();
                    case 6: return baseObject.getGefahrstufe();
                    default: throw new IllegalStateException();
                }
            }
        };
    }

    public static TextFilterator<MatTV> getTextFilterator() {
        return new TextFilterator<MatTV>() {
            @Override
            public void getFilterStrings(List<String> baseList, MatTV element) {
                baseList.add(String.valueOf(element.getId()));
                baseList.add(element.getBezeichnung());
                baseList.add(String.valueOf(element.getErstelldatum()));
                baseList.add(String.valueOf(element.getNr()));
                baseList.add(element.getInventurgruppe().getBezeichnung());
                baseList.add(String.valueOf(element.getErsteller()));
                baseList.add(String.valueOf(element.getGefahrstufe()));
            }
        };
    }

    @Override
    public String getViewShortcut() {
        return "ma01";
    }

    @Override
    public String[] getViewArgs() {
        return new String[] {String.valueOf(id)};
    }
}
