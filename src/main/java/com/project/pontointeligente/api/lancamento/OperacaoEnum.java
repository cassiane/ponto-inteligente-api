package com.project.pontointeligente.api.lancamento;

public enum OperacaoEnum {
    INCLUSAO("I"),
    ALTERACAO("A"),
    EXCLUSAO("E");

    private String operacao;

    OperacaoEnum(String operacao) {
        this.operacao = operacao;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
}
