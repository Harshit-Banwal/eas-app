package com.legaldocs.eas.common;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@MappedSuperclass
public class BaseEntity {
	@Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
