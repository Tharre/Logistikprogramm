package org.htl_hl.Logistikprogramm;

import org.jooq.ResultQuery;

/**
 * Created by Herbert on 06.04.2016.
 */
public class ResultWithCn {
    private ResultQuery q;
    private String[] cn;

    public ResultWithCn(ResultQuery resultQuery, String[] collumnNames) {
        q = resultQuery;
        cn = collumnNames;
    }

    public ResultQuery getQuery() {
        return q;
    }

    public String[] getCoullumnNames() {
        return cn;
    }
}
