package com.project.pontointeligente.api.lancamento;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConverterLancamentoLogParaLancamentoLogDto implements Converter<LancamentoLog, LancamentoLogDto> {

    @Override
    public LancamentoLogDto convert(LancamentoLog lancamentoLog) {
        LancamentoLogDto lancamentoLogDto = new LancamentoLogDto();
        lancamentoLogDto.setId(Optional.of(lancamentoLog.getId()));
        lancamentoLogDto.setData(lancamentoLog.getData().toLocalDateTime());
        lancamentoLogDto.setDataCriacao(lancamentoLog.getDataCriacao() != null ? lancamentoLog.getDataCriacao().toLocalDateTime() : null);
        lancamentoLogDto.setDataAtualizacao(lancamentoLog.getDataAtualizacao() != null ? lancamentoLog.getDataAtualizacao().toLocalDateTime() : null);
        lancamentoLogDto.setTipo(lancamentoLog.getTipo().toString());
        lancamentoLogDto.setDescricao(lancamentoLog.getDescricao());
        lancamentoLogDto.setFuncionarioId(lancamentoLog.getFuncionario().getId());
        lancamentoLogDto.setHash(lancamentoLog.getHash());
        lancamentoLogDto.setOperacao(lancamentoLog.getOperacao().toString());

        return lancamentoLogDto;
    }
}
