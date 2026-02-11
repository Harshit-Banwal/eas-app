import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Clause } from '../../shared/models/clause.model';
import { AiExplanation } from '../../shared/models/ai-explanation.model';
import { DocumentSummary } from '../../shared/models/document-summary.model';
import { AiRiskExplanation } from '../../shared/models/risk-explanation.model';
import { DocumentListItem } from '../../shared/models/document.model';

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  uploadDocument(file: File): Observable<{ documentId: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ documentId: string }>(`${this.baseUrl}/documents/upload`, formData);
  }

  getClauses(documentId: string) {
    return this.http.get<Clause[]>(`${this.baseUrl}/documents/${documentId}/clauses`);
  }

  explainClause(clauseId: string) {
    return this.http.post<AiExplanation>(`${this.baseUrl}/ai/explain-clause/${clauseId}`, {});
  }

  getDocumentSummary(documentId: string) {
    return this.http.get<DocumentSummary>(`${this.baseUrl}/documents/${documentId}/summary`);
  }

  getRiskExplanation(clauseId: string) {
    return this.http.post<AiRiskExplanation>(`${this.baseUrl}/ai/explain-risk/${clauseId}`, {});
  }

  getMyDocuments() {
    return this.http.get<DocumentListItem[]>(`${this.baseUrl}/documents/my`);
  }
}
