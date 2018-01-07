package com.project.pontointeligente.api.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.repositories.LancamentoRepository;
import com.project.pontointeligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	
	@Override
	public Optional<Lancamento> buscarPorFuncionarioId(Long funcionarioId) {
		return Optional.ofNullable(this.lancamentoRepository.findOne(funcionarioId));
	}

	@Override
	public Page<Lancamento> buscarPorFuncionarioIdPaginado(Long funcionarioId, Pageable pageable) {
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageable);
	}

	@Override
	public Lancamento persistir(Lancamento lancamento) {
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	public void Remover(Long id) {
		this.lancamentoRepository.delete(id);
	}
	
}
