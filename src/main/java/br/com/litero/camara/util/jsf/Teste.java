package br.com.litero.camara.util.jsf;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.StatusMediador;

public class Teste {
	
	
	public static void main(String[] args) {
		
		
		
		
		
		List<Caso> casos = getCasos();
		
		
		System.out.println("tamanho: "+casos.size());
		
		casos.forEach(c->{
			
			c.getPartes().forEach(p->{
				
				System.out.println(p.getPessoa().getNome());
			});
		});
		
		
		
		
	}
	
	
	public static List<Caso> getCasos(){
		
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("camaraPU");
		EntityManager manager = factory.createEntityManager();
		
		
		
		String query = "select distinct c from Caso c left join fetch c.partes pa"
				+ " join fetch pa.pessoa p0"
				+ " left join fetch c.mediador m "
				+ " left join c.mediadorCaso mc "
				+ " left join mc.mediador m0"
				+ " left join m0.pessoa p0"
				+ " join fetch m.pessoa p1"
				+ " where c.mediador.pessoa.pessoaId =:pId    "
				+ " or ( p0.pessoaId =:pId and mc.statusMediador =:sts )  ";
		
		
		Query q = manager.createQuery(query) ;
		q.setParameter("pId", 11L);
		q.setParameter("sts",StatusMediador.AGUARDANDO_APROVACAO);
		
		
		
		List<Caso> casos = q.getResultList();
		
		manager.close();
		
		return casos;
		
	}

}
