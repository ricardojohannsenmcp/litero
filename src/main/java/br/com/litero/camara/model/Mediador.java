package br.com.litero.camara.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="mediador")
public class Mediador {
	
	@Id
	@Column(name="mediador_id")
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private Long mediadorId;
	
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="pessoa_id",referencedColumnName="pessoa_id")
	private Pessoa pessoa;
	
	
	
	private boolean bloqueado;
	
	
	private String descricao;
	
	
	

	
	public Mediador() {
		
		especialidades = new HashSet<>();
	}
	
	
	@ManyToMany
	@JoinTable(name="mediador_especialidade",
	joinColumns= {@JoinColumn(name="mediador_id")},
	inverseJoinColumns= {@JoinColumn(name="especialidade_id")})
	private Set<Especialidade> especialidades;
	
	private boolean convidado;
	
	
	
	private boolean aceitou;
	

	public boolean isAceitou() {
		return aceitou;
	}
	public void setAceitou(boolean aceitou) {
		this.aceitou = aceitou;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	

	
	
	
	
	
	public boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	
	
	
	
	
	

	public Set<Especialidade> getEspecialidades() {
		return especialidades;
	}
	public void setEspecialidades(Set<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}
	public boolean isConvidado() {
		return convidado;
	}
	public void setConvidado(boolean convidado) {
		this.convidado = convidado;
	}
	
	
	
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setMediadorId(Long mediadorId) {
		this.mediadorId = mediadorId;
	}
	public Long getMediadorId() {
		return mediadorId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mediadorId == null) ? 0 : mediadorId.hashCode());
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
		Mediador other = (Mediador) obj;
		if (mediadorId == null) {
			if (other.mediadorId != null)
				return false;
		} else if (!mediadorId.equals(other.mediadorId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Mediador [mediadorId=" + mediadorId + "]";
	}


}
