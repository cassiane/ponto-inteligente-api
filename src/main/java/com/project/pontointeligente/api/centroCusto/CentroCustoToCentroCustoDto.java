package com.project.pontointeligente.api.centroCusto;

import com.project.pontointeligente.api.empresa.Empresa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CentroCustoToCentroCustoDto implements Converter<CentroCusto, CentroCustoDto> {

    @Override
    public CentroCustoDto convert(CentroCusto centroCusto) {
        CentroCustoDto centroCustoDto = new CentroCustoDto();
        Empresa empresa = centroCusto.getEmpresa();
        empresa.setFuncionarios(null);
        empresa.setCentroCusto(null);
        centroCustoDto.setEmpresa(empresa);
        centroCustoDto.setDescricao(centroCusto.getDescricao());
        centroCustoDto.setProjeto(centroCusto.getProjeto());
        centroCustoDto.setId(centroCusto.getId());
        centroCustoDto.setCentroCusto(centroCusto.getCentroCusto());

        return centroCustoDto;
    }
}
