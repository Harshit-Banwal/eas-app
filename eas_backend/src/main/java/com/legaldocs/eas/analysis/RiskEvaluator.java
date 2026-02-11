package com.legaldocs.eas.analysis;

import org.springframework.stereotype.Service;

@Service
public class RiskEvaluator {
	
	public RiskLevel evaluateRisk(ClauseType type, String text) {

        String lower = text.toLowerCase();

        switch (type) {

            case TERMINATION:
                if (lower.contains("without notice") ||
                    lower.contains("at any time")) {
                    return RiskLevel.HIGH;
                }
                return RiskLevel.MEDIUM;

            case NON_COMPETE:
                if (lower.contains("anywhere") ||
                    lower.contains("worldwide")) {
                    return RiskLevel.HIGH;
                }
                return RiskLevel.MEDIUM;

            case NOTICE_PERIOD:
                return RiskLevel.MEDIUM;

            default:
                return RiskLevel.LOW;
        }
    }
}
