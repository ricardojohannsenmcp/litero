package br.com.litero.camara.model;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.primefaces.json.JSONObject;

import br.com.litero.camara.util.json.JsonMessageType;
import br.com.litero.camara.util.json.Jsonify;

@Entity
@Table(name="proposta")
public class Proposta implements Jsonify{

	@Id
	@Column(name="proposta_id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long propostaId;

	private String descricao;

	@ManyToOne
	@JoinColumn(name="pessoa_id",referencedColumnName="pessoa_id")
	private Pessoa pessoa;


	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;


	private BigDecimal valor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	
	
	public Proposta() {
	
	}


	public Proposta(Caso caso, Pessoa pessoa) {
		
		this.pessoa = pessoa;
		this.caso = caso;
	}


	@Transient
	public String getProponente() {

		return this.pessoa.getNome();
	}
	
	
	@Transient
	public boolean isPropostaMediador() {
		
		//TODO return this.mediador != null;
		
		
		return false;
	}


	public Long getPropostaId() {
		return propostaId;
	}


	public void setPropostaId(Long propostaId) {
		this.propostaId = propostaId;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}




	public Pessoa getPessoa() {
		return pessoa;
	}


	public Caso getCaso() {
		return caso;
	}


	public BigDecimal getValor() {
		return valor;
	}


	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	@PrePersist
	public void prePersist() {
		
		this.data =  new Date();
	}


	@Override
	public JSONObject toJson() {
		
		JSONObject json = new JSONObject();
		json.put("type", JsonMessageType.PROPOSTA);
		return json;
	}


	public Date getData() {
		return data;
	}








}
