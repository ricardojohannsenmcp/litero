package br.com.litero.camara.repositorios;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Notificacao;

@Repository
public abstract class NotificacaoRepository  extends AbstractEntityRepository<Notificacao, Long>{
	
	
	public List<Notificacao> buscarNotificacoesNaoLidas(Long pessoaId){
		return typedQuery("select n from Notificacao n where n.pessoa.pessoaId =:pessoaId and n.dataAbertura is null").setParameter("pessoaId", pessoaId).getResultList();
	}
	
	
	public long quantidadeNotificacoesNaoLidas(Long pessoaId){
		return (long) entityManager().createQuery("select count(n) from Notificacao n where n.pessoa.pessoaId =:pessoaId and n.dataAbertura is null").setParameter("pessoaId",pessoaId ).getSingleResult();
	}
	
	

	

}
