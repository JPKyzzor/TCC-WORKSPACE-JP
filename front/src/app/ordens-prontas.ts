import { IMainPayload } from './interfaces/main-payload.interface';

export const ORDENS_PRONTAS: IMainPayload[] = [
  {
    idade: 34,
    sexo: 'F',
    peso: 68,
    tipo_frequencia_dose: 'dia',
    frequencia_dose: 2,
    embalagens: ['capsulas gelatinosas'],
    principiosAtivos: [
      { nome: 'Vitamina D3', tipoMedida: 'ui', quantidade: 2000 },
      { nome: 'Magnesio quelado', tipoMedida: 'mg', quantidade: 150 },
      { nome: 'Zinco quelado', tipoMedida: 'mg', quantidade: 15 },
      { nome: 'Melatonina', tipoMedida: 'mg', quantidade: 3 },
    ],
    excipientes: [
      { nome: 'Celulose microcristalina', tipoMedida: 'qsp', quantidade: 0 },
      { nome: 'Dioxido de silicio', tipoMedida: 'mg', quantidade: 5 },
      { nome: 'Estearato de magnesio', tipoMedida: 'mg', quantidade: 2 },
    ],
  },
  {
    idade: 55,
    sexo: 'M',
    peso: 82,
    tipo_frequencia_dose: 'semana',
    frequencia_dose: 1,
    embalagens: ['frasco ambar', 'blister'],
    principiosAtivos: [
      { nome: 'Metformina', tipoMedida: 'mg', quantidade: 500 },
      { nome: 'Gliclazida', tipoMedida: 'mg', quantidade: 30 },
    ],
    excipientes: [
      { nome: 'Lactose', tipoMedida: 'mg', quantidade: 100 },
      { nome: 'Amido', tipoMedida: 'mg', quantidade: 50 },
    ],
  },
  {
    idade: 70,
    sexo: 'F',
    peso: 60,
    tipo_frequencia_dose: 'dia',
    frequencia_dose: 1,
    embalagens: ['blister'],
    principiosAtivos: [{ nome: 'Levotiroxina', tipoMedida: 'mcg', quantidade: 100 }],
    excipientes: [
      { nome: 'Amido de milho', tipoMedida: 'mg', quantidade: 80 },
      { nome: 'Povidona', tipoMedida: 'mg', quantidade: 5 },
    ],
  },
];
