package org.htl_hl.Logistikprogramm;

import javax.swing.*;

/**
 * Created by Herbert on 05.04.2016.
 */
public class AbfrageButton extends JButton {
    private String abfrage;

    public AbfrageButton(String abfrage, String anzeigeText) {
        super(anzeigeText);
        this.abfrage = abfrage;
        this.setFocusable(true);
    }

    public String getAbfrage(){
        return abfrage;
    }
}