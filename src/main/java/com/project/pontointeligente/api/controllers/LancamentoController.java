package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.converter.ConverterLancamentoDtoParaLancamento;
import com.project.pontointeligente.api.converter.ConverterLancamentoParaLancamentoDto;
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
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

	public void CRUD(String operacao) {

	}

	/**
	 * Retorna um lançamento por ID.
	 *
	 * @param id
	 * @return ResponseEntity<Response<LancamentoDto>>
	 */
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

	/**
	 * Adiciona um novo lançamento.
	 *
     * @param lancamentoDto
	 * @param result
	 * @return ResponseEntity<Response<LancamentoDto>>
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result) throws Exception {
		log.info("Adicionando lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<>();
		Optional<Funcionario> funcionario = funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
		if (isFuncionarioInvalido(lancamentoDto, result, response, funcionario))
			return ResponseEntity.badRequest().body(response);

		Lancamento lancamento = dtoParaLancamento.convert(lancamentoDto, result, Optional.empty());
		lancamento.setFuncionario(funcionario.get());
        List<Lancamento> lancamentos = buscarPreviousHash();
        if (!CollectionUtils.isEmpty(lancamentos)) {
            lancamento.setPreviousHash(Objects.requireNonNull(lancamentos.stream()
					.reduce((first, second) -> second)
					.orElse(null)).getHash());
            lancamento.setHash(lancamento.calculateHash());

            lancamentos.add(lancamento);
            if (ValidadorBloco.isChainValid(lancamentos)) {
                lancamento = this.lancamentoServiceRepository.persistir(lancamento);
                response.setData(lancamentoParaDto.convert(lancamento));
                LancamentoLog log = new LancamentoLog(lancamento, OperacaoEnum.INCLUSAO);
                this.lancamentoServiceRepository.persistir(log);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.unprocessableEntity().body(null);
            }
        } else {
            throw new InfraestructureException("Não foram encontrados lançamentos anteriores.");
        }
    }

	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) {
		log.info("Atualizando lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<>();
		Optional<Funcionario> funcionario = funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
		if (isFuncionarioInvalido(lancamentoDto, result, response, funcionario))
			return ResponseEntity.badRequest().body(response);

		lancamentoDto.setId(Optional.of(id));
		Optional<Lancamento> lanc = this.lancamentoServiceRepository.buscarPorId(lancamentoDto.getId().get());
		Lancamento lancamento = dtoParaLancamento.convert(lancamentoDto, result, lanc);
		lancamento = lancamentoServiceRepository.persistir(lancamento);
		LancamentoLog log = new LancamentoLog(lancamento, OperacaoEnum.ALTERACAO);
		this.lancamentoServiceRepository.persistir(log);
		response.setData(lancamentoParaDto.convert(lancamento));
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
}
