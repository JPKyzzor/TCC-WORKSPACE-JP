package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.ExcipienteFeedback;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcipienteFeedbackRepository extends JpaRepository<ExcipienteFeedback, Long> {
    Optional<ExcipienteFeedback> findByRecomendacaoExcipienteId(Long recomendacaoExcipienteId);
    List<ExcipienteFeedback> findByRecomendacaoExcipienteIdIn(Collection<Long> recomendacaoExcipienteIds);
}
