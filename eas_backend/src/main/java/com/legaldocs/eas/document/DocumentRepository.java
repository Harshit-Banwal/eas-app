package com.legaldocs.eas.document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
	Optional<Document> findByIdAndUserId(UUID id, UUID userId);

	List<Document> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
