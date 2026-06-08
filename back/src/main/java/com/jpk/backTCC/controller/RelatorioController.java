package com.jpk.backTCC.controller;

import com.jpk.backTCC.dto.RelatorioResponseDTO;
import com.jpk.backTCC.service.RelatorioService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping
    public ResponseEntity<RelatorioResponseDTO> getRelatorio(
            @RequestParam(name = "usuarios_ids", required = false) List<Long> usuariosIds,
            @RequestParam(name = "feedback_nota_maior", required = false) Integer feedbackNotaMaior,
            @RequestParam(name = "feedback_nota_menor", required = false) Integer feedbackNotaMenor,
            @RequestParam(name = "feedback_preenchimento", required = false) List<String> feedbackPreenchimentos,
            @RequestParam(name = "tester", required = false) Boolean tester
    ) {
        return ResponseEntity.ok(relatorioService.getRelatorioCompleto(
                usuariosIds,
                feedbackNotaMaior,
                feedbackNotaMenor,
                feedbackPreenchimentos,
                tester
        ));
    }
}