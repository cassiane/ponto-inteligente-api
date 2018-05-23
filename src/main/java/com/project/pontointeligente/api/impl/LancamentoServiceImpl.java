package com.project.pontointeligente.api.impl;

import com.project.pontointeligente.api.controllers.ValidadorBloco;
import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import com.project.pontointeligente.api.enums.OperacaoEnum;
import com.project.pontointeligente.api.services.LancamentoService;
import com.project.pontointeligente.api.services.LancamentoServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class LancamentoServiceImpl  implements LancamentoService {

    public static Logger LOGGER = LoggerFactory.getLogger(LancamentoServiceImpl.class);
    private LancamentoServiceRepository lancamentoServiceRepository;

    @Autowired
    public LancamentoServiceImpl(LancamentoServiceRepository lancamentoServiceRepository) {
        this.lancamentoServiceRepository = lancamentoServiceRepository;
    }


    @Override
    public BindingResult validarFuncionario(LancamentoDto lancamentoDto, BindingResult result, Optional<Funcionario> funcionario) {
        return null;
    }

    @Override
    public BindingResult validarLancamento(Long lancamentoId) {
        List<Lancamento> lancamentos = buscarPreviousHash(lancamentoId);
        return null;
    }

    @Override
    public List<Lancamento> buscarPreviousHash(Long lancamentoId) {
        return lancamentoServiceRepository.buscarUltimosLancamentos(lancamentoId);
    }

    @Override
    public Lancamento persistirLancamento(Lancamento lancamento) {
        List<Lancamento> lancamentos = buscarPreviousHash(Long.valueOf(0));
        if (!CollectionUtils.isEmpty(lancamentos)) {
            LOGGER.info("Persistindo lançamento");
            if (isNull(lancamento.getId())) {
                LOGGER.info("Persistindo lançamento inclusão");
                lancamento.setAtivo(true);
                lancamento.setDataCriacao(Timestamp.valueOf(LocalDateTime.now()));
                lancamento.setHash(lancamento.calculateHash());
                lancamento.setPreviousHash(Objects.requireNonNull(lancamentos.stream()
                        .reduce((first, second) -> second)
                        .orElse(null)).getHash());
            }
//d060900c0d5cdfa5d902d8806a52c68f8c7855174d2fb3f0ef035bcafa796fd0
            lancamentos.add(lancamento);
            if (ValidadorBloco.isChainValid(lancamentos)) {
                LOGGER.info("Bloco validado");
                return persistirLancamentoComLog(lancamento);
            }
        }

        return null;
    }

    @Override
    public Optional<Lancamento> buscarLancamentoPorId(Long id) {
        return this.lancamentoServiceRepository.buscarPorId(id);
    }

    @Override
    public void remover(Lancamento lancamento) {
        this.lancamentoServiceRepository.remover(lancamento);
    }

    private Lancamento persistirLancamentoComLog(Lancamento lancamento) {
        lancamento = this.lancamentoServiceRepository.persistir(lancamento);
        LancamentoLog log = new LancamentoLog(lancamento, OperacaoEnum.INCLUSAO);
        this.lancamentoServiceRepository.persistir(log);
        return lancamento;
    }
}
