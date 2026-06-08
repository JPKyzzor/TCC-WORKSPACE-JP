import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgClass, UpperCasePipe } from '@angular/common';
import { Router } from '@angular/router';
import { IMainPayload } from '../../interfaces/main-payload.interface';
import { ORDENS_PRONTAS } from '../../ordens-prontas';
import { AuthService } from '../../services/auth.service';
import {
  OrdemManipulacaoService,
  IOrdemManipulacaoResponse,
  IFeedbackPayload,
  IRecomendacaoComId,
} from '../../services/ordem-manipulacao.service';

type TipoFeedback = 'geral' | 'principioAtivo' | 'excipiente' | 'embalagem';

interface FeedbackFormModel {
  nota: number;
  observacoes: string;
  falsoPositivo: boolean;
}

interface FeedbackState {
  salvando: boolean;
  sucesso: string | null;
  erro: string | null;
  enviado: boolean;
}

@Component({
  selector: 'app-formulario',
  imports: [FormsModule, UpperCasePipe, NgClass],
  templateUrl: './formulario.html',
  styleUrl: './formulario.scss',
})
export class FormularioComponent {
  private readonly ordemManipulacaoService = inject(OrdemManipulacaoService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly feedbackSuccessTimeouts: Record<string, ReturnType<typeof setTimeout>> = {};

  carregando = signal(false);
  erro = signal<string | null>(null);
  resposta = signal<IOrdemManipulacaoResponse | null>(null);

  feedbackForms: Record<string, FeedbackFormModel> = {};
  feedbackStates: Record<string, FeedbackState> = {};

  payload: IMainPayload = this.criarPayloadVazio();

  tiposMedidaPrincipioAtivo: Array<'mg' | 'ml' | 'mcg' | 'ui' | '%'> = [
    'mg',
    'ml',
    'mcg',
    'ui',
    '%',
  ];
  tiposMedidaExcipiente: Array<'mg' | 'ml' | 'mcg' | 'ui' | '%' | 'qsp'> = [
    'mg',
    'ml',
    'mcg',
    'ui',
    '%',
    'qsp',
  ];
  notasEstrela: number[] = [1, 2, 3, 4, 5];

  deslogar(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  usarValoresPadrao(): void {
    this.limparResposta();
    const aleatorio = ORDENS_PRONTAS[Math.floor(Math.random() * ORDENS_PRONTAS.length)];
    this.payload = JSON.parse(JSON.stringify(aleatorio));
  }

  getNivelClass(nivel: 'grave' | 'medio' | 'leve' | 'ok'): string {
    switch (nivel) {
      case 'grave':
        return 'nivel-grave';
      case 'medio':
        return 'nivel-medio';
      case 'leve':
        return 'nivel-leve';
      case 'ok':
        return 'nivel-ok';
      default:
        return '';
    }
  }

  limparResposta(): void {
    this.resposta.set(null);
    this.feedbackForms = {};
    this.feedbackStates = {};
  }

  proximaOrdemManipulacao(): void {
    if (!this.confirmarDescarteComFeedbackPendente('ir para a próxima análise')) {
      return;
    }

    this.payload = this.criarPayloadVazio();
    this.erro.set(null);
    this.limparResposta();
  }

  editarValores(): void {
    if (!this.confirmarDescarteComFeedbackPendente('editar os valores')) {
      return;
    }

    this.erro.set(null);
    this.limparResposta();
  }

  adicionarEmbalagem(): void {
    this.payload.embalagens.push('');
  }

  removerEmbalagem(index: number): void {
    if (this.payload.embalagens.length > 1) {
      this.payload.embalagens.splice(index, 1);
    }
  }

  adicionarPrincipioAtivo(): void {
    this.payload.principiosAtivos.push({ nome: '', tipoMedida: 'mg', quantidade: 0 });
  }

  removerPrincipioAtivo(index: number): void {
    if (this.payload.principiosAtivos.length > 1) {
      this.payload.principiosAtivos.splice(index, 1);
    }
  }

  adicionarExcipiente(): void {
    this.payload.excipientes.push({ nome: '', tipoMedida: 'mg', quantidade: 0 });
  }

  onTipoMedidaExcipienteChange(index: number): void {
    const excipiente = this.payload.excipientes[index];
    if (!excipiente) {
      return;
    }
    if (excipiente.tipoMedida === 'qsp') {
      excipiente.quantidade = 0;
    }
  }

  isExcipienteQsp(index: number): boolean {
    return this.payload.excipientes[index]?.tipoMedida === 'qsp';
  }

  removerExcipiente(index: number): void {
    if (this.payload.excipientes.length > 1) {
      this.payload.excipientes.splice(index, 1);
    }
  }

  trackByIndex(index: number): number {
    return index;
  }

  trackByRecomendacaoId(_index: number, item: IRecomendacaoComId): number {
    return item.id;
  }

  enviar(): void {
    this.erro.set(null);
    this.carregando.set(true);
    this.resposta.set(null);

    this.ordemManipulacaoService.analisar(this.payload).subscribe({
      next: (resposta) => {
        this.carregando.set(false);
        this.resposta.set(resposta);
      },
      error: (err) => {
        this.carregando.set(false);
        this.erro.set(
          err?.error?.message ??
            'Erro ao conectar com o servidor. Verifique se a API esta rodando.',
        );
      },
    });
  }

  salvarFeedback(tipo: TipoFeedback, recomendacaoId: number): void {
    const chave = this.montarChaveFeedback(tipo, recomendacaoId);
    const form = this.getFeedbackForm(chave);
    const state = this.getFeedbackState(chave);

    const payload: IFeedbackPayload = {
      nota: form.nota,
      observacoes: form.observacoes?.trim() || undefined,
      falsoPositivo: form.falsoPositivo,
    };

    state.salvando = true;
    state.erro = null;
    state.sucesso = null;

    this.feedbackRequest(tipo, recomendacaoId, payload).subscribe({
      next: () => {
        state.salvando = false;
        state.sucesso = 'Feedback salvo com sucesso.';
        state.enviado = true;
        this.scheduleSuccessClear(chave);
      },
      error: (err) => {
        state.salvando = false;
        state.erro = err?.error?.message ?? 'Nao foi possivel salvar este feedback.';
      },
    });
  }

  getFeedbackFormByItem(tipo: TipoFeedback, recomendacaoId: number): FeedbackFormModel {
    return this.getFeedbackForm(this.montarChaveFeedback(tipo, recomendacaoId));
  }

  getFeedbackStateByItem(tipo: TipoFeedback, recomendacaoId: number): FeedbackState {
    return this.getFeedbackState(this.montarChaveFeedback(tipo, recomendacaoId));
  }

  private feedbackRequest(tipo: TipoFeedback, recomendacaoId: number, payload: IFeedbackPayload) {
    switch (tipo) {
      case 'geral':
        return this.ordemManipulacaoService.salvarFeedbackRecomendacao(recomendacaoId, payload);
      case 'principioAtivo':
        return this.ordemManipulacaoService.salvarFeedbackPrincipioAtivo(recomendacaoId, payload);
      case 'excipiente':
        return this.ordemManipulacaoService.salvarFeedbackExcipiente(recomendacaoId, payload);
      case 'embalagem':
        return this.ordemManipulacaoService.salvarFeedbackEmbalagem(recomendacaoId, payload);
    }
  }

  private montarChaveFeedback(tipo: TipoFeedback, recomendacaoId: number): string {
    return `${tipo}-${recomendacaoId}`;
  }

  private getFeedbackForm(chave: string): FeedbackFormModel {
    if (!this.feedbackForms[chave]) {
      this.feedbackForms[chave] = {
        nota: 3,
        observacoes: '',
        falsoPositivo: false,
      };
    }
    return this.feedbackForms[chave];
  }

  private getFeedbackState(chave: string): FeedbackState {
    if (!this.feedbackStates[chave]) {
      this.feedbackStates[chave] = {
        salvando: false,
        sucesso: null,
        erro: null,
        enviado: false,
      };
    }
    return this.feedbackStates[chave];
  }

  private existeFeedbackPendente(): boolean {
    const respostaAtual = this.resposta();
    if (!respostaAtual) {
      return false;
    }

    const chavesEsperadas: string[] = [
      this.montarChaveFeedback('geral', respostaAtual.recomendacaoGeral.id),
      ...respostaAtual.recomendacoesPrincipiosAtivos.map((item) =>
        this.montarChaveFeedback('principioAtivo', item.id),
      ),
      ...respostaAtual.recomendacoesExcipientes.map((item) =>
        this.montarChaveFeedback('excipiente', item.id),
      ),
      ...respostaAtual.recomendacoesEmbalagens.map((item) =>
        this.montarChaveFeedback('embalagem', item.id),
      ),
    ];

    return chavesEsperadas.some((chave) => !this.feedbackStates[chave]?.enviado);
  }

  private confirmarDescarteComFeedbackPendente(acao: string): boolean {
    if (!this.existeFeedbackPendente()) {
      return true;
    }

    return window.confirm(`Você não preencheu todos os feedbacks. Tem certeza que deseja ${acao}?`);
  }

  private criarPayloadVazio(): IMainPayload {
    return {
      idade: 0,
      sexo: 'M',
      peso: 0,
      tipo_frequencia_dose: 'dia',
      frequencia_dose: 1,
      principiosAtivos: [{ nome: '', tipoMedida: 'mg', quantidade: 0 }],
      excipientes: [{ nome: '', tipoMedida: 'mg', quantidade: 0 }],
      embalagens: [''],
    };
  }

  private scheduleSuccessClear(chave: string): void {
    const timeoutAnterior = this.feedbackSuccessTimeouts[chave];
    if (timeoutAnterior) {
      clearTimeout(timeoutAnterior);
    }

    this.feedbackSuccessTimeouts[chave] = setTimeout(() => {
      const state = this.feedbackStates[chave];
      if (state) {
        state.sucesso = null;
      }
      delete this.feedbackSuccessTimeouts[chave];
    }, 2200);
  }
}
