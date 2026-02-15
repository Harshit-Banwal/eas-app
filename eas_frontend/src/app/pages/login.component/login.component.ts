import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  email = '';
  password = '';
  error = '';
  loading = signal(false);

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  login() {
    this.loading.set(true);

    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: (res) => {
        this.auth.saveToken(res.token);
        this.loading.set(false);
        this.router.navigate(['']);
      },
      error: () => {
        this.error = 'Invalid credentials';
        this.loading.set(false);
      },
    });
  }
}
