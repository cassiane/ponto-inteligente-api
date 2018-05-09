package com.project.pontointeligente.api.entities;

import com.google.common.base.MoreObjects;
import com.project.pontointeligente.api.enums.OperacaoEnum;
import com.project.pontointeligente.api.enums.TipoEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "lancamentolog")
public class LancamentoLog {

    private Long id;
    private Timestamp data;
    private String descricao;
    private Timestamp dataCriacao;
    private Timestamp dataAtualizacao;
    private TipoEnum tipo;
    private Funcionario funcionario;
    private String hash;
    private String previousHash;
    private OperacaoEnum operacao;
    private Long idLancamentoAlterado;

    public LancamentoLog(Lancamento lancamento, OperacaoEnum operacao) {
        this.data = lancamento.getData();
        this.descricao = lancamento.getDescricao();
        this.dataCriacao = lancamento.getDataCriacao();
        this.dataAtualizacao = lancamento.getDataAtualizacao();
        this.tipo = lancamento.getTipo();
        this.funcionario = lancamento.getFuncionario();
        this.hash = lancamento.getHash();
        this.previousHash = lancamento.getPreviousHash();
        this.idLancamentoAlterado = lancamento.getId();
        this.operacao = operacao;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "data", nullable = false)
    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    @Column(name = "descricao", nullable = true)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name = "data_criacao", nullable = false)
    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Column(name = "data_atualizacao", nullable = false)
    public Timestamp getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Timestamp dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    public TipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoEnum tipo) {
        this.tipo = tipo;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Column(name = "hash", nullable = false)
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name = "previous_hash", nullable = false)
    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "operacao", nullable = false)
    public OperacaoEnum getOperacao() {
        return operacao;
    }

    public void setOperacao(OperacaoEnum operacao) {
        this.operacao = operacao;
    }

    @Column(name = "id_lancamento_alterado")
    public Long getIdLancamentoAlterado() {
        return idLancamentoAlterado;
    }

    public void setIdLancamentoAlterado(Long idLancamentoAlterado) {
        this.idLancamentoAlterado = idLancamentoAlterado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LancamentoLog)) return false;
        LancamentoLog that = (LancamentoLog) o;
        return com.google.common.base.Objects.equal(getId(), that.getId()) &&
                com.google.common.base.Objects.equal(getData(), that.getData()) &&
                com.google.common.base.Objects.equal(getDescricao(), that.getDescricao()) &&
                com.google.common.base.Objects.equal(getDataCriacao(), that.getDataCriacao()) &&
                com.google.common.base.Objects.equal(getDataAtualizacao(), that.getDataAtualizacao()) &&
                getTipo() == that.getTipo() &&
                com.google.common.base.Objects.equal(getFuncionario(), that.getFuncionario()) &&
                com.google.common.base.Objects.equal(getHash(), that.getHash()) &&
                com.google.common.base.Objects.equal(getPreviousHash(), that.getPreviousHash()) &&
                getOperacao() == that.getOperacao() &&
                com.google.common.base.Objects.equal(getIdLancamentoAlterado(), that.getIdLancamentoAlterado());
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(getId(), getData(), getDescricao(), getDataCriacao(), getDataAtualizacao(), getTipo(), getFuncionario(), getHash(), getPreviousHash(), getOperacao(), getIdLancamentoAlterado());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("data", data)
                .add("descricao", descricao)
                .add("dataCriacao", dataCriacao)
                .add("dataAtualizacao", dataAtualizacao)
                .add("tipo", tipo)
                .add("funcionario", funcionario)
                .add("hash", hash)
                .add("previousHash", previousHash)
                .add("operacao", operacao)
                .add("idLancamentoAlterado", idLancamentoAlterado)
                .toString();
    }
}
