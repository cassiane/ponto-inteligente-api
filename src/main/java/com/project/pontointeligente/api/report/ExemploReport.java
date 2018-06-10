package com.project.pontointeligente.api.report;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Component
public class ExemploReport {

    private final JasperReportBuilder jasperReportBuilder;

    @Autowired
    public ExemploReport(JasperReportBuilder jasperReportBuilder) {
        this.jasperReportBuilder = jasperReportBuilder;
    }

    public byte[] print() {
        return jasperReportBuilder.build("exemplo", new HashMap<>(), getDetail());
    }

    private JRMapCollectionDataSource getDetail() {
        Map<String, Object> detail = new HashMap<>();
        detail.put("id", "1000");
        detail.put("nome", "Fulano");
        List<Map<String, ?>> details = new ArrayList<>();
        details.add(detail);
        return new JRMapCollectionDataSource(details);
    }

}
