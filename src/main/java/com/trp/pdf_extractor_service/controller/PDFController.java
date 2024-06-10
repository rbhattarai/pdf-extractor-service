package com.trp.pdf_extractor_service.controller;

import com.trp.pdf_extractor_service.dto.PDFContentDTO;
import com.trp.pdf_extractor_service.service.PDFService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class PDFController {

    private final PDFService pdfService;

    @PostMapping(path = "/extract-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PDFContentDTO> extractText(@NonNull @RequestParam("file") final MultipartFile pdfFile) {
        return ResponseEntity.ok().body(PDFContentDTO.builder().textContent(this.pdfService.extractText(pdfFile)).build());
    }

}
