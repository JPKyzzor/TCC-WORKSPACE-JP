package com.jpk.backTCC.service;

import com.jpk.backTCC.dto.FeedbackDTO;
import com.jpk.backTCC.entity.EmbalagemFeedback;
import com.jpk.backTCC.entity.ExcipienteFeedback;
import com.jpk.backTCC.entity.FeedbackRecomendacao;
import com.jpk.backTCC.entity.PrincipioAtivoFeedback;
import com.jpk.backTCC.entity.Recomendacao;
import com.jpk.backTCC.entity.RecomendacaoEmbalagem;
import com.jpk.backTCC.entity.RecomendacaoExcipiente;
import com.jpk.backTCC.entity.RecomendacaoPrincipioAtivo;
import com.jpk.backTCC.entity.Usuario;
import com.jpk.backTCC.repository.EmbalagemFeedbackRepository;
import com.jpk.backTCC.repository.ExcipienteFeedbackRepository;
import com.jpk.backTCC.repository.FeedbackRecomendacaoRepository;
import com.jpk.backTCC.repository.PrincipioAtivoFeedbackRepository;
import com.jpk.backTCC.repository.RecomendacaoEmbalagemRepository;
import com.jpk.backTCC.repository.RecomendacaoExcipienteRepository;
import com.jpk.backTCC.repository.RecomendacaoPrincipioAtivoRepository;
import com.jpk.backTCC.repository.RecomendacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FeedbackService {

    private final FeedbackRecomendacaoRepository feedbackRecomendacaoRepository;
    private final ExcipienteFeedbackRepository excipienteFeedbackRepository;
    private final PrincipioAtivoFeedbackRepository principioAtivoFeedbackRepository;
    private final EmbalagemFeedbackRepository embalagemFeedbackRepository;
    private final RecomendacaoRepository recomendacaoRepository;
    private final RecomendacaoExcipienteRepository recomendacaoExcipienteRepository;
    private final RecomendacaoPrincipioAtivoRepository recomendacaoPrincipioAtivoRepository;
    private final RecomendacaoEmbalagemRepository recomendacaoEmbalagemRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public FeedbackService(FeedbackRecomendacaoRepository feedbackRecomendacaoRepository,
                           ExcipienteFeedbackRepository excipienteFeedbackRepository,
                           PrincipioAtivoFeedbackRepository principioAtivoFeedbackRepository,
                           EmbalagemFeedbackRepository embalagemFeedbackRepository,
                           RecomendacaoRepository recomendacaoRepository,
                           RecomendacaoExcipienteRepository recomendacaoExcipienteRepository,
                           RecomendacaoPrincipioAtivoRepository recomendacaoPrincipioAtivoRepository,
                           RecomendacaoEmbalagemRepository recomendacaoEmbalagemRepository,
                           UsuarioAutenticadoService usuarioAutenticadoService) {
        this.feedbackRecomendacaoRepository = feedbackRecomendacaoRepository;
        this.excipienteFeedbackRepository = excipienteFeedbackRepository;
        this.principioAtivoFeedbackRepository = principioAtivoFeedbackRepository;
        this.embalagemFeedbackRepository = embalagemFeedbackRepository;
        this.recomendacaoRepository = recomendacaoRepository;
        this.recomendacaoExcipienteRepository = recomendacaoExcipienteRepository;
        this.recomendacaoPrincipioAtivoRepository = recomendacaoPrincipioAtivoRepository;
        this.recomendacaoEmbalagemRepository = recomendacaoEmbalagemRepository;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional
    public void salvarFeedbackRecomendacao(Long recomendacaoId, FeedbackDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        Recomendacao recomendacao = recomendacaoRepository.findById(recomendacaoId)
                .orElseThrow(() -> notFound("Recomendacao nao encontrada."));

        FeedbackRecomendacao feedback = feedbackRecomendacaoRepository.findByRecomendacaoId(recomendacaoId)
                .orElseGet(FeedbackRecomendacao::new);

        feedback.setRecomendacao(recomendacao);
        feedback.setUsuario(usuarioLogado);
        applyFeedback(feedback, dto);
        feedbackRecomendacaoRepository.save(feedback);
    }

    @Transactional
    public void salvarFeedbackExcipiente(Long recomendacaoExcipienteId, FeedbackDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        RecomendacaoExcipiente recomendacaoExcipiente = recomendacaoExcipienteRepository.findById(recomendacaoExcipienteId)
                .orElseThrow(() -> notFound("Recomendacao de excipiente nao encontrada."));

        ExcipienteFeedback feedback = excipienteFeedbackRepository.findByRecomendacaoExcipienteId(recomendacaoExcipienteId)
                .orElseGet(ExcipienteFeedback::new);

        feedback.setRecomendacaoExcipiente(recomendacaoExcipiente);
        feedback.setUsuario(usuarioLogado);
        applyFeedback(feedback, dto);
        excipienteFeedbackRepository.save(feedback);
    }

    @Transactional
    public void salvarFeedbackPrincipioAtivo(Long recomendacaoPrincipioAtivoId, FeedbackDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        RecomendacaoPrincipioAtivo recomendacaoPrincipioAtivo = recomendacaoPrincipioAtivoRepository.findById(recomendacaoPrincipioAtivoId)
                .orElseThrow(() -> notFound("Recomendacao de principio ativo nao encontrada."));

        PrincipioAtivoFeedback feedback = principioAtivoFeedbackRepository.findByRecomendacaoPrincipioAtivoId(recomendacaoPrincipioAtivoId)
                .orElseGet(PrincipioAtivoFeedback::new);

        feedback.setRecomendacaoPrincipioAtivo(recomendacaoPrincipioAtivo);
        feedback.setUsuario(usuarioLogado);
        applyFeedback(feedback, dto);
        principioAtivoFeedbackRepository.save(feedback);
    }

    @Transactional
    public void salvarFeedbackEmbalagem(Long recomendacaoEmbalagemId, FeedbackDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        RecomendacaoEmbalagem recomendacaoEmbalagem = recomendacaoEmbalagemRepository.findById(recomendacaoEmbalagemId)
                .orElseThrow(() -> notFound("Recomendacao de embalagem nao encontrada."));

        EmbalagemFeedback feedback = embalagemFeedbackRepository.findByRecomendacaoEmbalagemId(recomendacaoEmbalagemId)
                .orElseGet(EmbalagemFeedback::new);

        feedback.setRecomendacaoEmbalagem(recomendacaoEmbalagem);
        feedback.setUsuario(usuarioLogado);
        applyFeedback(feedback, dto);
        embalagemFeedbackRepository.save(feedback);
    }

    private void applyFeedback(FeedbackRecomendacao feedback, FeedbackDTO dto) {
        feedback.setNota(dto.getNota());
        feedback.setObservacoes(dto.getObservacoes());
        feedback.setFalsoPositivo(dto.getFalsoPositivo());
    }

    private void applyFeedback(ExcipienteFeedback feedback, FeedbackDTO dto) {
        feedback.setNota(dto.getNota());
        feedback.setObservacoes(dto.getObservacoes());
        feedback.setFalsoPositivo(dto.getFalsoPositivo());
    }

    private void applyFeedback(PrincipioAtivoFeedback feedback, FeedbackDTO dto) {
        feedback.setNota(dto.getNota());
        feedback.setObservacoes(dto.getObservacoes());
        feedback.setFalsoPositivo(dto.getFalsoPositivo());
    }

    private void applyFeedback(EmbalagemFeedback feedback, FeedbackDTO dto) {
        feedback.setNota(dto.getNota());
        feedback.setObservacoes(dto.getObservacoes());
        feedback.setFalsoPositivo(dto.getFalsoPositivo());
    }

    private ResponseStatusException notFound(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
