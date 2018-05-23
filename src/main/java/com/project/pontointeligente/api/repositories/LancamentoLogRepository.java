package com.project.pontointeligente.api.repositories;

import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Transactional(readOnly = true)
@NamedQueries({
        @NamedQuery(name = "LancamentoLogRepository.findByIdLancamentoAlterado", query = "SELECT lanc FROM lancamentoLog lanc WHERE lan.idLancamentoAlterado = :idLancamentoAlterado"),
})
public interface LancamentoLogRepository extends JpaRepository<LancamentoLog, Long> {

    Page<LancamentoLog> findByIdLancamentoAlterado(@Param("idLancamentoAlterado") Long idLancamentoAlterado, Pageable pageable);

}
