package com.trp.pdf_extractor_service.service;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PDFService {

    public static final String PDF_TEXT_EXTRACT_ERROR = "Failed to extract text from PDF";
    public static final String PDF_TABLE_EXTRACT_ERROR = "Failed to extract tables from PDF";


    public String extractText(final File pdfFile) {
        String extractedText = "";

        try (final PDDocument document = PDDocument.load(pdfFile)) {
            final PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        } catch (final Exception ex) {
            log.error(PDF_TEXT_EXTRACT_ERROR, ex);
            extractedText = PDF_TEXT_EXTRACT_ERROR;
        }

        return extractedText;
    }

    public String extractText(MultipartFile multipartFile) {
        StringBuilder extractedText = new StringBuilder();
        try (PDDocument document = PDDocument.load(multipartFile.getInputStream())) {
            document.getPages().forEach(page -> {
                try {
                    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                    stripper.setSortByPosition(true);
                    stripper.extractRegions(page);
                    extractedText.append(stripper.getTextForRegion("region"));
                } catch (IOException e) {
                    log.error(PDF_TEXT_EXTRACT_ERROR, e);
                }
            });
        } catch (Exception ex) {
            log.error(PDF_TEXT_EXTRACT_ERROR, ex);
            return PDF_TEXT_EXTRACT_ERROR;
        }
        return extractedText.toString();
    }

    public List<List<String>> extractTables(MultipartFile multipartFile) {
        List<List<String>> tables = new ArrayList<>();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            ObjectExtractor extractor = new ObjectExtractor(PDDocument.load(inputStream));
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            for (PageIterator it = extractor.extract(); it.hasNext(); ) {
                Page page = it.next();
                List<Table> pageTables = sea.extract(page);
                for (Table table : pageTables) {
                    List<String> tableData = new ArrayList<>();
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        StringBuilder rowText = new StringBuilder();
                        for (RectangularTextContainer cell : row) {
                            rowText.append(cell.getText()).append("\t");
                        }
                        tableData.add(rowText.toString());
                    }
                    tables.add(tableData);
                }
            }
        } catch (IOException e) {
            log.error(PDF_TABLE_EXTRACT_ERROR, e);
        }
        return tables;
    }




}
