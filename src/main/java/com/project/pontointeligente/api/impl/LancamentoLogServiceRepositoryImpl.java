package com.project.pontointeligente.api.impl;

import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import com.project.pontointeligente.api.enums.OperacaoEnum;
import com.project.pontointeligente.api.repositories.LancamentoLogRepository;
import com.project.pontointeligente.api.repositories.LancamentoRepository;
import com.project.pontointeligente.api.services.LancamentoLogServiceRepository;
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
public class LancamentoLogServiceRepositoryImpl implements LancamentoLogServiceRepository {

    public static Logger LOGGER = LoggerFactory.getLogger(LancamentoLogServiceRepositoryImpl.class);

	@Autowired
    private LancamentoLogRepository lancamentoLogRepository;

    @Cacheable("lancamentoPorId")
	public Page<LancamentoLog> buscarPorIdLancamentoAlterado(Long idLancamentoAlterado, PageRequest pageRequest) {
		LOGGER.info("Buscando o log do lancamento de id: {}", idLancamentoAlterado);
	    return this.lancamentoLogRepository.findByIdLancamentoAlterado(idLancamentoAlterado, pageRequest);
	}


}
