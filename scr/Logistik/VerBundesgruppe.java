package Logistik;

/**
 * Das Verwaltungsmen� f�r Bundesnummern im Midpanel �berpr�fen der Rechte. Je
 * nachdem wird der Button angezeigt oder nicht
 */
public class VerBundesgruppe extends LayoutMidPanel {
	public VerBundesgruppe(UserImport user) {
		super(user);
		if (user.hasRecht(Logistik.rechte.getRechtId("neue Bundesgruppe"))) {
			addNaviButton("Bundesgruppen anzeigen");
			addShowPanelScroll(new AnzBundesGruppe(user), "Bundesgruppen anzeigen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("alle Bundesgruppen"))) {
			addNaviButton("Bundesgruppe anlegen");
			addShowPanelScroll(new NewBundesgruppe(user), "Bundesgruppe anlegen");
		}
		if (user
				.hasRecht(Logistik.rechte.getRechtId("Bundesgruppe bearbeiten"))) {
			addNaviButton("Bundesgruppe bearbeiten");
			addShowPanelScroll(new EditBundesgruppe(user),
					"Bundesgruppe bearbeiten");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Bundesgruppe l�schen"))) {
			addNaviButton("Bundesgruppe l�schen");
			addShowPanelScroll(new DelBundesgruppe(user),
					"Bundesgruppe l�schen");
		}
	}
}