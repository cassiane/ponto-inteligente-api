package com.project.pontointeligente.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.project.pontointeligente.api.enums.PerfilEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "funcionario")
public class Funcionario implements Serializable {

	private static final long serialVersionUID = 3486088513568300015L;

	private Long id;
	private String nome, email, senha, cpf;
	private BigDecimal valorHora;
	private Float qtdHorasTrabalhoDia, qtdHorasAlmoco;
	private PerfilEnum perfil;
	private Date dataCriacao, dataAtualizacao;
	private Empresa empresa;
	private List<Lancamento> lancamentos;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "nome", nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "senha", nullable = false)
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	@Column(name = "cpf", nullable = false)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	@Column(name = "valor_hora", nullable = true)
	public BigDecimal getValorHora() {
		return valorHora;
	}
	
	@Transient
	public Optional <BigDecimal> getValorHoraOpt(){
		return Optional.ofNullable(valorHora);
	}

	public void setValorHora(BigDecimal valorHora) {
		this.valorHora = valorHora;
	}
	
	@Column(name = "qtd_horas_trabalho_dia")
	public Float getQtdHorasTrabalhoDia() {
		return qtdHorasTrabalhoDia;
	}
	
	@Transient
	public Optional <Float> getQtdHorasTrabalhoDiaOpt(){
		return Optional.ofNullable(qtdHorasTrabalhoDia);
	}
	
	public void setQtdHorasTrabalhoDia(Float qtdHorasTrabalhoDia) {
		this.qtdHorasTrabalhoDia = qtdHorasTrabalhoDia;
	}
	
	@Column(name = "qtd_horas_almoco", nullable = true)
	public Float getQtdHorasAlmoco() {
		return qtdHorasAlmoco;
	}
	
	@Transient
	public Optional <Float> getQtdHorasAlmocoOpt(){
		return Optional.ofNullable(qtdHorasAlmoco);
	}

	public void setQtdHorasAlmoco(Float qtdHorasAlmoco) {
		this.qtdHorasAlmoco = qtdHorasAlmoco;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "perfil", nullable = false)
	public PerfilEnum getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	@OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	@PreUpdate	
	public void preUpdate(){
		dataAtualizacao = new Date();
	}
	
	@PrePersist
	public void prePersist(){
		final Date atual = new Date();
		dataAtualizacao = atual;
		dataCriacao = atual;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Funcionario)) return false;
		Funcionario that = (Funcionario) o;
		return Float.compare(that.getQtdHorasTrabalhoDia(), getQtdHorasTrabalhoDia()) == 0 &&
				Float.compare(that.getQtdHorasAlmoco(), getQtdHorasAlmoco()) == 0 &&
				Objects.equal(getId(), that.getId()) &&
				Objects.equal(getNome(), that.getNome()) &&
				Objects.equal(getEmail(), that.getEmail()) &&
				Objects.equal(getSenha(), that.getSenha()) &&
				Objects.equal(getCpf(), that.getCpf()) &&
				Objects.equal(getValorHora(), that.getValorHora()) &&
				getPerfil() == that.getPerfil() &&
				Objects.equal(getDataCriacao(), that.getDataCriacao()) &&
				Objects.equal(getDataAtualizacao(), that.getDataAtualizacao()) &&
				Objects.equal(getEmpresa(), that.getEmpresa()) &&
				Objects.equal(getLancamentos(), that.getLancamentos());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), getNome(), getEmail(), getSenha(), getCpf(), getValorHora(), getQtdHorasTrabalhoDia(), getQtdHorasAlmoco(), getPerfil(), getDataCriacao(), getDataAtualizacao(), getEmpresa(), getLancamentos());
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("nome", nome)
                .append("email", email)
                .append("senha", senha)
                .append("cpf", cpf)
                .append("valorHora", valorHora)
                .append("qtdHorasTrabalhoDia", qtdHorasTrabalhoDia)
                .append("qtdHorasAlmoco", qtdHorasAlmoco)
                .append("perfil", perfil)
                .append("dataCriacao", dataCriacao)
                .append("dataAtualizacao", dataAtualizacao)
                .append("empresa", empresa)
                .append("lancamentos", lancamentos)
                .toString();
    }
}
