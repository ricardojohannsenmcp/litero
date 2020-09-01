package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.ConviteMediador;
import br.com.litero.camara.model.Mediador;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.repositorios.ConviteMediadorRepository;
import br.com.litero.camara.repositorios.MediadorRepository;
import br.com.litero.camara.repositorios.PessoaRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.CasoService;
import br.com.litero.camara.service.MediadorService;
import br.com.litero.camara.service.PessoaService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ConversationScoped
public class SelecaoMediadorMB implements Serializable{


	private static final long serialVersionUID = 1L;
	
	
	
	@Inject
	private Conversation conversation;
	
	
	
	
	private List<Mediador> mediadorList;
	
	@Inject
	private UsuarioWeb usuarioWeb;
	@Inject
	private FacesMessages messages;
	
	
	@Inject
	private MediadorService mediadorService;
	
	@Inject
	private CasoService casoService;
	
	private Caso casoAtual;

	
	private String desejaEscolherMediador = "S";
	
	
	private Mediador mediadorSelecionado;
	
	@Inject
	private ConviteMediadorRepository conviteMediadorRepository;
	
	
	@Inject
	private PessoaService pessoaService;
	
	
	private Pessoa convidado =  null;
	
	
	private String filtro;
	
	
	
	
	@Inject
private MediadorRepository mediadorRepository;
	
	
	
	
	public String init() {
		
		if(conversation.isTransient()) {
			
			conversation.begin();
	
		mediadorList =  mediadorService.recuperarTodos();
	
		
		}	
		
		return "InicioSelecaoMediador?faces-redirect=true";
		
	}
	
	
	public String voltarCaso() {
		
	
		
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
	}
	
	
	public String irParaSelecaoMediador() {
		
		if(desejaEscolherMediador.equalsIgnoreCase("S")) {
			
			
			return "SelecionarMediador?faces-redirect=true";
			
			
		}
		casoService.delegarEscolhaMediador(casoAtual, usuarioWeb.getUsuario());
		
		conversation.end();
		
		messages.info("A Câmara vai definir um mediador para o caso. Sujeito a aprovação da parte contrária.");
		
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
	}
	
	
	
	public String novoConvidado() {
		
		convidado =  new Pessoa();
		
		return "PreCadastroMediador?faces-redirect=true";
	}
	
	
	public String voltarSelecaoMediador() {
		convidado =  new Pessoa();
		
		return "SelecionarMediador?faces-redirect=true";
	}
	
	
	
	
	public String salvarPessoa() {

		this.convidado = pessoaService.adicionar(convidado);
		
		
		return "SelecaoMediadorConvidado?faces-redirect=true";
		
	}
	
	
	
	
	public String enviarConviteAoMediador() {
		
		
		Mediador mediador =  mediadorRepository.buscarMediadorPorPessoa(convidado.getPessoaId());
		
		if(mediador != null) {
			
			mediadorSelecionado =  mediador;
		}else {
			
			
			mediadorSelecionado = new Mediador();
			mediadorSelecionado.setPessoa(convidado);
			mediadorSelecionado.setConvidado(true);
			mediadorSelecionado.setBloqueado(true);
			
			mediadorRepository.save(mediadorSelecionado);
			
			
		}
		
		casoService.enviarConviteMediadorConvidado(casoAtual,mediadorSelecionado,usuarioWeb.getUsuario());
		
		
		conversation.end();
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
	}
	
	
	
	
	public String selecionarMediador(Mediador mediador){
		
		mediadorSelecionado =  mediador;
		return "ConfirmarMediador?faces-redirect=true";
		
	}
	
	
	public String confirmarEscolhaMediador() {
		
		
		
		
		
		
		
		casoService.definirMediador(casoAtual, mediadorSelecionado, usuarioWeb.getUsuario());
		
		
		
		
		
		
		
		conversation.end();
		messages.info("você definiu um mediador para o caso #"+casoAtual.getCasoId()+". Aguarde o aceite da parte contrária ");
		
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
		
	}
	
	
	
	
	public List<Pessoa> buscarPartes(String filtro){
		return pessoaService.buscarPessoas(filtro);
	}

	

	
	


	public void setConvidado(Pessoa convidado) {
		this.convidado = convidado;
	}


	public Caso getCasoAtual() {
		return casoAtual;
	}


	public void setCasoAtual(Caso casoAtual) {
		this.casoAtual = casoAtual;
	}


	public String getDesejaEscolherMediador() {
		return desejaEscolherMediador;
	}


	public void setDesejaEscolherMediador(String desejaEscolherMediador) {
		this.desejaEscolherMediador = desejaEscolherMediador;
	}





	public List<Mediador> getMediadorList() {
		return mediadorList;
	}



	public Mediador getMediadorSelecionado() {
		return mediadorSelecionado;
	}


	public void setMediadorSelecionado(Mediador mediadorSelecionado) {
		this.mediadorSelecionado = mediadorSelecionado;
	}


	public String getFiltro() {
		return filtro;
	}


	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}


	public Pessoa getConvidado() {
		return convidado;
	}


	
	

}
