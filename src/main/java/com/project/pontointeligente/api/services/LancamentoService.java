package com.project.pontointeligente.api.services;

import com.project.pontointeligente.api.entities.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {
	
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	Optional<Lancamento> buscarPorId(Long id);
	
	Lancamento persistir(Lancamento lancamento);

	//void remover(Long id);

	List<Lancamento> buscarUltimosLancamentos();
}
