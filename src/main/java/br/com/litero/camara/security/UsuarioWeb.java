package br.com.litero.camara.security;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.litero.camara.model.TipoUsuario;
import br.com.litero.camara.model.Usuario;

@Named
@SessionScoped
public class UsuarioWeb implements Serializable{

	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private long qtdMensagens = 0;

	public void loga(Usuario usuario) {
		this.usuario = usuario;
	}

	public String logout() {
		this.usuario = null;
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		final HttpServletRequest request = (HttpServletRequest)ec.getRequest();
		request.getSession(true).invalidate();
		
		return "Login?faces-redirect=true";
	}

	public boolean isLogado() {
		return this.usuario != null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public boolean isMediador() {
		return this.getUsuario().getTipoUsuario().equals(TipoUsuario.MEDIADOR);
	}

	public boolean isAdministrador() {
		return this.getUsuario().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR);
	}

	public boolean isParte() {
		return this.getUsuario().getTipoUsuario().equals(TipoUsuario.PARTE);
	}
	
	
	
	

	public long getQtdMensagens() {
		return qtdMensagens;
	}

	public void add(long qtdMensagens) {
		this.qtdMensagens = qtdMensagens;
	}
	


}
