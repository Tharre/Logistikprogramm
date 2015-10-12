package Logistik;

import javax.swing.*;

import java.awt.*;
import java.net.*;

/**
 * Willkommensseite / Startbildschirm
 */

public class Welcome extends LayoutMainPanel {

	public Welcome(UserImport user) {
		super(user);
		setLayoutM(new GridLayout());
		addM(new BildPanel());

	}

	public class BildPanel extends JPanel {
		private Image img;

		public BildPanel() {
			URL url = this.getClass().getResource("../blüte.jpg");
			img = new ImageIcon(url).getImage();
			JLabel l = new JLabel("Hallo " + user.getName() + "!",
					SwingConstants.LEFT);
			l.setFont(new Font("Sans Serif", Font.CENTER_BASELINE, 30));
			l.setOpaque(false);
			setLayout(new GridLayout(1, 1));
			add(l);
		}

		@Override
		public void paintComponent(Graphics g) {
			if (img != null) {
				g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
			}
		}
	}
}