package Logistik;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

/**
 * Fenster zum Anzeigen von Fehlernachrichten
 */

public class MessageError extends JFrame implements ActionListener {
	private JButton ok;

	public MessageError(String msg) {
		super("Fehler");
		Container c = getContentPane();
		c.setLayout(new GridLayout(2, 0));
		c.add(new JLabel(msg, SwingConstants.CENTER));
		ok = new JButton("ok");
		JPanel k = new JPanel();
		k.add(ok);
		c.add(k);
		ok.addActionListener(this);
		setSize(300, 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			dispose();
		}
	}
}