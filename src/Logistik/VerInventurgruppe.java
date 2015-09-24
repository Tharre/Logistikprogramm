package Logistik;

/**
 * Das Verwaltungsmenü für Inventurgruppen im Midpanel überprüfen der Rechte. Je
 * nachdem wird der Button angezeigt oder nicht
 */

public class VerInventurgruppe extends LayoutMidPanel {
	public VerInventurgruppe(UserImport user) {
		super(user);
		if (user.hasRecht(Logistik.rechte.getRechtId("neue Inventurgruppe"))) {
			addNaviButton("Inventurgruppen anzeigen");
			addShowPanelScroll(new AnzInvGruppe(user), "Inventurgruppen anzeigen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("alle Inventurgruppen"))) {
			addNaviButton("Inventurgruppe anlegen");
			addShowPanelScroll(new NewInventurgruppe(user), "Inventurgruppe anlegen");
		}
		if (user.hasRecht(Logistik.rechte
				.getRechtId("Inventurgruppe bearbeiten"))) {
			addNaviButton("Inventurgruppe bearbeiten");
			addShowPanelScroll(new EditInventurgruppe(user),
					"Inventurgruppe bearbeiten");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Inventurgruppe löschen"))) {
			addNaviButton("Inventurgruppe löschen");
			addShowPanelScroll(new DelInventurgruppe(user), "Inventurgruppe löschen");
		}		
	}
}