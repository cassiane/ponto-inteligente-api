package com.project.pontointeligente.api.report;

import com.google.common.collect.Lists;
import com.project.pontointeligente.api.lancamento.Lancamento;
import com.project.pontointeligente.api.lancamento.LancamentoService;
import com.project.pontointeligente.api.lancamento.TipoEnum;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportService {

    private final JasperReportBuilder jasperReportBuilder;
    private LancamentoService lancamentoService;

    @Autowired
    public ReportService(JasperReportBuilder jasperReportBuilder, LancamentoService lancamentoService) {
        this.jasperReportBuilder = jasperReportBuilder;
        this.lancamentoService = lancamentoService;
    }

    public byte[] print() {
        return jasperReportBuilder.build("exemplo", new HashMap<>(), getDetail(null));
    }

    public byte[] exibirFolhaPontoCompetenciaAtualPorFuncionario(Long idFuncionario) {
        List<Lancamento> lancamentos = lancamentoService.buscarLancamentosCompetenciaAtualPorFuncionarioId(idFuncionario);
        return jasperReportBuilder.build("pontoeletronicofuncionario", new HashMap<>(), getDetail(lancamentos));
    }

    private JRMapCollectionDataSource getDetail(List<Lancamento> lancamentos) {
        List<Map<String, ?>> details = Lists.newArrayList();
        Map<String, Object> detailTitle = new HashMap<>();
        detailTitle.put("nmFuncionario", lancamentos.stream().findFirst().get().getFuncionario().getNome());
        detailTitle.put("competencia", getCompetencia(lancamentos));
        details.add(detailTitle);

        for (Lancamento lancamento : lancamentos) {
            Map<String, Object> detail = new HashMap<>();
            Timestamp hora = Timestamp.valueOf(lancamento.getData().toLocalDateTime());

            if (isEntrada(lancamento.getTipo())) {
                detail.put("entrada", hora);
            } else {
                detail.put("saida", hora);
            }
            detail.put("centroCusto", lancamento.getCentroCusto().getCentroCusto());
            details.add(detail);
        }

        return new JRMapCollectionDataSource(details);
    }

    private String getCompetencia(List<Lancamento> lancamentos) {
        LocalDateTime competencia = lancamentos.stream().findFirst().get().getData().toLocalDateTime();
        return competencia.getYear() + "/" + competencia.getMonthValue();
    }

    private boolean isEntrada(TipoEnum tpLlancamento) {
        return TipoEnum.INICIO_TRABALHO.equals(tpLlancamento) || TipoEnum.TERMINO_ALMOCO.equals(tpLlancamento);
    }

}
