package br.com.litero.camara.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="anexo")
public class Anexo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="anexo_id")
	private Long anexoId;
	private String caminho;
	private String nome;
	@ManyToOne
	@JoinColumn(name="postagem_id",referencedColumnName="postagem_id")
	private Postagem postagem;
	
	
	
	
	public Anexo() {
	
	}


	public Anexo(Postagem postagem, String caminho) {
		
		this.postagem =  postagem;
		this.caminho = caminho;
	}
	
	
	public Long getAnexoId() {
		return anexoId;
	}
	public void setAnexoId(Long anexoId) {
		this.anexoId = anexoId;
	}
	public String getCaminho() {
		return caminho;
	}
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Postagem getPostagem() {
		return postagem;
	}
	public void setPostagem(Postagem postagem) {
		this.postagem = postagem;
	}
	
	
	
	
	
	
	

}
