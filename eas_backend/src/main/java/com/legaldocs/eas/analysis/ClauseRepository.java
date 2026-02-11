package com.legaldocs.eas.analysis;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClauseRepository extends JpaRepository<Clause, UUID>{
	List<Clause> findByDocumentId(UUID documentId);
}
