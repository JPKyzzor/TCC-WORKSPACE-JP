package com.jpk.backTCC.dto;

import java.util.List;

public class OrdemManipulacaoResponseDTO {

    private Long ordemManipulacaoId;
    private RecomendacaoComIdDTO recomendacaoGeral;
    private List<RecomendacaoComIdDTO> recomendacoesPrincipiosAtivos;
    private List<RecomendacaoComIdDTO> recomendacoesExcipientes;
    private List<RecomendacaoComIdDTO> recomendacoesEmbalagens;

    public OrdemManipulacaoResponseDTO() {}

    public Long getOrdemManipulacaoId() {
        return ordemManipulacaoId;
    }

    public void setOrdemManipulacaoId(Long ordemManipulacaoId) {
        this.ordemManipulacaoId = ordemManipulacaoId;
    }

    public RecomendacaoComIdDTO getRecomendacaoGeral() {
        return recomendacaoGeral;
    }

    public void setRecomendacaoGeral(RecomendacaoComIdDTO recomendacaoGeral) {
        this.recomendacaoGeral = recomendacaoGeral;
    }

    public List<RecomendacaoComIdDTO> getRecomendacoesPrincipiosAtivos() {
        return recomendacoesPrincipiosAtivos;
    }

    public void setRecomendacoesPrincipiosAtivos(List<RecomendacaoComIdDTO> recomendacoesPrincipiosAtivos) {
        this.recomendacoesPrincipiosAtivos = recomendacoesPrincipiosAtivos;
    }

    public List<RecomendacaoComIdDTO> getRecomendacoesExcipientes() {
        return recomendacoesExcipientes;
    }

    public void setRecomendacoesExcipientes(List<RecomendacaoComIdDTO> recomendacoesExcipientes) {
        this.recomendacoesExcipientes = recomendacoesExcipientes;
    }

    public List<RecomendacaoComIdDTO> getRecomendacoesEmbalagens() {
        return recomendacoesEmbalagens;
    }

    public void setRecomendacoesEmbalagens(List<RecomendacaoComIdDTO> recomendacoesEmbalagens) {
        this.recomendacoesEmbalagens = recomendacoesEmbalagens;
    }
}
