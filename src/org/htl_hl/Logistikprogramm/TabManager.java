package org.htl_hl.Logistikprogramm;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;
import org.jooq.DSLContext;
import sql.generated.logistik_test.tables.Staat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


public class TabManager {

	private Color colors[] =
			{new Color(240, 40, 40), new Color(240, 140, 10), new Color(195, 195, 5), new Color(20, 195, 35),
			 new Color(60, 60, 230), new Color(160, 25, 170)};
	private final JTabbedPane tabbedPane;

	private Map<String, SubProgram> knownApplications = new HashMap<>();

	public <E extends Viewable> SubProgram helper(LConnection server, String identifier, E obj,
	                                                     Class<E> typeClass) {
		DSLContext ctx = server.create;
		TextFilterator<E> filterator = GlazedLists.textFilterator(obj.getPropertyNames());
		AdvancedTableFormat<E> tf = new BeanTableFormat<>(typeClass, obj.getPropertyNames(), obj.getColumnNames());
		TableView<E> tv = new TableView<>(server, obj.getQuery(ctx), typeClass, filterator, tf, this);
		return new SubProgram(identifier, tv);
	}

	public TabManager(LConnection server) {
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setUI(new MyTabbedPaneUI());

		// built-in subprograms
		// TODO: we are passing -1 here because if the constructor is empty JOOQ uses them instead of the real one
		knownApplications.put("mat01", helper(server, "Material anzeigen", new MatTV(-1), MatTV.class));
		knownApplications.put("inv01", helper(server, "Inventurgruppe anzeigen", new InvGrpTV(-1), InvGrpTV.class));
		knownApplications.put("sta01", helper(server, "Staat anzeigen", new StaatTV(-1), StaatTV.class));
		knownApplications.put("fma01", helper(server, "Firma anzeigen", new FirmaTV(-1), FirmaTV.class));

		// external subprograms
		// load all sorts of external subprograms here as well if you like ...
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public Map<String, SubProgram> getKnownApplications() {
		return knownApplications;
	}

	public void addTab(String shortcut, String[] args) {
		SubProgram s = knownApplications.get(shortcut);
		add(s.getIdentifier(), s.createJPanel(args));
	}

	public void addTab(SubProgram s) {
		add(s.getIdentifier(), s.createJPanel(null));
	}

	public void add(String label, JPanel panel) {
		int count = tabbedPane.getTabCount() % colors.length;
		int tabCount = tabbedPane.getTabCount() - 1;

		for (int i = 0; i <= tabCount; i++) {
			if (tabbedPane.getComponentAt(i).getName().equals(label))
				return;
		}

		tabbedPane.addTab(label, panel);

		tabCount = tabbedPane.getTabCount() - 1;
		tabbedPane.getComponentAt(tabCount).setName(label);

		final JPanel pnlTab = new JPanel(new BorderLayout());
		pnlTab.setOpaque(false);
		JLabel lbllabel = new JLabel(label);
		lbllabel.setForeground(colors[count]);
		MyButton btnClose = new MyButton("x");

		pnlTab.add(lbllabel, BorderLayout.WEST);

		btnClose.setBorder(null);
		btnClose.setFont(new Font("Arial", Font.BOLD, 14));
		btnClose.setPreferredSize(new Dimension(34, 16));
		Container c = new Container();
		c.setLayout(new GridLayout(1, 1));
		c.add(btnClose);
		pnlTab.add(c, BorderLayout.CENTER);

		tabbedPane.setTabComponentAt(tabCount, pnlTab);

		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				int i = tabbedPane.indexOfTabComponent(pnlTab);
				if (i != -1)
					tabbedPane.remove(i);
			}
		});

		tabCount = tabbedPane.getTabCount() - 1;
		tabbedPane.setSelectedIndex(tabCount);
	}
}
