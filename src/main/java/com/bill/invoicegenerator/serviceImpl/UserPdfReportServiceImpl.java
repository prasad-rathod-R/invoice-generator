package com.bill.invoicegenerator.serviceImpl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bill.invoicegenerator.repositories.UsersRepository;
import com.bill.invoicegenerator.request.UserRequest;
import com.bill.invoicegenerator.service.UserPdfReportService;

@Service
public class UserPdfReportServiceImpl implements UserPdfReportService {

	@Autowired
	UsersRepository userrewpo;

	@Override
	public ResponseEntity<Map<String, Object>> generatePdf(UserRequest request) {
		try {

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

			PDType1Font font = PDType1Font.HELVETICA;
			PDFont italicFont = PDType1Font.HELVETICA_OBLIQUE;

			myTextWriter.addSingleLineText("Customer Name: " + name, 25, pageHeight - 250, font, 16, Color.BLACK);
			myTextWriter.addSingleLineText("MO. No:" + callNo, 25, pageHeight - 274, font, 16, Color.BLACK);

			MytableClass table = new MytableClass(document, contentSteam);

			int cellWidths[] = { 70,70, 50, 70, 100, 100 };
			table.setTable(cellWidths, 25, 20, pageHeight - 350);
			table.setTableFont(font, 16, Color.BLACK);

			Color TableHeadColor = new Color(169, 169, 169);
			Color TableBodyColor = new Color(169, 169, 169);

			table.addCell("no", TableHeadColor);
			table.addCell("name", TableHeadColor);
			table.addCell("age", TableHeadColor);
			table.addCell("email", TableHeadColor);
			table.addCell("city", TableHeadColor);
			table.addCell("dish", TableHeadColor);
			int serialMumber = 1;
			List<Object[]> getAllTableDataQuery = userrewpo.getTableData(request.getId());

			for (Object[] arr : getAllTableDataQuery) {
				serialMumber++;
				table.addCell(String.valueOf(serialMumber), TableHeadColor);
				
				List<String> nameCellContent = splitStringToFitCell(arr[0] != null ? arr[0].toString() : "N/A",
						contentSteam, font, 16, cellWidths[0]);
				 for (String line : nameCellContent) {
				        table.addCell(line, TableHeadColor);
				    }
				
				table.addCell(nameCellContent.toString(), TableHeadColor);
				table.addCell(arr[1] != null ? arr[1].toString() : "N/A", TableHeadColor);
				table.addCell(arr[2] != null ? arr[2].toString() : "N/A", TableHeadColor);
				table.addCell(arr[3] != null ? arr[3].toString() : "N/A", TableHeadColor);
				table.addCell(arr[4] != null ? arr[4].toString() : "N/A", TableHeadColor);
			}

			contentSteam.close();
			addPageNumbers(document, "Page {0}", 60, 18);
			String filePath = "K:\\PDF\\results\\myPdf2.pdf";
			document.save(filePath);
			document.close();

			// Convert the document to a byte array
			byte[] pdfBytes = convertPdfToByteArray(filePath);

			// Set the response map
			Map<String, Object> response = new HashMap<>();
			response.put("message", "PDF generated successfully");
			response.put("pdfBytes", pdfBytes);

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception appropriately (e.g., log it or return an error response)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private static byte[] convertPdfToByteArray(String filePath) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (PDDocument document = PDDocument.load(new File(filePath))) {
			document.save(baos);
		}
		return baos.toByteArray();
	}

	private static String adjustFontSizeForWord(String word, PDFont font, float fontSize, float maxWidth) {
		float wordWidth = 0;
		try {
			wordWidth = font.getStringWidth(word) / 1000 * fontSize;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (wordWidth > maxWidth) {
			fontSize -= 1;
			try {
				wordWidth = font.getStringWidth(word) / 1000 * fontSize;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return word;
	}

	private static List<String> splitStringToFitCell(String text, PDPageContentStream contentStream, PDType1Font font,
			int fontSize, int maxWidth) throws IOException {
		System.out.println("Word: " + text);

		List<String> lines = new ArrayList<>();
		StringBuilder lineBuilder = new StringBuilder();
		float lineWidth = 0;

		for (String word : text.split("\\s")) {
			word = adjustFontSizeForWord(word, font, fontSize, maxWidth);
			if (word.length() > 10) {
				System.out.println("Entering in If");
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
