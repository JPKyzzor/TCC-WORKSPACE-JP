package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.Recomendacao;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {
	List<Recomendacao> findByOrdemManipulacaoIdIn(Collection<Long> ordemManipulacaoIds);
}

