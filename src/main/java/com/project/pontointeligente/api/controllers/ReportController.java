package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.report.ExemploReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ExemploReport exemploReport;

    @Autowired
    public ReportController(ExemploReport exemploReport) {
        this.exemploReport = exemploReport;
    }

    @ResponseBody
    @RequestMapping(value = "/exemplo", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] exemploReport() {
        return exemploReport.print();
    }

}
