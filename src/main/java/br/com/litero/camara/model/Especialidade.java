package br.com.litero.camara.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="especialidade")
public class Especialidade {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="especialidade_id")
	private Long especialidadeId;
	
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="ramo_id",referencedColumnName="ramo_id")
	private Ramo ramo;
	
	@NotEmpty
	private String nome;


	public Long getEspecialidadeId() {
		return especialidadeId;
	}


	public void setEspecialidadeId(Long especialidadeId) {
		this.especialidadeId = especialidadeId;
	}


	public Ramo getRamo() {
		return ramo;
	}


	public void setRamo(Ramo ramo) {
		this.ramo = ramo;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((especialidadeId == null) ? 0 : especialidadeId.hashCode());
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
		Especialidade other = (Especialidade) obj;
		if (especialidadeId == null) {
			if (other.especialidadeId != null)
				return false;
		} else if (!especialidadeId.equals(other.especialidadeId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Especialidade [especialidadeId=" + especialidadeId + "]";
	}
	
	
	

}
