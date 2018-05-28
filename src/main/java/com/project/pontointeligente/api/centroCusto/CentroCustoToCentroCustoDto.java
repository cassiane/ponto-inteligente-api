package com.project.pontointeligente.api.centroCusto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CentroCustoToCentroCustoDto implements Converter<CentroCusto, CentroCustoDto> {

    @Override
    public CentroCustoDto convert(CentroCusto centroCusto) {
        CentroCustoDto centroCustoDto = new CentroCustoDto();
        centroCustoDto.setEmpresa(centroCusto.getEmpresa());
        centroCustoDto.setDescricao(centroCusto.getDescricao());
        centroCustoDto.setProjeto(centroCusto.getProjeto());
        centroCustoDto.setId(centroCusto.getId());
        centroCustoDto.setCentroCusto(centroCusto.getCentroCusto());

        return centroCustoDto;
    }
}
