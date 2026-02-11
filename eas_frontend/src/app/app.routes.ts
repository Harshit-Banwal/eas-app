import { Routes } from '@angular/router';
import { LandingComponent } from './pages/landing.component/landing.component';
import { UploadComponent } from './pages/upload.component/upload.component';
import { AnalysisComponent } from './pages/analysis.component/analysis.component';
import { authGuard } from './core/guards/auth-guard';
import { LoginComponent } from './pages/login.component/login.component';
import { SignupComponent } from './pages/signup.component/signup.component';
import { DashboardComponent } from './pages/dashboard.component/dashboard.component';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'upload', component: UploadComponent, canActivate: [authGuard] },
  { path: 'document/:id', component: AnalysisComponent, canActivate: [authGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
];
