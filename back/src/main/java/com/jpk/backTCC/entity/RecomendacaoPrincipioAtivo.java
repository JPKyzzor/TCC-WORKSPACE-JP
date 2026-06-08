package com.jpk.backTCC.entity;

import com.jpk.backTCC.entity.enums.NivelEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recomendacao_principio_ativo")
@Getter
@Setter
public class RecomendacaoPrincipioAtivo extends EntidadeComCriacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recomendacao_id", nullable = false)
    private Recomendacao recomendacao;

    @Column(nullable = false)
    private Integer ordem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NivelEnum nivel;

    @Column(nullable = true, length = 1000)
    private String mensagem;

    @OneToOne(mappedBy = "recomendacaoPrincipioAtivo")
    private PrincipioAtivoFeedback feedback;
}

