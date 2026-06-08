import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IMainPayload } from '../interfaces/main-payload.interface';
import { environment } from '../../environments/environment';

export interface IOrdemManipulacaoResponse {
  ordemManipulacaoId: number;
  recomendacaoGeral: IRecomendacaoComId;
  recomendacoesPrincipiosAtivos: IRecomendacaoComId[];
  recomendacoesExcipientes: IRecomendacaoComId[];
  recomendacoesEmbalagens: IRecomendacaoComId[];
}

export interface IRecomendacaoComId {
  id: number;
  nivel: 'grave' | 'medio' | 'leve' | 'ok';
  mensagem?: string;
}

export interface IFeedbackPayload {
  nota: number;
  observacoes?: string;
  falsoPositivo?: boolean;
}

@Injectable({ providedIn: 'root' })
export class OrdemManipulacaoService {
  private readonly http = inject(HttpClient);

  private readonly ordemManipulacaoUrl = `${environment.apiBaseUrl}/ordem-manipulacao`;
  private readonly feedbackUrl = `${environment.apiBaseUrl}/feedback`;

  analisar(payload: IMainPayload): Observable<IOrdemManipulacaoResponse> {
    return this.http.post<IOrdemManipulacaoResponse>(this.ordemManipulacaoUrl, payload);
  }

  salvarFeedbackRecomendacao(recomendacaoId: number, payload: IFeedbackPayload): Observable<void> {
    return this.http.put<void>(`${this.feedbackUrl}/recomendacao/${recomendacaoId}`, payload);
  }

  salvarFeedbackPrincipioAtivo(
    recomendacaoPrincipioAtivoId: number,
    payload: IFeedbackPayload,
  ): Observable<void> {
    return this.http.put<void>(
      `${this.feedbackUrl}/principio-ativo/${recomendacaoPrincipioAtivoId}`,
      payload,
    );
  }

  salvarFeedbackExcipiente(
    recomendacaoExcipienteId: number,
    payload: IFeedbackPayload,
  ): Observable<void> {
    return this.http.put<void>(
      `${this.feedbackUrl}/excipiente/${recomendacaoExcipienteId}`,
      payload,
    );
  }

  salvarFeedbackEmbalagem(
    recomendacaoEmbalagemId: number,
    payload: IFeedbackPayload,
  ): Observable<void> {
    return this.http.put<void>(`${this.feedbackUrl}/embalagem/${recomendacaoEmbalagemId}`, payload);
  }
}
