package Budget;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.Vector;

/**
 *In der Klasse DatenImport werden die Daten aus der Datenbank gelesen
 *<p>
 * Title: DatenImport
 * 
 * @author Haupt, Liebhart
 **/
public class DatenImport {

	// double
	/**
	 * hier wird das ausgegebene Budget gespeichert, wenn es mit einem
	 * SQL-Befehl aus der Datenbank gelesen wird
	 **/
	private double ausgegeben;
	/**
	 * hier wird das gesperrte Budget gespeichert, wenn es mit einem SQL-Befehl
	 * aus der Datenbank gelesen wird
	 **/
	private double gesperrt;
	/**
	 * hier wird das geplante Budget gespeichert, wenn es mit einem SQL-Befehl
	 * aus der Datenbank gelesen wird
	 **/
	private double geplant;
	/**
	 * hier wird das verfuegbare Budget gespeichert, wenn es mit einem
	 * SQL-Befehl aus der Datenbank gelesen wird
	 **/
	private double verfuegbar;

	// String
	/** Befehl fuer die Datenbank **/
	private String query;
	/** Befehl fuer die Datebank **/
	private String query2;
	/** hier wird das Datum gespeichert **/
	private String datumS;

	// Vector
	/** die ausgelesenen Daten **/
	private Vector daten = new Vector();
	/** eine Zeile der Tabelle **/
	private Vector row;
	/** eine Zeile der Tabelle **/
	private Vector row2;
	/** eine Zeile der Tabelle **/
	private Vector row3;
	/** eine Zeile der Tabelle **/
	private Vector row4;
	/** eine Zeile der Tabelle **/
	private Vector row5;
	/** ein Hilfsvector **/
	private Vector hilfVector = new Vector();
	/** Hilfsvector um Berechnungen durchzufuehren **/
	private Vector<Double> geplantV = new Vector<Double>();
	/** Hilfsvector um Berechnungen durchzufuehren **/
	private Vector<Integer> nummerV = new Vector<Integer>();
	/** ein Hilfsvector **/
	private Vector hilfV = new Vector();

	// Statement
	/** Statement fuer die Budgetdatenbank **/
	private Statement stmt;
	/** Statement fuer die Logistikdatenbank **/
	private Statement stmtL;

	// ResulSet
	/** ResultSet **/
	private ResultSet rs;
	/** ResultSet **/
	private ResultSet rs2;

	// Connection
	/** Connection zur Budget-Datenbank */
	private Connection con;
	/** Connection zur Logistik-Datenbank */
	private Connection conL;

	// sonstiges
	/** Datum in Millisekunden **/
	private long datum;
	/** Objekt von der Klasse Date, fuer die Datumsberechnung **/
	private Date date;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budgetdatenbank
	 * @param conL
	 *            Connection zur Logistikdatenbank
	 */
	public DatenImport(Connection con, Connection conL) {
		this.con = con;
		this.conL = conL;

	}

	public DatenImport() {
	}

	/**
	 * liest Daten von der Budget-Datenbank aus
	 * 
	 * @param tabelle
	 *            Name der Tabelle
	 * @param kennnummer
	 *            welche Tabelle der Datenbank ausgelesen werden sollen (jede
	 *            Tabelle hat eine Kennnumer)
	 * @return daten Daten der Tabelle und ausgerechnete Daten
	 */
	public Vector auslesenDaten(String tabelle, int kennnummer) {

		daten.clear();
		geplantV.clear();
		nummerV.clear();

		try {
			query = "select * from " + tabelle;

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();
				// es gibt in jeder Tabelle ein Feld Nummer
				nummerV.add(rs.getInt("nummer"));

				// es soll die Nummer aber nur bei den Tabellen LMB, UT3 und
				// Sonderbudget angezeigt werden
				if (kennnummer < 5 && kennnummer != 2 || kennnummer > 8) {
					row.add(rs.getString("nummer"));

					row.add(rs.getString("name"));
					// bei allen anderen soll die selbst eingegebene Nummer
					// angezeigt werden
				} else {
					row.add(rs.getString("NummerSelbst"));
					row.add(rs.getString("name"));

				}

				// bei UT3 gibt es EDV- HAT Anteil
				if (kennnummer == 1) {

					try {
						row.add(runde(Double.parseDouble((rs
								.getString("EDVHATAnteil")))));
					} catch (NumberFormatException e) {
						row.add((rs.getString("EDVHATAnteil")));
					}

				}
				// bei UT3, LMB1 und LMB2 gibt es ein festgeplantes Budget
				if (kennnummer == 3 || kennnummer == 9 || kennnummer == 1) {
					row.add(runde(rs.getDouble("festgeplant")));

				}

				// bei Projekt gibt es zusatzlich noch andere Felder
				if (kennnummer == 2) {

					row.add(rs.getString("Lehrer"));
					row.add(rs.getString("Kurzz"));
					row.add(rs.getString("Klasse"));
					row.add(rs.getString("Datum"));
					row.add(rs.getString("Teilnehmer"));
					row.add(rs.getString("Abteilung"));
				}

				if (kennnummer == 5 || kennnummer == 6 || kennnummer == 7) {
					row.add(runde(rs.getDouble("geplant")));
					// die geplanten Betraege werden zusaetzlich noch auf einen
					// Vector gespeichert fuer die spaetere Berechnung
					geplantV.add(runde(rs.getDouble("geplant")));

				}
				// bei der Tabelle Kostenstelle gibt es noch eine Raumnummer und
				// eine Kurzbezeichnung
				if (kennnummer == 8) {
					row.add(rs.getString("Raumnummer"));
					row.add(rs.getString("Kurzbezeichn"));
				}

				if (kennnummer == 1 || kennnummer == 3 || kennnummer == 4
						|| kennnummer == 8 || kennnummer == 9) {
					row.add(runde(rs.getDouble("geplant")));
					geplant = rs.getDouble("geplant");
					row.add(runde(rs.getDouble("ausgegeben")));
					ausgegeben = rs.getDouble("ausgegeben");

					row.add(runde(rs.getDouble("gesperrt")));
					gesperrt = runde(rs.getDouble("gesperrt"));

					// das verfuegbare Budget wird ausgerechnet
					verfuegbar = berechneVerfuegbar(gesperrt, ausgegeben,
							geplant);
					row.add(runde(verfuegbar));
				}
				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		// wenn es sich um eine Bereich, Hauptkostenstelle oder Kostenstelle
		// handelt, dann wird das verfuegbare Budget von den Kostenstellen
		// berechnet, da man nur bei Kostenstellen Betraege sperren und ausgeben
		// kann
		if (kennnummer == 7 || kennnummer == 6 || kennnummer == 5) {

			for (int i = 0; i < daten.size(); i++) {

				// wenn es sich um das Hauptbudget handelt
				if (kennnummer == 5 && nummerV.get(i) == 1) {

					double[] betraege;
					betraege = getSummeAusgegebenGesperrt("select sum(ausgegeben), sum(gesperrt) from kostenstelleut8");

					((Vector) daten.get(i)).add(3, betraege[0]);
					((Vector) daten.get(i)).add(4, betraege[1]);
					((Vector) daten.get(i)).add(5,
							berechneVerfuegbarUeber(betraege));

					// bei allen anderen Hauptbereichen, Bereichen und
					// Hauptkostenstellen
				} else {
					ausgegeben = berechneAusgegeben(nummerV.get(i), kennnummer);

					gesperrt = berechneGesperrt(nummerV.get(i), kennnummer);

					((Vector) daten.get(i)).add(3, ausgegeben);
					((Vector) daten.get(i)).add(4, gesperrt);
					((Vector) daten.get(i)).add(5, berechneVerfuegbar(gesperrt,
							ausgegeben, geplantV.get(i)));

				}
			}
		}

		return daten;
	}// end auslesenDaten()

	/**
	 * Es werden Daten aus der Budget-Datenbank ausgelesen; man kann eine
	 * where-klausl für den SQL-Befehl mitschicken
	 * 
	 * @param tabelle
	 *            Name der Tabelle
	 * @param spalteDatenbank
	 *            welche Spalte in der Datenbank soll ueberprueft werden
	 * @param whereklausl
	 *            die selbst definierte where-klausl
	 * @param kennnummer
	 *            welche Tabelle der Datenbank ausgelesen werden sollen (jede
	 *            Tabelle hat eine Kennnumer)
	 * @return daten Daten der Tabelle und ausgerechnete Daten
	 */
	public Vector auslesenDatenMitWhereKlausel(String tabelle,
			String spalteDatenbank, int whereklausl, int kennnummer) {

		daten.clear();
		nummerV.clear();
		geplantV.clear();

		double[] werte = new double[2];

		// in der Tabelle ut3 stehen in der ersten Zeile (mit der Nummer 1) der
		// EDV- HAT- Anteil mit einenm Beistrich getrennt (EDVAnteil, HATAnteil)
		if (kennnummer == 1 && whereklausl == 1) {

			werte = getEDVHATAnteil();
		}

		try {
			query = "select * from " + tabelle + " where " + spalteDatenbank
					+ "= " + whereklausl;

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();
				// Diese Spalten sind in jeder Tabelle gleich

				nummerV.add(rs.getInt("nummer"));

				if (kennnummer < 5 && kennnummer != 2 || kennnummer > 8) {
					row.add(rs.getString("nummer"));

					row.add(rs.getString("name"));
				} else {
					row.add(rs.getString("NummerSelbst"));
					row.add(rs.getString("name"));

				}

				if (kennnummer == 1 && whereklausl != 1) {

					System.out
							.println("EDVHAT " + rs.getString("EDVHATAnteil"));
					System.out.println("festgeplant "
							+ rs.getDouble("festgeplant"));
					row.add(runde(Double.parseDouble((rs
							.getString("EDVHATAnteil")))));
					row.add(runde(rs.getDouble("festgeplant")));

				}
				if (kennnummer == 1 && whereklausl == 1) {

					// die Prozentwerte werden zusammengerechnet
					double summe = werte[0] + werte[1];

					double geplant = rs.getDouble("geplant");
					// es wird der absolute EDV- HAT Anteil berechnet
					row.add(runde(geplant * summe / 100));

				}

				if ((kennnummer == 3 || kennnummer == 9)
						&& spalteDatenbank.equals("kennung")) {
					row.add(runde(rs.getDouble("festgeplant")));

				}

				if (kennnummer == 2) {
					row.add(rs.getString("nummerSelbst"));
					row.add(rs.getString("name"));
					row.add(rs.getString("Lehrer"));
					row.add(rs.getString("Kurzz"));
					row.add(rs.getString("Klasse"));
					row.add(rs.getString("Datum"));
					row.add(rs.getString("Teilnehmer"));
					row.add(rs.getString("Abteilung"));
				}

				if (kennnummer == 5 || kennnummer == 6 || kennnummer == 7) {
					row.add(rs.getString("geplant"));
					geplantV.add(rs.getDouble("geplant"));

				}

				if (kennnummer == 8) {
					row.add(rs.getString("Raumnummer"));
					row.add(rs.getString("Kurzbezeichn"));
				}

				if (kennnummer == 1 || kennnummer == 3 || kennnummer == 4
						|| kennnummer == 8 || kennnummer == 9) {
					row.add(runde(rs.getDouble("geplant")));
					geplant = rs.getDouble("geplant");
					row.add(runde(rs.getDouble("ausgegeben")));
					ausgegeben = runde(rs.getDouble("ausgegeben"));

					row.add(runde(rs.getDouble("gesperrt")));
					gesperrt = runde(rs.getDouble("gesperrt"));

					verfuegbar = runde(berechneVerfuegbar(gesperrt, ausgegeben,
							geplant));
					row.add(verfuegbar);
				}
				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			ex.printStackTrace();
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		if (kennnummer == 7 || kennnummer == 6 || kennnummer == 5) {

			for (int i = 0; i < daten.size(); i++) {

				if (kennnummer == 5 && nummerV.get(i) == 1) {

					double[] betraege;
					betraege = getSummeAusgegebenGesperrt("select sum(ausgegeben), sum(gesperrt) from kostenstelleut8");

					((Vector) daten.get(i)).add(3, betraege[0]);
					((Vector) daten.get(i)).add(4, betraege[1]);
					((Vector) daten.get(i)).add(5,
							berechneVerfuegbarUeber(betraege));

				} else {

					ausgegeben = berechneAusgegeben(nummerV.get(i), kennnummer);

					gesperrt = berechneGesperrt(nummerV.get(i), kennnummer);

					((Vector) daten.get(i)).add(3, ausgegeben);
					((Vector) daten.get(i)).add(4, gesperrt);
					((Vector) daten.get(i)).add(5, berechneVerfuegbar(gesperrt,
							ausgegeben, geplantV.get(i)));

				}

			}

		}

		return daten;
	}// end auslesenDatenMitWhereKlausl()

	/**
	 * berechnet den ausgegebenen Betrag
	 * 
	 * @param nummer
	 *            Nummer des Datensatzes
	 * @param kennnummer
	 *            Kennnummer der Tabelle
	 * @return ausgegebener Betrag
	 */
	public double berechneAusgegeben(int nummer, int kennnummer) {
		double summeAusgegeben = 0;
		try {
			switch (kennnummer) {
			case 5:
				query = "select sum(ausgegeben) from  kostenstelleut8 k,hauptkostenstelleut8 hk, bereichut8 b where b.hauptnummer="
						+ nummer
						+ " AND hk.hauptnummer=b.nummer AND k.hauptnummer=hk.nummer";
				break;
			case 6:
				query = "select sum(ausgegeben) from  kostenstelleut8 k,hauptkostenstelleut8 hk where hk.hauptnummer="
						+ nummer + " AND k.hauptnummer=hk.nummer";
				break;
			case 7:
				query = "select sum(ausgegeben) from kostenstelleut8 where hauptnummer="
						+ nummer;

			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				summeAusgegeben = rs.getDouble("sum(ausgegeben)");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return runde(summeAusgegeben);

	}

	/**
	 * berechnet das verfuegbare Budget fuer die Haupttabelle UT8
	 * 
	 * @param hilf
	 *            Summe der ausgegebenen Betraege und Summer der gesperrten
	 *            Betraege aller Kostenstellen UT8
	 * @return verfuegbarer Betrag des gesamten Budgets UT8
	 */
	public double berechneVerfuegbarUeber(double[] hilf) {
		double verfuegbar = 0;
		query = "select geplant from hauptbereichut8 where nummer=1";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				geplant = rs.getDouble("geplant");

			verfuegbar = geplant - hilf[0] - hilf[1];

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return runde(verfuegbar);

	}

	/**
	 * gibt das gesamte gesperrte Budget zurueck
	 * 
	 * @param nummer
	 *            Nummer des Datensatzes
	 * @param kennnummer
	 *            Kennnummer der Tabelle
	 * @return Summe der gesperrten Betraege
	 */
	public double berechneGesperrt(int nummer, int kennnummer) {
		double summeGesperrt = 0;

		try {
			switch (kennnummer) {
			case 5:
				query = "select sum(gesperrt) from  kostenstelleut8 k,hauptkostenstelleut8 hk, bereichut8 b where b.hauptnummer="
						+ nummer
						+ " AND hk.hauptnummer=b.nummer AND k.hauptnummer=hk.nummer";
				break;
			case 6:
				query = "select sum(gesperrt) from  kostenstelleut8 k,hauptkostenstelleut8 hk where hk.hauptnummer="
						+ nummer + " AND k.hauptnummer=hk.nummer";
				break;
			case 7:
				query = "select sum(gesperrt) from kostenstelleut8 where hauptnummer="
						+ nummer;
			}

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				summeGesperrt = rs.getDouble("sum(gesperrt)");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return runde(summeGesperrt);

	}

	/**
	 * gibt eine Vector zurueck, an der ersten Stelle steht die Summe der
	 * ausgegebenen Betraege; an der zweiten Stelle steht die Summe der
	 * gesperrten Betraege
	 * 
	 * @param query
	 *            Befehl fuer die Datebank
	 * @return Vector mit den Betraegen
	 */
	public double[] getSummeAusgegebenGesperrt(String query) {

		double[] betraege = new double[2];

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				betraege[0] = runde(rs.getDouble("sum(ausgegeben)"));
				betraege[1] = runde(rs.getDouble("sum(gesperrt)"));
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return betraege;

	}

	/**
	 * Es werden Daten aus der Logistik-Datenbank ausgelesen
	 * 
	 * @param tabelle
	 *            aus welcher Tabelle soll ausgelesen werden
	 * @return gibt den Vector mit den ausgelesenen Daten zurueck
	 */
	public Vector auslesenDatenLogistik(String tabelle) {

		daten.clear();
		row2 = new Vector();

		try {
			query = "select * from " + tabelle;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row2.add(rs.getInt("firma"));
				row.add(rs.getInt("bestId"));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);

				row.add(datumS);
				row.add(rs.getString("antragsteller"));
				row.add(getStatusLieferungZurNummer(rs.getInt("status")));
				row.add(getStatusBestellungZurNummer(rs.getInt("statusbez")));
				row.add(rs.getString("budget"));
				row.add(rs.getString("wnummer"));

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

			for (int i = 0; i < daten.size(); i++) {
				((Vector) daten.get(i)).add(getFirmennameZurID(Integer
						.parseInt(row2.get(i).toString())));
			}

		}// end try
		catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return daten;

	}// end auslesenDaten()

	/**
	 * Es werden alle nicht bezahlte Bestellungen ausgelesen
	 * 
	 * @return gibt den Vector mit den ausgelesenen Daten zurueck
	 */
	public Vector auslesenheurigeBestellungen() {

		daten.clear();
		Date datumE = new Date();
		int jahr = datumE.getYear();
		Date datumD = new Date(jahr, 0, 0);

		try {
			query = "select * from bestellung where datum>"
					+ (datumD.getTime() / 1000);

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			Vector row2 = new Vector();
			while (rs.next()) {
				row = new Vector();

				row2.add(rs.getInt("firma"));
				row.add(rs.getInt("bestId"));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);

				row.add(datumS);
				row.add(rs.getString("antragsteller"));
				row.add(getStatusLieferungZurNummer(rs.getInt("status")));
				row.add(getStatusBestellungZurNummer(rs.getInt("statusbez")));
				row.add(rs.getString("budget"));
				row.add(rs.getString("wnummer"));

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

			for (int i = 0; i < daten.size(); i++) {
				((Vector) daten.get(i)).add(getFirmennameZurID(Integer
						.parseInt(row2.get(i).toString())));
			}

		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return daten;
	}

	public Vector auslesenNichtbezahlteBestellung() {

		daten.clear();

		try {
			query = "select * from bestellung where ((statusbez=2)||(statusbez=1)) AND ((status=3)||(status=7)||(status=5)||(status=4)||(status=6))";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			Vector row2 = new Vector();
			while (rs.next()) {
				row = new Vector();

				row2.add(rs.getInt("firma"));
				row.add(rs.getInt("bestId"));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);

				row.add(datumS);
				row.add(rs.getString("antragsteller"));
				row.add(getStatusLieferungZurNummer(rs.getInt("status")));
				row.add(getStatusBestellungZurNummer(rs.getInt("statusbez")));
				row.add(rs.getString("budget"));
				row.add(rs.getString("wnummer"));

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

			for (int i = 0; i < daten.size(); i++) {
				((Vector) daten.get(i)).add(getFirmennameZurID(Integer
						.parseInt(row2.get(i).toString())));
			}

		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return daten;
	}

	/**
	 * 
	 * Es werden alle teilweise bezahlte Bestellungen ausgelesen
	 * 
	 * @return gibt den Vector mit den ausgelesenen Daten zurueck
	 */
	public Vector auslesenTeilweiseBezahlteBestellungen() {
		daten.clear();

		try {
			query = "select * from bestellung where  statusbez=3 AND ((status=4)||(status=5)||(status=6)||(status=7))";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			row2 = new Vector();

			while (rs.next()) {
				row = new Vector();

				row2.add(rs.getInt("firma"));
				row.add(rs.getInt("bestId"));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);

				row.add(datumS);
				row.add(rs.getString("antragsteller"));
				row.add(getStatusLieferungZurNummer(rs.getInt("status")));
				row.add(getStatusBestellungZurNummer(rs.getInt("statusbez")));
				row.add(rs.getString("budget"));
				row.add(rs.getString("wnummer"));

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

			for (int i = 0; i < daten.size(); i++) {
				((Vector) daten.get(i)).add(getFirmennameZurID(Integer
						.parseInt(row2.get(i).toString())));
			}

		} catch (SQLException ex) // mögliche Fehler während der DB-Sitzung
		// ausgeben
		{
			System.out.println("SQL-Exception: " + ex.getMessage());
			System.out.println("SQL-State: " + ex.getSQLState());
			System.out.println("SQL-ErrorCode: " + ex.getErrorCode());
		}

		return daten;

	}

	/**
	 * liesst die Namen der Abteilungen aus
	 * 
	 * @param query
	 *            Befehl fuer die Datenbank
	 * @return Vector mit den Abteilungen
	 */
	public Vector<String> namenAbteilungen(String query) {
		Vector<String> namen = new Vector<String>();
		int zaehler = 0;

		try {

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				namen.add(rs.getString("name"));
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return namen;
	}

	/**
	 * Gibt den ausgegebenen Betrag von einer Abteilung vom Budget LMB zurueck
	 * 
	 * @param abteilungsname
	 *            Name der gewuenschten Abteilung
	 * @param kennnung
	 *            um welches LMB handelt es sich
	 * @return der ausgegebene Betrag vom LMB einer Abteilung
	 */
	public double getAbteilungsbetragLMB(String abteilungsname, int kennnung) {
		ausgegeben = 0;

		try {
			query = "select * from lmb where name like '" + abteilungsname
					+ "' and kennung=" + kennnung;

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				ausgegeben = rs.getDouble("ausgegeben");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return runde(ausgegeben);
	}

	/**
	 * Gibt den ausgegebenen Betrag von einer Abteilung vom Budget UT3 zurueck
	 * 
	 * @param abteilungsname
	 *            Name der gewuenschten Abteilung
	 * @return der ausgegebener Betrag vom UT3 einer Abteilung
	 */
	public double getAbteilungsbetragUT3(String abteilungsname) {

		ausgegeben = 0;

		try {
			query = "select * from abteilungut3 where name like '"
					+ abteilungsname + "'";

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				ausgegeben = rs.getDouble("ausgegeben");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return runde(ausgegeben);
	}

	/**
	 * Sucht die Daten zu einer bestimmten Rechnung
	 * 
	 * @param query
	 *            ein SQL-Befehl, der ausgefuehrt werden soll
	 * @return einen Vector mit dem Ergebnis
	 */
	public Vector sucheRechnung(String query) {

		row = new Vector();
		int rechnungsstatus;
		double rechnungsb;
		double skonto;
		double sonderabzug;
		daten = new Vector();

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();
				row.add(rs.getString("Nummer"));
				row.add(rs.getString("wNummer"));
				row.add(rs.getString("Bestellbetrag"));
				row.add(rs.getString("Rechnungsbetrag"));
				rechnungsb = rs.getDouble("Rechnungsbetrag");
				row.add(rs.getString("externeNummer"));
				row.add(rs.getString("interneNummer"));
				row.add(rs.getString("Inventarnummer"));
				row.add(rs.getString("Buchhaltungsbelege"));
				row.add(rs.getString("Sonderabzug"));
				sonderabzug = rs.getDouble("Sonderabzug");
				skonto = rs.getDouble("Skonto");
				row.add(rs.getString("Skonto"));
				row.add(rs.getString("Zahlart"));
				row.add((rechnungsb - sonderabzug)
						- ((rechnungsb - sonderabzug) * skonto / 100));
				rechnungsstatus = rs.getInt("Rechnungsstatus");
				if (rechnungsstatus == 1)
					row.add("Teilrechnung");
				else
					row.add("Gesamtrechnung");

				daten.add(row);
			}// end while

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return daten;
	}

	/**
	 * Die Daten der Banfs zu einem bestimmmten Projekt werden ausgelesen
	 * 
	 * @param projektname
	 *            zu welchem Projekt die Daten der Banfs gesucht werden sollen
	 * @return ein Vector mit dem Ergebnis
	 */
	public Vector auslesenDetails(String projektname) {

		daten.clear();
		row2 = new Vector();
		
		String projektnameKurz;
		projektnameKurz = projektname.substring(0, 20);

		try {
			query = "select * from banf where kostenstelle like '"
					+ projektname + "'";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				row = new Vector();

				row.add(rs.getInt("id"));
				row.add(rs.getString("antragsteller"));
				row.add(rs.getString("kostenstelle"));
				row.add(getStatusBanfZurNummer(rs.getInt("status")));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);

				row.add(datumS);
				row2.add(rs.getInt("firma"));

				daten.add(row);
			}// end while

			rs.close();
			stmt.close();

			for (int i = 0; i < daten.size(); i++) {
				((Vector) daten.get(i)).add(5, getFirmennameZurID(Integer
						.parseInt(row2.get(i).toString())));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return daten;

	}

	public Vector auslesenBuchungen(String projektname) {
		daten.clear();
		row2 = new Vector();
		row3 = new Vector();
		row4 = new Vector();

		try {
			query = "select b.id, b.user, b.datum, b.material, b.firma, -b.stk, b.kst, fm.id, (((fm.preisExkl*fm.mwst/100)+fm.preisExkl)*(-b.stk)) as preisGes from buchungen b, firma_material fm where kst like '"
					+ projektname
					+ "' AND b.firma=fm.firma AND b.material=fm.material";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row2 = new Vector();
				row2.add(rs.getInt("b.id"));
				row2.add(rs.getString("b.user"));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);
				row2.add(datumS);
				row2.add(rs.getDouble("preisGes"));
				row2.add(rs.getDouble("-b.stk"));
				row3.add(rs.getInt("b.material"));
				row4.add(rs.getInt("b.firma"));

				daten.add(row2);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		// Material
		for (int i = 0; i < row3.size(); i++) {
			((Vector) daten.get(i)).add(5, getNameZurBezeichnung(Integer
					.parseInt(row3.get(i).toString())));
		}
		// Firma
		for (int i = 0; i < row4.size(); i++) {
			((Vector) daten.get(i)).add(6, getFirmennameZurID(Integer
					.parseInt(row4.get(i).toString())));
		}

		return daten;
	}

	/**
	 * Berechnet das noch verfuegbare Budget
	 * 
	 * @param gesperrt
	 *            gesperrter Betrag
	 * @param ausgegeben
	 *            ausgegebener Betrag
	 * @param geplant
	 *            geplanter Betrag
	 * @return verfuegabarer Betrag
	 */
	public double berechneVerfuegbar(double gesperrt, double ausgegeben,
			double geplant) {
		verfuegbar = geplant - gesperrt - ausgegeben;

		return runde(verfuegbar);

	}

	/**
	 * Es werden alle Bestellungen gesucht, die mit dem mitgeschickten Budget
	 * bezahlt wurden
	 * 
	 * @param budget
	 *            zu welchem Budget sollen die Bestellungen gesucht werden
	 * @return ein Vector mit dem Ergebnis
	 */
	public Vector bestellungenZuBudget(String budget, long von, long bis) {

		daten.clear();
		row2 = new Vector();
		row3 = new Vector();
		row4 = new Vector();
		row5 = new Vector();
		Vector status = new Vector();
		int rechnungssumme = 0;

		try {
			if (budget.equals("Sonderbudget")) {
				query = "select * from bestellung where budget NOT like 'UT8' AND"
						+ " budget NOT like 'UT3' AND budget NOT like 'LMB1'"
						+ " AND budget NOT like 'LMB2' and datum>"
						+ von
						+ " and datum<" + bis;
			} else
				query = "select * from bestellung where budget like '" + budget
						+ "' and datum>" + von + " and datum<" + bis;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row2.add(rs.getInt("firma"));
				row3.add(rs.getInt("bestId"));
				row.add(rs.getInt("bestId"));
				row.add(rs.getString("budget"));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);
				row.add(datumS);
				row.add(rs.getString("wnummer"));
				row4.add(rs.getString("wnummer"));
				row.add(rs.getString("antragsteller"));
				status.add(rs.getInt("status"));
				row.add(getStatusLieferungZurNummer(rs.getInt("status")));
				row.add(getStatusBestellungZurNummer(rs.getInt("statusbez")));

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

			for (int i = 0; i < daten.size(); i++) {
				((Vector) daten.get(i)).add(getFirmennameZurID(Integer
						.parseInt(row2.get(i).toString())));
			}

			for (int i = 0; i < daten.size(); i++) {
				if (Integer.parseInt(status.get(i).toString()) != 15) {
					((Vector) daten.get(i)).add("€ "
							+ runde(summeBestellung(Integer.parseInt(row3
									.get(i).toString()), 0)));
				} else
					((Vector) daten.get(i)).add("€ -");

			}

			row2.clear();

			query = "select rechnungsbetrag,wnummer from rechnung";
			stmt = con.createStatement();
			rs2 = stmt.executeQuery(query);

			while (rs2.next()) {
				row2.add(rs2.getDouble("rechnungsbetrag"));
				row5.add(rs2.getString("wnummer"));
			}

			double best;
			double rech = 0;
			double dif;
			boolean yes = false;

			for (int i = 0; i < daten.size(); i++) {
				for (int j = 0; j < row5.size(); j++) {

					if (row4.get(i).toString().equals(row5.get(j).toString())) {

						rech += ((Double.parseDouble(row2.get(j).toString())));

						yes = true;
						continue;
					}
				}
				if (yes) {
					best = summeBestellung(Integer.parseInt(row3.get(i)
							.toString()), 0);
					dif = (rech - best);
					((Vector) daten.get(i)).add("€ " + runde(rech));
					((Vector) daten.get(i)).add("€ " + runde(dif));
					rech = 0;
				} else {
					((Vector) daten.get(i)).add("€ -");
					((Vector) daten.get(i)).add("€ -");

				}
				yes = false;

			}

			return daten;

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;

	}

	/**
	 * berechnet die Summe einer Bestellung
	 * 
	 * @param bestNr
	 *            die BestellID der Bestellung
	 * @param status
	 *            Status der Bestellung; bei gelöschten wird keine Summe
	 *            berechnet
	 * @return
	 */
	public double summeBestellung(int bestNr, int status) {
		double summe = 0;

		try {
			query = "select * from banf b, banfpos fp, bestpos bp"
					+ " where b.id=fp.banf" + " AND fp.banfposnr=bp.banfposnr"
					+ " AND bp.bestellId=" + bestNr;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				if (status != 15)// nicht gelöscht
					summe += (rs.getDouble("bp.preisInkl"))
							* (rs.getInt("bp.menge"));
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return summe;
	}

	/**
	 * Es werden Details zu einer bestimmten Bestellung ausgelesen
	 * 
	 * @param bestNr
	 *            die Bestellnummer, zu der die Details gesucht werden soll
	 * @return ein Vector mit dem Ergebnis
	 */
	public Vector detailsZuBestellung(int bestNr, long d) {

		daten.clear();
		row2 = new Vector();
		row3 = new Vector();
		row4 = new Vector();
		row5 = new Vector();

		try {
			query = "select * from banf b, banfpos fp, bestpos bp"
					+ " where b.id=fp.banf" + " AND fp.banfposnr=bp.banfposnr"
					+ " AND bp.bestellId=" + bestNr + " and datum>" + d;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row.add(rs.getInt("id"));
				row.add(rs.getString("antragsteller"));
				row.add(rs.getString("kostenstelle"));
				row5.add(rs.getInt("firma"));

				row2.add(row);
			}

			rs.close();
			stmt.close();

			query = "select * from banfpos fp, bestpos bp "
					+ "where bp.bestellId=" + bestNr + " AND"
					+ " fp.banfposnr=bp.banfposnr";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row.add(rs.getInt("menge"));
				row.add(rs.getInt("bp.preisInkl"));
				row.add(rs.getInt("bp.preisGesamt"));
				row.add(rs.getInt("bp.mwst"));
				row4.add(rs.getInt("bezeichnung"));

				row3.add(row);

			}

			rs.close();
			stmt.close();

			for (int i = 0; i < row3.size(); i++) {
				((Vector) row3.get(i)).add(0, getNameZurBezeichnung(Integer
						.parseInt(row4.get(i).toString())));
			}
			for (int i = 0; i < row3.size(); i++) {
				((Vector) row3.get(i)).add(1, getFirmennameZurID(Integer
						.parseInt(row5.get(i).toString())));
			}

			Vector hilf = new Vector();
			Vector hilf2 = new Vector();
			for (int i = 0; i < row2.size(); i++) {
				hilf = (Vector) row2.get(i);
				hilf.add(0, bestNr);
				hilf2 = (Vector) row3.get(i);

				for (int j = 0; j < ((Vector) row3.get(i)).size(); j++) {
					hilf.add(hilf2.get(j));
				}
				daten.add(hilf);
			}

			return daten;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Es werden alle Banfs zu einem bestimmten Antragsteller ausgelesen
	 * 
	 * @param lehrer
	 *            der Antragssteller, zu dem alle Banfs gesucht werden
	 * @return ein Vector mit dem Ergebnis
	 */
	public Vector banfAuflistungNachLehrer(String lehrer, long von, long bis) {

		daten.clear();
		row2 = new Vector();

		try {

			lehrer = lehrer.toLowerCase();

			query = "select * from banf where antragsteller like '" + lehrer
					+ "' and datum>" + von + " and datum<" + bis;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row2.add(rs.getInt("firma"));
				row.add(rs.getInt("id"));
				row.add(rs.getString("antragsteller"));
				row.add(rs.getString("kostenstelle"));
				row.add(getStatusBanfZurNummer(rs.getInt("status")));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);

				row.add(datumS);

				daten.add(row);
			}// end while

			rs.close();
			stmt.close();

			for (int i = 0; i < daten.size(); i++) {
				((Vector) daten.get(i)).add(getFirmennameZurID(Integer
						.parseInt(row2.get(i).toString())));
			}

			return daten;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Es werden alle Usernamen, die Zugriff auf die Datenbanken haben,
	 * ausgelesen
	 * 
	 * @return ein String-Vector mit dem Ergebnis
	 */
	public Vector getAntragsteller() {

		daten.clear();

		try {
			query = "select * from user order by cn;";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				daten.add(rs.getString("cn"));

			}

			rs.close();
			stmt.close();

			return daten;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Es werden alle existierenden Firmennamen ausgelesen
	 * 
	 * @return ein Vector mit dem Ergebnis
	 */
	public Vector getFirmennamen() {

		daten.clear();

		try {
			query = "select * from firma order by firmenname";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				daten.add(rs.getString("firmenname"));

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return daten;
	}

	/**
	 * liefert den Firmennamen zu einer bestimmten ID
	 * 
	 * @param nummer
	 *            FirmenID
	 * @return den Firmennamen
	 */
	public String getFirmennameZurID(int nummer) {
		String name = null;

		try {
			query = "select * from firma where id=" + nummer;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				name = rs.getString("firmenname");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return name;

	}

	/**
	 * Es wird eine Firmen-ID zu einem bestimmten Firmennamen gesucht
	 * 
	 * @param firma
	 *            der Firmenname, zu dem die Firmen-ID gesucht wird
	 * @return Firmen-ID
	 */
	public int getIDzuName(String firma) {
		int id = 0;

		try {
			query = "select * from firma where firmenname like'" + firma + "'";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				id = rs.getInt("id");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return id;

	}

	/**
	 * Es werden die gekauften Artikel zu einer bestimmten Firma ausgelesen
	 * 
	 * @param firma
	 *            die Firma, zu der die gekauften Artikel gesucht werden
	 * @return ein Vector mit dem Ergebnis
	 */
	public Vector getGekaufteArtikel(String firma, long von, long bis) {
		int id;
		id = getIDzuName(firma);
		daten.clear();
		row2 = new Vector();

		try {
			query = "select * from bestellung b, bestpos bp  where b.firma= "
					+ id
					+ " and b.bestID = bp.bestellID and b.statusbez=4  and datum>"
					+ von + " and datum<" + bis;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();
				row2.add(rs.getInt("bezeichnung"));
				row.add(rs.getString("menge"));
				row.add(rs.getString("preisGesamt") + " €");
				daten.add(row);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < daten.size(); i++) {
			((Vector) daten.get(i)).add(0, getNameZurBezeichnung(Integer
					.parseInt(row2.get(i).toString())));
		}

		return daten;

	}

	/**
	 * Zu einer bestimmten Materialnummer wird die Bezeichnung ausgelesen
	 * 
	 * @param bezeichnung
	 *            die Materialnummer, zu der man die Bezeichnung wissen moechte
	 * @return die Bezeichnung des Materials
	 */
	public String getNameZurBezeichnung(int bezeichnung) {
		String name = null;
		try {
			query = "select * from material where id=" + bezeichnung;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				name = rs.getString("Bezeichnung");

			}

			rs.close();
			stmt.close();

			return name;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return name;
	}

	/**
	 * Wandelt die Bezahlstatus-Nummer einer Bestellung oder einer
	 * Bestell-Position in einen passenden Text um
	 * 
	 * @param nummer
	 *            die Status-Nummer der Bestellung
	 * @return den Text "komplett bezahlt", "teilweise bezahlt", "nicht bezahlt"
	 *         oder "keine Angabe"
	 */
	public String getStatusBestellungZurNummer(int nummer) {
		switch (nummer) {
		case 4:
			return "komplett bezahlt";

		case 3:
			return "teilweise bezahlt";

		case 2:
			return "komplett geliefert";
		case 1:
			return "teilweise geliefert";
		}
		return "keine Lieferung";
	}

	/**
	 * liesst die Daten aller Rechnungen aus
	 * 
	 * @return Vector mit den Daten
	 */
	public Vector auslesenRechnungen() {
		daten.clear();
		query = "select * from rechnung";

		try {

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row.add(rs.getString("Nummer"));
				row.add(rs.getString("Rechnungsbetrag"));
				row.add(rs.getString("Bestellbetrag"));
				row.add(rs.getString("Skonto"));
				row.add(rs.getString("externeNummer"));
				row.add(rs.getString("interneNummer"));
				row.add(rs.getString("zahlart"));
				row.add(rs.getString("wNummer"));
				row.add(rs.getString("buchhaltungsbelege"));
				row.add(rs.getString("inventarnummer"));
				row.add(rs.getString("rechnungsstatus"));
				row.add(rs.getString("sonderabzug"));

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return daten;

	}

	/**
	 * Es werden die Daten einer Abteilung fuer eine Grafik ausgelesen
	 * 
	 * @param query
	 *            der SQL-Befehl, der ausgefuehrt werden soll
	 * @return ein Double-Array mit den ausgegebenen Betraege
	 */
	public double getDatenFuerGrafikAbteilung(String abteilung, long von,
			long bis, String budget) {

		double ausgegeben = 0;

		String query = "SELECT wNummer FROM bestellung b, bestpos besp, banfpos bp, banf ba WHERE b.bestId = besp.bestellId AND besp.banfposnr = bp.banfposnr AND bp.banf = ba.id AND ba.kostenstelle LIKE '"
				+ abteilung
				+ "' and b.budget like '"
				+ budget
				+ "' and b.datum>" + von + " and b.datum<" + bis;

		Vector<String> wNummerBestellung = new Vector<String>();
		Vector<String> wNummerRechnung = getWNummer();
		Vector<String> wNummerbezahlt = new Vector<String>();

		try {

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				wNummerBestellung.add(rs.getString("wNummer"));
			}

			rs.close();
			stmt.close();

			for (int i = 0; i < wNummerBestellung.size(); i++) {
				for (int j = 0; j < wNummerRechnung.size(); j++)
					if (wNummerBestellung.get(i).equals(wNummerRechnung.get(j)))
						wNummerbezahlt.add(wNummerBestellung.get(i));

			}

			for (int i = 0; i < wNummerbezahlt.size(); i++) {
				query = "select rechnungsbetrag,skonto,sonderabzug from rechnung where wNummer like '"
						+ wNummerbezahlt.get(i) + "'";

				stmt = con.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {

					double rechnungsbetrag = rs.getDouble("rechnungsbetrag");
					double sonderabzug = rs.getDouble("sonderabzug");
					double skontoProzent = rs.getDouble("skonto");

					double skonto = runde((rechnungsbetrag - sonderabzug)
							* (skontoProzent / 100));

					ausgegeben = ausgegeben + rechnungsbetrag - sonderabzug
							- skonto;

				}

				rs.close();
				stmt.close();

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return runde(ausgegeben);

	}

	/**
	 * Es werden die Daten fuer die Status-Berechnung ausgelesen
	 * 
	 * @param query
	 *            der SQL-Befehl, der ausgefuehrt werden soll
	 * @return ein Double-Array mit den ausgegebenen, gesperrten und geplanten
	 *         Betraege
	 */
	public double[] getDatenfuerStatus(String tabelleName, int kennung,
			String name, long von, long bis) {

		double[] werte = new double[3];

		werte[0] = runde(getDatenAusgegeben(name, kennung, von, bis));
		werte[1] = runde(getDatenGesperrt(name, kennung, von, bis));
		geplant = runde(getDatenGeplant(tabelleName, kennung, name));
		werte[2] = runde(geplant - werte[1] - werte[0]);

		return werte;

	}

	public Vector<String> getWNummer() {
		Vector<String> wNummer = new Vector<String>();

		String query = "select wNummer from rechnung";

		try {

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				wNummer.add(rs.getString("wNummer"));
			}

		} catch (SQLException e) {

			e.printStackTrace();

		}
		return wNummer;

	}

	public double getDatenGesperrt(String budget, int kennung, long von,
			long bis) {
		double gesperrt = 0;

		String query = "select wNummer from bestellung where budget like '"
				+ budget + "' and datum>" + von + " and datum<" + bis;

		Vector<String> wNummerBestellung = new Vector<String>();
		Vector<String> wNummerRechnung = getWNummer();
		Vector<String> wNummergesperrt = new Vector<String>();
		Vector<Integer> bestID = new Vector<Integer>();

		try {

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				wNummerBestellung.add(rs.getString("wNummer"));
			}

			rs.close();
			stmt.close();

			for (int i = 0; i < wNummerBestellung.size(); i++) {
				boolean uebereinstimmung = false;
				for (int j = 0; j < wNummerRechnung.size(); j++)
					if ((wNummerBestellung.get(i)
							.equals(wNummerRechnung.get(j)))) {
						uebereinstimmung = true;
					}
				if (!uebereinstimmung)
					wNummergesperrt.add(wNummerBestellung.get(i));

			}

			for (int i = 0; i < wNummergesperrt.size(); i++) {
				query = "select bestId from bestellung where wNummer like '"
						+ wNummergesperrt.get(i) + "'";

				stmt = conL.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {

					bestID.add(rs.getInt("bestId"));

				}

				rs.close();
				stmt.close();

				for (int j = 0; j < bestID.size(); j++) {
					query = "select preisGesamt from bestpos where bestellId="
							+ bestID.get(j);

					stmt = conL.createStatement();
					rs = stmt.executeQuery(query);

					while (rs.next()) {

						gesperrt = gesperrt + rs.getDouble("preisGesamt");

					}

					rs.close();
					stmt.close();

				}

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return gesperrt;

	}

	public double getDatenAusgegeben(String budget, int kennung, long von,
			long bis) {
		double ausgegeben = 0;

		String query = "select wNummer from bestellung where budget like '"
				+ budget + "' and datum>" + von + " and datum<" + bis;

		Vector<String> wNummerBestellung = new Vector<String>();
		Vector<String> wNummerRechnung = getWNummer();
		Vector<String> wNummerbezahlt = new Vector<String>();

		try {

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				wNummerBestellung.add(rs.getString("wNummer"));
			}

			rs.close();
			stmt.close();

			for (int i = 0; i < wNummerBestellung.size(); i++) {
				for (int j = 0; j < wNummerRechnung.size(); j++)
					if (wNummerBestellung.get(i).equals(wNummerRechnung.get(j)))
						wNummerbezahlt.add(wNummerBestellung.get(i));

			}

			for (int i = 0; i < wNummerbezahlt.size(); i++) {
				query = "select rechnungsbetrag,skonto,sonderabzug from rechnung where wNummer like '"
						+ wNummerbezahlt.get(i) + "'";

				stmt = con.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {

					double rechnungsbetrag = rs.getDouble("rechnungsbetrag");
					double sonderabzug = rs.getDouble("sonderabzug");
					double skontoProzent = rs.getDouble("skonto");

					double skonto = runde((rechnungsbetrag - sonderabzug)
							* (skontoProzent / 100));

					ausgegeben = ausgegeben + rechnungsbetrag - sonderabzug
							- skonto;

				}

				rs.close();
				stmt.close();

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return ausgegeben;

	}

	/**
	 * gibt den geplanten Betrag zurueck
	 * 
	 * @param tabelleName
	 *            Name der Tabelle
	 * @param kennung
	 *            Kennung, um welches LMB es sich handelt
	 * @return geplanter Betrag
	 */
	public double getDatenGeplant(String tabelleName, int kennung, String name) {
		double hilfGeplant;
		geplant = 0;

		if (kennung == 1 || kennung == 2) {
			if (kennung == 1)
				query = "select * from " + tabelleName + " where nummer=1";
			else
				query = "select * from " + tabelleName + " where nummer=2";

			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					geplant = rs.getDouble("geplant");

				}

				rs.close();
				stmt.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}
		} else if (kennung == 0) {
			query = "select * from " + tabelleName;

			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					hilfGeplant = rs.getDouble("geplant");
					geplant = geplant + hilfGeplant;
				}

				rs.close();
				stmt.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}
		} else {
			query = "select * from " + tabelleName + " where name like '"
					+ name + "'";

			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					hilfGeplant = rs.getDouble("geplant");
					geplant = geplant + hilfGeplant;
				}

				rs.close();
				stmt.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return runde(geplant);

	}

	/**
	 * gibt die Daten ueber zu zahlende Bestellungen zurueck
	 * 
	 * @param id
	 *            BestellID
	 * @return Vector mit den Daten
	 */
	public ResultSet zuBezahlen(int id) {
		daten.clear();
		/*
		 * row2 = new Vector(); row3 = new Vector(); row4 = new Vector();
		 */

		try {
			query = "select bestp.id,b.id,b.antragsteller,b.kostenstelle,m.bezeichnung, (bestp.geliefert-bestp.bezahlt) as zuBezahlen,bestp.menge as Gesamtmenge, bestp.status, bestp.statusbez,bestp.preisInkl,bestp.mwst,round((bestp.geliefert-bestp.bezahlt)*bestp.preisInkl,3) as preisGesamt,bestp.preisExkl from banf b, bestellung best, banfpos bp, bestpos bestp, material m where best.bestId= "
					+ id
					+ " and bestp.bestellId=best.bestId and bestp.banfposnr = bp.banfposnr and bp.banf = b.id and bp.bezeichnung = m.id and (bestp.status=3 or bestp.status=4 or bestp.status=6)and (bestp.statusbez=1 or bestp.statusbez=2 or bestp.statusbez=3) and ((bestp.geliefert-bestp.bezahlt)<>0)";

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * liefert die BestellID zu einer W-Nummer
	 * 
	 * @param bestID
	 *            BestellID
	 * @return W-Nummer
	 */
	public String getWNummerZuBestellID(int bestID) {

		String wNummer = "";

		try {
			query = "select * from bestellung where bestID= " + bestID;

			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				wNummer = rs.getString("wnummer");

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return wNummer;
	}

	/**
	 * liesst alle Namen der mitgeschickten Tabelle aus
	 * 
	 * @param tabellennamen
	 *            Name der Tabelle
	 * @return Namen der Eintraege der Tabelle
	 */
	public Vector auslesenUT8(String tabellennamen) {
		daten.clear();
		try {
			query = "select * from " + tabellennamen + " order by name";

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				daten.add(rs.getString("Name"));

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return daten;
	}

	/**
	 * liefert den Namen zu einer Nummer
	 * 
	 * @param name
	 *            Name der Kostenstelle
	 * @param tabellennamen
	 *            Name der Tabelle
	 * @return Nummer der Kostenstelle
	 */
	public int getNummerZuName(String name, String tabellennamen) {

		int nummer = 0;

		try {
			query = "select * from " + tabellennamen + " where name like '"
					+ name + "';";

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				nummer = rs.getInt("Nummer");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}

		return nummer;
	}

	/**
	 * liefert alle Daten der Banfs zu einer Kostenstelle
	 * 
	 * 
	 * @param kostenstelle
	 *            Name der Kostenstelle
	 * @return Daten der Banfs
	 */
	public Vector getBanfs(String kostenstelle, long von, long bis) {

		daten = new Vector();

		query = "select * from banf where kostenstelle like '" + kostenstelle
				+ "' and datum>" + von + " and datum<" + bis;

		row2 = new Vector();
		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row.add(rs.getString("antragsteller"));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);
				row.add(datumS);

				row2.add(rs.getString("firma"));
				row.add(getStatusBanfZurNummer(rs.getInt("status")));
				row.add(rs.getString("kostenstelle"));

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		for (int i = 0; i < daten.size(); i++) {
			((Vector) daten.get(i)).add(getFirmennameZurID(Integer
					.parseInt(row2.get(i).toString())));
		}
		return daten;
	}

	/**
	 * gibt die Namen der Kostenstellen eines Hauptbereiches, Bereiches oder
	 * einer Hauptkostenstelle zurueck
	 * 
	 * @param ausgewaehlt
	 *            welcher Hauptbereich, Bereich oder Hauptkostenstelle
	 *            ausgewaehlt wurde
	 * @param welcheStufe
	 *            gibt Auskunft, ob man die Kostenstellen eines Hauptbereiches,
	 *            Bereiches oder Hauptkostenstelle moechte
	 * @return Vector mit den Namen der Kostenstellen
	 */
	public Vector getKostenstelle(String ausgewaehlt, int welcheStufe) {
		daten.clear();
		int nummer;
		String tabellennamen = "";

		switch (welcheStufe) {
		case 1:
			tabellennamen = "hauptbereichut8";
			break;
		case 2:
			tabellennamen = "bereichut8";
			break;
		case 3:
			tabellennamen = "hauptkostenstelleut8";

		}
		nummer = getNummerZuName(ausgewaehlt, tabellennamen);
		try {
			{
				if (welcheStufe == 1)
					query = "select * from  kostenstelleut8 k,hauptkostenstelleut8 hk, bereichut8 b where b.hauptnummer="
							+ nummer
							+ " AND hk.hauptnummer=b.nummer AND k.hauptnummer=hk.nummer";
			}
			if (welcheStufe == 2) {
				query = "select * from  kostenstelleut8 k,hauptkostenstelleut8 hk where hk.hauptnummer="
						+ nummer + " AND k.hauptnummer=hk.nummer";
			}
			if (welcheStufe == 3) {
				query = "select * from kostenstelleut8  where hauptnummer="
						+ nummer;
			}

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				daten.add(rs.getString("Name"));

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return daten;
	}

	/**
	 * liefert den Umsatz aller Bestellungen von einer bestimmten Firma
	 * 
	 * @param firma
	 *            Name der Firma
	 * @return Umsatz
	 */
	public double getUmsatzJeFirma(String firma, long von, long bis) {
		double umsatzFirma = 0;
		Vector<String> wNummer = new Vector<String>();
		double skonto;
		double betrag;
		double sonderabzug;
		int id;
		id = getIDzuName(firma);
		try {

			query = "select * from bestellung where firma= " + id
					+ " and datum>" + von + " and datum<" + bis;
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				wNummer.add(rs.getString("wnummer"));

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < wNummer.size(); i++) {
			query = "select * from rechnung where wNummer like '"
					+ wNummer.get(i) + "'";

			try {

				stmt = con.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {

					skonto = rs.getDouble("Skonto");
					betrag = rs.getDouble("Rechnungsbetrag");
					sonderabzug = rs.getDouble("Sonderabzug");

					umsatzFirma = umsatzFirma
							+ runde(betrag - sonderabzug
									- ((betrag - sonderabzug) * skonto / 100));
				}

				rs.close();
				stmt.close();

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return umsatzFirma;
	}

	/**
	 * gibt die zugehoerigen Nummern zu Namen zurueck
	 * 
	 * @param eintraege
	 *            Namen
	 * @return Vector mit den Daten
	 */
	public Vector getNummernVonEintraegen(Vector eintraege) {
		daten.clear();
		String[] tabellen = { "hauptbereichut8", "bereichut8",
				"hauptkostenstelleut8", "kostenstelleut8" };

		try {
			for (int i = 0; i < eintraege.size(); i++) {

				query = "select * from " + tabellen[i] + " where name like '"
						+ eintraege.get(i).toString() + "';";

				stmt = con.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					daten.add(rs.getInt("Nummer"));
				}

				rs.close();
				stmt.close();
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		return daten;

	}

	/**
	 * liefert einen Datensatz zu einer bestimmten Nummer
	 * 
	 * @param zeilennummer
	 *            die Nummer
	 * @param kennnummer
	 *            Kennnummer der Tabelle
	 * @param nummer
	 *            0: man will den Namen aendern; 1: man will die Raumnummer
	 *            aendern; 2: man will die Kurzbezeichnung ändern; 3: man will
	 *            die nummerSelbst
	 * @return den aktuellen Namen / Raumnummer aus der Datenbank
	 */
	public String getEintragZuNummer(int zeilennummer, int kennnummer,
			int nummer) {
		String namebearbeiten = "";

		String tabellennamen = getNameZuKennnummer(kennnummer);
		query = "select * from " + tabellennamen + " where nummer="
				+ zeilennummer;

		try {

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				if (nummer == 0)
					namebearbeiten = rs.getString("name");
				else if (nummer == 1)
					namebearbeiten = rs.getString("raumnummer");
				else if (nummer == 2)
					namebearbeiten = rs.getString("kurzbezeichn");
				else if (nummer == 3)
					namebearbeiten = rs.getString("nummerSelbst");

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return namebearbeiten;

	}

	public String[] getEintraegeZuProjekt(int zeilennummer) {

		String[] eintraege = new String[8];
		query = "select * from projekt where nummerSelbst=" + zeilennummer;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				for (int i = 0; i < eintraege.length; i++) {
					eintraege[i] = rs.getString(i + 2);
				}

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return eintraege;

	}

	/**
	 * man bekommt den Tabellennamen zu einer Kennnummer
	 * 
	 * @param kennnummer
	 */
	public String getNameZuKennnummer(int kennnummer) {
		String tabellennamen = "";

		switch (kennnummer) {
		case 1:
			tabellennamen = "abteilungut3";
			break;
		case 2:
			tabellennamen = "projekt";
			break;
		case 3:
			tabellennamen = "lmb";
			break;
		case 4:
			tabellennamen = "sonderbudget";
			break;
		case 5:
			tabellennamen = "hauptbereichut8";
			break;
		case 6:
			tabellennamen = "bereichut8";
			break;
		case 7:
			tabellennamen = "hauptkostenstelleut8";
			break;
		case 8:
			tabellennamen = "kostenstelleut8";
			break;
		case 9:
			tabellennamen = "lmb";

		}

		return tabellennamen;

	}

	/**
	 * liefert Daten von einem bestimmten Projekt, von dem die NummerSelbst
	 * bekannt ist
	 * 
	 * @param zeilennummer
	 *            selbst eingegebe Nummer
	 * @return Daten vom Projekt
	 */
	public String[] getEintragZuProjekt(int zeilennummer) {

		String[] projekt = new String[8];

		query = "select * from projekt where NummerSelbst=" + zeilennummer;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				projekt[0] = rs.getString("nummerSelbst");
				projekt[1] = rs.getString("name");
				projekt[2] = rs.getString("lehrer");
				projekt[3] = rs.getString("kurzz");
				projekt[4] = rs.getString("klasse");
				projekt[5] = rs.getString("datum");
				projekt[6] = rs.getString("teilnehmer");
				projekt[7] = rs.getString("abteilung");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projekt;

	}

	/**
	 * gibt alle Abteilungesnamen zurueck
	 * 
	 * @return Vector mit den Abteilungsnamen
	 */
	public Vector getAbteilungen() {
		query = "select name from abteilungut3 where nummer<>1 union select name from lmb where nummer>2";

		row = new Vector();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				row.add(rs.getString("name"));

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row;

	}

	public Vector getProjekte() {
		query = "select name from projekt;";

		row = new Vector();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				row.add(rs.getString("name"));

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row;

	}

	/**
	 * liesst die Anteile der Budgets von einem bestimmten Projekt aus
	 * 
	 * @param projektname
	 *            Name des Projektes
	 * @return Vector mit den Daten
	 */
	public Vector auslesenAnteile(String projektname) {
		daten.clear();
		row3 = new Vector();
		String wnummer = "";
		double anteilUt8 = 0;
		double anteilUt3 = 0;
		double anteilLmb1 = 0;
		double anteilLmb2 = 0;
		double anteilSonder = 0;
		double summe = 0;
		double prozentUt8 = 0;
		double prozentUt3 = 0;
		double prozentLmb1 = 0;
		double prozentLmb2 = 0;
		double prozentSonder = 0;
		double skonto = 0;
		double betrag = 0;
		double sonderabzug = 0;

		query = "select * from rechnung;";

		row = new Vector();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row.add(rs.getString("wNummer"));
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < row.size(); i++) {
			wnummer = row.get(i).toString();

			query = "select * from bestellung b,bestpos bp,banfpos fp,banf f"
					+ " where b.wnummer like '" + wnummer + "' AND"
					+ " b.bestId=bp.bestellId AND"
					+ " bp.banfposnr=fp.banfposnr AND" + " fp.banf=f.id AND"
					+ " f.kostenstelle like '" + projektname + "';";

			row2 = new Vector();

			try {
				stmt = conL.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {

					row2.add(rs.getString("b.wnummer"));
					row2.add(rs.getString("b.budget"));
				}

				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			row3.add(row2);
		}

		for (int i = 0; i < row3.size(); i++) {
			row2 = new Vector();
			row2 = (Vector) row3.get(i);

			if (row2.size() != 0) {
				query = "select * from rechnung where wNummer like '"
						+ row2.get(0).toString() + "';";

				try {
					stmt = con.createStatement();
					rs = stmt.executeQuery(query);

					while (rs.next()) {

						skonto = rs.getDouble("Skonto");
						betrag = rs.getDouble("Rechnungsbetrag");
						sonderabzug = rs.getDouble("Sonderabzug");

						if (row2.get(1).toString().equals("UT8")) {
							anteilUt8 += runde(betrag - sonderabzug
									- ((betrag - sonderabzug) * skonto / 100));
						} else if (row2.get(1).toString().equals("UT3")) {
							anteilUt3 += runde(betrag - sonderabzug
									- ((betrag - sonderabzug) * skonto / 100));
						} else if (row2.get(1).toString().equals("LMB1")) {
							anteilLmb1 += runde(betrag - sonderabzug
									- ((betrag - sonderabzug) * skonto / 100));
						} else if (row2.get(1).toString().equals("LMB1_2011")) {
							anteilLmb2 += runde(betrag - sonderabzug
									- ((betrag - sonderabzug) * skonto / 100));
						} else {
							anteilSonder += runde(betrag - sonderabzug
									- ((betrag - sonderabzug) * skonto / 100));
						}
					}// end while

					rs.close();
					stmt.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}// end for

		summe = runde(anteilUt8 + anteilUt3 + anteilLmb1 + anteilLmb2
				+ anteilSonder);
		prozentUt8 = runde(anteilUt8 / (summe / 100));
		prozentUt3 = runde(anteilUt3 / (summe / 100));
		prozentLmb1 = runde(anteilLmb1 / (summe / 100));
		prozentLmb2 = runde(anteilLmb2 / (summe / 100));
		prozentSonder = runde(anteilSonder / (summe / 100));

		row2.clear();
		row2.add(anteilUt8);
		row2.add(prozentUt8);
		row2.add(anteilUt3);
		row2.add(prozentUt3);
		row2.add(anteilLmb1);
		row2.add(prozentLmb1);
		row2.add(anteilLmb2);
		row2.add(prozentLmb2);
		row2.add(anteilSonder);
		row2.add(prozentSonder);

		String[] budgets = { "UT8", "UT3", "LMB1", "LMB1_2011", "Sonderbudget" };
		int j = 0;

		for (int i = 0; i < 10; i += 2) {
			row3 = new Vector();
			row3.add(budgets[j]);

			if (Double.doubleToLongBits(Double.parseDouble(row2.get(i)
					.toString())) == Double.doubleToLongBits(Double.NaN))
				row3.add("Kein Wert");
			else
				row3.add(row2.get(i));

			if (Double.doubleToLongBits(Double.parseDouble(row2.get(i + 1)
					.toString())) == Double.doubleToLongBits(Double.NaN))
				row3.add("Kein Wert");
			else
				row3.add(row2.get(i + 1));

			daten.add(row3);

			j++;
		}

		return daten;

	}// end auslesenAnteile

	/**
	 * liefert den Namen zu einer bestimmten Zeile aus einer bestimmten Tabelle
	 * 
	 * @param zeile
	 *            die betroffene Zeile
	 * @param tabelle
	 *            die betroffene Tabelle
	 * @return der gesuchte Name
	 */
	public String auslesenName(int zeile, String tabelle) {
		daten.clear();
		String name = "";

		query = "select * from " + tabelle + " where nummer=" + zeile;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				name = rs.getString("Name");
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return name;
	}

	/**
	 * Wandelt die Lieferstatus-Nummer einer Bestellung in einen passenden Text
	 * um
	 * 
	 * @param nummer
	 *            die Status-Nummer der Lieferung
	 * @return den Text, wie geliefert wurde
	 */
	public String getStatusLieferungZurNummer(int nummer) {

		if (nummer == 1)
			return "nicht abgeschickt";
		else if (nummer == 3)
			return "abgeschickt";
		else if (nummer == 4)
			return "teilweise falsch/nicht geliefert";
		else if (nummer == 5)
			return "teilweise richtig geliefert";
		else if (nummer == 6)
			return "komplett - Teile nicht lieferbar";
		else if (nummer == 15)
			return "gelöscht";
		else
			return "richtig geliefert";

	}

	/**
	 * Wandelt die Status-Nummer einer Banf-Position in Text um
	 * 
	 * @param nummer
	 *            die Status-Nummer der Banf-Position
	 * @return den Text, ob schon bestellt wurde oder nicht
	 */
	public String getStatusBanfPosZurNummer(int nummer) {
		if (nummer == 1)
			return "nicht bestellt";
		else if (nummer == 2)
			return "bestellt";
		else
			return "abgewiesen";
	}

	/**
	 * Wandelt die Status-Nummer einer Banf in Text um
	 * 
	 * @param nummer
	 *            die Status-Nummer der Banf
	 * @return den Text, ob schon bestellt wurde oder nicht
	 */
	public String getStatusBanfZurNummer(int nummer) {
		if (nummer == 3)
			return "nicht bestellt";
		else if (nummer == 4)
			return "in Bearbeitung";
		else if (nummer == 5)
			return "fertig";
		else
			return "abgewiesen";
	}

	/**
	 * Wandelt die Lieferstatus-Nummer einer Bestell-Position in Text um
	 * 
	 * @param nummer
	 *            die Status-Nummer der Bestell-Position
	 * @return den Text, ob die Position schon verschickt wurde oder ob sie
	 *         schon geliefert wurde
	 */
	public String getStatusBestellPosZurNummer(int nummer) {
		if (nummer == 1)
			return "nicht abgeschickt";
		else if (nummer == 2)
			return "abgeschickt";
		else if (nummer == 3)
			return "richtig geliefert";
		else if (nummer == 4)
			return "abweichend geliefert";
		else if (nummer == 5)
			return "nicht lieferbar";
		else
			return "komplett - Teile nicht lieferbar";
	}

	/**
	 * liefert die Bestellposition zu einer BanfID
	 * 
	 * @param banfID
	 *            BanfID
	 * @return Bestellpositonsnummer
	 */
	public int getBestPosNrZuBanf(int banfID) {
		int bestpos = 0;

		query = "select * from bestpos bp, banfpos fp, banf f "
				+ "where bp.banfposnr=fp.banfposnr AND fp.banf=" + banfID;

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				bestpos = rs.getInt("bp.id");
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}

		return bestpos;
	}

	/**
	 * ABC-Analyse nach Artikel
	 * 
	 * @return ein Vector mit dem Ergebnis
	 */
	public Vector getABCArtikel() {

		// liesst alle Materialnummern aus
		Vector<Integer> artikelnummer = new Vector<Integer>();

		// liesst alle Preise von einem bestimmten Material aus
		Vector<Double> summe = new Vector<Double>();
		Vector<Double> menge = new Vector<Double>();

		query = "select bezeichnung, sum(preisGesamt),sum(menge) from bestellung b, bestpos bp where bp.statusbez=4 and bp.status !=15 and bp.bestellid= b.bestId group by bezeichnung order by sum(preisGesamt) desc";

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				artikelnummer.add(rs.getInt("bezeichnung"));
				summe.add(rs.getDouble("sum(preisGesamt)"));
				menge.add(rs.getDouble("sum(menge)"));

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		double gesamtsummeMenge = 0;
		for (int i = 0; i < menge.size(); i++)
			gesamtsummeMenge = gesamtsummeMenge + menge.get(i);

		double gesamtsumme = 0;
		for (int i = 0; i < summe.size(); i++)
			gesamtsumme = gesamtsumme + summe.get(i);

		double[][] prozentwerte = new double[artikelnummer.size()][2];

		// rechnet die Prozentwerte aus
		for (int j = 0; j < artikelnummer.size(); j++) {
			prozentwerte[j][0] = runde((summe.get(j) * 100 / gesamtsumme));
			prozentwerte[j][1] = runde((menge.get(j) * 100 / gesamtsummeMenge));
		}

		if (prozentwerte.length == 0)
			System.out.println("KEINE DATEN");

		else

		{

			double[][] kumuliert = new double[prozentwerte.length][2];
			// kummulieren

			kumuliert[0][0] = prozentwerte[0][0];
			kumuliert[0][1] = prozentwerte[0][1];

			for (int k = 0; k < kumuliert.length - 1; k++) {
				kumuliert[k + 1][0] = prozentwerte[k + 1][0] + kumuliert[k][0];
				kumuliert[k + 1][1] = prozentwerte[k + 1][1] + kumuliert[k][1];

			}

			daten = new Vector();

			boolean istA = false;

			for (int i = 0; i < kumuliert.length; i++) {

				row = new Vector();
				if (kumuliert[i][0] < 80 && kumuliert[i][1] < 20) {
					row.add("A");
					istA = true;
				} else if (kumuliert[i][0] < 95 && kumuliert[i][1] < 40) {

					if (i == 0 && !istA)
						row.add("A");
					else
						row.add("B");

				} else {
					if (i == 0 && !istA)
						row.add("A");
					else

						row.add("C");
				}

				row.add(getArtikelzuId(artikelnummer.get(i)));
				row.add(runde(summe.get(i)) + " €");
				row.add(menge.get(i));

				daten.add(row);

			}

			return daten;
		}

		return null;
	}

	/**
	 * Sortierverfahren
	 * 
	 * @param a
	 *            double[][] zum Sortieren
	 * @return a sortiertes double[][]
	 */
	public double[][] insertionSort(double[][] a) {
		for (int i = 1; i < a.length; i++) {
			double hilf = a[i][0];
			double hilf2 = a[i][1];
			int j = i - 1;

			while ((j >= 0) && (a[j][0] < hilf)) {
				a[j + 1][0] = a[j][0];
				a[j + 1][1] = a[j + 1][1];
				j--;
			}
			a[j + 1][0] = hilf;
			a[j + 1][1] = hilf2;
		}

		return a;
	}

	/**
	 * ABC-Analyse nach Firmen
	 * 
	 * @return Vector mit dem Ergebnis
	 */
	public Vector getABCFirma() {

		// liesst alle Materialnummern aus
		Vector<Integer> firmennummer = new Vector<Integer>();

		// liesst alle Preise von einem bestimmten Material aus
		Vector<Double> summe = new Vector<Double>();
		Vector<Double> menge = new Vector<Double>();

		query = "select firma, sum(preisGesamt),sum(menge) from bestellung b, bestpos bp where bp.statusbez=4 and bp.status !=15 and bp.bestellid= b.bestId group by firma order by sum(preisGesamt) desc";

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				firmennummer.add(rs.getInt("firma"));
				summe.add(rs.getDouble("sum(preisGesamt)"));
				menge.add(rs.getDouble("sum(menge)"));

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		double gesamtsummeMenge = 0;
		for (int i = 0; i < menge.size(); i++)
			gesamtsummeMenge = gesamtsummeMenge + menge.get(i);

		double gesamtsumme = 0;
		for (int i = 0; i < summe.size(); i++)
			gesamtsumme = gesamtsumme + summe.get(i);

		double[][] prozentwerte = new double[firmennummer.size()][2];

		// rechnet die Prozentwerte aus
		for (int j = 0; j < firmennummer.size(); j++) {
			prozentwerte[j][0] = runde((summe.get(j) * 100 / gesamtsumme));
			prozentwerte[j][1] = runde((menge.get(j) * 100 / gesamtsummeMenge));
		}

		if (prozentwerte.length == 0)
			System.out.println("KEINE DATEN");

		else

		{

			double[][] kumuliert = new double[prozentwerte.length][2];
			// kummulieren

			kumuliert[0][0] = prozentwerte[0][0];
			kumuliert[0][1] = prozentwerte[0][1];

			for (int k = 0; k < kumuliert.length - 1; k++) {
				kumuliert[k + 1][0] = prozentwerte[k + 1][0] + kumuliert[k][0];
				kumuliert[k + 1][1] = prozentwerte[k + 1][1] + kumuliert[k][1];

			}

			daten = new Vector();
			boolean istA = false;

			for (int i = 0; i < kumuliert.length; i++) {

				row = new Vector();
				if (kumuliert[i][0] < 80 && kumuliert[i][1] < 20) {
					row.add("A");
					istA = true;
				} else if (kumuliert[i][0] < 95 && kumuliert[i][1] < 40) {

					if (i == 0 && !istA)
						row.add("A");
					else
						row.add("B");

				} else {
					if (i == 0 && !istA)
						row.add("A");
					else

						row.add("C");
				}

				row.add(getFirmennameZurID(firmennummer.get(i)));
				row.add(runde(summe.get(i)) + " €");
				row.add(menge.get(i));

				daten.add(row);

			}

			return daten;
		}

		return null;
	}

	/**
	 * liest die Artikelbezeichnung zu einer Materialnummer aus
	 * 
	 * @param id
	 *            die Materialnummer
	 * @return die Bezeichnung des Artikels
	 */
	public String getArtikelzuId(int id) {
		String name = "";
		query = "select bezeichnung from material where id=" + id;

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				name = rs.getString("bezeichnung");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return name;

	}

	/**
	 * prueft, ob es bereits eine Rechnung mit der W-Nummer gibt
	 * 
	 * @param wNummer
	 *            W-Nummmer, die ueberprueft wird
	 * @return true - wenn es bereits eine Rechnung gibt; false - wenn es noch
	 *         keine Rechnung gibt
	 */
	public boolean gibtRechnung(String wNummer) {
		query = "select * from rechnung where wNummer like '" + wNummer + "'";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				return true;

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * liefert alle Sonderbudgets
	 * 
	 * @return Vector mit den Namen der Sonderbudgets
	 */
	public Vector getSonderbudgets() {
		daten = new Vector();
		query = "select * from sonderbudget";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				daten.add(rs.getString("Name"));

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return daten;

	}

	/**
	 * liefert die Bestellungen, bei denen das Budget ein bestimmtes
	 * Sonderbudget ist
	 * 
	 * @param sonderbudget
	 *            Name des Sonderbudgets
	 * @return Vector mit den Daten der Bestellungen
	 */
	public Vector getBestellungZuSonderbudget(String sonderbudget, long von,
			long bis) {
		daten.clear();
		row = new Vector();
		row2 = new Vector();
		row3 = new Vector();
		row4 = new Vector();
		row5 = new Vector();

		Vector status = new Vector();

		query = "select * from bestellung where budget like '" + sonderbudget
				+ "' and datum>" + von + " and datum<" + bis;

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row2.add(rs.getInt("firma"));
				row3.add(rs.getInt("bestId"));
				row.add(rs.getInt("bestId"));
				row.add(rs.getString("budget"));
				datum = rs.getLong("datum");
				date = new Date(datum * 1000);
				datumS = "" + (date.getDate()) + "." + (date.getMonth() + 1)
						+ "." + (date.getYear() + 1900);
				row.add(datumS);
				row.add(rs.getString("wnummer"));
				row4.add(rs.getString("wnummer"));
				row.add(rs.getString("antragsteller"));
				status.add(rs.getInt("status"));
				row.add(getStatusLieferungZurNummer(rs.getInt("status")));
				row.add(getStatusBestellungZurNummer(rs.getInt("statusbez")));

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

			for (int i = 0; i < daten.size(); i++) {
				((Vector) daten.get(i)).add(getFirmennameZurID(Integer
						.parseInt(row2.get(i).toString())));
			}

			for (int i = 0; i < daten.size(); i++) {
				if (Integer.parseInt(status.get(i).toString()) != 15) {
					((Vector) daten.get(i)).add("€ "
							+ runde(summeBestellung(Integer.parseInt(row3
									.get(i).toString()), 0)));
				} else
					((Vector) daten.get(i)).add("€ -");

			}

			row2.clear();

			query = "select rechnungsbetrag,wnummer from rechnung";
			stmt = con.createStatement();
			rs2 = stmt.executeQuery(query);

			while (rs2.next()) {
				row2.add(rs2.getDouble("rechnungsbetrag"));
				row5.add(rs2.getString("wnummer"));
			}

			double best;
			double rech = 0;
			double dif;
			boolean yes = false;

			for (int i = 0; i < daten.size(); i++) {
				for (int j = 0; j < row5.size(); j++) {

					if (row4.get(i).toString().equals(row5.get(j).toString())) {

						rech += ((Double.parseDouble(row2.get(j).toString())));

						yes = true;
						continue;
					}
				}
				if (yes) {
					best = summeBestellung(Integer.parseInt(row3.get(i)
							.toString()), 0);
					dif = (rech - best);
					((Vector) daten.get(i)).add("€ " + runde(rech));
					((Vector) daten.get(i)).add("€ " + runde(dif));
					rech = 0;
				} else {
					((Vector) daten.get(i)).add("€ -");
					((Vector) daten.get(i)).add("€ -");

				}
				yes = false;

			}

			return daten;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;

	}

	/**
	 * liefert einen Vector mit den Bezahlstati der mitgeschickten
	 * Bestellpositionen
	 * 
	 * @param bestPosIds
	 *            die IDs der Bestellpositionen, zu denen man den Bezahlstatus
	 *            will
	 * @return einen Vector mit den Bezahlstati
	 */
	public Vector bezahlteBestPos(Vector bestPosIds) {
		daten = new Vector();

		for (int i = 0; i < bestPosIds.size(); i++) {

			query = "select * from bestpos where id="
					+ Integer.parseInt(bestPosIds.get(i).toString());

			try {
				stmt = conL.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					daten.add(rs.getInt("statusbez"));
				}

				rs.close();
				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return daten;

	}

	/**
	 * liefert den geplanten Betrag zu einer bestimmten Kennnummer
	 * 
	 * @param kennummer
	 *            Kennnummer der Tabelle
	 * @return geplanter Betrag
	 */
	public double getHauptbudget(int kennummer) {
		double budget = 0;
		if (kennummer == 3)
			query = "select * from lmb where nummer=1 and kennung=1";
		else if (kennummer == 9)
			query = "select * from lmb where nummer=2 and kennung=2";
		else
			query = "select * from abteilungut3 where nummer=1";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				budget = rs.getDouble("geplant");

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return budget;
	}

	/**
	 * liefert den EDVHAT Anteil in Prozent
	 * 
	 * @return double[] mit den beiden Werten
	 */
	public double[] getEDVHATAnteil() {

		double[] werte = new double[2];
		String anteile = "";
		String[] split;
		// die beiden Werte sind in der Spalte EDVHAT Anteil gespeichert
		// (edv,hat)
		query = "select * from abteilungut3 where nummer=1";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				anteile = rs.getString("EDVHATAnteil");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!anteile.equals("0")) {
			// aufsplitten des Strings
			split = anteile.split(",");

			werte[0] = Double.parseDouble(split[0]);
			werte[1] = Double.parseDouble(split[1]);
		}
		return werte;

	}

	/**
	 * gibt den LT- Anteile in Prozent zurueck
	 * 
	 * @param kennummer
	 *            Kennnummer der Tabelle
	 * @return Lt- Anteil
	 */
	public double getLTAnteil(int kennummer) {
		double anteil = 0;

		if (kennummer == 9)
			query = "select * from lmb where nummer=2";
		else
			query = "select * from lmb where nummer=1";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				anteil = rs.getDouble("festgeplant");

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return anteil;

	}

	/**
	 * liefert die eindeutige Nummer zu einer selbst eingegebenen Nummer
	 * 
	 * @param nummerSelbst
	 *            die selbst eingegebene Nummer
	 * @param kostenstelle
	 *            Name der Kostenstelle
	 * @return die eindeutige Nummer
	 */
	public int getNummerZuSelbstNummer(String nummerSelbst, String kostenstelle) {
		int nummer = 0;
		query = "select * from hauptbereichut8 where nummerSelbst like'"
				+ nummerSelbst + "' and name like'" + kostenstelle + "'";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				nummer = rs.getInt("Nummer");

			}

			rs.close();

			query = "select * from bereichut8 where nummerSelbst like'"
					+ nummerSelbst + "' and name like'" + kostenstelle + "'";

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				nummer = rs.getInt("Nummer");

			}

			rs.close();

			query = "select * from hauptkostenstelleut8 where nummerSelbst like'"
					+ nummerSelbst + "' and name like'" + kostenstelle + "'";

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				nummer = rs.getInt("Nummer");

			}

			rs.close();

			query = "select * from kostenstelleut8 where nummerSelbst like'"
					+ nummerSelbst + "' and name like'" + kostenstelle + "'";

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				nummer = rs.getInt("Nummer");

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return nummer;
	}

	/**
	 * liesst alle heurigen Rechnungen aus der Tabelle rechnung aus
	 * 
	 * @return Vector mit allen Rechnungsdaten
	 */
	public Vector getRechnungenHeuer(long jahr) {

		daten.clear();
		int rechnungsstatus;
		double rechnungsb;
		double skonto;
		double sonderabzug;

		if (jahr < 10)
			query = "select * from rechnung where wNummer like '%-0" + jahr
					+ "-%' or wNummer like '%-0" + jahr + "'";
		else
			query = "select * from rechnung where wNummer like '%-" + jahr
					+ "-%' or wNummer like '%-" + jahr + "'";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row.add(rs.getString("wNummer"));
				row.add(rs.getString("Bestellbetrag"));
				row.add(rs.getString("Rechnungsbetrag"));
				rechnungsb = rs.getDouble("Rechnungsbetrag");
				row.add(rs.getString("externeNummer"));
				row.add(rs.getString("interneNummer"));
				row.add(rs.getString("Inventarnummer"));
				row.add(rs.getString("Buchhaltungsbelege"));
				row.add(rs.getString("Sonderabzug"));
				row.add(rs.getString("Skonto"));
				skonto = rs.getDouble("Skonto");
				sonderabzug = rs.getDouble("Sonderabzug");
				row.add(rs.getString("Zahlart"));
				row.add(runde((rechnungsb - sonderabzug)
						- ((rechnungsb - sonderabzug) * skonto / 100)));
				rechnungsstatus = rs.getInt("Rechnungsstatus");
				if (rechnungsstatus == 1)
					row.add("Teilrechnung");
				else
					row.add("Gesamtrechnung");

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return daten;
	}

	/**
	 * liesst alle heurigen Rechnungen aus der Tabelle rechnung aus
	 * 
	 * @return Vector mit allen Rechnungsdaten
	 */
	public Vector getRechnungen() {

		daten.clear();
		int rechnungsstatus;
		double rechnungsb;
		double skonto;
		double sonderabzug;

		query = "select * from rechnung";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				row = new Vector();

				row.add(rs.getString("wNummer"));
				row.add(rs.getString("Bestellbetrag"));
				row.add(rs.getString("Rechnungsbetrag"));
				rechnungsb = rs.getDouble("Rechnungsbetrag");
				row.add(rs.getString("externeNummer"));
				row.add(rs.getString("interneNummer"));
				row.add(rs.getString("Inventarnummer"));
				row.add(rs.getString("Buchhaltungsbelege"));
				row.add(rs.getString("Sonderabzug"));
				row.add(rs.getString("Skonto"));
				skonto = rs.getDouble("Skonto");
				sonderabzug = rs.getDouble("Sonderabzug");
				row.add(rs.getString("Zahlart"));
				row.add(runde((rechnungsb - sonderabzug)
						- ((rechnungsb - sonderabzug) * skonto / 100)));
				rechnungsstatus = rs.getInt("Rechnungsstatus");
				if (rechnungsstatus == 1)
					row.add("Teilrechnung");
				else
					row.add("Gesamtrechnung");

				daten.add(row);

			}// end while

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return daten;
	}

	/**
	 * liesst das geplante Budget zu einer bestimmten Nummer in einer bestimmten
	 * Tabelle aus
	 * 
	 * @param tabelle
	 *            die betroffene Tabelle
	 * @param nummer
	 *            die betroffene Nummer
	 * @return double-Wert; das gesperrte Budget
	 */
	public double getGesperrtesBudget(String tabelle, int nummer) {
		daten.clear();
		row = new Vector();
		double gesperrt = 0;

		query = "select * from " + tabelle + " where nummer=" + nummer;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				gesperrt = rs.getDouble("gesperrt");
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gesperrt;
	}

	/**
	 * man bekommt die Anzahl der Datensaetze in einer Tabelle
	 * 
	 * @param tabelle
	 *            die betroffene Tabelle
	 * @return die Anzahl der Datensaetze
	 */
	public int getAnzahlDatensaetze(String tabelle) {
		int anzahl = 0;

		query = "select count(*) from " + tabelle;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				anzahl = rs.getInt(1);
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return anzahl;
	}

	public Vector<Integer> getNummernVonDatensaetze(String tabelleName) {
		query = "select * from " + tabelleName;

		Vector<Integer> nummern = new Vector<Integer>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				nummern.add(rs.getInt("Nummer"));

			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return nummern;

	}

	/**
	 * rundet eine Zahl auf 2 Kommastellen
	 * 
	 * @param zahl
	 *            die zu rundende Zahl
	 * @return die gerundete Zahl
	 */
	public double runde(double zahl) {
		zahl = zahl * 100;
		zahl = Math.round(zahl);
		zahl = zahl / 100;
		return zahl;

	}

	/**
	 * prueft anhand des Bezahlstatus einer Bestellposition, ob sie lieferbar
	 * ist oder nicht
	 * 
	 * @param bestPosIds
	 *            die IDs der Bestellpositionen, die ueberprueft werden sollen
	 * @return ein Vector mit true und false; true: lieferbar; false: nicht
	 *         lieferbar
	 */
	public Vector istBestPosLieferbar(Vector bestPosIds) {
		daten = new Vector();
		int status = 0;

		for (int i = 0; i < bestPosIds.size(); i++) {
			query = "select * from bestpos where id="
					+ Integer.parseInt(bestPosIds.get(i).toString());

			try {
				stmt = conL.createStatement();
				rs = stmt.executeQuery(query);

				while (rs.next()) {
					status = (rs.getInt("status"));
				}

				rs.close();
				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (status == 5)
				daten.add(false);
			else
				daten.add(true);
		}
		return daten;

	}

	public String getNameZurNummer(int nummer, String tabelle) {
		String name = "";
		query = "select name from " + tabelle + " where nummer=" + nummer;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				name = rs.getString("name");
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * Man kann sämtliche Daten, die vom Typ String sind, mit Hilfe der
	 * Zeilennummer auslesen
	 * 
	 * @param nummer
	 *            Zeilennummer des Datensatzes
	 * @param tabelle
	 *            aus welcher Tabelle soll ausgelesen werden
	 * @param x
	 *            was soll ausgelesen werden
	 * @return Ergebnis
	 */
	public String getXZurNummer(int nummer, String tabelle, String x) {
		String ergebnis = "";
		query = "select " + x + " as x from " + tabelle + " where nummer="
				+ nummer;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				ergebnis = rs.getString("x");
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ergebnis;
	}

	public int getYZurZummer(int nummer, String tabelle, String y) {
		int ergebnis = 0;

		query = "select " + y + " as y from " + tabelle + " where nummer="
				+ nummer;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				ergebnis = rs.getInt("y");
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ergebnis;
	}

	public Vector getProjektDaten(int zeilennummer) {
		daten = new Vector();

		query = "select * from projekt where nummerSelbst=" + zeilennummer;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				daten.add(rs.getString("name"));
				daten.add(rs.getString("abteilung"));
				daten.add(rs.getString("datum"));
				daten.add(rs.getString("klasse"));
				daten.add(rs.getString("kurzz"));
				daten.add(rs.getString("lehrer"));
				daten.add(rs.getInt("nummerSelbst"));
				daten.add(rs.getString("teilnehmer"));
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return daten;
	}

	public int getKennungLmb(int zeilennummer) {
		int kennung = 0;

		query = "select kennung from lmb where nummer=" + zeilennummer;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				kennung = rs.getInt("kennung");
			}

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return kennung;
	}

	public int getFirmaZuBestellung(int bestID) {
		int firma = 0;
		query = "select firma from bestellung where bestID=" + bestID;

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				firma = rs.getInt("firma");

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return firma;

	}

	public int getBestellposIDzuBanfposID(int banfposID) {
		int bestellposID = 0;
		query = "select id from bestpos where banfposnr=" + banfposID;

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				bestellposID = rs.getInt("id");

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bestellposID;

	}

	public Vector<Integer> getMaterialZuBestellPosition(int bestPosID) {
		Vector<Integer> material = new Vector<Integer>();

		query = "select bezeichnung from bestpos where bestellId=" + bestPosID;

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next())
				material.add(rs.getInt("bezeichnung"));

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return material;

	}

	public void aendereFirmaMaterialBeziehungPreis(double preis, int material,
			int firma) {
		query = "update firma_material set preisExkl=" + preis
				+ " where material=" + material + " and firma=" + firma;
		try {
			stmt = conL.createStatement();
			stmt.executeUpdate(query);
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean alleGeliefert(int id) {
		boolean alleGeliefert;
		int menge = 0;
		int geliefert = 0;
		int status = 0;

		query = "select menge,geliefert,status from bestpos where id=" + id;

		try {
			stmt = conL.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				menge = rs.getInt("menge");
				geliefert = rs.getInt("geliefert");
				status = rs.getInt("status");
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (status == 6)
			alleGeliefert = true;
		else if (menge == geliefert)
			alleGeliefert = true;
		else
			alleGeliefert = false;

		return alleGeliefert;

	}

}// end Klasse DatenImport

