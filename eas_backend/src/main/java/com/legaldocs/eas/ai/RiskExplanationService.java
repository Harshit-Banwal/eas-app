package com.legaldocs.eas.ai;

import org.springframework.stereotype.Service;

import com.legaldocs.eas.analysis.Clause;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

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
                
                response = response.replace("```json", "")
                        .replace("```", "")
                        .trim();

			     ObjectMapper mapper = new ObjectMapper();
			
			     String explanation = "";
			     String impact = "";
			     String suggestion = "";
			
			     try {
		    	    JsonNode node = mapper.readTree(response);

		    	    JsonNode explanationNode = node.get("explanation");
		    	    JsonNode impactNode = node.get("impact");
		    	    JsonNode suggestionNode = node.get("suggestion");

		    	    explanation = extractTextSafely(explanationNode);
		    	    impact = extractTextSafely(impactNode);
		    	    suggestion = extractTextSafely(suggestionNode);

		    	} catch (Exception e) {
		    	    explanation = response; // fallback
		    	}

                // TEMP simple parsing (can be improved later)
                AiRiskExplanation risk = new AiRiskExplanation();
                risk.setClauseId(clause.getId());
                risk.setExplanation(explanation);
                risk.setImpact(impact);
                risk.setSuggestion(suggestion);
                risk.setDisclaimer(
                    "AI-generated risk explanation. Not legal advice."
                );

                return repo.save(risk);
            });
    }
    
    private String extractTextSafely(JsonNode node) {
        if (node == null) return "";
        if (node.isString()) return node.asString();
        return node.toString(); // fallback if model misbehaves
    }
}
