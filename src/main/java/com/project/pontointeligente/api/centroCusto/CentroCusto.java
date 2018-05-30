package com.project.pontointeligente.api.centroCusto;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.project.pontointeligente.api.empresa.Empresa;
import com.project.pontointeligente.api.lancamento.Lancamento;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "centro_custo")
public class CentroCusto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private int centroCusto;
    private Empresa empresa;
    private String projeto;
    private String descricao;
    private List<Lancamento> lancamentos;

    public CentroCusto() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "centro_custo", nullable = false)
    public int getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(int centroCusto) {
        this.centroCusto = centroCusto;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Column(name = "projeto", nullable = false)
    public String getProjeto() {
        return projeto;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
    }

    @Column(name = "descricao", nullable = true)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @OneToMany(mappedBy = "centroCusto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CentroCusto)) return false;
        CentroCusto that = (CentroCusto) o;
        return getId() == that.getId() &&
                getCentroCusto() == that.getCentroCusto() &&
                Objects.equal(getEmpresa(), that.getEmpresa()) &&
                Objects.equal(getProjeto(), that.getProjeto()) &&
                Objects.equal(getDescricao(), that.getDescricao()) &&
                Objects.equal(getLancamentos(), that.getLancamentos());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getCentroCusto(), getEmpresa(), getProjeto(), getDescricao(), getLancamentos());
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("centroCusto", centroCusto)
                .add("empresa", empresa)
                .add("projeto", projeto)
                .add("descricao", descricao)
                .add("lancamentos", lancamentos)
                .toString();
    }
}
