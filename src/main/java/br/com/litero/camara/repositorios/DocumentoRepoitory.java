package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Documento;

@Repository
public abstract class DocumentoRepoitory extends AbstractEntityRepository<Documento, Long>{
	

	
	public List<Documento> recuperarDocumentosPorCaso(Long casoId){
		return typedQuery("select d from Documento d where d.caso.casoId =:casoId").setParameter("casoId",casoId).getResultList();
	}
	
	
	public List<Documento> recuperarDocumentosPublicadosPorCaso(Long casoId){
		return typedQuery("select d from Documento d where d.dataPublicacao is null and d.caso.casoId =:casoId").setParameter("casoId",casoId).getResultList();
	}

}
