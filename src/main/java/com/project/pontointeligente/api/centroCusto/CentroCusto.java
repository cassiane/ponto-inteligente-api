package com.project.pontointeligente.api.centroCusto;

import com.project.pontointeligente.api.empresa.Empresa;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "centro_custo")
public class CentroCusto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int centroCusto;
    private Empresa empresa;
    private String projeto;
    private String descricao;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
