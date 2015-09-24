package Logistik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
 * Panel zum Anzeigen eine Leiste mit Buttons
 */

public class LayoutLinkPanel extends JPanel implements ActionListener, Runnable {
	private Image bg;
	private Logistik index;
	private Thread runner;
	private Image buffer;

	private Image[] heads;

	public LayoutLinkPanel(Logistik index, String image, int height) {
		this.index = index;
		URL u = this.getClass().getResource("" + image);
		bg = new ImageIcon(u).getImage();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(1, height));

		u = this.getClass().getResource("jakob.gif");
		heads = new Image[1];
		heads[0] = new ImageIcon(u).getImage();

	}

	public void addButton(String imagePath, String info) {
		LayoutNaviButton btn = LayoutButtonCreator
				.createButton(imagePath, info);
		btn.addActionListener(this);
		add(btn);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	}

	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		index.setMenu(btn.getToolTipText());
	}

	public void run() {
		int sleepTime = 50;
		long told = System.currentTimeMillis();
		while (runner != null) {
			try {
				Thread.sleep(sleepTime);
			} catch (Exception e) {
			}
			if (getWidth() > 0) {
				if (buffer == null || buffer.getWidth(null) != getWidth()
						|| buffer.getHeight(null) != getHeight()) {
					buffer = createImage(getWidth(), getHeight());

				}
				told = System.currentTimeMillis();
				buffer.getGraphics();

				
			}
		}
	}
}