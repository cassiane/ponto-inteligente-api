package com.project.pontointeligente.api.report;

public interface ReportService {

    byte[] imprimirEspelhoPontoAnaliticoPorFuncionario(Long idFuncionario);

    byte[] imprimirEspelhoPontoAnaliticoPorCentroCusto(int centroCusto);

    byte[] imprimirEspelhoPontoResumoPorFuncionario(Long idFuncionario);

    byte[] imprimirEspelhoPontoResumoPorCentroCusto(int centroCusto);

    byte[] imprimirComprovantePonto(Long idLancamento);
}
