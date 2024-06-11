package com.trp.pdf_extractor_service.controller;

import com.trp.pdf_extractor_service.dto.PDFContentDTO;
import com.trp.pdf_extractor_service.service.PDFService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class PDFController {

    private final PDFService pdfService;

    @PostMapping(path = "/extract-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PDFContentDTO> extractText(@NonNull @RequestParam("file") final MultipartFile pdfFile) {
        if (pdfFile.isEmpty()) {
            return new ResponseEntity<PDFContentDTO>(HttpStatus.BAD_REQUEST);
        }
        File convFile = null;
        try {
            convFile = File.createTempFile("temp", ".pdf");
            pdfFile.transferTo(convFile);

            var pdfContent = this.pdfService.extractText(convFile);
            var pdfContentDTO = PDFContentDTO.builder().textContent(pdfContent).build();
            return ResponseEntity.ok().body(pdfContentDTO);
        } catch (Exception e) {
            log.error("Error processing PDF file: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if(convFile != null && convFile.exists()) {
                if(!convFile.delete()) {
                    log.warn("Failed to delete temporary file: {}", convFile.getAbsolutePath());
                }
            }
        }
    }

    @PostMapping(path = "/extract-tables", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PDFContentDTO> extractTables(@NonNull @RequestParam("file") final MultipartFile pdfFile) {
        if (pdfFile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            var tables = pdfService.extractTables(pdfFile);
            var pdfContentDTO = PDFContentDTO.builder().tables(tables).build();
            return ResponseEntity.ok().body(pdfContentDTO);
        } catch (Exception e) {
            log.error("Error processing PDF file: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
