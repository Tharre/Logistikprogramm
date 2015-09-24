package Budget;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 *Mit Hilfe dieser Klasse wird die Datenbank mit den Standarddaten befuellt
 *<p>
 * Title: Datenbankbefuellung
 * 
 *@author Haupt, Liebhart
 **/
public class DBBefuellung {

	/** Die Connection zur Budget-Datenbank **/
	private Connection con;

	// -------------------------------UT8------------------------------
	/** Alle Kostenstellen, die zum Budget UT8 gehoeren **/

	private String[] kostenstellenut8ARRAY = { "Arbeitsvorbereitung",
			"Garderoben", "Lager-EL-Labor", "Lager-zentral",
			"Lehrerzimmer-WST", "Schularzt", "Werkstättenleitung",
			"Werkzeugausgabe", "Blechbearbeitung", "CNC-Ausbildung1",
			"CNC-Ausbildung2", "Dreherei", "Fräserei", "Galvanik",
			"Gerätebau-WST", "Gießerei", "Grundlehrgang", "Härterei",
			"Heiztechnik-WST", "Kunststoff", "Leichtbau",
			"Maschinengrundausbildung 1+2", "Mechanische Werkstätten",
			"Modelltischlerei", "Montage", "Schmiede", "Schweißerei A",
			"Schweißerei E", "Werkzeugbau", "Einführung Elektrotechnik",
			"Elektroinstallation1", "Elektroinstallationbus",
			"Elektromaschinenbau", "Elektromechanische Werkstätte",
			"Gerätebau", "Installationstechnik 1", "Schaltungstechnik",
			"Steuerungs- u. Regelungstechnik-WST", "Steuerungstechnik",
			"Computertechnik1", "Computertechnik2", "Computerwerkstätte",
			"Elektronik-WST", "Elektronik1", "Elektronik2", "Elektronik3",
			"Elektronik4", "Fernmeldetechnik", "Mikroprozessortechnologie",
			"Netzwerktechnik2", "Nieder-Hochfrequenztechnik", "Printtechnik",
			"Schaltungstechnik-WST", "Steuerungs- u. Regelungstechnik1",
			"Steuerungs- u. Regelungstechnik2",
			"Speicherprogrammierbare Steuerungen", "EDV-Saal1", "EDV-Saal2",
			"EDV-Saal3", "EDV-Saal4", "EDV-Saal5", "EDV-Saal6", "EDV-Saal7",
			"EDV-Saal8", "EDV-Saal9", "EDV-Saal10", "EDV-Saal11", "EDV-Saal12",
			"EDV-Saal13", "EDV-Saal14", "Audivisuelle Mittel", "Chemiesaal",
			"Klassenzimmer Altbau", "Klassenzimmer Neubau", "Physiksaal",
			"Ed_Mueller_Saal", "Lehrerzimmer-Theorie", "Bibliothek",
			"Heiz- und Energietechnik", "Hydraulik", "Messtechnik",
			"Pneumatik", "Strömungsmaschinen", "Verbrennungskraftmaschinen",
			"Werkstoffprüfung", "E-Lager", "Elektromaschinen",
			"Elektrotechnik-Labor", "Mikroelektronik", "Netzwerktechnik1",
			"Prozessrechentechnik", "Steuerungs- u. Regelungstechnik-Labor",
			"Photovoltaik", "Elektronik-Labor", "Heiztechnik-Labor",
			"Abschlussprüfung MF4", "Abschlussprüfung EF4",
			"Abschlussprüfung TIF",
			"Sonderprojekt aus Theorie und Laborbereich",
			"Elektrotechnik-TDOT", "Maschinenbau-TDOT",
			"Technische Informatik-TDOT", "Wirtschaftsingenieurwesen-TDOT",
			"Netzwerk- u. PC-Reparatur", "Netzwerk- u. PC-Arbeitsplatzber.",
			"Gebäudeinstandhaltung innen", "Gebäudeinstandhaltung außen",
			"Haustechnik", "Außenanlagen", "Sonderprojekt,Gebäudeumbauten",
			"Lehrmittel Theorie", "Turnsaal/Sportgeräte", "Sportanlagen",
			"Sicherheit,Arbeitsmedizin,Evaluierung", "Klassenräume Internat",
			"Sporthalle", "Traglufthalle", "Telefonanlage",
			"Gas, O2-Flaschen, Reinigungstücher", "Containerklassen",
			"Personalkosten (Schule u. Turnsaal)",
			"Reinigungsmittel (Abr. ü. Internat)",
			"Reinigung durch Fremdfirmen", "Stromkosten", "Heizkosten",
			"Prüfkosten", "Traktor, Rasenmäher, Anhänger, Reparatur, Service",
			"Amt. Mitteilungen, Zeitungen, Normen, Druckkosten",
			"Kopierkosten (Abr. über EV)", "Büromaterial",
			"Ifde. Telefonkosten, Datenübertr.", "Porto, Post, Bankspesen",
			"Einkauf Schulwarte", "Inserate, Einschaltungen, PR",
			"Sonderkosten Zubau", "Netzbetreuung", "Softwarelizenzen",
			"Hardware" };


	private String[] raumnummerUt8ARRAY = { "W010", "W042-W052", "W107a",
			"W063", "W053", "W001", "W003", "W062", "W079", "W055", "W056",
			"W072", "W066", "W057", "W080a", "W074", "W067", "W058", "WK",
			"W076", "W068", "W066a", "W066", "W080", "W078", "W073", "W069",
			"W070", "W059", "W087a", "W088a", "WN033", "W084", "W089a", "W080",
			"W085", "WN025", "WN022", "W089b", "WN003", "WN002", "WN023",
			"W083", "WN035", "WN038", "WN036", "WN015", "WN020", "WN013",
			"W102", "WN037", "WN017", "WN025", "W088b", "W089b", "W087",
			"TN101", "TN113", "TN201", "TN213", "TN301", "TN313", "TN401",
			"TN413", "Leer", "Leer", "Leer", "Leer", "Leer", "Leer", "Leer",
			"Leer", "Leer", "Leer", "Leer", "Leer", "Leer", "Leer", "WN028",
			"WN034", "W112", "WN031", "W113", "W104", "W103", "W107a", "W109",
			"W091", "W110", "W101", "W111", "W092", "WN027", "W101", "WN029",
			"APMF", "APEF", "APTIF", "", "ToTET", "ToTMB", "ToTTI", "ToTWI",
			"WN002", "", "GEB01", "GEB02", "GEB03", "GEB04", "GEB05", "LM01",
			"TS01", "SPANL", "SI01", "KLAIN", "SPHA", "TRAGL", "TELAN", "GAS",
			"CONT", "REI-PERS", "REI-MAT", "REI-FF", "E-STR", "E-HEIZ",
			"E-PRÜF", "FP", "ZS", "VW-KOP", "VW,BÜRO", "VW-TELE", "VW-SPESEN",
			"VW-SW", "VW-PR", "VW-SOKO", "EDV-NET", "EDV-SOFT", "EDV-HARD" };

	

	// int
	/** wie viele Datensaetze sind in der Tabelle "abteilungut3" **/
	private int anzUt3;
	/** wie viele Datensaetze sind in der Tabelle "lmb" **/
	private int anzLmb;
	/** wie viele Datensaetze sind in der Tabelle "kostenstelleut8" **/
	private int anzUt8Kst;
	/** wie viele Datensaetze sind in der Tabelle "hauptkostenstelleut8" **/
	private int anzUt8HKst;
	/** wie viele Datensaetze sind in der Tabelle "bereichut8" **/
	private int anzUt8Ber;
	/** wie viele Datensaetze sind in der Tabelle "hauptbereichut8" **/
	private int anzUt8HBer;

	// sonstiges
	/** Query, auf dem der SQL-Befehl gespeichert ist **/
	private String query;
	/** Statement, zum Ausfuehren von SQL-Befehlen **/
	private Statement stmt;
	/** Objekt der Klasse DatenImport **/
	private DatenImport di;
	private String[] kostenstellenut8;
	private String[] hauptkostenstellenut8;
	private String[] bereicheut8;
	private String[] hauptbereicheut8;
	private String[] nummerSelbstUt8_kst;
	private String[] nummerSelbstUt8_hkst;
	private String[] nummerSelbstUt8_hber;
	private String[] nummerSelbstUt8_ber;
	private int[] hauptnummerUt8_kst;
	private int[] hauptnummerUt8_hkst;
	private int[] hauptnummerUt8_ber;
	private String[] raumnummerUt8;
	private String[] kurzbezUt8;
	private String[] abteilungenut3;
	private String[] abteilungenlmb;
	private int[] kennung;
	private String[] sonderbudget;
	private String[] projektName;
	private String[] projektDatum;
	private String[] projektNummerSelbst;
	private String[] projektTeilnehmer;
	private String[] projektLehrer;
	private String[] projektKurzz;
	private String[] projektKlasse;
	private String[] projektAbteilung;

	/**
	 * Konstruktor
	 * 
	 * @param con
	 *            Connection zur Budgetdatenbank
	 */
	public DBBefuellung(Connection con) {
		this.con = con;
		di = new DatenImport(con, con);

		NewTextdatei text = new NewTextdatei();
		kostenstellenut8 = text.getKostenstellenut8();

		hauptkostenstellenut8 = text.getHauptkostenstelleut8();
		bereicheut8 = text.getBereicheut8();
		hauptbereicheut8 = text.getHauptbereicheut8();
		nummerSelbstUt8_kst = text.getnummerSelbstUt8_kst();
		nummerSelbstUt8_hkst = text.getnummerSelbstUt8_hkst();
		nummerSelbstUt8_ber = text.getnummerSelbstUt8_ber();
		nummerSelbstUt8_hber = text.getnummerSelbstUt8_hber();
		hauptnummerUt8_kst = text.gethauptnummerUt8_kst();
		hauptnummerUt8_hkst = text.gethauptnummerUt8_hkst();
		hauptnummerUt8_ber = text.gethauptnummerUt8_ber();
		raumnummerUt8 = text.getraumnummerUt8();
		kurzbezUt8 = text.getkurzbezUt8();
		abteilungenut3 = text.getAbteilungenUT3();
		abteilungenlmb = text.getAbteilungenLMB1();
		kennung = text.getKennungLMB();

		sonderbudget = text.getSonderbudget();
		projektName = text.getProjektName();
		projektDatum = text.getProjektDatum();
		projektNummerSelbst = text.getProjektNummer();
		projektTeilnehmer = text.getProjektTeilnehmer();
		projektLehrer = text.getProjektLehrer();
		projektKurzz = text.getProjektKurzz();
		projektKlasse = text.getProjektKlasse();
		projektAbteilung = text.getProjektAbteilung();
		


		anzUt3 = di.getAnzahlDatensaetze("abteilungut3");
		anzLmb = di.getAnzahlDatensaetze("lmb");
		anzUt8Kst = di.getAnzahlDatensaetze("kostenstelleut8");
		anzUt8HKst = di.getAnzahlDatensaetze("hauptkostenstelleut8");
		anzUt8Ber = di.getAnzahlDatensaetze("bereichut8");
		anzUt8HBer = di.getAnzahlDatensaetze("hauptbereichut8");

		if ((anzUt3 == 0) && (anzLmb == 0) && (anzUt8Kst == 0)
				&& (anzUt8HKst == 0) && (anzUt8Ber == 0) && (anzUt8HBer == 0))
			befuelleDatenbank();
		else
			JOptionPane
					.showMessageDialog(null,
							"Die Datenbank ist nicht leer und konnte deswegen nicht neu befuellt werden!");

	}

	/**
	 * befuellt die Datenbank mit den Standardwerten
	 */
	public void befuelleDatenbank() {
		try {
			stmt = con.createStatement();

			// kostenstellenut8
			for (int i = 0; i < kostenstellenut8.length; i++) {

				query = "insert into kostenstelleut8(hauptnummer,nummer,Raumnummer,name,NummerSelbst,kurzbezeichn)"
						+ " values("
						+ hauptnummerUt8_kst[i]
						+ ","
						+ (i + 1)
						+ ",'"
						+ raumnummerUt8[i]
						+ "','"
						+ kostenstellenut8[i]
						+ "','"
						+ nummerSelbstUt8_kst[i]
						+ "','"
						+ kurzbezUt8[i] + "')";
				stmt.executeUpdate(query);

			}

			// hauptkostenstellenut8
			for (int i = 0; i < hauptkostenstellenut8.length; i++) {
				query = "insert into hauptkostenstelleut8(hauptnummer,nummer,name,NummerSelbst) values("
						+ hauptnummerUt8_hkst[i]
						+ ","
						+ (i + 1)
						+ ",'"
						+ hauptkostenstellenut8[i]
						+ "','"
						+ nummerSelbstUt8_hkst[i] + "')";

				stmt.executeUpdate(query);

			}

			// bereichut8
			for (int i = 0; i < bereicheut8.length; i++) {
				query = "insert into bereichut8(hauptnummer,nummer,name,NummerSelbst) values("
						+ hauptnummerUt8_ber[i]
						+ ","
						+ (i + 1)
						+ ",'"
						+ bereicheut8[i]
						+ "','"
						+ nummerSelbstUt8_ber[i]
						+ "')";

				stmt.executeUpdate(query);

			}

			// hauptbereichut8
			for (int i = 0; i < hauptbereicheut8.length; i++) {
				query = "insert into hauptbereichut8(nummer,name,NummerSelbst) values("
						+ (i + 1)
						+ ",'"
						+ hauptbereicheut8[i]
						+ "','"
						+ nummerSelbstUt8_hber[i] + "')";

				stmt.executeUpdate(query);

			}

			// abteilungut3
			for (int i = 0; i < abteilungenut3.length; i++) {
				query = "insert into abteilungut3(nummer,name) values("
						+ (i + 1) + ",'" + abteilungenut3[i] + "')";
				stmt.executeUpdate(query);

			}

			// lmb
			for (int i = 0; i < abteilungenlmb.length; i++) {
				query = "insert into lmb(nummer,name,kennung) values("
						+ (i + 1) + ",'" + abteilungenlmb[i] + "',"
						+ kennung[i] + ")";
				stmt.executeUpdate(query);

			}

			// sonderbudget
			for (int i = 0; i < sonderbudget.length; i++) {
				if(sonderbudget[i].equals(""))
				{
					continue;
				}
				else{
					query = "insert into sonderbudget(name) values('"
						+ sonderbudget[i] + "')";
					stmt.executeUpdate(query);
				}
				

			}

			// sonderbudget
			for (int i = 0; i < projektName.length; i++) {
				query = "insert into projekt(nummerSelbst,name,lehrer,kurzz,klasse,datum,teilnehmer,abteilung)values('"
						+ Integer.parseInt(projektNummerSelbst[i])
						+ "','"
						+ projektName[i]
						+ "','"
						+ projektLehrer[i]
						+ "','"
						+ projektKurzz[i]
						+ "','"
						+ projektKlasse[i]
						+ "','"
						+ projektDatum[i]
						+ "','"
						+ projektTeilnehmer[i]
						+ "','"
						+ projektAbteilung[i] + "')";

				stmt.executeUpdate(query);

			}

			JOptionPane
					.showMessageDialog(null, "Die Datenbank wurde befuellt!");
		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null,
					"Es gab einen Fehler beim Befuellen der Datenbank");

			e.printStackTrace();

		}
	}
}
