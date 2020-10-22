package br.com.litero.camara.managedbeans;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.exceptions.NegocioException;
import br.com.litero.camara.model.Endereco;
import br.com.litero.camara.model.EstadoCivil;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.TipoPessoa;
import br.com.litero.camara.model.Usuario;
import br.com.litero.camara.model.UsuarioForm;
import br.com.litero.camara.repositorios.UsuarioRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.AutenticationService;
import br.com.litero.camara.service.CepService;
import br.com.litero.camara.service.PessoaService;
import br.com.litero.camara.util.jsf.FacesMessages;



@Named
@ViewScoped
public class CadastroMB implements Serializable{


	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaService pessoaService;
	
	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Inject
	private AutenticationService authService;
	@Inject
	private CepService cepService;
	@Inject
	private UsuarioWeb usuarioWeb;
	private Pessoa parte;
	private UsuarioForm usuarioForm;


	@Inject
	private FacesMessages  messages;


	public CadastroMB(){
		this.parte = new Pessoa();
		this.usuarioForm = new UsuarioForm();
	}


	public void buscarCep() {
		Endereco endereco = cepService.buscarCep(parte.getCep());
		parte.completarEndereco(endereco);	
	}


	public String salvar() {
		
		if(usuarioRepository.buscarPorLogin(usuarioForm.getLogin())!=null) {
			messages.error("O Login digitado já existe.");
		}
		else {
			parte = pessoaService.adicionar(parte,usuarioForm);
			
			
			
			Usuario usuario = authService.autenticar(usuarioForm.getLogin(), usuarioForm.getSenha());
			if (usuario != null) {
				usuarioWeb.loga(usuario);
				return "/Casos?faces-redirect=true"; 
			}
			
		}
		
		return null;
	}



	public EstadoCivil[] getEstadosCivis() {
		return EstadoCivil.values();
	}


	public TipoPessoa[] getTiposPessoa() {
		return TipoPessoa.values();
	}



	public Pessoa getParte() {
		return parte;
	}




	public UsuarioForm getUsuarioForm() {
		return usuarioForm;
	}






}
