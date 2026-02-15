package com.legaldocs.eas.document;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.legaldocs.eas.extraction.EmbeddingService;
import com.legaldocs.eas.extraction.LegalChunker;
import com.legaldocs.eas.extraction.PdfTextExtractor;

import jakarta.transaction.Transactional;

import com.legaldocs.eas.analysis.ClauseDetector;
import com.legaldocs.eas.analysis.RiskEvaluator;
import com.legaldocs.eas.common.CurrentUser;
import com.legaldocs.eas.common.Helper;
import com.legaldocs.eas.analysis.Clause;
import com.legaldocs.eas.analysis.ClauseRepository;
import com.legaldocs.eas.analysis.ClauseType;


@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentPageRepository pageRepository;
    private final PdfTextExtractor pdfTextExtractor;
    private final ClauseDetector clauseDetector;
    private final RiskEvaluator riskEvaluator;
    private final ClauseRepository clauseRepository;
    private final DocumentChunkRepository chunkRepository;
    private final LegalChunker legalChunker;
    private final Helper helper;
    private final EmbeddingService embeddingService;

    public DocumentService(
    			DocumentRepository documentRepository,
	            DocumentPageRepository pageRepository,
	            ClauseRepository clauseRepository,
	            ClauseDetector clauseDetector,
	            RiskEvaluator riskEvaluator,
	            DocumentChunkRepository chunkRepository,
	            LegalChunker legalChunker,
	            PdfTextExtractor pdfTextExtractor,
	            Helper helper,
	            EmbeddingService embeddingService
  ) {
		this.documentRepository = documentRepository;
		this.pageRepository = pageRepository;
		this.clauseRepository = clauseRepository;
		this.clauseDetector = clauseDetector;
		this.riskEvaluator = riskEvaluator;
		this.chunkRepository = chunkRepository;
		this.legalChunker = legalChunker;
		this.pdfTextExtractor = pdfTextExtractor;
		this.helper = helper;
		this.embeddingService = embeddingService;
	}

    @Transactional
    public UUID uploadDocument(MultipartFile file) {
    	
    	UUID userId = CurrentUser.id();

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Document document = new Document();
        document.setUserId(userId);
        document.setFileName(file.getOriginalFilename());
        document.setStatus(DocumentStatus.UPLOADED);

        Document saved = documentRepository.save(document);

        try {
            Map<Integer, String> pages =
                    pdfTextExtractor.extractTextByPage(file.getInputStream());

            pages.forEach((pageNo, text) -> {
                DocumentPage page = new DocumentPage();
                page.setDocumentId(saved.getId());
                page.setPageNumber(pageNo);
                page.setText(text);
                pageRepository.save(page);
            });

            saved.setStatus(DocumentStatus.EXTRACTED);
            documentRepository.save(saved);
            
            pageRepository.findAll().stream()
            .filter(p -> p.getDocumentId().equals(saved.getId()))
            .forEach(page -> {

            	List<String> chunks = legalChunker.chunk(page.getText());

                for (String chunkText : chunks) {
                	
                	DocumentChunk chunk = new DocumentChunk();
                    chunk.setDocumentId(saved.getId());
                    chunk.setChunkText(chunkText);
                    chunk.setChunkType(
                    	helper.isHeading(chunkText) ? DocumentChunkType.HEADING : DocumentChunkType.CLAUSE
                    );
                   
                    DocumentChunk savedChunk = chunkRepository.save(chunk);
                    
                    float[] embedding = embeddingService.embed(chunkText);
                    chunkRepository.updateEmbedding(
                    	    savedChunk.getId(),
                    	    helper.toVector(embedding)
                    	);
                    
                    if (savedChunk.getChunkType() == DocumentChunkType.CLAUSE) {
                    
                    	ClauseType type = clauseDetector.detectClauseType(chunkText);

	                    if (type != null && type != ClauseType.OTHER) {
	                        Clause clause = new Clause();
	                        clause.setDocumentId(saved.getId());
	                        clause.setClauseType(type);
	                        clause.setOriginalText(chunkText);
	                        clause.setRiskLevel(
	                            riskEvaluator.evaluateRisk(type, chunkText)
	                        );
	                        clauseRepository.save(clause);
	                    }
                    }
                }
            });

            saved.setStatus(DocumentStatus.ANALYZED);
            documentRepository.save(saved);

        } catch (Exception e) {
            throw new RuntimeException("Text extraction failed", e);
        }

        return saved.getId();
    }
    
    
    public List<Document> getMyDocuments() {
        UUID userId = CurrentUser.id();
        return documentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

}
