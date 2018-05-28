package com.project.pontointeligente.api.lancamento;

import com.project.pontointeligente.api.lancamento.LancamentoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

@Transactional(readOnly = true)
@NamedQueries({
        @NamedQuery(name = "LancamentoLogRepository.findByIdLancamentoAlterado", query = "SELECT lanc FROM lancamentoLog lanc WHERE lan.idLancamentoAlterado = :idLancamentoAlterado"),
})
public interface LancamentoLogRepository extends JpaRepository<LancamentoLog, Long> {

    List<LancamentoLog> findByIdLancamentoAlterado(@Param("idLancamentoAlterado") Long idLancamentoAlterado);

}
