package com.project.pontointeligente.api.entities;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.project.pontointeligente.api.enums.TipoEnum;
import com.project.pontointeligente.api.utils.HashUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "lancamento")
public class Lancamento implements Serializable {

	private static final long serialVersionUID = 236188582712441484L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Lancamento.class);

    private Long id;
	private Timestamp data;
	private String descricao;
	private Timestamp dataCriacao;
	private Timestamp dataAtualizacao;
	private TipoEnum tipo;
	private Funcionario funcionario;
    private String hash;
    private String previousHash;
	
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
	
	@Column(name = "descricao")
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

    @Column(name = "data_atualizacao", nullable = false)
	public Timestamp getDataAtualizacao() {
		return dataAtualizacao;
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

	public void setDataCriacao(Timestamp dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public void setDataAtualizacao(Timestamp dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@PreUpdate
	public void preUpdate(){
		dataAtualizacao = Timestamp.valueOf(LocalDateTime.now());
	}

	@PrePersist
	public void prePersist(){
        final Timestamp atual = Timestamp.valueOf(LocalDateTime.now());
        dataAtualizacao = atual;
        dataCriacao = atual;
	}

    public String calculateHash() {
	    LOGGER.info("Calculando hash dos dados: cpf: {}, data: {}", this.getFuncionario().getCpf(), data.toString());
		return HashUtils.applySha256(
                        funcionario.getCpf() +
								data.toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lancamento)) return false;
        Lancamento that = (Lancamento) o;
        return Objects.equal(getId(), that.getId()) &&
                Objects.equal(getData(), that.getData()) &&
                Objects.equal(getDescricao(), that.getDescricao()) &&
                Objects.equal(getDataCriacao(), that.getDataCriacao()) &&
                Objects.equal(getDataAtualizacao(), that.getDataAtualizacao()) &&
                getTipo() == that.getTipo() &&
                Objects.equal(getFuncionario(), that.getFuncionario()) &&
                Objects.equal(getHash(), that.getHash()) &&
                Objects.equal(getPreviousHash(), that.getPreviousHash());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getData(), getDescricao(), getDataCriacao(), getDataAtualizacao(), getTipo(), getFuncionario(), getHash(), getPreviousHash());
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
				.toString();
	}
}
