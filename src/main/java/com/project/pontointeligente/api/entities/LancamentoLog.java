package com.project.pontointeligente.api.entities;

import com.project.pontointeligente.api.enums.OperacaoEnum;
import com.project.pontointeligente.api.enums.TipoEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "lancamentolog")
public class LancamentoLog {

    private Long id;
    private LocalDateTime data;
    private String descricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private TipoEnum tipo;
    private Funcionario funcionario;
    private String hash;
    private String previousHash;
    private OperacaoEnum operacao;
    private Long idLancamentoAlterado;

    public LancamentoLog(Lancamento lancamento) {
        this.data = lancamento.getData();
        this.descricao = lancamento.getDescricao();
        this.dataCriacao = lancamento.getDataCriacao();
        this.dataAtualizacao = lancamento.getDataAtualizacao();
        this.tipo = lancamento.getTipo();
        this.funcionario = lancamento.getFuncionario();
        this.hash = lancamento.getHash();
        this.previousHash = lancamento.getPreviousHash();
        this.idLancamentoAlterado = lancamento.getId();
    }

    public LancamentoLog(LocalDateTime data, String descricao, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                         TipoEnum tipo, Funcionario funcionario, String hash, String previousHash, OperacaoEnum operacao,
                         Long idLancamentoAlterado) {
        this.data = data;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.tipo = tipo;
        this.funcionario = funcionario;
        this.hash = hash;
        this.previousHash = previousHash;
        this.operacao = operacao;
        this.idLancamentoAlterado = idLancamentoAlterado;
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
    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
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
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Column(name = "data_atualizacao", nullable = false)
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
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

    @PreUpdate
    public void preUpdate(){
        operacao = OperacaoEnum.ALTERACAO;
    }

    @PrePersist
    public void prePersist(){
        operacao = OperacaoEnum.INCLUSAO;
    }

}
