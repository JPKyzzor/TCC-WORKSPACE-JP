package com.jpk.backTCC.controller;

import com.jpk.backTCC.entity.Recomendacao;
import com.jpk.backTCC.service.RecomendacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recomendacao")
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    public RecomendacaoController(RecomendacaoService recomendacaoService) {
        this.recomendacaoService = recomendacaoService;
    }

    @PostMapping
    public ResponseEntity<Long> criarRecomendacao(@RequestBody Recomendacao recomendacao) {
        Recomendacao salva = recomendacaoService.criarRecomendacao(recomendacao);
        return ResponseEntity.ok(salva.getId());
    }
}

