package com.project.pontointeligente.api.centroCusto;

import com.project.pontointeligente.api.centroCusto.CentroCusto;

import java.util.List;

public interface CentroCustoService {

    CentroCusto persistir(CentroCusto centroCusto);
    void excluir(CentroCusto centroCusto);
    List<CentroCusto> listar();
    CentroCusto buscarPorId(Long id);
}
