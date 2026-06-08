package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.OrdemManipulacaoEmbalagem;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemManipulacaoEmbalagemRepository extends JpaRepository<OrdemManipulacaoEmbalagem, Long> {
	List<OrdemManipulacaoEmbalagem> findByOrdemManipulacaoIdInOrderByOrdemManipulacaoIdAscOrdemAsc(Collection<Long> ordemManipulacaoIds);
}
