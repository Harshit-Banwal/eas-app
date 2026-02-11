package com.legaldocs.eas.document;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentSummaryRepository extends JpaRepository<DocumentSummary, UUID>{
	
	Optional<DocumentSummary> findByDocumentId(UUID documentId);
}
