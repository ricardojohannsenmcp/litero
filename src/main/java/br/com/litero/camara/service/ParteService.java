package br.com.litero.camara.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.litero.camara.model.TipoParte;
import br.com.litero.camara.repositorios.ParteRepository;

@ApplicationScoped
public class ParteService {
	
	@Inject
	private ParteRepository parteRepository;
	
	
	
	/*public List<Parte> buscarRequerentesCaso(Long casoId){
		
		return parteDao.buscarPartesCaso(casoId, TipoParte.REQUERENTE);
	}
	
	public List<Parte> buscarRequeridosCaso(Long casoId){
		
		return parteDao.buscarPartesCaso(casoId, TipoParte.REQUERIDO);
	}
	
	*/
	
	
	
	

	
	public long recuperarQuantidadePorTipoParte(Enum<TipoParte> tipoParte) {
		
		return parteRepository.recuperarQuantidadePorTipoParte(tipoParte);
	}

}
