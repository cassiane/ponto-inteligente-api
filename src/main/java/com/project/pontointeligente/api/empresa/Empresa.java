package com.project.pontointeligente.api.empresa;

import com.google.common.base.Objects;
import com.project.pontointeligente.api.centroCusto.CentroCusto;
import com.project.pontointeligente.api.funcionario.Funcionario;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

	private static final long serialVersionUID = 2190446218374523593L;

	private Long id;
	private String razaoSocial;
	private String cnpj;
	private Date dataCriacao;
	private Date dataAtualizacao;
	private List<Funcionario> funcionarios;
	private List<CentroCusto> centroCusto;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "razao_social", nullable = false)
	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	@Column(name = "cnpj", nullable = false)
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "data_atualizacao", nullable = false)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<CentroCusto> getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(List<CentroCusto> centroCusto) {
		this.centroCusto = centroCusto;
	}

	@PreUpdate
	public void preUpdate() {
		dataAtualizacao = new Date();
	}

	@PrePersist
	public void prePersist() {
		final Date atual = new Date();
		dataCriacao = atual;
		dataAtualizacao = atual;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Empresa)) return false;
		Empresa empresa = (Empresa) o;
		return Objects.equal(getId(), empresa.getId()) &&
				Objects.equal(getRazaoSocial(), empresa.getRazaoSocial()) &&
				Objects.equal(getCnpj(), empresa.getCnpj()) &&
				Objects.equal(getDataCriacao(), empresa.getDataCriacao()) &&
				Objects.equal(getDataAtualizacao(), empresa.getDataAtualizacao()) &&
				Objects.equal(getFuncionarios(), empresa.getFuncionarios());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), getRazaoSocial(), getCnpj(), getDataCriacao(), getDataAtualizacao(), getFuncionarios());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("razaoSocial", razaoSocial)
				.append("cnpj", cnpj)
				.append("dataCriacao", dataCriacao)
				.append("dataAtualizacao", dataAtualizacao)
				.append("funcionarios", funcionarios)
				.toString();
	}
}
