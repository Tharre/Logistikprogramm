package Logistik;

/**
 * Das Verwaltungsmen� f�r Inventurgruppen im Midpanel �berpr�fen der Rechte. Je
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
		if (user.hasRecht(Logistik.rechte.getRechtId("Inventurgruppe l�schen"))) {
			addNaviButton("Inventurgruppe l�schen");
			addShowPanelScroll(new DelInventurgruppe(user), "Inventurgruppe l�schen");
		}		
	}
}