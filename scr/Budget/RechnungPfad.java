package Budget;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RechnungPfad extends JFrame implements ActionListener{
	
	
	/** Hinweis zum Pfad**/
	private JLabel hinweis = new JLabel(
			"Bitte geben Sie den Pfad ein. Bsp.: O:/WL/Rechnungen/");
	/** Hinweis auf Slash**/
	private JLabel hinweis2 = new JLabel("Am Ende muss ein Slash [/] stehen!");
	/** Eingabefeld fuer den Pfad **/
	private JTextField eingabefeld = new JTextField();
	/**Button speichern**/
	private JButton speichern = new JButton("OK");
	/**Pfad**/
	private String pfad;

	public RechnungPfad()
	{
		super("Pfad Eingabe");
		speichern.addActionListener(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(4, 1));
		add(hinweis);
		add(hinweis2);
		add(eingabefeld);
		add(speichern);
	}

	public void actionPerformed(ActionEvent arg0) {
		
		pfad = eingabefeld.getText();
		schreibeInTextdatei(pfad);
		JOptionPane
		.showMessageDialog(null,
				"Pfad wurde gespeichert!");
		dispose();
		
	}
	
	public void schreibeInTextdatei(String pfad) {
		Writer r_out;

		try {
			r_out = new FileWriter(
					new File(
							"C:/Projekt_Textdateien/PfadRechnungen.txt"));

			BufferedWriter br_out = new BufferedWriter(r_out);

			r_out.write(pfad);

			r_out.close();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane
			.showMessageDialog(null,
					"Fehler beim Schreiben in die Textdatei!");
		}

	}

}
