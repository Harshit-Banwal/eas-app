package com.legaldocs.eas.ai;

import org.springframework.stereotype.Service;

import com.legaldocs.eas.analysis.Clause;

@Service
public class RiskExplanationService {
	
	private final AiRiskExplanationRepository repo;
    private final LlmClient llmClient;

    public RiskExplanationService(
        AiRiskExplanationRepository repo,
        LlmClient llmClient
    ) {
        this.repo = repo;
        this.llmClient = llmClient;
    }
    
    public AiRiskExplanation explainRisk(Clause clause) {

        return repo.findByClauseId(clause.getId())
            .orElseGet(() -> {

                String prompt = RiskPromptBuilder.build(
                    clause.getOriginalText(),
                    clause.getRiskLevel()
                );

                String response = llmClient.generate(prompt);

                // TEMP simple parsing (can be improved later)
                AiRiskExplanation risk = new AiRiskExplanation();
                risk.setClauseId(clause.getId());
                risk.setExplanation(response);
                risk.setImpact("See explanation above.");
                risk.setSuggestion("Review and clarify before signing.");
                risk.setDisclaimer(
                    "AI-generated risk explanation. Not legal advice."
                );

                return repo.save(risk);
            });
    }
}
