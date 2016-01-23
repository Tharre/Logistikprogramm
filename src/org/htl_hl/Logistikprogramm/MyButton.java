package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;


public class MyButton extends JButton {
	private Shape shape;

	public MyButton(String label) {
		super(label);
		setContentAreaFilled(false);
	}

	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}

}