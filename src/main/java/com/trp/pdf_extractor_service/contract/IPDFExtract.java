package com.trp.pdf_extractor_service.contract;

public interface IPDFExtract {
    String extractText();

    String extractImages();

    String extractTables();
}
