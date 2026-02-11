package com.legaldocs.eas.common;

import java.util.List;

import org.springframework.stereotype.Service;

import com.legaldocs.eas.document.DocumentChunk;

@Service
public class Helper {
	
	public boolean isHeading(String text) {
	    String trimmed = text.trim();

	    // 1️⃣ Numbered clauses are NOT headings
	    if (trimmed.matches("^\\d+\\.\\s.*")) {
	        return false;
	    }

	    // 2️⃣ Real headings are short + ALL CAPS
	    if (trimmed.length() < 80 && trimmed.equals(trimmed.toUpperCase())) {
	        return true;
	    }

	    return false;
	}
	
	
	public float cosineSimilarity(float[] a, float[] b) {
        float dot = 0f, normA = 0f, normB = 0f;

        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (float)(Math.sqrt(normA) * Math.sqrt(normB));
    }
	
	public String toVector(float[] vector) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i < vector.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
	
	public float confidenceCalculator(
            String clauseText,
            List<DocumentChunk> context
    ) {
        int clauseLength = clauseText.length();
        int contextCount = context.size();

        float score = 0.0f;

        // Clause clarity
        if (clauseLength > 300) score += 0.35f;
        else if (clauseLength > 150) score += 0.25f;
        else score += 0.15f;

        // Context support
        if (contextCount >= 3) score += 0.30f;
        else if (contextCount == 2) score += 0.20f;
        else score += 0.10f;

        // Penalize ambiguity
        if (clauseText.toLowerCase().contains("as applicable")
            || clauseText.toLowerCase().contains("may be")
            || clauseText.toLowerCase().contains("subject to")) {
            score -= 0.10f;
        }

        return Math.min(Math.max(score, 0.4f), 0.95f);
    }
}
