import { Routes } from '@angular/router';
import { FormularioComponent } from './pages/formulario/formulario';
import { LoginComponent } from './pages/login/login';
import { RelatorioComponent } from './pages/relatorio/relatorio';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { loginGuard } from './guards/login.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'formulario', pathMatch: 'full' },
  { path: 'login', component: LoginComponent, canActivate: [loginGuard] },
  { path: 'formulario', component: FormularioComponent, canActivate: [authGuard] },
  { path: 'relatorio', component: RelatorioComponent, canActivate: [authGuard, adminGuard] },
  { path: '**', redirectTo: 'formulario' },
];
