package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.converter.ConverterLancamentoDtoParaLancamento;
import com.project.pontointeligente.api.converter.ConverterLancamentoParaLancamentoDto;
import com.project.pontointeligente.api.dtos.CrudDto;
import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.entities.LancamentoLog;
import com.project.pontointeligente.api.enums.OperacaoEnum;
import com.project.pontointeligente.api.exceptions.InfraestructureException;
import com.project.pontointeligente.api.response.Response;
import com.project.pontointeligente.api.services.FuncionarioService;
import com.project.pontointeligente.api.services.LancamentoService;
import com.project.pontointeligente.api.services.LancamentoServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoControllerPersistencia {

	private static final Logger log = LoggerFactory.getLogger(LancamentoControllerPersistencia.class);

	private LancamentoService lancamentoService;
	private FuncionarioService funcionarioService;
	private ConverterLancamentoDtoParaLancamento dtoParaLancamento;
	private ConverterLancamentoParaLancamentoDto lancamentoParaDto;

	@Autowired
	public LancamentoControllerPersistencia(LancamentoService lancamentoService,
                                            FuncionarioService funcionarioService,
                                            ConverterLancamentoDtoParaLancamento dtoParaLancamento,
                                            ConverterLancamentoParaLancamentoDto lancamentoParaDto) {
        this.lancamentoService = lancamentoService;
        this.funcionarioService = funcionarioService;
		this.dtoParaLancamento = dtoParaLancamento;
		this.lancamentoParaDto = lancamentoParaDto;
	}

	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result)
    {
		log.info("Adicionando lançamento: {}", lancamentoDto.toString());
        CrudDto crudDto = CRUD(lancamentoDto, result, OperacaoEnum.INCLUSAO, null);
        return inclusaoAlteracao(crudDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
                                                             @Valid @RequestBody LancamentoDto lancamentoDto,
                                                             BindingResult result)
    {
        log.info("Atualizando lançamento: {}", lancamentoDto.toString());
        CrudDto crudDto = CRUD(lancamentoDto, result, OperacaoEnum.ALTERACAO, id);
        return inclusaoAlteracao(crudDto);
    }

    private ResponseEntity<Response<LancamentoDto>> inclusaoAlteracao(CrudDto crudDto) {
        if (isNull(crudDto) || !crudDto.isValido()) {
            return ResponseEntity.badRequest().body(crudDto.getResponse());
        }
        Lancamento lancamento = lancamentoService.persistirLancamento(crudDto.getLancamento());
        if (nonNull(lancamento)) {
            crudDto.getResponse().setData(lancamentoParaDto.convert(lancamento));
            return ResponseEntity.ok(crudDto.getResponse());
        }

        return ResponseEntity.badRequest().body(crudDto.getResponse());
    }

    @DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento: {}", id);
		Response<String> response = new Response<>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarLancamentoPorId(id);

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

		lancamentoService.remover(lancamento.get());
		return ResponseEntity.ok(new Response<String>());
	}

    private CrudDto CRUD(LancamentoDto lancamentoDto, BindingResult result,
                         OperacaoEnum operacao, Long id) {
        CrudDto dto = new CrudDto();
        Response<LancamentoDto> response = new Response<>();
        Optional<Funcionario> funcionario = funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
        dto.setResponse(response);

        dto.setValido(!isFuncionarioInvalido(lancamentoDto, result, response, funcionario));

        Optional<Lancamento> lanc = null;
        if (OperacaoEnum.ALTERACAO.equals(operacao)) {
            lancamentoDto.setId(Optional.of(id));
            lanc = this.lancamentoService.buscarLancamentoPorId(lancamentoDto.getId().get());
        }

        dto.setFuncionario(funcionario);
        Lancamento lancamento = dtoParaLancamento.convert(lancamentoDto, result, lanc);
        lancamento.setFuncionario(funcionario.get());
        dto.setLancamento(lancamento);

        return dto;
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


}
