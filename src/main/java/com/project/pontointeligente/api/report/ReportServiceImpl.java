package com.project.pontointeligente.api.report;

import com.google.common.collect.Lists;
import com.project.pontointeligente.api.lancamento.Lancamento;
import com.project.pontointeligente.api.lancamento.LancamentoService;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportServiceImpl implements ReportService {

    private final JasperReportBuilder jasperReportBuilder;
    private LancamentoService lancamentoService;

    @Autowired
    public ReportServiceImpl(JasperReportBuilder jasperReportBuilder, LancamentoService lancamentoService) {
        this.jasperReportBuilder = jasperReportBuilder;
        this.lancamentoService = lancamentoService;
    }

    @Override
    public byte[] imprimirEspelhoPontoAnaliticoPorFuncionario(Long idFuncionario) {
        List<Lancamento> lancamentos = lancamentoService.buscarLancamentosCompetenciaAtualPorFuncionarioId(idFuncionario);
        return jasperReportBuilder.build("pontoeletronicofuncionario", new HashMap<>(), setCampos(lancamentos));
    }

    @Override
    public byte[] imprimirEspelhoPontoAnaliticoPorCentroCusto(int centroCusto) {
        return new byte[0];
    }

    @Override
    public byte[] imprimirEspelhoPontoResumoPorFuncionario(Long idFuncionario) {
        return new byte[0];
    }

    @Override
    public byte[] imprimirEspelhoPontoResumoPorCentroCusto(int centroCusto) {
        return new byte[0];
    }

    @Override
    public byte[] imprimirComprovantePonto(Long idLancamento) {
        return new byte[0];
    }

    private JRMapCollectionDataSource setCampos(List<Lancamento> lancamentos) {
        List<Map<String, ?>> details = Lists.newArrayList();
        Map<String, Object> detailTitle = new HashMap<>();
        detailTitle.put("nmFuncionario", lancamentos.stream().findFirst().get().getFuncionario().getNome());
        detailTitle.put("competencia", getCompetencia(lancamentos));
        details.add(detailTitle);

        for (int i=1; i<lancamentos.size();i=i+2) {
            Map<String, Object> detail = new HashMap<>();
            Timestamp hora = Timestamp.valueOf(lancamentos.get(i-1).getData().toLocalDateTime());
            Timestamp hora2 = Timestamp.valueOf(lancamentos.get(i).getData().toLocalDateTime());
            detail.put("centroCusto", lancamentos.get(i).getCentroCusto().getCentroCusto());
            detail.put("entrada", hora);
            detail.put("saida", hora2);

            LocalTime tempDateTime = LocalTime.from(hora.toLocalDateTime().toLocalTime());
            Long hours = tempDateTime.until( hora2.toLocalDateTime(), ChronoUnit.HOURS);
            tempDateTime = tempDateTime.plusHours( hours );

            Long minutes = tempDateTime.until( hora2.toLocalDateTime(), ChronoUnit.MINUTES);
            tempDateTime = tempDateTime.plusMinutes( minutes );

            Long seconds = tempDateTime.until( hora2.toLocalDateTime(), ChronoUnit.SECONDS);
            LocalTime fim = LocalTime.of(hours.intValue(), minutes.intValue(), seconds.intValue());
            detail.put("total", String.valueOf(fim));

            details.add(detail);
        }

        return new JRMapCollectionDataSource(details);
    }

    private String getCompetencia(List<Lancamento> lancamentos) {
        LocalDateTime competencia = lancamentos.stream().findFirst().get().getData().toLocalDateTime();
        return competencia.getYear() + "/" + competencia.getMonthValue();
    }
}
