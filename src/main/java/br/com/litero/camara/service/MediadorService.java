package br.com.litero.camara.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.litero.camara.model.Mediador;
import br.com.litero.camara.repositorios.MediadorRepository;

@ApplicationScoped
public class MediadorService {
	
	@Inject
	private MediadorRepository mediadorRepository;
	
	public List<Mediador> recuperarTodos(){
		return mediadorRepository.findAll();
	}
	
	@Transactional
	public Mediador adicionarOuAtualizar(Mediador mediador) {
			mediadorRepository.save(mediador);
		return mediador;
	}
	
	public Mediador recuperar(Long mediadorId) {
		return mediadorRepository.findBy(mediadorId);
	}
	
	
	public Mediador buscarPorCpf(String cpf) {
		return mediadorRepository.buscarPorCpf(cpf);
	}
	
	
	public List<Mediador> buscarMediadoresPorCaso(Long casoId){
		return mediadorRepository.buscarMediadoresPorCaso(casoId);
	}
	
	
	@Transactional
	public void remover(Mediador mediador) {
		mediadorRepository.remove(mediadorRepository.save(mediador));
	}
	

}
