package com.jpk.backTCC.dto;

public class ExcipienteDTO {

    private String nome;
    private String tipoMedida; // mg, ml, mcg, ui, %, qsp
    private Double quantidade;

    public ExcipienteDTO() {}

    public ExcipienteDTO(String nome, String tipoMedida, Double quantidade) {
        this.nome = nome;
        this.tipoMedida = tipoMedida;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(String tipoMedida) {
        this.tipoMedida = tipoMedida;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}

