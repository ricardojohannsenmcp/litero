package br.com.litero.camara.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;



@Entity
@Table(name="pessoa")
public class Pessoa implements Serializable{


	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pessoa_id")
	private Long pessoaId;


	@OneToOne(mappedBy="pessoa")
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	private EstadoCivil estadoCivil;

	@Enumerated(EnumType.STRING)
	private TipoPessoa tipoPessoa;

	@NotEmpty
	private String nome;

	
	@Email
	private String email;

	
	@Column(name="cpf_cnpj")
	private String cpfCnpj;

	private String registro;
	private String orgaoEmissor;

	@Past
	@Temporal(TemporalType.DATE)
	@Column(name="data_emissao")
	private Date dataEmissao;

	private String profissao;

	private String logradouro;

	private String telefone;

	private String cep;

	private String bairro;

	private String complemento;

	private String uf;

	private String cidade;
	
	@Column(name="informacao_adicional")
	private String informacaoAdicional;

	@Past
	@Temporal(TemporalType.DATE)
	@Column(name="data_nascimento")
	private Date dataNascimento;

	@Transient
	private String senha;

	@Transient
	public String getRotuloCpfCnpj() {


		try {
			return this.tipoPessoa.equals(TipoPessoa.FISICA) ? "CPF" : "CNPJ";
		} catch (Exception e) {
			return "";
		}
	}




	public Pessoa() {

		this.tipoPessoa = TipoPessoa.FISICA;
	}





	public Date getDataNascimento() {
		return dataNascimento;
	}




	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	@Transient
	public boolean isMediadorCaso(Caso caso) {

		if(caso == null) {
			throw new RuntimeException("caso é nulo");
		}else {
			if(caso.getMediador() == null) {
				throw new RuntimeException("mediador do caso não foi definido");
			}
			return this.equals(caso.getMediador().getPessoa());
		}
	}

	@Transient
	public boolean isRequerentePrincipalCaso(Caso caso) {

		if(caso == null) {
			throw new RuntimeException("caso é nulo");
		}

		for(Parte p:caso.getRequerentes()) {
			if(p.getPessoa().equals(this) && p.isPrincipal()){
				return true;
			}
		}
		return false;

	}
	
	@Transient
	public boolean isRequerido(Caso caso) {

		if(caso == null) {
			throw new RuntimeException("caso é nulo");
		}

		for(Parte p:caso.getRequeridos()) {
			if(p.getPessoa().equals(this)){
				return true;
			}
		}
		return false;

	}




	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getProfissao() {
		return profissao;
	}
	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	@Column(name="numero_logradouro")
	private String  numeroLogradouro;







	public Long getPessoaId() {
		return pessoaId;
	}




	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}




	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getNumeroLogradouro() {
		return numeroLogradouro;
	}
	public void setNumeroLogradouro(String numeroLogradouro) {
		this.numeroLogradouro = numeroLogradouro;
	}



	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}
	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	public void completarEndereco(Endereco endereco) {
		if(endereco!=null) {
			this.logradouro = endereco.getLogradouro();
			this.bairro =  endereco.getBairro();
			this.complemento =  endereco.getComplemento();
			this.cidade =  endereco.getCidade();
			this.uf = endereco.getUf();
		}
	}





	public String getRegistro() {
		return registro;
	}




	public void setRegistro(String registro) {
		this.registro = registro;
	}




	public String getOrgaoEmissor() {
		return orgaoEmissor;
	}




	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}




	public Date getDataEmissao() {
		return dataEmissao;
	}




	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	



	public String getInformacaoAdicional() {
		return informacaoAdicional;
	}




	public void setInformacaoAdicional(String informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
	}




	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Transient
	public String getPrimeiroNome() {
		if(getNome()!=null) {
			String[] split = getNome().split(" ");
			return split[0];
		}
		return "";
	}


	@Override
	public String toString() {
		return this.pessoaId.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result + ((cpfCnpj == null) ? 0 : cpfCnpj.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((logradouro == null) ? 0 : logradouro.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((numeroLogradouro == null) ? 0 : numeroLogradouro.hashCode());
		result = prime * result + ((pessoaId == null) ? 0 : pessoaId.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (cpfCnpj == null) {
			if (other.cpfCnpj != null)
				return false;
		} else if (!cpfCnpj.equals(other.cpfCnpj))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (logradouro == null) {
			if (other.logradouro != null)
				return false;
		} else if (!logradouro.equals(other.logradouro))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numeroLogradouro == null) {
			if (other.numeroLogradouro != null)
				return false;
		} else if (!numeroLogradouro.equals(other.numeroLogradouro))
			return false;
		if (pessoaId == null) {
			if (other.pessoaId != null)
				return false;
		} else if (!pessoaId.equals(other.pessoaId))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}





}
