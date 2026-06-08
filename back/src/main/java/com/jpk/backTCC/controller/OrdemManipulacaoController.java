package com.jpk.backTCC.controller;

import com.jpk.backTCC.dto.OrdemManipulacaoResponseDTO;
import com.jpk.backTCC.dto.CriarOrdemManipulacaoDTO;
import com.jpk.backTCC.service.OrdemManipulacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ordem-manipulacao")
public class OrdemManipulacaoController {

    private final OrdemManipulacaoService ordemManipulacaoService;

    public OrdemManipulacaoController(OrdemManipulacaoService ordemManipulacaoService) {
        this.ordemManipulacaoService = ordemManipulacaoService;
    }

    @PostMapping
    public ResponseEntity<OrdemManipulacaoResponseDTO> criarOrdemManipulacao(@RequestBody CriarOrdemManipulacaoDTO payload) {
        OrdemManipulacaoResponseDTO resposta = ordemManipulacaoService.criarOrdemManipulacao(payload);
        return ResponseEntity.ok(resposta);
    }
}
