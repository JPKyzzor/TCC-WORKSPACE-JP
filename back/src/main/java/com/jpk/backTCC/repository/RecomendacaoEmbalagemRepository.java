package com.jpk.backTCC.repository;

import com.jpk.backTCC.entity.RecomendacaoEmbalagem;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacaoEmbalagemRepository extends JpaRepository<RecomendacaoEmbalagem, Long> {
	List<RecomendacaoEmbalagem> findByRecomendacaoIdInOrderByRecomendacaoIdAscOrdemAsc(Collection<Long> recomendacaoIds);
}
