package br.com.litero.camara.managedbeans;

import java.io.Serializable;
import java.util.Optional;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Parte;
import br.com.litero.camara.persistence.Transactional;
import br.com.litero.camara.security.UsuarioWeb;
import br.com.litero.camara.service.CasoService;
import br.com.litero.camara.util.jsf.FacesMessages;

@Named
@ViewScoped
public class ConfirmacaoMediadorMB implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioWeb usuarioWeb;
	
	private Caso casoAtual;
	
	@Inject
	private CasoMB casoMB;
	
	
	@Inject
	private CasoService casoService;
	
	
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
		
		
	casoService.aprovarMediador(casoAtual, usuarioWeb.getUsuario());
		
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
		
		
	}
	
	
	@Transactional
	public String negar() {
	
		casoService.reprovarMediador(casoAtual,usuarioWeb.getUsuario());
		
		return "Caso?faces-redirect=true&casoId="+casoAtual.getCasoId();
	}
	
	
	public boolean isExibe() {
		return casoAtual.isAguardandoValidacaoMediador() && isRequeridoPricipal();
	}
	
	
	
	
	public boolean isRequeridoPricipal() {
		return parte.isPrincipal() && parte.getPessoa().isRequerido(casoAtual)
				;
	}


	public Caso getCasoAtual() {
		return casoAtual;
	}


	public Parte getParte() {
		return parte;
	}

}
