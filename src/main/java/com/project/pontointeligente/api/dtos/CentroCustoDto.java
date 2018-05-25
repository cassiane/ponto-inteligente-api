package com.project.pontointeligente.api.dtos;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.project.pontointeligente.api.entities.Empresa;

public class CentroCustoDto {

    private int id;
    private int centroCusto;
    private Empresa empresa;
    private String projeto;
    private String descricao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(int centroCusto) {
        this.centroCusto = centroCusto;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getProjeto() {
        return projeto;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CentroCustoDto)) return false;
        CentroCustoDto that = (CentroCustoDto) o;
        return getId() == that.getId() &&
                getCentroCusto() == that.getCentroCusto() &&
                Objects.equal(getEmpresa(), that.getEmpresa()) &&
                Objects.equal(getProjeto(), that.getProjeto()) &&
                Objects.equal(getDescricao(), that.getDescricao());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getCentroCusto(), getEmpresa(), getProjeto(), getDescricao());
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("centroCusto", centroCusto)
                .add("empresa", empresa)
                .add("projeto", projeto)
                .add("descricao", descricao)
                .toString();
    }
}
