package com.bill.invoicegenerator;

import java.awt.Color;
import java.io.IOException;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceGeneratorApplication {

	public static void main(String[] args) throws IOException {

		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		String name = "Prasad Rathod";
		String callNo = "7676646825";

		Format d_format = new SimpleDateFormat("dd MMM yy");
		Format tFormat = new SimpleDateFormat("HH:mm");

		int pageWidth = (int) page.getTrimBox().getWidth();
		int pageHeight = (int) page.getTrimBox().getHeight();

		PDPageContentStream contentSteam = new PDPageContentStream(document, page);
		MytextClass myTextWriter = new MytextClass(document, contentSteam);

		PDFont font = PDType1Font.HELVETICA;
		PDFont italicFont = PDType1Font.HELVETICA_OBLIQUE;

		PDImageXObject myImage = PDImageXObject.createFromFile("src/main/resources/images/tadka_Image.jpg", document);
		contentSteam.drawImage(myImage, 0, pageHeight - 235, pageWidth, 239);

		String[] contactDetails = new String[] { "7676646835", "8867138193" };

		myTextWriter.addMultiLineText(contactDetails, 18,
				(int) (pageWidth - font.getStringWidth("7676646825") / 1000 * 15 - 10), pageHeight - 25, font, 15,
				Color.BLACK);

		myTextWriter.addSingleLineText("Indian Tadka", 25, pageHeight - 150, font, 40, Color.BLACK);

		myTextWriter.addSingleLineText("Customer Name: " + name, 25, pageHeight - 250, font, 16, Color.BLACK);
		myTextWriter.addSingleLineText("MO. No:" + callNo, 25, pageHeight - 274, font, 16, Color.BLACK);

		String invoiceNumber = "Invoice# 2536 ";
		float textWidth = myTextWriter.getTextWidth(invoiceNumber, font, 16);
		myTextWriter.addSingleLineText(invoiceNumber, (int) (pageWidth - 25 - textWidth), pageHeight - 250, font, 16,
				Color.BLACK);

		float dateTextWidth = myTextWriter.getTextWidth("Date: " + d_format.format(new Date()), font, 16);
		myTextWriter.addSingleLineText("Date: " + d_format.format(new Date()), (int) (pageWidth - 25 - dateTextWidth),
				pageHeight - 274, font, 16, Color.BLACK);

		String time = tFormat.format(new Date());

		float timeTextwidth = myTextWriter.getTextWidth("Time: " + time, font, 16);
		myTextWriter.addSingleLineText("Time: " + time, (int) (pageWidth - 25 - timeTextwidth), pageHeight - 298, font,
				16, Color.BLACK);

		MytableClass table = new MytableClass(document, contentSteam);

		int cellWidths[] = { 70, 160, 120, 90, 100 };
		table.setTable(cellWidths, 30, 25, pageHeight - 350);
		table.setTableFont(font, 16, Color.BLACK);

		Color TableHeadColor = new Color(240, 93, 11);
		Color TableBodyColor = new Color(219, 218, 198);

		table.addCell("Sl.No", TableHeadColor);
		table.addCell("Items", TableHeadColor);
		table.addCell("price", TableHeadColor);
		table.addCell("Qty", TableHeadColor);
		table.addCell("Total", TableHeadColor);

		table.addCell("1", TableBodyColor);
		table.addCell("Masala Dosa", TableBodyColor);
		table.addCell("120", TableBodyColor);
		table.addCell("2", TableBodyColor);
		table.addCell("240", TableBodyColor);

		table.addCell("1", TableBodyColor);
		table.addCell("Samosa", TableBodyColor);
		table.addCell("20", TableBodyColor);
		table.addCell("2", TableBodyColor);
		table.addCell("240", TableBodyColor);

		table.addCell("1", TableBodyColor);
		table.addCell("Puri", TableBodyColor);
		table.addCell("120", TableBodyColor);
		table.addCell("2", TableBodyColor);
		table.addCell("240", TableBodyColor);

		table.addCell("1", TableBodyColor);
		table.addCell("North Indian meal", TableBodyColor);
		table.addCell("120", TableBodyColor);
		table.addCell("2", TableBodyColor);
		table.addCell("240", TableBodyColor);

		table.addCell("", null);
		table.addCell("", null);
		table.addCell("Sub Total", null);
		table.addCell("", null);
		table.addCell("880", null);

		table.addCell("", null);
		table.addCell("", null);
		table.addCell("GST", null);
		table.addCell("5%", null);
		table.addCell("44", null);

		table.addCell("", null);
		table.addCell("", null);
		table.addCell("Grand Total", TableHeadColor);
		table.addCell("", TableHeadColor);
		table.addCell("924", TableHeadColor);

		String paymentMethod[] = { "Methods of payment we accept:", "Cash,Cheque, Gpay, RuPay, Maestro,",
				"Visa, Mastercard and American Express " };
		myTextWriter.addMultiLineText(paymentMethod, 15, 25, 180, italicFont, 10, new Color(122, 122, 122));

		contentSteam.setStrokingColor(Color.BLACK);
		contentSteam.setLineWidth(2);
		contentSteam.moveTo(pageWidth - 250, 150);
		contentSteam.lineTo(pageWidth - 25, 150);
		contentSteam.stroke();

		String authSign = "Authorized Signture";
		float authSignture = myTextWriter.getTextWidth(authSign, italicFont, 16);
		int xpos = pageWidth - 250 + pageWidth - 25;
		myTextWriter.addSingleLineText(authSign, (int) (xpos - authSignture) / 2, 125, italicFont, 16, Color.BLACK);

		String BottomLineText = "Rain or shine, its time to dine";
		float bottonLineWidth = myTextWriter.getTextWidth(BottomLineText, italicFont, xpos);
		myTextWriter.addSingleLineText(BottomLineText, (int) ((pageWidth - bottonLineWidth) / 2), 50, italicFont, 20,
				Color.DARK_GRAY);

		Color bottomRectColor = new Color(255, 91, 0);
		contentSteam.setNonStrokingColor(bottomRectColor);
		contentSteam.addRect(0, 0, pageWidth, 30);
		contentSteam.fill();

		contentSteam.close();
		addPageNumbers(document, "Page {0}", 60, 18);
		document.save("K:\\PDF\\results\\myPdf.pdf");
		document.close();
		System.out.println("Document Generated Sucessfully!!!!!!!!!!!!");

	}

	private static List<String> splitStringToFitCell(String text, PDPageContentStream contentStream, PDType1Font font,
			float fontSize, float maxWidth) throws IOException {
		List<String> lines = new ArrayList<>();
		StringBuilder lineBuilder = new StringBuilder();
		float lineWidth = 0;

		for (String word : text.split("\\s")) {

			if (word.length() > 18) {
				String[] words = splitWordByLength(word, maxWidth);
				for (String word1 : words) {
					float wordWidth = font.getStringWidth(word1) / 1000 * fontSize;
					if (lineWidth + wordWidth <= maxWidth) {

						lineBuilder.append(word1).append(" ");
						lineWidth += wordWidth + font.getStringWidth(" ") / 1000 * fontSize;
					} else {
						lines.add(lineBuilder.toString().trim());
						lineBuilder = new StringBuilder(word1 + " ");
						lineWidth = wordWidth + font.getStringWidth(" ") / 1000 * fontSize;
					}
				}

			} else {

				float wordWidth = font.getStringWidth(word) / 1000 * fontSize;
				if (lineWidth + wordWidth <= maxWidth) {

					lineBuilder.append(word).append(" ");
					lineWidth += wordWidth + font.getStringWidth(" ") / 1000 * fontSize;
				} else {
					lines.add(lineBuilder.toString().trim());
					lineBuilder = new StringBuilder(word + " ");
					lineWidth = wordWidth + font.getStringWidth(" ") / 1000 * fontSize;
				}
			}
		}

		if (lineBuilder.length() > 0) {
			lines.add(lineBuilder.toString().trim());
		}

		return lines;
	}

	private static String[] splitWordByLength(String text, float maxWidth) {

		String word = text;
		float fontSize = 12;

		// Fixed length for splitting
		int segmentLength = (int) (fontSize * 1.5);

		// Calculate the number of segments
		int numSegments = (int) Math.ceil((double) word.length() / segmentLength);

		// Initialize a String array to store the segments
		String[] segments = new String[numSegments];

		// Split the word into segments and store them in the array
		for (int i = 0; i < numSegments; i++) {
			int start = i * segmentLength;
			int end = Math.min(start + segmentLength, word.length()); // Ensure end doesn't exceed word length
			segments[i] = word.substring(start, end);
		}

		// If the last segment is empty, remove it
		if (segments[numSegments - 1].isEmpty()) {
			String[] newSegments = new String[numSegments - 1];
			System.arraycopy(segments, 0, newSegments, 0, numSegments - 1);
			segments = newSegments;
		}
		return segments;
	}

	public static void addPageNumbers(PDDocument document, String numberingFormat, int offset_X, int offset_Y)
			throws IOException {
		int page_counter = 1;
		for (PDPage page : document.getPages()) {
			PDPageContentStream contentStream = new PDPageContentStream(document, page,
					PDPageContentStream.AppendMode.APPEND, true, false);
			contentStream.beginText();
			contentStream.setFont(PDType1Font.TIMES_ITALIC, 10);
			PDRectangle pageSize = page.getMediaBox();
			float x = pageSize.getLowerLeftX();
			float y = pageSize.getLowerLeftY();
			contentStream.newLineAtOffset(x + pageSize.getWidth() - offset_X, y + offset_Y);
			String text = MessageFormat.format(numberingFormat, page_counter);
			contentStream.showText(text);
			contentStream.endText();
			contentStream.close();
			++page_counter;
		}
	}

	private static int generateSerialNumber(int row) {
		return row + 1;
	}

	private static class MytextClass {
		PDDocument document;
		PDPageContentStream contentStream;

		public MytextClass(PDDocument document, PDPageContentStream contentStream) {
			this.document = document;
			this.contentStream = contentStream;
		}

		void addSingleLineText(String text, int xPosition, int yPosition, PDFont font, float fontSize, Color color)
				throws IOException {
			contentStream.beginText();
			contentStream.setFont(font, fontSize);
			contentStream.setNonStrokingColor(color);
			contentStream.newLineAtOffset(xPosition, yPosition);
			contentStream.showText(text);
			contentStream.endText();
			contentStream.moveTo(0, 0);
		}

		void addMultiLineText(String[] textArray, float leading, int xPosition, int yPosition, PDFont font,
				float fontSize, Color color) throws IOException {
			contentStream.beginText();
			contentStream.setFont(font, fontSize);
			contentStream.setNonStrokingColor(color);
			contentStream.setLeading(leading);
			contentStream.newLineAtOffset(xPosition, yPosition);

			for (String text : textArray) {

				contentStream.showText(text);
				contentStream.newLine();
			}
			contentStream.endText();
			contentStream.moveTo(0, 0);
		}

		float getTextWidth(String text, PDFont font, float fontSize) throws IOException {
			return font.getStringWidth(text) / 1000 * fontSize;
		}
	}

	private static class MytableClass {
		PDDocument document;
		PDPageContentStream contentStream;
		private int[] colWidths;
		private int cellHeight;
		private int yPosition;
		private int xPosition;
		private int colPosition = 0;
		private int xInitialPosition;
		private float fontSize;
		private PDFont font;
		private Color fontColor;

		public MytableClass(PDDocument document, PDPageContentStream contentStream) {
			this.document = document;
			this.contentStream = contentStream;
		}

		void setTable(int[] colWidths, int cellHeight, int xPosition, int yPosition) {
			this.colWidths = colWidths;
			this.cellHeight = cellHeight;
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			xInitialPosition = xPosition;
		}

		void setTableFont(PDFont font, float fontSize, Color fontColor) {
			this.font = font;
			this.fontSize = fontSize;
			this.fontColor = fontColor;
		}

		void addCell(String text, Color fillColor) throws IOException {
			contentStream.setStrokingColor(1f);

			if (fillColor != null) {
				contentStream.setNonStrokingColor(fillColor);
			}

			contentStream.addRect(xPosition, yPosition, colWidths[colPosition], cellHeight);

			if (fillColor == null)
				contentStream.stroke();
			else
				contentStream.fillAndStroke();

			contentStream.beginText();
			contentStream.setNonStrokingColor(fontColor);

			if (colPosition == 4 || colPosition == 2) {

				float fontWidth = font.getStringWidth(text) / 1000 * fontSize;
				contentStream.newLineAtOffset(xPosition + colWidths[colPosition] - 20 - fontWidth, yPosition + 10);

			} else {
				contentStream.newLineAtOffset(xPosition + 20, yPosition + 10);
			}
			contentStream.showText(text);
			contentStream.endText();

			xPosition = xPosition + colWidths[colPosition];
			colPosition++;

			if (colPosition == colWidths.length) {
				colPosition = 0;
				xPosition = xInitialPosition;
				yPosition -= cellHeight;

			}

		}

	}
}
