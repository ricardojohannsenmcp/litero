package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Ramo;

@Repository
public  abstract class RamoRepository extends AbstractEntityRepository<Ramo, Long> {
	
	
	
	public List<Ramo> buscarPorArea(Long areaId){
		
		return typedQuery("select r from Ramo r where r.area.areaId =:areaId order by r.nome").setParameter("areaId", areaId).getResultList();
	}

}
