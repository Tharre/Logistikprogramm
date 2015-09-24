package Logistik;

import javax.swing.*;
import java.awt.*;
import java.net.*;

/**
 * Panel welches die wichtigsten Methoden und das Layout des Menus zur Verfügung
 * stellt
 */

public class LayoutMainPanel extends JPanel {
	private Image ul;
	private Image u;
	private Image ur;
	private Image l;
	private Image r;
	private Image dl;
	private Image d;
	private Image dr;
	private JPanel up;
	private JPanel left;
	private JPanel down;
	private JPanel right;
	protected JScrollPane center;
	private JPanel pCenter;
	protected DBConnection con;
	protected DBConnection conB;
	protected UserImport user;

	public LayoutMainPanel(UserImport user) {
		this.user = user;
		con = user.getConnection();
		conB = user.getConnectionKst();
		setLayout(new BorderLayout());
		URL url = this.getClass().getResource("u.gif");
		u = new ImageIcon(url).getImage();
		url = this.getClass().getResource("d.gif");
		d = new ImageIcon(url).getImage();
		url = this.getClass().getResource("l.gif");
		l = new ImageIcon(url).getImage();
		url = this.getClass().getResource("r.gif");
		r = new ImageIcon(url).getImage();

		url = this.getClass().getResource("ul.gif");
		ul = new ImageIcon(url).getImage();
		url = this.getClass().getResource("ur.gif");
		ur = new ImageIcon(url).getImage();
		url = this.getClass().getResource("dl.gif");
		dl = new ImageIcon(url).getImage();
		url = this.getClass().getResource("dr.gif");
		dr = new ImageIcon(url).getImage();

		setBackground(new Color(200, 200, 200));

		up = new JPanel();
		up.setPreferredSize(new Dimension(1, 15));
		up.setOpaque(false);
		left = new JPanel();
		left.setPreferredSize(new Dimension(15, 1));
		left.setOpaque(false);
		down = new JPanel();
		down.setPreferredSize(new Dimension(1, 15));
		down.setOpaque(false);
		right = new JPanel();
		right.setPreferredSize(new Dimension(15, 1));
		right.setOpaque(false);
		pCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));
		center = new JScrollPane(pCenter);
		center.setSize(100, 100);
		add(up, BorderLayout.NORTH);
		add(left, BorderLayout.WEST);
		add(down, BorderLayout.SOUTH);
		add(right, BorderLayout.EAST);
		add(center, BorderLayout.CENTER);
	}

	@Override
	public void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(u, 0, 0, getWidth(), u.getHeight(this), this);
			g.drawImage(d, 0, getHeight() - d.getHeight(this), getWidth(),
					getHeight(), this);
			g.drawImage(l, 0, 0, l.getWidth(this), getHeight(), this);
			g.drawImage(r, getWidth() - r.getWidth(this), 0, getWidth(),
					getHeight(), this);
			g.drawImage(ul, 0, 0, this);
			g.drawImage(ur, getWidth() - ur.getWidth(this), 0, this);
			g.drawImage(dl, 0, getHeight() - dl.getHeight(this), this);
			g.drawImage(dr, getWidth() - dr.getWidth(this), getHeight()
					- dl.getHeight(this), this);
		}
		pCenter.setPreferredSize(new Dimension(getWidth() - 40, pCenter
				.getPreferredSize().height));
		center.getViewport().setSize(
				new Dimension(getWidth() - 30, pCenter.getHeight()));
		// revalidate();
		repaint2(g);
	}

	public void repaint2(Graphics g) {
	}

	public void addM(Component c) {
		pCenter.add(c);
	}

	public void addM(Component c, String align) {
		pCenter.add(c, align);
	}

	public void removeMall() {
		pCenter.removeAll();
	}

	public void removeM(Component c) {
		pCenter.remove(c);
	}

	public void setLayoutM(LayoutManager mgr) {
		pCenter.setLayout(mgr);
	}

	public void activate() {
		repaint();
	}

	public void deactivate() {
	}
}