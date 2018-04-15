package com.project.pontointeligente.api.repositories;

import com.project.pontointeligente.api.entities.LancamentoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface LancamentoLogRepository extends JpaRepository<LancamentoLog, Long> {
}
