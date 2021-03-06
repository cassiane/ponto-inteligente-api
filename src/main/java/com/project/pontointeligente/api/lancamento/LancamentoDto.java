package com.project.pontointeligente.api.lancamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDateTime;
import java.util.Optional;

public class LancamentoDto {

	private Optional<Long> id = Optional.empty();
	private LocalDateTime data;
	private String tipo;
	private String descricao;
	private Long funcionarioId;
	private String hash;
	private Long centroCusto;

	public LancamentoDto() {
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

    private String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

	public Long getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(Long centroCusto) {
		this.centroCusto = centroCusto;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LancamentoDto)) return false;
        LancamentoDto that = (LancamentoDto) o;
        return Objects.equal(getId(), that.getId()) &&
                Objects.equal(getData(), that.getData()) &&
                Objects.equal(getTipo(), that.getTipo()) &&
                Objects.equal(getDescricao(), that.getDescricao()) &&
                Objects.equal(getFuncionarioId(), that.getFuncionarioId()) &&
                Objects.equal(getHash(), that.getHash()) &&
				Objects.equal(getCentroCusto(), that.getCentroCusto());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getData(), getTipo(), getDescricao(), getFuncionarioId(), getHash(), getCentroCusto());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("data", data)
                .add("tipo", tipo)
                .add("descricao", descricao)
                .add("funcionarioId", funcionarioId)
                .add("hash", hash)
				.add("centroCusto", centroCusto)
                .toString();
    }
}
