package br.com.litero.camara.managedbeans;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.litero.camara.model.Caso;
import br.com.litero.camara.model.Parte;
import br.com.litero.camara.repositorios.CasoRepository;

@Named
@RequestScoped
public class ReciboMB {
	
	@Inject
	private CasoRepository casoRepository;
	
	private Caso caso;
	
	private Long casoId;
	
	private List<Parte> requerentes;
	
	private List<Parte> requeridos;
	
	
	public void inicializar() {
		
		
		this.caso = casoRepository.recuperarCaso(casoId);
		
	}


	public Caso getCaso() {
		return caso;
	}


	public Long getCasoId() {
		return casoId;
	}


	public List<Parte> getRequerentes() {
		return requerentes;
	}


	public List<Parte> getRequeridos() {
		return requeridos;
	}
	
	
	
	

}
