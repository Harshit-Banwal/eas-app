package com.legaldocs.eas.ai;

import java.util.List;

import com.legaldocs.eas.document.DocumentChunk;

public class PromptBuilder {
	
	public static String build(
	        String clauseText,
	        List<DocumentChunk> context
	    ) {
	        StringBuilder sb = new StringBuilder();

	        sb.append("""
			You are a legal document explainer.
			You are NOT a lawyer.
			Explain ONLY using the provided text.
			Do NOT add assumptions.
		
			CLAUSE:
			""").append(clauseText).append("\n\n");

	        sb.append("RELATED CONTEXT:\n");
	        for (DocumentChunk c : context) {
	            sb.append("- ").append(c.getChunkText()).append("\n");
	        }

	        sb.append("""
			Return:
			- Plain English explanation
			- Who it favors
			- One-line risk warning
			""");
	        
	        sb.append("""
    		If the answer is not fully supported by the text, say:
    		"Not enough information in the document."
    		""");

	        return sb.toString();
	    }
}
