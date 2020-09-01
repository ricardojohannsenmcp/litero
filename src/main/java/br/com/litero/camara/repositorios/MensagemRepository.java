package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Mensagem;

@Repository
public abstract class MensagemRepository extends AbstractEntityRepository<Mensagem, Long>{
	
	
	public List<Mensagem>recuperarMensagensPorCaso(Long casoId){
		return typedQuery("select m from Mensagem m  where m.caso.casoId  =:casoId order by m.data").setParameter("casoId", casoId).getResultList(); 
	}
	
	
	public List<Mensagem> recuperarMensagensComAnexoPorCaso(Long casoId){
		return typedQuery("select new br.com.litero.camara.model.Mensagem(m.mensagemId,m.data,texto,m.anexo) from Mensagem m where m.anexo is not null and  m.caso.casoId =:casoId")
				.setParameter("casoId",casoId).getResultList();  
	}

}
