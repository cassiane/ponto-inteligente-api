package com.project.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.pontointeligente.api.entities.Lancamento;

public interface LancamentoService {
	
    Optional<Lancamento> buscarPorFuncionarioId(Long funcionarioId);
	
	Page<Lancamento> buscarPorFuncionarioIdPaginado(Long funcionarioId, Pageable pageable);
	
	Lancamento persistir(Lancamento lancamento);
	
	void Remover(Long id);
}
