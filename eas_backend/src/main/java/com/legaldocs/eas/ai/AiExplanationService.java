package com.legaldocs.eas.ai;

import java.util.List;

import org.springframework.stereotype.Service;

import com.legaldocs.eas.analysis.Clause;
import com.legaldocs.eas.common.Helper;
import com.legaldocs.eas.document.DocumentChunk;
import com.legaldocs.eas.extraction.ChunkRetrievalService;
import com.legaldocs.eas.extraction.EmbeddingService;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class AiExplanationService {
	
	private final AiExplanationRepository aiRepository;
	private final EmbeddingService embeddingService;
    private final ChunkRetrievalService retrievalService;
    private final LlmClient llmClient;
    private final Helper helper;

    public AiExplanationService(
            AiExplanationRepository aiRepository,
            EmbeddingService embeddingService,
            ChunkRetrievalService retrievalService,
            LlmClient llmClient,
            Helper helper
        ) {
            this.aiRepository = aiRepository;
            this.embeddingService = embeddingService;
            this.retrievalService = retrievalService;
            this.llmClient = llmClient;
            this.helper = helper;
        }
    
    public AiExplanation explainClause(Clause clause) {

        return aiRepository.findByClauseId(clause.getId())
                .orElseGet(() -> {

                	float[] queryEmbedding =
                            embeddingService.embed(clause.getOriginalText());

                        List<DocumentChunk> context =
                            retrievalService.retrieve(
                                clause.getDocumentId(),
                                queryEmbedding
                            );

                        String prompt = PromptBuilder.build(
                            clause.getOriginalText(),
                            context
                        );

                        String answer = llmClient.generate(prompt);
                        
                        answer = answer.replace("```json", "")
                                .replace("```", "")
                                .trim();
                        
                        ObjectMapper mapper = new ObjectMapper();

                        String explanation;

                        try {
                            JsonNode node = mapper.readTree(answer).get("plainEnglish");
                            
                            if (node == null) {
                                explanation = answer;
                            } else if (node.isString()) {
                                explanation = node.asString();
                            } else {
                                // If model still returns object → convert object to readable text
                                explanation = node .toString();
                            }
                        } catch (Exception e) {
                            explanation = answer; // fallback
                        }

                        AiExplanation exp = new AiExplanation();
                        exp.setClauseId(clause.getId());
                        exp.setPlainEnglish(explanation);
                        float confidence =
                        	    helper.confidenceCalculator(
                        	        clause.getOriginalText(),
                        	        context
                        	    );
                        exp.setConfidenceScore(confidence);
                        exp.setDisclaimer(
                            "AI-generated explanation. Not legal advice."
                        );

                        return aiRepository.save(exp);
                });
    }
}
