package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.RecomendacaoExcipiente;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacaoExcipienteRepository extends JpaRepository<RecomendacaoExcipiente, Long> {
	List<RecomendacaoExcipiente> findByRecomendacaoIdInOrderByRecomendacaoIdAscOrdemAsc(Collection<Long> recomendacaoIds);
}

