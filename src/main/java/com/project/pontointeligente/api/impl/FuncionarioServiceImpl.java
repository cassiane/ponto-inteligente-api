package com.project.pontointeligente.api.impl;

import java.util.Optional;

import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.security.JwtUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.repositories.FuncionarioRepository;
import com.project.pontointeligente.api.services.FuncionarioService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Service
public class FuncionarioServiceImpl implements FuncionarioService{

	public static Logger LOGGER = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	public static final String FUNCIONARIO = "funcionario";

	@Autowired
	private FuncionarioRepository funcionarioRepository;


	@Override
	public Funcionario persistir(Funcionario funcionario) {
		return this.funcionarioRepository.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
        LOGGER.info("Buscando funcionário por cpf: {}", cpf);
        Optional<Funcionario> funcionario = Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
        LOGGER.info("Funcionário {} encontrado", funcionario.isPresent() ? "" : "não");

        return funcionario;
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
        LOGGER.info("Buscando funcionário por email: {}", email);
        Optional<Funcionario> funcionario = Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
        LOGGER.info("Funcionário {} encontrado", funcionario.isPresent() ? "" : "não");

        return funcionario;
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		LOGGER.info("Buscando funcionário por id: {}", id);
		Optional<Funcionario> funcionario =  Optional.ofNullable(this.funcionarioRepository.findOne(id));
		LOGGER.info("Funcionário {} encontrado", funcionario.isPresent() ? "" : "não");

		return funcionario;
	}

	@Override
	public BindingResult validarFuncionarioNoLancamento(LancamentoDto lancamentoDto, BindingResult result, Optional<Funcionario> funcionario) {
		LOGGER.info("Validando funcionário id {}: ", lancamentoDto.getFuncionarioId());
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		JwtUser usuarioAutenticado = (JwtUser) autentication.getPrincipal();

		if (lancamentoDto.getFuncionarioId() == null) {
			result.addError(new ObjectError(FUNCIONARIO, "Funcionário não informado."));
		}

		if (!funcionario.isPresent()) {
			result.addError(new ObjectError(FUNCIONARIO, "Funcionário não encontrado. ID inexistente."));
		}

		if (!usuarioAutenticado.getUsername().equals(funcionario.get().getEmail())) {
			result.addError(new ObjectError(FUNCIONARIO, "Funcionário informado é diferente do usuário autenticado."));
		}

		LOGGER.info("Resultado da validação: {}", result);
		return result;
	}

}
