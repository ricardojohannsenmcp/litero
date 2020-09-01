package br.com.litero.camara.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.litero.camara.model.Arbitro;
import br.com.litero.camara.repositorios.ArbitroRepository;

@ApplicationScoped
public class ArbitroService {
	
	
	@Inject
	private ArbitroRepository arbitroRepository;
	
	
	public List<Arbitro> recuperarTodos(){
		return arbitroRepository.findAll();
	}
	
	@Transactional
	public Arbitro adicionarOuAtualizar(Arbitro arbitro) {
			arbitroRepository.save(arbitro);
		return arbitro;
	}
	
	public Arbitro recuperar(Long arbitroId) {
		return arbitroRepository.findBy(arbitroId);
	}
	
	public Arbitro buscarPorCpf(String cpf) {
		return arbitroRepository.buscarPorCpf(cpf);
	}
	
	
	@Transactional
	public void remover(Arbitro arbitro) {
		
		arbitroRepository.remove(arbitroRepository.save(arbitro));
	}
	
	
}
