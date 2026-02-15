package com.legaldocs.eas.document;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.legaldocs.eas.ai.DocumentSummaryPromptBuilder;
import com.legaldocs.eas.ai.LlmClient;
import com.legaldocs.eas.common.CurrentUser;
import com.legaldocs.eas.extraction.ChunkRetrievalService;
import com.legaldocs.eas.extraction.EmbeddingService;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class DocumentSummaryService {
	
	private final DocumentSummaryRepository summaryRepo;
    private final ChunkRetrievalService retrievalService;
    private final EmbeddingService embeddingService;
    private final LlmClient llmClient;
    private final DocumentRepository documentRepository;

	
	public DocumentSummaryService(
	        DocumentSummaryRepository summaryRepo,
	        ChunkRetrievalService retrievalService,
	        EmbeddingService embeddingService,
	        LlmClient llmClient,
	        DocumentRepository documentRepository
	    ) {
	        this.summaryRepo = summaryRepo;
	        this.retrievalService = retrievalService;
	        this.embeddingService = embeddingService;
	        this.llmClient = llmClient;
	        this.documentRepository = documentRepository;
	    }
	
	
	public DocumentSummary summarize(UUID documentId) {
		
		UUID userId = CurrentUser.id();

	    documentRepository.findByIdAndUserId(documentId, userId)
	        .orElseThrow(() ->
	            new ResponseStatusException(
	                HttpStatus.FORBIDDEN,
	                "You do not have access to this document"
	            )
	        );
		
		return summaryRepo.findByDocumentId(documentId)
	            .orElseGet(() -> {  

	                float[] queryEmbedding =
	                    embeddingService.embed(
	                        "Summarize the employment agreement with risks and obligations"
	                    );
	                
	                List<DocumentChunk> context =
	                        retrievalService.retrieve(documentId, queryEmbedding);

	                String prompt =
	                        DocumentSummaryPromptBuilder.build(context);
	                
	                String response = llmClient.generate(prompt);
	                
	                response = response.replace("```json", "")
	                        .replace("```", "")
	                        .trim();
	                
	                ObjectMapper mapper = new ObjectMapper();

	                String summaryText = "Unable to generate summary.";
	                String risksText = "No risks detected.";
	                String favorsText = "Neutral";
	                
	                try {
	                    JsonNode node = mapper.readTree(response);

	                    summaryText = node.get("summary").asString();

	                    JsonNode risksNode = node.get("risks");
	                    if (risksNode != null && risksNode.isArray()) {
	                        StringBuilder risksBuilder = new StringBuilder();
	                        risksNode.forEach(r ->
	                            risksBuilder.append("• ").append(r.asString()).append("\n")
	                        );
	                        risksText = risksBuilder.toString().trim();
	                    }

	                    favorsText = node.get("favors").asString();

	                } catch (Exception e) {
	                    summaryText = response;
	                }
	                
	                DocumentSummary summary = new DocumentSummary();
	                summary.setDocumentId(documentId);
	                summary.setSummary(summaryText);
	                summary.setRiskOverview(risksText);
	                summary.setFavors(favorsText);
	                summary.setDisclaimer(
	                    "AI-generated summary. Not legal advice."
	                );

	                return summaryRepo.save(summary);
	            });
	}
}
