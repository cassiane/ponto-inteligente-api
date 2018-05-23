package com.project.pontointeligente.api.controllers;

import com.project.pontointeligente.api.dtos.CadastroPFDto;
import com.project.pontointeligente.api.facade.CadastroPFFacade;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

	private final CadastroPFFacade cadastroFacade;

	public CadastroPFController(CadastroPFFacade cadastroFacade) {
		this.cadastroFacade = cadastroFacade;
	}

	@PostMapping
	public CadastroPFDto cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto) {
		return cadastroFacade.salvar(cadastroPFDto);
	}

}
