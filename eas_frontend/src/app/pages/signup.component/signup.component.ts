import { Component, signal } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { single } from 'rxjs';

@Component({
  selector: 'app-signup.component',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  email = '';
  password = '';
  error = '';
  loading = signal(false);

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  signup() {
    this.loading.set(true);
    this.auth.signup({ email: this.email, password: this.password }).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/login']);
      },
      error: () => {
        this.loading.set(false);
        this.error = 'Signup failed';
      },
    });
  }
}
