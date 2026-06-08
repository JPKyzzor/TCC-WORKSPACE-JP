package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.FeedbackRecomendacao;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRecomendacaoRepository extends JpaRepository<FeedbackRecomendacao, Long> {
    Optional<FeedbackRecomendacao> findByRecomendacaoId(Long recomendacaoId);
    List<FeedbackRecomendacao> findByRecomendacaoIdIn(Collection<Long> recomendacaoIds);
}

