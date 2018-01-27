package com.project.pontointeligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.project.pontointeligente.api.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
	
	//Anotação para melhorar a performance de consulta no banco pois não cria uma transação por se tratar de apenas um select
	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);
}
