package com.legaldocs.eas.ai;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ai_explanations")
public class AiExplanation {
	
	@Id
    @GeneratedValue
    private UUID id;

    @Column(name = "clause_id", nullable = false)
    private UUID clauseId;

    @Column(name = "plain_english", nullable = false, columnDefinition = "TEXT")
    private String plainEnglish;

    @Column(name = "confidence_score", nullable = false)
    private float confidenceScore;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String disclaimer;

    public UUID getId() {
        return id;
    }

    public UUID getClauseId() {
        return clauseId;
    }

    public void setClauseId(UUID clauseId) {
        this.clauseId = clauseId;
    }

    public String getPlainEnglish() {
        return plainEnglish;
    }

    public void setPlainEnglish(String plainEnglish) {
        this.plainEnglish = plainEnglish;
    }

    public float getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(float confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
