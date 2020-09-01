package br.com.litero.camara.repositorios;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.Usuario;

@Repository
public abstract class UsuarioRepository extends AbstractEntityRepository<Usuario, Long> {


	
	
	
	public Usuario buscarPor(String login,String senha) {
		try {			
			
			
			
			
			return typedQuery("select u from Usuario u where u.login = :login and u.senha = :senha")
					.setParameter("login", login).setParameter("senha", senha).getSingleResult();
		} catch (Exception e) {
			
			
			e.printStackTrace();
			
			
				return null;
		}
 	}
	
	
	public Usuario buscarPorLogin(String login) {
		try {			
			return typedQuery("select u from Usuario u where u.login = :login")
					.setParameter("login", login).getSingleResult();
		} catch (Exception e) {
				return null;
		}
	}
	
	
	
	public Usuario buscarUsuarioPorPessoa(Pessoa pessoa) {
		try {
			return typedQuery("select u from Usuario u where u.pessoa = :pessoa ").setParameter("pessoa", pessoa).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	
}
