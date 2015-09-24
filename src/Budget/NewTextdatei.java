package Budget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class NewTextdatei {

	String[] gesplitet;
	//String pfad = "C:/Projekt_Textdateien/";
	String pfad = "D:/HTL/Diplomarbeit/Textdateien/";
	int stelle;

	public NewTextdatei() {

	}

	private String auslesenDatei(String name) {
		String s;
		String text = "";

		Reader r_in;
		try {
			r_in = new FileReader(
					new File(
							pfad
									+ name));

			BufferedReader br = new BufferedReader(r_in);

			try {
				while ((s = br.readLine()) != null)
					text = s;

				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return text;

	}

	private String[] splitString(String text) {
		return gesplitet = text.split(",");
	}

	public String[] getBereicheut8() {

		return splitString(auslesenDatei("bereichut8.txt"));

	}

	public String[] getHauptbereicheut8() {
		return splitString(auslesenDatei("hauptbereichut8.txt"));
	}

	public String[] getKostenstellenut8() {
		return splitString(auslesenDatei("kostenstellenut8.txt"));
	}

	public String[] getHauptkostenstelleut8() {
		return splitString(auslesenDatei("hauptkostenstelleut8.txt"));
	}

	public String[] getAbteilungenUT3() {
		return splitString(auslesenDatei("abteilungenut3.txt"));
	}

	public String[] getAbteilungenLMB1() {
		return splitString(auslesenDatei("abteilungenlmb.txt"));
	}

	public int[] getKennungLMB() {
		String[] kennung = splitString(auslesenDatei("kennung.txt"));
		int[] kennungI = new int[kennung.length];

		for (int i = 0; i < kennung.length; i++)
			kennungI[i] = Integer.parseInt(kennung[i]);

		return kennungI;

	}

	public String[] getnummerSelbstUt8_kst() {
		return splitString(auslesenDatei("nummerSelbstUt8kst.txt"));

	}

	public String[] getnummerSelbstUt8_hkst() {
		return splitString(auslesenDatei("nummerSelbstUt8hkst.txt"));

	}

	public String[] getnummerSelbstUt8_ber() {
		return splitString(auslesenDatei("nummerSelbstUt8ber.txt"));

	}

	public String[] getnummerSelbstUt8_hber() {
		return splitString(auslesenDatei("nummerSelbstUt8hber.txt"));

	}

	public int[] gethauptnummerUt8_kst() {
		String[] hNummer = splitString(auslesenDatei("hauptnummerUt8kst.txt"));

		int[] hNummerI = new int[hNummer.length];

		for (int i = 0; i < hNummer.length; i++)
			hNummerI[i] = Integer.parseInt(hNummer[i]);

		return hNummerI;

	}

	public int[] gethauptnummerUt8_hkst() {

		String[] hNummer = splitString(auslesenDatei("hauptnummerUt8hkst.txt"));
		int[] hNummerI = new int[hNummer.length];

		for (int i = 0; i < hNummer.length; i++)
			hNummerI[i] = Integer.parseInt(hNummer[i]);

		return hNummerI;

	}

	public int[] gethauptnummerUt8_ber() {
		String[] hNummer = splitString(auslesenDatei("hauptnummerUt8ber.txt"));
		int[] hNummerI = new int[hNummer.length];

		for (int i = 0; i < hNummer.length; i++)
			hNummerI[i] = Integer.parseInt(hNummer[i]);

		return hNummerI;

	}

	public String[] getraumnummerUt8() {
		return splitString(auslesenDatei("raumnummerUt8.txt"));

	}

	public String[] getkurzbezUt8() {
		return splitString(auslesenDatei("kurzbezUt8.txt"));

	}

	public String[] getSonderbudget() {
		return splitString(auslesenDatei("sonderbudget.txt"));

	}

	public String[] getProjektName() {
		return splitString(auslesenDatei("projektName.txt"));

	}

	public String[] getProjektDatum() {
		return splitString(auslesenDatei("projektDatum.txt"));

	}

	public String[] getProjektKlasse() {
		return splitString(auslesenDatei("projektKlasse.txt"));

	}

	public String[] getProjektAbteilung() {
		return splitString(auslesenDatei("projektAbteilung.txt"));

	}

	public String[] getProjektTeilnehmer() {
		return splitString(auslesenDatei("projektTeilnehmer.txt"));

	}

	public String[] getProjektKurzz() {
		return splitString(auslesenDatei("projektkurzz.txt"));

	}

	public String[] getProjektLehrer() {
		return splitString(auslesenDatei("projektLehrer.txt"));

	}

	public String[] getProjektNummer() {
		return splitString(auslesenDatei("projektNummer.txt"));

	}
	
	

	public void schreibeInTextdatei(String[] daten, String name) {
		Writer r_out;

		try {
			r_out = new FileWriter(
					new File(
							pfad
									+ name));

			BufferedWriter br_out = new BufferedWriter(r_out);

			for (int i = 0; i < daten.length - 1; i++)
			{
				br_out.write(daten[i] + ",");
			}

			br_out.write(daten[daten.length - 1]);

			br_out.close();
			r_out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void schreibeEinenDatensatzInTextdatei(String datensatz, String name) {
		Writer r_out;
		try {
			r_out = new FileWriter(
					new File(
							pfad
									+ name), true);

			BufferedWriter br_out = new BufferedWriter(r_out);

			br_out.write(","+datensatz);

			br_out.close();
			r_out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	

	public void loescheDatensatzAusTextdatei(Object datensatz, String tabelle) {

		try {
			Reader r_in = new FileReader(new File(
					pfad
							+ tabelle + ".txt"));
			BufferedReader br_in = new BufferedReader(r_in);

			String text = "";
			String[] datenSaetze = {};
			String[] datenSaetzeNeu;
			stelle = 0;
			
			while((text=br_in.readLine())!=null)
			{
				datenSaetze = text.split(",");	
			}
			
			datenSaetzeNeu = new String[datenSaetze.length-1];
						
			int j=0;
			for(int i=0; i<datenSaetze.length; i++)
			{
				if(datenSaetze[i].equals(""+datensatz))
				{
					stelle = i;
					continue;
				}
				else if(datenSaetze[i]==datensatz.toString())
				{
					stelle = i;
					continue;
				}
				else
				{
					datenSaetzeNeu[j] = datenSaetze[i];
					j++;
				}
			}
	
			r_in.close();
			br_in.close();
			
			schreibeNeueDatenInTextdatei(tabelle,datenSaetzeNeu);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public void loescheDatensatzAnBestimmterStelle(Object datensatz,String name)
	{
	
		
		try {
			Reader r_in = new FileReader(new File(
					pfad
							+ name + ".txt"));
			BufferedReader br_in = new BufferedReader(r_in);
			
			String text = "";
			String[] datenSaetze = {};
			String[] datenSaetzeNeu;
			
			while((text=br_in.readLine())!=null)
			{
				datenSaetze = text.split(",");	
			}
						
			datenSaetzeNeu = new String[datenSaetze.length-1];
			
			int j=0;
			for(int i=0; i<datenSaetze.length;i++)
			{
				
				if(i==stelle)
				{
					continue;
				}
				else
				{
					datenSaetzeNeu[j] = datenSaetze[i];

					j++;
				}
			}
			schreibeNeueDatenInTextdatei(name,datenSaetzeNeu);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
		
	public void schreibeNeueDatenInTextdatei(String name,String[] datenSaetzeNeu)
	{
		BufferedWriter br_out;
		try {
			br_out = new BufferedWriter(new FileWriter(new File(
					pfad
							+ name + ".txt")));
			
			for(int i=0; i<datenSaetzeNeu.length-1;i++)
			{
				br_out.write(datenSaetzeNeu[i]);
				br_out.write(',');
			}
			br_out.write(datenSaetzeNeu[datenSaetzeNeu.length-1]);
			br_out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
					
		
	}
}
