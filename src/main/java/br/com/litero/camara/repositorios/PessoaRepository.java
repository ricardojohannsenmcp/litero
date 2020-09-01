package br.com.litero.camara.repositorios;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

import br.com.litero.camara.model.Pessoa;

@Repository
public abstract class PessoaRepository  extends AbstractEntityRepository<Pessoa, Long> implements CriteriaSupport<Pessoa>{
	
	
	
	public List<Pessoa> buscarPorNomeCpfCnpj(String nomeCpfCnpj){
		return typedQuery("select p from Pessoa p where p.nome like :nome or p.cpfCnpj like :cpfCnpj order by p.nome")
	    .setParameter("nome", "%"+nomeCpfCnpj+"%")
	    .setParameter("cpfCnpj", nomeCpfCnpj+"%")
	    .setMaxResults(10).getResultList();
	   
	}
	
	
	
	public Pessoa buscarPorNomeCpfCnpjIgual(String nomeCpfCnpj){
		try {
			return typedQuery("select p from Pessoa p where p.cpfCnpj =:nomeCpfCnpj ").setParameter("nomeCpfCnpj", nomeCpfCnpj).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}




	

}
