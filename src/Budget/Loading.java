package Budget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.*;



public class Loading extends JFrame{
	
	public URL u;
	public Image image;
	public JLabel lbl;
	JLabel imageLabel = new JLabel();
	JLabel text = new JLabel("Bitte warten...");
	
	public Loading() {
	    
		setSize(200, 50);
		setLayout(new GridLayout());
		lbl = new JLabel("Bitte warten...", SwingConstants.CENTER);
		add(lbl);
		getContentPane().setBackground(Color.BLACK);
		lbl.setBackground(Color.BLACK);
		lbl.setForeground(Color.WHITE);
		lbl.setOpaque(true);
		setUndecorated(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
