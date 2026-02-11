package com.legaldocs.eas.ai;

import java.util.List;

import com.legaldocs.eas.document.DocumentChunk;

public class DocumentSummaryPromptBuilder {
	public static String build(List<DocumentChunk> chunks) {

        StringBuilder sb = new StringBuilder("""
			You are a legal document summarizer.
			You are NOT a lawyer.
			Use ONLY the provided text.
			Do NOT add assumptions.
			
			DOCUMENT CONTEXT:
		""");

        for (DocumentChunk c : chunks) {
            sb.append("- ").append(c.getChunkText()).append("\n");
        }

        sb.append("""
			Return:
			1. Plain English summary (5–6 lines)
			2. Key risks (bulleted)
			3. Who the document favors (Employer / Employee / Neutral)
        """);
        
        sb.append("""
        		If the answer is not fully supported by the text, say:
        		"Not enough information in the document."
        """);

        return sb.toString();
    }
}
