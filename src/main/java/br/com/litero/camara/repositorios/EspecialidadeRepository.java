package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Especialidade;

@Repository
public abstract class EspecialidadeRepository extends AbstractEntityRepository<Especialidade, Long> {
	
	
public List<Especialidade> buscarPorRamo(Long ramoId){
		
		return typedQuery("select e from Especialidade e where e.ramo.ramoId =:ramoId order by e.nome").setParameter("ramoId", ramoId).getResultList();
	}

}
