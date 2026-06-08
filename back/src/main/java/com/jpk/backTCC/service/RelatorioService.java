package com.jpk.backTCC.service;

import com.jpk.backTCC.dto.RelatorioResponseDTO;
import com.jpk.backTCC.entity.EmbalagemFeedback;
import com.jpk.backTCC.entity.ExcipienteFeedback;
import com.jpk.backTCC.entity.FeedbackRecomendacao;
import com.jpk.backTCC.entity.OrdemManipulacao;
import com.jpk.backTCC.entity.OrdemManipulacaoEmbalagem;
import com.jpk.backTCC.entity.OrdemManipulacaoExcipiente;
import com.jpk.backTCC.entity.OrdemManipulacaoPrincipioAtivo;
import com.jpk.backTCC.entity.PrincipioAtivoFeedback;
import com.jpk.backTCC.entity.Recomendacao;
import com.jpk.backTCC.entity.RecomendacaoEmbalagem;
import com.jpk.backTCC.entity.RecomendacaoExcipiente;
import com.jpk.backTCC.entity.RecomendacaoPrincipioAtivo;
import com.jpk.backTCC.entity.Usuario;
import com.jpk.backTCC.repository.EmbalagemFeedbackRepository;
import com.jpk.backTCC.repository.ExcipienteFeedbackRepository;
import com.jpk.backTCC.repository.FeedbackRecomendacaoRepository;
import com.jpk.backTCC.repository.OrdemManipulacaoEmbalagemRepository;
import com.jpk.backTCC.repository.OrdemManipulacaoExcipienteRepository;
import com.jpk.backTCC.repository.OrdemManipulacaoPrincipioAtivoRepository;
import com.jpk.backTCC.repository.OrdemManipulacaoRepository;
import com.jpk.backTCC.repository.PrincipioAtivoFeedbackRepository;
import com.jpk.backTCC.repository.RecomendacaoEmbalagemRepository;
import com.jpk.backTCC.repository.RecomendacaoExcipienteRepository;
import com.jpk.backTCC.repository.RecomendacaoPrincipioAtivoRepository;
import com.jpk.backTCC.repository.RecomendacaoRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RelatorioService {

    private static final long ADMIN_USER_ID = 1L;

    private final OrdemManipulacaoRepository ordemManipulacaoRepository;
    private final OrdemManipulacaoPrincipioAtivoRepository ordemManipulacaoPrincipioAtivoRepository;
    private final OrdemManipulacaoExcipienteRepository ordemManipulacaoExcipienteRepository;
    private final OrdemManipulacaoEmbalagemRepository ordemManipulacaoEmbalagemRepository;
    private final RecomendacaoRepository recomendacaoRepository;
    private final RecomendacaoPrincipioAtivoRepository recomendacaoPrincipioAtivoRepository;
    private final RecomendacaoExcipienteRepository recomendacaoExcipienteRepository;
    private final RecomendacaoEmbalagemRepository recomendacaoEmbalagemRepository;
    private final FeedbackRecomendacaoRepository feedbackRecomendacaoRepository;
    private final PrincipioAtivoFeedbackRepository principioAtivoFeedbackRepository;
    private final ExcipienteFeedbackRepository excipienteFeedbackRepository;
    private final EmbalagemFeedbackRepository embalagemFeedbackRepository;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public RelatorioService(OrdemManipulacaoRepository ordemManipulacaoRepository,
                            OrdemManipulacaoPrincipioAtivoRepository ordemManipulacaoPrincipioAtivoRepository,
                            OrdemManipulacaoExcipienteRepository ordemManipulacaoExcipienteRepository,
                            OrdemManipulacaoEmbalagemRepository ordemManipulacaoEmbalagemRepository,
                            RecomendacaoRepository recomendacaoRepository,
                            RecomendacaoPrincipioAtivoRepository recomendacaoPrincipioAtivoRepository,
                            RecomendacaoExcipienteRepository recomendacaoExcipienteRepository,
                            RecomendacaoEmbalagemRepository recomendacaoEmbalagemRepository,
                            FeedbackRecomendacaoRepository feedbackRecomendacaoRepository,
                            PrincipioAtivoFeedbackRepository principioAtivoFeedbackRepository,
                            ExcipienteFeedbackRepository excipienteFeedbackRepository,
                            EmbalagemFeedbackRepository embalagemFeedbackRepository,
                            UsuarioAutenticadoService usuarioAutenticadoService) {
        this.ordemManipulacaoRepository = ordemManipulacaoRepository;
        this.ordemManipulacaoPrincipioAtivoRepository = ordemManipulacaoPrincipioAtivoRepository;
        this.ordemManipulacaoExcipienteRepository = ordemManipulacaoExcipienteRepository;
        this.ordemManipulacaoEmbalagemRepository = ordemManipulacaoEmbalagemRepository;
        this.recomendacaoRepository = recomendacaoRepository;
        this.recomendacaoPrincipioAtivoRepository = recomendacaoPrincipioAtivoRepository;
        this.recomendacaoExcipienteRepository = recomendacaoExcipienteRepository;
        this.recomendacaoEmbalagemRepository = recomendacaoEmbalagemRepository;
        this.feedbackRecomendacaoRepository = feedbackRecomendacaoRepository;
        this.principioAtivoFeedbackRepository = principioAtivoFeedbackRepository;
        this.excipienteFeedbackRepository = excipienteFeedbackRepository;
        this.embalagemFeedbackRepository = embalagemFeedbackRepository;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Transactional(readOnly = true)
    public RelatorioResponseDTO getRelatorioCompleto(List<Long> usuariosIds,
                                                     Integer feedbackNotaMaior,
                                                     Integer feedbackNotaMenor,
                                                     List<String> feedbackPreenchimentos,
                                                     Boolean tester) {
        validarAcessoAdmin();
        Set<FeedbackPreenchimentoFiltro> preenchimentosFiltro = parseFeedbackPreenchimentos(feedbackPreenchimentos);
        Set<Long> usuariosIdsFiltro = usuariosIds == null
                ? Collections.emptySet()
                : usuariosIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());

        List<OrdemManipulacao> ordens = ordemManipulacaoRepository.findAll();
        ordens.sort(Comparator.comparing(OrdemManipulacao::getId));
        if (ordens.isEmpty()) {
            RelatorioResponseDTO.ResumoTipoDTO vazio = new RelatorioResponseDTO.ResumoTipoDTO(null, 0, 0, 0, 0, 0, 0, 0);
            return new RelatorioResponseDTO(
                    Collections.emptyList(),
                    new RelatorioResponseDTO.ResumoMediasDTO(vazio, vazio, vazio, vazio, vazio)
            );
        }

        List<Long> ordemIds = ordens.stream().map(OrdemManipulacao::getId).toList();

        List<OrdemManipulacaoPrincipioAtivo> principiosAtivos = ordemManipulacaoPrincipioAtivoRepository
                .findByOrdemManipulacaoIdInOrderByOrdemManipulacaoIdAscOrdemAsc(ordemIds);
        List<OrdemManipulacaoExcipiente> excipientes = ordemManipulacaoExcipienteRepository
                .findByOrdemManipulacaoIdInOrderByOrdemManipulacaoIdAscOrdemAsc(ordemIds);
        List<OrdemManipulacaoEmbalagem> embalagens = ordemManipulacaoEmbalagemRepository
                .findByOrdemManipulacaoIdInOrderByOrdemManipulacaoIdAscOrdemAsc(ordemIds);

        Map<Long, List<OrdemManipulacaoPrincipioAtivo>> principiosAtivosPorOrdem = groupByOrdemId(principiosAtivos);
        Map<Long, List<OrdemManipulacaoExcipiente>> excipientesPorOrdem = groupByOrdemId(excipientes);
        Map<Long, List<OrdemManipulacaoEmbalagem>> embalagensPorOrdem = groupByOrdemId(embalagens);

        List<Recomendacao> recomendacoes = recomendacaoRepository.findByOrdemManipulacaoIdIn(ordemIds);
        Map<Long, Recomendacao> recomendacaoPorOrdemId = recomendacoes.stream()
                .sorted(Comparator.comparing(Recomendacao::getId))
                .collect(Collectors.toMap(r -> r.getOrdemManipulacao().getId(), r -> r, (existente, ignorar) -> existente, LinkedHashMap::new));

        List<Long> recomendacaoIds = recomendacoes.stream().map(Recomendacao::getId).toList();

        List<RecomendacaoPrincipioAtivo> recomendacoesPrincipioAtivo = recomendacaoIds.isEmpty()
                ? Collections.emptyList()
                : recomendacaoPrincipioAtivoRepository.findByRecomendacaoIdInOrderByRecomendacaoIdAscOrdemAsc(recomendacaoIds);
        List<RecomendacaoExcipiente> recomendacoesExcipiente = recomendacaoIds.isEmpty()
                ? Collections.emptyList()
                : recomendacaoExcipienteRepository.findByRecomendacaoIdInOrderByRecomendacaoIdAscOrdemAsc(recomendacaoIds);
        List<RecomendacaoEmbalagem> recomendacoesEmbalagem = recomendacaoIds.isEmpty()
                ? Collections.emptyList()
                : recomendacaoEmbalagemRepository.findByRecomendacaoIdInOrderByRecomendacaoIdAscOrdemAsc(recomendacaoIds);

        Map<Long, List<RecomendacaoPrincipioAtivo>> recomendacoesPrincipioAtivoPorRecomendacao = groupByRecomendacaoId(recomendacoesPrincipioAtivo);
        Map<Long, List<RecomendacaoExcipiente>> recomendacoesExcipientePorRecomendacao = groupByRecomendacaoId(recomendacoesExcipiente);
        Map<Long, List<RecomendacaoEmbalagem>> recomendacoesEmbalagemPorRecomendacao = groupByRecomendacaoId(recomendacoesEmbalagem);

        List<FeedbackRecomendacao> feedbacksRecomendacao = recomendacaoIds.isEmpty()
                ? Collections.emptyList()
                : feedbackRecomendacaoRepository.findByRecomendacaoIdIn(recomendacaoIds);

        List<Long> recomendacaoPrincipioAtivoIds = recomendacoesPrincipioAtivo.stream().map(RecomendacaoPrincipioAtivo::getId).toList();
        List<Long> recomendacaoExcipienteIds = recomendacoesExcipiente.stream().map(RecomendacaoExcipiente::getId).toList();
        List<Long> recomendacaoEmbalagemIds = recomendacoesEmbalagem.stream().map(RecomendacaoEmbalagem::getId).toList();

        List<PrincipioAtivoFeedback> feedbacksPrincipioAtivo = recomendacaoPrincipioAtivoIds.isEmpty()
                ? Collections.emptyList()
                : principioAtivoFeedbackRepository.findByRecomendacaoPrincipioAtivoIdIn(recomendacaoPrincipioAtivoIds);
        List<ExcipienteFeedback> feedbacksExcipiente = recomendacaoExcipienteIds.isEmpty()
                ? Collections.emptyList()
                : excipienteFeedbackRepository.findByRecomendacaoExcipienteIdIn(recomendacaoExcipienteIds);
        List<EmbalagemFeedback> feedbacksEmbalagem = recomendacaoEmbalagemIds.isEmpty()
                ? Collections.emptyList()
                : embalagemFeedbackRepository.findByRecomendacaoEmbalagemIdIn(recomendacaoEmbalagemIds);

        Map<Long, FeedbackRecomendacao> feedbackRecomendacaoPorRecomendacaoId = feedbacksRecomendacao.stream()
                .collect(Collectors.toMap(f -> f.getRecomendacao().getId(), f -> f));
        Map<Long, PrincipioAtivoFeedback> feedbackPrincipioAtivoPorRecomendacaoItemId = feedbacksPrincipioAtivo.stream()
                .collect(Collectors.toMap(f -> f.getRecomendacaoPrincipioAtivo().getId(), f -> f));
        Map<Long, ExcipienteFeedback> feedbackExcipientePorRecomendacaoItemId = feedbacksExcipiente.stream()
                .collect(Collectors.toMap(f -> f.getRecomendacaoExcipiente().getId(), f -> f));
        Map<Long, EmbalagemFeedback> feedbackEmbalagemPorRecomendacaoItemId = feedbacksEmbalagem.stream()
                .collect(Collectors.toMap(f -> f.getRecomendacaoEmbalagem().getId(), f -> f));

        List<OrdemManipulacao> ordensFiltradas = ordens.stream()
            .filter(ordem -> passouFiltroUsuarios(ordem, usuariosIdsFiltro))
            .filter(ordem -> passouFiltroTester(ordem, tester))
            .filter(ordem -> {
                FeedbackStatusPorOrdem status = montarStatusFeedback(
                    ordem,
                    recomendacaoPorOrdemId,
                    recomendacoesPrincipioAtivoPorRecomendacao,
                    recomendacoesExcipientePorRecomendacao,
                    recomendacoesEmbalagemPorRecomendacao,
                    feedbackRecomendacaoPorRecomendacaoId,
                    feedbackPrincipioAtivoPorRecomendacaoItemId,
                    feedbackExcipientePorRecomendacaoItemId,
                    feedbackEmbalagemPorRecomendacaoItemId
                );
                return passouFiltroNotaMaior(status.notas(), feedbackNotaMaior)
                    && passouFiltroNotaMenor(status.notas(), feedbackNotaMenor)
                    && passouFiltroPreenchimento(status, preenchimentosFiltro);
            })
            .toList();

        if (ordensFiltradas.isEmpty()) {
            RelatorioResponseDTO.ResumoTipoDTO vazio = new RelatorioResponseDTO.ResumoTipoDTO(null, 0, 0, 0, 0, 0, 0, 0);
            return new RelatorioResponseDTO(
                    Collections.emptyList(),
                    new RelatorioResponseDTO.ResumoMediasDTO(vazio, vazio, vazio, vazio, vazio)
            );
        }

        ResumoAcumulador resumoNotas = new ResumoAcumulador();
        ResumoAcumulador resumoGeral = new ResumoAcumulador();
        ResumoAcumulador resumoPrincipioAtivo = new ResumoAcumulador();
        ResumoAcumulador resumoExcipiente = new ResumoAcumulador();
        ResumoAcumulador resumoEmbalagem = new ResumoAcumulador();

        List<RelatorioResponseDTO.OrdemRelatorioDTO> ordemDtos = new ArrayList<>();
        for (OrdemManipulacao ordem : ordensFiltradas) {
            Recomendacao recomendacao = recomendacaoPorOrdemId.get(ordem.getId());
            Map<Integer, RecomendacaoPrincipioAtivo> recomendacaoPaPorOrdem = mapRecomendacaoPorOrdem(
                    recomendacao,
                    recomendacoesPrincipioAtivoPorRecomendacao,
                    RecomendacaoPrincipioAtivo::getOrdem
            );
            Map<Integer, RecomendacaoExcipiente> recomendacaoExcipientePorOrdem = mapRecomendacaoPorOrdem(
                    recomendacao,
                    recomendacoesExcipientePorRecomendacao,
                    RecomendacaoExcipiente::getOrdem
            );
            Map<Integer, RecomendacaoEmbalagem> recomendacaoEmbalagemPorOrdem = mapRecomendacaoPorOrdem(
                    recomendacao,
                    recomendacoesEmbalagemPorRecomendacao,
                    RecomendacaoEmbalagem::getOrdem
            );

                    FeedbackRecomendacao feedbackGeral = recomendacao == null
                        ? null
                        : feedbackRecomendacaoPorRecomendacaoId.get(recomendacao.getId());
                    if (recomendacao != null) {
                        resumoGeral.adicionar(feedbackGeral == null ? null : feedbackGeral.getNota());
                        resumoNotas.adicionar(feedbackGeral == null ? null : feedbackGeral.getNota());
                    }

                    List<RelatorioResponseDTO.PrincipioAtivoLinhaDTO> principiosAtivosDto = new ArrayList<>();
                    for (OrdemManipulacaoPrincipioAtivo item : principiosAtivosPorOrdem.getOrDefault(ordem.getId(), Collections.emptyList())) {
                    RecomendacaoPrincipioAtivo recomendacaoItem = recomendacaoPaPorOrdem.get(item.getOrdem());
                    PrincipioAtivoFeedback feedback = recomendacaoItem == null
                        ? null
                        : feedbackPrincipioAtivoPorRecomendacaoItemId.get(recomendacaoItem.getId());
                    resumoPrincipioAtivo.adicionar(feedback == null ? null : feedback.getNota());
                    resumoNotas.adicionar(feedback == null ? null : feedback.getNota());

                    principiosAtivosDto.add(new RelatorioResponseDTO.PrincipioAtivoLinhaDTO(
                        item.getId(),
                        item.getOrdem(),
                        item.getNome(),
                        item.getQuantidade(),
                        item.getUnidade(),
                        toRecomendacaoItemDTO(recomendacaoItem, feedbackPrincipioAtivoPorRecomendacaoItemId)
                    ));
                    }

                    List<RelatorioResponseDTO.ExcipienteLinhaDTO> excipientesDto = new ArrayList<>();
                    for (OrdemManipulacaoExcipiente item : excipientesPorOrdem.getOrDefault(ordem.getId(), Collections.emptyList())) {
                    RecomendacaoExcipiente recomendacaoItem = recomendacaoExcipientePorOrdem.get(item.getOrdem());
                    ExcipienteFeedback feedback = recomendacaoItem == null
                        ? null
                        : feedbackExcipientePorRecomendacaoItemId.get(recomendacaoItem.getId());
                    resumoExcipiente.adicionar(feedback == null ? null : feedback.getNota());
                    resumoNotas.adicionar(feedback == null ? null : feedback.getNota());

                    excipientesDto.add(new RelatorioResponseDTO.ExcipienteLinhaDTO(
                        item.getId(),
                        item.getOrdem(),
                        item.getNome(),
                        item.getQuantidade(),
                        item.getUnidade(),
                        toRecomendacaoItemDTO(recomendacaoItem, feedbackExcipientePorRecomendacaoItemId)
                    ));
                    }

                    List<RelatorioResponseDTO.EmbalagemLinhaDTO> embalagensDto = new ArrayList<>();
                    for (OrdemManipulacaoEmbalagem item : embalagensPorOrdem.getOrDefault(ordem.getId(), Collections.emptyList())) {
                    RecomendacaoEmbalagem recomendacaoItem = recomendacaoEmbalagemPorOrdem.get(item.getOrdem());
                    EmbalagemFeedback feedback = recomendacaoItem == null
                        ? null
                        : feedbackEmbalagemPorRecomendacaoItemId.get(recomendacaoItem.getId());
                    resumoEmbalagem.adicionar(feedback == null ? null : feedback.getNota());
                    resumoNotas.adicionar(feedback == null ? null : feedback.getNota());

                    embalagensDto.add(new RelatorioResponseDTO.EmbalagemLinhaDTO(
                        item.getId(),
                        item.getOrdem(),
                        item.getNome(),
                        toRecomendacaoItemDTO(recomendacaoItem, feedbackEmbalagemPorRecomendacaoItemId)
                    ));
                    }

            RelatorioResponseDTO.RecomendacaoGeralDTO recomendacaoGeral = toRecomendacaoGeralDTO(
                    recomendacao,
                    feedbackRecomendacaoPorRecomendacaoId
            );

            ordemDtos.add(new RelatorioResponseDTO.OrdemRelatorioDTO(
                    ordem.getId(),
                    ordem.getCriadoEm(),
                    ordem.getIdade(),
                    ordem.getPeso(),
                    ordem.getSexo(),
                    ordem.getTipoFrequenciaDose(),
                    ordem.getFrequenciaDose(),
                    ordem.getUsuario().getId(),
                    ordem.getUsuario().getLogin(),
                    recomendacaoGeral,
                    principiosAtivosDto,
                    excipientesDto,
                    embalagensDto
            ));
        }

        RelatorioResponseDTO.ResumoMediasDTO resumo = new RelatorioResponseDTO.ResumoMediasDTO(
                resumoNotas.toDTO(),
                resumoGeral.toDTO(),
                resumoPrincipioAtivo.toDTO(),
                resumoExcipiente.toDTO(),
                resumoEmbalagem.toDTO()
        );

        return new RelatorioResponseDTO(ordemDtos, resumo);
    }

    private void validarAcessoAdmin() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        if (usuarioLogado.getId() == null || usuarioLogado.getId() != ADMIN_USER_ID) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso restrito ao administrador.");
        }
    }

    private RelatorioResponseDTO.RecomendacaoGeralDTO toRecomendacaoGeralDTO(
            Recomendacao recomendacao,
            Map<Long, FeedbackRecomendacao> feedbackPorRecomendacaoId
    ) {
        if (recomendacao == null) {
            return null;
        }

        FeedbackRecomendacao feedback = feedbackPorRecomendacaoId.get(recomendacao.getId());
        return new RelatorioResponseDTO.RecomendacaoGeralDTO(
                recomendacao.getId(),
                recomendacao.getNivelGeral(),
                recomendacao.getMensagemGeral(),
                toFeedbackItemDTO(feedback)
        );
    }

    private RelatorioResponseDTO.RecomendacaoItemDTO toRecomendacaoItemDTO(
            RecomendacaoPrincipioAtivo recomendacao,
            Map<Long, PrincipioAtivoFeedback> feedbackPorRecomendacaoItemId
    ) {
        if (recomendacao == null) {
            return null;
        }

        return new RelatorioResponseDTO.RecomendacaoItemDTO(
                recomendacao.getId(),
                recomendacao.getNivel(),
                recomendacao.getMensagem(),
                toFeedbackItemDTO(feedbackPorRecomendacaoItemId.get(recomendacao.getId()))
        );
    }

    private RelatorioResponseDTO.RecomendacaoItemDTO toRecomendacaoItemDTO(
            RecomendacaoExcipiente recomendacao,
            Map<Long, ExcipienteFeedback> feedbackPorRecomendacaoItemId
    ) {
        if (recomendacao == null) {
            return null;
        }

        return new RelatorioResponseDTO.RecomendacaoItemDTO(
                recomendacao.getId(),
                recomendacao.getNivel(),
                recomendacao.getMensagem(),
                toFeedbackItemDTO(feedbackPorRecomendacaoItemId.get(recomendacao.getId()))
        );
    }

    private RelatorioResponseDTO.RecomendacaoItemDTO toRecomendacaoItemDTO(
            RecomendacaoEmbalagem recomendacao,
            Map<Long, EmbalagemFeedback> feedbackPorRecomendacaoItemId
    ) {
        if (recomendacao == null) {
            return null;
        }

        return new RelatorioResponseDTO.RecomendacaoItemDTO(
                recomendacao.getId(),
                recomendacao.getNivel(),
                recomendacao.getMensagem(),
                toFeedbackItemDTO(feedbackPorRecomendacaoItemId.get(recomendacao.getId()))
        );
    }

    private RelatorioResponseDTO.FeedbackItemDTO toFeedbackItemDTO(FeedbackRecomendacao feedback) {
        if (feedback == null) {
            return null;
        }

        return new RelatorioResponseDTO.FeedbackItemDTO(
                feedback.getId(),
                feedback.getNota(),
                feedback.getObservacoes(),
                feedback.getFalsoPositivo(),
                feedback.getUsuario().getId(),
                feedback.getUsuario().getLogin()
        );
    }

    private RelatorioResponseDTO.FeedbackItemDTO toFeedbackItemDTO(PrincipioAtivoFeedback feedback) {
        if (feedback == null) {
            return null;
        }

        return new RelatorioResponseDTO.FeedbackItemDTO(
                feedback.getId(),
                feedback.getNota(),
                feedback.getObservacoes(),
                feedback.getFalsoPositivo(),
                feedback.getUsuario().getId(),
                feedback.getUsuario().getLogin()
        );
    }

    private RelatorioResponseDTO.FeedbackItemDTO toFeedbackItemDTO(ExcipienteFeedback feedback) {
        if (feedback == null) {
            return null;
        }

        return new RelatorioResponseDTO.FeedbackItemDTO(
                feedback.getId(),
                feedback.getNota(),
                feedback.getObservacoes(),
                feedback.getFalsoPositivo(),
                feedback.getUsuario().getId(),
                feedback.getUsuario().getLogin()
        );
    }

    private RelatorioResponseDTO.FeedbackItemDTO toFeedbackItemDTO(EmbalagemFeedback feedback) {
        if (feedback == null) {
            return null;
        }

        return new RelatorioResponseDTO.FeedbackItemDTO(
                feedback.getId(),
                feedback.getNota(),
                feedback.getObservacoes(),
                feedback.getFalsoPositivo(),
                feedback.getUsuario().getId(),
                feedback.getUsuario().getLogin()
        );
    }

    private Set<FeedbackPreenchimentoFiltro> parseFeedbackPreenchimentos(List<String> feedbackPreenchimentos) {
        if (feedbackPreenchimentos == null || feedbackPreenchimentos.isEmpty()) {
            return Collections.emptySet();
        }

        Set<FeedbackPreenchimentoFiltro> filtros = new java.util.HashSet<>();
        for (String item : feedbackPreenchimentos) {
            if (item == null || item.isBlank()) {
                continue;
            }

            String valor = item.trim().toLowerCase(Locale.ROOT);
            FeedbackPreenchimentoFiltro filtro = switch (valor) {
                case "total" -> FeedbackPreenchimentoFiltro.TOTAL;
                case "parcial" -> FeedbackPreenchimentoFiltro.PARCIAL;
                case "nenhum" -> FeedbackPreenchimentoFiltro.NENHUM;
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "feedback_preenchimento invalido. Use: total, parcial ou nenhum.");
            };
            filtros.add(filtro);
        }
        return filtros;
    }

    private boolean passouFiltroTester(OrdemManipulacao ordem, Boolean tester) {
        if (tester == null) {
            return true;
        }
        return ordem.getUsuario().isTester() == tester;
    }

    private boolean passouFiltroUsuarios(OrdemManipulacao ordem, Set<Long> usuariosIdsFiltro) {
        if (usuariosIdsFiltro == null || usuariosIdsFiltro.isEmpty()) {
            return true;
        }
        return usuariosIdsFiltro.contains(ordem.getUsuario().getId());
    }

    private boolean passouFiltroNotaMaior(List<Integer> notas, Integer feedbackNotaMaior) {
        if (feedbackNotaMaior == null) {
            return true;
        }
        return notas.stream().anyMatch(nota -> nota >= feedbackNotaMaior);
    }

    private boolean passouFiltroNotaMenor(List<Integer> notas, Integer feedbackNotaMenor) {
        if (feedbackNotaMenor == null) {
            return true;
        }
        return notas.stream().anyMatch(nota -> nota <= feedbackNotaMenor);
    }

    private boolean passouFiltroPreenchimento(FeedbackStatusPorOrdem status, Set<FeedbackPreenchimentoFiltro> preenchimentosFiltro) {
        if (preenchimentosFiltro == null || preenchimentosFiltro.isEmpty()) {
            return true;
        }

        boolean atendeTotal = status.totalEsperado() > 0 && status.preenchidos() == status.totalEsperado();
        boolean atendeParcial = status.preenchidos() > 0 && status.preenchidos() < status.totalEsperado();
        boolean atendeNenhum = status.preenchidos() == 0;

        return (preenchimentosFiltro.contains(FeedbackPreenchimentoFiltro.TOTAL) && atendeTotal)
                || (preenchimentosFiltro.contains(FeedbackPreenchimentoFiltro.PARCIAL) && atendeParcial)
                || (preenchimentosFiltro.contains(FeedbackPreenchimentoFiltro.NENHUM) && atendeNenhum);
    }

    private FeedbackStatusPorOrdem montarStatusFeedback(
            OrdemManipulacao ordem,
            Map<Long, Recomendacao> recomendacaoPorOrdemId,
            Map<Long, List<RecomendacaoPrincipioAtivo>> recomendacoesPrincipioAtivoPorRecomendacao,
            Map<Long, List<RecomendacaoExcipiente>> recomendacoesExcipientePorRecomendacao,
            Map<Long, List<RecomendacaoEmbalagem>> recomendacoesEmbalagemPorRecomendacao,
            Map<Long, FeedbackRecomendacao> feedbackRecomendacaoPorRecomendacaoId,
            Map<Long, PrincipioAtivoFeedback> feedbackPrincipioAtivoPorRecomendacaoItemId,
            Map<Long, ExcipienteFeedback> feedbackExcipientePorRecomendacaoItemId,
            Map<Long, EmbalagemFeedback> feedbackEmbalagemPorRecomendacaoItemId
    ) {
        Recomendacao recomendacao = recomendacaoPorOrdemId.get(ordem.getId());
        if (recomendacao == null) {
            return new FeedbackStatusPorOrdem(0, 0, Collections.emptyList());
        }

        int totalEsperado = 1;
        int preenchidos = 0;
        List<Integer> notas = new ArrayList<>();

        FeedbackRecomendacao feedbackGeral = feedbackRecomendacaoPorRecomendacaoId.get(recomendacao.getId());
        if (feedbackGeral != null && feedbackGeral.getNota() != null) {
            preenchidos++;
            notas.add(feedbackGeral.getNota());
        }

        List<RecomendacaoPrincipioAtivo> recomendacoesPa = recomendacoesPrincipioAtivoPorRecomendacao
                .getOrDefault(recomendacao.getId(), Collections.emptyList());
        totalEsperado += recomendacoesPa.size();
        for (RecomendacaoPrincipioAtivo recomendacaoPa : recomendacoesPa) {
            PrincipioAtivoFeedback feedback = feedbackPrincipioAtivoPorRecomendacaoItemId.get(recomendacaoPa.getId());
            if (feedback != null && feedback.getNota() != null) {
                preenchidos++;
                notas.add(feedback.getNota());
            }
        }

        List<RecomendacaoExcipiente> recomendacoesExc = recomendacoesExcipientePorRecomendacao
                .getOrDefault(recomendacao.getId(), Collections.emptyList());
        totalEsperado += recomendacoesExc.size();
        for (RecomendacaoExcipiente recomendacaoExc : recomendacoesExc) {
            ExcipienteFeedback feedback = feedbackExcipientePorRecomendacaoItemId.get(recomendacaoExc.getId());
            if (feedback != null && feedback.getNota() != null) {
                preenchidos++;
                notas.add(feedback.getNota());
            }
        }

        List<RecomendacaoEmbalagem> recomendacoesEmb = recomendacoesEmbalagemPorRecomendacao
                .getOrDefault(recomendacao.getId(), Collections.emptyList());
        totalEsperado += recomendacoesEmb.size();
        for (RecomendacaoEmbalagem recomendacaoEmb : recomendacoesEmb) {
            EmbalagemFeedback feedback = feedbackEmbalagemPorRecomendacaoItemId.get(recomendacaoEmb.getId());
            if (feedback != null && feedback.getNota() != null) {
                preenchidos++;
                notas.add(feedback.getNota());
            }
        }

        return new FeedbackStatusPorOrdem(totalEsperado, preenchidos, notas);
    }

    private record FeedbackStatusPorOrdem(int totalEsperado, int preenchidos, List<Integer> notas) {}

    private enum FeedbackPreenchimentoFiltro {
        TOTAL,
        PARCIAL,
        NENHUM
    }

    private <T> Map<Long, List<T>> groupByOrdemId(Collection<T> itens) {
        Map<Long, List<T>> agrupado = new HashMap<>();
        for (T item : itens) {
            Long ordemId;
            if (item instanceof OrdemManipulacaoPrincipioAtivo pa) {
                ordemId = pa.getOrdemManipulacao().getId();
            } else if (item instanceof OrdemManipulacaoExcipiente excipiente) {
                ordemId = excipiente.getOrdemManipulacao().getId();
            } else if (item instanceof OrdemManipulacaoEmbalagem embalagem) {
                ordemId = embalagem.getOrdemManipulacao().getId();
            } else {
                continue;
            }
            agrupado.computeIfAbsent(ordemId, key -> new ArrayList<>()).add(item);
        }
        return agrupado;
    }

    private <T> Map<Long, List<T>> groupByRecomendacaoId(Collection<T> itens) {
        Map<Long, List<T>> agrupado = new HashMap<>();
        for (T item : itens) {
            Long recomendacaoId;
            if (item instanceof RecomendacaoPrincipioAtivo pa) {
                recomendacaoId = pa.getRecomendacao().getId();
            } else if (item instanceof RecomendacaoExcipiente excipiente) {
                recomendacaoId = excipiente.getRecomendacao().getId();
            } else if (item instanceof RecomendacaoEmbalagem embalagem) {
                recomendacaoId = embalagem.getRecomendacao().getId();
            } else {
                continue;
            }
            agrupado.computeIfAbsent(recomendacaoId, key -> new ArrayList<>()).add(item);
        }
        return agrupado;
    }

    private <T> Map<Integer, T> mapRecomendacaoPorOrdem(
            Recomendacao recomendacao,
            Map<Long, List<T>> recomendacoesPorRecomendacao,
            java.util.function.Function<T, Integer> ordemExtractor
    ) {
        if (recomendacao == null) {
            return Collections.emptyMap();
        }

        return recomendacoesPorRecomendacao
                .getOrDefault(recomendacao.getId(), Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ordemExtractor, item -> item, (existente, ignorar) -> existente));
    }

    private Double calcularMediaNotasGerais(List<FeedbackRecomendacao> feedbacksRecomendacao,
                                            List<PrincipioAtivoFeedback> feedbacksPrincipioAtivo,
                                            List<ExcipienteFeedback> feedbacksExcipiente,
                                            List<EmbalagemFeedback> feedbacksEmbalagem) {
        List<Integer> todasAsNotas = new ArrayList<>();
        todasAsNotas.addAll(feedbacksRecomendacao.stream().map(FeedbackRecomendacao::getNota).toList());
        todasAsNotas.addAll(feedbacksPrincipioAtivo.stream().map(PrincipioAtivoFeedback::getNota).toList());
        todasAsNotas.addAll(feedbacksExcipiente.stream().map(ExcipienteFeedback::getNota).toList());
        todasAsNotas.addAll(feedbacksEmbalagem.stream().map(EmbalagemFeedback::getNota).toList());
        return calcularMedia(todasAsNotas);
    }

    private Double calcularMedia(List<Integer> notas) {
        if (notas == null || notas.isEmpty()) {
            return null;
        }

        return notas.stream()
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    private class ResumoAcumulador {
        private final List<Integer> notas = new ArrayList<>();
        private int nota1;
        private int nota2;
        private int nota3;
        private int nota4;
        private int nota5;
        private int semResposta;
        private int total;

        void adicionar(Integer nota) {
            total++;
            if (nota == null) {
                semResposta++;
                return;
            }
            notas.add(nota);
            switch (nota) {
                case 1 -> nota1++;
                case 2 -> nota2++;
                case 3 -> nota3++;
                case 4 -> nota4++;
                case 5 -> nota5++;
                default -> {
                    // Ignora notas fora do intervalo esperado.
                }
            }
        }

        RelatorioResponseDTO.ResumoTipoDTO toDTO() {
            Double media = notas.isEmpty()
                    ? null
                    : notas.stream().mapToInt(Integer::intValue).average().orElse(0);
            return new RelatorioResponseDTO.ResumoTipoDTO(
                    media,
                    nota1,
                    nota2,
                    nota3,
                    nota4,
                    nota5,
                    semResposta,
                    total
            );
        }
    }
}
