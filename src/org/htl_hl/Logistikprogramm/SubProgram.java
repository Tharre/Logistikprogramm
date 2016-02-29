package org.htl_hl.Logistikprogramm;

import javax.swing.JPanel;

public class SubProgram {

    private String identifier;
    private View view;

    public SubProgram(String identifier, View view) {
        this.identifier = identifier;
        this.view = view;
    }

    public String getIdentifier() {
        return identifier;
    }

    public JPanel createJPanel(String[] args) {
        return view.createJPanel(args);
    }

    public String toString() {
        return identifier;
    }
}
