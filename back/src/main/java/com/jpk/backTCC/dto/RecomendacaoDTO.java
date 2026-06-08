package com.jpk.backTCC.dto;

public class RecomendacaoDTO {

    private NivelRecomendacao nivel;
    private String mensagem;

    public RecomendacaoDTO() {}

    public RecomendacaoDTO(NivelRecomendacao nivel, String mensagem) {
        this.nivel = nivel;
        this.mensagem = mensagem;
    }

    public RecomendacaoDTO(NivelRecomendacao nivel) {
        this.nivel = nivel;
    }

    public NivelRecomendacao getNivel() {
        return nivel;
    }

    public void setNivel(NivelRecomendacao nivel) {
        this.nivel = nivel;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

