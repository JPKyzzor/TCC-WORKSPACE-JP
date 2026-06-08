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
@Table(name = "feedback_embalagem")
@Getter
@Setter
public class EmbalagemFeedback extends EntidadeComCriacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "embalagem_recomendacao_id", nullable = false, unique = true)
    private RecomendacaoEmbalagem recomendacaoEmbalagem;

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
