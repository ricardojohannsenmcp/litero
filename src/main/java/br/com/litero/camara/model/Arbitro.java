package br.com.litero.camara.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.NotEmpty;




@Entity
@Table(name="arbitro")
public class Arbitro {
	
	@Id
	@Column(name="arbitro_id")
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private Long arbitroId;
	
	
	
	@ManyToOne
	@JoinColumn(name="pessoa_id",referencedColumnName="pessoa_id")
	private Pessoa pessoa;
	
	
	
	private String descricao;
	

	
	@ManyToMany
	@JoinTable(name="arbitro_especialidade",
	joinColumns= {@JoinColumn(name="arbitro_id")},
	inverseJoinColumns= {@JoinColumn(name="especialidade_id")})
	private Set<Especialidade> especialidades;
	
	private boolean convidado;
	
	
	
	
	
	
	
	
	
	public Arbitro() {
		especialidades =  new HashSet<>();
	}
	
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
	
	
	
	
	public void setArbitroId(Long arbitroId) {
		this.arbitroId = arbitroId;
	}
	public Long getArbitroId() {
		return arbitroId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arbitroId == null) ? 0 : arbitroId.hashCode());
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
		Arbitro other = (Arbitro) obj;
		if (arbitroId == null) {
			if (other.arbitroId != null)
				return false;
		} else if (!arbitroId.equals(other.arbitroId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Arbitro [arbitroId=" + arbitroId + "]";
	}
	
	
	
	

}