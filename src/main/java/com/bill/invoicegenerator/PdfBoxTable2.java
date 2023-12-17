package com.bill.invoicegenerator;

import be.quodlibet.boxable.*;
import be.quodlibet.boxable.line.LineStyle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;

public class PdfBoxTable2 {
	public static void main(String[] args) throws IOException {
		String outputFileName = "K:\\PDF\\results\\myPdf2.pdf";
		if (args.length > 0)
			outputFileName = args[0];

		// Create a new font object selecting one of the PDF base fonts
		PDFont fontPlain = PDType1Font.TIMES_ROMAN;
	

		// Create a document and add a page to it
		PDDocument document = new PDDocument();
		PDRectangle landscape = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
		PDPage page = new PDPage(landscape);

		// PDRectangle.LETTER and others are also possible
		PDRectangle rect = page.getMediaBox();
		// rect can be used to get the page width and height
		document.addPage(page);

		// Start a new content stream which will "hold" the to be created content
		PDPageContentStream cos = new PDPageContentStream(document, page);

		// Dummy Table
		float margin = 10;
		// starting y position is whole page height subtracted by top and bottom margin
		float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
		// we want table across whole page width (subtracted by left and right margin
		// ofcourse)
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

		boolean drawContent = true;
		float yStart = yStartNewPage;
		float bottomMargin = 70;
		// y position is your coordinate of top left corner of the table
		float yPosition = 550;

		BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, margin, document, page,
				true, drawContent);

		// the parameter is the row height
		Row<PDPage> headerRow = table.createRow(0);
		// the first parameter is the cell width
		Cell<PDPage> serial = headerRow.createCell(5, "ser");
		Cell<PDPage> startdate = headerRow.createCell(10, "startdate");
		Cell<PDPage> enddate = headerRow.createCell(10, "enddate");
		Cell<PDPage> columns1 = headerRow.createCell(15, "remarks");
		Cell<PDPage> columns2 = headerRow.createCell(15, "another start date");
		Cell<PDPage> columns3 = headerRow.createCell(17, "another remarks and");
		Cell<PDPage> columns4 = headerRow.createCell(12, "columns15");
		Cell<PDPage> column5 = headerRow.createCell(12, "columns16");

		serial.setFont(fontPlain);
		serial.setFontSize(18);
		startdate.setFontSize(18);
		enddate.setFontSize(18);
		columns1.setFontSize(18);
		columns2.setFontSize(18);
		columns3.setFontSize(18);
		columns4.setFontSize(18);
		column5.setFontSize(18);

		table.addHeaderRow(headerRow);

		Row<PDPage> row = table.createRow(20);
	    serial = row.createCell(5, "1");
		startdate = row.createCell(10,"10 APR 24");
		startdate = row.createCell(10,"15 APR 24");
		startdate = row.createCell(15,"10 APR 24 some text");
		startdate = row.createCell(15,"text wrapping is some hectic task here");
		startdate = row.createCell(17,"columns15");
		startdate = row.createCell(12,"columns15");
		startdate = row.createCell(12,"columns15");

		table.draw();

		float tableHeight = table.getHeaderAndDataHeight();
		System.out.println("tableHeight = " + tableHeight);

		// close the content stream
		cos.close();

		// Save the results and ensure that the document is properly closed:
		document.save(outputFileName);
		document.close();
	}
}
