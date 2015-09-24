package Logistik;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

public class Kommentar extends JFrame implements ActionListener {
	private JTextArea txtFeld = new JTextArea();
	private JButton speichern = new JButton("Speichern");
	private JButton abbruch = new JButton("Abbrechen");
	private JPanel pButtons = new JPanel();
	private DBConnection con;
	private Object banfID;
	private String kommentarAlt;
	private String kommentar;

	public Kommentar(String name, Object banfID, DBConnection con) {
		super(name);
		this.banfID = banfID;
		this.con = con;

		speichern.addActionListener(this);
		abbruch.addActionListener(this);

		String query1 = "select kommentar from banf where id=" + banfID;
		ResultSet rs1 = con.mysql_query(query1);
		try {
			while (rs1.next()) {
				kommentarAlt = rs1.getString("kommentar");
			}

			rs1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		txtFeld.setSize(350, 200);
		txtFeld.setText(kommentarAlt);
		setLayout(new BorderLayout());
		pButtons.setLayout(new GridLayout(1, 3));
		this.add(txtFeld, BorderLayout.CENTER);
		pButtons.add(new JLabel());
		pButtons.add(speichern);
		pButtons.add(abbruch);
		this.add(pButtons, BorderLayout.SOUTH);
	}
	
	public Kommentar(int bestPosId, DBConnection con)
	{
		super("Kommentar");
		
		String query1 = "select lieferkommentar from bestpos where id=" + bestPosId;
		ResultSet rs1 = con.mysql_query(query1);
		try {
			while (rs1.next()) {
				kommentar = rs1.getString("lieferkommentar");
			}

			rs1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		txtFeld.setSize(350, 200);
		txtFeld.setText(kommentar);
		txtFeld.setEditable(false);
		this.add(txtFeld);
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == speichern) {

			String query = "update banf set kommentar='" + txtFeld.getText()
					+ "' where id=" + banfID;
			con.mysql_update(query);

			dispose();
		} else {
			dispose();
		}

	}

}
