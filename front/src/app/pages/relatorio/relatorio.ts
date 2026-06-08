import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, DecimalPipe, UpperCasePipe } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ResumoTipoCardComponent } from './resumo-tipo-card/resumo-tipo-card';
import {
  IRelatorioOrdem,
  IRelatorioResponse,
  IUsuarioOpcao,
  RelatorioService,
} from '../../services/relatorio.service';

type PreenchimentoOpcao = 'total' | 'parcial' | 'nenhum';

@Component({
  selector: 'app-relatorio',
  imports: [FormsModule, DatePipe, DecimalPipe, UpperCasePipe, ResumoTipoCardComponent],
  templateUrl: './relatorio.html',
  styleUrl: './relatorio.scss',
})
export class RelatorioComponent {
  private readonly relatorioService = inject(RelatorioService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  carregando = signal(false);
  erro = signal<string | null>(null);
  usuarios = signal<IUsuarioOpcao[]>([]);
  relatorio = signal<IRelatorioResponse | null>(null);

  filtroUsuariosIds: number[] = [];
  filtroTester = false;
  usarFiltroTester = false;
  filtroFeedbackPreenchimento: PreenchimentoOpcao[] = [];
  filtroNotaMaior: number | null = null;
  filtroNotaMenor: number | null = null;

  readonly opcoesPreenchimento: Array<{ value: PreenchimentoOpcao; label: string }> = [
    { value: 'total', label: 'Total' },
    { value: 'parcial', label: 'Parcial' },
    { value: 'nenhum', label: 'Nenhum' },
  ];

  readonly totalOrdens = computed(() => this.relatorio()?.ordens.length ?? 0);

  ngOnInit(): void {
    this.carregarDadosIniciais();
  }

  deslogar(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  irParaFormulario(): void {
    this.router.navigate(['/formulario']);
  }

  aplicarFiltros(): void {
    this.erro.set(null);

    if (!this.notasSaoValidas()) {
      this.erro.set('Os filtros de nota devem estar entre 1 e 5.');
      return;
    }

    this.carregando.set(true);

    this.relatorioService
      .buscarRelatorio({
        usuariosIds: this.filtroUsuariosIds,
        tester: this.usarFiltroTester ? this.filtroTester : undefined,
        feedbackPreenchimento: this.filtroFeedbackPreenchimento,
        feedbackNotaMaior: this.filtroNotaMaior,
        feedbackNotaMenor: this.filtroNotaMenor,
      })
      .subscribe({
        next: (resposta) => {
          this.relatorio.set(resposta);
          this.carregando.set(false);
        },
        error: (err) => {
          this.erro.set(err?.error?.message ?? 'Nao foi possivel carregar o relatorio.');
          this.carregando.set(false);
        },
      });
  }

  limparFiltros(): void {
    this.filtroUsuariosIds = [];
    this.filtroTester = false;
    this.usarFiltroTester = false;
    this.filtroFeedbackPreenchimento = [];
    this.filtroNotaMaior = null;
    this.filtroNotaMenor = null;
    this.erro.set(null);
    this.relatorio.set(null);
  }

  getNotaTexto(nota: number | null | undefined): string {
    if (nota == null) {
      return '-';
    }
    return nota.toFixed(2);
  }

  getFalsoPositivoTexto(valor: boolean | null | undefined): string {
    if (valor == null) {
      return '-';
    }
    return valor ? 'Sim' : 'Não';
  }

  trackByUsuarioId(_index: number, item: IUsuarioOpcao): number {
    return item.id;
  }

  trackByOrdemId(_index: number, ordem: IRelatorioOrdem): number {
    return ordem.ordemManipulacaoId;
  }

  isUsuarioSelecionado(usuarioId: number): boolean {
    return this.filtroUsuariosIds.includes(usuarioId);
  }

  alternarUsuario(usuarioId: number, event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    if (checked) {
      if (!this.filtroUsuariosIds.includes(usuarioId)) {
        this.filtroUsuariosIds = [...this.filtroUsuariosIds, usuarioId];
      }
      return;
    }

    this.filtroUsuariosIds = this.filtroUsuariosIds.filter((id) => id !== usuarioId);
  }

  isPreenchimentoSelecionado(opcao: PreenchimentoOpcao): boolean {
    return this.filtroFeedbackPreenchimento.includes(opcao);
  }

  alternarPreenchimento(opcao: PreenchimentoOpcao, event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    if (checked) {
      if (!this.filtroFeedbackPreenchimento.includes(opcao)) {
        this.filtroFeedbackPreenchimento = [...this.filtroFeedbackPreenchimento, opcao];
      }
      return;
    }

    this.filtroFeedbackPreenchimento = this.filtroFeedbackPreenchimento.filter(
      (item) => item !== opcao,
    );
  }

  private carregarDadosIniciais(): void {
    this.carregando.set(true);
    this.erro.set(null);

    this.relatorioService.listarUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios.set(usuarios);
        this.carregando.set(false);
      },
      error: (err) => {
        this.erro.set(err?.error?.message ?? 'Nao foi possivel carregar a lista de usuarios.');
        this.carregando.set(false);
      },
    });
  }

  private notasSaoValidas(): boolean {
    return this.notaValida(this.filtroNotaMaior) && this.notaValida(this.filtroNotaMenor);
  }

  private notaValida(nota: number | null): boolean {
    if (nota == null) {
      return true;
    }
    return nota >= 1 && nota <= 5;
  }
}
