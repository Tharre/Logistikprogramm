package Logistik;

import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Button zum Navigieren zwischen den verschiedenen Menus
 */

public class LayoutNaviButton extends JButton implements MouseListener {
	private Image img;
	private boolean over = false;
	private boolean klick = false;

	public LayoutNaviButton(String path, String info) {
		// System.gc();
		// System.out.println("NaviButton");

		URL url = this.getClass().getResource("" + path);
		setIcon(new ImageIcon(url));
		img = new ImageIcon(url).getImage();
		setToolTipText(info);
		setBorder(null);
		setOpaque(false);
		addMouseListener(this);

		// setSize(img.getWidth(null), img.getHeight(null));
	}

	/*
	 * public void paintComponent(Graphics g) {
	 * System.out.println("AYYYYYYYYYY1"); g.drawImage(img, 0,0,this);
	 * System.out.println("AYY"); }
	 */

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		if (isEnabled()) {
			mouseOver();
		}
	}

	public void mouseExited(MouseEvent e) {
		normal();
	}

	public void mousePressed(MouseEvent e) {
		if (isEnabled()) {
			klick();
		}
	}

	public void mouseReleased(MouseEvent e) {

		unklick();
	}

	public void mouseOver() {
		over = true;
		repaint();
	}

	public void normal() {
		over = false;
		repaint();
	}

	public void klick() {
		klick = true;
		repaint();
	}

	public void unklick() {
		klick = false;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
		if (over && !klick) {
			Graphics2D g2d = (Graphics2D) g;
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.65f);
			g2d.setComposite(alphaComp);
			g.setColor(Color.WHITE);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		if (klick) {
			Graphics2D g2d = (Graphics2D) g;
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.65f);
			g2d.setComposite(alphaComp);
			g.setColor(new Color(100, 100, 100));
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
	}

}