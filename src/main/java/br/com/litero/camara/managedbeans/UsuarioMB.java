package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.TipoUsuario;
import br.com.litero.camara.model.Usuario;
import br.com.litero.camara.service.PessoaService;
import br.com.litero.camara.service.UsuarioService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class UsuarioMB implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private PessoaService pessoaService;
	
	@Inject
	private FacesMessages messages;
	
	
	private List<Usuario> usuarios;
	private Usuario usuario;
	
	private static final String  LISTANDO = "listando";
	private static final String  INCLUINDO = "incluindo";
	
	private String modo = LISTANDO;
	
	
	
	
	
	
	public UsuarioMB() {
		
		
		
	}


	public void listar() {
		
		
		this.usuarios = usuarioService.recuperarTodos();
	}
	
	
	public void novoUsuario() {
		
		this.usuario = new Usuario();
		modo = INCLUINDO;
	}
	
	public void editarUsuario(Long usuarioId) {
		
		this.usuario = usuarioService.recuperar(usuarioId);
		modo = INCLUINDO;
	}
	
	
	public void removerUsuario(Usuario usuario) {
		
		try {
			usuarioService.removerUsuario(usuario);
			listar();

			
			usuario = null;
			
			messages.info("Usuário removido com sucesso");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messages.error("Usuário não pode ser removido");
		}
	}
	
	
	
	public List<Pessoa> buscarPartes(String filtro){
		return pessoaService.buscarPessoas(filtro);
	}
	
	

	public Usuario getUsuario() {
		return usuario;
	}


	public String getModo() {
		return modo;
	}


	
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	
public TipoUsuario[] getTiposUsuario() {
		
		return TipoUsuario.values();
	}




public void salvar() {
	
	
	
	try {
		if(usuario.getUsuarioId() == null) {
			
			usuarioService.adiciona(usuario);
		}else {
			
			usuarioService.editar(usuario);
		}
		
		
		this.usuarios = usuarioService.recuperarTodos();
		
		modo = LISTANDO;
		
		messages.info("Dados salvos com sucesso");
	} catch (Exception e) {
		e.printStackTrace();
		messages.error(e.getMessage());
	}
}



public void cancelar() {
	
	this.usuario = null;

	modo = LISTANDO;
}


	public boolean isListando() {
		
		return this.modo.equals(LISTANDO);
	}
	
	public boolean isIncluindo() {
		
		return this.modo.equals(INCLUINDO);
	}

	
	
	

}
