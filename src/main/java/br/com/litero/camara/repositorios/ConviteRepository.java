package br.com.litero.camara.repositorios;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Convite;

@Repository
public abstract class ConviteRepository extends AbstractEntityRepository<Convite, Long>{
	
	
	
	public Convite recuperar(String token) {
		
	
			return typedQuery("select c from Convite c where c.token = :token").setParameter("token",token).getResultList().get(0);
		
	}

}
