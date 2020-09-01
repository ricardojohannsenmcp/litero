package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.primefaces.context.RequestContext;

import br.com.litero.camara.exceptions.NegocioException;
import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Endereco;
import br.com.litero.camara.model.Mediador;
import br.com.litero.camara.model.Parte;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.model.TipoParte;
import br.com.litero.camara.repositorios.CasoRepository;
import br.com.litero.camara.repositorios.MediadorRepository;
import br.com.litero.camara.repositorios.ParteRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.CasoService;
import br.com.litero.camara.service.CepService;
import br.com.litero.camara.service.PessoaService;
import br.com.litero.camara.util.jsf.FacesMessages;

@ViewScoped
@Named
public class EditarCasoMB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CasoService casoService;
	@Inject
	private PessoaService pessoaService;
	@Inject
	private FacesMessages messages;
	@Inject
	private UsuarioWeb usuarioWeb;
	@Inject
	private CepService cepService;
	private Long casoId;
	private Caso caso;
	private Pessoa pessoaSelecionada =  null;
	
	@Inject
	private MediadorRepository mediadorRepository;
	
	
	@Inject
	private CasoRepository casoRepository;
	
	@Inject
	private ParteRepository parteRepository;
	
	
	
	private List<Mediador> mediadorList = new ArrayList<>();
	
	
	
	private static final String VIEW_CASO= "view_caso"; 
	private static final String VIEW_PARTE= "view_parte"; 
	
	
	private String currentView = VIEW_CASO;
	
	
	private TipoParte tipoParteSelecionada = null;
	
	
	private int activeIndex = 0;

	public void exibir() {
		
		if(casoId != null) {
		this.caso = casoService.recuperar(casoId);
		}else {
			this.caso = new Caso();
		}
		
		mediadorList = mediadorRepository.findAll();
	}
	
	@Transactional
	public String salvarCaso() {
		try {
			
			if(caso.getRequerentes().isEmpty()) {
				
				throw new NegocioException("O caso deve possuir no mínimo um requerente");
			}
			
			if(caso.getRequeridos().isEmpty()) {
				
				throw new NegocioException("O caso deve possuir no mínimo um requerido");

			}
			
			
			
			if(caso.getCasoId() != null) {
				
				
				caso.setPartes(caso.getPartes());
				
				casoRepository.save(caso);
				
				
				
				
				
				
			}else {
				
				casoService.cadastrar(caso,usuarioWeb.getUsuario());
			}
			
			
			
			
			
			return "Casos?faces-redirect=true;";
		} catch (Exception e) {
			e.printStackTrace();
			messages.error(e.getMessage());
		}
		return null;
	}
	

	
	public List<Pessoa> buscarPartes(String filtro){
		return pessoaService.buscarPessoas(filtro);
	}

	public void removerParte(Parte casoParte) {

		try {
			if(casoParte.getTipoParte().equals(TipoParte.REQUERENTE)) {
				
				if(casoParte.isPrincipal()) {
					
					throw new NegocioException("Não pode remover o autor do caso");
				}
				
			this.caso.removerParte(casoParte);
			RequestContext.getCurrentInstance().update("dt_requerentes");
			}else {
				messages.error("não é requerente no caso");
			}
		} catch (Exception e) {
			messages.error(e.getMessage());
		}
	}
	
	public void removerParteContraria(Parte casoParte) {
		
		try {
			if(casoParte.getTipoParte().equals(TipoParte.REQUERIDO)) {
				this.caso.removerParte(casoParte);	
	if(casoParte.isPrincipal()) {
					
					throw new NegocioException("Não pode remover o requerido principal");
				}
				RequestContext.getCurrentInstance().update("dt_requeridos");
				}else {
					messages.error("não é requerido no caso");
				}
		} catch (Exception e) {
			messages.error(e.getMessage());
		}	
	}
	
	public void adicionarParte() {
		try {
			
			
			if(caso.getMediador() != null && caso.isMediadorCaso(pessoaSelecionada)) {
				
				throw new NegocioException("A pessoa selecionada já foi definida como mediador do caso");
			}
			
			
			if(caso.isPessoaParteCaso(pessoaSelecionada)){
				
				throw new NegocioException("A pessoa selecionada já faz parte do caso");
			}
			
			Parte parte = new Parte(caso, pessoaSelecionada, TipoParte.REQUERENTE);
			
			
			if(caso.getRequerentes().isEmpty() && parte.getTipoParte().equals(TipoParte.REQUERENTE)) {
				
				parte.setPrincipal(true);
			}
			
        if(caso.getRequeridos().isEmpty() && parte.getTipoParte().equals(TipoParte.REQUERIDO)) {
				
				parte.setPrincipal(true);
			}
			
			
			
			this.caso.adicionarParte(parte);
			pessoaSelecionada = null;
		} catch (Exception e) {
			messages.error(e.getMessage());
			 FacesContext.getCurrentInstance().renderResponse();
		}
	}
	
	public void adicionarParteContraria() {
		try {
			
			Parte casoParte = new Parte(caso, pessoaSelecionada, TipoParte.REQUERIDO);
			this.caso.adicionarParte(casoParte);
			pessoaSelecionada = null;

		} catch (Exception e) {
			 FacesContext.getCurrentInstance().renderResponse();
			messages.error(e.getMessage());
		}
	}
	
	
	public void alterarRequerentePrincipal(Parte parte) {
		
		
		
		
		for(Parte requerente : caso.getRequerentes()) {
			requerente.setPrincipal(false);
			if(requerente.getPessoa().equals(parte.getPessoa())) {
				requerente.setPrincipal(true);
			}
		}
	}
	
	
	
	
	
	public void alterarRequeridoPrincipal(Parte parte) {
		
		
		
		
		for(Parte requerido : caso.getRequeridos()) {
			requerido.setPrincipal(false);
			if(requerido.getPessoa().equals(parte.getPessoa())) {
				requerido.setPrincipal(true);
			}
		}
	}
	
	
	
	
	
	public void salvarPessoa() {
		
		try {
			pessoaService.adicionar(pessoaSelecionada);
			
			Parte parte = new Parte(caso, pessoaSelecionada, tipoParteSelecionada);
			
if(caso.getRequerentes().isEmpty() && parte.getTipoParte().equals(TipoParte.REQUERENTE)) {
				
				parte.setPrincipal(true);
			}
			
        if(caso.getRequeridos().isEmpty() && parte.getTipoParte().equals(TipoParte.REQUERIDO)) {
				
				parte.setPrincipal(true);
			}
			
			
			
			
			this.caso.adicionarParte(parte);
			pessoaSelecionada = null;
			
   tipoParteSelecionada = null;
			
			
			currentView =  VIEW_CASO;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	public void voltarViewCaso() {
		
		
		pessoaSelecionada = null;
		
	    tipoParteSelecionada = null;
			
			
			currentView =  VIEW_CASO;
	}
	
	
	
	public boolean isViewCaso() {
		
		return currentView.equals(VIEW_CASO);
	}
	
	public boolean isViewParte() {
		
		return currentView.equals(VIEW_PARTE);
	}
	
	
	
	
	public boolean isPermiteSelecionarMediador() {
		
		try {
			return caso.getStatus().equals(StatusCaso.IMPASSE) || caso.getStatus().equals(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR_CAMARA);
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	public String novaParte() {
		this.pessoaSelecionada = new Pessoa();
		return "GerenciarParte?faces-redirect=true";
	}
	
	public void buscarCep() {
		Endereco endereco = cepService.buscarCep(pessoaSelecionada.getCep());
		pessoaSelecionada.completarEndereco(endereco);	
	}
	
	public void novaParteRequerente() {
		this.pessoaSelecionada = new Pessoa();
		tipoParteSelecionada =  TipoParte.REQUERENTE;
		currentView = VIEW_PARTE;
	}
	
	public void novaParteRequerido() {
		
		tipoParteSelecionada =  TipoParte.REQUERIDO;
		
		this.pessoaSelecionada = new Pessoa();
		currentView = VIEW_PARTE;
	}
	
	public String voltar() {
		return "EditarCaso?faces-redirect=true";
	}
	
	public Long getCasoId() {
		return casoId;
	}
	
	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa parteSelecionada) {
		this.pessoaSelecionada = parteSelecionada;
	}

	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}

	public Caso getCaso() {
		return caso;
	}

	public void setCaso(Caso caso) {
		this.caso = caso;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public String getCurrentView() {
		return currentView;
	}

	public List<Mediador> getMediadorList() {
		return mediadorList;
	}
	
	@PreDestroy
	public void destroy() {
		
		System.out.println("perdeu referencia");
	}
	
	

}
