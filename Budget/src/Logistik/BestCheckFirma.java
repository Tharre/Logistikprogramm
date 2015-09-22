package Logistik;

/**
 * Überprüfung ob nur 1 Firma beim Bestellablauf gewählt wurde
 */
public class BestCheckFirma {
	/**
	 * Hilfsvariable zum Speichern, ob nur eiene Firma gewählt wurde
	 */
	public Boolean eine;

	public BestCheckFirma(DBConnection con, Object[] hakBanf) {

		Object[] check = new Object[hakBanf.length];
		for (int x = 0; x < hakBanf.length; x++) {
			check[x] = hakBanf[x];
		}

		eine = true;
		for (int i = 0; i < check.length - 1; i++) {
			if (!(check[i].equals(check[i + 1]))) {
				eine = false;
			}
		}

	}

}