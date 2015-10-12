package Logistik;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.sql.*;

/**
 * Fenster zum einloggen des Users in das Logistikprogramm
 */

public class Login extends JFrame implements ActionListener, KeyListener {

	private JTextField name;
	private JPasswordField passwort;
	private JButton login;
	private JButton notlogin;
	private DBConnection con;
	private String st;

	public Login(String title, String st) {
		super(title);
		this.st = st;
		setSize(400, 200);
		LoginPanel p = new LoginPanel();
		con = new DBConnection("logistik_2", "root", "test");
		p.setLayout(new GridLayout(3, 1));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		p1.setOpaque(false);
		p2.setOpaque(false);
		p3.setOpaque(false);
		p4.setOpaque(false);
		p5.setOpaque(false);
		name = new JTextField(20);

		name.setBorder(BorderFactory.createTitledBorder("Name"));
		passwort = new JPasswordField(20);
		passwort.setBorder(BorderFactory.createTitledBorder("Passwort"));

		/*
		 * URL u2= this.getClass().getResource("../not-login.gif"); icon= new
		 * ImageIcon(u2); Dimension d = new Dimension(icon.getIconWidth(),
		 * icon.getIconHeight());
		 */

		login = new JButton("Login");
		notlogin = new JButton("Not-Login");
		/*
		 * notlogin.setPreferredSize(d); notlogin.setIcon(icon);
		 */

		p3.setLayout(new BorderLayout());
		p4.setLayout(new GridLayout(1, 4));
		p5.setLayout(new GridLayout(1, 5));

		name.setOpaque(false);
		passwort.setOpaque(false);

		p5.add(new JLabel());
		p5.add(new JLabel());
		p5.add(login);
		p5.add(new JLabel());
		p5.add(new JLabel());

		p4.add(new JLabel());
		p4.add(new JLabel());
		p4.add(new JLabel());
		p4.add(notlogin);

		p1.add(name);
		p2.add(passwort);
		p3.add(p5, BorderLayout.NORTH);
		p3.add(p4, BorderLayout.SOUTH);
		p.add(p1);
		p.add(p2);
		p.add(p3);
		add(p);
		setVisible(true);
		login.addActionListener(this);
		login.addKeyListener(this);
		notlogin.addActionListener(this);
		InputMap inmap = login.getInputMap();

		KeyStroke enterPressed = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0,
				false);
		KeyStroke enterReleased = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0,
				true);

		inmap.put(enterPressed, "pressed");
		inmap.put(enterReleased, "released");

		login.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inmap);
	}

	// public static void main(String[] args)
	// {
	// new Login("login",null);
	// }

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {

			if (IMAP.login(name.getText(), passwort.getText())
					&& login(name.getText())) {
System.out.println("1");
				final Laden l = new Laden();

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new Logistik("Logistik", new UserImport(name.getText(),
								con, st));
						l.dispose();

					}
				});
				dispose();
			} else {
				new MessageError("Login fehlgeschlagen!");
			}
		}
		if (e.getSource() == notlogin) {
			JFrame f = new NotLogin("Not-Login", this);
			f.pack();
			f.setVisible(true);
		}
	}

	private class LoginPanel extends JPanel {
		private Image bg;

		public LoginPanel() {
			URL u = this.getClass().getResource("../Login_bg.gif");
			bg = new ImageIcon(u).getImage();
		}

		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
		}
	}

	private boolean login(String l) {
		String qry = "SELECT id FROM ldap_user WHERE cn LIKE '" + l + "';";
		boolean jaNein;
		ResultSet rs = con.mysql_query(qry);
		try {
			if (rs.next()) {
				jaNein = true;
			} else {
				jaNein = false;
			}

			rs.close();

			return jaNein;
		} catch (Exception e) {
			return false;
		}
	}

	public void beenden() {
		dispose();
	}

	public void keyPressed(KeyEvent k) {
		if (k.getKeyCode() == 10) {
			if (IMAP.login(name.getText(), passwort.getText())
					&& login(name.getText())) {
				final Laden l = new Laden();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new Logistik("Logistik", new UserImport(name.getText(),
								con, st));
						l.dispose();
					}
				});
				dispose();
			} else {
				new MessageError("Login fehlgeschlagen!");
			}
		}

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}