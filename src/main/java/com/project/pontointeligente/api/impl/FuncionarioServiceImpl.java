package com.project.pontointeligente.api.impl;

import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.exceptions.ValidationException;
import com.project.pontointeligente.api.repositories.FuncionarioRepository;
import com.project.pontointeligente.api.security.JwtUser;
import com.project.pontointeligente.api.services.FuncionarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements FuncionarioService{

	private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	private static final String FUNCIONARIO = "funcionario";

	private final FuncionarioRepository funcionarioRepository;

	@Autowired
	public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}

	@Override
	public Funcionario persistir(Funcionario funcionario) {
		LOGGER.info("Validando funcionário: {}", funcionario);
		validar(funcionario);
		LOGGER.info("Persistindo funcionário: {}", funcionario);
		return this.funcionarioRepository.save(funcionario);
	}

	private void validar(Funcionario funcionario) {
		validarCpfExistente(funcionario);
		validarEmailExistente(funcionario);
	}

	private void validarCpfExistente(Funcionario funcionario) {
		buscarPorCpf(funcionario.getCpf())
				.ifPresent(func -> { throw new ValidationException(FUNCIONARIO, "CPF já existente."); });
	}

	private void validarEmailExistente(Funcionario funcionario) {
		buscarPorEmail(funcionario.getEmail())
				.ifPresent(func -> { throw new ValidationException(FUNCIONARIO, "Email já existente."); });
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
	public List<ObjectError> validarFuncionarioNoLancamento(Long idFuncionarioLancamento, Optional<Funcionario> funcionario) {
		LOGGER.info("Validando funcionário id {}: ", idFuncionarioLancamento);
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		JwtUser usuarioAutenticado = (JwtUser) autentication.getPrincipal();

		List<ObjectError> result = new ArrayList<>();

		if (idFuncionarioLancamento == null) {
			result.add(new ObjectError(FUNCIONARIO, "Funcionário não informado."));
		}

		if (!funcionario.isPresent()) {
			result.add(new ObjectError(FUNCIONARIO, "Funcionário não encontrado. ID inexistente."));
		}

		if (!usuarioAutenticado.getUsername().equals(funcionario.get().getEmail())) {
			result.add(new ObjectError(FUNCIONARIO, "Funcionário informado é diferente do usuário autenticado."));
		}

		LOGGER.info("Resultado da validação: {}", result);
		return result;
	}

}
