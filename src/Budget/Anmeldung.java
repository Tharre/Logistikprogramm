package Budget;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

/**
 *In der Klasse Anmeldung wird die Anmeldungsmaske, mit welcher Datenbank man
 * sich verbinden moechte, erzeugt.
 *<p>
 * Title: Anmeldung
 * 
 * @author Haupt Marianne, Liebhart Stefanie
 **/
public class Anmeldung extends JFrame implements ActionListener, KeyListener {

	/** Button, wenn man mit der aktuellen Datenbank arbeiten moechte **/
	private JRadioButton aktuell = new JRadioButton("aktuelles Jahr");
	/** Button, wenn man mit der Datenbank des Vorjahres arbeiten moechte **/
	private JRadioButton alt = new JRadioButton("vergangenes Jahr");
	/** Button, um das Programm zu starten */
	private JButton start = new JButton("Programm starten");
	/** ButtonGroup fuer die Buttons aktuell und alt **/
	private ButtonGroup bg = new ButtonGroup();
	/** Panel fuer die Komponenten **/
	private JPanel panel = new JPanel();
	private JFrame f;
	/** Ladefenster "Bitte warten..." **/
	Loading load;
	
	/**
	 * Konstruktor
	 */
	public Anmeldung() {

		super("Anmeldung");
		setLayout(new BorderLayout());

		aktuell.setSelected(true);

		bg.add(aktuell);
		bg.add(alt);

		add(new JLabel("Mit welchen Daten möchten Sie arbeiten?"),
				BorderLayout.NORTH);

		panel.setLayout(new GridLayout());

		panel.add(aktuell);
		panel.add(alt);

		add(panel, BorderLayout.CENTER);
		add(start, BorderLayout.SOUTH);

		start.addActionListener(this);
		start.addKeyListener(this);
		aktuell.addKeyListener(this);
		alt.addKeyListener(this);
	}

	/**
	 * ActionPerformed
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {

			load = new Loading();
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					URL u = this.getClass().getResource("../1cent.jpg");
					Image image = new ImageIcon(u).getImage();

					if (alt.isSelected()) {

						f = new Budget(false, "Budgetprogramm Vorjahr");
						f.setIconImage(image);
						f.setVisible(true);
						load.dispose();

					} else {
						f = new Budget(true, "Budgetprogramm aktuelles Jahr");
						f.setIconImage(image);
						f.setVisible(true);
						load.dispose();
						

					}
				}
			});

		}
		dispose();
	}

	//Was macht diese Methode?
	 public void keyPressed(KeyEvent k) {

		if (k.getKeyCode() == 10) {
			URL u = this.getClass().getResource("../1cent.jpg");
			Image image = new ImageIcon(u).getImage();

			if (alt.isSelected()) {
				f = new Budget(false, "Budgetprogramm Vorjahr");
				f.setIconImage(image);
				f.setVisible(true);
			} else {
				f = new Budget(true, "Budgetprogramm aktuelles Jahr");
				f.setIconImage(image);
				f.setVisible(true);
			}
			dispose();
		}
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void schliesse() {
		f.dispose();
	}

}
