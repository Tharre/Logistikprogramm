package org.htl_hl.Logistikprogramm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Login extends JFrame implements ActionListener, KeyListener {
	int gridX = 9;
	int gridY = 3;
	JLabel[][] platzhalter = new JLabel[gridX][gridY];

	Container c = this;

	JLabel lBn = new JLabel("Benutzername:");
	JLabel lPw = new JLabel("Passwort:");

	JTextField tf = new JTextField();
	JPasswordField pwf = new JPasswordField();

	JButton btLogin = new JButton("Login");

	public Login() {
		super("Login");
		setSize(510, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		c.setLayout(new GridLayout(gridX, gridY));
		ZeileZeichnen(0);
		elementZeichnen(1, 0);
		c.add(lBn);
		elementZeichnen(1, 2);
		elementZeichnen(2, 0);
		c.add(tf);
		elementZeichnen(2, 2);
		ZeileZeichnen(3);
		elementZeichnen(4, 0);
		c.add(lPw);
		elementZeichnen(4, 2);
		elementZeichnen(5, 0);
		c.add(pwf);
		elementZeichnen(5, 2);
		ZeileZeichnen(6);
		elementZeichnen(7, 0);
		c.add(btLogin);
		elementZeichnen(7, 2);
		ZeileZeichnen(8);

		btLogin.addActionListener(this);
		btLogin.addKeyListener(this);
		tf.addKeyListener(this);
		pwf.addKeyListener(this);
	}

	public static void main(String[] args) {
		new Login();
	}

	private void ZeileZeichnen(int x) {
		for (int j = 0; j < gridY; j++) {
			elementZeichnen(x, j);
		}
	}

	private void elementZeichnen(int x, int y) {
		platzhalter[x][y] = new JLabel();
		c.add(platzhalter[x][y]);
	}

	// TODO: implement
	private boolean benutzernameVorhanden() {
		String benutzername = tf.getText();
		return true;
	}

	// TODO: implement
	private boolean passwortKorrekt() {
		char[] passwort = pwf.getPassword();
		return false;
	}

	private void anmelden() {
		if (benutzernameVorhanden()) {
			if (passwortKorrekt()) {
				// TODO: anmelden
			} else {
				Frame f = new Frame();
				JOptionPane.showMessageDialog(f, "Passwort falsch!");
			}
		} else {
			Frame f = new Frame();
			JOptionPane.showMessageDialog(f, "Benutzername nicht bekannt!");
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			anmelden();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		anmelden();
	}

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }
}