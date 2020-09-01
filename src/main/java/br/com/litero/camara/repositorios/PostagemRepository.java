package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Postagem;


@Repository
public abstract class PostagemRepository extends AbstractEntityRepository<Postagem, Long> {
	


	public List<Postagem> buscarTodosPorCaso(Long casoId){
		return typedQuery("select  p from Postagem p  where p.caso.casoId =:id").setParameter("id", casoId).getResultList();
	}


/*	public List<Postagem> buscarPostagensVisiveis(Long casoId,Long autorId){

		TypedQuery<Postagem> q = em
				.createQuery("select p from Postagem p  where p.caso.casoId =:id and (p.dataAprovacao is not null or p.autor.pessoaId = :pId )",
						Postagem.class).setParameter("id", casoId);

		return q.getResultList();
	}*/

}
