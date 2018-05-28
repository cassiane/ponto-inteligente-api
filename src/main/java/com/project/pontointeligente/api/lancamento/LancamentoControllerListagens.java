package com.project.pontointeligente.api.lancamento;

import com.project.pontointeligente.api.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private ConverterLancamentoLogParaLancamentoLogDto lancamentoLogParaDto;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    @Autowired
    public LancamentoControllerListagens(LancamentoService lancamentoService,
                                         ConverterLancamentoParaLancamentoDto lancamentoParaDto,
                                         ConverterLancamentoLogParaLancamentoLogDto lancamentoLogParaDto) {
        this.lancamentoService = lancamentoService;
        this.lancamentoParaDto = lancamentoParaDto;
        this.lancamentoLogParaDto = lancamentoLogParaDto;
    }

    @GetMapping(value = "/isLancamentoValido/{lancamentoId}")
    public ResponseEntity<Response<Boolean>> verificarLancamentoValido(@PathVariable("lancamentoId") Long id) throws Exception {
        Response<Boolean> response = new Response<>();
        Optional<Lancamento> lancamento = lancamentoService.buscarLancamentoPorId(id);
        if (lancamento.isPresent()) {
            List<Lancamento> lancamentos = lancamentoService.buscarPreviousHash(id);
            if (ValidadorBloco.isChainValid(lancamentos)) {
                response.setData(true);
                return ResponseEntity.ok(response);
            } else {
                throw new Exception("Lançamento não é válido");
            }
        } else throw new Exception("Lançamento não encontrado");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
        log.info("Buscando lançamento por ID: {}", id);
        Response<LancamentoDto> response = new Response<>();
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

        Optional<Lancamento> lancamento = this.lancamentoService.buscarLancamentoPorId(idLancamentoAlterado);
        if (lancamento.isPresent()) {
            if (!TipoEnum.REGISTRO_INICIAL.equals(lancamento.get().getTipo())) {
                List<LancamentoLog> logs = this.lancamentoService.buscarLogsPorIdLancamentoAlterado(idLancamentoAlterado);
                List<LancamentoLogDto> lancamentosLogDto = logs.stream().map(this.lancamentoLogParaDto::convert).collect(Collectors.toList());

                response.setData(lancamentosLogDto);
                return ResponseEntity.ok(response);
            } else {
                response.getErrors().add("Registro inicial não pode ser auditado.");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
        } else {
            response.getErrors().add("Lançamento não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    @GetMapping(value = "/funcionario/{funcionarioId}")
    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
            @PathVariable("funcionarioId") Long funcionarioId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {
        log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", funcionarioId, pag);
        Response<Page<LancamentoDto>> response = new Response<>();

        PageRequest pageRequest = new PageRequest(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord);
        Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
        Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.lancamentoParaDto.convert(lancamento));

        response.setData(lancamentosDto);
        return ResponseEntity.ok(response);
    }
}
