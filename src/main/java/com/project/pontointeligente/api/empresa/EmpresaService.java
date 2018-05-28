package com.project.pontointeligente.api.empresa;

import java.util.Optional;

import com.project.pontointeligente.api.empresa.Empresa;

public interface EmpresaService {
	
	/*
	 * Retorna uma empresa dado um cnpj
	 * */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	/*
	 * Persiste uma empresa
	 * */
	Empresa persistir(Empresa empresa);
}
