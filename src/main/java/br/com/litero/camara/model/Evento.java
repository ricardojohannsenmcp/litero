package br.com.litero.camara.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="evento")
public class Evento {
	
	
	@Id
	@Column(name="evento_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eventoId;
	
	
	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;
	
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_evento")
	private TipoEvento tipoEvento;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@ManyToOne
	@JoinColumn(name="usuario_id",referencedColumnName="usuario_id")
	private Usuario usuario;

	public Evento() {
		
	}

	public Evento(Caso caso, TipoEvento tipoEvento,String descricao, Usuario usuario) {
		
		this.caso =  caso;
		this.descricao = descricao;
		this.tipoEvento = tipoEvento;
		this.usuario = usuario;
	}
	
	

	
	
	@PrePersist
	public void prePersist() {
		
		this.data =  new Date();
	}
	
	

	public Date getData() {
		return data;
	}

	public Long getEventoId() {
		return eventoId;
	}

	public String getDescricao() {
		return descricao;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public Caso getCaso() {
		return caso;
	}

	public Usuario getUsuario() {
		return usuario;
	}

}
