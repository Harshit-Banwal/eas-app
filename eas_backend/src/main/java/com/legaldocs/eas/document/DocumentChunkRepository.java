package com.legaldocs.eas.document;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {
	
	@Query(value = """
		    SELECT
		        id,
		        document_id,
		        chunk_text,
		        chunk_type
		    FROM document_chunks
		    WHERE document_id = :documentId
		    ORDER BY embedding <-> CAST(:queryEmbedding AS vector)
		    LIMIT :limit
		    """, nativeQuery = true)
		List<DocumentChunk> findTopKRelevant(
		    @Param("documentId") UUID documentId,
		    @Param("queryEmbedding") String queryEmbedding,
		    @Param("limit") int limit
		);
	
	@Modifying
	@Query(value = """
	    UPDATE document_chunks
	    SET embedding = CAST(:embedding AS vector)
	    WHERE id = :chunkId
	""", nativeQuery = true)
	void updateEmbedding(
	    @Param("chunkId") UUID chunkId,
	    @Param("embedding") String embedding
	);
}
