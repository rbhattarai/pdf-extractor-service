package com.trp.pdf_extractor_service.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class PDFService {

    private final String PDF_TEXT_EXTRACT_ERROR = "Failed to extract text from PDF";

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
}
