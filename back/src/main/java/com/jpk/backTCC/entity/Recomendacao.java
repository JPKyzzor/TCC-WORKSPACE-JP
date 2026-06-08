package com.jpk.backTCC.entity;

import com.jpk.backTCC.entity.enums.NivelEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recomendacao")
@Getter
@Setter
public class Recomendacao extends EntidadeComCriacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mensagem_geral", length = 1000)
    private String mensagemGeral;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_geral", length = 10)
    private NivelEnum nivelGeral;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ordemManipulacao_id", nullable = false)
    private OrdemManipulacao ordemManipulacao;

    @OneToMany(mappedBy = "recomendacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecomendacaoExcipiente> recomendacoesExcipientes = new ArrayList<>();

    @OneToMany(mappedBy = "recomendacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecomendacaoPrincipioAtivo> recomendacoesPrincipiosAtivos = new ArrayList<>();

    @OneToMany(mappedBy = "recomendacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecomendacaoEmbalagem> recomendacoesEmbalagens = new ArrayList<>();

    @OneToOne(mappedBy = "recomendacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private FeedbackRecomendacao feedback;
}

