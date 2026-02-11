package com.legaldocs.eas.ai;

import com.legaldocs.eas.analysis.RiskLevel;

public class RiskPromptBuilder {
	public static String build(String clauseText, RiskLevel riskLevel) {

        return """
			You are a legal risk analyst.
			You are NOT a lawyer.
			Explain risk ONLY using the clause text.
			
			CLAUSE:
			""" + clauseText + """
			
			RISK LEVEL: """ + riskLevel + """
			
			Return exactly:
			1. Why this clause is risky
			2. What negative impact it can have on the employee
			3. What the employee should double-check or negotiate
			
			If risk is LOW, explain why it is relatively safe.
			Do NOT add assumptions.
			""";
    }
}
