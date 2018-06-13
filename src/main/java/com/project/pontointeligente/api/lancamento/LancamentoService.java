package com.project.pontointeligente.api.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    List<Lancamento> buscarPreviousHash(Long lancamentoId);

    Lancamento persistirLancamento(Lancamento lancamento);

    Optional<Lancamento> buscarLancamentoPorId(Long id);

    void remover(Lancamento lancamento);

    Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);

    List<LancamentoLog> buscarLogsPorIdLancamentoAlterado(Long idLancamentoAlterado);

    List<Lancamento> buscarLancamentosCompetenciaAtualPorFuncionarioId(Long idFuncionario);
}
