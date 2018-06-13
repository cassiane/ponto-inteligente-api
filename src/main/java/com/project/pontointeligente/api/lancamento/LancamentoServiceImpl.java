package com.project.pontointeligente.api.lancamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class LancamentoServiceImpl  implements LancamentoService {

    public static Logger LOGGER = LoggerFactory.getLogger(LancamentoServiceImpl.class);
    private LancamentoRepository lancamentoRepository;
    private LancamentoLogRepository lancamentoLogRepository;

    @Autowired
    public LancamentoServiceImpl(LancamentoRepository lancamentoRepository, LancamentoLogRepository lancamentoLogRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.lancamentoLogRepository = lancamentoLogRepository;
    }

    @Override
    public List<Lancamento> buscarPreviousHash(Long lancamentoId) {
        LOGGER.info("Buscando últimos 25 lançamentos");
        return lancamentoRepository.findTop25ByOptionalId(lancamentoId);
    }

    @Override
    public Lancamento persistirLancamento(Lancamento lancamento) {
        Optional<Lancamento> lancamentoOptional = Optional.empty();
        Long id = Long.valueOf(0);
        OperacaoEnum operacaoEnum = OperacaoEnum.INCLUSAO;
        if (nonNull(lancamento.getId())) {
            lancamentoOptional = buscarLancamentoPorId(lancamento.getId());
            if (!lancamentoOptional.isPresent()) {
                return null;
            }
            id = lancamentoOptional.get().getId();
            operacaoEnum = OperacaoEnum.ALTERACAO;
        }

        List<Lancamento> lancamentos = buscarPreviousHash(id);
        if (!CollectionUtils.isEmpty(lancamentos)) {
            LOGGER.info("Persistindo lançamento");

            if (!lancamentoOptional.isPresent()) {
                LOGGER.info("Persistindo lançamento inclusão");
                lancamento.setAtivo(true);
                lancamento.setDataCriacao(Timestamp.valueOf(LocalDateTime.now()));
                lancamento.setHash(lancamento.calculateHash());
                lancamento.setPreviousHash(Objects.requireNonNull(lancamentos.stream()
                        .reduce((first, second) -> second)
                        .orElse(null)).getHash());
            }
            lancamentos.add(lancamento);
            if (ValidadorBloco.isChainValid(lancamentos)) {
                LOGGER.info("Bloco validado");
                return persistirLancamentoComLog(lancamento, operacaoEnum);
            }
        }

        return null;
    }

    @Override
    @Cacheable("lancamentoPorId")
    public Optional<Lancamento> buscarLancamentoPorId(Long id) {
        LOGGER.info("Buscando o lançamento de id: {}", id);
        return Optional.ofNullable(this.lancamentoRepository.findOne(id));
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
    public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
        LOGGER.info("Buscar lançamentos pelo id do funcionário: {}", funcionarioId);
        return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
    }

    @CachePut("lancamentoPorId")
    private Lancamento persistirLancamentoComLog(Lancamento lancamento, OperacaoEnum operacaoEnum) {
        lancamento = this.lancamentoRepository.save(lancamento);
        LancamentoLog log = new LancamentoLog(lancamento, operacaoEnum);
        lancamentoLogRepository.save(log);
        return lancamento;
    }

    @Override
    public List<LancamentoLog> buscarLogsPorIdLancamentoAlterado(Long idLancamentoAlterado) {
        LOGGER.info("Buscando o log do lancamento de id: {}", idLancamentoAlterado);
        return this.lancamentoLogRepository.findByIdLancamentoAlterado(idLancamentoAlterado);
    }

    @Override
    public List<Lancamento> buscarLancamentosCompetenciaAtualPorFuncionarioId(Long idFuncionario) {
        LOGGER.info("Buscar lançamentos pelo id do funcionário: {}", idFuncionario);
        LocalDateTime dataInicial = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime dataFinal = LocalDateTime.of(LocalDate.now().withDayOfMonth(
                LocalDate.now().lengthOfMonth()),
                LocalTime.MAX);

        return this.lancamentoRepository.findCompetenciaAtualByFuncionarioId(idFuncionario,
                Timestamp.valueOf(dataInicial),
                Timestamp.valueOf(dataFinal));
    }
}
