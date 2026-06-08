package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.PrincipioAtivoFeedback;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrincipioAtivoFeedbackRepository extends JpaRepository<PrincipioAtivoFeedback, Long> {
    Optional<PrincipioAtivoFeedback> findByRecomendacaoPrincipioAtivoId(Long recomendacaoPrincipioAtivoId);
    List<PrincipioAtivoFeedback> findByRecomendacaoPrincipioAtivoIdIn(Collection<Long> recomendacaoPrincipioAtivoIds);
}
