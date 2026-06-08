package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.RecomendacaoPrincipioAtivo;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacaoPrincipioAtivoRepository extends JpaRepository<RecomendacaoPrincipioAtivo, Long> {
	List<RecomendacaoPrincipioAtivo> findByRecomendacaoIdInOrderByRecomendacaoIdAscOrdemAsc(Collection<Long> recomendacaoIds);
}

