package com.project.pontointeligente.api.impl;

import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.repositories.LancamentoRepository;
import com.project.pontointeligente.api.services.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@CachePut("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		return this.lancamentoRepository.save(lancamento);
	}

    @Override
    public List<Lancamento> buscarUltimosLancamentos() {
        return lancamentoRepository.findTop25();
    }

    @Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		return Optional.ofNullable(this.lancamentoRepository.findOne(id));
	}


}
