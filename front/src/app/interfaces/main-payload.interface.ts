export interface IMainPayload {
  idade: number;
  sexo: 'M' | 'F';
  peso: number;
  tipo_frequencia_dose: 'dia' | 'semana' | 'mes';
  frequencia_dose: number;
  embalagens: string[];
  principiosAtivos: IExcipiente[];
  excipientes: IExcipiente[];
}

export interface IExcipiente {
  nome: string;
  tipoMedida: 'mg' | 'ml' | 'mcg' | 'ui' | '%' | 'qsp';
  quantidade: number;
}
