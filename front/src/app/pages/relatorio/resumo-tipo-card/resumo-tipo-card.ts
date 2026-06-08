import { Component, Input } from '@angular/core';
import { IRelatorioResumoTipo } from '../../../services/relatorio.service';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartOptions, Plugin } from 'chart.js';

type BarraResumo = {
  chave: 'quantidade_sem_resposta' | 'quantidade_nota_1' | 'quantidade_nota_2' | 'quantidade_nota_3' | 'quantidade_nota_4' | 'quantidade_nota_5';
  label: string;
};

@Component({
  selector: 'app-resumo-tipo-card',
  imports: [BaseChartDirective],
  templateUrl: './resumo-tipo-card.html',
  styleUrl: './resumo-tipo-card.scss',
})
export class ResumoTipoCardComponent {
  @Input({ required: true }) label = '';
  @Input({ required: true }) resumo!: IRelatorioResumoTipo;

  readonly barras: BarraResumo[] = [
    { chave: 'quantidade_sem_resposta', label: 'Sem resp.' },
    { chave: 'quantidade_nota_1', label: '1' },
    { chave: 'quantidade_nota_2', label: '2' },
    { chave: 'quantidade_nota_3', label: '3' },
    { chave: 'quantidade_nota_4', label: '4' },
    { chave: 'quantidade_nota_5', label: '5' },
  ];

  readonly barChartOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    layout: {
      padding: {
        top: 18,
      },
    },
    plugins: {
      legend: { display: false },
      tooltip: { enabled: true },
    },
    scales: {
      x: {
        grid: { display: false },
      },
      y: {
        beginAtZero: true,
        ticks: {
          precision: 0,
          stepSize: 1,
        },
      },
    },
  };

  readonly barChartPlugins: Plugin<'bar'>[] = [
    {
      id: 'bar-value-labels',
      afterDatasetsDraw: (chart) => {
        const { ctx } = chart;
        const dataset = chart.data.datasets[0];
        const meta = chart.getDatasetMeta(0);

        ctx.save();
        ctx.font = '600 11px Arial';
        ctx.fillStyle = '#334155';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'bottom';

        meta.data.forEach((bar, index) => {
          const value = Number(dataset.data[index] ?? 0);
          const x = bar.x;
          const y = Math.max(bar.y - 4, 12);
          ctx.fillText(String(value), x, y);
        });

        ctx.restore();
      },
    },
  ];

  get mediaTexto(): string {
    if (this.resumo?.media == null) {
      return '-';
    }
    return this.resumo.media.toFixed(2);
  }

  get total(): number {
    return this.resumo?.quantidade_total ?? 0;
  }

  get barChartData(): ChartConfiguration<'bar'>['data'] {
    return {
      labels: this.barras.map((barra) => barra.label),
      datasets: [
        {
          data: this.barras.map((barra) => this.getValor(barra.chave)),
          backgroundColor: [
            '#94a3b8', // Sem resposta
            '#ef4444', // Nota 1
            '#f97316', // Nota 2
            '#f59e0b', // Nota 3
            '#84cc16', // Nota 4
            '#22c55e', // Nota 5
          ],
          borderRadius: 6,
          borderSkipped: false,
          maxBarThickness: 28,
        },
      ],
    };
  }

  getValor(chave: BarraResumo['chave']): number {
    return this.resumo?.[chave] ?? 0;
  }
}
