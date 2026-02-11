import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { AiExplanation } from '../../shared/models/ai-explanation.model';
import { Clause } from '../../shared/models/clause.model';
import { DocumentService } from '../../core/services/document.service';
import { ActivatedRoute } from '@angular/router';
import { DocumentSummary } from '../../shared/models/document-summary.model';
import { AiRiskExplanation } from '../../shared/models/risk-explanation.model';
import { ConfidenceMeterComponent } from '../../shared/components/confidence-meter.component/confidence-meter.component';

@Component({
  selector: 'app-analysis',
  imports: [CommonModule, ConfidenceMeterComponent],
  templateUrl: './analysis.component.html',
  styleUrl: './analysis.component.css',
})
export class AnalysisComponent {
  clauses = signal<Clause[]>([]);
  explanations = signal<Record<string, AiExplanation>>({});
  loading = signal(true);
  summary = signal<DocumentSummary | null>(null);
  summaryLoading = signal(true);
  riskExplanations = signal<Record<string, AiRiskExplanation>>({});
  riskLoading = signal<Record<string, boolean>>({});

  constructor(
    private route: ActivatedRoute,
    private documentService: DocumentService,
  ) {
    const documentId = this.route.snapshot.paramMap.get('id')!;
    this.loadSummary(documentId);
    this.loadClauses(documentId);
  }

  loadSummary(documentId: string) {
    this.documentService.getDocumentSummary(documentId).subscribe((summary) => {
      this.summary.set(summary);
      this.summaryLoading.set(false);
    });
  }

  loadClauses(documentId: string) {
    this.documentService.getClauses(documentId).subscribe((clauses) => {
      this.clauses.set(clauses);
      this.loading.set(false);

      clauses.forEach((c) => this.loadExplanation(c.id));
    });
  }

  loadExplanation(clauseId: string) {
    this.documentService.explainClause(clauseId).subscribe((exp) => {
      this.explanations.update((e) => ({ ...e, [clauseId]: exp }));
    });
  }

  loadRiskExplanation(clauseId: string) {
    // already loaded → toggle only
    if (this.riskExplanations()[clauseId]) {
      this.riskExplanations.update((r) => {
        const copy = { ...r };
        delete copy[clauseId];
        return copy;
      });
      return;
    }

    this.riskLoading.update((l) => ({ ...l, [clauseId]: true }));

    this.documentService.getRiskExplanation(clauseId).subscribe((exp) => {
      this.riskExplanations.update((r) => ({ ...r, [clauseId]: exp }));
      this.riskLoading.update((l) => ({ ...l, [clauseId]: false }));
    });
  }

  explanationFor(id: string) {
    return this.explanations()[id];
  }
}
