package Logistik;

/**
 * Das Verwaltungsmenü für Firmen im Midpanel überprüfen der Rechte. Je nachdem
 * wird der Button angezeigt oder nicht
 */

public class VerFirmen extends LayoutMidPanel {
	public VerFirmen(UserImport user) {
		super(user);
		if (user.hasRecht(Logistik.rechte.getRechtId("alle Firmen"))) {
			addNaviButton("Firmen anzeigen");
			addShowPanel(new AnzFirmen(user), "Firmen anzeigen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("neue Firma"))) {
			addNaviButton("Firma anlegen");
			addShowPanel(new NewFirma(user), "Firma anlegen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Firma bearbeiten"))) {
			addNaviButton("Firma bearbeiten");
			addShowPanel(new EditFirma(user), "Firma bearbeiten");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Mehrfachänderung"))) {
			addNaviButton("Mehrfachänderung");
			addShowPanel(new MehrfachaenderungFirma(user), "Mehrfachänderung");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Firma löschen"))) {
			addNaviButton("Firma löschen");
			addShowPanel(new DelFirma(user), "Firma löschen");
		}
	}
}