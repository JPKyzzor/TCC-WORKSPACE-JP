package com.jpk.backTCC.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "feedback_principio_ativo")
@Getter
@Setter
public class PrincipioAtivoFeedback extends EntidadeComCriacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "principio_ativo_recomendacao_id", nullable = false, unique = true)
    private RecomendacaoPrincipioAtivo recomendacaoPrincipioAtivo;

    @Column(length = 1000)
    private String observacoes;

    @Column(nullable = false)
    private Integer nota;

    @Column(name = "falso_positivo")
    private Boolean falsoPositivo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
