package com.legaldocs.eas.ai;

import com.legaldocs.eas.analysis.RiskLevel;

public class RiskPromptBuilder {
	public static String build(String clauseText, RiskLevel riskLevel) {

		return """
				You are a legal risk analyst.

				Use ONLY the provided clause text.
				Do NOT add assumptions.

				Return STRICT JSON only.
				Do NOT include markdown.
				Do NOT include nested objects.
				Do NOT include arrays.
				All values MUST be single plain text strings.
				Do NOT structure the response.

				JSON format EXACTLY:

				{
				  "explanation": "one paragraph explaining why risky",
				  "impact": "one paragraph explaining impact",
				  "suggestion": "one paragraph suggesting what to negotiate"
				}

				CLAUSE:
				""" + clauseText + """

				RISK LEVEL: """ + riskLevel + """

				Return valid JSON only.
				""";
    }
}
