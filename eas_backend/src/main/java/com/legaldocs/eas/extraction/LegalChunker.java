package com.legaldocs.eas.extraction;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class LegalChunker {
	
	// Split BEFORE clause numbers: 1. 2. 3. etc.
    private static final String CLAUSE_SPLIT_REGEX =
            "(?=\\b\\d+\\.\\s)";

    public List<String> chunk(String text) {
        List<String> chunks = new ArrayList<>();

        String normalized = text
                .replaceAll("\\s+", " ")
                .trim();

        String[] parts = normalized.split(CLAUSE_SPLIT_REGEX);

        for (String part : parts) {
            String cleaned = part.trim();
            if (cleaned.length() > 40) {
                chunks.add(cleaned);
            }
        }
        return chunks;
    }
}
