package com.trp.pdf_extractor_service.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.*;
import java.io.IOException;

public class CustomPDFTextStripper extends PDFTextStripperByArea {

    public CustomPDFTextStripper() throws IOException {
        super();
    }

    public String extractTextFromArea(PDDocument document, int page, Rectangle area) throws IOException {
        addRegion("targetArea", area);
        extractRegions(document.getPage(page - 1)); // PDFBox uses 0-based index for pages
        return getTextForRegion("targetArea");
    }
}

