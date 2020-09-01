package br.com.litero.camara.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.litero.camara.exceptions.NegocioException;
import br.com.litero.camara.model.Usuario;
import br.com.litero.camara.repositorios.UsuarioRepository;

@ApplicationScoped
public class UsuarioService {
	
	@Inject
	private UsuarioRepository usuarioRepository;
	
	
	@Transactional
	public Usuario adiciona(Usuario usuario) {
		
		
		Usuario user = usuarioRepository.buscarPorLogin(usuario.getLogin());
		
		if(user != null && user.equals(usuario)) {
			
			throw new NegocioException("Este login já está em uso por outro usuário");
		}
		
		
		Usuario userInDatabase = usuarioRepository.buscarUsuarioPorPessoa(usuario.getPessoa());
		if(userInDatabase != null && !userInDatabase.equals(usuario)) {
			throw new NegocioException("A pessoa selecionada já possui um usuário cadastrado na base de dados");
		}
		
		
		 usuarioRepository.save(usuario);
		 return usuario;
	}
	
	@Transactional
	public Usuario editar(Usuario usuario) {
		Usuario usuarioOriginal =  usuarioRepository.findBy(usuario.getUsuarioId());
		usuarioOriginal.setTipoUsuario(usuario.getTipoUsuario());
		usuarioOriginal.setBloqueado(usuario.isBloqueado());
		usuarioRepository.save(usuario);
		usuario = null;
		return usuarioOriginal;	
	}
	
	
	public Usuario recuperar(Long id) {
		return usuarioRepository.findBy(id);
	}
	
	
	@Transactional
	public void removerUsuario(Usuario usuario) {
			usuarioRepository.remove(usuarioRepository.save(usuario));
	}
	
	public List<Usuario> recuperarTodos(){
		return usuarioRepository.findAll();
	}

}
