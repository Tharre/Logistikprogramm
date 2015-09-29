package Logistik;

/**
 * Das Verwaltungsmen� f�r Firmen im Midpanel �berpr�fen der Rechte. Je nachdem
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
		if (user.hasRecht(Logistik.rechte.getRechtId("Mehrfach�nderung"))) {
			addNaviButton("Mehrfach�nderung");
			addShowPanel(new MehrfachaenderungFirma(user), "Mehrfach�nderung");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Firma l�schen"))) {
			addNaviButton("Firma l�schen");
			addShowPanel(new DelFirma(user), "Firma l�schen");
		}
	}
}