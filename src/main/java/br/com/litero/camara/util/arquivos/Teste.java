package br.com.litero.camara.util.arquivos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.litero.camara.model.Caso;

public class Teste {

	public static void main(String[] args) {
		
		
		
		
		EntityManagerFactory factory =  Persistence.createEntityManagerFactory("camaraPU");
		
		EntityManager em = factory.createEntityManager();
		
		
		
		String hql = "select distinct  c  from Caso c left  join fetch c.partes  pa  join  pa.pessoa p  where p  ";

		Query q =  em.createQuery(hql);
		
		
		List<Caso> casos = q.getResultList();
		
		em.close();
		
		casos.forEach(c->{
			
			System.out.println("Caso: "+ c.getCasoId());
			
			c.getPartes().forEach(p ->{
				System.out.println("parte-> "+p.getPessoa().getNome());
			});
		});
		
	

	}

}
