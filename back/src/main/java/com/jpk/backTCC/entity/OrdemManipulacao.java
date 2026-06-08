package com.jpk.backTCC.entity;

import com.jpk.backTCC.entity.enums.TipoFrequenciaDose;
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
@Table(name = "ordem_manipulacao")
@Getter
@Setter
public class OrdemManipulacao extends EntidadeComCriacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int idade;

    @Column(nullable = false)
    private double peso;

    @Column(nullable = false, length = 1)
    private String sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_frequencia_dose", length = 10)
    private TipoFrequenciaDose tipoFrequenciaDose;

    @Column(name = "frequencia_dose")
    private Integer frequenciaDose;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

}



