package com.project.pontointeligente.api.lancamento;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.sql.Timestamp;
import java.util.Optional;

@Component
public class ConverterLancamentoDtoParaLancamento {

    public Lancamento convert(LancamentoDto lancamentoDto, BindingResult result, Optional<Lancamento> lanc)  {
        Lancamento lancamento = new Lancamento();
        if (lancamentoDto.getId().isPresent()) {
            if (lanc.isPresent()) {
                lancamento = lanc.get();
            } else {
                result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
            }
        }

        lancamento.setDescricao(lancamentoDto.getDescricao());
        lancamento.setData(Timestamp.valueOf(lancamentoDto.getData()));

        if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
            lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
        } else {
            result.addError(new ObjectError("tipo", "Tipo inválido."));
        }

        return lancamento;
    }
}
