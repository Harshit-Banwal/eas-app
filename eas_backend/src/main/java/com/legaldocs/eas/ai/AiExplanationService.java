package com.legaldocs.eas.ai;

import java.util.List;

import org.springframework.stereotype.Service;

import com.legaldocs.eas.analysis.Clause;
import com.legaldocs.eas.common.Helper;
import com.legaldocs.eas.document.DocumentChunk;
import com.legaldocs.eas.extraction.ChunkRetrievalService;
import com.legaldocs.eas.extraction.EmbeddingService;

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

                        AiExplanation exp = new AiExplanation();
                        exp.setClauseId(clause.getId());
                        exp.setPlainEnglish(answer);
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
