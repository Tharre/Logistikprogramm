package org.htl_hl.Logistikprogramm;

import javax.swing.JPanel;

public class SubProgram {

    private String identifier;
    private String shortcut;
    private View view;

    public SubProgram(String identifier, String shortcut, View view) {
        this.identifier = identifier;
        this.shortcut = shortcut;
        this.view = view;
    }

    public String getIdentifier() {
        return identifier;
    }

    public JPanel createJPanel(String[] args) {
        return view.createJPanel(args);
    }
}
