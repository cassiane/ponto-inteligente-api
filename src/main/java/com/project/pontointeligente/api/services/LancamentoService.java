package com.project.pontointeligente.api.services;

import com.project.pontointeligente.api.dtos.LancamentoDto;
import com.project.pontointeligente.api.entities.Funcionario;
import org.springframework.validation.BindingResult;

import java.util.Optional;

public interface LancamentoService {

    BindingResult validarFuncionario(LancamentoDto lancamentoDto, BindingResult result, Optional<Funcionario> funcionario);
}
