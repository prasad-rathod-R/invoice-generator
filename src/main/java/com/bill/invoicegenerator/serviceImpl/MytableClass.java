package com.bill.invoicegenerator.serviceImpl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MytableClass {
	private final PDDocument document;
	private final PDPageContentStream contentStream;
	private int[] colWidths;
	private int cellHeight;
	private int yPosition;
	private int xPosition;
	private int xInitialPosition;
	private PDFont font;
	private Color fontColor;
	private float fontSize;

	private static final float CELL_MIN_HEIGHT = 15f;

	public MytableClass(PDDocument document, PDPageContentStream contentStream) {
		this.document = document;
		this.contentStream = contentStream;
	}

	void setTable(int[] colWidths, int cellHeight, int xPosition, int yPosition) {
		this.colWidths = colWidths;
		this.cellHeight = cellHeight;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.xInitialPosition = xPosition;
	}

	void setTableFont(PDFont font, float fontSize, Color fontColor) {
		this.font = font;
		this.fontSize = fontSize;
		this.fontColor = fontColor;
	}

	void printHeader(Color headerColor, Color textColor) throws IOException {
		contentStream.setFont(font, fontSize);

		int headerXPosition = xInitialPosition;
		int headerYPosition = yPosition - cellHeight;
		float yOffset = (cellHeight - fontSize) / 2;

		for (int i = 0; i < colWidths.length; i++) {
			contentStream.setNonStrokingColor(headerColor);
			contentStream.addRect(headerXPosition, headerYPosition, colWidths[i], cellHeight);
			contentStream.fillAndStroke();

			contentStream.beginText();
			contentStream.setNonStrokingColor(textColor);
			float textWidth = font.getStringWidth(getColumnName(i)) / 1000 * fontSize;
			float xTextPosition = headerXPosition + (colWidths[i] - textWidth) / 2;
			float yTextPosition = headerYPosition + yOffset;
			contentStream.newLineAtOffset(xTextPosition, yTextPosition);
			contentStream.showText(getColumnName(i));
			contentStream.endText();

			headerXPosition += colWidths[i];
		}

		yPosition -= cellHeight;
	}

	void addRow(List<String> rowData, Color dataColor, Color textColor) throws IOException {
		if (rowData.size() != colWidths.length) {
			throw new IllegalArgumentException("Invalid row data");
		}

		float rowHeight = calculateRowHeight(rowData);
		if (yPosition - rowHeight < 0) {
			contentStream.close();
			// Add logic to start a new page if needed
			// PDPage newPage = new PDPage(PDRectangle.A4);
			// document.addPage(newPage);
			// contentStream = new PDPageContentStream(document, newPage);
			// printHeader(dataColor, textColor);
			// yPosition = (int) (newPage.getTrimBox().getHeight() - 100);
		}

		for (int i = 0; i < colWidths.length; i++) {
			contentStream.setNonStrokingColor(dataColor);
			contentStream.addRect(xPosition, yPosition - rowHeight, colWidths[i], rowHeight);
			contentStream.fillAndStroke();

			contentStream.beginText();
			contentStream.setNonStrokingColor(textColor);
			float fontWidth = font.getStringWidth(rowData.get(i)) / 1000 * fontSize;
			float xTextPosition = xPosition + (colWidths[i] - fontWidth) / 2;
			float yTextPosition = yPosition - rowHeight + (rowHeight - fontSize) / 2;
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

	private static final Map<Integer, String> COLUMN_NAMES = Map.of(0, "Sl.No", 1, "Items", 2, "Price", 3, "Qty", 4,
			"Total");

	private String getColumnName(int index) {
		return COLUMN_NAMES.getOrDefault(index, "");
	}
}
