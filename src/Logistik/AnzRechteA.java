package Logistik;

import java.awt.*;
import javax.swing.*;

import java.sql.*;

public class AnzRechteA extends JFrame {
	public LayoutRechteTable table;

	public AnzRechteA(String[] sn, Class[] cl, String[] rechte) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(600, 500);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		table = new LayoutRechteTable(null, sn, cl, null, rechte, 0);

		JScrollPane scrollpane = new JScrollPane(table);
		c.add(scrollpane, BorderLayout.CENTER);
		validate();
		this.repaint();
	}

	public AnzRechteA(String[] sn, Class[] cl, String[] rechte,
			ResultSet rs, String[] spalten, int z) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(600, 500);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		table = new LayoutRechteTable(spalten, sn, cl, rs, rechte, z);

		JScrollPane scrollpane = new JScrollPane(table);
		c.add(scrollpane, BorderLayout.CENTER);
		validate();
		this.repaint();
	}

	public AnzRechteA(String[] sn, String[] s, Class[] cl, ResultSet rs,
			String[] rechte) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
		this.setSize(600, 500);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		table = new LayoutRechteTable(s, sn, cl, rs, rechte, -1);

		JScrollPane scrollpane = new JScrollPane(table);
		c.add(scrollpane, BorderLayout.CENTER);
		validate();
		this.repaint();
	}
}