package com.jpk.backTCC.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

@MappedSuperclass
@Getter
public abstract class EntidadeComCriacao {

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false, columnDefinition = "timestamptz default now()")
    private OffsetDateTime criadoEm;
}
