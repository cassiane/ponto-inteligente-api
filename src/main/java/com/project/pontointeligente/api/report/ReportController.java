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
    @RequestMapping(value = "/folha-ponto-analitico/centro-custo/{centroCusto}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] folhaPontoAnaliticoPorCentroCusto(@PathVariable("centroCusto") int centroCusto) {
        return reportService.imprimirEspelhoPontoAnaliticoPorCentroCusto(centroCusto);
    }

    @ResponseBody
    @RequestMapping(value = "/folha-ponto-analitico/funcionario/{idFuncionario}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] folhaPontoAnaliticoPorFuncionario(@PathVariable("idFuncionario") Long idFuncionario) {
        return reportService.imprimirEspelhoPontoAnaliticoPorFuncionario(idFuncionario);
    }

    @ResponseBody
    @RequestMapping(value = "/folha-ponto-resumo/centro-custo/{centroCusto}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] folhaPontoResumoPorCentroCusto(@PathVariable("centroCusto") int centroCusto) {
        return reportService.imprimirEspelhoPontoResumoPorCentroCusto(centroCusto);
    }

    @ResponseBody
    @RequestMapping(value = "/folha-ponto-resumo/funcionario/{idFuncionario}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] folhaPontoResumoPorFuncionario(@PathVariable("idFuncionario") Long idFuncionario) {
        return reportService.imprimirEspelhoPontoResumoPorFuncionario(idFuncionario);
    }

    @ResponseBody
    @RequestMapping(value = "/comprovante-lancamento/{idLancamento}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] comprovantePonto(@PathVariable("idLancamento") Long idLancamento) {
        return reportService.imprimirComprovantePonto(idLancamento);
    }

}
