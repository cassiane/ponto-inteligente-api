package com.project.pontointeligente.api.entities;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.project.pontointeligente.api.enums.OperacaoEnum;
import com.project.pontointeligente.api.enums.TipoEnum;
import com.project.pontointeligente.api.utils.HashUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "lancamento")
public class Lancamento implements Serializable {

	private static final long serialVersionUID = 236188582712441484L;

	private Long id;
	private Date data;
	private String descricao;
	private String localizacao;
	private Date dataCriacao;
	private Date dataAtualizacao;
	private TipoEnum tipo;
	private Funcionario funcionario;
    private String hash;
    private String previousHash;
    private OperacaoEnum operacao;
    private int nonce;
    private Long idLancamentoAlterado;
    private String hashAssinaturaEmpresa;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "data", nullable = false)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	@Column(name = "descricao", nullable = true)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "localizacao", nullable = true)
	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
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

    @Column(name = "hash_assinatura_empresa", nullable = false)
    public String getHashAssinaturaEmpresa() {
        return hashAssinaturaEmpresa;
    }

    public void setHashAssinaturaEmpresa(String hashAssinaturaEmpresa) {
        this.hashAssinaturaEmpresa = hashAssinaturaEmpresa;
    }

    public Lancamento() {
        final Date atual = new Date();
        dataAtualizacao = atual;
        dataCriacao = atual;
    }

    //    @PreUpdate
//	public void preUpdate(){
//		dataAtualizacao = new Date();
//		operacao = OperacaoEnum.ALTERACAO;
//	}

//	@PrePersist
//	public void prePersist(){\
//        final Date atual = new Date();
//        dataAtualizacao = atual;
//        dataCriacao = atual;
//	}

    public String calculateHash() {
        String calculatedhash = HashUtils.applySha256(
                previousHash +
                        dataCriacao.toString() +
                        dataAtualizacao.toString() +
                        data.toString() +
                        funcionario.getCpf() +
                        tipo.toString() +
                        operacao.toString() +
                        Integer.toString(nonce)
        );
        return calculatedhash;
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        String target = HashUtils.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lancamento)) return false;
        Lancamento that = (Lancamento) o;
        return nonce == that.nonce &&
                Objects.equal(getId(), that.getId()) &&
                Objects.equal(getData(), that.getData()) &&
                Objects.equal(getDescricao(), that.getDescricao()) &&
                Objects.equal(getLocalizacao(), that.getLocalizacao()) &&
                Objects.equal(getDataCriacao(), that.getDataCriacao()) &&
                Objects.equal(getDataAtualizacao(), that.getDataAtualizacao()) &&
                getTipo() == that.getTipo() &&
                Objects.equal(getFuncionario(), that.getFuncionario()) &&
                Objects.equal(getHash(), that.getHash()) &&
                Objects.equal(getPreviousHash(), that.getPreviousHash()) &&
                getOperacao() == that.getOperacao() &&
                Objects.equal(getIdLancamentoAlterado(), that.getIdLancamentoAlterado()) &&
                Objects.equal(getHashAssinaturaEmpresa(), that.getHashAssinaturaEmpresa());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getData(), getDescricao(), getLocalizacao(), getDataCriacao(), getDataAtualizacao(), getTipo(), getFuncionario(), getHash(), getPreviousHash(), getOperacao(), nonce, getIdLancamentoAlterado(), getHashAssinaturaEmpresa());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("data", data)
                .add("descricao", descricao)
                .add("localizacao", localizacao)
                .add("dataCriacao", dataCriacao)
                .add("dataAtualizacao", dataAtualizacao)
                .add("tipo", tipo)
                .add("funcionario", funcionario)
                .add("hash", hash)
                .add("previousHash", previousHash)
                .add("operacao", operacao)
                .add("nonce", nonce)
                .add("idLancamentoAlterado", idLancamentoAlterado)
                .add("hashAssinaturaEmpresa", hashAssinaturaEmpresa)
                .toString();
    }


}
