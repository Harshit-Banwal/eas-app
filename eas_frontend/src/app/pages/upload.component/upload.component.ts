import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DocumentService } from '../../core/services/document.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-upload.component',
  imports: [CommonModule],
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css',
})
export class UploadComponent {
  selectedFile = signal<File | null>(null);
  uploading = signal(false);
  error = signal<string | null>(null);

  constructor(
    private documentService: DocumentService,
    private router: Router,
  ) {}

  //     Handle file select (click or drop)
  setFile(file: File | null) {
    if (file && file.type !== 'application/pdf') {
      this.error.set('Only PDF files are allowed');
      return;
    }
    this.error.set(null);
    this.selectedFile.set(file);
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    this.setFile(input.files?.[0] ?? null);
  }

  //     Drag & Drop
  onDrop(event: DragEvent) {
    event.preventDefault();
    this.setFile(event.dataTransfer?.files?.[0] ?? null);
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
  }

  removeFile() {
    this.selectedFile.set(null);
  }

  upload() {
    if (!this.selectedFile()) return;

    this.uploading.set(true);
    this.error.set(null);

    this.documentService.uploadDocument(this.selectedFile()!).subscribe({
      next: (documentId) => {
        //      Reset UI after success
        this.selectedFile.set(null);

        this.router.navigate(['/document', documentId]);
      },
      error: () => {
        this.error.set('Upload failed. Please try again.');
      },
      complete: () => {
        this.uploading.set(false);
      },
    });
  }
}
