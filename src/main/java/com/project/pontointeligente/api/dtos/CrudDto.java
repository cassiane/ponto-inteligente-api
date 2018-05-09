package com.project.pontointeligente.api.dtos;

import com.project.pontointeligente.api.entities.Funcionario;
import com.project.pontointeligente.api.entities.Lancamento;
import com.project.pontointeligente.api.response.Response;

import java.util.Optional;

public class CrudDto {

    private Lancamento lancamento;
    private Optional<Funcionario> funcionario;
    private Response<LancamentoDto> response;
    private boolean valido;

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public Response<LancamentoDto> getResponse() {
        return response;
    }

    public void setResponse(Response<LancamentoDto> response) {
        this.response = response;
    }

    public Optional<Funcionario> getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Optional<Funcionario> funcionario) {
        this.funcionario = funcionario;
    }

    public Lancamento getLancamento() {
        return lancamento;
    }

    public void setLancamento(Lancamento lancamento) {
        this.lancamento = lancamento;
    }
}
