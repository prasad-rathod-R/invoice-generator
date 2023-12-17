package com.bill.invoicegenerator.serviceImpl;

import be.quodlibet.boxable.*;
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
import com.bill.invoicegenerator.service.PdfBoxTableService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfBoxTableServiceImpl implements PdfBoxTableService {
	@Autowired
	UsersRepository userrewpo;

	@Override
	public ResponseEntity<Map<String, Object>> generatePdf2(UserRequest request) {
		try {
			String outputFileName = "K:\\PDF\\results\\myPdf2.pdf";
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
//			Cell<PDPage> serial = headerRow.createCell(5, "ser");
//			Cell<PDPage> startdate = headerRow.createCell(10, "startdate");
//			Cell<PDPage> enddate = headerRow.createCell(10, "enddate");
//			Cell<PDPage> columns1 = headerRow.createCell(15, "remarks");
//			Cell<PDPage> columns2 = headerRow.createCell(15, "another start date");
//			Cell<PDPage> columns3 = headerRow.createCell(17, "another remarks and");
//			Cell<PDPage> columns4 = headerRow.createCell(12, "columns15");
//			Cell<PDPage> column5 = headerRow.createCell(12, "columns16");
			Cell<PDPage> serial = headerRow.createCell(5, "ser");
			Cell<PDPage> name = headerRow.createCell(10, "name");
			Cell<PDPage> age = headerRow.createCell(10, "age");
			Cell<PDPage> email = headerRow.createCell(15, "email");
			Cell<PDPage> city = headerRow.createCell(15, "city");
			Cell<PDPage> dish = headerRow.createCell(17, "dish");

			serial.setFont(fontPlain);
			serial.setFontSize(18);
//			startdate.setFontSize(18);
//			enddate.setFontSize(18);
//			columns1.setFontSize(18);
//			columns2.setFontSize(18);
//			columns3.setFontSize(18);
//			columns4.setFontSize(18);
//			column5.setFontSize(18);

			table.addHeaderRow(headerRow);
			//Row<PDPage> row = table.createRow(20);

			List<Object[]> getAllTableDataQuery = userrewpo.getTableData(request.getId());
			int serialMumber = 1;
			for (Object[] arr : getAllTableDataQuery) {
				
				Row<PDPage> row = table.createRow(20);

				serial = row.createCell(5, String.valueOf(serialMumber));

				name = row.createCell(10, arr[0] != null ? arr[0].toString() : "N/A");
				age = row.createCell(10, arr[1] != null ? arr[1].toString() : "N/A");
				email = row.createCell(15, arr[2] != null ? arr[2].toString() : "N/A");
				city = row.createCell(15, arr[3] != null ? arr[3].toString() : "N/A");
				dish = row.createCell(17, arr[4] != null ? arr[4].toString() : "N/A");

			}

			table.draw();

			float tableHeight = table.getHeaderAndDataHeight();
			System.out.println("tableHeight = " + tableHeight);

			// close the content stream
			cos.close();

			// Save the results and ensure that the document is properly closed:
			document.save(outputFileName);
			document.close();

			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("message", "PDF generated successfully.");
			// You can add additional information to the response map if needed.

			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();

			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("message", "Error generating PDF: " + e.getMessage());

			return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
