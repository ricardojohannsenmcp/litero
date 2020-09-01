package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Evento;

@Repository
public abstract class EventoRepository extends AbstractEntityRepository<Evento, Long>{
	

	public List<Evento> recuperarEventosPorCaso(Long casoId){
		return typedQuery("select e from Evento e where e.caso.casoId = :casoId order by e.eventoId ").setParameter("casoId", casoId).getResultList();
	}
	
	

}
