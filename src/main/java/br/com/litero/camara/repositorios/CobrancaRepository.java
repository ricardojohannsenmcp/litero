package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Cobranca;

@Repository
public abstract class CobrancaRepository extends AbstractEntityRepository<Cobranca, Long>{
	

	
	public List<Cobranca> recuperarCobrancasPorCaso(Long casoId){
		return typedQuery("select c from Cobranca c where  c.caso.casoId  =:cId").setParameter("cId", casoId).getResultList();  
	}
	
	
	public List<Cobranca> recuperarCobrancasPorCaso(Long casoId,Long pessoaId){
		return typedQuery("select c from Cobranca c where c.pessoa.pessoaId =:pId and c.caso.casoId  =:cId").setParameter("cId", casoId).setParameter("pId", pessoaId).getResultList();  
	}
	
	public Long quantidadeDeCobrancasPorCaso(@QueryParam("cId")Long casoId) {
		return (Long) entityManager().createQuery("select count(c) from Cobranca c where c.caso.casoId  =:cId").setParameter("cId", casoId).getSingleResult();
	}
	
	

}
