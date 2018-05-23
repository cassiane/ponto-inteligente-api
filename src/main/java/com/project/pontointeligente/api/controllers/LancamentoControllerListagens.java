package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.converter.ConverterLancamentoLogParaLancamentoLogDto;
import com.project.pontointeligente.api.converter.ConverterLancamentoParaLancamentoDto;
import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.dtos.LancamentoLogDto;
import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import com.project.pontointeligente.api.response.Response;
import com.project.pontointeligente.api.services.LancamentoLogServiceRepository;
import com.project.pontointeligente.api.services.LancamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoControllerListagens {

    private static final Logger log = LoggerFactory.getLogger(LancamentoControllerListagens.class);

    private LancamentoService lancamentoService;
    private ConverterLancamentoParaLancamentoDto lancamentoParaDto;
    private LancamentoLogServiceRepository lancamentoLogServiceRepository;
    private ConverterLancamentoLogParaLancamentoLogDto lancamentoLogParaDto;
    private int qtdPorPagina;

    @Value("${paginacao.qtd_por_pagina}")
    public int getQtdPorPagina() {
        return qtdPorPagina;
    }

    public void setQtdPorPagina(int qtdPorPagina) {
        this.qtdPorPagina = qtdPorPagina;
    }

    @Autowired
    public LancamentoControllerListagens(LancamentoService lancamentoService,
                                         ConverterLancamentoParaLancamentoDto lancamentoParaDto, LancamentoLogServiceRepository lancamentoLogServiceRepository, ConverterLancamentoLogParaLancamentoLogDto lancamentoLogParaDto) {
        this.lancamentoService = lancamentoService;
        this.lancamentoParaDto = lancamentoParaDto;
        this.lancamentoLogServiceRepository = lancamentoLogServiceRepository;
        this.lancamentoLogParaDto = lancamentoLogParaDto;
    }

    @GetMapping(value = "/isLancamentoValido/{lancamentoId}")
    public LancamentoDto verificarLancamentoValido(@PathVariable("lancamentoId") Long id) throws Exception {
        Optional<Lancamento> lancamento = lancamentoService.buscarLancamentoPorId(id);
        if (lancamento.isPresent()) {
            List<Lancamento> lancamentos = lancamentoService.buscarPreviousHash(id);
            if (ValidadorBloco.isChainValid(lancamentos)) {
                return lancamentoParaDto.convert(lancamento.get());
            } else {
                throw new Exception("Lançamento não é válido");
            }
        } else {
            throw new Exception("Lançamento não encontrado");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
        log.info("Buscando lançamento por ID: {}", id);
        Response<LancamentoDto> response = new Response<LancamentoDto>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarLancamentoPorId(id);

        if (!lancamento.isPresent()) {
            log.info("Lançamento não encontrado para o ID: {}", id);
            response.getErrors().add("Lançamento não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(lancamentoParaDto.convert(lancamento.get()));
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/auditoria/{id}")
    public ResponseEntity<Response<List<LancamentoLogDto>>> auditoria(
            @PathVariable("id") Long idLancamentoAlterado) {
        log.info("Buscando historico para ID do lancamento: {}, página: {}", idLancamentoAlterado);
        Response<List<LancamentoLogDto>> response = new Response<>();

        List<LancamentoLog> logs = this.lancamentoLogServiceRepository.buscarPorIdLancamentoAlterado(idLancamentoAlterado);
        List<LancamentoLogDto> lancamentosLogDto = logs.stream().map(this.lancamentoLogParaDto::convert).collect(Collectors.toList());

        response.setData(lancamentosLogDto);
        return ResponseEntity.ok(response);
    }

//    @GetMapping(value = "/funcionario/{funcionarioId}")
//    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
//            @PathVariable("funcionarioId") Long funcionarioId,
//            @RequestParam(value = "pag", defaultValue = "0") int pag,
//            @RequestParam(value = "ord", defaultValue = "id") String ord,
//            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {
//        log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", funcionarioId, pag);
//        Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();
//
//        PageRequest pageRequest = new PageRequest(pag, getQtdPorPagina(), Sort.Direction.valueOf(dir), ord);
//        Page<Lancamento> lancamentos = this.lancamentoServiceRepository.buscarPorFuncionarioId(funcionarioId, pageRequest);
//        Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.lancamentoParaDto.convert(lancamento));
//
//        response.setData(lancamentosDto);
//        return ResponseEntity.ok(response);
//    }
}
