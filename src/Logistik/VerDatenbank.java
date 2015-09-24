package Logistik;

/**
 * Das Verwaltungsmenü für Datenbank im Midpanel überprüfen der Rechte. Je
 * nachdem wird der Button angezeigt oder nicht
 */
public class VerDatenbank extends LayoutMidPanel {
	public VerDatenbank(UserImport user) {
		super(user);
		if (user.hasRecht(Logistik.rechte.getRechtId("DB leeren"))) {
			addNaviButton("DB leeren");
			addShowPanel(new DBLeeren(user), "DB leeren");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("DB exportieren"))) {
			addNaviButton("DB exportieren");
			addShowPanel(new DBExport(user), "DB exportieren");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("DB importieren"))) {
			addNaviButton("DB importieren");
			addShowPanel(new DBImport(user), "DB importieren");
		}
	}
}