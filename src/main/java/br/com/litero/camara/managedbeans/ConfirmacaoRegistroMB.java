package br.com.litero.camara.managedbeans;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.litero.camara.exceptions.NegocioException;
import br.com.litero.camara.model.Convite;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.TipoUsuario;
import br.com.litero.camara.model.Usuario;
import br.com.litero.camara.model.UsuarioForm;
import br.com.litero.camara.repositorios.ConviteRepository;
import br.com.litero.camara.repositorios.PessoaRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.UsuarioService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class ConfirmacaoRegistroMB implements Serializable{
	
	
	private static final long serialVersionUID = 1L;



	private String token;
	
	
	@Inject
	private ConviteRepository conviteRepository;
	
	@Inject
	private PessoaRepository pessoaRepository;
	
	@Inject
	private UsuarioService usuarioService;
	
	
	private Pessoa pessoa;
	
	private UsuarioForm usuarioForm = new UsuarioForm();
	
	
	@Inject
	private FacesMessages messages;
	
	
	@Inject
	private UsuarioWeb usuarioWeb;
	

	
	public void init() {
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		
		if(token ==  null) {
			
			throw new NegocioException("Requisição inválida. Parâmetro nulo");
		}
		
		System.out.println(token);
		
		Convite convite = conviteRepository.recuperar(token);
		
		
		if(convite !=  null) {
			
			if(convite.isExpirado()) {
				
				throw new NegocioException("Token expirou");

			}
			
			pessoa  = pessoaRepository.findBy(convite.getPessoa().getPessoaId());
			this.usuarioForm = new UsuarioForm();

		}else {
			
			throw new NegocioException("Convite não localizado na base de dados");
		}
	}
	
	
	
	public String salvar() {
		
		
		try {
			Usuario usuario = new Usuario();
			usuario.setPessoa(pessoa);
			usuario.setLogin(usuarioForm.getLogin());
			usuario.setSenha(usuarioForm.getSenha());
			usuario.setConfirmaSenha(usuarioForm.getConfirmacaoSenha());
			usuario.setBloqueado(false);
			usuario.setTipoUsuario(TipoUsuario.PARTE);
			
			usuarioService.adiciona(usuario);
			
			usuarioWeb.loga(usuario);
			return "/Casos?faces-redirect=true"; 
			
			
			
		} catch (Exception e) {
			
			
			messages.error(e.getMessage());
			
			return null;
		}
		
	

	}
	
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	public Pessoa getPessoa() {
		return pessoa;
	}



	public UsuarioForm getUsuarioForm() {
		return usuarioForm;
	}



	public void setUsuarioForm(UsuarioForm usuarioForm) {
		this.usuarioForm = usuarioForm;
	}
	
	
	
	
	
	
	
	
	

}
