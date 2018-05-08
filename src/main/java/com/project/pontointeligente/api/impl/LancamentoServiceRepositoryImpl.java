package com.project.pontointeligente.api.impl;

import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import com.project.pontointeligente.api.repositories.LancamentoLogRepository;
import com.project.pontointeligente.api.repositories.LancamentoRepository;
import com.project.pontointeligente.api.services.LancamentoServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LancamentoServiceRepositoryImpl implements LancamentoServiceRepository {

    public static Logger LOGGER = LoggerFactory.getLogger(LancamentoServiceRepositoryImpl.class);

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
    private LancamentoLogRepository lancamentoLogRepository;

	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
        LOGGER.info("Buscar lançamentos pelo id do funcionário: {}", funcionarioId);
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@CachePut("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		LOGGER.info("Salvando lançamento: {}", lancamento);
	    return this.lancamentoRepository.save(lancamento);
	}

    @Override
    public LancamentoLog persistir(LancamentoLog lancamentoLog) {
        LOGGER.info("Salvando log do lançamento: {}", lancamentoLog);
	    return lancamentoLogRepository.save(lancamentoLog);
    }

    @Override
    public void remover(Long id) {
        LOGGER.info("Excluindo o lançamento de id: {}", id);
	    lancamentoRepository.delete(id);
    }

    @Override
    public List<Lancamento> buscarUltimosLancamentos() {
        LOGGER.info("Buscando últimos 25 lançamentos");
	    return lancamentoRepository.findTop25();
    }

    @Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		LOGGER.info("Buscando o lançamento de id: {}", id);
	    return Optional.ofNullable(this.lancamentoRepository.findOne(id));
	}


}
