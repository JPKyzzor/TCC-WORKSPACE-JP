package com.jpk.backTCC.dto;

import java.util.List;

public class CriarOrdemManipulacaoDTO {

    private Integer idade;
    private String sexo; // "M" ou "F"
    private Double peso;
    private String tipo_frequencia_dose; // dia | semana | mes
    private Integer frequencia_dose;
    private List<ExcipienteDTO> principiosAtivos;
    private List<ExcipienteDTO> excipientes;
    private List<String> embalagens;

    public CriarOrdemManipulacaoDTO() {}

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getTipo_frequencia_dose() {
        return tipo_frequencia_dose;
    }

    public void setTipo_frequencia_dose(String tipo_frequencia_dose) {
        this.tipo_frequencia_dose = tipo_frequencia_dose;
    }

    public Integer getFrequencia_dose() {
        return frequencia_dose;
    }

    public void setFrequencia_dose(Integer frequencia_dose) {
        this.frequencia_dose = frequencia_dose;
    }

    public List<ExcipienteDTO> getPrincipiosAtivos() {
        return principiosAtivos;
    }

    public void setPrincipiosAtivos(List<ExcipienteDTO> principiosAtivos) {
        this.principiosAtivos = principiosAtivos;
    }

    public List<ExcipienteDTO> getExcipientes() {
        return excipientes;
    }

    public void setExcipientes(List<ExcipienteDTO> excipientes) {
        this.excipientes = excipientes;
    }

    public List<String> getEmbalagens() {
        return embalagens;
    }

    public void setEmbalagens(List<String> embalagens) {
        this.embalagens = embalagens;
    }
}

