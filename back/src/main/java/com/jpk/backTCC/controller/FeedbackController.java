package com.jpk.backTCC.controller;

import com.jpk.backTCC.dto.FeedbackDTO;
import com.jpk.backTCC.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PutMapping("/recomendacao/{recomendacaoId}")
    public ResponseEntity<Void> salvarFeedbackRecomendacao(@PathVariable Long recomendacaoId,
                                                            @Valid @RequestBody FeedbackDTO payload) {
        feedbackService.salvarFeedbackRecomendacao(recomendacaoId, payload);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/principio-ativo/{recomendacaoPrincipioAtivoId}")
    public ResponseEntity<Void> salvarFeedbackPrincipioAtivo(@PathVariable Long recomendacaoPrincipioAtivoId,
                                                              @Valid @RequestBody FeedbackDTO payload) {
        feedbackService.salvarFeedbackPrincipioAtivo(recomendacaoPrincipioAtivoId, payload);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/excipiente/{recomendacaoExcipienteId}")
    public ResponseEntity<Void> salvarFeedbackExcipiente(@PathVariable Long recomendacaoExcipienteId,
                                                          @Valid @RequestBody FeedbackDTO payload) {
        feedbackService.salvarFeedbackExcipiente(recomendacaoExcipienteId, payload);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/embalagem/{recomendacaoEmbalagemId}")
    public ResponseEntity<Void> salvarFeedbackEmbalagem(@PathVariable Long recomendacaoEmbalagemId,
                                                         @Valid @RequestBody FeedbackDTO payload) {
        feedbackService.salvarFeedbackEmbalagem(recomendacaoEmbalagemId, payload);
        return ResponseEntity.noContent().build();
    }
}
