package com.project.pontointeligente.api.services;

import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface LancamentoLogServiceRepository {
	
	List<LancamentoLog> buscarPorIdLancamentoAlterado(Long lancamentoId);
	
}
