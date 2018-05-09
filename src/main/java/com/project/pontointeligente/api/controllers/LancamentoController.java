package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.converter.ConverterLancamentoDtoParaLancamento;
import com.project.pontointeligente.api.converter.ConverterLancamentoParaLancamentoDto;
import com.project.pontointeligente.api.dtos.AssinaturaDto;
import com.project.pontointeligente.api.dtos.CrudDto;
import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import com.project.pontointeligente.api.enums.OperacaoEnum;
import com.project.pontointeligente.api.exceptions.InfraestructureException;
import com.project.pontointeligente.api.response.Response;
import com.project.pontointeligente.api.services.FuncionarioService;
import com.project.pontointeligente.api.services.LancamentoServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);

	private LancamentoServiceRepository lancamentoServiceRepository;
	private FuncionarioService funcionarioService;
	private ConverterLancamentoDtoParaLancamento dtoParaLancamento;
	private ConverterLancamentoParaLancamentoDto lancamentoParaDto;

	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	@Autowired
	public LancamentoController(LancamentoServiceRepository lancamentoServiceRepository, FuncionarioService funcionarioService, ConverterLancamentoDtoParaLancamento dtoParaLancamento, ConverterLancamentoParaLancamentoDto lancamentoParaDto) {
		this.lancamentoServiceRepository = lancamentoServiceRepository;
		this.funcionarioService = funcionarioService;
		this.dtoParaLancamento = dtoParaLancamento;
		this.lancamentoParaDto = lancamentoParaDto;
	}

	private CrudDto CRUD(LancamentoDto lancamentoDto, BindingResult result,
                           OperacaoEnum operacao, Long id) {
	    CrudDto dto = new CrudDto();
        Response<LancamentoDto> response = new Response<>();
        Optional<Funcionario> funcionario = funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
        dto.setResponse(response);
        if (isFuncionarioInvalido(lancamentoDto, result, response, funcionario)) {
            dto.setValido(false);
        }

        Optional<Lancamento> lanc = null;
        if (OperacaoEnum.ALTERACAO.equals(operacao)) {
            lancamentoDto.setId(Optional.of(id));
            lanc = this.lancamentoServiceRepository.buscarPorId(lancamentoDto.getId().get());
        }

        Lancamento lancamento = dtoParaLancamento.convert(lancamentoDto, result, lanc);
        dto.setFuncionario(funcionario);
        dto.setLancamento(lancamento);

        return dto;
    }

    @PostMapping
    public ResponseEntity<Response<AssinaturaDto>> assinarLancamento(@Valid @RequestBody AssinaturaDto assinaturaDto,
                                                                     BindingResult result) {
	    Optional<Lancamento> lancamento = lancamentoServiceRepository.buscarPorId(assinaturaDto.getLancamentoId());
	    if (lancamento.isPresent()) {
	        Funcionario funcionario = lancamento.get().getFuncionario();
	        if (funcionario.getSenhaAssinatura().isEmpty()) {
	            //é necessário informar uma senha para assinatura em seu cadastro.
            } else if (funcionario.getSenhaAssinatura().equals(assinaturaDto.getSenha())) {
	            
            }
        }

    }

	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result) throws Exception {
		log.info("Adicionando lançamento: {}", lancamentoDto.toString());
        CrudDto crudDto = CRUD(lancamentoDto, result, OperacaoEnum.INCLUSAO, null);
        if (isNull(crudDto)) {
            return ResponseEntity.badRequest().body(crudDto.getResponse());
        }
        Lancamento lancamento = crudDto.getLancamento();
		lancamento.setFuncionario(crudDto.getFuncionario().get());
        lancamento.setAtivo(true);
        lancamento.setDataCriacao(Timestamp.valueOf(LocalDateTime.now()));
		List<Lancamento> lancamentos = buscarPreviousHash();
        if (!CollectionUtils.isEmpty(lancamentos)) {
            lancamento.setPreviousHash(Objects.requireNonNull(lancamentos.stream()
					.reduce((first, second) -> second)
					.orElse(null)).getHash());
            lancamento.setHash(lancamento.calculateHash());

            lancamentos.add(lancamento);
            if (ValidadorBloco.isChainValid(lancamentos)) {
                return persistirLancamentoComLog(crudDto.getResponse(), lancamento);
            } else {
                return ResponseEntity.unprocessableEntity().body(null);
            }
        } else {
            throw new InfraestructureException("Não foram encontrados lançamentos anteriores.");
        }
    }

    private ResponseEntity<Response<LancamentoDto>> persistirLancamentoComLog(Response<LancamentoDto> response, Lancamento lancamento) {
        lancamento = this.lancamentoServiceRepository.persistir(lancamento);
        response.setData(lancamentoParaDto.convert(lancamento));
        LancamentoLog log = new LancamentoLog(lancamento, OperacaoEnum.INCLUSAO);
        this.lancamentoServiceRepository.persistir(log);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) {
		log.info("Atualizando lançamento: {}", lancamentoDto.toString());
        CrudDto dto = CRUD(lancamentoDto, result, OperacaoEnum.ALTERACAO, id);
        if (isNull(dto)) {
            return ResponseEntity.badRequest().body(dto.getResponse());
        }
		return persistirLancamentoComLog(dto.getResponse(), dto.getLancamento());
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento: {}", id);
		Response<String> response = new Response<>();
		Optional<Lancamento> lancamento = this.lancamentoServiceRepository.buscarPorId(id);

		if (!lancamento.isPresent()) {
			log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		} else {
			Funcionario funcionarioLancamento = lancamento.get().getFuncionario();
			List<ObjectError> erros = funcionarioService.validarFuncionarioNoLancamento(funcionarioLancamento.getId(), Optional.of(funcionarioLancamento));
			if (erros.size() > 0) {
				erros.forEach(erro -> response.getErrors().add(erro.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
		}

		this.lancamentoServiceRepository.remover(lancamento.get());
		return ResponseEntity.ok(new Response<String>());
	}

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
        log.info("Buscando lançamento por ID: {}", id);
        Response<LancamentoDto> response = new Response<LancamentoDto>();
        Optional<Lancamento> lancamento = this.lancamentoServiceRepository.buscarPorId(id);

        if (!lancamento.isPresent()) {
            log.info("Lançamento não encontrado para o ID: {}", id);
            response.getErrors().add("Lançamento não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(lancamentoParaDto.convert(lancamento.get()));
        return ResponseEntity.ok(response);
    }

	@GetMapping(value = "/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
			@PathVariable("funcionarioId") Long funcionarioId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", funcionarioId, pag);
		Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();

		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Lancamento> lancamentos = this.lancamentoServiceRepository.buscarPorFuncionarioId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.lancamentoParaDto.convert(lancamento));

		response.setData(lancamentosDto);
		return ResponseEntity.ok(response);
	}

    private boolean isFuncionarioInvalido(@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result,
                                          Response<LancamentoDto> response, Optional<Funcionario> funcionario) {
        List<ObjectError> erros = funcionarioService.validarFuncionarioNoLancamento(lancamentoDto.getFuncionarioId(), funcionario);

        if (erros.size() > 0) {
            log.error("Erro validando lançamento: {}", erros);
            erros.forEach(erro -> {
                result.addError(erro);
                response.getErrors().add(erro.getDefaultMessage());
            });
            return true;
        }
        return false;
    }

    private List<Lancamento> buscarPreviousHash() {
        return lancamentoServiceRepository.buscarUltimosLancamentos();
    }
}
