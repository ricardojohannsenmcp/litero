package br.com.litero.camara.model;

import java.util.Date;

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

import br.com.litero.camara.util.json.Jsonify;

@Entity
@Table(name="notificacao")
public class Notificacao implements  Jsonify{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long notificacaoId;
	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;
	@ManyToOne
	@JoinColumn(name="pessoa_id")
	private Pessoa pessoa;
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	private String texto;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAbertura;
	
	public Notificacao() {}

	public Notificacao(Caso caso, Pessoa pessoa, String texto) {
		this.caso = caso;
		this.pessoa = pessoa;
		this.texto = texto;
	}

	@PrePersist
	public void prePersist() {
		this.data = new Date();
	}
	
	public void abrir() {
		this.dataAbertura =  new Date();
	}
	
	public Long getNotificacaoId() {
		return notificacaoId;
	}

	public Caso getCaso() {
		return caso;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public Date getData() {
		return data;
	}

	public String getTexto() {
		return texto;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		return json;

	}
	
}
