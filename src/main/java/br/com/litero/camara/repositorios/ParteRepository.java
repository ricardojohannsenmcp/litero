package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Parte;
import br.com.litero.camara.model.TipoParte;

@Repository
public abstract class ParteRepository  extends AbstractEntityRepository<Parte, Long>{
	
public List<Parte> buscarPartesCaso(Long casoId, TipoParte tipoParte){
	return typedQuery("select cp from Parte cp where cp.caso.casoId =:casoId and cp.tipoParte = :tipo")
			.setParameter("casoId",casoId )
			.setParameter("tipo",tipoParte)
			.getResultList();
	}
	
	public long recuperarQuantidadePorTipoParte(Enum<TipoParte> tipoParte) {
		return (long) entityManager().createQuery("select COUNT(p) from Parte p where p.tipoParte like :tipoParte").setParameter("tipoParte", tipoParte).getSingleResult();
				
	}
	
	
	

}
