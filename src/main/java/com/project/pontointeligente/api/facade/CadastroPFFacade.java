package com.project.pontointeligente.api.facade;

import com.project.pontointeligente.api.converter.CadastroPFDtoToFuncionario;
import com.project.pontointeligente.api.converter.FuncionarioToCadastroPFDto;
import com.project.pontointeligente.api.dtos.CadastroPFDto;
import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.services.FuncionarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CadastroPFFacade")
public class CadastroPFFacade implements CadastroFacade<CadastroPFDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CadastroPFFacade.class);

    private final FuncionarioService funcionarioService;
    private final CadastroPFDtoToFuncionario toFuncionario;
    private final FuncionarioToCadastroPFDto toCadastroPFDto;

    @Autowired
    public CadastroPFFacade(FuncionarioService funcionarioService, CadastroPFDtoToFuncionario toFuncionario, FuncionarioToCadastroPFDto toCadastroPFDto) {
        this.funcionarioService = funcionarioService;
        this.toFuncionario = toFuncionario;
        this.toCadastroPFDto = toCadastroPFDto;
    }

    @Override
    public CadastroPFDto salvar(CadastroPFDto dto) {
        LOGGER.info("Cadastrando PF: {}", dto);
        Funcionario funcionario = this.toFuncionario.convert(dto);
        Funcionario funcionarioSalvo = this.funcionarioService.persistir(funcionario);
        CadastroPFDto cadastroPFDtoSalvo = this.toCadastroPFDto.convert(funcionarioSalvo);
        LOGGER.info("PF cadastrado: {}", cadastroPFDtoSalvo);
        return cadastroPFDtoSalvo;
    }

}
