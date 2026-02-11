import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-confidence-meter',
  imports: [CommonModule],
  templateUrl: './confidence-meter.component.html',
  styleUrl: './confidence-meter.component.css',
})
export class ConfidenceMeterComponent {
  @Input({ required: true }) score!: number;

  get percentage(): number {
    return Math.round(this.score * 100);
  }

  get label(): string {
    if (this.percentage >= 80) return 'High confidence';
    if (this.percentage >= 60) return 'Moderate confidence';
    return 'Low confidence';
  }
}
