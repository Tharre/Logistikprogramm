package Logistik;

import javax.swing.*;
import java.awt.*;

/**
 * Klasse zum Erzeugen von Buttons Kann Bild-Buttons und Text-Buttons erzeugen
 */

public class LayoutButtonCreator {
	public static LayoutNaviButton createButton(String path, String info) {
		LayoutNaviButton btn = new LayoutNaviButton(path, info);
		// System.gc();
		// System.out.println("ButtonCreator");
		return btn;
	}

	public static JToggleButton createTextButton(String name) {
		JToggleButton btn = new JToggleButton(name);
		btn.setToolTipText(name);
		// btn.setOpaque(false);
		btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 10,
				btn.getPreferredSize().height));
		return btn;
	}
}