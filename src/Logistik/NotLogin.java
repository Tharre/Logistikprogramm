package Logistik;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class NotLogin extends JFrame implements ActionListener {

	private JButton ok;
	private JButton abbrechen;
	private JTextField user;
	private JTextField pswt;
	private final String passwort = "budget09HL";
	private String userCheck;
	private String pswtCheck;
	private DBConnection con;
	private Login login;

	public NotLogin(String title, Login login) {
		super(title);

		this.login = login;
		con = new DBConnection("logistik_2", "logistik1", "4ahwii");
		setLayout(new GridLayout(3, 1));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();

		p1.setOpaque(false);
		p2.setOpaque(false);
		p3.setOpaque(false);

		user = new JTextField(20);
		user.setBorder(BorderFactory.createTitledBorder("Name"));
		pswt = new JPasswordField(20);
		pswt.setBorder(BorderFactory.createTitledBorder("Passwort"));

		ok = new JButton("Login");
		ok.addActionListener(this);
		abbrechen = new JButton("Abbrechen");
		abbrechen.addActionListener(this);

		p1.add(user);
		p2.add(pswt);
		p3.setLayout(new FlowLayout());

		p3.add(ok);
		p3.add(abbrechen);

		this.add(p1);
		this.add(p2);
		this.add(p3);

		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			userCheck = user.getText();
			pswtCheck = pswt.getText();

			if ((userCheck.equals("Mayer"))
					&& ((pswtCheck.equals("" + passwort)))) {
				final Laden l = new Laden();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new Logistik("Logistik", new UserImport("Mayer", con, ""));
						l.dispose();
					}
				});
				login.beenden();
				dispose();
			} else {
				new MessageError("Login fehlgeschlagen!");
			}
		}
		if (e.getSource() == abbrechen) {
			dispose();
		}

	}

}
