package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.OrdemManipulacaoPrincipioAtivo;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemManipulacaoPrincipioAtivoRepository extends JpaRepository<OrdemManipulacaoPrincipioAtivo, Long> {
	List<OrdemManipulacaoPrincipioAtivo> findByOrdemManipulacaoIdInOrderByOrdemManipulacaoIdAscOrdemAsc(Collection<Long> ordemManipulacaoIds);
}

