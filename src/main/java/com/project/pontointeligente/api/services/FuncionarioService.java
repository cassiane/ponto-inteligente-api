package com.project.pontointeligente.api.services;

import java.util.List;
import java.util.Optional;

import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.entities.Funcionario;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public interface FuncionarioService {
	
	Funcionario persistir(Funcionario funcionario);
	Optional<Funcionario> buscarPorCpf(String cpf);
	Optional<Funcionario> buscarPorEmail(String email);
	Optional<Funcionario> buscarPorId(Long id);
	List<ObjectError> validarFuncionarioNoLancamento(Long idFuncionarioLancamento, Optional<Funcionario> funcionario);
}
