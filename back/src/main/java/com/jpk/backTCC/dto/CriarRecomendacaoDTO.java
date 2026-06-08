package com.jpk.backTCC.dto;

import java.util.List;

public class CriarRecomendacaoDTO {

    private List<RecomendacaoDTO> recomendacoesPrincipiosAtivos;
    private List<RecomendacaoDTO> recomendacoesExcipientes;
    private List<RecomendacaoDTO> recomendacoesEmbalagens;
    private RecomendacaoDTO recomendacaoGeral;

    public CriarRecomendacaoDTO() {}

    public List<RecomendacaoDTO> getRecomendacoesPrincipiosAtivos() {
        return recomendacoesPrincipiosAtivos;
    }

    public void setRecomendacoesPrincipiosAtivos(List<RecomendacaoDTO> recomendacoesPrincipiosAtivos) {
        this.recomendacoesPrincipiosAtivos = recomendacoesPrincipiosAtivos;
    }

    public List<RecomendacaoDTO> getRecomendacoesExcipientes() {
        return recomendacoesExcipientes;
    }

    public void setRecomendacoesExcipientes(List<RecomendacaoDTO> recomendacoesExcipientes) {
        this.recomendacoesExcipientes = recomendacoesExcipientes;
    }

    public List<RecomendacaoDTO> getRecomendacoesEmbalagens() {
        return recomendacoesEmbalagens;
    }

    public void setRecomendacoesEmbalagens(List<RecomendacaoDTO> recomendacoesEmbalagens) {
        this.recomendacoesEmbalagens = recomendacoesEmbalagens;
    }

    public RecomendacaoDTO getRecomendacaoGeral() {
        return recomendacaoGeral;
    }

    public void setRecomendacaoGeral(RecomendacaoDTO recomendacaoGeral) {
        this.recomendacaoGeral = recomendacaoGeral;
    }
}
