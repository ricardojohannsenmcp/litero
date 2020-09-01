package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Mediador;
import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.repositorios.MediadorRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.CasoService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class EscolhaMediadorCamaraMB implements  Serializable{

	private static final long serialVersionUID = 1L;
	
	

	@Inject
	private CasoMB casoMB;
	
	@Inject
	private MediadorRepository mediadorRepository;
	
	private Caso casoAtual;
	
	@Inject
	private CasoService casoService;
	
	@Inject
	private UsuarioWeb usuarioWeb;
	
	

	private List<Mediador> mediadores;
	
	private Mediador mediador;
	@Inject
	private FacesMessages messages;

	
	
	
	@PostConstruct
	public void init() {
		
		
		casoAtual =  casoMB.getCaso();
		mediadores = mediadorRepository.findAll();
		
	}
	
	
	public String salvar() {
		
		if(mediador != null) {
		
		casoService.forcarDefinicaoMediador(casoAtual, mediador, usuarioWeb.getUsuario());
		
		
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
		
		}else {
			
			messages.error("selecione um mediador");
		}
		
		
		return null;
		
	}
	
	
	public boolean isExibe() {
		
		return this.usuarioWeb.isAdministrador() && (this.casoAtual.getStatus().equals(StatusCaso.IMPASSE) ||  this.casoAtual.getStatus().equals(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR_CAMARA)  );
	}
	
	
	
	

	public List<Mediador> getMediadores() {
		
		
		return mediadores;
	}


	public Caso getCasoAtual() {
		return casoAtual;
	}


	public Mediador getMediador() {
		return mediador;
	}


	public void setMediador(Mediador mediador) {
		this.mediador = mediador;
	}
	
	
	
	
	

}
