package br.com.litero.camara.model;

import java.text.DateFormat;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.primefaces.json.JSONObject;

import br.com.litero.camara.util.json.JsonMessageType;
import br.com.litero.camara.util.json.Jsonify;

@Entity
@Table(name="mensagem")
public class Mensagem implements Jsonify{
	
	@Id
	@Column(name="mensagem_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long mensagemId;
	
	@NotEmpty
	@Length(max=2000)
	private String texto;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="pessoa_id",referencedColumnName="pessoa_id")
	private Pessoa pessoa;
	
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	
	private String anexo;
	
	@PrePersist
	public void prePersist() {
		
		this.data = new Date();
	}
	
	public Mensagem() {}
	

	public Mensagem(Caso caso,Pessoa pessoa,String texto) {
		
		this.caso = caso;
		this.pessoa =  pessoa;
		this.texto = texto;
	}
	
	
	
	

	public Mensagem(Caso caso,Pessoa pessoa, String texto,  String anexo) {
		
	    this.caso = caso;
		this.pessoa = pessoa;
		this.anexo = anexo;
		this.texto = texto;
	}

	public Mensagem(Long mensagemId,Date data, String texto,  String anexo) {
		this.mensagemId = mensagemId;
		this.texto = texto;
		this.data = data;
		this.anexo = anexo;
	}

	public Long getMensagemId() {
		return mensagemId;
	}

	public void setMensagemId(Long mensagemId) {
		this.mensagemId = mensagemId;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}


	
	
	
	

	public Caso getCaso() {
		return caso;
	}

	public void setCaso(Caso caso) {
		this.caso = caso;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Date getData() {
		return data;
	}
	
	
	public void adicionarAnexo(String anexo) {
		
		this.anexo = anexo;
	}

	
	public String getAnexo() {
		return anexo;
	}

	

	@Transient
	public String getCorMensagem(Long pessoaId) {
		return this.pessoa.getPessoaId().equals(pessoaId)? "balao":"balao2";
	}


	public JSONObject toJson() {
		
		JSONObject json = new JSONObject();
		json.put("texto", this.texto);
		json.put("usuario", this.pessoa.getNome());
		json.put("data", DateFormat.getDateTimeInstance().format(this.data));
		json.put("casoId", this.caso.getCasoId());
		json.put("estilo", "balao");
		if(this.anexo != null) {
			json.put("anexo", this.getAnexo());
			json.put("type", JsonMessageType.ANEXO);
		}else {
			json.put("type", JsonMessageType.MENSSAGEM);

		}
		return json;
	}
	
	
	

}
