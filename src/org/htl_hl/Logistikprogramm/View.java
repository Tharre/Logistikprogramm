package org.htl_hl.Logistikprogramm;

import javax.swing.JPanel;

/**
 * This is a small wrapper interface with the purpose of delaying the creation of JPanels until they're needed.
 */
public interface View {

    JPanel createJPanel(String[] args);

}
