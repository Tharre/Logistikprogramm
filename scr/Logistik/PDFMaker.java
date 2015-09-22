package Logistik;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.text.*;
import java.awt.Color;

/**
 * Klasse zum Erzeugen von Bestellungs-PDFs
 */

public class PDFMaker extends PdfPageEventHelper {
	private Document doc;
	private PdfWriter writer;
	private java.awt.Image htlLogo;
	private java.awt.Image uMayI;
	private java.awt.Image uHamI;
	private PdfPTable firma;
	private PdfPTable reihen;
	private int numRows = 0;
	private double preisGesamt = 0;
	public PdfTemplate tpl;
	public BaseFont helv;
	public String address1 = "";
	public String wNummer;
	public String budget;

	public PDFMaker(String path, String budget) {
		URL url = this.getClass().getResource("logo.gif");
		htlLogo = new ImageIcon(url).getImage();
		url = this.getClass().getResource("UMay.gif");
		uMayI = new ImageIcon(url).getImage();
		url = this.getClass().getResource("UntStiedl.jpg");
		uHamI = new ImageIcon(url).getImage();
		this.budget= budget;

		try {
			doc = new Document(PageSize.A4, 30, 30, 110, 72);
			File f = new File(path);
			writer = PdfWriter.getInstance(doc, new FileOutputStream(f));
			wNummer = f.getName();
			doc.addTitle("Bestellung");
			doc.addAuthor("HTBL Hollabrunn");
			doc.addSubject("");
			doc.addKeywords("Bestellung");
			doc.addCreator("Logistik-Programm");
			doc.open();
			writer.setPageEvent(this);
			tpl = writer.getDirectContent().createTemplate(100, 100);
			tpl.setBoundingBox(new Rectangle(-50, -50, 100, 100));
			helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void close() {
		doc.close();
	}

	public void setAddress(String adr) {
		address1 = adr;
	}

	public PdfPTable addHead(String s) {
		try {
			float[] widths = new float[] { 20f, 30f, 50f };
			Font tableFont = FontFactory.getFont("Helvetica", 10, 0,
					Color.BLACK);
			float leading = tableFont.getSize() * 1.3f;
			PdfPTable table = new PdfPTable(3);
			Rectangle border = new Rectangle(0f, 0f);
			border.setBorderWidthBottom(0f);
			border.setBorderColor(Color.BLACK);
			table.getDefaultCell().setBorderColor(Color.WHITE);
			table.setWidthPercentage(100f);
			Image image = Image.getInstance(htlLogo, Color.WHITE, false);
			table.setWidths(widths);
			table.addCell(makeImageCell(image, Element.ALIGN_MIDDLE,
					Element.ALIGN_CENTER, tableFont, leading, 0f, border, true,
					true));
			tableFont = FontFactory.getFont("Helvetica", 8, 0, Color.BLACK);
			table.addCell(makeCell("HTBL - 2020 Hollabrunn    310447\n ",
					Element.ALIGN_BOTTOM, Element.ALIGN_CENTER, tableFont,
					leading, 0f, border, true, true));
			tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
			table.addCell(makeAddress(s, tableFont, -1f, border));
			PdfPCell cell = new PdfPCell();
			border.setBorderWidthBottom(1f);
			cell.setColspan(3);
			cell.cloneNonPositionParameters(border);
			table.addCell(cell);
			return table;
			// doc.add(table);
			// table=new PdfPTable(1);
			// border.setBorderWidthBottom(0f);
			// table.addCell(makeCell(" ", Element.ALIGN_BOTTOM,
			// Element.ALIGN_CENTER, tableFont, leading, 0f, border, true,
			// true));
			// doc.add(table);
			// return table;
		} catch (Exception e) {
		}
		return null;
	}

	public void addFirma(String name, String anschrift, String plzOrt) {
		firma = new PdfPTable(2);
		firma.setWidthPercentage(100f);
		Font tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
		float leading = tableFont.getSize() * 1.3f;
		Rectangle border = new Rectangle(0f, 0f);
		try {
			float[] widths = new float[] { 45f, 55f };
			PdfPTable f = new PdfPTable(1);
			tableFont = FontFactory.getFont("Helvetica", 10, Font.BOLD,
					Color.BLACK);
			leading = tableFont.getSize() * 1.3f;
			f.addCell(makeCell("Firma", Element.ALIGN_TOP, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
			leading = tableFont.getSize() * 1.3f;
			f.addCell(makeCell(name, Element.ALIGN_TOP, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			f.addCell(makeCell(anschrift, Element.ALIGN_TOP,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(plzOrt, Element.ALIGN_TOP, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			PdfPCell cell = new PdfPCell(f);
			cell.setPaddingLeft(15f);
			cell.setPaddingTop(3f);
			cell.setPaddingBottom(3f);
			firma.addCell(cell);
			firma.setWidths(widths);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addFooter(String lieferbed, String lieferfrist, String verpArt,
			String banfId, String ersteller, String adr, String bemerkung,
			boolean elternverein) {
		addRow(null, null, null, null, null, null, null);
		Font tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
		float leading = tableFont.getSize() * 1.3f;
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		float[] widths = new float[] { 40f, 60f };
		Rectangle border = new Rectangle(0f, 0f);
		try {
			addLine();
			addLine();
			PdfPTable t = new PdfPTable(2);
			t.addCell(makeCell("Lieferbedingungen:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell(lieferbed, Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell("Lieferfrist:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell(lieferfrist, Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell("Verpackungsart:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell(verpArt, Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell("Erstellt von:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell(ersteller, Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell("Bemerkung:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			t.addCell(makeCell(bemerkung, Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
		/*	PdfPCell cell = makeCell("Bemerkung: " + bemerkung,
					Element.ALIGN_BOTTOM, Element.ALIGN_LEFT, tableFont,
					leading, 3f, border, true, true);
			cell.setColspan(3);
			t.addCell(cell); */
			PdfPCell cell = new PdfPCell(t);
			t.setWidths(widths);
			cell.cloneNonPositionParameters(new Rectangle(0f, 0f));
			table.addCell(cell); 
			t = new PdfPTable(1);
			border.setBorderWidthBottom(1f);
			border.setBorderWidthTop(1f);
			border.setBorderWidthLeft(1f);
			border.setBorderWidthRight(1f);
			border.setBorderColor(Color.BLACK);
			t.addCell(makeCell("Warenversandadresse/Warenempfaenger",
					Element.ALIGN_BOTTOM, Element.ALIGN_CENTER, tableFont,
					leading, 3f, border, true, true));
			border = new Rectangle(0f, 0f);
			t.addCell(makeCell(adr, Element.ALIGN_BOTTOM, Element.ALIGN_RIGHT,
					tableFont, leading, 3f, border, true, true));
			t.addCell(makeCell("Dechant Pfeiferstrasse 1 ,2020 Hollabrunn",
					Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, tableFont,
					leading, 2f, border, true, false));
			t.addCell(makeCell(
					"Tel.: ++43(0)2952 3361-0     Fax: ++43(0)2952 3361-335",
					Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, tableFont,
					leading, 2f, border, true, false));
			t.addCell(makeCell("E-mail: leopold.mayer@htl-hl.ac.at",
					Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, tableFont,
					leading, 2f, border, true, false));
			cell = new PdfPCell(t);
			cell.cloneNonPositionParameters(new Rectangle(0f, 0f));
			table.addCell(cell);
			doc.add(table);
			addLine();
			addLine();
			t = new PdfPTable(1);
			border.setBorderWidthBottom(1f);
			border.setBorderWidthTop(1f);
			border.setBorderWidthLeft(1f);
			border.setBorderWidthRight(1f);
			if (!elternverein) {
				border.setBorderColor(Color.BLACK);
				t
						.addCell(makeCell(
								"Im Waren- und Dienstleistungsverkehr mit Bundesdienststellen sind die Vertragspartnerinnen oder Vertragspartner zur Ausstellung und Übermittlung von e-Rechnungen verpflichtet. Diese Regelung ist als Vertragsbedingung bindend.",
								Element.ALIGN_LEFT, Element.ALIGN_LEFT,
								tableFont, leading, 2f, border, true, true));
				doc.add(t);
			}
			addLine();
			addLine();
			border = new Rectangle(0f, 0f);
			table = new PdfPTable(2);
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			table.addCell(makeCell("Hollabrunn, am "
					+ sdf.format(System.currentTimeMillis()),
					Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, tableFont,
					leading, 2f, border, true, false));
			t = new PdfPTable(2);
			Image uMay = Image.getInstance(uMayI, Color.WHITE, false);
			Image uHam = Image.getInstance(uHamI, Color.WHITE, false);
			t.addCell(makeImageCell(uMay, Element.ALIGN_MIDDLE,
					Element.ALIGN_CENTER, tableFont, leading, 0f, border, true,
					true));
			t.addCell(makeImageCell(uHam, Element.ALIGN_MIDDLE,
					Element.ALIGN_CENTER, tableFont, leading, 0f, border, true,
					true));
			t.addCell(makeCell("Ing. Leopold Mayer, MBA", Element.ALIGN_MIDDLE,
					Element.ALIGN_CENTER, tableFont, leading, 2f, border, true,
					false));
			t.addCell(makeCell("Ing. Thomas Stiedl, BEd",
					Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, tableFont,
					leading, 2f, border, true, false));
			t.addCell(makeCell(" ", Element.ALIGN_BOTTOM, Element.ALIGN_CENTER,
					tableFont, 5f, 0f, border, true, true));
			t.addCell(makeCell(" ", Element.ALIGN_BOTTOM, Element.ALIGN_CENTER,
					tableFont, 5f, 0f, border, true, true));

			PdfPCell cel = makeCell("Werkstaettenleitung",
					Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, tableFont,
					leading, 2f, border, true, false);
			cel.setColspan(2);
			t.addCell(cel);
			PdfPCell c = new PdfPCell(t);
			c.cloneNonPositionParameters(border);
			table.addCell(c);
			widths = new float[] { 30f, 70f };
			table.setWidths(widths);
			table.setWidthPercentage(100f);
			doc.add(table);
		} catch (Exception e) {
		}
	}

	public void addAuftrag(String nummer, String datum, String kdNr,
			String uid, String kreditorennr, String einkaeufergr) {
		Font tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
		float leading = tableFont.getSize() * 1.3f;
		Rectangle border = new Rectangle(0f, 0f);
		float[] widths = new float[] { 70f, 70f };
		try {
			PdfPTable f = new PdfPTable(2);
			tableFont = FontFactory.getFont("Helvetica", 12, Font.BOLD,
					Color.BLACK);
			leading = tableFont.getSize() * 1.3f;
			f.addCell(makeCell("Auftrag", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell("", Element.ALIGN_BOTTOM, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
			leading = tableFont.getSize() * 1.3f;
			f.addCell(makeCell("Nummer:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(nummer, Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell("Datum:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(datum, Element.ALIGN_BOTTOM, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			f.addCell(makeCell("Kd.Nr.:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(kdNr, Element.ALIGN_BOTTOM, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			if(!budget.equals("LMB1")&&!budget.equals("LMB2"))
			{
			f.addCell(makeCell("unsere UID:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(uid, Element.ALIGN_BOTTOM, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			}
			f.addCell(makeCell("Ihre Kreditorennummer:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(kreditorennr, Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell("Auftragsreferenz:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell("UJ6:" + nummer, Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.setWidths(widths);
			PdfPCell cell = new PdfPCell(f);
			cell.setPaddingLeft(50f);
			cell.cloneNonPositionParameters(new Rectangle(0f, 0f));
			firma.addCell(cell);
			doc.add(firma);
			addLine();
			addLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		PdfPTable table = addHead(address1);
		table.setTotalWidth(document.right() - document.left());
		table.writeSelectedRows(0, -1, document.left(), document.getPageSize()
				.getHeight() - 40, cb);
		cb.saveState();
	
		String text = "Seite " + writer.getPageNumber() + " von ";
		float textSize = helv.getWidthPoint(text, 8);
		float textBase = document.bottom() - 20;
		cb.beginText();
		cb.setFontAndSize(helv, 8);
		// for odd pagenumbers, show the footer at the left
		float adjust = helv.getWidthPoint("0", 12);
		cb.setTextMatrix(document.left(), textBase);
		cb.showText(wNummer);
		cb.setTextMatrix(document.right() / 2 - (textSize + adjust) / 2,
				textBase);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(tpl, document.right() / 2 + (textSize - adjust) / 2,
				textBase);
		cb.saveState();
	}

	public PdfPCell makeAddress(String address, Font tableFont, float leading,
			Rectangle border) {
		PdfPTable table = new PdfPTable(1);
		tableFont = FontFactory
				.getFont("Helvetica", 10, Font.BOLD, Color.BLACK);
		Rectangle borders = new Rectangle(0f, 0f);
		table.addCell(makeCell(address, Element.ALIGN_MIDDLE,
				Element.ALIGN_RIGHT, tableFont, leading, 2f, borders, true,
				false));
		tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
		table.addCell(makeCell("Anton Ehrenfriedstrasse 10 ,2020 Hollabrunn",
				Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, tableFont, leading,
				2f, borders, true, false));
		table.addCell(makeCell("Tel.: ++43(0)2952 3361-0",
				Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, tableFont, leading,
				2f, borders, true, false));
		table.addCell(makeCell("Fax: ++43(0)2952 3361-335",
				Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, tableFont, leading,
				2f, borders, true, false));
		table.addCell(makeCell("E-mail: leopold.mayer@htl-hl.ac.at",
				Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, tableFont, leading,
				2f, borders, true, false));
		PdfPCell cell = new PdfPCell(table);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		cell.cloneNonPositionParameters(border);
		cell.setUseAscender(false);
		cell.setUseDescender(true);
		cell.setUseBorderPadding(true);
		cell.setPadding(0f);
		return cell;
	}

	public void addLine() throws Exception {
		Font tableFont = FontFactory.getFont("Helvetica", 10, Font.BOLD,
				Color.BLACK);
		Rectangle border = new Rectangle(0f, 0f);
		PdfPTable table = new PdfPTable(1);
		table.addCell(makeCell(" ", Element.ALIGN_BOTTOM, Element.ALIGN_CENTER,
				tableFont, 5f, 0f, border, true, true));
		doc.add(table);
	}

	public void addAngebot(String aNr, String anNr, String datum) {
		Font tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
		float leading = tableFont.getSize() * 1.3f;
		Rectangle border = new Rectangle(0f, 0f);
		float[] widths = new float[] { 10f, 15f, 15f, 20f, 15f, 30f };
		try {
			PdfPTable f = new PdfPTable(6);
			tableFont = FontFactory.getFont("Helvetica", 10, Font.BOLD,
					Color.BLACK);
			leading = tableFont.getSize() * 1.3f;
			f.addCell(makeCell("Auftrag:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
			leading = tableFont.getSize() * 1.3f;
			f.addCell(makeCell(aNr, Element.ALIGN_BOTTOM, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			tableFont = FontFactory.getFont("Helvetica", 10, 0, Color.BLACK);
			leading = tableFont.getSize() * 1.3f;
			f.addCell(makeCell("Ihr Angebot:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(anNr, Element.ALIGN_BOTTOM, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			f.addCell(makeCell("Sachbearbeiter:", Element.ALIGN_BOTTOM,
					Element.ALIGN_LEFT, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(datum, Element.ALIGN_BOTTOM, Element.ALIGN_LEFT,
					tableFont, leading, 3f, border, true, true));
			f.setWidths(widths);
			f.setWidthPercentage(100f);
			doc.add(f);
			addLine();
			addLine();
			f = new PdfPTable(1);
			f.addCell(makeCell(
					"Wir bestellen zu den beiliegenden Leistungsbedingungen",
					Element.ALIGN_BOTTOM, Element.ALIGN_LEFT, tableFont,
					leading, 3f, border, true, true));
			f.setWidthPercentage(100f);
			doc.add(f);
			addLine();
			reihen = new PdfPTable(8);
			reihen.setWidthPercentage(100f);
			widths = new float[] { 5f, 36f, 13f, 8f, 8f, 13f, 5f, 12f };
			reihen.setWidths(widths);
			tableFont = FontFactory.getFont("Helvetica", 10, Font.BOLD,
					Color.BLACK);
			leading = tableFont.getSize() * 1.3f;
			reihen.addCell(makeCell("Pos", Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, null, true,
					true));
			reihen.addCell(makeCell("Artikelbezeichnung", Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, null, true,
					true));
			reihen.addCell(makeCell("Art.Nummer", Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, null, true,
					true));
			reihen.addCell(makeCell("Menge", Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, null, true,
					true));
			reihen.addCell(makeCell("Einheit", Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, null, true,
					true));
			reihen.addCell(makeCell("Preis/Einheit", Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, null, true,
					true));
			reihen.addCell(makeCell("Ust", Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, null, true,
					true));
			reihen.addCell(makeCell("Betrag", Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, null, true,
					true));
			doc.add(reihen);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addRow(String bezeichnung, String artNr, String menge,
			String einheit, String preis, String ust, String betrag) {
		try {
			numRows++;
			PdfPTable f = new PdfPTable(8);
			Font tableFont = FontFactory.getFont("Helvetica", 10, 0,
					Color.BLACK);
			float leading = tableFont.getSize() * 1.3f;
			f.setWidthPercentage(100f);
			float[] widths = new float[] { 5f, 36f, 13f, 8f, 8f, 13f, 5f, 12f };
			f.setWidths(widths);
			Rectangle border = null;
			if (artNr == null) {
				DecimalFormat df = new DecimalFormat("#0.00");
				border = new Rectangle(0f, 0f);
				tableFont = FontFactory.getFont("Helvetica", 10, Font.BOLD,
						Color.BLACK);
				bezeichnung = "Summe";
				artNr = "";
				menge = "";
				einheit = "";
				preis = "";
				ust = "";
				betrag = df.format(preisGesamt);
				f.addCell(makeCell("", Element.ALIGN_BOTTOM,
						Element.ALIGN_CENTER, tableFont, leading, 3f, border,
						true, true));
			} else {
				f.addCell(makeCell(numRows + "", Element.ALIGN_BOTTOM,
						Element.ALIGN_CENTER, tableFont, leading, 3f, border,
						true, true));
			}
			f.addCell(makeCell(bezeichnung, Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(artNr, Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(menge, Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(einheit, Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(preis, Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, border, true,
					true));
			f.addCell(makeCell(ust, Element.ALIGN_BOTTOM, Element.ALIGN_CENTER,
					tableFont, leading, 3f, border, true, true));
			f.addCell(makeCell(betrag, Element.ALIGN_BOTTOM,
					Element.ALIGN_CENTER, tableFont, leading, 3f, border, true,
					true));
			doc.add(f);
			preisGesamt += Double.valueOf(betrag);
		} catch (Exception e) {
		}
	}

	private static PdfPCell makeCell(String text, int vAlignment,
			int hAlignment, Font font, float leading, float padding,
			Rectangle borders, boolean ascender, boolean descender) {

		Paragraph p = new Paragraph(text, font);
		p.setLeading(leading);

		PdfPCell cell = new PdfPCell(p);
		if (leading > 0f) {
			cell.setLeading(leading, 0);
		}
		cell.setVerticalAlignment(vAlignment);
		cell.setHorizontalAlignment(hAlignment);
		if (borders != null) {
			cell.cloneNonPositionParameters(borders);
		}
		cell.setUseAscender(ascender);
		cell.setUseDescender(descender);
		cell.setUseBorderPadding(true);
		cell.setPadding(padding);
		return cell;
	}

	private static PdfPCell makeImageCell(Image text, int vAlignment,
			int hAlignment, Font font, float leading, float padding,
			Rectangle borders, boolean ascender, boolean descender) {

		PdfPCell cell = new PdfPCell(text);
		cell.setLeading(leading, 0);
		cell.setVerticalAlignment(vAlignment);
		cell.setHorizontalAlignment(hAlignment);
		cell.cloneNonPositionParameters(borders);
		cell.setUseAscender(ascender);
		cell.setUseDescender(descender);
		cell.setUseBorderPadding(true);
		cell.setPadding(padding);
		return cell;
	}

	@Override
	public void onCloseDocument(PdfWriter writer, Document document) {
		tpl.beginText();
		tpl.setFontAndSize(helv, 8);
		tpl.setTextMatrix(0, 0);
		tpl.showText("" + (writer.getPageNumber() - 1));
		tpl.endText();
	}
}