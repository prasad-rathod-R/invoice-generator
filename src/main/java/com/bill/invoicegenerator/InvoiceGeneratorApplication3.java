package com.bill.invoicegenerator;

import java.awt.Color;
import java.io.IOException;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceGeneratorApplication3 {

	@Autowired
	private static InvoiceService invoiceService = new InvoiceService(); // Initialize the service

	public static void main(String[] args) throws IOException {

		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		String name = invoiceService.getCustomerName(); // Replace with your actual method

		Format d_format = new SimpleDateFormat("dd MMM yy");
		Format tFormat = new SimpleDateFormat("HH:mm");

		int pageWidth = (int) page.getTrimBox().getWidth();
		int pageHeight = (int) page.getTrimBox().getHeight();

		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		MytextClass myTextWriter = new MytextClass(document, contentStream);

		PDFont font = PDType1Font.HELVETICA;
		PDFont italicFont = PDType1Font.HELVETICA_OBLIQUE;

		myTextWriter.addSingleLineText("Customer Name: " + name, 25, pageHeight - 50, font, 16, Color.BLACK);

		MytableClass table = new MytableClass(document, contentStream);

		int cellWidths[] = { 70, 160, 120, 90, 100 };
		table.setTable(cellWidths, 20, 20, pageHeight - 100);
		PDFont fontTable = PDType1Font.HELVETICA; // or any other font
		Color fontColor = Color.BLACK; // or any other color

		// Adjust the font sizes as needed
		float headerFontSize = 18;
		float bodyFontSize = 16;

		// Call setTableFont with the correct argument types
		table.setTableFont(fontTable, headerFontSize, bodyFontSize, fontColor);

//		Color TableHeadColor = new Color(240, 93, 11);
//		Color TableBodyColor = new Color(219, 218, 198);

		table.printHeader(Color.BLACK, Color.WHITE);

		List<InvoiceItem> invoiceItems = invoiceService.getInvoiceItems(); // Replace with your actual method

		for (InvoiceItem item : invoiceItems) {
			table.addRow(List.of(item.getSlNo(), item.getItem(), item.getPrice(), item.getQty(), item.getTotal()),
					Color.GRAY, Color.WHITE);
		}

		contentStream.close();
		addPageNumbers(document, "Page {0}", 60, 18);
		document.save("K:\\PDF\\result3\\myPdf.pdf");
		document.close();
		System.out.println("Document Generated Successfully!");
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
		int headerXPosition = xInitialPosition;
		int headerYPosition = yPosition + 10;

		private float headerFontSize = 18; // Adjust the header font size as needed
		private float bodyFontSize = 16; //

		public MytableClass(PDDocument document, PDPageContentStream contentStream) {
			this.document = document;
			this.contentStream = contentStream;
			this.headerFontSize = 18;
			this.bodyFontSize = 16;
		}

		void setTable(int[] colWidths, int cellHeight, int xPosition, int yPosition) {
			this.colWidths = colWidths;
			this.cellHeight = cellHeight;
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			xInitialPosition = xPosition;
		}

		void setTableFont(PDFont font, float headerFontSize, float bodyFontSize, Color fontColor) {
			this.font = font;
			this.headerFontSize = headerFontSize;
			this.bodyFontSize = bodyFontSize;
			this.fontColor = fontColor;
		}

		void printHeader(Color headerColor, Color textColor) throws IOException {
			contentStream.setFont(font, headerFontSize);

			int headerXPosition = xInitialPosition;
			int headerYPosition = yPosition - cellHeight; // Adjust to align the header properly
			float yOffset = (cellHeight - fontSize) / 2; // Adjust vertical offset for better alignment

			for (int i = 0; i < colWidths.length; i++) {
				contentStream.setNonStrokingColor(headerColor);
				contentStream.addRect(headerXPosition, headerYPosition, colWidths[i], cellHeight);
				contentStream.fillAndStroke();

				contentStream.beginText();
				contentStream.setNonStrokingColor(textColor);
				float textWidth = font.getStringWidth(getColumnName(i)) / 1000 * fontSize;
				float xTextPosition = headerXPosition + (colWidths[i] - textWidth) / 2; // Center text horizontally
				float yTextPosition = headerYPosition + yOffset; // Center text vertically
				contentStream.newLineAtOffset(xTextPosition, yTextPosition);
				contentStream.showText(getColumnName(i));
				contentStream.endText();

				headerXPosition += colWidths[i];
			}

			// Move to the next line after printing the header
			yPosition -= cellHeight;
		}

		void addRow(List<String> rowData, Color dataColor, Color textColor) throws IOException {
			if (rowData.size() != colWidths.length) {
				throw new IllegalArgumentException("Invalid row data");
			}

			// Check if there is enough space for the row, otherwise start a new page
			float rowHeight = calculateRowHeight(rowData);
			if (yPosition - rowHeight < 0) {
				contentStream.close();
				PDPage newPage = new PDPage(PDRectangle.A4);
				document.addPage(newPage);
				contentStream = new PDPageContentStream(document, newPage);
				printHeader(dataColor, textColor); // Print header on the new page
				yPosition = (int) (newPage.getTrimBox().getHeight() - 100); // Reset yPosition for the new page
			}

			for (int i = 0; i < colWidths.length; i++) {
				contentStream.setNonStrokingColor(dataColor);
				contentStream.addRect(xPosition, yPosition - rowHeight, colWidths[i], rowHeight);
				contentStream.fillAndStroke();

				contentStream.beginText();
				contentStream.setNonStrokingColor(textColor);
				float fontWidth = font.getStringWidth(rowData.get(i)) / 1000 * bodyFontSize;
				float xTextPosition = xPosition + (colWidths[i] - fontWidth) / 2; // Center text horizontally
				float yTextPosition = yPosition - rowHeight + (rowHeight - fontSize) / 2; // Center text vertically
				contentStream.newLineAtOffset(xTextPosition, yTextPosition);
				contentStream.showText(rowData.get(i));
				contentStream.endText();

				xPosition += colWidths[i];
			}

			xPosition = xInitialPosition;
			yPosition -= rowHeight;
		}

		private float calculateRowHeight(List<String> rowData) throws IOException {
			float maxHeight = 0;
			for (int i = 0; i < colWidths.length; i++) {
				float fontWidth = font.getStringWidth(rowData.get(i)) / 1000 * fontSize;
				float cellHeight = Math.max(CELL_MIN_HEIGHT, fontWidth / colWidths[i] * fontSize + 5);
				maxHeight = Math.max(maxHeight, cellHeight);
			}
			return maxHeight;
		}

		private static final float CELL_MIN_HEIGHT = 15f; // Minimum height for a cell

		private static final Map<Integer, String> COLUMN_NAMES = Map.of(0, "Sl.No", 1, "Items", 2, "Price", 3, "Qty", 4,
				"Total");

		private String getColumnName(int index) {
			return COLUMN_NAMES.getOrDefault(index, "");
		}

		public int getCellHeight() {
			return cellHeight;
		}
	}

	// Placeholder class, replace it with your actual InvoiceService
	private static class InvoiceService {
		public String getCustomerName() {
			return "Prasad Rathod";
		}

		public List<InvoiceItem> getInvoiceItems() {
			List<InvoiceItem> items = new ArrayList<>();

			for (int i = 1; i <= 40; i++) {
				String slNo = String.valueOf(i);
				String itemName = "Item " + i;
				String price = String.valueOf(100 + i); // Example: Price increases with each item
				String qty = String.valueOf(i);
				String total = String.valueOf(Integer.parseInt(price) * Integer.parseInt(qty));

				items.add(new InvoiceItem(slNo, itemName, price, qty, total));
			}

			return items;
		}
	}

	private static class InvoiceItem {
		private String slNo;
		private String item;
		private String price;
		private String qty;
		private String total;

		public InvoiceItem(String slNo, String item, String price, String qty, String total) {
			this.slNo = slNo;
			this.item = item;
			this.price = price;
			this.qty = qty;
			this.total = total;
		}

		public String getSlNo() {
			return slNo;
		}

		public String getItem() {
			return item;
		}

		public String getPrice() {
			return price;
		}

		public String getQty() {
			return qty;
		}

		public String getTotal() {
			return total;
		}
	}
}
