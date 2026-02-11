export interface DocumentSummary {
  documentId: string;
  summary: string;
  riskOverview: string;
  favors: 'Employer' | 'Employee' | 'Neutral';
  disclaimer: string;
}
