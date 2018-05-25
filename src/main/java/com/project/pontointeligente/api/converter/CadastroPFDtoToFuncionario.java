package com.project.pontointeligente.api.converter;

import com.project.pontointeligente.api.dtos.CadastroPFDto;
import com.project.pontointeligente.api.entities.Empresa;
import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.enums.PerfilEnum;
import com.project.pontointeligente.api.exceptions.ValidationException;
import com.project.pontointeligente.api.services.EmpresaService;
import com.project.pontointeligente.api.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CadastroPFDtoToFuncionario implements Converter<CadastroPFDto, Funcionario> {

    private final EmpresaService empresaService;

    @Autowired
    public CadastroPFDtoToFuncionario(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @Override
    public Funcionario convert(CadastroPFDto cadastroPFDto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(cadastroPFDto.getNome());
        funcionario.setCpf(cadastroPFDto.getCpf());
        funcionario.setEmail(cadastroPFDto.getEmail());
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));

        cadastroPFDto.getQtdHorasAlmoco()
                .ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
        cadastroPFDto.getQtdHorasTrabalhoDia().ifPresent(
                qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
        cadastroPFDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        empresa.orElseThrow(() -> new ValidationException("empresa", "Empresa não cadastrada."));
        funcionario.setEmpresa(empresa.get());

        return funcionario;
    }

}
