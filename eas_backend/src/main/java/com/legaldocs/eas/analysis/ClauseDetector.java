package com.legaldocs.eas.analysis;

import org.springframework.stereotype.Service;

@Service
public class ClauseDetector {
	
	public ClauseType detectClauseType(String text) {

        String lower = text.toLowerCase();

        if (lower.contains("termination") || lower.contains("terminate")) {
            return ClauseType.TERMINATION;
        }

        if (lower.contains("notice period") || lower.contains("notice")) {
            return ClauseType.NOTICE_PERIOD;
        }

        if (lower.contains("non-compete") || lower.contains("non compete")) {
            return ClauseType.NON_COMPETE;
        }

        if (lower.contains("confidential")) {
            return ClauseType.CONFIDENTIALITY;
        }

        if (lower.contains("intellectual property") || lower.contains("ip rights")) {
            return ClauseType.IP_RIGHTS;
        }

        return null; // unknown or irrelevant text
    }

}
