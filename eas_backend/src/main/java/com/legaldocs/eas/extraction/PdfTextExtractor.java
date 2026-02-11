package com.legaldocs.eas.extraction;

import java.io.InputStream;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
public class PdfTextExtractor {
	
	public Map<Integer, String> extractTextByPage(InputStream inputStream) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            Map<Integer, String> pages = new LinkedHashMap<>();

            int totalPages = document.getNumberOfPages();
            for (int i = 1; i <= totalPages; i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                pages.put(i, stripper.getText(document));
            }
            return pages;
        } catch (Exception e) {
            throw new RuntimeException("PDF extraction failed", e);
        }
    }
}
