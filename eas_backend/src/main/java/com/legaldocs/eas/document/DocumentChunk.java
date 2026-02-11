package com.legaldocs.eas.document;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk {
	
	@Id
    @GeneratedValue
    private UUID id;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "chunk_text", nullable = false, columnDefinition = "TEXT")
    private String chunkText;

    @Enumerated(EnumType.STRING)
    @Column(name = "chunk_type", nullable = false)
    private DocumentChunkType documentChunkType;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getDocumentId() {
		return documentId;
	}

	public void setDocumentId(UUID documentId) {
		this.documentId = documentId;
	}

	public String getChunkText() {
		return chunkText;
	}

	public void setChunkText(String chunkText) {
		this.chunkText = chunkText;
	}

	public DocumentChunkType getChunkType() {
		return documentChunkType;
	}

	public void setChunkType(DocumentChunkType documentChunkType) {
		this.documentChunkType = documentChunkType;
	}    
    
}
