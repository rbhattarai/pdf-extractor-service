package com.trp.pdf_extractor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PDFContentDTO {

    private String textContent;

    private List<List<String>> tables;

    private List<String> images;
}
