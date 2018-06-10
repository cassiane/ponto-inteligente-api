package com.project.pontointeligente.api.report;

import com.project.pontointeligente.api.report.exception.GeracaoRelatorioException;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JasperReportBuilder {

    private static final String JRXML_RESOURCE_PATH = "report/";
    private static final String JASPER_DESTINATION_PATH = System.getProperty("java.io.tmpdir") + "ponto-inteligente-api/report";
    private static final String JRXML_EXTENSION = ".jrxml";
    private static final String JASPER_EXTENSION = ".jasper";

    byte[] build(String reportName, Map<String, Object> params, JRDataSource dataSource) {
        try {
            return buildReport(reportName, params, dataSource);
        } catch (Exception e) {
            throw new GeracaoRelatorioException("Não foi possível gerar o relatório.", e);
        }
    }

    private byte[] buildReport(String reportName, Map<String, Object> params, JRDataSource dataSource) throws JRException {
        JasperReport jasperReport = getJasperReport(reportName);
        return JasperRunManager.runReportToPdf(jasperReport, params, dataSource);
    }

    private JasperReport getJasperReport(String reportName) throws JRException {
        String jrxmlPath = JRXML_RESOURCE_PATH + reportName + JRXML_EXTENSION;
        return JasperCompileManager.compileReport(jrxmlPath);
    }

}
