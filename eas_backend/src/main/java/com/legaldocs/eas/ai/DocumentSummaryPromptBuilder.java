package com.legaldocs.eas.ai;

import java.util.List;

import com.legaldocs.eas.document.DocumentChunk;

public class DocumentSummaryPromptBuilder {
	public static String build(List<DocumentChunk> chunks) {

        StringBuilder sb = new StringBuilder("""
		You are a legal document analyzer.
		
		Use ONLY the provided text.
		Do NOT add assumptions.
		Return STRICT JSON only.
		Do NOT include markdown.
		Do NOT include explanations outside JSON.
		
		JSON format:
		
		{
		  "summary": "5-6 sentence plain English summary",
		  "risks": [
		    "risk 1",
		    "risk 2"
		  ],
		  "favors": "Employer | Employee | Neutral"
		}
		
		DOCUMENT CONTEXT:
		""");

        for (DocumentChunk c : chunks) {
            sb.append("- ").append(c.getChunkText()).append("\n");
        }

        sb.append("""
		If insufficient information exists, still return valid JSON.
		""");

        return sb.toString();
    }
}
