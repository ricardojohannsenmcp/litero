package br.com.litero.camara.repositorios;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Mediador;

@Repository
public abstract class MediadorRepository extends AbstractEntityRepository<Mediador, Long>{

	public Mediador buscarPorCpf(String cpf) {
		try {
			return typedQuery("select m from Mediador m where m.pessoa.cpfCnpj =:cpf ").setParameter("cpf", cpf).getSingleResult();
		} catch (Exception e) {
		return null;
		}
	}
	
	public List<Mediador> buscarMediadoresPorCaso(Long casoId){
		return typedQuery("select m from Caso c join c.mediadores m where c.casoId =:casoId ").setParameter("casoId", casoId).getResultList();
	}
	
	
	public Mediador buscarMediadorPorPessoa(Long pessoaId){
		try {
			return typedQuery("select m from Mediador m where m.pessoa.pessoaId =:pessoaId ").setParameter("pessoaId", pessoaId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
