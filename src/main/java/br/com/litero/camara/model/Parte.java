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
import javax.persistence.Table;

@Entity
@Table(name="parte")
public class Parte {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="parte_id")
	private Long parteId;
	
	@ManyToOne
	@JoinColumn(name="caso_id",referencedColumnName="caso_id")
	private Caso caso;
	
	@ManyToOne
	@JoinColumn(name="pessoa_id",referencedColumnName="pessoa_id")
	private Pessoa pessoa;
	
	@Enumerated(EnumType.STRING)
	private TipoParte tipoParte;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status_parte")
	private StatusParte statusParte = StatusParte.AGUARDANDO_CONFIRMACAO;
	
	
	@Column(name="principal")
	private boolean principal;
	
	
	public Parte() {
		
	}


	public Parte(Caso caso, Pessoa parte, TipoParte tipoParte) {
		this.caso = caso;
		this.pessoa = parte;
		this.tipoParte = tipoParte;
	}
	
	
	public Long getCasoParteId() {
		return parteId;
	}
	public void setCasoParteId(Long casoParteId) {
		this.parteId = casoParteId;
	}
	public Caso getCaso() {
		return caso;
	}
	public void setCaso(Caso caso) {
		this.caso = caso;
	}
	
	
	public boolean isAguardandoConfirmacao() {
		return this.statusParte.equals(StatusParte.AGUARDANDO_CONFIRMACAO);
	}


	public StatusParte getStatusParte() {
		return statusParte;
	}


	public void setStatusParte(StatusParte statusParte) {
		this.statusParte = statusParte;
	}


	public boolean isPrincipal() {
		return principal;
	}


	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}


	public Long getParteId() {
		return parteId;
	}


	public Pessoa getPessoa() {
		return pessoa;
	}


	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}


	public TipoParte getTipoParte() {
		return tipoParte;
	}
	
	public void setTipoParte(TipoParte tipoParte) {
		this.tipoParte = tipoParte;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parteId == null) ? 0 : parteId.hashCode());
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
		Parte other = (Parte) obj;
		if (parteId == null) {
			if (other.parteId != null)
				return false;
		} else if (!parteId.equals(other.parteId))
			return false;
		return true;
	}
	
	
	

}
