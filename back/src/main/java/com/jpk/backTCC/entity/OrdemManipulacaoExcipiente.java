package com.jpk.backTCC.entity;

import com.jpk.backTCC.entity.enums.UnidadeMedida;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ordemManipulacao_excipiente")
@Getter
@Setter
public class OrdemManipulacaoExcipiente extends EntidadeComCriacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private UnidadeMedida unidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ordemManipulacao_id", nullable = false)
    private OrdemManipulacao ordemManipulacao;
}

