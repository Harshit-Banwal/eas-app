package com.legaldocs.eas.document;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
	
	private final DocumentService documentService;
	private final DocumentSummaryService service;

    public DocumentController(
    		DocumentService documentService,
    		DocumentSummaryService service) {
        this.documentService = documentService;
        this.service = service;
    }

    @PostMapping("/upload")
    public UUID uploadDocument(@RequestParam("file") MultipartFile file) {
        UUID documentId = documentService.uploadDocument(file); 
        return documentId;
    }
    
    @GetMapping("/{documentId}/summary")
    public DocumentSummary getSummary(
            @PathVariable UUID documentId
    ) {
        return service.summarize(documentId);
    }
    
    @GetMapping("/my")
    public List<Document> myDocuments() {
        return documentService.getMyDocuments();
    }
    
}
