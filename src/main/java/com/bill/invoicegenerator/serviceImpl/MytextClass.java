package com.bill.invoicegenerator.serviceImpl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;

public class MytextClass {
    private final PDDocument document;
    private final PDPageContentStream contentStream;

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
