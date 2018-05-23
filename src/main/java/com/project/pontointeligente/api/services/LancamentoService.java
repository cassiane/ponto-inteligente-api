package com.project.pontointeligente.api.services;

import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.entities.Lancamento;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    BindingResult validarFuncionario(LancamentoDto lancamentoDto, BindingResult result, Optional<Funcionario> funcionario);

    BindingResult validarLancamento(Long lancamentoId);

    List<Lancamento> buscarPreviousHash(Long lancamentoId);

    Lancamento persistirLancamento(Lancamento lancamento);

    Optional<Lancamento> buscarLancamentoPorId(Long id);

    void remover(Lancamento lancamento);
}
