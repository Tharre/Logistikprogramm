package Logistik;

/**
 * Das Verwaltungsmen� f�r User im Midpanel �berpr�fen der Rechte. Je nachdem
 * wird der Button angezeigt oder nicht
 */

public class VerUser extends LayoutMidPanel {
	public VerUser(UserImport user) {
		super(user);

		if (user.hasRecht(Logistik.rechte.getRechtId("Usergruppen anzeigen"))) {
			addNaviButton("Usergruppen anzeigen");
			addShowPanelScroll(new AnzUsergroups(user), "Usergruppen anzeigen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("neue Usergruppe"))) {
			addNaviButton("Usergruppe anlegen");
			addShowPanelScroll(new NewUsergruppe(user), "Usergruppe anlegen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Usergruppen bearbeiten"))) {
			addNaviButton("Usergruppe bearbeiten");
			addShowPanelScroll(new EditUsergruppe(user),
					"Usergruppe bearbeiten");
		}
		if (user.hasRecht(Logistik.rechte
				.getRechtId("User einer Usergruppe zuweisen"))) {
			addNaviButton("User einer Usergruppe zuweisen");
			addShowPanelScroll(new UserUsergroup(user),
					"User einer Usergruppe zuweisen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Usergruppen l�schen"))) {
			addNaviButton("Usergruppe l�schen");
			addShowPanelScroll(new DelUsergruppe(user), "Usergruppe l�schen");
		}

	}
}