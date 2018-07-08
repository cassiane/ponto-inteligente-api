package com.project.pontointeligente.api.report;

import com.google.common.collect.Lists;
import com.project.pontointeligente.api.lancamento.Lancamento;
import com.project.pontointeligente.api.lancamento.LancamentoService;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ReportServiceImpl implements ReportService {

    public static final String ESPELHO_PONTO_RESUMO_POR_FUNCIONARIO = "espelhoPontoResumoPorFuncionario";
    public static final String ESPELHO_PONTO_ANALITICO_POR_FUNCIONARIO = "espelhoPontoAnaliticoPorFuncionario";
    public static final String ESPELHO_PONTO_ANALITICO_POR_CENTRO_CUSTO = "espelhoPontoAnaliticoPorCentroCusto";
    public static final String ESPELHO_PONTO_RESUMO_POR_CENTRO_CUSTO = "espelhoPontoResumoPorCentroCusto";
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
        return jasperReportBuilder.build(ESPELHO_PONTO_ANALITICO_POR_FUNCIONARIO, new HashMap<>(),
                setCampos(lancamentos, false));
    }

    @Override
    public byte[] imprimirEspelhoPontoAnaliticoPorCentroCusto(int centroCusto) {
        List<Lancamento> lancamentos = lancamentoService.buscarLancamentosCompetenciaAtualPorCentroCusto(centroCusto);
        return jasperReportBuilder.build(ESPELHO_PONTO_ANALITICO_POR_CENTRO_CUSTO, new HashMap<>(),
                setCampos(lancamentos, false));
    }

    @Override
    public byte[] imprimirEspelhoPontoResumoPorFuncionario(Long idFuncionario) {
        List<Lancamento> lancamentos = lancamentoService.buscarLancamentosCompetenciaAtualPorFuncionarioId(idFuncionario);
        lancamentos.stream()
                .filter(lancamento -> lancamento.getAtivo() && lancamento.getAssinaturaHash() != null)
                .collect(Collectors.toList());

        return jasperReportBuilder.build(ESPELHO_PONTO_RESUMO_POR_FUNCIONARIO, new HashMap<>(),
                setCampos(lancamentos, true));
    }

    @Override
    public byte[] imprimirEspelhoPontoResumoPorCentroCusto(int centroCusto) {
        List<Lancamento> lancamentos = lancamentoService.buscarLancamentosCompetenciaAtualPorCentroCusto(centroCusto);
        lancamentos.stream()
                .filter(lancamento -> lancamento.getAtivo() && lancamento.getAssinaturaHash() != null)
                .collect(Collectors.toList());

        return jasperReportBuilder.build(ESPELHO_PONTO_RESUMO_POR_CENTRO_CUSTO, new HashMap<>(),
                setCampos(lancamentos, true));
    }

    @Override
    public byte[] imprimirComprovantePonto(Long idLancamento) {
        return new byte[0];
    }

    private JRMapCollectionDataSource setCampos(List<Lancamento> lancamentos, boolean isResumo) {
        List<Map<String, ?>> details = Lists.newArrayList();

        String nomeFuncionario = getNomeFuncionario(lancamentos);
        String competencia = getCompetencia(lancamentos);
        float horasPrevistas = getHorasPrevistas(lancamentos);
        float horasTrabalhadas = 0;

        for (int i=1; i<lancamentos.size();i=i+2) {
            horasTrabalhadas = construirDetalhes(lancamentos, isResumo, details, nomeFuncionario, competencia, horasTrabalhadas, i);
        }

        calcularSaldoSalario(lancamentos, isResumo, details, horasPrevistas, horasTrabalhadas);
        return new JRMapCollectionDataSource(details);
    }

    private float construirDetalhes(List<Lancamento> lancamentos, boolean isResumo, List<Map<String, ?>> details, String nomeFuncionario, String competencia, float horasTrabalhadas, int i) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("nmFuncionario", nomeFuncionario);
        detail.put("competencia", competencia);
        detail.put("centroCusto", lancamentos.get(i).getCentroCusto().getCentroCusto());

        Timestamp hora = Timestamp.valueOf(lancamentos.get(i - 1).getData().toLocalDateTime());
        Timestamp hora2 = Timestamp.valueOf(lancamentos.get(i).getData().toLocalDateTime());
        detail.put("entrada", hora);
        detail.put("saida", hora2);

        LocalTime fim = calcularTotalDia(hora, hora2);
        detail.put("total", String.valueOf(fim));
        details.add(detail);

        if (isResumo) {
            horasTrabalhadas += fim.getHour() * 60 + fim.getMinute() + Float.valueOf(fim.getSecond()) / 60;
        } else {
            detail.put("ativo", isAtivo(lancamentos, i));
            detail.put("assinado", isAssinado(lancamentos, i));
        }

        return horasTrabalhadas;
    }

    private LocalTime calcularTotalDia(Timestamp hora, Timestamp hora2) {
        LocalTime tempDateTime = LocalTime.from(hora.toLocalDateTime().toLocalTime());
        Long hours = tempDateTime.until(hora2.toLocalDateTime(), ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        Long minutes = tempDateTime.until(hora2.toLocalDateTime(), ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        Long seconds = tempDateTime.until(hora2.toLocalDateTime(), ChronoUnit.SECONDS);
        return LocalTime.of(hours.intValue(), minutes.intValue(), seconds.intValue());
    }

    private void calcularSaldoSalario(List<Lancamento> lancamentos, boolean isResumo, List<Map<String, ?>> details, float horasPrevistas, float horasTrabalhadas) {
        if (isResumo) {
            float saldo = horasTrabalhadas / 60 - horasPrevistas;
            BigDecimal salario = BigDecimal.ZERO;
            salario = lancamentos.stream().findFirst().get().getFuncionario().getValorHora().multiply(BigDecimal.valueOf(horasTrabalhadas / 60));
            Map<String, Object> detailSaldo = (Map<String, Object>) details.get(details.size() - 1);
            detailSaldo.put("saldo", String.valueOf(saldo));
            detailSaldo.put("salario", salario.toString());
            details.remove(details.size() - 1);
            details.add(detailSaldo);
        }
    }

    private float getHorasPrevistas(List<Lancamento> lancamentos) {
        return lancamentos.stream()
                .findFirst().get()
                .getFuncionario().getQtdHorasTrabalhoDia() * getDiasUteis(YearMonth.now());
    }

    private String getNomeFuncionario(List<Lancamento> lancamentos) {
        return lancamentos.stream().findFirst().get().getFuncionario().getNome();
    }

    private String isAtivo(List<Lancamento> lancamentos, int i) {
        return lancamentos.get(i).getAtivo() && lancamentos.get(i - 1).getAtivo() ? "Sim" : "Não";
    }

    private String isAssinado(List<Lancamento> lancamentos, int i) {
        return lancamentos.get(i).getAssinaturaHash() != null &&
                lancamentos.get(i - 1).getAssinaturaHash() != null ? "Sim" : "Não";
    }

    private String getCompetencia(List<Lancamento> lancamentos) {
        LocalDateTime competencia = lancamentos.stream().findFirst().get().getData().toLocalDateTime();
        return competencia.getYear() + "/" + competencia.getMonthValue();
    }

    private int getDiasUteis(YearMonth competencia) {
        Stream<LocalDate> todosOsDiasDoMes =
                Stream.iterate(competencia.atDay(1), data -> data.plusDays(1))
                        .limit(competencia.lengthOfMonth());

        Stream<LocalDate> diasUteisDoMes =
                todosOsDiasDoMes.filter(data ->
                        !data.getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                                !data.getDayOfWeek().equals(DayOfWeek.SUNDAY));

        List<LocalDate> listaDosDiasUteisDoMes =
                diasUteisDoMes.collect(Collectors.toList());

        return listaDosDiasUteisDoMes.size();
    }
}
