package com.legaldocs.eas.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ClauseDetector {
	
	private static final Map<ClauseType, List<String>> RULES = new HashMap<>() {{
        put(ClauseType.TERMINATION, List.of("termination", "terminate", "terminated"));
        put(ClauseType.NOTICE_PERIOD, List.of("notice period", "written notice", "prior notice"));
        put(ClauseType.COMPENSATION, List.of("salary", "compensation", "remuneration", "wages", "ctc"));
        put(ClauseType.WORK_HOURS, List.of("working hours", "work hours", "office hours", "shift"));
        put(ClauseType.CONFIDENTIALITY, List.of("confidential", "non-disclosure", "nda"));
        put(ClauseType.NON_COMPETE, List.of("non-compete", "non compete"));
        put(ClauseType.NON_SOLICITATION, List.of("non-solicit", "non solicit"));
        put(ClauseType.INTELLECTUAL_PROPERTY, List.of("intellectual property", "ip rights", "work product", "ownership"));
        put(ClauseType.LEAVE_POLICY, List.of("leave", "annual leave", "paid leave", "sick leave"));
        put(ClauseType.PROBATION, List.of("probation", "probationary period"));
        put(ClauseType.GOVERNING_LAW, List.of("governing law"));
        put(ClauseType.JURISDICTION, List.of("jurisdiction", "courts of"));
        put(ClauseType.DISPUTE_RESOLUTION, List.of("dispute", "arbitration", "mediation"));
        put(ClauseType.INDEMNIFICATION, List.of("indemnify", "indemnification"));
        put(ClauseType.LIABILITY, List.of("liability", "liable", "damages"));
        put(ClauseType.DATA_PROTECTION, List.of("data protection", "privacy", "personal data"));
        put(ClauseType.ASSIGNMENT, List.of("assignment", "assign this agreement"));
        put(ClauseType.AMENDMENT, List.of("amendment", "modified in writing"));
        put(ClauseType.FORCE_MAJEURE, List.of("force majeure", "act of god"));
	}};

	
	public ClauseType detectClauseType(String text) {
	
	    if (text == null || text.isBlank()) {
	        return ClauseType.OTHER;
	    }
	
	    String lower = text.toLowerCase();
	
	    for (Map.Entry<ClauseType, List<String>> entry : RULES.entrySet()) {
	        for (String keyword : entry.getValue()) {
	            if (lower.contains(keyword)) {
	                return entry.getKey();
	            }
	        }
	    }
	
	    return ClauseType.OTHER;
	}

}
