package br.com.litero.camara.repositorios;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.ConviteMediador;

@Repository
public abstract class ConviteMediadorRepository extends AbstractEntityRepository<ConviteMediador, Long> {
	
	
	
	public ConviteMediador recuperar(String token) {
		
		
		return typedQuery("select c from Convite c where c.token = :token").setParameter("token",token).getResultList().get(0);
	
}


}
