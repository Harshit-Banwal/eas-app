package com.legaldocs.eas.analysis;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.legaldocs.eas.common.CurrentUser;
import com.legaldocs.eas.document.DocumentRepository;

@RestController
@RequestMapping("/api/documents")
public class ClauseController {
	
	private final ClauseRepository clauseRepository;
	private final DocumentRepository documentRepository;

	public ClauseController(
		    ClauseRepository clauseRepository,
		    DocumentRepository documentRepository
		) {
		    this.clauseRepository = clauseRepository;
		    this.documentRepository = documentRepository;
		}

    
    @GetMapping("/{documentId}/clauses")
    public List<Clause> getClauses(@PathVariable UUID documentId) {
    	
    	UUID userId = CurrentUser.id();

        documentRepository.findByIdAndUserId(documentId, userId)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN)
            );
        
        return clauseRepository.findByDocumentId(documentId);
    }
}
