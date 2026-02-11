export interface Clause {
  id: string;
  documentId: string;
  clauseType: string;
  originalText: string;
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH';
}
