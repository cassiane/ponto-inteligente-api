package com.project.pontointeligente.api.converter;

import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.entities.Lancamento;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConverterLancamentoParaLancamentoDto implements Converter<Lancamento, LancamentoDto> {

    @Override
    public LancamentoDto convert(Lancamento lancamento) {
        LancamentoDto lancamentoDto = new LancamentoDto();
        lancamentoDto.setId(Optional.of(lancamento.getId()));
        lancamentoDto.setData(lancamento.getData().toLocalDateTime());
        lancamentoDto.setTipo(lancamento.getTipo().toString());
        lancamentoDto.setDescricao(lancamento.getDescricao());
        lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());
        lancamentoDto.setHash(lancamento.getHash());

        return lancamentoDto;
    }
}
