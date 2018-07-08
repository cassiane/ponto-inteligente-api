package com.project.pontointeligente.api.centroCusto;

import java.util.List;
import java.util.Optional;

public interface CentroCustoService {

    CentroCusto persistir(CentroCusto centroCusto);

    void excluir(Long centroCusto) throws Exception;

    List<CentroCustoDto> listar();

    Optional<CentroCustoDto> buscarPorId(Long id);
}
