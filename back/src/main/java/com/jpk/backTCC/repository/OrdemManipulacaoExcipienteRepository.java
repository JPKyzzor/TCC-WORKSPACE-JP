package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.OrdemManipulacaoExcipiente;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemManipulacaoExcipienteRepository extends JpaRepository<OrdemManipulacaoExcipiente, Long> {
	List<OrdemManipulacaoExcipiente> findByOrdemManipulacaoIdInOrderByOrdemManipulacaoIdAscOrdemAsc(Collection<Long> ordemManipulacaoIds);
}

