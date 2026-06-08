package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.EmbalagemFeedback;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbalagemFeedbackRepository extends JpaRepository<EmbalagemFeedback, Long> {
    Optional<EmbalagemFeedback> findByRecomendacaoEmbalagemId(Long recomendacaoEmbalagemId);
    List<EmbalagemFeedback> findByRecomendacaoEmbalagemIdIn(Collection<Long> recomendacaoEmbalagemIds);
}
