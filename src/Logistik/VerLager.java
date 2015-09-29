package Logistik;

/*
 * Zeigt das Lagerverwaltungsmen� an.
 */
public class VerLager extends LayoutMidPanel {
	public VerLager(UserImport user) {
		super(user);

		if (user.hasRecht(Logistik.rechte.getRechtId("Lieferung"))) {
			addNaviButton("Lieferung");
			addShowPanel(new AnzBestAlle("ausst�ndig", user), "Lieferung");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Aush�ndigung"))) {
			addNaviButton("Aush�ndigung1");
			addShowPanel(new LagerumbuchungenA(user, LagerumbuchungenA.AUSHAENDIGEN),
					"Aush�ndigung1");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Aush�ndigung"))) {
			addNaviButton("Aush�ndigungN");
			addShowPanel(new LagerumbuchungenA(user,
					LagerumbuchungenA.MEHREREAUSHAENDIGEN), "Aush�ndigungN");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Zubuchen"))) {
			addNaviButton("Zubuchen");
			addShowPanel(new LagerumbuchungenA(user, LagerumbuchungenA.ZUBUCHEN_USER),
					"Zubuchen");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Zur�ck zu Firma"))) {
			addNaviButton("Zur�ck zu Firma");
			addShowPanel(new LagerumbuchungenA(user, LagerumbuchungenA.FIRMA),
					"Zur�ck zu Firma");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("Korrektur"))) {
			addNaviButton("Korrektur");
			addShowPanel(new LagerumbuchungenA(user, LagerumbuchungenA.KORREKTUR),
					"Korrektur");
		}

	}
}