package com.project.pontointeligente.api.centroCusto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CentroCustoDtoToCentroCusto implements Converter<CentroCustoDto, CentroCusto> {

    @Override
    public CentroCusto convert(CentroCustoDto centroCustoDto) {
        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setEmpresa(centroCustoDto.getEmpresa());
        centroCusto.setDescricao(centroCustoDto.getDescricao());
        centroCusto.setProjeto(centroCustoDto.getProjeto());
        centroCusto.setId(centroCustoDto.getId());
        centroCusto.setCentroCusto(centroCustoDto.getCentroCusto());
        return centroCusto;
    }
}
