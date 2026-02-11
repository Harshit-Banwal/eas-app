package com.legaldocs.eas.extraction;

import java.util.*;

import org.springframework.stereotype.Service;

import com.legaldocs.eas.common.Helper;
import com.legaldocs.eas.document.DocumentChunk;
import com.legaldocs.eas.document.DocumentChunkRepository;

@Service
public class ChunkRetrievalService {
	
	private final DocumentChunkRepository repo;
	private final Helper helper;

    public ChunkRetrievalService(
    		DocumentChunkRepository repo,
    		Helper helper
  ) {
        this.repo = repo;
        this.helper = helper;
    }
    
    public List<DocumentChunk> retrieve(
            UUID documentId,
            float[] queryEmbedding
    ) {
    	String vectorLiteral = helper.toVector(queryEmbedding);
        return repo.findTopKRelevant(documentId, vectorLiteral, 3);
    }
}
