package br.com.litero.camara.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="mediador_caso")
public class MediadorCaso {
	
	@Id
	@Column(name="mediador_caso_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long mediadorCasoId;
	
	@ManyToOne
	@JoinColumn(name="mediador_id",referencedColumnName="mediador_id")
	private Mediador mediador;
	
	@OneToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;
	
	@Enumerated(EnumType.STRING)
	private StatusMediador statusMediador;
	
	
	
	
	

	public MediadorCaso() {
	
	}

	public MediadorCaso(Mediador mediador, Caso caso, StatusMediador statusMediador) {
		
		this.mediador = mediador;
		this.caso = caso;
		this.statusMediador = statusMediador;
	}

	public Long getMediadorCasoId() {
		return mediadorCasoId;
	}

	public void setMediadorCasoId(Long mediadorCasoId) {
		this.mediadorCasoId = mediadorCasoId;
	}

	public Mediador getMediador() {
		return mediador;
	}

	public void setMediador(Mediador mediador) {
		this.mediador = mediador;
	}

	public Caso getCaso() {
		return caso;
	}

	public void setCaso(Caso caso) {
		this.caso = caso;
	}

	public StatusMediador getStatusMediador() {
		return statusMediador;
	}

	public void setStatusMediador(StatusMediador statusMediador) {
		this.statusMediador = statusMediador;
	}


	
	
	
	
	
	

}
