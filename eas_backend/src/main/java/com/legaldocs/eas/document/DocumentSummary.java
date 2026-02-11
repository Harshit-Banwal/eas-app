package com.legaldocs.eas.document;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_summaries")
public class DocumentSummary {
	
	@Id
    @GeneratedValue
    private UUID id;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "risk_overview", nullable = false, columnDefinition = "TEXT")
    private String riskOverview;

    @Column(nullable = false)
    private String favors;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String disclaimer;

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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getRiskOverview() {
		return riskOverview;
	}

	public void setRiskOverview(String riskOverview) {
		this.riskOverview = riskOverview;
	}

	public String getFavors() {
		return favors;
	}

	public void setFavors(String favors) {
		this.favors = favors;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
    
    
}
