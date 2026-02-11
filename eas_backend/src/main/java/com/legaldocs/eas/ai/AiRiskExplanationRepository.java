package com.legaldocs.eas.ai;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AiRiskExplanationRepository extends JpaRepository<AiRiskExplanation, UUID> {
	
	Optional<AiRiskExplanation> findByClauseId(UUID clauseId);
}
