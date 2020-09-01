package br.com.litero.camara.model;

import java.util.Date;

public class NotificacaoDTO {
	
	private Long notificacaoId;
	private Date data;
	private String texto;
	private Long casoId;
	private Long pessoaId;
	private String nome;
	
	public NotificacaoDTO(Long notificacaoId, Date data, String texto, Long casoId, Long pessoaId, String nome) {
		this.notificacaoId = notificacaoId;
		this.data = data;
		this.texto = texto;
		this.casoId = casoId;
		this.pessoaId = pessoaId;
		this.nome = nome;
	}

	public Long getNotificacaoId() {
		return notificacaoId;
	}

	public void setNotificacaoId(Long notificacaoId) {
		this.notificacaoId = notificacaoId;
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

	public Long getCasoId() {
		return casoId;
	}

	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}

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
	

}
