package br.com.litero.camara.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="area")
public class Area {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="area_id")
	private Long areaId;
	
	@NotEmpty
	private String nome;
	
/*	@OneToMany(mappedBy="area")
	private Collection<Ramo> ramos;*/

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
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
		result = prime * result + ((areaId == null) ? 0 : areaId.hashCode());
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
		Area other = (Area) obj;
		if (areaId == null) {
			if (other.areaId != null)
				return false;
		} else if (!areaId.equals(other.areaId))
			return false;
		return true;
	}
	
	
	
	
	/*

	public Collection<Ramo> getRamos() {
		return ramos;
	}

	public void setRamos(Collection<Ramo> ramos) {
		this.ramos = ramos;
	}
*/
	@Override
	public String toString() {
		return "Area [areaId=" + areaId + "]";
	}
	
	

}
