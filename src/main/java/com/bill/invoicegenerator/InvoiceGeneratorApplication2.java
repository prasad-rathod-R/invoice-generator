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
public class InvoiceGeneratorApplication2 {

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

		myTextWriter.addSingleLineText("Customer Name: " + name, 25, pageHeight - 100, font, 16, Color.BLACK);

		MytableClass table = new MytableClass(document, contentSteam);

		int cellWidths[] = { 70, 160, 120, 90, 100 };
		table.setTable(cellWidths, 30, 25, pageHeight - 150);
		table.setTableFont(font, 16, Color.BLACK);

		Color TableHeadColor = new Color(240, 93, 11);
		Color TableBodyColor = new Color(219, 218, 198);

		table.addCell("Sl.No", TableHeadColor);
		table.addCell("Items", TableHeadColor);
		table.addCell("price", TableHeadColor);
		table.addCell("Qty", TableHeadColor);
		table.addCell("Total", TableHeadColor);

		table.addCell("1", TableBodyColor);
		table.addCell("Masala Dosa paneer a", TableBodyColor);
		table.addCell("120", TableBodyColor);
		table.addCell("2", TableBodyColor);
		table.addCell("240", TableBodyColor);

		contentSteam.close();
		addPageNumbers(document, "Page {0}", 60, 18);
		document.save("K:\\PDF\\results\\myPdf450.pdf");
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

		    float cellWidth = colWidths[colPosition];
		    float cellHeight = calculateCellHeight(text, font, fontSize, cellWidth);

		    contentStream.addRect(xPosition, yPosition, cellWidth, cellHeight);

		    if (fillColor == null)
		        contentStream.stroke();
		    else
		        contentStream.fillAndStroke();

		    contentStream.beginText();
		    contentStream.setNonStrokingColor(fontColor);
		    contentStream.setFont(font, fontSize);

		    float availableWidth = cellWidth - 20; // Adjust as needed

		    contentStream.newLineAtOffset(xPosition + 20, yPosition + cellHeight - 10);

		    // Split the text into lines that fit within the available width
		    String[] lines = splitText(text, font, fontSize, availableWidth);

		    // Show each line of text
		    for (String line : lines) {
		        contentStream.showText(line);
		        contentStream.newLineAtOffset(0, -fontSize); // Move to the next line
		    }

		    contentStream.endText();

		    xPosition = xPosition + colWidths[colPosition];
		    colPosition++;

		    if (colPosition == colWidths.length) {
		        colPosition = 0;
		        xPosition = xInitialPosition;
		        yPosition -= cellHeight; // Adjust the y position based on the dynamic cell height
		    }
		}

		// Helper method to split text into lines that fit within a given width
		String[] splitText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
			List<String> lines = new ArrayList<>();
			int lastSpace = -1;

			while (text.length() > 0) {
				int spaceIndex = text.indexOf(' ', lastSpace + 1);
				if (spaceIndex < 0)
					spaceIndex = text.length();

				String subString = text.substring(0, spaceIndex);
				float width = font.getStringWidth(subString) / 1000 * fontSize;

				if (width > maxWidth) {
					if (lastSpace < 0)
						lastSpace = spaceIndex;
					subString = text.substring(0, lastSpace);
					lines.add(subString);
					text = text.substring(lastSpace).trim();
					lastSpace = -1;
				} else if (spaceIndex == text.length()) {
					lines.add(text);
					text = "";
				} else {
					lastSpace = spaceIndex;
				}
			}

			return lines.toArray(new String[0]);
		}

		// Helper method to calculate dynamic cell width based on text content
		private float calculateCellWidth(String text, PDFont font, float fontSize) throws IOException {
			float textWidth = font.getStringWidth(text) / 1000 * fontSize;
			return Math.max(textWidth + 20, colWidths[colPosition]); // Minimum cell width of colWidths[colPosition]
		}

		// Helper method to calculate dynamic cell height based on text content, font,
		// and font size
		// Helper method to calculate dynamic cell height based on text content, font,
		// and font size
		private float calculateCellHeight(String text, PDFont font, float fontSize, float cellWidth)
				throws IOException {
			float leading = 1.5f * fontSize;
			String[] lines = splitText(text, font, fontSize, cellWidth - 20); // Adjust available width for text
																				// wrapping
			int lineCount = lines.length;

			if (lineCount > 1) {
				return lineCount * (fontSize + leading) + 20;
			} else {
				return cellHeight;
			}
		}

	}
}