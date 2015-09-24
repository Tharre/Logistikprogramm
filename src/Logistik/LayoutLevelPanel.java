package Logistik;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 * Klasse zum Anzeigen von hierarchisch angeordneten Daten
 */

public class LayoutLevelPanel extends JScrollPane {
	public ArrayList<JButton> btns;
	public int lastLevel = 100;
	public ArrayList<JPanel> comps;
	public LayoutLevel lvll;
	public JPanel pa;

	public LayoutLevelPanel() {
		pa = new JPanel();
		lvll = new LayoutLevel();
		btns = new ArrayList();
		comps = new ArrayList();
		pa.setLayout(lvll);
		setViewportView(pa);
	}

	public void add(JComponent c, int lvl) {
		JButton btn = createButton("+");
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		if (lvl > lastLevel) {
			JButton b = ((btns.get(btns.size() - 1)));
			b.setEnabled(true);
		}
		btn.setEnabled(false);
		p.add(btn);
		p.add(c);
		p.setBorder(null);
		// p.setBackground(getBackground(lvl));
		final int i = comps.size() + 1;
		final int l = lvl + 1;
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnKlicked(i, l, (JButton) e.getSource());
			}
		});
		btns.add(btn);
		comps.add(p);
		p.setOpaque(false);
		pa.add(p, "" + lvl);
		lastLevel = lvl;
		if (lvl == 0) {
			lvll.setVisible(comps.size() - 1, true);
		}
	}

	public void addVisible(JComponent c, int lvl, boolean v) {
		String z = "+";
		if (v) {
			z = "-";
		}
		JButton btn = createButton(z);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		if (lvl > lastLevel) {
			JButton b = ((btns.get(btns.size() - 1)));
			b.setEnabled(true);
		}
		btn.setEnabled(false);
		p.add(btn);
		p.add(c);
		p.setBorder(null);
		// p.setBackground(getBackground(lvl));
		final int i = comps.size() + 1;
		final int l = lvl + 1;
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnKlicked(i, l, (JButton) e.getSource());
			}
		});
		btns.add(btn);
		comps.add(p);
		p.setOpaque(false);
		pa.add(p, "" + lvl);
		lastLevel = lvl;
		lvll.setVisible(comps.size() - 1, v);
	}

	public Color getBackground(int lvl) {
		return new Color(200 + lvl * 10, 200 + lvl * 10, 200 + lvl * 10);
	}

	public void btnKlicked(int z, int l, JButton btn) {
		if (btn.getToolTipText().equals("+")) {
			for (int i = z; lvll.getLvl(i) >= l; i++) {
				if (lvll.getLvl(i) == l) {
					lvll.setVisible(i, true);
				}
				if (i >= comps.size() - 1) {
					break;
				}
			}
			btn.setToolTipText("-");
			btn.setText("-");
		} else {
			for (int i = z; lvll.getLvl(i) >= l; i++) {
				lvll.setVisible(i, false);
				JButton b = btns.get(i);
				b.setToolTipText("+");
				b.setText("+");
				if (i >= comps.size() - 1) {
					break;
				}
			}
			btn.setToolTipText("+");
			btn.setText("+");
		}
		revalidate();
		repaint();
	}

	public JButton createButton(String text) {
		JButton b = new JButton(text);
		b.setToolTipText(text);
		b.setOpaque(false);
		b.setBorder(null);
		return b;
	}
}