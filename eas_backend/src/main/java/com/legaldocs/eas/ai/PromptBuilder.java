package com.legaldocs.eas.ai;

import java.util.List;

import com.legaldocs.eas.document.DocumentChunk;

public class PromptBuilder {
	
	public static String build(
            String clauseText,
            List<DocumentChunk> context
    ) {

		StringBuilder sb = new StringBuilder("""
				You are a legal contract explainer.

				Use ONLY the provided clause and related context.
				Do NOT add assumptions.

				Return STRICT JSON only.
				Do NOT include markdown.
				Do NOT include nested objects.
				Do NOT return arrays.
				The value of "plainEnglish" MUST be a single string.

				JSON format EXACTLY:

				{
				  "plainEnglish": "one single plain text paragraph explanation"
				}

				If unsure, still return a single string.

				CLAUSE:
				""").append(clauseText).append("\n\n");

				sb.append("RELATED CONTEXT:\n");
				for (DocumentChunk c : context) {
				    sb.append("- ").append(c.getChunkText()).append("\n");
				}

				sb.append("\nReturn valid JSON only.");

				return sb.toString();

    }
}
