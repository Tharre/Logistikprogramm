package Logistik;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;

/**
 * Panel für die Anzeige des angemeldeten Benutzers und Anmeldezeitpunkt
 */

public class LayoutBottomPanel extends JPanel implements ActionListener {
	private Image bg;
	private ImageIcon bg2, bg3;
	private JLabel name;
	private JButton abmelden, ldap;
	private Logistik logistikFrame;
	private JPanel osten;
	// private DBConnection con = new
	// DBConnection("logistik_2","logistik1","4ahwii");
	private DBConnection con;

	public LayoutBottomPanel(String image, int height, UserImport user,
			Logistik logistikFrame) {
		this.logistikFrame = logistikFrame;

		con = user.getConnection();

		URL u = this.getClass().getResource("" + image);
		bg = new ImageIcon(u).getImage();
		setPreferredSize(new Dimension(1, height));
		Font f = new Font("arial", Font.PLAIN, 10);
		name = new JLabel("Eingeloggt als: " + user.getName() + ", seit "
				+ user.getLoginTimeString(), SwingConstants.CENTER);
		name.setFont(f);

		URL u2 = this.getClass().getResource("abmelden3.gif");
		bg2 = new ImageIcon(u2);

		abmelden = new JButton();

		osten = new JPanel();
		Dimension d = new Dimension(bg2.getIconWidth(), bg2.getIconHeight());
		osten.setLayout(new BorderLayout());

		// Rechte für Ldap-Aktualisierung
		String ldapCn = user.getCn();
		if (ldapCn.equals("Mayer")) {
			URL u3 = this.getClass().getResource("ldap.gif");
			bg3 = new ImageIcon(u3);

			ldap = new JButton();
			ldap.setPreferredSize(d);
			ldap.setIcon(bg3);
			ldap.addActionListener(this);

			osten.add(ldap, BorderLayout.CENTER);
		}

		abmelden.setPreferredSize(d);
		abmelden.setIcon(bg2);
		abmelden.addActionListener(this);
		osten.add(abmelden, BorderLayout.EAST);

		setLayout(new BorderLayout());
		add(name, BorderLayout.CENTER);
		add(osten, BorderLayout.EAST);

	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == abmelden) {

			final Laden l = new Laden();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new Login("Login", "");
					l.dispose();
				}
			});

			logistikFrame.schliesse();
			logistikFrame.dispose();

		}
		if (e.getSource() == ldap) {
			try {
				Runtime
						.getRuntime()
						.exec(
								"explorer http://logistik.htl-hl.ac.at/Logistik_2/vJava/java/update.php");

			} catch (Exception ex) {
				ex.getMessage();
			}

		}
	}
}