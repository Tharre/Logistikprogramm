package Logistik;

/*
 * Zeigt das Lagerverwaltungsmenü an.
 */
public class VerLager extends LayoutMidPanel {
	public VerLager(UserImport user) {
		super(user);

		if (user.hasRecht(Logistik.rechte.getRechtId("Lieferung"))) {
			addNaviButton("Lieferung");
			addShowPanel(new AnzBestAlle("ausständig", user), "Lieferung");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Aushändigung"))) {
			addNaviButton("Aushändigung1");
			addShowPanel(new LagerumbuchungenA(user, LagerumbuchungenA.AUSHAENDIGEN),
					"Aushändigung1");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Aushändigung"))) {
			addNaviButton("AushändigungN");
			addShowPanel(new LagerumbuchungenA(user,
					LagerumbuchungenA.MEHREREAUSHAENDIGEN), "AushändigungN");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Zubuchen"))) {
			addNaviButton("Zubuchen");
			addShowPanel(new LagerumbuchungenA(user, LagerumbuchungenA.ZUBUCHEN_USER),
					"Zubuchen");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Zurück zu Firma"))) {
			addNaviButton("Zurück zu Firma");
			addShowPanel(new LagerumbuchungenA(user, LagerumbuchungenA.FIRMA),
					"Zurück zu Firma");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Korrektur"))) {
			addNaviButton("Korrektur");
			addShowPanel(new LagerumbuchungenA(user, LagerumbuchungenA.KORREKTUR),
					"Korrektur");
		}

	}
}