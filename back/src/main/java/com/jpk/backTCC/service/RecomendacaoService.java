package com.jpk.backTCC.service;

import com.jpk.backTCC.entity.Recomendacao;
import com.jpk.backTCC.entity.RecomendacaoEmbalagem;
import com.jpk.backTCC.entity.RecomendacaoExcipiente;
import com.jpk.backTCC.entity.RecomendacaoPrincipioAtivo;
import com.jpk.backTCC.repository.RecomendacaoEmbalagemRepository;
import com.jpk.backTCC.repository.RecomendacaoExcipienteRepository;
import com.jpk.backTCC.repository.RecomendacaoPrincipioAtivoRepository;
import com.jpk.backTCC.repository.RecomendacaoRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecomendacaoService {

    private final RecomendacaoRepository recomendacaoRepository;
    private final RecomendacaoEmbalagemRepository embalagemRepository;
    private final RecomendacaoExcipienteRepository excipienteRepository;
    private final RecomendacaoPrincipioAtivoRepository principioAtivoRepository;

    public RecomendacaoService(RecomendacaoRepository recomendacaoRepository,
                               RecomendacaoEmbalagemRepository embalagemRepository,
                               RecomendacaoExcipienteRepository excipienteRepository,
                               RecomendacaoPrincipioAtivoRepository principioAtivoRepository) {
        this.recomendacaoRepository = recomendacaoRepository;
        this.embalagemRepository = embalagemRepository;
        this.excipienteRepository = excipienteRepository;
        this.principioAtivoRepository = principioAtivoRepository;
    }

    @Transactional
    public Recomendacao criarRecomendacao(Recomendacao recomendacao) {

        List<RecomendacaoExcipiente> excipientes = new ArrayList<>(recomendacao.getRecomendacoesExcipientes());
        List<RecomendacaoPrincipioAtivo> principiosAtivos = new ArrayList<>(recomendacao.getRecomendacoesPrincipiosAtivos());
        List<RecomendacaoEmbalagem> embalagens = new ArrayList<>(recomendacao.getRecomendacoesEmbalagens());

        recomendacao.getRecomendacoesExcipientes().clear();
        recomendacao.getRecomendacoesPrincipiosAtivos().clear();
        recomendacao.getRecomendacoesEmbalagens().clear();
        Recomendacao salva = recomendacaoRepository.save(recomendacao);

        if (!excipientes.isEmpty()) {
            for (int i = 0; i < excipientes.size(); i++) {
                RecomendacaoExcipiente e = excipientes.get(i);
                e.setOrdem(i + 1);
                e.setRecomendacao(salva);
            }
            excipienteRepository.saveAll(excipientes);
        }

        if (!principiosAtivos.isEmpty()) {
            for (int i = 0; i < principiosAtivos.size(); i++) {
                RecomendacaoPrincipioAtivo p = principiosAtivos.get(i);
                p.setOrdem(i + 1);
                p.setRecomendacao(salva);
            }
            principioAtivoRepository.saveAll(principiosAtivos);
        }

        if (!embalagens.isEmpty()) {
            for (int i = 0; i < embalagens.size(); i++) {
                RecomendacaoEmbalagem e = embalagens.get(i);
                e.setOrdem(i + 1);
                e.setRecomendacao(salva);
            }
            embalagemRepository.saveAll(embalagens);
        }

        salva.setRecomendacoesExcipientes(excipientes);
        salva.setRecomendacoesPrincipiosAtivos(principiosAtivos);
        salva.setRecomendacoesEmbalagens(embalagens);
        return salva;
    }
}
