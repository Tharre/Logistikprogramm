package Logistik;

/**
 * Das Verwaltungsmenü für Bestellanforderungen (BANFs) im Midpanel überprüfen
 * der Rechte. Je nachdem wird der Button angezeigt oder nicht
 */

public class VerBanf extends LayoutMidPanel {
	public VerBanf(UserImport user) {
		super(user);

		if (user.hasRecht(Logistik.rechte.getRechtId("Banf: alle"))) {
			addNaviButton("Banfs anzeigen");
			addShowPanel(new AnzBanfA("alle", user), "Banfs anzeigen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("neue Banf"))) {
			addNaviButton("Banf anlegen");
			addShowPanel(new NewBanf(user), "Banf anlegen");
		}
		
		if (user.hasRecht(Logistik.rechte.getRechtId("Banf: fertig"))) {
			addNaviButton("fertig");
			addShowPanel(new AnzBanfA("fertig", user), "fertig");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Banf: zu bestellen"))) {
			addNaviButton("zu bestellen");
			addShowPanel(new AnzBanfA("zuBestellen", user), "zu bestellen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Banf: abgewiesen"))) {
			addNaviButton("abgewiesen");
			addShowPanel(new AnzBanfA("abgewiesen", user), "abgewiesen");
		}

	}
}