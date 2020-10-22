package br.com.litero.camara.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.litero.camara.model.Usuario;
import br.com.litero.camara.repositorios.UsuarioRepository;

@RequestScoped
public class AutenticationService {
	
	@Inject
	private UsuarioRepository usuarioDAO;
	
	public Usuario autenticar(String login, String senha) {
		
		
		Usuario usuario = usuarioDAO.buscarPor(login, senha);
		return usuario;
		
	}

}
