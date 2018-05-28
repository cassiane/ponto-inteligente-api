package com.project.pontointeligente.api.lancamento;

import com.project.pontointeligente.api.lancamento.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "LancamentoRepository.findByFuncionarioId", query = "SELECT lanc FROM lancamento lanc WHERE lan.funcionario.id = :funcionarioId"),
})
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	List<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);
	
	//Lançamento paginados
	Page<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM lancamento lanc where :lancamentoId = 0 or id < :lancamentoId LIMIT 0,25 ")
    List<Lancamento> findTop25ByOptionalId(@Param("lancamentoId") Long lancamentoId);
}
