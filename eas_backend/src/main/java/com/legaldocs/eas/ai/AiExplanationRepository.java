package com.legaldocs.eas.ai;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AiExplanationRepository extends JpaRepository<AiExplanation, UUID> {
	Optional<AiExplanation> findByClauseId(UUID clauseId);
}
