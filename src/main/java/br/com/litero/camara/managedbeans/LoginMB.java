package br.com.litero.camara.managedbeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.model.Usuario;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.AutenticationService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@RequestScoped
public class LoginMB {
	
	private String login;
	private String senha;
	
	@Inject
	private AutenticationService authService;
	
	@Inject
	private UsuarioWeb usuarioWeb;
	
	@Inject
	private FacesMessages  messages;
	
	
	
	public String logar() {
		
		Usuario usuario = authService.autenticar(login, senha);
		
		
		if (usuario != null) {
			
			usuarioWeb.loga(usuario);
			return "/Casos?faces-redirect=true"; 
		}
		

		messages.error("Login ou senha inválida.");
		return null;
	}
	
	public String sair() {
		usuarioWeb.logout();
		return "login";
	}
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	

}
