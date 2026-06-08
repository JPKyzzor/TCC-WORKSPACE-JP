import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  login = '';
  senha = '';

  carregando = signal(false);
  erro = signal<string | null>(null);

  entrar(): void {
    this.erro.set(null);

    if (!this.login.trim() || !this.senha.trim()) {
      this.erro.set('Informe login e senha.');
      return;
    }

    this.carregando.set(true);

    this.authService.login(this.login.trim(), this.senha).subscribe({
      next: () => {
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/formulario';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err) => {
        this.carregando.set(false);
        this.erro.set(err?.error?.message ?? 'Não foi possível realizar login.');
      },
      complete: () => {
        this.carregando.set(false);
      },
    });
  }
}
