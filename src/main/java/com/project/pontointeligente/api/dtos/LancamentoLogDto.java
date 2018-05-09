package com.project.pontointeligente.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.project.pontointeligente.api.enums.OperacaoEnum;

import java.time.LocalDateTime;
import java.util.Optional;

public class LancamentoLogDto {

	private Optional<Long> id = Optional.empty();
	private LocalDateTime data;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;
	private String tipo;
	private String descricao;
	private Long funcionarioId;
	private String hash;
	private String operacao;

	public LancamentoLogDto() {
	}

	public Optional<Long> getId() {
		return id;
	}

	public void setId(Optional<Long> optional) {
		this.id = optional;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getFuncionarioId() {
		return funcionarioId;
	}

	public void setFuncionarioId(Long funcionarioId) {
		this.funcionarioId = funcionarioId;
	}

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LancamentoLogDto that = (LancamentoLogDto) o;
		return Objects.equal(id, that.id) &&
				Objects.equal(data, that.data) &&
				Objects.equal(dataCriacao, that.dataCriacao) &&
				Objects.equal(dataAtualizacao, that.dataAtualizacao) &&
				Objects.equal(tipo, that.tipo) &&
				Objects.equal(descricao, that.descricao) &&
				Objects.equal(funcionarioId, that.funcionarioId) &&
				Objects.equal(hash, that.hash) &&
				Objects.equal(operacao, that.operacao);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, data, dataCriacao, dataAtualizacao, tipo, descricao, funcionarioId, hash, operacao);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("data", data)
				.add("dataCriacao", dataCriacao)
				.add("dataAtualizacao", dataAtualizacao)
				.add("tipo", tipo)
				.add("descricao", descricao)
				.add("funcionarioId", funcionarioId)
				.add("hash", hash)
				.add("operacao", operacao)
				.toString();
	}
}
