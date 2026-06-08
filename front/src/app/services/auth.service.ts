import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

interface LoginRequest {
  login: string;
  senha: string;
}

interface LoginResponse {
  token: string;
  usuarioId: number;
  login: string;
}

const TOKEN_KEY = 'auth_token';
const USER_ID_KEY = 'auth_user_id';
const USER_LOGIN_KEY = 'auth_user_login';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly authUrl = `${environment.apiBaseUrl}/auth`;

  login(login: string, senha: string): Observable<LoginResponse> {
    const payload: LoginRequest = { login, senha };

    return this.http
      .post<LoginResponse>(`${this.authUrl}/login`, payload)
      .pipe(tap((response) => this.salvarSessao(response)));
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_ID_KEY);
    localStorage.removeItem(USER_LOGIN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getUsuarioId(): number | null {
    const value = localStorage.getItem(USER_ID_KEY);
    if (!value) {
      return null;
    }

    const parsed = Number(value);
    return Number.isNaN(parsed) ? null : parsed;
  }

  getUsuarioLogin(): string | null {
    return localStorage.getItem(USER_LOGIN_KEY);
  }

  isAdmin(): boolean {
    return this.getUsuarioId() === 1;
  }

  private salvarSessao(response: LoginResponse): void {
    localStorage.setItem(TOKEN_KEY, response.token);
    localStorage.setItem(USER_ID_KEY, String(response.usuarioId));
    localStorage.setItem(USER_LOGIN_KEY, response.login);
  }
}
