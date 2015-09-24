package Logistik;

/**
 * Das Verwaltungsmenü für Bestellungen im Midpanel überprüfen der Rechte. Je
 * nachdem wird der Button angezeigt oder nicht
 */
public class VerBestellung extends LayoutMidPanel {
	public VerBestellung(UserImport user) {
		super(user);

		if (user.hasRecht(Logistik.rechte.getRechtId("Bestellungen: alle"))) {
			addNaviButton("Bestellungen anzeigen");
			addShowPanel(new AnzBestAlle("alle", user), "Bestellungen anzeigen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Bestellungen: alle"))) {
			addNaviButton("heuer");
			addShowPanel(new AnzBestAlle("heuer", user), "heuer");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("neue Bestellung"))) {
			addNaviButton("Bestellung anlegen");
			addShowPanel(new NewBestellung(user), "Bestellung anlegen");
		}
		if (user.hasRecht(Logistik.rechte
				.getRechtId("Bestellungen: nicht abgeschickt"))) {
			addNaviButton("nicht abgeschickt");
			addShowPanel(new AnzBestAlle("nicht abgeschickt", user),
					"nicht abgeschickt");
		}
		if (user.hasRecht(Logistik.rechte
				.getRechtId("Bestellungen: abgeschickt"))) {
			addNaviButton("abgeschickt");
			addShowPanel(new AnzBestAlle("abgeschickt", user), "abgeschickt");
		}
		if (user.hasRecht(Logistik.rechte
				.getRechtId("Bestellungen: ausständig"))) {
			addNaviButton("ausständig");
			addShowPanel(new AnzBestAlle("ausständig", user), "ausständig");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Bestellungen: alle"))) {
			addNaviButton("komplett geliefert");
			addShowPanel(new AnzBestAlle("komplett geliefert", user),
					"komplett geliefert");
		}
		if (user.hasRecht(Logistik.rechte
				.getRechtId("Bestellungen: fehlerhaft"))) {
			addNaviButton("fehlerhaft");
			addShowPanel(new AnzBestAlle("fehlerhaft", user), "fehlerhaft");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Bestellungen: fertig"))) {
			addNaviButton("fertig");
			addShowPanel(new AnzBestAlle("fertig", user), "fertig");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Bestellungen: alle"))) {
			addNaviButton2("suchen");
			addShowPanel(new AnzBestAlle("suchen", user), "suchen");
		}

	}
}