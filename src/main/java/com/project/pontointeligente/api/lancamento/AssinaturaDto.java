package com.project.pontointeligente.api.lancamento;

public class AssinaturaDto {

    private Long lancamentoId;
    private String senha;
    private String token;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Long getLancamentoId() {
        return lancamentoId;
    }

    public void setLancamentoId(Long lancamentoId) {
        this.lancamentoId = lancamentoId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
