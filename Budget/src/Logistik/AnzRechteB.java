package Logistik;

import java.util.*;

public class AnzRechteB {
	ArrayList rechte;

	public AnzRechteB() {
		rechte = new ArrayList();
		initRechte();
	}

	public void initRechte() {
		/* 0 */rechte.add("neue Usergruppe");
		/* 1 */rechte.add("User einer Usergruppe zuweisen");
		/* 2 */rechte.add("Usergruppen anzeigen");
		/* 3 */rechte.add("Usergruppen bearbeiten");
		/* 4 */rechte.add("Usergruppen l�schen");

		/* 5 */rechte.add("neue Firma");
		/* 6 */rechte.add("alle Firmen");
		/* 7 */rechte.add("Firma l�schen");
		/* 8 */rechte.add("Firma bearbeiten");

		/* 9 */rechte.add("qry Usergruppe");
		/* 10 */rechte.add("qry User");
		/* 11 */rechte.add("qry Rechte");
		/* 12 */rechte.add("qry Firma");
		/* 13 */rechte.add("qry Material");
		/* 14 */rechte.add("qry Material-Firma");
		/* 15 */rechte.add("qry Lager");
		/* 16 */rechte.add("qry Banf");

		/* 17 */rechte.add("neue Bestellung");
		/* 18 */rechte.add("Bestellungen: alle");
		/* 19 */rechte.add("Bestellungen: nicht abgeschickt");
		/* 20 */rechte.add("Bestellungen: abgeschickt");
		/* 21 */rechte.add("Bestellungen: ausst�ndig");
		/* 22 */rechte.add("Bestellungen: fertig");
		/* 23 */rechte.add("Bestellungen: fehlerhaft");

		/* 24 */rechte.add("alle Materialien");
		/* 25 */rechte.add("Material l�schen");
		/* 26 */rechte.add("neues Material");
		/* 27 */rechte.add("Firma Material zuweisen");
		/* 28 */rechte.add("Firma Material l�schen");
		/* 29 */rechte.add("Material bearbeiten");
		/* 30 */rechte.add("Firma-Material bearbeiten");

		/* 31 */rechte.add("alle Inventurgruppen");
		/* 32 */rechte.add("neue Inventurgruppe");
		/* 33 */rechte.add("Inventurgruppe l�schen");

		/* 34 */rechte.add("neue Banf");
		/* 35 */rechte.add("Banf: alle");
		/* 36 */rechte.add("Banf: fertig");
		/* 37 */rechte.add("Banf: abgewiesen");
		/* 38 */rechte.add("Banf: zu bestellen");

		/* 39 */rechte.add("alle Bundesgruppen");
		/* 40 */rechte.add("neue Bundesgruppe");
		/* 41 */rechte.add("Bundesgruppe l�schen");

		/* 42 */rechte.add("DB leeren");
		/* 43 */rechte.add("DB exportieren");
		/* 44 */rechte.add("DB importieren");

		/* 45 */rechte.add("Budget");
		/* 46 */rechte.add("Abteilungen als Kostenstellen ausw�hlen");

		/* 47 */rechte.add("Lieferung");
		/* 48 */rechte.add("Aush�ndigung");
		/* 49 */rechte.add("Zubuchen");
		/* 50 */rechte.add("Zur�ck zu Firma");
		/* 51 */rechte.add("Korrektur");
		/* 52 */rechte.add("Mehrfach�nderung");
	}

	public String[] getRechte() {
		String[] rights = new String[rechte.size()];
		for (int i = 0; i < rights.length; i++) {
			String recht = (String) rechte.get(i);
			rights[i] = recht;
		}
		return rights;
	}

	public int getRechtId(String recht) {
		String[] r = getRechte();
		for (int i = 0; i < r.length; i++) {
			if (r[i].equals(recht)) {
				return i;
			}
		}
		return -1;
	}

	public String getRecht(int id) {
		String[] r = getRechte();
		if (id < r.length) {
			return r[id];
		}
		return null;
	}
}