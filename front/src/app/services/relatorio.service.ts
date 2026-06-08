import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface IUsuarioOpcao {
  id: number;
  login: string;
}

export interface IRelatorioResumo {
  resumoNotas: IRelatorioResumoTipo;
  resumoGeral: IRelatorioResumoTipo;
  resumoPrincipioAtivo: IRelatorioResumoTipo;
  resumoExcipiente: IRelatorioResumoTipo;
  resumoEmbalagem: IRelatorioResumoTipo;
}

export interface IRelatorioResumoTipo {
  media: number | null;
  quantidade_nota_1: number;
  quantidade_nota_2: number;
  quantidade_nota_3: number;
  quantidade_nota_4: number;
  quantidade_nota_5: number;
  quantidade_sem_resposta: number;
  quantidade_total: number;
}

export interface IRelatorioFeedbackItem {
  feedbackId: number;
  nota: number;
  observacoes?: string;
  falsoPositivo?: boolean;
  usuarioId: number;
  usuarioLogin: string;
}

export interface IRelatorioRecomendacaoItem {
  recomendacaoItemId: number;
  nivel: string;
  mensagem?: string;
  feedback: IRelatorioFeedbackItem | null;
}

export interface IRelatorioLinhaPrincipioAtivo {
  itemId: number;
  ordem: number;
  nome: string;
  quantidade: number;
  unidade: string;
  recomendacao: IRelatorioRecomendacaoItem | null;
}

export interface IRelatorioLinhaExcipiente {
  itemId: number;
  ordem: number;
  nome: string;
  quantidade: number;
  unidade: string;
  recomendacao: IRelatorioRecomendacaoItem | null;
}

export interface IRelatorioLinhaEmbalagem {
  itemId: number;
  ordem: number;
  nome: string;
  recomendacao: IRelatorioRecomendacaoItem | null;
}

export interface IRelatorioRecomendacaoGeral {
  recomendacaoId: number;
  nivel: string;
  mensagem?: string;
  feedback: IRelatorioFeedbackItem | null;
}

export interface IRelatorioOrdem {
  ordemManipulacaoId: number;
  criadoEm: string;
  idade: number;
  peso: number;
  sexo: string;
  tipoFrequenciaDose?: string;
  frequenciaDose?: number;
  usuarioId: number;
  usuarioLogin: string;
  recomendacaoGeral: IRelatorioRecomendacaoGeral | null;
  principiosAtivos: IRelatorioLinhaPrincipioAtivo[];
  excipientes: IRelatorioLinhaExcipiente[];
  embalagens: IRelatorioLinhaEmbalagem[];
}

export interface IRelatorioResponse {
  ordens: IRelatorioOrdem[];
  resumo: IRelatorioResumo;
}

export interface IRelatorioFiltros {
  usuariosIds?: number[];
  tester?: boolean;
  feedbackPreenchimento?: Array<'total' | 'parcial' | 'nenhum'>;
  feedbackNotaMaior?: number | null;
  feedbackNotaMenor?: number | null;
}

@Injectable({ providedIn: 'root' })
export class RelatorioService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  listarUsuarios(): Observable<IUsuarioOpcao[]> {
    return this.http.get<IUsuarioOpcao[]>(`${this.baseUrl}/usuarios`);
  }

  buscarRelatorio(filtros: IRelatorioFiltros): Observable<IRelatorioResponse> {
    let params = new HttpParams();

    if (filtros.usuariosIds?.length) {
      for (const id of filtros.usuariosIds) {
        params = params.append('usuarios_ids', String(id));
      }
    }

    if (typeof filtros.tester === 'boolean') {
      params = params.set('tester', String(filtros.tester));
    }

    if (filtros.feedbackPreenchimento?.length) {
      for (const preenchimento of filtros.feedbackPreenchimento) {
        params = params.append('feedback_preenchimento', preenchimento);
      }
    }

    if (filtros.feedbackNotaMaior != null) {
      params = params.set('feedback_nota_maior', String(filtros.feedbackNotaMaior));
    }

    if (filtros.feedbackNotaMenor != null) {
      params = params.set('feedback_nota_menor', String(filtros.feedbackNotaMenor));
    }

    return this.http.get<IRelatorioResponse>(`${this.baseUrl}/relatorio`, { params });
  }
}
