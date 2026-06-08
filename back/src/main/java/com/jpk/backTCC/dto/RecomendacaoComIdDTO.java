package com.jpk.backTCC.dto;

public class RecomendacaoComIdDTO {

    private Long id;
    private NivelRecomendacao nivel;
    private String mensagem;

    public RecomendacaoComIdDTO() {}

    public RecomendacaoComIdDTO(Long id, NivelRecomendacao nivel, String mensagem) {
        this.id = id;
        this.nivel = nivel;
        this.mensagem = mensagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
