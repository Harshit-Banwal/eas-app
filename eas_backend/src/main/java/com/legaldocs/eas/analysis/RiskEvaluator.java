package com.legaldocs.eas.analysis;

import org.springframework.stereotype.Service;

@Service
public class RiskEvaluator {
	
	public RiskLevel evaluateRisk(ClauseType type, String text) {

        if (text == null || text.isBlank()) {
            return RiskLevel.LOW;
        }

        String lower = text.toLowerCase();

        return switch (type) {

            case TERMINATION -> evaluateTermination(lower);

            case NOTICE_PERIOD -> evaluateNotice(lower);

            case NON_COMPETE -> evaluateNonCompete(lower);

            case NON_SOLICITATION -> evaluateNonSolicitation(lower);

            case COMPENSATION -> evaluateCompensation(lower);

            case WORK_HOURS -> evaluateWorkHours(lower);

            case CONFIDENTIALITY -> evaluateConfidentiality(lower);

            case INTELLECTUAL_PROPERTY -> evaluateIP(lower);

            case LEAVE_POLICY -> evaluateLeave(lower);

            case PROBATION -> evaluateProbation(lower);

            case GOVERNING_LAW -> RiskLevel.LOW;

            case JURISDICTION -> RiskLevel.MEDIUM;

            case DISPUTE_RESOLUTION -> evaluateDispute(lower);

            case INDEMNIFICATION -> evaluateIndemnity(lower);

            case LIABILITY -> evaluateLiability(lower);

            case DATA_PROTECTION -> RiskLevel.MEDIUM;

            case ASSIGNMENT -> RiskLevel.MEDIUM;

            case AMENDMENT -> evaluateAmendment(lower);

            case FORCE_MAJEURE -> RiskLevel.LOW;

            case OTHER -> RiskLevel.LOW;
        };
    }
	
	private RiskLevel evaluateTermination(String lower) {
        if (containsAny(lower,
                "without notice",
                "at any time",
                "without cause")) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.MEDIUM;
    }

    private RiskLevel evaluateNotice(String lower) {
        if (containsAny(lower,
                "immediate termination",
                "no notice required")) {
            return RiskLevel.HIGH;
        }
        if (containsAny(lower,
                "one month",
                "30 days")) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private RiskLevel evaluateNonCompete(String lower) {
        if (containsAny(lower,
                "worldwide",
                "anywhere",
                "for life",
                "perpetual")) {
            return RiskLevel.HIGH;
        }
        if (containsAny(lower,
                "12 months",
                "24 months")) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private RiskLevel evaluateNonSolicitation(String lower) {
        if (containsAny(lower,
                "customers",
                "clients",
                "employees for 2 years")) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private RiskLevel evaluateCompensation(String lower) {
        if (containsAny(lower,
                "discretion",
                "at company’s sole discretion",
                "subject to change")) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.MEDIUM;
    }

    private RiskLevel evaluateWorkHours(String lower) {
        if (containsAny(lower,
                "extended hours",
                "overtime without compensation")) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.MEDIUM;
    }

    private RiskLevel evaluateConfidentiality(String lower) {
        if (containsAny(lower,
                "perpetual",
                "indefinite")) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private RiskLevel evaluateIP(String lower) {
        if (containsAny(lower,
                "all inventions",
                "before employment",
                "assign all rights")) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.MEDIUM;
    }

    private RiskLevel evaluateLeave(String lower) {
        if (containsAny(lower,
                "company discretion",
                "may deny leave")) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private RiskLevel evaluateProbation(String lower) {
        if (containsAny(lower,
                "extended probation",
                "at company discretion")) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private RiskLevel evaluateDispute(String lower) {
        if (containsAny(lower,
                "binding arbitration",
                "no appeal")) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private RiskLevel evaluateIndemnity(String lower) {
        if (containsAny(lower,
                "unlimited liability",
                "indemnify the company")) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.MEDIUM;
    }

    private RiskLevel evaluateLiability(String lower) {
        if (containsAny(lower,
                "unlimited",
                "full responsibility")) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.MEDIUM;
    }

    private RiskLevel evaluateAmendment(String lower) {
        if (containsAny(lower,
                "company may amend",
                "without consent")) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.MEDIUM;
    }
    
    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
