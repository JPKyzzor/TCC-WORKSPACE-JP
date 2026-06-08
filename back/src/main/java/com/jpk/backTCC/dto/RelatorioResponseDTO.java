package com.jpk.backTCC.dto;

import com.jpk.backTCC.entity.enums.NivelEnum;
import com.jpk.backTCC.entity.enums.TipoFrequenciaDose;
import com.jpk.backTCC.entity.enums.UnidadeMedida;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioResponseDTO {

    private List<OrdemRelatorioDTO> ordens;
    private ResumoMediasDTO resumo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdemRelatorioDTO {
        private Long ordemManipulacaoId;
        private OffsetDateTime criadoEm;
        private Integer idade;
        private Double peso;
        private String sexo;
        private TipoFrequenciaDose tipoFrequenciaDose;
        private Integer frequenciaDose;
        private Long usuarioId;
        private String usuarioLogin;
        private RecomendacaoGeralDTO recomendacaoGeral;
        private List<PrincipioAtivoLinhaDTO> principiosAtivos;
        private List<ExcipienteLinhaDTO> excipientes;
        private List<EmbalagemLinhaDTO> embalagens;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecomendacaoGeralDTO {
        private Long recomendacaoId;
        private NivelEnum nivel;
        private String mensagem;
        private FeedbackItemDTO feedback;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrincipioAtivoLinhaDTO {
        private Long itemId;
        private Integer ordem;
        private String nome;
        private Double quantidade;
        private UnidadeMedida unidade;
        private RecomendacaoItemDTO recomendacao;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExcipienteLinhaDTO {
        private Long itemId;
        private Integer ordem;
        private String nome;
        private Double quantidade;
        private UnidadeMedida unidade;
        private RecomendacaoItemDTO recomendacao;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbalagemLinhaDTO {
        private Long itemId;
        private Integer ordem;
        private String nome;
        private RecomendacaoItemDTO recomendacao;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecomendacaoItemDTO {
        private Long recomendacaoItemId;
        private NivelEnum nivel;
        private String mensagem;
        private FeedbackItemDTO feedback;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackItemDTO {
        private Long feedbackId;
        private Integer nota;
        private String observacoes;
        private Boolean falsoPositivo;
        private Long usuarioId;
        private String usuarioLogin;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResumoMediasDTO {
        private ResumoTipoDTO resumoNotas;
        private ResumoTipoDTO resumoGeral;
        private ResumoTipoDTO resumoPrincipioAtivo;
        private ResumoTipoDTO resumoExcipiente;
        private ResumoTipoDTO resumoEmbalagem;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResumoTipoDTO {
        private Double media;

        @JsonProperty("quantidade_nota_1")
        private Integer quantidadeNota1;

        @JsonProperty("quantidade_nota_2")
        private Integer quantidadeNota2;

        @JsonProperty("quantidade_nota_3")
        private Integer quantidadeNota3;

        @JsonProperty("quantidade_nota_4")
        private Integer quantidadeNota4;

        @JsonProperty("quantidade_nota_5")
        private Integer quantidadeNota5;

        @JsonProperty("quantidade_sem_resposta")
        private Integer quantidadeSemResposta;

        @JsonProperty("quantidade_total")
        private Integer quantidadeTotal;
    }
}
