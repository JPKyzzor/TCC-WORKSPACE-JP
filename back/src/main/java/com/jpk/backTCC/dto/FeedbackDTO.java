package com.jpk.backTCC.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FeedbackDTO {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer nota;

    @Size(max = 1000)
    private String observacoes;

    private Boolean falsoPositivo;

    public FeedbackDTO() {}

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getFalsoPositivo() {
        return falsoPositivo;
    }

    public void setFalsoPositivo(Boolean falsoPositivo) {
        this.falsoPositivo = falsoPositivo;
    }
}
