package br.com.litero.camara.model;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="postagem")
public class Postagem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="postagem_id")
	private Long postagemId;
	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;
	
	@ManyToOne
	@JoinColumn(name="autor_id",referencedColumnName="pessoa_id")
	private Pessoa autor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	private String texto;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_aprovacao")
	private Date dataAprovacao;
	
	@OneToMany(mappedBy="postagem",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private List<Anexo> anexos;
	
	

	public Postagem() {
		
		this(null,null);
	}

	public Postagem(Caso caso, Pessoa pessoa) {
		
		
		this.caso = caso;
		this.autor = pessoa;
		this.anexos = new ArrayList<>();
	}
	
	@PrePersist
	public void prePersist() {
		
		
		if(caso.isMediadorCaso(autor)) {
			
			this.dataAprovacao = new Date();
		}
		
		this.data = new Date();
	}
	
	
	@Transient
	public boolean isAprovado() {
		
		return this.dataAprovacao != null;
	}
	
	
	public void aprovar(Pessoa pessoa) {
		
		if(pessoa == null) {
			
			throw new RuntimeException("informe a pessoa");
		}
		
	/*	if(!pessoa.isMediadorCaso(this.caso)) {
			
			throw new RuntimeException("apenas o mediador pode aprovar mensagens");
		}*/
		this.dataAprovacao = new Date();
		
	}
	

	public Long getPostagemId() {
		return postagemId;
	}

	public void setPostagemId(Long postagemId) {
		this.postagemId = postagemId;
	}

	public Caso getCaso() {
		return caso;
	}

	public void setCaso(Caso caso) {
		this.caso = caso;
	}

	public Pessoa getAutor() {
		return autor;
	}

	public void setAutor(Pessoa autor) {
		this.autor = autor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}
	
	
	
	

}
