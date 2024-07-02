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

    public static final String ERROR_MESSAGE_FAILED_DELETE = "Failed to delete temporary file: {}";
    public static final String ERROR_MESSAGE_PDF_PROCESS = "Error processing PDF file";

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
            log.error(ERROR_MESSAGE_PDF_PROCESS, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if(convFile != null && convFile.exists()) {
                if(!convFile.delete()) {
                    log.warn(ERROR_MESSAGE_FAILED_DELETE, convFile.getAbsolutePath());
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

        File convFile = null;
        try {
            convFile = File.createTempFile("temp", ".pdf");
            pdfFile.transferTo(convFile);
            var tables = pdfService.extractTables(convFile);
            var pdfContentDTO = PDFContentDTO.builder().tables(tables).build();
            return ResponseEntity.ok().body(pdfContentDTO);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE_PDF_PROCESS, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if(convFile != null && convFile.exists()) {
                if(!convFile.delete()) {
                    log.warn(ERROR_MESSAGE_FAILED_DELETE, convFile.getAbsolutePath());
                }
            }
        }
    }

    @PostMapping(path = "/extract-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PDFContentDTO> extractImages(@NonNull @RequestParam("file") final MultipartFile pdfFile) {
        if (pdfFile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        File convFile = null;
        try {
            convFile = File.createTempFile("temp", ".pdf");
            pdfFile.transferTo(convFile);
            var images = pdfService.extractImages(convFile);
            var pdfContentDTO = PDFContentDTO.builder().images(images).build();
            return ResponseEntity.ok().body(pdfContentDTO);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE_PDF_PROCESS, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if(convFile != null && convFile.exists()) {
                if(!convFile.delete()) {
                    log.warn(ERROR_MESSAGE_FAILED_DELETE, convFile.getAbsolutePath());
                }
            }
        }
    }

    @PostMapping(path = "/extract-all", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PDFContentDTO> extractAll(@NonNull @RequestParam("file") final MultipartFile pdfFile) {
        if (pdfFile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        File convFile = null;
        try {
            convFile = File.createTempFile("temp", ".pdf");
            pdfFile.transferTo(convFile);
            var allContents = pdfService.extractAll(convFile);
            return ResponseEntity.ok().body(allContents);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE_PDF_PROCESS, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if(convFile != null && convFile.exists()) {
                if(!convFile.delete()) {
                    log.warn(ERROR_MESSAGE_FAILED_DELETE, convFile.getAbsolutePath());
                }
            }
        }
    }

    @PostMapping(path = "/extract-section", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PDFContentDTO> extractTextFromSection(
            @NonNull @RequestParam("file") final MultipartFile pdfFile,
            @RequestParam("page") int page,
            @RequestParam("x") int x,
            @RequestParam("y") int y,
            @RequestParam("width") int width,
            @RequestParam("height") int height) {
        if (pdfFile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        File convFile = null;
        try {
            convFile = File.createTempFile("temp", ".pdf");
            pdfFile.transferTo(convFile);

            var pdfContent = pdfService.extractTextFromSection(convFile, page, x, y, width, height);
            var pdfContentDTO = PDFContentDTO.builder().textContent(pdfContent).build();
            return ResponseEntity.ok().body(pdfContentDTO);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE_PDF_PROCESS, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (convFile != null && convFile.exists()) {
                if (!convFile.delete()) {
                    log.warn(ERROR_MESSAGE_FAILED_DELETE, convFile.getAbsolutePath());
                }
            }
        }
    }


}
