package Logistik;

/**
 * Das Verwaltungsmen� f�r Materialien im Midpanel �berpr�fen der Rechte. Je
 * nachdem wird der Button angezeigt oder nicht
 */

public class VerMaterial extends LayoutMidPanel {
	public VerMaterial(UserImport user) {
		super(user);
		if (user.hasRecht(Logistik.rechte.getRechtId("alle Materialien"))) {
			addNaviButton("Materialien anzeigen");
			addShowPanel(new AnzMaterialien(user), "Materialien anzeigen");

			addNaviButton("Materialien exportieren");
			addShowPanel(new MaterialExport(user),
					"Materialien exportieren");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("neues Material"))) {
			addNaviButton("Material anlegen");
			addShowPanel(new NewMaterial(user), "Material anlegen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Material bearbeiten"))) {
			addNaviButton("Material bearbeiten");
			addShowPanel(new EditMaterial(user), "Material bearbeiten");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Mehrfach�nderung"))) {
			addNaviButton("Mehrfach�nderung");
			addShowPanel(new MehrfachaenderungMaterial(user), "Mehrfach�nderung");

		}
		if (user.hasRecht(Logistik.rechte
				.getRechtId("Firma-Material bearbeiten"))) {
			addNaviButton("Firma-Material bearbeiten");
			addShowPanel(new EditFirmaMaterial(user),
					"Firma-Material bearbeiten");
		}
		if (user
				.hasRecht(Logistik.rechte.getRechtId("Firma Material zuweisen"))) {
			addNaviButton("Firma Material zuweisen");
			addShowPanel(new FirmaMaterial(user), "Firma Material zuweisen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Material l�schen"))) {
			addNaviButton("Material l�schen");
			addShowPanel(new DelMaterial(user), "Material l�schen");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("Firma Material l�schen"))) {
			addNaviButton("Firma Material l�schen");
			addShowPanel(new DelFirmaMaterial(user), "Firma Material l�schen");
		}
	}
}