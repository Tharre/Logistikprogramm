package Logistik;

/*
 * Zeigt die Abfragetypen an.
 * Erzeugt ein AnzeigeAbfragen-Object und gibt den gewählten Typ mit.
 */
public class Abfragen extends LayoutMidPanel {

	public Abfragen(UserImport user) {
		super(user);

		if (user.hasRecht(Logistik.rechte.getRechtId("qry Usergruppe"))) {
			addNaviButton("Usergruppe");
			addShowPanel(new AnzAbfragen("usergruppe", user), "Usergruppe");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("qry User"))) {
			addNaviButton("User");
			addShowPanel(new AnzAbfragen("user", user), "User");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("qry Rechte"))) {
			addNaviButton("Rechte");
			addShowPanel(new AnzAbfragen("rechte", user), "Rechte");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("qry Firma"))) {
			addNaviButton("Firma");
			addShowPanel(new AnzAbfragen("firma", user), "Firma");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("qry Material"))) {
			addNaviButton("Material");
			addShowPanel(new AnzAbfragen("material", user), "Material");
		}

		if (user.hasRecht(Logistik.rechte.getRechtId("qry Material-Firma"))) {
			addNaviButton("Material-Firma");
			addShowPanel(new AnzAbfragen("material-firma", user),
					"Material-Firma");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("qry Lager"))) {
			addNaviButton("Lager");
			addShowPanel(new AnzAbfragen("lager", user), "Lager");
		}
		if (user.hasRecht(Logistik.rechte.getRechtId("qry Banf"))) {
			addNaviButton("Banf/Best.");
			addShowPanel(new AnzAbfragen("banf", user), "Banf/Best.");
		}

	}

}// class
