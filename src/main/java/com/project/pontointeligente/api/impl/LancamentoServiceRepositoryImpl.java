package com.project.pontointeligente.api.impl;

import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import com.project.pontointeligente.api.enums.OperacaoEnum;
import com.project.pontointeligente.api.repositories.LancamentoLogRepository;
import com.project.pontointeligente.api.repositories.LancamentoRepository;
import com.project.pontointeligente.api.services.LancamentoServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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

	private LancamentoRepository lancamentoRepository;
	private LancamentoLogRepository lancamentoLogRepository;

	@Autowired
	public LancamentoServiceRepositoryImpl(LancamentoRepository lancamentoRepository, LancamentoLogRepository lancamentoLogRepository) {
		this.lancamentoRepository = lancamentoRepository;
		this.lancamentoLogRepository = lancamentoLogRepository;
	}

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
	@CacheEvict("lancamentoPorId")
    public void remover(Lancamento lancamento) {
        LOGGER.info("Excluindo o lançamento de id: {}", lancamento.getId());
        lancamento.setAtivo(false);
	    Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
	    LancamentoLog log = new LancamentoLog(lancamentoSalvo, OperacaoEnum.EXCLUSAO);
		lancamentoLogRepository.save(log);
    }

	@Override
    public List<Lancamento> buscarUltimosLancamentos(Long lancamentoId) {
        LOGGER.info("Buscando últimos 25 lançamentos");
	    return lancamentoRepository.findTop25ByOptionalId(lancamentoId);
    }

    @Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		LOGGER.info("Buscando o lançamento de id: {}", id);
	    return Optional.ofNullable(this.lancamentoRepository.findOne(id));
	}


}
