package br.com.litero.camara.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.litero.camara.model.Evento;
import br.com.litero.camara.repositorios.EventoRepository;

@ApplicationScoped
public class EventoService {
	
	
	@Inject
	private EventoRepository eventoRepository;
	
	public Evento adiciona(Evento evento) {
		return eventoRepository.save(evento);
	}
	
	public List<Evento> recuperarEventosPorCaso(Long casoId){
		return eventoRepository.recuperarEventosPorCaso(casoId);
	}
	
	

}
