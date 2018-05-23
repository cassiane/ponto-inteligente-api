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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class LancamentoServiceImpl  implements LancamentoService {

    public static Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);
    private LancamentoServiceRepository lancamentoServiceRepository;

    @Value(value = "${jwt.secred}")
    private String senhaAssinatura;

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
        if (isNull(lancamento.getId())) {
            List<Lancamento> lancamentos = buscarPreviousHash(Long.valueOf(0));
            if (!CollectionUtils.isEmpty(lancamentos)) {
                lancamento.setPreviousHash(Objects.requireNonNull(lancamentos.stream()
                        .reduce((first, second) -> second)
                        .orElse(null)).getHash());
                lancamento.setAtivo(true);
                lancamento.setDataCriacao(Timestamp.valueOf(LocalDateTime.now()));
                lancamento.setHash(lancamento.calculateHash(senhaAssinatura));

                lancamentos.add(lancamento);
                if (ValidadorBloco.isChainValid(lancamentos, senhaAssinatura)) {
                    persistirLancamentoComLog(lancamento);
                }
            }
        }

        return persistirLancamentoComLog(lancamento);
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
