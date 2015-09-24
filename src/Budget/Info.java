package Budget;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;

/**
 *In der Klasse Info werden die Infos zum Programm und zu den Programmierern angezeigt
 *<p>
 * Title: Info
 * 
 * @author Haupt, Liebhart
 **/
public class Info extends JFrame implements ActionListener{

	//sonstiges
	/**der Button "Bedienungsanleitung"**/
	private JButton btnAnleitung = new JButton("Bedienungsanleitung");
	/**ein BevelBorder-Objekt, fuer den Rahmen**/
	private BevelBorder bevelBorder = new BevelBorder(BevelBorder.RAISED);
	
	//Panels
	/**das Panel, auf dem alle anderen Elemente angeordnet sind**/
	private JPanel pMain = new JPanel();
	/**das Panel, auf dem der Button angeordnet ist**/
	private JPanel pButton = new JPanel();
	/**beinhaltet die Ueberschrift**/
	private JPanel eins = new JPanel();
	/**beihnaltet den Beschreibungstext**/
	private JPanel zwei = new JPanel();
	/**beinhaltet die Labels Impressum, Marianne und Stefanie**/
	private JPanel drei = new JPanel();
	/**beinhaltet die Beschreibungen zu den Programmierern**/
	private JPanel vier = new JPanel();
	/**beinhaltet das Label Impressum**/
	private JPanel pDreiInhalt = new JPanel();
	/**beinhaltet die Labels Marianne und Stefanie**/
	private JPanel pDreiUnten = new JPanel();
	
	//Labels
	/**die Ueberschrift "Budgetprogramm"**/
	private JLabel lUeberschrift = new JLabel("BUDGETPROGRAMM", JLabel.CENTER);
	/**das Label Impressum**/
	private JLabel lImpressum = new JLabel("Impressum",
			JLabel.CENTER);
	/**beinhaltet den Beschreibungstext**/
	private JLabel lBeschreibung;
	/**das Label Stefanie Liebhart**/
	private JLabel lSteffi = new JLabel("Stefanie Liebhart",JLabel.CENTER);
	/**das Label Marianne Haupt**/
	private JLabel lMarianne = new JLabel("Marianne Haupt",JLabel.CENTER);
	/**beinhaltet den Beschreibungstext zu Stefanie**/
	private JLabel lSteffiText;
	/**beinhaltet den Beschreibungstext zu Marianne**/
	private JLabel lMarianneText;

	/** Container **/
	private Container c;

	/**
	 * Konstruktor
	 */
	public Info() {
		super("Information");
		c = getContentPane();
		
		btnAnleitung.addActionListener(this);
		
		pMain.setLayout(new GridLayout(5, 1));
		pMain.setBorder(bevelBorder);
		
		eins.setLayout(new BorderLayout());
		zwei.setLayout(new BorderLayout());
		drei.setLayout(new GridLayout(1, 3));
		vier.setLayout(new GridLayout(1, 2));
		pDreiInhalt.setLayout(new GridLayout(2,1));
		pDreiUnten.setLayout(new GridLayout(1,2));
		pButton.setLayout(new GridLayout(3,3));
		
		eins.setBackground(new Color(255,255,17));
		zwei.setBackground(new Color(255,255,17));
		pDreiInhalt.setBackground(new Color(255,255,17));
		pDreiUnten.setBackground(new Color(255,255,17));
		vier.setBackground(new Color(255,255,17));
		pButton.setBackground(new Color(255,255,17));

		lUeberschrift.setForeground(new Color(128,0,0));
		lMarianne.setForeground(new Color(191,0,0));
		lSteffi.setForeground(new Color(191,0,0));
		
		lImpressum.setFont(new Font("Sans Serif", Font.BOLD, 15));
		lSteffi.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lMarianne.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lUeberschrift.setFont(new Font("Sans Serif", Font.BOLD, 20));

		lBeschreibung = new JLabel(
				"<html>Mit Hilfe dieses Programmes werden die Budgets der HTL Hollabrunn "
						+ "geplant und verwaltet. <br>Das Programm wurde von Herrn Leopold Mayer in Auftrag gegeben. <br><br>"
						+ "realisiert von: <br>Liebhart Stefanie und Haupt Marianne <br>(2008/2009)" +
								"<br><br>notwendige Ressourcen: Java6-Installation, VPN-Verbindung zum HTL-Schulnetz, AcrobatReader</html>",JLabel.CENTER);

		lSteffiText = new JLabel(
				"<html>Wohnohrt:    Wenzersdorf" +
				"  <br>Geburtstag:  April 1990" +
				"  <br>Kontakt:     stefanie.liebhart@aon.at</html>",JLabel.CENTER);
		lMarianneText = new JLabel(
				"<html>Wohnohrt:    Laa/Thaya" +
				"  <br>Geburtstag:  Maerz 1990" +
				"  <br>Kontakt:     marianne.haupt@gmx.at</html>",JLabel.CENTER);


		pDreiUnten.add(lSteffi);
		pDreiUnten.add(lMarianne);
		pDreiInhalt.add(lImpressum);
		pDreiInhalt.add(pDreiUnten);
		eins.add(lUeberschrift, BorderLayout.CENTER);
		zwei.add(lBeschreibung,BorderLayout.CENTER);
		drei.add(pDreiInhalt);
		vier.add(lSteffiText);
		vier.add(lMarianneText);
		pButton.add(new JLabel(""));
		pButton.add(new JLabel(""));
		pButton.add(new JLabel(""));
		pButton.add(new JLabel(""));
		pButton.add(btnAnleitung);
		pButton.add(new JLabel(""));
		pButton.add(new JLabel(""));
		pButton.add(new JLabel(""));
		pButton.add(new JLabel(""));
		

		pMain.add(eins);
		pMain.add(zwei);
		pMain.add(drei);
		pMain.add(vier);
		pMain.add(pButton);
		
		c.add(pMain);

	}
	
	/**
	 * lauscht, ob der Button "Bedienungsanleitung" gedrueckt wurde
	 * 
	 * @param e ein ActionEvent-Objekt
	 */
	public void actionPerformed(ActionEvent e)
	{
		try {
			Runtime.getRuntime().exec ("explorer http://Logistik.htl-hl.ac.at/Logistik_2/Bedienungsanleitung.pdf");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
