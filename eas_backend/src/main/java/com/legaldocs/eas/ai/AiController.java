package com.legaldocs.eas.ai;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.legaldocs.eas.analysis.Clause;
import com.legaldocs.eas.analysis.ClauseRepository;
import com.legaldocs.eas.common.CurrentUser;
import com.legaldocs.eas.document.DocumentRepository;

@RestController
@RequestMapping("/api/ai")
public class AiController {
	
	private final ClauseRepository clauseRepository;
    private final AiExplanationService aiService;
    private final RiskExplanationService riskService;
    private final DocumentRepository documentRepository;

    public AiController(
            ClauseRepository clauseRepository,
            AiExplanationService aiService,
            RiskExplanationService riskService,
            DocumentRepository documentRepository
    ) {
        this.clauseRepository = clauseRepository;
        this.aiService = aiService;
        this.riskService = riskService;
        this.documentRepository = documentRepository;
    }
    
    @PostMapping("/explain-clause/{clauseId}")
    public AiExplanation explainClause(@PathVariable UUID clauseId) {

        Clause clause = clauseRepository.findById(clauseId)
                .orElseThrow(() -> new IllegalArgumentException("Clause not found"));
        
        UUID userId = CurrentUser.id();

        documentRepository.findByIdAndUserId(
                clause.getDocumentId(),
                userId
            )
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN)
            );

        return aiService.explainClause(clause);
    }
    

    @PostMapping("/explain-risk/{clauseId}")
    public AiRiskExplanation explainRisk(
            @PathVariable UUID clauseId
    ) {
        Clause clause = clauseRepository.findById(clauseId)
            .orElseThrow(() -> new IllegalArgumentException("Clause not found"));
        
        UUID userId = CurrentUser.id();

        documentRepository.findByIdAndUserId(
                clause.getDocumentId(),
                userId
            )
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN)
            );


        return riskService.explainRisk(clause);
    }
}
