package com.project.pontointeligente.api.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @ResponseBody
    @RequestMapping(value = "/exemplo", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] exemploReport() {
        return reportService.print();
    }

    @ResponseBody
    @RequestMapping(value = "/folha-ponto/{idFuncionario}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] folhaPonto(@PathVariable("idFuncionario") Long idFuncionario) {
        return reportService.exibirFolhaPontoCompetenciaAtualPorFuncionario(idFuncionario);
    }

}
