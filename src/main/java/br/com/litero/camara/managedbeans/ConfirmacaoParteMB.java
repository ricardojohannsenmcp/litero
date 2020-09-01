package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.Optional;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.litero.camara.exceptions.NegocioException;
import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Parte;
import br.com.litero.camara.model.Pessoa;
import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.model.StatusParte;
import br.com.litero.camara.persistence.Transactional;
import br.com.litero.camara.repositorios.CasoRepository;
import br.com.litero.camara.repositorios.ParteRepository;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class ConfirmacaoParteMB implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioWeb usuarioWeb;
	
	private Caso casoAtual;
	
	@Inject
	private CasoMB casoMB;
	
	@Inject
	private ParteRepository parteRepository;
	
	@Inject
	private CasoRepository casoRepository;
	
	
	@Inject
	private FacesMessages messages;
	
	
	
	private Parte parte;
	
	
	public void init() {
		
		
		casoAtual = casoMB.getCaso();
		
		Optional<Parte> parteOptional = casoAtual.getPartes().stream().filter(p-> p.getPessoa().equals(usuarioWeb.getUsuario().getPessoa()) ).findAny();
	
	  if(parteOptional.isPresent()) {
		  
		  parte = parteOptional.get();
		  
	  }
	
	}
	
	
	@Transactional
	public String  aceitar() {
		
		
		parte.setStatusParte(StatusParte.ACEITOU_PARTICIPAR_CASO);
		parteRepository.save(parte);
		
		if(parte.isPrincipal()) {
			
			casoAtual.setStatus(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR);
			casoRepository.save(casoAtual);
		}
		messages.info("Participação no caso aceita");
		
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
		
		
	}
	
	
	@Transactional
	public String negar() {
		
		
		parte.setStatusParte(StatusParte.NEGOU_PARTICIPAR_CASO);
		parteRepository.save(parte);
		messages.info("Participação no caso negada");
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
	}
	
	
	
	public boolean isParticipaCaso() {
		
		return parte.getStatusParte().equals(StatusParte.ACEITOU_PARTICIPAR_CASO);
	}
	
	
	
	
	
	public boolean isExibirConvite() {
		try {
			return casoAtual.isProcessoComunicacao() && parte.getPessoa().isRequerido(casoAtual) && parte.isAguardandoConfirmacao();
		} catch (Exception e) {
			return false;
		}
	}
	
	


	public Caso getCasoAtual() {
		return casoAtual;
	}


	public Parte getParte() {
		return parte;
	}
	
	
	public boolean isAguardandoConfirmacao() {
		return this.parte.isAguardandoConfirmacao();
	}
	
	
	
	
	
	
	

}
