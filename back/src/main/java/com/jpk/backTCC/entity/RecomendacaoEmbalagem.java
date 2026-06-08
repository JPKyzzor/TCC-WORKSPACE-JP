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
@Table(name = "recomendacao_embalagem")
@Getter
@Setter
public class RecomendacaoEmbalagem extends EntidadeComCriacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer ordem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NivelEnum nivel;

    @Column(nullable = false, length = 1000)
    private String mensagem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recomendacao_id", nullable = false)
    private Recomendacao recomendacao;

    @OneToOne(mappedBy = "recomendacaoEmbalagem")
    private EmbalagemFeedback feedback;
}
