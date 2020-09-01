package br.com.litero.camara.repositorios;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Arbitro;

@Repository
public abstract class ArbitroRepository extends AbstractEntityRepository<Arbitro, Long>{

	public Arbitro buscarPorCpf(String cpf) {
		try {
			return   typedQuery("select c from Arbitro c where c.pessoa.cpfCnpj =:cpf ").setParameter("cpf", cpf).getSingleResult();
		} catch (Exception e) {
			return null;
		}	
	}
	
	
}
