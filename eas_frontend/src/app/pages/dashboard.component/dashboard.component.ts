import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { DocumentListItem } from '../../shared/models/document.model';
import { DocumentService } from '../../core/services/document.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  documents = signal<DocumentListItem[]>([]);
  loading = signal(true);

  constructor(
    private documentService: DocumentService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.documentService.getMyDocuments().subscribe((docs) => {
      this.documents.set(docs);
      this.loading.set(false);
    });
  }

  openDocument(docId: string) {
    this.router.navigate(['/document', docId]);
  }
}
