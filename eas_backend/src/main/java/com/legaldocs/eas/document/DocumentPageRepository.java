package com.legaldocs.eas.document;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DocumentPageRepository extends JpaRepository<DocumentPage, UUID> {
}